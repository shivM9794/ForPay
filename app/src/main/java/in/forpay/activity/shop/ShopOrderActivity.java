package in.forpay.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.forpay.adapter.shop.ShopOrderAdapter;
import in.forpay.databinding.ActivityShopOrderBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ShopOrderModel;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ShopOrderActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityShopOrderBinding binding;
    private AppCompatActivity activity;
    private ShopOrderAdapter adapter;
    private ArrayList<Object> arrayList;
    private ArrayList<Object> arrayListItems;
    private ProgressHelper progressHelper;
    private String shopId,contactNumber,rating,orderId,lastActive,image,shopName,deliveryCharge,totalCost,shopOrderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShopOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);

        shopId=getIntent().getStringExtra(Constant.SHOP_ID);
        shopOrderModel=getIntent().getStringExtra(Constant.SHOP_ORDER_MODEL);
        init();
    }

    private void init() {
        progressHelper.show();
        setShopOrderAdapter();
        parseShopOrderResponse(shopOrderModel);
        binding.backPress.setOnClickListener(v->onBackPressed());
        /*binding.chat.setOnClickListener(v->startActivity(new Intent(activity,ShopListChatActivity.class)
                    .putExtra(Constant.SHOP_ID,shopId)
                    .putExtra(Constant.SHOP_NAME,shopName)
                    .putExtra(Constant.ORDER_ID,orderId)
                    .putExtra(Constant.RATING,rating)
                    .putExtra(Constant.LAST_ACTIVE,lastActive)
                    .putExtra(Constant.SHOP_IMAGE,image)
                    .putExtra(Constant.CONTACT_NUMBER,contactNumber)));*/

        binding.add.setOnClickListener(v->{

            ArrayList<ShopOrderModel.DataBean.OrderHistoryBean.ItemsBean> items;

            if ((((ShopOrderModel.DataBean) arrayList.get(0)).getOrderHistory()!=null)){
                items = new ArrayList<>((((ShopOrderModel.DataBean) arrayList.get(0)).getOrderHistory().get(0).getItems()));
            }else {
                items=new ArrayList<>();
            }
            startActivity(new Intent(activity,DetailsOrderActivity.class)
                    .putExtra(Constant.ITEMS_LIST,items)
                    .putExtra(Constant.SHOP_IMAGE,image)
                    .putExtra(Constant.DELIVERY_CHARGE,deliveryCharge)
                    .putExtra(Constant.TOTAL_COST,totalCost));
        });

        binding.call.setOnClickListener(v->Utility.call(activity,contactNumber));
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
                ShopOrderModel.DataBean.OrderHistoryBean orderHistory=((ShopOrderModel.DataBean) arrayList.get(0)).getOrderHistory().get(0);
                ShopOrderModel.DataBean.OrderHistoryBean.ItemsBean items= (((ShopOrderModel.DataBean) arrayList.get(0)).getOrderHistory().get(0).getItems().get(position));

                Intent intent=new Intent(activity, SoldOrderDetailActivity.class);
                intent.putExtra(Constant.ORDER_ID,orderHistory.getOrderId());
                intent.putExtra(Constant.POSITION,position);
                startActivity(intent);
                break;
            }
            case Constant.RATING:{
                ShopOrderModel.DataBean model=(ShopOrderModel.DataBean) arrayList.get(0);
                ShopOrderModel.DataBean.OrderHistoryBean orderHistory=((ShopOrderModel.DataBean) arrayList.get(0)).getOrderHistory().get(0);

                startActivity(new Intent(activity,RatingOrderActivity.class)
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

                    orderId=model.getData().getOrderHistory().get(0).getOrderId();
                    rating=model.getData().getOrderHistory().get(0).getRating();
                    deliveryCharge=model.getData().getOrderHistory().get(0).getDeliveryCharge();
                    totalCost=model.getData().getOrderHistory().get(0).getTotalCost();
                }

                adapter.notifyDataSetChanged();

                binding.lastActive.setText(model.getData().getLastActive());
                contactNumber=model.getData().getContactNumber();
                lastActive=model.getData().getLastActive();
                image=model.getData().getImgUrl();
                shopName=model.getData().getShopName();

                if (!shopName.isEmpty()){
                    binding.shopName.setText(shopName);
                    binding.shopName.setVisibility(View.VISIBLE);
                }
                if (!lastActive.isEmpty()){
                    binding.lastActive.setText(lastActive);
                    binding.lastActive.setVisibility(View.VISIBLE);
                }

                Utility.imageLoader(activity,image,binding.image);

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