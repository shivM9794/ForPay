package in.forpay.activity.balance;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityDmtBinding;
import in.forpay.model.response.GetDmtResponse;
import in.forpay.model.response.GetPayoutBankVerify;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class DmtActivity extends AppCompatActivity {

    private ActivityDmtBinding mBinding;
    Activity activity;
    ProgressHelper progressHelper;
    private String amount, ifscCode, accountNumber, accountName, pin, remark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utility.checkLoggedUser(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dmt);
        activity = this;
        progressHelper = new ProgressHelper(activity);

        mBinding.backBtn.setOnClickListener(v -> finish());


        init();
        checkCustomer();
        checkBeneficiaryName();


    }

    private void checkBeneficiaryName() {


        mBinding.editTextIfscCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 11) {
                    if (before == 0 && count == 1) {
                        veriyfyName();
                    } else {

                    }

                }

            }
        });

    }


    private void veriyfyName() {

        progressHelper.show();

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("ifscCode", mBinding.editTextIfscCode.getText().toString());
        map1.put("accountNumber", mBinding.editTextAccountNumber.getText().toString());


        String request = Utility.mapWrapper(activity, map1);


        if (Utility.isNetworkConnected(activity)) {

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_PAYOUT_BANK_VERIFY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            progressHelper.dismiss();
                            parseResponseVerification(result);


                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }

    }


    private void parseResponseVerification(String result) {


        GetPayoutBankVerify response = new Gson().fromJson(result, GetPayoutBankVerify.class);
        mBinding.editTextAccountName.setText(response.getAccountName());


    }

    private void init() {

        mBinding.submitTextView.setOnClickListener(v -> {
            submit();
        });
    }

    private void checkCustomer() {

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        String request = Utility.mapWrapper(this, map1);

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_CUSTOMER_DETAIL, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseCustomerResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void parseCustomerResponse(String result) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {

            GetDmtResponse response = new Gson().fromJson(result, GetDmtResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {
                mBinding.availableLimit.setText("Rs "+ response.getAvailableLimit());
            } else {
                Utility.showToastLatest(activity, response.getResText(), response.getResCode());
            }


        } else {
            Utility.showToastLatest(activity, getString(R.string.server_not_responding), "ERROR");
        }

    }

    private void submit() {
        amount = mBinding.editTextAmount.getText().toString();
        accountName = mBinding.editTextAccountName.getText().toString();
        ifscCode = mBinding.editTextIfscCode.getText().toString();
        pin = mBinding.editTextPin.getText().toString();
        remark = mBinding.editTextRemark.getText().toString();
        accountNumber = mBinding.editTextAccountNumber.getText().toString();


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("operatorId", "94");
        map1.put("amount", amount);
        map1.put("accountNumber", accountNumber);
        map1.put("accountName", accountName);
        map1.put("ifscCode", ifscCode);
        map1.put("remark", remark);
        map1.put("pin", pin);
        mBinding.editTextPin.setText("");


        String request = Utility.mapWrapper(this, map1);

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseSendFundResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }

    }


    private void parseSendFundResponse(String result) {

        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

            if (response.getResCode().equals(Constant.CODE_200)) {

                Bundle bundle = new Bundle();
                bundle.putString("outputJson", response.getData().getOutputJson());
                bundle.putString("uniqId", response.getData().getUniqId());
                bundle.putString("serviceType", response.getData().getType());
                bundle.putString("offerDetails", "");
                bundle.putString("pin", pin);
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
            Utility.showToast(this, getString(R.string.server_not_responding), "");
        }

    }


}
