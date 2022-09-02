package in.forpay.activity.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.balance.EWalletActivity;
import in.forpay.adapter.ShopCategoryAdapter;
import in.forpay.databinding.ActivityShopCategory2Binding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.response.GetEWalletListResponse;
import in.forpay.model.response.GetShopCategoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class Shop_CategoryActivity extends AppCompatActivity  {

    private ActivityShopCategory2Binding binding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private OxyMePref oxyMePref;
    private ArrayList<GetShopCategoryResponse.Data> dataArrayList = new ArrayList<>();
    private ArrayList<GetShopCategoryResponse.Data> arrayList;
    private ShopCategoryAdapter shopCategoryAdapter;
    private static final int REQUEST_SELECT_CONTACT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_category2);
        activity = this;
        progressHelper = new ProgressHelper(activity);
        oxyMePref = new OxyMePref(activity);
        binding.backPress.setOnClickListener(v -> finish());

        show(Boolean.TRUE);


    }

    private void show(Boolean showProgressbar) {

        if (showProgressbar) {
            progressHelper.show();

        }
        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this, map1);


        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_SHOP_CATEGORY,
                    request, new CallbackListener() {

                        @Override
                        public void onResult(Exception e, String result, ResponseManager responseManager) {

                            if (showProgressbar) {
                                progressHelper.dismiss();
                            }
                            parseCashbackResponseData(result);
                        }
                    });
        }

    }

    private void parseCashbackResponseData(String result) {

        try {
            GetShopCategoryResponse response = new Gson().fromJson(result, GetShopCategoryResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {
//                Log.d("ShopCategoryResponse", "response " + response);

                dataArrayList = response.getData();
                shopCategory();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void shopCategory() {
        binding.recyclercategoryShop.setLayoutManager(new GridLayoutManager(this, 3));
        shopCategoryAdapter = new ShopCategoryAdapter(activity, dataArrayList);
        binding.recyclercategoryShop.setAdapter(shopCategoryAdapter);
    }




    }
