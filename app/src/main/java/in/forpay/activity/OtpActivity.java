package in.forpay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityOtpBinding;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.model.response.GetLoginValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.PreferenceConnector;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import in.forpay.util.databaseTask;

public class OtpActivity extends AppCompatActivity {

    private ActivityOtpBinding mBinding;
    private String mMobile = "";
    private String mPassword = "";
    private String mOTP = "";
    private ProgressHelper progressHelper;
    private String mImei = "";
    private String referer="";
    private TextView continueWithPassword;
    private TextView continueWithOtp;
    private String loginType="";
    private AppCompatActivity activity;
    private CountDownTimer countDownTimer;
    private boolean isCountDownTimerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Utility.setAppLocale(Utility.getDefaultLanguage(this), this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp);

        mBinding.setActivity(this);
        activity=this;
        progressHelper = new ProgressHelper(this);

        continueWithOtp= findViewById(R.id.continueWithOtp);
        continueWithPassword=findViewById(R.id.continueWithPassword);
        mBinding.otpLayout.setVisibility(View.GONE);



        loginType = getIntent().getStringExtra("loginType");
        referer = getIntent().getStringExtra("refer");
        mMobile=getIntent().getStringExtra("mobile");

        mBinding.editTextOTP.setText("");


        mBinding.resendOtpButton.setVisibility(View.GONE);
        mBinding.labelHelp.setVisibility(View.GONE);
        mBinding.otpLayout.setVisibility(View.GONE);

        try {
           // Log.d("GCM", "Gcm Try");

            Utility.subscribeGcm(this);
            Utility.getFirebaseMessagingToken(this);
        }
        catch (Exception e){
            //Log.d("GCM", "Gcm Exception "+e.toString());
        }
        getOtpViaCallTimer();
        countDownTimer.start();

        loginWith();

