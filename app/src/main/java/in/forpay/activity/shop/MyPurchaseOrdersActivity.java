package in.forpay.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.shop.MyPurchaseOrderAdapter;
import in.forpay.databinding.ActivityMyPurchaseOrdersBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.MyPurchaseOrderModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class MyPurchaseOrdersActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityMyPurchaseOrdersBinding binding;
    private AppCompatActivity activity;
    private MyPurchaseOrderAdapter adapter;
    private ArrayList<Object> arrayList;
    private ArrayList<Object> arrayListItems;
    private ProgressHelper progressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyPurchaseOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);

        init();
    }

    private void init() {
        setMyPurchaseAdapter();
        getMyOrderHistoryList();
        binding.backPress.setOnClickListener(v->onBackPressed());

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ArrayList<Object> arrayListFilter=new ArrayList();
                try{
                    if (editable.toString().isEmpty()) {
                        arrayListFilter = arrayListItems;
                    } else {
                        for (Object items : arrayListItems){
                            MyPurchaseOrderModel.DataBean.OrderHistoryBean.ItemsBean itemsBean=((MyPurchaseOrderModel.DataBean.OrderHistoryBean.ItemsBean)items);
                            if (itemsBean.getItemName().toLowerCase().trim().contains(editable.toString().toLowerCase().trim())){
                                arrayListFilter.add(items);
                            }
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                adapter.filter(arrayListFilter);
            }
        });
    }

    private void setMyPurchaseAdapter() {
        arrayList=new ArrayList<>();
        arrayListItems=new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new MyPurchaseOrderAdapter(activity,arrayList,arrayListItems,this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, String tag) {
        MyPurchaseOrderModel.DataBean model=(MyPurchaseOrderModel.DataBean) arrayList.get(0);
        MyPurchaseOrderModel.DataBean.OrderHistoryBean orderHistory=((MyPurchaseOrderModel.DataBean) arrayList.get(0)).getOrderHistory();

        switch (tag){
            case Constant.VIEW_ORDER:{
                Intent intent=new Intent(activity, SoldOrderDetailActivity.class);
                intent.putExtra(Constant.ORDER_ID,orderHistory.getOrderId());
                intent.putExtra(Constant.POSITION,position);
                startActivity(intent);
                break;
            }
            case Constant.RATING:{

                startActivity(new Intent(activity,RatingOrderActivity.class)
                        .putExtra(Constant.SHOP_ID,model.getShopId())
                        .putExtra(Constant.ORDER_ID,orderHistory.getOrderId())
                        .putExtra(Constant.SHOP_IMAGE,model.getImgUrl())
                        .putExtra(Constant.IS_FROM_PURCHASE,true));
                break;
            }
        }
    }

    private void getMyOrderHistoryList() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code

            String request = Utility.mapWrapper(this, map1);

            myOrderHistoryRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void myOrderHistoryRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_MY_ORDER_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseMyOrderHistoryResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseMyOrderHistoryResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("MyOrderHistoryResponse","response "+response);

            MyPurchaseOrderModel model= new Gson().fromJson(response, MyPurchaseOrderModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                arrayList.addAll(model.getData());
                arrayListItems.addAll(model.getData().get(0).getOrderHistory().getItems());
                adapter.notifyDataSetChanged();

                if (arrayList.size()==0)
                    Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Purchase Data Found");
            }else {
                if (model.getData()==null)
                    Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Purchase Data Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}