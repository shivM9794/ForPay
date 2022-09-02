package in.forpay.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.shop.ShopSubCategoryAdapter;
import in.forpay.databinding.ActivityShopSubCategoryBinding;
import in.forpay.listener.SelectedIdListener;
import in.forpay.model.shop.AddShopModel;
import in.forpay.model.shop.ShopCategoryModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ShopSubCategoryActivity extends AppCompatActivity implements SelectedIdListener {

    private ActivityShopSubCategoryBinding binding;
    private AppCompatActivity activity;
    private ShopSubCategoryAdapter adapter;
    private ArrayList<List<ShopCategoryModel.DataBean.SubCategoryBean>> arrayList=new ArrayList<>();
    private ArrayList<String> selectedIdList=new ArrayList<>();
    private ArrayList<String> categoryList=new ArrayList<>();
    private String cityName="",address="",latitude="",longitude="";
    private String categoryId,shopImage,selectedId,shopId,shopName,contactNo,deliveryRange,productId;
    private ProgressHelper progressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShopSubCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);

        arrayList.addAll(getIntent().getParcelableArrayListExtra(Constant.SHOP_SUB_CATEGORY_LIST));
        shopImage=getIntent().getStringExtra(Constant.SHOP_IMAGE);
        shopName=getIntent().getStringExtra(Constant.SHOP_NAME);
        contactNo=getIntent().getStringExtra(Constant.SHOP_CONTACT_NO);
        deliveryRange=getIntent().getStringExtra(Constant.SHOP_DELIVERY_RANGE);
        latitude=getIntent().getStringExtra(Constant.LOCATION_LATITUDE);
        longitude=getIntent().getStringExtra(Constant.LOCATION_LONGITUDE);
        categoryId= getIntent().getStringExtra(Constant.SHOP_CATEGORY_ID);
        categoryList= getIntent().getStringArrayListExtra(Constant.SHOP_CATEGORY_LIST);
        shopId= getIntent().getStringExtra(Constant.SHOP_ID);
        productId= getIntent().getStringExtra(Constant.PRODUCT_ID);

        init();
    }

    private void init() {
        setShopCategoryAdapter();
        if (shopImage != null) {
            Utility.imageLoader(activity,shopImage,binding.image);
        }
        binding.backPress.setOnClickListener(v->onBackPressed());

        binding.save.setOnClickListener(v->{
            selectedId=android.text.TextUtils.join(",", selectedIdList);
            if (!selectedId.isEmpty()){
                addShop();
            }
        });

        if (!productId.isEmpty()) {
            List<String> productList = Arrays.asList(productId.split(","));

            for (List<ShopCategoryModel.DataBean.SubCategoryBean> arrayList :arrayList){
                for (ShopCategoryModel.DataBean.SubCategoryBean model:arrayList){
                    for (String productId:productList){
                        if (productId.equals(model.getId())){
                            selectedIdList.add(productId);
                        }
                    }
                }
            }

            adapter.notifyDataSetChanged();
        }
    }

    private void setShopCategoryAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new ShopSubCategoryAdapter(activity,arrayList,selectedIdList,categoryList,this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(ArrayList<String> selectedIdList) {
        this.selectedIdList=selectedIdList;
    }

    private void addShop() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("shopId",shopId);
            map1.put("shopName",shopName);
            map1.put("contactNumber",contactNo);
            map1.put("deliveryRange",deliveryRange);
            map1.put("latitude",latitude);
            map1.put("longitude",longitude);
            map1.put("catIds",categoryId);
            map1.put("productIds",selectedId);

            String request = Utility.mapWrapper(this, map1);

            addShopRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void addShopRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();

            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_ADD_SHOP, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseAddShopResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseAddShopResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("AddShopResponse","response "+response);
            AddShopModel addShopModel=new Gson().fromJson(response, AddShopModel.class);
            if (addShopModel.getResCode().equals(Constant.CODE_200)) {
                startActivity(new Intent(activity, ShopActivity.class));
                finish();
            } else {
                Utility.showToast(activity, addShopModel.getResText(), "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}