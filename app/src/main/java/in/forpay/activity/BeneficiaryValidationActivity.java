package in.forpay.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityBeneficiaryValidationBinding;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class BeneficiaryValidationActivity extends AppCompatActivity {


    private static final String TAG = "BeneficiaryValidation";
    private ActivityBeneficiaryValidationBinding binding;
    Activity activity;
    ProgressHelper progressHelper;
    Boolean showProgressbar = false;
    String status = "PENDING";
    PowerManager pm;
    private Handler handler;
    private Runnable runnable;
    final int[] runTime = {0};
    volatile boolean activityStopped = false;
    String orderId = "";
    String resText = "";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utility.checkLoggedUser(this);

        sharedPreferences = getSharedPreferences(Constant.STORAGE_NAME, Context.MODE_PRIVATE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_beneficiary_validation);
        activity = this;

        progressHelper = new ProgressHelper(this);

        binding.idSubmit.setOnClickListener(view -> {

            submit();
        });

        binding.textViewBack.setOnClickListener(v -> {
            finish();
        });

    }


    private void submit() {
        if (validation()) {
            binding.editTextName.setText("");
            progressHelper.show();


            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("versionCode", Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));
            map1.put("ifscCode", binding.editTextIfscCode.getText().toString());
            map1.put("accountNumber", binding.editTextAccountNumber.getText().toString());
            map1.put("pin", binding.editTextPin.getText().toString());
            map1.put("operatorId", "524");
            binding.editTextPin.setText("");

            String request = Utility.mapWrapper(this, map1);


            if (Utility.isNetworkConnected(this)) {
                QueryManager.getInstance().postRequest(this, Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
                    @Override
                    public void onResult(Exception e, String result, ResponseManager responseManager) {
                        progressHelper.dismiss();
                        parseSubmitResponseData(result);
                    }
                });
            }
        }
    }

    private boolean validation() {
        String accountNumber = binding.editTextAccountNumber.getText().toString().trim();
        String ifscCode = binding.editTextIfscCode.getText().toString().trim();
        String pin = binding.editTextPin.getText().toString().trim();
        if (TextUtils.isEmpty(accountNumber)) {
            Utility.showToast(activity, "Please enter account number", "");
            return false;
        } else if (TextUtils.isEmpty(ifscCode)) {
            Utility.showToast(activity, "Please enter ifsc code", "");
            return false;
        } else if (TextUtils.isEmpty(pin)) {
            Utility.showToast(activity, "Please enter pin", "");
            return false;
        }
        return true;
    }

    private void parseSubmitResponseData(String result) {

        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

            if (response.getResCode().equals(Constant.CODE_200)) {
                Bundle bundle = new Bundle();
                bundle.putString("outputJson", response.getData().getOutputJson());
                bundle.putString("uniqId", response.getData().getUniqId());
                bundle.putString("serviceType", response.getData().getType());

                bundle.putString("pin", binding.editTextPin.getText().toString());
                bundle.putString("extraData", response.getData().getExtraData());
                bundle.putParcelable(Constant.INSUFFICIENTDATA, response.getData().getInsufficientData());
                bundle.putParcelable(Constant.POPUPDATA, response.getData().getPopupData());
                bundle.putString("offerDetails","");

                Utility.openCheckOutActivity(activity, response.getData().getType(), bundle);


            } else {
                Utility.showToast(activity, response.getResText(), response.getResCode());
            }

                /*
                GetRechargeNowResponse response = new Gson().fromJson(result, GetRechargeNowResponse.class);
                if (response.getData().getResCode().equals(Constant.CODE_200)) {
                    Utility.showToast(getActivity(), response.getData().getResText(), response.getData().getResCode());
                    Utility.setBalance(getActivity(), response.getData().getBal());
                    Utility.setCommissionBalance(getActivity(), response.getData().getCommissionBal());
                    addOrderId(response.getData());
                    refreshUI();
                } else if (response.getData().getResCode().equals(Constant.CODE_201)) {
                    Utility.showToast(getActivity(), response.getData().getResText(), response.getData().getResCode());
                    Utility.setBalance(getActivity(), response.getData().getBal());
                    Utility.setCommissionBalance(getActivity(), response.getData().getCommissionBal());
                    addOrderId(response.getData());
                    refreshUI();
                } else {
                    if (response.getData().getStatus().equals(Constant.FAILED)) {
                        if (!response.getData().getOrderId().equals("0")) {
                            addOrderId(response.getData());
                        }
                    }
                    Utility.showToast(getActivity(), response.getData().getResText(), response.getData().getResCode());
                }
                */

        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding), "");
            startActivity(new Intent(this, ServerNotResponseActivity.class));
        }


    }


}
