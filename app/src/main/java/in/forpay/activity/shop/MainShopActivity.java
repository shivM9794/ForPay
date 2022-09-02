package in.forpay.activity.shop;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.adapter.shop.ShopExtraAdapter;
import in.forpay.databinding.ActivityMainShopBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ShopModelExtra;
import in.forpay.util.Constant;

public class MainShopActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityMainShopBinding binding;
    private AppCompatActivity activity;
    private ArrayList<ShopModelExtra> arrayList;
    private ShopExtraAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main_shop);
        activity=this;

        init();
    }

    private void init() {
        setAdapter();
    }

    private void setAdapter() {

        arrayList=new ArrayList<>();

        ShopModelExtra model = new ShopModelExtra(R.drawable.add_new_shop, Constant.ADD_NEW_SHOP);
        ShopModelExtra model1 = new ShopModelExtra(R.drawable.edit_your_shop, Constant.EDIT_YOUR_SHOP);
        ShopModelExtra model2 = new ShopModelExtra(R.drawable.shop_local, Constant.SHOP_LOCAL);
        ShopModelExtra model3 = new ShopModelExtra(R.drawable.my_purchase_oder, Constant.MY_PURCHASE_ORDERS);
        ShopModelExtra model4 = new ShopModelExtra(R.drawable.my_sold_orders, Constant.MY_SOLD_ORDERS);
        ShopModelExtra model5 = new ShopModelExtra(R.drawable.generate_new_order, Constant.GENERATE_NEW_ORDER);
        ShopModelExtra model6 = new ShopModelExtra(R.drawable.my_rating, Constant.MY_RATING);
        ShopModelExtra model7 = new ShopModelExtra(R.drawable.my_chat, Constant.MY_CHAT);

        arrayList.add(model);
        arrayList.add(model1);
        arrayList.add(model2);
        arrayList.add(model3);
        arrayList.add(model4);
        arrayList.add(model5);
        arrayList.add(model6);
        arrayList.add(model7);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new ShopExtraAdapter(activity,arrayList,this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, String tag) {
        switch (tag){
            case Constant.ADD_NEW_SHOP:{
                //startActivity(new Intent(activity,AddShopActivity.class));
                break;
            }
            case Constant.EDIT_YOUR_SHOP:{
                //startActivity(new Intent(activity,RatingOrderActivity.class));
                break;
            }
            case Constant.SHOP_LOCAL:{
                //startActivity(new Intent(activity,ShopListActivity.class));
                break;
            }
            case Constant.MY_PURCHASE_ORDERS:{
                //startActivity(new Intent(activity,MyPurchaseOrdersActivity.class));
                break;
            }case Constant.MY_SOLD_ORDERS:{
                //startActivity(new Intent(activity,SoldOrderListActivity.class));
                break;
            }
            case Constant.GENERATE_NEW_ORDER:{
                startActivity(new Intent(activity,GenerateNewOrderActivity.class));
                break;
            }
            case Constant.MY_RATING:{
                //startActivity(new Intent(activity,MyRatingActivity.class));
                break;
            }
            case Constant.MY_CHAT:{
                //startActivity(new Intent(activity,ShopChatActivity.class));
                break;
            }
        }
    }
}