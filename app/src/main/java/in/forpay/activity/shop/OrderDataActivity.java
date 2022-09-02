package in.forpay.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.forpay.adapter.shop.ViewPagerShopNewAdapter;
import in.forpay.databinding.ActivityOrderDataBinding;
import in.forpay.fragment.shop.ChatFragment;
import in.forpay.fragment.shop.OrderDetailFragment;
import in.forpay.model.shop.ShopOrderModel;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class OrderDataActivity extends AppCompatActivity {

    private ActivityOrderDataBinding binding;
    private AppCompatActivity activity;
    private ViewPagerShopNewAdapter adapter;
    private ArrayList<Object> arrayList;
    private ProgressHelper progressHelper;
    private String shopId,contactNumber,shopName,lastActive,image="",deliveryCharge="",totalCost="",shopOrderModel,toUserId;
    private String orderId="",rating="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);

        shopId=getIntent().getStringExtra(Constant.SHOP_ID);
        shopOrderModel=getIntent().getStringExtra(Constant.SHOP_ORDER_MODEL);
        toUserId=getIntent().getStringExtra(Constant.TO_USER_ID);

        init();
    }

    private void init() {
        progressHelper.show();
        parseShopOrderResponse(shopOrderModel);
        setViewPagerData();
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.backPress.setOnClickListener(v->onBackPressed());

        binding.add.setOnClickListener(v->{

            ArrayList<ShopOrderModel.DataBean.OrderHistoryBean.ItemsBean> itemsArrayList;

            if ((((ShopOrderModel.DataBean) arrayList.get(0)).getOrderHistory()!=null)){
                itemsArrayList = new ArrayList<>((((ShopOrderModel.DataBean) arrayList.get(0)).getOrderHistory().get(0).getItems()));
            }else {
                itemsArrayList=new ArrayList<>();
            }
            startActivity(new Intent(activity, DetailsOrderActivity.class)
                    .putExtra(Constant.ITEMS_LIST,itemsArrayList)
                    .putExtra(Constant.SHOP_IMAGE,image)
                    .putExtra(Constant.DELIVERY_CHARGE,deliveryCharge)
                    .putExtra(Constant.TOTAL_COST,totalCost));
        });

        binding.call.setOnClickListener(v-> Utility.call(activity,contactNumber));

        binding.ratingLayout.setOnClickListener(v-> startActivity(new Intent(activity, ShopReviewActivity.class)
                .putExtra(Constant.SHOP_ID,shopId)
                .putExtra(Constant.ORDER_ID,orderId)));

        binding.createOrder.setOnClickListener(view -> startActivity(new Intent(activity,GenerateNewOrderActivity.class)));

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                try {
                    Fragment fragment = adapter.getFragment(position);
                    if (fragment instanceof ChatFragment) {
                        ((ChatFragment) fragment).stopRecording();
                        binding.ratingLayout.setVisibility(View.VISIBLE);
                    }else {
                        binding.ratingLayout.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        binding.switchBtn.setOnClickListener(view -> {
            if (binding.switchBtn.isChecked()){
                binding.topLayout.setVisibility(View.GONE);
                binding.switchBtn.setChecked(true);
            }
            else {
                binding.topLayout.setVisibility(View.VISIBLE);
                binding.switchBtn.setChecked(false);
            }
        });
    }

    private void setViewPagerData() {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.SHOP_ID,shopId);
        bundle.putString(Constant.SHOP_ORDER_MODEL,shopOrderModel);
        bundle.putString(Constant.TO_USER_ID,toUserId);

        adapter=new ViewPagerShopNewAdapter(getSupportFragmentManager(),bundle, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new ChatFragment(),"Chat");
        adapter.addFragment(new OrderDetailFragment(),"Order Detail");
        binding.viewPager.setAdapter(adapter);
    }
    private void parseShopOrderResponse(String response) {
        progressHelper.dismiss();
        try {
            Log.d("ShopOrderResponse","response "+response);

            ShopOrderModel model= new Gson().fromJson(response, ShopOrderModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                arrayList=new ArrayList<>();
                arrayList.add(model.getData());

                if (model.getData().getOrderHistory()!=null) {
                    deliveryCharge=model.getData().getOrderHistory().get(0).getDeliveryCharge();
                    totalCost=model.getData().getOrderHistory().get(0).getTotalCost();

                    orderId=model.getData().getOrderHistory().get(0).getOrderId();
                    rating=model.getData().getOrderHistory().get(0).getRating();

                    binding.rating.setText(rating+" "+"Rating");
                }
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}