        mBinding.otpLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressHelper.show();
                onClickSignIn();
            }
        });
        mBinding.continueWithPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginType="passwordActivity";
                loginWith();
            }
        });

        mBinding.btnChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtpActivity.this,
                        LoginActivity1.class);
                startActivity(intent);
                finish();
            }
        });


        mBinding.continueWithOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginType="otpActivity";
                onClickResendOtp("smsOtp");
                loginWith();



            }
        });

        mBinding.resendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickResendOtp("smsOtp");
            }
        });

        mBinding.getOtpViaCall.setOnClickListener(view -> {
            mBinding.getOtpViaCall.setTextColor(ContextCompat.getColor(activity,R.color.gray_shop));
            mBinding.getOtpViaCall.setEnabled(false);
            mBinding.callTimer.setVisibility(View.VISIBLE);
            onClickResendOtp("callOtp");
            getOtpViaCallTimer();
        });
    }

    private void getOtpViaCallTimer() {
        countDownTimer=new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long l) {
                isCountDownTimerRunning=true;
                mBinding.callTimer.setText("00 : "+String.format("%02d",(int) (l/1000)));
            }

            @Override
            public void onFinish() {
                isCountDownTimerRunning=false;
                mBinding.getOtpViaCall.setEnabled(true);
                mBinding.callTimer.setText("");
                mBinding.callTimer.setVisibility(View.GONE);
                mBinding.getOtpViaCall.setTextColor(ContextCompat.getColor(activity,R.color.green_new));
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(OtpActivity.this,mBinding.otpNewLayout);
    }

    public void loginWith(){

        mBinding.labelHelp.setVisibility(View.VISIBLE);
        mBinding.mobileNumber.setVisibility(View.GONE);
        mBinding.labelSentotp.setVisibility(View.GONE);

        if(loginType.equals(Constant.ACTION_OTP_ACTIVITY)){
            mBinding.passLayout.setVisibility(View.GONE);
            mBinding.editTextPassword.setText("");
            mBinding.otpLayout.setVisibility(View.VISIBLE);

            mBinding.resendOtpButton.setVisibility(View.VISIBLE);
            mBinding.mobileNumber.setText("On Mobile "+mMobile);
            mBinding.labelSentotp.setText("Otp has been sent");
            mBinding.mobileNumber.setVisibility(View.VISIBLE);
            mBinding.labelSentotp.setVisibility(View.VISIBLE);

            mBinding.getOtpViaCall.setVisibility(View.VISIBLE);

            if (isCountDownTimerRunning)
                mBinding.callTimer.setVisibility(View.VISIBLE);
        }
        else{
            mBinding.passLayout.setVisibility(View.VISIBLE);
            mBinding.editTextPassword.setText("");
            mBinding.otpLayout.setVisibility(View.GONE);

            mBinding.resendOtpButton.setVisibility(View.GONE);

            mBinding.getOtpViaCall.setVisibility(View.GONE);
            mBinding.callTimer.setVisibility(View.GONE);


        }
        //mBinding.showHelpLine.setText(Utility.getHelfline(this));
    }

    public void onClickSignIn() {

        if (validation()) {
            Map<String,String> map1 = new HashMap<>();
            String temRefer="";
            if(Utility.getReferId(this)==null){
                temRefer=referer;
            }

            else if(Utility.getReferId(this).isEmpty()){
                temRefer=referer;
            }
            else{
                temRefer=Utility.getReferId(this);
            }



            map1.put("token",Utility.getToken(this)); // key
            map1.put("imei",Utility.getImei(this)); // imei
            map1.put("versionCode",Utility.getVersionCode(this)); // version code
            map1.put("gcmToken",Utility.getGcmToken(this));
            map1.put("customerName",Utility.getLocalName(this));
            map1.put("email",Utility.getLocalEmail(this)); // email
            map1.put("serialNumber",Utility.getSerialNumber()); // serialNumber
            map1.put("modelName",Utility.getDeviceModel()); // modelName
            map1.put("brandName",Utility.getDeviceBrand()); // brandName
            map1.put("mobile",mMobile); // mobile
            map1.put("isShop",Utility.getIsShop(this));
            map1.put("language",Utility.getDefaultLanguage(this));
            map1.put("otp",mOTP);
            map1.put("password",mPassword);
            map1.put("latitude" , Utility.getLatitude(this));
            map1.put("longitude" , Utility.getLongitude(this));
            map1.put("deviceUniqueKey",Utility.getDeviceUniqueKey(this)); // version code
            map1.put("refer",temRefer);
            map1.put("action",loginType);
            map1.put("androidApiVersion",Utility.getAndroidApiVersion()+"");

            map1.put("os",Utility.getOs(this));
            login(Utility.mapWrapper(this,map1));
        }


    }
    public void onClickResendOtp(String otpType){



        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(this)); // key
        map1.put("imei",Utility.getImei(this)); // imei
        map1.put("versionCode",Utility.getVersionCode(this)); // version code
        map1.put("email",Utility.getLocalEmail(this)); // email

        map1.put("customerName",Utility.getLocalName(this));
        map1.put("serialNumber",Utility.getSerialNumber()); // serialNumber
        map1.put("modelName",Utility.getDeviceModel()); // modelName
        map1.put("brandName",Utility.getDeviceBrand()); // brandName
        map1.put("mobile",mMobile); // mobile
        map1.put("action",loginType);
        map1.put("latitude" , Utility.getLatitude(this));
        map1.put("longitude" , Utility.getLongitude(this));
        map1.put("refer",referer);
        map1.put("androidApiVersion",Utility.getAndroidApiVersion()+"");
        map1.put("otpType",otpType);
        map1.put("deviceUniqueKey",Utility.getDeviceUniqueKey(this)); // version code
        map1.put("os",Utility.getOs(this));
        resendOtp(Utility.mapWrapper(this,map1));

    }

    private boolean validation() {

        mPassword = mBinding.editTextPassword.getText().toString().trim();
        mOTP = mBinding.editTextOTP.getText().toString().trim();
         if (mBinding.passLayout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(mPassword)) {
                Utility.showToast(this, "Please enter password.", "");
                return false;
            }

        } else if (mBinding.otpLayout.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(mOTP)) {
                Utility.showToast(this, "Please enter otp.","");
                return false;
            }
        }
        if(mPassword.equals("") || mPassword.isEmpty()){

        }
        else{
            mPassword = Utility.md5(mPassword);
        }

        return true;
    }

    private void resendOtp(String request){
        if(Utility.isNetworkConnected(this)){
            progressHelper.show();

            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_LOGIN, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseOtpResponse(result, responseManager);


                        }
                    });
        }
    }


    private void parseOtpResponse(String result , ResponseManager responseManager){
        if(!isDestroyed()){
            progressHelper.dismiss();
            if(Utility.isServerRespond(result)){
                GetLoginValidateResponse response = new Gson().fromJson(result, GetLoginValidateResponse.class);


                if (response.getStatus().equals("SUCCESS")) {

                    Utility.showToastLatest(this,
                            response.getResText(),"SUCCESS");


                }

                else {
                    Utility.showToastLatest(this,
                            response.getResText(),"ERROR");
                }
            }

            else{
                //Utility.showToast(this, getString(R.string.server_not_responding),"");
                startActivity(new Intent(this, ServerNotResponseActivity.class));

            }
        }
    }
    /**
     * Login from server
     */
    private void login(String request) {
        if (Utility.isNetworkConnected(this)) {

            //progressHelper.show();

            DatabaseHelper databaseHelper = new DatabaseHelper(OtpActivity.this);
            databaseHelper.deleteServiceTypeTable(); // Delete service type table
            //databaseHelper.deleteOrderIdTable(); // Delete order id table
            databaseHelper.deleteRechargeHistoryTable(); // Delete recharge history table
            databaseHelper.deleteTable("chat_support");


            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_LOGIN_VALIDATE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result, responseManager);

                            //Log.d("TEST","request - "+result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result, ResponseManager responseManager) {
        if (!isDestroyed()) {
            progressHelper.dismiss();

            if (Utility.isServerRespond(result/*, responseManager*/)) {
                //databaseTask task = new databaseTask(getApplicationContext());
                //task.execute(result, "login");

try {
    new Thread(new Runnable() {
        @Override
        public void run() {

            new databaseTask(OtpActivity.this).doInBackground(result, "login");
        }
    }).start();
}
catch (Exception e){
    
}



                try{
                    GetLoginValidateResponse response = new Gson().fromJson(result, GetLoginValidateResponse.class);

                    if (response.getResCode().equals(Constant.CODE_200)) {


                        Utility.setUsername(this,mMobile);

                        Utility.setUserLogin(this, true);

                        Utility.setToken(this, response.getData().getToken());


                        PreferenceConnector.writeString(this, PreferenceConnector.IMEI,
                                Utility.getImei(this));
                        // Set circle list in shared preference
                        Utility.setCircleList(OtpActivity.this, response.getData().getCircleList());


                        Intent intent = new Intent(OtpActivity.this,
                                HomeNewActivity.class);
                        progressHelper.show();

                        Utility.getServiceList(activity, "HomeUpdates",Constant.METHOD_HOME_UPDATE,false,"OtpActivity", new HomeUpdateListener() {
                            @Override
                            public void onDone() {
                                startActivity(intent);
                                //progressHelper.dismiss();
                                finish();
                            }
                        });

                        //startActivity(intent);
                        //finish();

                    }



                    else {
                        Utility.showToastLatest(this,
                                response.getResText(),"ERROR");
                    }
                }
                catch (Exception e){
                    Log.d("TEST","output  ex "+e.toString());
                }

            } else {
                //Utility.showToast(this, getString(R.string.server_not_responding),"");
                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }
            if (!isDestroyed()) {
                //progressHelper.dismiss();
                Log.d("progress helper", "hide");
            }

        }
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
        super.onDestroy();
    }
}
