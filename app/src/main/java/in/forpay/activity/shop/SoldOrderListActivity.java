package in.forpay.activity.shop;

import android.annotation.SuppressLint;
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
import in.forpay.adapter.shop.SoldOrderListAdapter;
import in.forpay.databinding.ActivitySoldOrderListBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.MySoldOrderHistoryModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class SoldOrderListActivity extends AppCompatActivity implements ItemClickListener {

    private ActivitySoldOrderListBinding binding;
    private AppCompatActivity activity;
    private SoldOrderListAdapter adapter;
    private ArrayList<Object> arrayList;
    private ArrayList<Object> arrayListItems;
    private ProgressHelper progressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySoldOrderListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper =new ProgressHelper(activity);
    }

    private void init() {
        setSoldOrderHistoryAdapter();
        mySoldOrderHistory();
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
                            MySoldOrderHistoryModel.DataBean.OrderHistoryBean.ItemsBean itemsBean=((MySoldOrderHistoryModel.DataBean.OrderHistoryBean.ItemsBean)items);
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

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void setSoldOrderHistoryAdapter() {
        arrayList=new ArrayList<>();
        arrayListItems=new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new SoldOrderListAdapter(activity,arrayList,arrayListItems,this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, String tag) {
        MySoldOrderHistoryModel.DataBean model=(MySoldOrderHistoryModel.DataBean) arrayList.get(0);
        MySoldOrderHistoryModel.DataBean.OrderHistoryBean orderHistory=((MySoldOrderHistoryModel.DataBean) arrayList.get(0)).getOrderHistory();
        MySoldOrderHistoryModel.DataBean.OrderHistoryBean.ItemsBean items= (((MySoldOrderHistoryModel.DataBean) arrayList.get(0)).getOrderHistory().getItems().get(position));
        switch (tag){
            case Constant.VIEW_ORDER:{

                Intent intent=new Intent(activity, SoldOrderDetailActivity.class);
                intent.putExtra(Constant.ORDER_ID,orderHistory.getOrderId());
                intent.putExtra(Constant.POSITION,position);
                intent.putExtra(Constant.IS_FROM_SELLER,true);
                startActivity(intent);
                break;
            }
            case Constant.RATING:{
                startActivity(new Intent(activity,UserReviewActivity.class).putExtra(Constant.ORDER_ID,orderHistory.getOrderId()));
                break;
            }
            case Constant.STATUS:{
                startActivity(new Intent(activity,OrderProcessingActivity.class)
                        .putExtra(Constant.ORDER_ID,orderHistory.getOrderId()));
                break;
            }
        }
    }

    private void mySoldOrderHistory() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code

            String request = Utility.mapWrapper(this, map1);

            mySoldOrderHistoryRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void mySoldOrderHistoryRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_MY_SOLD_ORDER_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseMySoldOrderHistoryResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    @SuppressLint("LongLogTag")
    private void parseMySoldOrderHistoryResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("MySoldOrderHistoryResponse","response "+response);

            MySoldOrderHistoryModel shopCategory= new Gson().fromJson(response, MySoldOrderHistoryModel.class);
            if (shopCategory.getResCode().equals(Constant.CODE_200)) {
                arrayList.addAll(shopCategory.getData());
                arrayListItems.addAll(shopCategory.getData().get(0).getOrderHistory().getItems());
                adapter.notifyDataSetChanged();

                if (arrayListItems.size()==0)
                    Utility.inflateNoDataFoundLayout(activity, binding.inflateLayout,"No Order Data Found");

            }else
                Utility.inflateNoDataFoundLayout(activity, binding.inflateLayout,"No Order Data Found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}