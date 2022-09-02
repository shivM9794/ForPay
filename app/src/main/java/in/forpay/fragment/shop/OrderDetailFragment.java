package in.forpay.fragment.shop;

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
import in.forpay.activity.shop.DetailsOrderActivity;
import in.forpay.activity.shop.RatingOrderActivity;
import in.forpay.activity.shop.SoldOrderDetailActivity;
import in.forpay.adapter.shop.ShopOrderAdapter;
import in.forpay.databinding.FragmentOrderDetailBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ShopOrderModel;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class OrderDetailFragment extends Fragment implements ItemClickListener {

    private FragmentOrderDetailBinding binding;
    private Activity activity;
    private ShopOrderAdapter adapter;
    private ArrayList<Object> arrayList;
    private ArrayList<Object> arrayListItems;
    private ProgressHelper progressHelper;
    private String shopId,shopOrderModel;

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_order_detail, container, false);
        activity=getActivity();
        progressHelper=new ProgressHelper(activity);

        final Bundle args = getArguments();
        assert args != null;
        shopId=args.getString(Constant.SHOP_ID);
        shopOrderModel=args.getString(Constant.SHOP_ORDER_MODEL);

        init();

        return binding.getRoot();
    }

    private void init() {
        progressHelper.show();
        setShopOrderAdapter();
        parseShopOrderResponse(shopOrderModel);
    }

    private void setShopOrderAdapter() {
        arrayList=new ArrayList<>();
        arrayListItems=new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new ShopOrderAdapter(activity,arrayList,arrayListItems,this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, String tag) {
        switch (tag){
            case Constant.VIEW_ORDER:{
                ShopOrderModel.DataBean model=(ShopOrderModel.DataBean) arrayList.get(0);
                ShopOrderModel.DataBean.OrderHistoryBean orderHistory=((ShopOrderModel.DataBean) arrayList.get(0)).getOrderHistory().get(position);
                //ShopOrderModel.DataBean.OrderHistoryBean.ItemsBean items= (((ShopOrderModel.DataBean) arrayList.get(0)).getOrderHistory().get(0).getItems().get(position));

                Intent intent=new Intent(activity, SoldOrderDetailActivity.class);
                intent.putExtra(Constant.ORDER_ID,orderHistory.getOrderId());
                intent.putExtra(Constant.POSITION,position);
                startActivity(intent);
                break;
            }
            case Constant.RATING:{
                ShopOrderModel.DataBean model=(ShopOrderModel.DataBean) arrayList.get(0);
                ShopOrderModel.DataBean.OrderHistoryBean orderHistory=((ShopOrderModel.DataBean) arrayList.get(0)).getOrderHistory().get(0);

                startActivity(new Intent(activity, RatingOrderActivity.class)
                        .putExtra(Constant.SHOP_ID,shopId)
                        .putExtra(Constant.ORDER_ID,orderHistory.getOrderId())
                        .putExtra(Constant.SHOP_IMAGE,model.getImgUrl()));
                break;
            }
        }
    }

    private void parseShopOrderResponse(String response) {
        progressHelper.dismiss();
        try {
            Log.d("ShopOrderResponse","response "+response);

            ShopOrderModel model= new Gson().fromJson(response, ShopOrderModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                arrayList.add(model.getData());

                if (model.getData().getOrderHistory()!=null) {
                    arrayListItems.addAll(model.getData().getOrderHistory());
                }
                adapter.notifyDataSetChanged();

                if (arrayListItems.size()==0){
                    Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Order Data Found");
                }
            }else
                Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Order Data Found");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}