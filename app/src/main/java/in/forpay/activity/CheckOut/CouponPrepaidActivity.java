package in.forpay.activity.CheckOut;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.PrepaidCouponAdapter;
import in.forpay.model.response.GetCashBackOfferResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class CouponPrepaidActivity extends AppCompatActivity {


    RecyclerView rechargeCoupons;
    ProgressHelper progressHelper;
    private PrepaidCouponAdapter prepaidCouponAdapter;
    private ArrayList<GetCashBackOfferResponse.Data> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_prepaid);
        progressHelper = new ProgressHelper(this);
        rechargeCoupons = findViewById(R.id.rechargeCoupons);

        openCoupon(true);

        ImageView backBtn;
        backBtn=findViewById(R.id.back_Btn);
        backBtn.setOnClickListener(v -> {
            finish();
        });


    }

    private void openCoupon(Boolean showProgressbar) {

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
            QueryManager.getInstance().postRequest(this, Constant.METHOD_CASHBACK_COUPON_OFFER, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {

                    if (showProgressbar) {
                        progressHelper.dismiss();
                    }

                    Log.d("CouponPrepaidActivity", "Response " + result);
                    parseCashbackResponseData(result);


                }
            });
        }
    }

    private void parseCashbackResponseData(String result) {

        GetCashBackOfferResponse response = new Gson().fromJson(result, GetCashBackOfferResponse.class);
        try {
            if (response.getResCode().equals(Constant.CODE_200)) {
                // text_View_Amount.setText(response.getResCode().toString());
                Log.d("CouponPrepaidActivity", "json 1 " + response.getOfferDetails().get(0).getAmount());
                mList = response.getOfferDetails();
                showCoupon();

            } else {
                Utility.showToastLatest(this, response.getResText(), response.getResCode());
            }


        } catch (Exception e) {
            Log.d("CouponPrepaidActivity", "Response 2 " + result + " error " + e.toString());
        }
    }


    private void showCoupon() {
        rechargeCoupons.setLayoutManager(new LinearLayoutManager(this));
        prepaidCouponAdapter = new PrepaidCouponAdapter(this, mList);
        rechargeCoupons.setAdapter(prepaidCouponAdapter);
    }
}