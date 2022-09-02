package in.forpay.activity.kyc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityAadhaarKycBinding;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class AadhaarKycActivity extends AppCompatActivity {
    ProgressHelper progressHelper;
    Activity activity;
    ActivityAadhaarKycBinding binding;
    String aadhaarNumber, panNumber, panName, sessionId, distributorOtp, aadhaarOtp = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_aadhaar_kyc);
        activity = this;
        progressHelper = new ProgressHelper(this);


        initKycCheck();


        binding.submitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitKyc("submit");
            }
        });

        binding.submitVideoKycView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitVideoKycView();
            }
        });

        binding.backBtn.setOnClickListener(v -> finish());


    }

    private void initKycCheck() {
        binding.LLaadhaarInput.setVisibility(View.GONE);
        submitKyc("check");
    }

    private void submitVideoKycView() {
        Intent intent = new Intent(AadhaarKycActivity.this, AddKycActivity.class);

        startActivity(intent);
    }

    private void submitKyc(String type) {

        progressHelper.show();
        distributorOtp = binding.editTextDistributorOtp.getText().toString();
        aadhaarOtp = binding.editTextAadhaarOtp.getText().toString();
        aadhaarNumber = binding.editTextAadhaarNumber.getText().toString();
        panNumber = binding.editTextPanNumber.getText().toString();

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("aadhaarNumber", aadhaarNumber);
        map1.put("panNumber", panNumber);
        map1.put("sessionId", sessionId);
        map1.put("panName", panName);
        map1.put("distributorOtp", distributorOtp);
        map1.put("aadhaarOtp", aadhaarOtp);

        String request = Utility.mapWrapper(this, map1);

        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_AADHAAR_KYC, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {
                    progressHelper.dismiss();
                    parseKycResponseData(result, type);
                }
            });
        }
    }

    private void parseKycResponseData(String result, String type) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            String resCode = jsonObject.getString("resCode");
            String resText = jsonObject.getString("resText");
            String isDistributor = jsonObject.getString("isDistributor");
            String status = jsonObject.getString("status");

            if (type.equals("check")) {
                if (resCode.equals("201") || resCode.equals("203") || 1 == 1) {
                    binding.LLaadhaarInput.setVisibility(View.VISIBLE);
                    Utility.showToastLatest(this, resText, resCode);
                    binding.submitVideoKycView.setVisibility(View.GONE);
                    binding.addressLayout.setVisibility(View.GONE);
                    if (resCode.equals("200")) {
                        binding.editTextAadhaarNumber.setText(jsonObject.getString("aadhaarNumber"));
                        binding.editTextPanNumber.setText(jsonObject.getString("panNumber"));
                        binding.editTextAddress.setText(jsonObject.getString("userAddress"));
                        binding.addressLayout.setVisibility(View.VISIBLE);
                        binding.editTextAadhaarNumber.setEnabled(false);
                        binding.editTextPanNumber.setEnabled(false);
                        binding.editTextAddress.setEnabled(false);
                        binding.submitTextView.setVisibility(View.GONE);

                        if (!jsonObject.getString("isVideoKyc").equals("yes")) {
                            binding.submitVideoKycView.setVisibility(View.VISIBLE);

                        }
                    }
                } else {
                    Utility.showToastLatest(this, resText, resCode);
                }

            } else {
                //binding.LayoutDistributorOtp.setVisibility(View.GONE);
                //binding.LapyoutAadhaarOtp.setVisibility(View.GONE);
                if (status.equals("OTPSENT")) {
                    binding.LapyoutAadhaarOtp.setVisibility(View.VISIBLE);
                    sessionId = jsonObject.getString("sessionId");
                    if (!sessionId.isEmpty()) {
                        panName = jsonObject.getString("panName");
                    }

                    if (isDistributor.equals("yes")) {
                        binding.LayoutDistributorOtp.setVisibility(View.VISIBLE);
                    }
                } else if (resCode.equals("200")) {
                    Utility.showToastLatest(this, resText, resCode);
                } else {
                    Utility.showToastLatest(this, resText, resCode);
                }

            }


        } catch (Exception e) {

            Utility.showToastLatest(this, e.toString(), "ERROR");
        }

    }
}
