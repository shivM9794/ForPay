package in.forpay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class SplashActivity extends AppCompatActivity {
    private OxyMePref oxyMePref;
    private ProgressHelper progressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressHelper = new ProgressHelper(this);
        oxyMePref = new OxyMePref(this);
        oxyMePref.putBoolean(Constant.IS_SPLASH_OPEN, true);
        Utility.setOs(this, "Android");
        //Log.e("SplashActivity", "1");

        if (Utility.getEnckey(this).isEmpty() || Utility.getSid(this).isEmpty()) {
            //Log.e("SplashActivity", "2");
            Utility.setDeviceUniqueKey(this, Utility.getRandomString(16));
            ArrayList<String> list = new ArrayList<>();
            list.add("key=" + Utility.getToken(this)); // key
            list.add("imei=" + Utility.getImei(this)); // imei
            list.add("versionCode=" + Utility.getVersionCode(this)); // version code
            list.add("deviceUniqueKey=" + Utility.getDeviceUniqueKey(this)); // version code

            String request = Utility.dataWrapper(list);
            callFirstApi(request);

            //Log.d("LoginActivity","First api called");
        } else {
            //Log.e("SplashActivity", "3");
            openNextScreen();
            //Log.d("LoginActivity","First api called with key "+Utility.getEnckey(this));
        }


    }

    /**
     * Open next screen after handler
     */
    private void openNextScreen() {

        Log.e("SplashActivity", "4");
//        progressHelper.show();

        if (Utility.getUserLogin(this)) {

            //Utility.showToast(this,"Hello test");

            String serviceListLocation = "serviceList_HomeUpdates" + Constant.METHOD_HOME_UPDATE;
            if (oxyMePref.getString(serviceListLocation) == null || oxyMePref.getString(serviceListLocation).isEmpty()) {
                //Log.d("NavigationMenuError","Yesss"+oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE));
                Log.e("SplashActivity", "5");
                progressHelper.show();
                /*
                Utility.getHomeUpdate(this,"yes","login", new HomeUpdateListener() {
                    @Override
                    public void onDone() {
                        Log.e("NavigationMenuError","true");
                        progressHelper.dismiss();
                        Intent intent = new Intent(SplashActivity.this,
                                HomeNewActivity.class);
                        startActivity(intent);
                    }
                });


                 */
                Utility.getServiceList(this, "HomeUpdates", Constant.METHOD_HOME_UPDATE, false, "SplashActivity", new HomeUpdateListener() {
                    @Override
                    public void onDone() {


                        progressHelper.dismiss();
                        Intent intent = new Intent(SplashActivity.this,
                                HomeNewActivity.class);
                        startActivity(intent);
                    }
                });
            } else {
                Log.e("SplashActivity", "6");
                Intent intent = new Intent(SplashActivity.this,
                        HomeNewActivity.class);
                startActivity(intent);
            }
        } else {
            Log.e("SplashActivity", "7");
            Intent intent = new Intent(SplashActivity.this,
                    LoginActivity1.class);
            startActivity(intent);
        }

        finish();
    }

    private void callFirstApi(String request) {
        Log.e("SplashActivity", "8");
        Log.d("LoginActivity", "First api called request " + request);
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();

            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_FIRSTAPI, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            parseResponse(result);

                        }
                    });
        } else {
            Utility.showToastLatest(SplashActivity.this, "Internet not available", "ERROR");
        }

    }

    private void parseResponse(String result) {
        Log.e("SplashActivity", "9");
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {

            Log.d("LoginActivity", "First api called result " + result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.optString("resCode").equals(Constant.CODE_200)) {

                    Utility.setEnckey(SplashActivity.this, jsonObject.optString("publicKey"));
                    Utility.setSid(SplashActivity.this, jsonObject.optString("sid"));
                    Utility.setLoginPermission(SplashActivity.this,jsonObject.getString("permission"));

                    Utility.setFirstApiRespose(this,result);




                    openNextScreen();

                } else
                    Utility.showToastLatest(SplashActivity.this, jsonObject.getString("resText"), "ERROR");
            } catch (Exception jsonException) {
                Utility.showToastLatest(SplashActivity.this, jsonException.toString(), "ERROR");
            }


        } else {
            Utility.showToastLatest(SplashActivity.this, "Server Not Responding", "ERROR");
        }

    }


}
