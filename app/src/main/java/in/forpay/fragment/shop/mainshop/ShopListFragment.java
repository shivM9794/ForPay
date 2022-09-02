package in.forpay.fragment.shop.mainshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.activity.shop.LocationShopActivity;
import in.forpay.activity.shop.OrderDataActivity;
import in.forpay.activity.shop.ShopReviewActivity;
import in.forpay.adapter.shop.ShopAdapter;
import in.forpay.databinding.FragmentShopListBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ShopModel;
import in.forpay.model.shop.ShopOrderModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class ShopListFragment extends Fragment implements ItemClickListener {

    private FragmentShopListBinding binding;
    private Activity activity;
    private ArrayList<Object> arrayList=new ArrayList<>();
    private ShopAdapter adapter;
    private String shopId;
    private String shopOrderModel;
    private String destinationLatitude,destinationLongitude;
    private ProgressHelper progressHelper;

    public ShopListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_shop_list, container, false);
        activity=getActivity();
        progressHelper=new ProgressHelper(activity);

        init();

        return binding.getRoot();
    }

    private void init() {
        setShopListAdapter();
    }

    public void setData(){

    }

    public void filterData(ArrayList<Object> arrayList){
        this.arrayList=arrayList;
        adapter.filter(this.arrayList);
        adapter.notifyDataSetChanged();

        if (arrayList.size()==0)
            Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Shop Data Found");
        else {
            binding.inflateLayout.removeAllViews();
            binding.inflateLayout.addView(binding.recyclerView);
        }
    }

    /*public void filter(ArrayList<Object> arrayList){
        this.arrayList=arrayList;
        adapter.filter(this.arrayList);
    }*/

    public void noDataFound(){
        Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Shop Data Found");
    }

    public void close(ArrayList<Object> arrayList){
        this.arrayList=arrayList;
        adapter.filter(this.arrayList);
        binding.inflateLayout.removeAllViews();
        binding.inflateLayout.addView(binding.recyclerView);
        adapter.notifyDataSetChanged();
    }

    private void setShopListAdapter() {
        arrayList=new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new ShopAdapter(activity,arrayList,this);
        binding.recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position, String tag) {
        switch (tag) {
            case Constant.SHOP_LIST: {
                ShopModel.DataBean model = (ShopModel.DataBean) arrayList.get(position);
                if (model.getProducts().size() > 0 && !model.getProducts().get(0).getProductId().isEmpty()) {
                    shopId = model.getShopId();
                    getShopOrder(tag);
                }
                break;
            }
            case Constant.SHOP_RATING: {
                ShopModel.DataBean model = (ShopModel.DataBean) arrayList.get(position);
                shopId = model.getShopId();
                getShopOrder(tag);
                break;
            }
            case Constant.SHOP_DISTANCE: {
                ShopModel.DataBean model = (ShopModel.DataBean) arrayList.get(position);
                shopId = model.getShopId();
                destinationLatitude = model.getLatitude();
                destinationLongitude = model.getLongitude();
                getShopOrder(tag);
            }
            default: {
                break;
            }
        }
    }

    private void getShopOrder(String tag) {
        if (Utility.isNetworkConnected(activity)) {

            ArrayList<String> list = new ArrayList<>();
            list.add("token=" + Utility.getToken(activity));
            list.add("imei=" + Utility.getImei(activity));
            list.add("appVersion=" + Utility.getVersionCode(activity));
            list.add("shopId=" +shopId);
            String request = Utility.dataWrapper(list);
            shopOrderRequest(request,tag);

        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void shopOrderRequest(String request,String tag) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_SHOP_ORDER, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseShopOrderResponse(result, responseManager,tag);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void parseShopOrderResponse(String response, ResponseManager responseManager,String tag) {
        progressHelper.dismiss();
        try {
            Log.d("ShopOrderResponse","response "+response);

            ShopOrderModel model= new Gson().fromJson(response, ShopOrderModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                shopOrderModel=response;

                switch (tag) {
                    case Constant.SHOP_LIST:{
                           /* startActivity(new Intent(activity,ShopListChatActivity.class).putExtra(Constant.SHOP_ID,shopId)
                            .putExtra(Constant.SHOP_ORDER_MODEL,shopOrderModel));*/
                        startActivity(new Intent(activity, OrderDataActivity.class).putExtra(Constant.SHOP_ID,shopId)
                                .putExtra(Constant.SHOP_ORDER_MODEL,shopOrderModel));
                        break;
                    }
                    case Constant.SHOP_RATING: {
                        String orderId ;
                        if (model.getData().getOrderHistory()==null)
                            orderId="";
                        else
                            orderId=model.getData().getOrderHistory().get(0).getOrderId();
                        startActivity(new Intent(activity, ShopReviewActivity.class)
                                .putExtra(Constant.SHOP_ID,shopId)
                                .putExtra(Constant.ORDER_ID,orderId));
                        break;
                    }
                    case Constant.SHOP_DISTANCE: {
                        startActivity(new Intent(activity, LocationShopActivity.class)
                                .putExtra(Constant.DESTINATION_LATITUDE,destinationLatitude)
                                .putExtra(Constant.DESTINATION_LONGITUDE,destinationLongitude)
                                .putExtra(Constant.SHOP_NAME,model.getData().getShopName()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}