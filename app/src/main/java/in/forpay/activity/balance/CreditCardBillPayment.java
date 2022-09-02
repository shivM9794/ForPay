package in.forpay.activity.balance;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityCreditCardBillPaymentBinding;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class CreditCardBillPayment extends AppCompatActivity {

    private static final String TAG = "creditcardbillpayment";
    private ActivityCreditCardBillPaymentBinding binding;
    Activity activity;
    ProgressHelper progressHelper;
    Boolean showProgressbar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utility.checkLoggedUser(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_credit_card_bill_payment);
        activity = this;

        progressHelper = new ProgressHelper(this);

        toolbar();

        binding.submitTextView.setOnClickListener(v -> {

            payBill();
        });

    }

    private void toolbar() {

        binding.backBtn.setOnClickListener(v -> onBackPressed());
    }


    private void payBill() {

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("creditCardNumber", binding.editTextCreditCardNumber.getText().toString());
        map1.put("amount", binding.editTextAmount.getText().toString());
        map1.put("pin", binding.editTextPin.getText().toString());
        map1.put("otp", binding.editTextOtp.getText().toString());
        map1.put("operatorId", "532");
        String request = Utility.mapWrapper(this, map1);
        String method = Constant.METHOD_CC_SEND_OTP;
        if (!binding.editTextOtp.getText().equals("")) {
            method = Constant.METHOD_VALIDATE_RECHARGE;


        }

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    method, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }

    }

    private void parseResponse(String result) {

        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            if (!binding.editTextOtp.getText().equals("")) {

                GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

                if (response.getResCode().equals(Constant.CODE_200)) {

                    Bundle bundle = new Bundle();
                    bundle.putString("outputJson", response.getData().getOutputJson());
                    bundle.putString("uniqId", response.getData().getUniqId());
                    bundle.putString("serviceType", response.getData().getType());
                    bundle.putString("offerDetails", "");
                    bundle.putString("pin", binding.editTextPin.getText().toString());
                    bundle.putString("amount", response.getData().getBillAmount());
                    bundle.putString("selectedMode", "");
                    bundle.putString("mobile", response.getData().getMobile());
                    bundle.putString("operatorId", response.getData().getOperatorId());
                    bundle.putString("extraData", response.getData().getExtraData());
                    bundle.putString("coupon", "");
                    bundle.putString("couponId", "");
                    bundle.putParcelable(Constant.INSUFFICIENTDATA, response.getData().getInsufficientData());
                    bundle.putParcelable(Constant.POPUPDATA, response.getData().getPopupData());


                    Utility.openCheckOutActivity(activity, response.getData().getType(), bundle);


                } else {
                    Utility.showToast(this, response.getResText(), response.getResCode());
                }
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("resCode").equals(Constant.CODE_200)) {
                        binding.editTextOtp.setVisibility(View.VISIBLE);
                    } else {
                        Utility.showToastLatest(activity, jsonObject.getString("resText"), "ERROR");
                    }
                } catch (Exception e) {
                    Utility.showToastLatest(activity, "Error " + e, "ERROR");
                }

            }


        } else {
            Utility.showToastLatest(activity, getString(R.string.server_not_responding), "ERROR");
        }
        binding.editTextPin.setText("");
    }

}
