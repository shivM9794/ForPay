package in.forpay.activity.shop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityRatingOrderBinding;
import in.forpay.model.shop.GetOrderRatingModel;
import in.forpay.model.shop.RateOrderModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class RatingOrderActivity extends AppCompatActivity {

    private ActivityRatingOrderBinding binding;
    private AppCompatActivity activity;
    private ProgressHelper progressHelper;
    private String orderId,shopId,rating,review,image;
    private boolean isFromPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRatingOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);

        orderId=getIntent().getStringExtra(Constant.ORDER_ID);
        shopId=getIntent().getStringExtra(Constant.SHOP_ID);
        image=getIntent().getStringExtra(Constant.SHOP_IMAGE);
        isFromPurchase=getIntent().getBooleanExtra(Constant.IS_FROM_PURCHASE,false);

        init();
    }

    private void init() {
        getOrderRating();

        Glide.with(activity).load(image).into(binding.image);
        binding.backPress.setOnClickListener(v->onBackPressed());

        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating= String.valueOf(v);
            }
        });

        binding.review.setOnClickListener(v->binding.alert.setVisibility(View.INVISIBLE));

        binding.submit.setOnClickListener(v->{
            review=binding.review.getText().toString();
            if (!review.isEmpty()) {
                rateOrder();
            }else binding.alert.setVisibility(View.VISIBLE);
        });
    }

    private void getOrderRating() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("orderId",orderId);

            String request = Utility.mapWrapper(this, map1);

            orderRatingRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void orderRatingRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_GET_ORDER_RATING, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseOrderRatingResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }

    }

    private void parseOrderRatingResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("OrderRatingResponse","response "+response);

            GetOrderRatingModel model= new Gson().fromJson(response, GetOrderRatingModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                if (!model.getData().getRating().isEmpty())
                    binding.ratingBar.setRating(Float.parseFloat(model.getData().getRating()));
                binding.review.setText(model.getData().getRatingText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rateOrder() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("orderId",orderId);
            map1.put("shopId",shopId);
            map1.put("rating",rating);
            map1.put("ratingText",review);

            String request = Utility.mapWrapper(this, map1);

            rateOrderRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void rateOrderRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_RATE_ORDER, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseRateOrderResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseRateOrderResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("RateOrderResponse","response "+response);

            RateOrderModel model= new Gson().fromJson(response, RateOrderModel.class);

            if (model.getResCode().equals(Constant.CODE_200)) {
                onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}