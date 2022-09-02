package in.forpay.activity.shop;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.shop.UserReviewAdapter;
import in.forpay.databinding.ActivityUserReviewBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.GetUserRating;
import in.forpay.model.shop.ShopCategoryModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class UserReviewActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityUserReviewBinding binding;
    private Activity activity;
    private UserReviewAdapter adapter;
    private ArrayList<Object> arrayList;
    private ProgressHelper progressHelper;
    private String orderId,rating,review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);

        orderId=getIntent().getStringExtra(Constant.ORDER_ID);

        init();
    }

    private void init() {
        setMyRateAdapter();
        getUserRating();
        binding.backPress.setOnClickListener(v->onBackPressed());

        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating= String.valueOf(v);
            }
        });

        binding.submit.setOnClickListener(v->{
            rateUser();
        });
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

    private void getUserRating() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("orderId",orderId);

            String request = Utility.mapWrapper(this, map1);

            getUserRatingRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void getUserRatingRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_GET_USER_RATING, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseGetUserRatingResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseGetUserRatingResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("GetUserRatingResponse","response "+response);

            GetUserRating model= new Gson().fromJson(response, GetUserRating.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                arrayList.addAll(model.getData());
                if (!model.getYourRaing().isEmpty())
                    binding.ratingBar.setRating(Float.parseFloat(model.getYourRaing()));
                binding.review.setText(model.getYourRatingText());
                adapter.notifyDataSetChanged();

                if (arrayList.size()==0){
                    Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Review Found");
                }
            } else {
                Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Review Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rateUser() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("orderId",orderId);
            map1.put("rating",rating);
            map1.put("ratingText",binding.review.getText().toString());

            String request = Utility.mapWrapper(this, map1);


            rateUserRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void rateUserRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_RATE_USER, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseRateUserResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseRateUserResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("RateUserResponse","response "+response);

            ShopCategoryModel shopCategory= new Gson().fromJson(response, ShopCategoryModel.class);
            if (shopCategory.getResCode().equals(Constant.CODE_200)) {
                onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}