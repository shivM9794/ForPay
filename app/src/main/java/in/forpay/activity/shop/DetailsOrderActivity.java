package in.forpay.activity.shop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import in.forpay.adapter.shop.DetailsOrderAdapter;
import in.forpay.databinding.ActivityDetailsOrderBinding;
import in.forpay.model.shop.ShopOrderModel;
import in.forpay.model.shop.SoldOrderDetailModel;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class DetailsOrderActivity extends AppCompatActivity {

    private ActivityDetailsOrderBinding binding;
    private AppCompatActivity activity;
    private DetailsOrderAdapter adapter;
    private ArrayList<ShopOrderModel.DataBean.OrderHistoryBean.ItemsBean> arrayList;
    private ArrayList<SoldOrderDetailModel.DataBean.OrderHistoryBean.ItemsBean> orderDetailArrayList;
    private String image,deliveryCharge,totalCost;
    private boolean isFromOrderDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailsOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;

        arrayList=getIntent().getParcelableArrayListExtra(Constant.ITEMS_LIST);
        orderDetailArrayList=getIntent().getParcelableArrayListExtra(Constant.ITEMS_LIST);
        image=getIntent().getStringExtra(Constant.SHOP_IMAGE);
        deliveryCharge=getIntent().getStringExtra(Constant.DELIVERY_CHARGE);
        totalCost=getIntent().getStringExtra(Constant.TOTAL_COST);
        isFromOrderDetail=getIntent().getBooleanExtra(Constant.IS_FROM_ORDER_DETAIL,false);

        init();
    }

    private void init() {
        if (isFromOrderDetail){
            if (orderDetailArrayList.size()==0){
                Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Data Found");
            }else
                setOrderDetailAdapter();
        }else {
            if (arrayList.size()==0){
                Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Data Found");
            }else
                setDetailOrderAdapter();
        }

        Utility.imageLoader(activity,image,binding.image);
        //binding.deliveryCharge.setText(deliveryCharge);
        binding.total.setText(totalCost);
        binding.backPress.setOnClickListener(v->onBackPressed());


    }

    private void setDetailOrderAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new DetailsOrderAdapter(activity,arrayList,isFromOrderDetail);
        binding.recyclerView.setAdapter(adapter);
    }

    private void setOrderDetailAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new DetailsOrderAdapter(activity,orderDetailArrayList,isFromOrderDetail);
        binding.recyclerView.setAdapter(adapter);
    }
}