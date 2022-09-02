package in.forpay.activity.aeps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import in.forpay.R;
import in.forpay.model.response.GetAepsDetailResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class StartAepsActivity extends AppCompatActivity {

    //private ActivityAepsStartActivityBinding mBinding;
    private Activity activity;
    private ProgressHelper progressHelper;
    int REQ_CODE_AEPS = 1001;
    int AEPS_REQUEST_CODE = 10923;
    public static final String HMAC_SHA256 = "HmacSHA256";

    private int requestCode;

    private String value1 = "";
    private String value2 = "";
    private String value3 = "";
    private String value4 = "";
    private String value5 = "";
    private String value6 = "";
    private String value7 = "";
    private String value8 = "";
    private String value9 = "";
    private String value10 = "";
    private String value11 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Utility.setAppLocale(Utility.getDefaultLanguage(this), this);
        activity = StartAepsActivity.this;
        //mBinding= DataBindingUtil.setContentView(this, R.layout.activity_aeps_start_activity);
        //mBinding.setActivity(this);
        progressHelper = new ProgressHelper(activity);

        init();


    }


    private void init() {


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("location", Utility.getLatitude(StartAepsActivity.this) + "," + Utility.getLongitude(StartAepsActivity.this)); // amount

        String request = Utility.mapWrapper(this, map1);

        getAepsDetails(request);

    }

    private void getAepsDetails(String request) {
        if (in.forpay.util.Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_AEPS_DETAILS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);


                        }
                    });
        } else {
            in.forpay.util.Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }


    private void parseResponse(String result) {

        progressHelper.dismiss();

        if (in.forpay.util.Utility.isServerRespond(result)) {
            GetAepsDetailResponse response = new Gson().fromJson(result, GetAepsDetailResponse.class);


            if (response.getResCode().equals(Constant.CODE_200)) {

                value1 = response.getValue1();
                value2 = response.getValue2();
                value3 = response.getValue3();
                value4 = response.getValue4();
                value5 = response.getValue5();
                value6 = response.getValue6();
                value7 = response.getValue7();
                value8 = response.getValue8();
                value9 = response.getValue9();
                value10 = response.getValue10();
                if (response.getValue1().equals("payworld")) {
                    String headerKey = value2;
                    String bodyKey = value3;
                    Boolean receipt = false;
                    if (value4.equals("yes")) {
                        receipt = true;
                    }

/*
                    Intent intent = new Intent(StartAepsActivity.this, AepsHome.class);
                    Utility utility = Utility.getInstance();
                    intent.putExtra("header", utility.encryptHeader(getHeaderJson(), headerKey));
                    intent.putExtra("body", utility.encryptBody(getBodyJson(), bodyKey));
                    intent.putExtra("receipt", receipt);
                    startActivityForResult(intent, REQ_CODE_AEPS);

 */
                } else if (response.getValue1().equals("bankit")) {

                    /*
                    Intent i = new Intent(StartAepsActivity.this, AepsActivity.class);
                    i.putExtra("agent_id", response.getValue2());
                    i.putExtra("developer_id",response.getValue3());
                    i.putExtra("password", response.getValue4());
                    i.putExtra("primary_color", R.color.colorPrimary);
                    i.putExtra("accent_color", R.color.colorAccent);
                    i.putExtra("primary_dark_color", R.color.colorPrimaryDark);
                    i.putExtra("clientTransactionId", createMultipleTransactionID());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("bankVendorType", response.getValue5());
                    startActivityForResult(i,300);


                     */


                } else if (response.getValue1().equals("api")) {

                    Intent intent = new Intent(StartAepsActivity.this, StartAepsApiActivity.class);
                    activity.startActivity(intent);
                    finish();

                } else {
                    in.forpay.util.Utility.showToastLatest(activity, "No gateway found", "ERROR");
                }

            }
        } else {
            in.forpay.util.Utility.showToastLatest(activity, getString(R.string.server_not_responding), "ERROR");

        }
    }


    private String getHeaderJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merchantId", value5);
            //jsonObject.put("Timestamp", Utility.getCurrentTimeStamp());
            jsonObject.put("merchantKey", value6);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject.toString();
    }

    private String getBodyJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AgentId", value7);
            jsonObject.put("merchantService", value8);
            jsonObject.put("Version", value9);
            jsonObject.put("Mobile", value10);
            jsonObject.put("Email", value11);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject.toString();
    }


    private void postAepsResponseAfterCapture(String postData) {


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("response", postData); // email
        String request = Utility.mapWrapper(activity, map1);


        if (in.forpay.util.Utility.isNetworkConnected(activity)) {

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_AEPS_DETAILS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {


                        }
                    });
        } else {
            in.forpay.util.Utility.showToast(activity, getString(R.string.internet_connect), "");
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String dataRes = "";
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Set<String> keys = bundle.keySet();
                Iterator<String> it = keys.iterator();

                while (it.hasNext()) {
                    String key = it.next();
                    dataRes += "" + key + "=" + bundle.get(key) + "&";


                }

            }

        }
        postAepsResponseAfterCapture(dataRes);
        //in.forpay.util.Utility.showToastLatest(this,"Success...... ",""+requestCode+" - "+resultCode+" -"+data+" data res "+dataRes);
        Log.d("StartAepsActivity", "message is " + requestCode + " - " + resultCode + " -" + dataRes);
        if (requestCode == REQ_CODE_AEPS) {
            if (resultCode == RESULT_OK) {
                //success
            } else {
                //failed
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);

    }

    @Override
    public void finish() {
        super.finish();
    }
}
