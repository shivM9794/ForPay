package in.forpay.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.shop.SelectCategoryAdapter;
import in.forpay.databinding.ActivityShopCategoryBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.ShopCategoryModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ShopCategoryActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityShopCategoryBinding binding;
    private AppCompatActivity activity;
    private ProgressHelper progressHelper;
    private SelectCategoryAdapter adapter;
    private ArrayList<ShopCategoryModel.DataBean> arrayList=new ArrayList<>();
    private String cityName="",address="",latitude="",longitude="";
    private String categoryId,shopImage,selectedId,shopId,shopName,contactNo,deliveryRange,productId;
    private boolean isFromEditYourShop;
    private ArrayList<String> selectedIdList=new ArrayList<>();
    private ArrayList<String> selectedCategoryList=new ArrayList<>();
    private ArrayList<List<ShopCategoryModel.DataBean.SubCategoryBean>> selectedSubCategoryList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShopCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper = new ProgressHelper(activity);

        isFromEditYourShop=getIntent().getBooleanExtra(Constant.IS_FROM_EDIT_YOUR_SHOP,false);

        shopImage=getIntent().getStringExtra(Constant.SHOP_IMAGE);
        shopName=getIntent().getStringExtra(Constant.SHOP_NAME);
        contactNo=getIntent().getStringExtra(Constant.SHOP_CONTACT_NO);
        deliveryRange=getIntent().getStringExtra(Constant.SHOP_DELIVERY_RANGE);
        latitude=getIntent().getStringExtra(Constant.LOCATION_LATITUDE);
        longitude=getIntent().getStringExtra(Constant.LOCATION_LONGITUDE);
        categoryId= getIntent().getStringExtra(Constant.SHOP_CATEGORY_ID);
        shopId= getIntent().getStringExtra(Constant.SHOP_ID);
        productId= getIntent().getStringExtra(Constant.PRODUCT_ID);

        init();
    }

    private void init() {
        setCategoryAdapter();
        getShopCategoryList();

        binding.backPress.setOnClickListener(v -> onBackPressed());

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedId=android.text.TextUtils.join(",", selectedIdList);
                if (!selectedId.isEmpty()){

                    Intent intent=new Intent(activity, ShopSubCategoryActivity.class);
                    intent.putExtra(Constant.SHOP_CATEGORY_ID,selectedId);
                    intent.putExtra(Constant.SHOP_CATEGORY_LIST,selectedCategoryList);
                    intent.putExtra(Constant.SHOP_SUB_CATEGORY_LIST, selectedSubCategoryList);
                    intent.putExtra(Constant.PRODUCT_ID,productId);
                    intent.putExtra(Constant.IS_FROM_EDIT_YOUR_SHOP,isFromEditYourShop);
                    intent.putExtra(Constant.SHOP_IMAGE, shopImage);
                    intent.putExtra(Constant.SHOP_NAME, shopName);
                    intent.putExtra(Constant.SHOP_CONTACT_NO, contactNo);
                    intent.putExtra(Constant.SHOP_DELIVERY_RANGE, deliveryRange);
                    intent.putExtra(Constant.LOCATION_LATITUDE, latitude);
                    intent.putExtra(Constant.LOCATION_LONGITUDE, longitude);
                    intent.putExtra(Constant.SHOP_ID, shopId);
                    startActivity(intent);
                }else {
                    Utility.showToast(activity, "Please Select Category", "");
                }

            }
        });
    }

    private void setCategoryAdapter() {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(activity,2));
        adapter=new SelectCategoryAdapter(activity,arrayList,selectedIdList,this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, String tag) {
        if (selectedIdList.contains(arrayList.get(position).getId())) {
            selectedIdList.remove(arrayList.get(position).getId());
            selectedCategoryList.remove(arrayList.get(position).getName());
            selectedSubCategoryList.remove(arrayList.get(position).getSubCategory());
        }else{
            selectedIdList.add(arrayList.get(position).getId());
            selectedCategoryList.add(arrayList.get(position).getName());
            if (arrayList.get(position).getSubCategory()!=null)
                selectedSubCategoryList.add(arrayList.get(position).getSubCategory());
            else
                selectedSubCategoryList.add(new ArrayList<>());
        }
        adapter.notifyItemChanged(position);
    }

    private void getShopCategoryList() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code

            String request = Utility.mapWrapper(this, map1);

            shopCategoryRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void shopCategoryRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_SHOP_CATEGORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseShopCategoryResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseShopCategoryResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("ShopCategoryResponse","response "+response);

            ShopCategoryModel shopCategory= new Gson().fromJson(response, ShopCategoryModel.class);
            if (shopCategory.getResCode().equals(Constant.CODE_200)) {
                arrayList.addAll(shopCategory.getData());
                adapter.notifyDataSetChanged();

                if (!categoryId.isEmpty()) {
                    String[] categoryList = categoryId.split(",");
                    for (ShopCategoryModel.DataBean model:arrayList){
                        for (String categoryId:categoryList){
                            if (categoryId.equals(model.getId())){
                                selectedIdList.add(categoryId);
                                selectedCategoryList.add(model.getName());
                                if (model.getSubCategory()!=null)
                                    selectedSubCategoryList.add(model.getSubCategory());
                                else
                                    selectedSubCategoryList.add(new ArrayList<>());
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                if(arrayList.size()==0){
                    Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Category Found");
                }
            }else {
                Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Category Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}