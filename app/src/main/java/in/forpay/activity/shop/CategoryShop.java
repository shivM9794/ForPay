package in.forpay.activity.shop;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityCategoryShopBinding;
import in.forpay.model.response.GetCategoryShopResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class CategoryShop extends AppCompatActivity {

    private ActivityCategoryShopBinding binding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private OxyMePref oxyMePref;
    private CategoryShop categoryShopAdapter;
    private ArrayList<GetCategoryShopResponse.Data> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_shop);
        activity = this;
        progressHelper = new ProgressHelper(activity);
        oxyMePref = new OxyMePref(activity);

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
            QueryManager.getInstance().postRequest(this, Constant.METHOD_SHOP_CATEGORY, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {

                    if (showProgressbar) {
                        progressHelper.dismiss();
                    }
                    parseShopResponseData(result);
                }
            });
        }

    }

    private void parseShopResponseData(String result) {
        try {


            GetCategoryShopResponse response = new Gson().fromJson(result, GetCategoryShopResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {
                Log.d("ShopCategoryResponse", "response " + response);

//                data = response.getData();
//                shopCategory();
            }


        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void shopCategory() {
        binding.recyclercategoryShop.setLayoutManager(new GridLayoutManager(this, 3));
//        categoryShopAdapter = new CategoryShop(activity, data);
//        binding.recyclercategoryShop.setAdapter(categoryShopAdapter);

    }
}
