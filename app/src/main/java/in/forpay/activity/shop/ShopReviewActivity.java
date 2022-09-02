package in.forpay.activity.shop;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.shop.UserReviewAdapter;
import in.forpay.databinding.ActivityShopReviewBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.GetUserRating;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ShopReviewActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityShopReviewBinding binding;
    private Activity activity;
    private UserReviewAdapter adapter;
    private ArrayList<Object> arrayList;
    private ProgressHelper progressHelper;
    private String orderId,shopId,rating,review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityShopReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);

        shopId=getIntent().getStringExtra(Constant.SHOP_ID);
        orderId=getIntent().getStringExtra(Constant.ORDER_ID);

        init();
    }

    private void init() {
        setMyRateAdapter();
        getShopRating();
        binding.backPress.setOnClickListener(v->onBackPressed());
    }

    private void setMyRateAdapter() {
        arrayList=new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter=new UserReviewAdapter(activity,arrayList,this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, String tag) {

    }

    private void getShopRating() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("orderId",orderId);
            map1.put("shopId",shopId);

            String request = Utility.mapWrapper(this, map1);

            getShopRatingRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void getShopRatingRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_GET_SHOP_RATING, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseGetShopRatingResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseGetShopRatingResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("GetShopRatingResponse","response "+response);

            GetUserRating model= new Gson().fromJson(response, GetUserRating.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                arrayList.addAll(model.getData());
                adapter.notifyDataSetChanged();

                if (arrayList.size()==0){
                    Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Review Found");
                }
            }
            else {
                Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Review Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}