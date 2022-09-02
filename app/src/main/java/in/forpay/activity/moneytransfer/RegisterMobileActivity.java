package in.forpay.activity.moneytransfer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.ActivityRgisterMobileBinding;
import in.forpay.model.response.GetCustomerHistoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class RegisterMobileActivity extends AppCompatActivity {

    private ActivityRgisterMobileBinding mBinding;
    private ProgressHelper progressHelper;
    private String mOTP = "";
    private boolean checkOtp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rgister_mobile);
        mBinding.setActivity(this);
        init();
    }

    /**
     * Click on back button
     */
    public void onClickBack() {
        onBackPressed();
    }

    /**
     * Click on register button
     */
    public void onClickRegister() {
        if (validation()) {


            Map<String,String> map1 = new HashMap<>();

            map1.put("token",Utility.getToken(this)); // key
            map1.put("imei",Utility.getImei(this)); // imei
            map1.put("versionCode",Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));
            map1.put("mobile",mBinding.editTextMobile.getText().toString().trim());
            map1.put("name",mBinding.editTextName.getText().toString().trim());
            map1.put("pincode",mBinding.editTextPincode.getText().toString().trim());
            map1.put("otp",mOTP);

            String request = Utility.mapWrapper(this,map1);


            customerRegister(request);
        }
    }

    private void init() {
        progressHelper = new ProgressHelper(this);
        getBundle();
        setToolbar();
    }

    /**
     * Get data from bundle
     */
    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String mobile = bundle.getString("mobile");
            mBinding.editTextMobile.setText(mobile);
        }
    }

    /**
     * Set toolbar
     */
    private void setToolbar() {
        mBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Validation for all fields
     */
    private boolean validation() {
        String name = mBinding.editTextName.getText().toString().trim();
        String mobile = mBinding.editTextMobile.getText().toString().trim();
        String pincode = mBinding.editTextPincode.getText().toString().trim();

        mOTP = mBinding.editTextOtp.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Utility.showToast(this, "Please enter a name","");
            return false;
        } else if (TextUtils.isEmpty(mobile)) {
            Utility.showToast(this, "Please enter a mobile number","");
            return false;
        } else if (TextUtils.isEmpty(pincode)) {
            Utility.showToast(this, "Please enter a pincode","");
            return false;
        } else if(checkOtp){
            if(TextUtils.isEmpty(mOTP)){
                Utility.showToast(this, "Please enter a OTP","");
            }
        }
        return true;
    }

    /**
     * Customer register
     */
    private void customerRegister(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_CUSTOMER_REGISTER, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
        if (!isDestroyed()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetCustomerHistoryResponse response = new Gson().fromJson(result,
                        GetCustomerHistoryResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    mBinding.layoutOtp.setVisibility(View.GONE);
                    mBinding.editTextOtp.setText("");
                    Utility.showToast(this, response.getResText(),response.getResCode());
                    finish();
                }

                else if(response.getResCode().equals(Constant.CODE_178)){
                    mBinding.layoutOtp.setVisibility(View.VISIBLE);
                    checkOtp = true;
                }

                else {
                    mBinding.editTextOtp.setText("");
                    Utility.showToast(this, response.getResText(),"");
                }
            } else {
                //Utility.showToast(this, getString(R.string.server_not_responding),"");
                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }
        }
    }
}
