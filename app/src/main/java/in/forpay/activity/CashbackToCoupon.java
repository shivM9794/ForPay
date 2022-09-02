package in.forpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityCashbackToCouponBinding;
import in.forpay.model.response.GetProfileResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import in.forpay.webView.WebViewActivity;

public class CashbackToCoupon extends AppCompatActivity {


    private static final String TAG = "Cashbacktocoupon";
    private ActivityCashbackToCouponBinding binding;
    Activity activity;
    ProgressHelper progressHelper;
    Boolean showProgressbar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utility.checkLoggedUser(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cashback_to_coupon);
        activity = this;

        progressHelper = new ProgressHelper(this);


        init(true);

        binding.submitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!binding.checkBoxTOS.isChecked()) {

                    Utility.showToastLatest(activity, "To use our service you must need to accept TOS", "ERROR");

                } else {
                    submit();
                }
            }
        });

        binding.textViewTos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTos();
            }
        });

    }

    private void openTos() {

        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", "https://forpay.in/cashbackPolicy.php?language" + Utility.getDefaultLanguage(activity));
        intent.putExtra("webName", "CashbackPolicy");
        startActivity(intent);
    }

    private void submit() {


        progressHelper.show();


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("quantity", binding.editTextQuantity.getText().toString());
        map1.put("method", "confirm");
        map1.put("couponAmount", binding.editTextAmount.getText().toString());
        map1.put("toMobile", binding.editTextMobileNumber.getText().toString());
        map1.put("couponName", binding.editTextCouponName.getText().toString());
        map1.put("couponDescription", binding.editTextMessage.getText().toString());

        String request = Utility.mapWrapper(this, map1);


        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_CASHBACK_TO_COUPON, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {
                    progressHelper.dismiss();
                    parseSubmitResponseData(result);
                }
            });
        }

    }

    private void parseSubmitResponseData(String result) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            String resCode = jsonObject.getString("resCode");
            String resText = jsonObject.getString("resText");

            if (resCode.equals(Constant.CODE_200)) {
                Utility.showToastLatest(activity, resText, resCode);
                init(false);
            } else {
                Utility.showToastLatest(activity, resText, "ERROR");
            }

        } catch (Exception e) {
            Utility.showToastLatest(activity, e.toString(), "ERROR");
        }


    }

    private void init(Boolean showProgressbar) {

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
            QueryManager.getInstance().postRequest(this, Constant.METHOD_PROFILE, request, new CallbackListener() {
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

        GetProfileResponse response = new Gson().fromJson(result, GetProfileResponse.class);
        if (response.getResCode().equals(Constant.CODE_200)) {
            String oldMobile = binding.editTextMobileNumber.getText().toString();
            binding.textCommissionBalance.setText(response.getData().getCommissionBal());
            if (oldMobile.equals("") || oldMobile.isEmpty()) {
                binding.editTextMobileNumber.setText(response.getData().getMobile());

            }


        } else {
            Utility.showToastLatest(activity, response.getResText(), response.getResCode());
        }
    }
}
