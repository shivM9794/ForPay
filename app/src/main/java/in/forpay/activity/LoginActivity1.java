package in.forpay.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import in.forpay.R;
import in.forpay.adapter.FavContactAdapter;
import in.forpay.adapter.SensitiveInfoAdapter;
import in.forpay.adapter.TshirtAddressAdapter;
import in.forpay.databinding.ActivityLogin1Binding;
import in.forpay.databinding.DialogWhyNeedPermissionBinding;
import in.forpay.model.response.GetFirstApiResponse;
import in.forpay.model.response.GetLoginResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.PreferenceConnector;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import in.forpay.webView.WebViewActivity;

public class LoginActivity1 extends AppCompatActivity {

    private ActivityLogin1Binding mBinding;
    private String mMobile = "";

    private ProgressHelper progressHelper;
    private String mImei = "";
    private String referer="";
    private String refererOriginal="";
    private String installReceiver="";
    private String loginActivityReferer="step1";
    private GetLoginResponse responseFinal;
    private String showRefer="";
    private SensitiveInfoAdapter sensitiveInfoAdapter;
    private ArrayList<GetFirstApiResponse.Data> dataPermission;

    private String loginType="";
    ProgressDialog dialog;

    GoogleSignInClient mGoogleSignInClient;
    Context context;
    InstallReferrerClient referrerClient;
    int LAUNCH_LANGUAGE_ACTIVITY = 1;
    int LAUNCH_ASK_SHOP=2;
    int LAUNCH_DISTRIBUTOR_DETAILS=3;
    int RESOLVE_EMAIL=101;


/*
    String[] permissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};


 */
/*
    String[] permissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS
    };


 */
    String[] permissions = new String[]{

            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setAppLocale(Utility.getDefaultLanguage(this), this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login1);
        mBinding.setActivity(this);
        progressHelper = new ProgressHelper(this);

        context = LoginActivity1.this;


        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if(!android_id.isEmpty() && android_id!=null ){
            if(!Utility.getLoginPermission(LoginActivity1.this).equals("yes")){
                permissions=null;
            }

        }


        if(Utility.getLocalEmail(this).isEmpty()) {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestProfile()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            requestHint();
        }

        InitiateRefferer();
        init();

        String tosText="Privacy Policy";
        String conditionText = "Conditions";
        String tos ="I Accept "+tosText+" and "+conditionText;


        SpannableString spannableString = new SpannableString(tos);
        ClickableSpan clickableSpanTos = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Log.e("tos","called");

                Intent intent = new Intent(LoginActivity1.this, WebViewActivity.class);
                intent.putExtra("url", "https://forpay.in/privacy.php");
                intent.putExtra("webName", "privacy");
                startActivity(intent);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

        };

        ClickableSpan clickableSpanCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                Intent intent = new Intent(LoginActivity1.this, WebViewActivity.class);
                intent.putExtra("url", "https://forpay.in/tos.php");
                intent.putExtra("webName", "tos");
                startActivity(intent);

            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

        };

        spannableString.setSpan(clickableSpanTos, tos.indexOf(tosText), tos.indexOf(tosText)+tosText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(clickableSpanCondition, tos.indexOf(conditionText), tos.indexOf(conditionText)+conditionText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mBinding.textViewTos.setText(spannableString);

        mBinding.textViewTos.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.textViewTos.setHighlightColor(Color.TRANSPARENT);



    }

    private void senstiveAdapter() {



    }


    private void requestHint() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setEmailAddressIdentifierSupported(true)
                .build();



        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    RESOLVE_EMAIL, null, 0, 0, 0);
        }
        catch (Exception e){
            Log.d("Phone","Error "+e.toString());
        }
    }

    private void init() {
//        progressHelper = new ProgressHelper(this);
        if (!checkAllPermissionsGranted()) {
            openWhyNeedPermissionDialog();
        }
        int AppView=Utility.getAppViewCount(this);
        if(AppView==0){
            //mBinding.refferLayout.setVisibility(View.VISIBLE);
        }
        else{
            //mBinding.refferLayout.setVisibility(View.GONE);

        }



        Utility.setAppViewCount(this,AppView+1);

        mBinding.editTextMobile.setText(Utility.getUsername(this));
        String tmpMobile=Utility.getUsername(this);
        if(tmpMobile.isEmpty()){
            tmpMobile = PreferenceConnector.readString(this, PreferenceConnector.USER_NAME, "");
            //Utility.showToastLatest(this,"Username is "+tmpMobile,"");

        }

        if(!tmpMobile.isEmpty()){
            mBinding.refferLayout.setVisibility(View.GONE);
            mBinding.editTextMobile.setText(tmpMobile);
            //mBinding.llTos.setVisibility(View.GONE);
        }




        try{
            SharedPreferences sharedPreferences = getSharedPreferences(Constant.STORAGE_NAME, Context.MODE_PRIVATE);
            installReceiver =sharedPreferences.getString("installReceiver", "");
            referer = sharedPreferences.getString("referer", "");

            refererOriginal =sharedPreferences.getString("refererOriginal","");

            if(referer.length()==7){
                mBinding.refferLayout.setVisibility(View.GONE);

            }
        }
        catch (Exception e){
            installReceiver="Exception loginactivity1 "+e.toString();
        }




    }

    private void openWhyNeedPermissionDialog(){

        //checkPermission();

        senstiveAdapter();
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_why_need_permission,null);
        DialogWhyNeedPermissionBinding bind=DialogWhyNeedPermissionBinding.bind(view);
        AlertDialog.Builder builder=new AlertDialog.Builder(context);

        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        GetFirstApiResponse getFirstApiResponse = new Gson().fromJson(Utility.getFirstApiResponse(this),GetFirstApiResponse.class);
        //Log.d("asdfgh","pojbfd"+getFirstApiResponse.getPermissionDetails().size());

        dataPermission=getFirstApiResponse.getPermissionDetails();
        if(dataPermission!=null) {

            try {
                RecyclerView sensitiveRecycler = view.findViewById(R.id.sensitiveRecycler);
                sensitiveRecycler.setLayoutManager(new LinearLayoutManager(this));
                sensitiveInfoAdapter = new SensitiveInfoAdapter(this, dataPermission);
                sensitiveRecycler.setAdapter(sensitiveInfoAdapter);

                dialog.show();
                bind.ok.setOnClickListener(v -> {
                    checkPermission();
                    dialog.dismiss();
                });

                bind.ignore.setOnClickListener(v -> {
                    dialog.dismiss();
                });
            }
            catch (Exception e){

            }
        }

    }

    /**
     * Check location permission
     */
    private void checkPermission() {


        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                mImei = Utility.getImei(LoginActivity1.this);
                Utility.callPingData(LoginActivity1.this);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                //Utility.showToast(PersonalDetailActivity.this, "Location permission is necessary");
            }
        };



        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                        "Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(permissions)
                .check();
    }

    private boolean checkAllPermissionsGranted() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        if(permissions!=null) {
            for (String p : permissions) {
                result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }

            if (!listPermissionsNeeded.isEmpty()) {
                return false;
            }
        }
        mImei = Utility.getImei(LoginActivity1.this);
        return true;
    }

    /**
     * Click on sign in
     */
    public void onClickSignIn() {
//Log.e("clicked","true");
        progressHelper.show();
        if (validation()) {


            if(referer!=null){
                if(referer.length()!=7){
                    if(mBinding.editTextReferId.getText()!=null) {
                        referer = mBinding.editTextReferId.getText().toString();
                    }
                    else{
                        referer="";
                    }
                }
            }


            if(referer==null){

            }
            else if(referer.isEmpty()){
                Utility.setReferId(this,referer);
            }

            Map<String,String> map1 = new HashMap<>();

            map1.put("token",Utility.getToken(this)); // key
            map1.put("imei",Utility.getImei(this)); // imei
            map1.put("versionCode",Utility.getVersionCode(this)); // version code

            map1.put("customerName",Utility.getLocalName(this));
            map1.put("email",Utility.getLocalEmail(this)); // email
            map1.put("serialNumber",Utility.getSerialNumber()); // serialNumber
            map1.put("modelName",Utility.getDeviceModel()); // modelName
            map1.put("brandName",Utility.getDeviceBrand()); // brandName
            map1.put("mobile",mMobile); // mobile
            map1.put("loginType",loginType);
            map1.put("latitude" , Utility.getLatitude(this));
            map1.put("longitude" , Utility.getLongitude(this));
            map1.put("refer",referer);
            map1.put("androidApiVersion",Utility.getAndroidApiVersion()+"");
            map1.put("refererOriginal",refererOriginal);
            map1.put("installReceiver",installReceiver);
            map1.put("loginActivityReferer",loginActivityReferer);
            map1.put("os",Utility.getOs(this));
            map1.put("deviceUniqueKey",Utility.getDeviceUniqueKey(this)); // version code
            map1.put("isShop",Utility.getIsShop(this));

            login(Utility.mapWrapper(this,map1));


        }




    }


    /**
     * Validation for required fields
     */
    private boolean validation() {
        mMobile = mBinding.editTextMobile.getText().toString().trim();

        if (TextUtils.isEmpty(mImei)) {
            Utility.showToast(this, "Please allow required permissions at setting.","");
            progressHelper.dismiss();
            return false;
        }
        if (TextUtils.isEmpty(mMobile)) {
            Utility.showToast(this, "Please enter mobile number.","");
            progressHelper.dismiss();
            return false;
        }

        if(!mBinding.checkBoxTOS.isChecked()){

            Utility.showToastLatest(this,"To use our service you must need to accept TOS","ERROR");
            progressHelper.dismiss();
            return false;
        }


        return true;
    }


    /**
     * Login from server
     */
    private void login(String request) {
        if (Utility.isNetworkConnected(this)) {
//            progressHelper.show();



            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_LOGIN, request, new CallbackListener() {
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



                //Log.d("TEST","output  responseed "+result);
                try{

                    GetLoginResponse response = new Gson().fromJson(result, GetLoginResponse.class);
                    responseFinal=response;
                    Utility.setIsShop(LoginActivity1.this,response.getData().getIsShop());
                    if (response.getData().getAction().equals(Constant.ACTION_OTP_ACTIVITY) || response.getData().getAction().equals(Constant.ACTION_PASSWORD_ACTIVITY)) {
//Received response from server and asked user to login via otp input
                        showRefer=response.getData().getShowRefer();
                        if(response.getData().getAction().equals(Constant.ACTION_OTP_ACTIVITY) && response.getData().getFirstTimeUser().equals("yes")){

                            //Intent intent = new Intent(LoginActivity1.this,LanguageChangeActivity.class);


                            Intent i = new Intent(this, LanguageChangeActivity.class);
                            i.putExtra("loginType", response.getData().getAction());
                            startActivityForResult(i, LAUNCH_LANGUAGE_ACTIVITY);


                        }
                        else {
                            callOtpActivity(response);
                        }
                        //Utility.showToastLatest(this,response.getResText(),"SUCCESS");

                    }


                    else {
                        Utility.showToastLatest(this,
                                response.getResText(),"ERROR");
                    }


                }
                catch (Exception e){
                    e.printStackTrace();
                    Utility.showToastLatest(this,
                            e.toString(),"ERROR");
                }

            } else {

                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }



        }
    }

    private void callOtpActivity(GetLoginResponse response){
        if(response!=null) {
            Intent intent = new Intent(LoginActivity1.this,
                    OtpActivity.class);
            intent.putExtra("loginType", response.getData().getAction());

            intent.putExtra("refer", referer);
            intent.putExtra("mobile", mMobile);
            intent.putExtra("locationRequired", response.getData().getLocationRequired());
            intent.putExtra("showRefer", showRefer);
            Utility.setHelpLine(this, response.getData().getHelpLine());
            Log.d("LanguageActivity", "startActivity");
            startActivity(intent);
            finish();
        }
        else{
            Utility.showToastLatest(LoginActivity1.this,"Unable to send call otp , Please try again","ERROR");
        }
    }
    void InitiateRefferer()

    {
        referrerClient = InstallReferrerClient.newBuilder(context).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        // Connection established.
                        GetRefferrData();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        loginActivityReferer="Api not available";
                        // API not available on the current Play Store app.
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        loginActivityReferer="connection error";
                        // Connection couldn't be established.
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                loginActivityReferer="service disconnected";
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    void GetRefferrData()

    {
        ReferrerDetails ref = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            ref = referrerClient.getInstallReferrer();

            if(!ref.getInstallReferrer().equals("") && ref.getInstallReferrer()!=null)

            {
                editor.putString("referer", ref.getInstallReferrer());
                editor.putString("refererOriginal",ref.getInstallReferrer());
                editor.apply();
                loginActivityReferer="referer found -"+ref.getInstallReferrer();
            }
            else{
                editor.putString("refererOriginal","LoginActivity1 empty");
                editor.apply();
                loginActivityReferer="LoginActivity1 empty";
            }
        } catch (Exception e) {
            editor.putString("refererOriginal","LoginActivity1 error"+e.toString());
            editor.apply();
            e.printStackTrace();
            loginActivityReferer="LoginActivity1 error "+e.toString();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d("LanguageActivity","result is "+requestCode+" "+resultCode+" "+Activity.RESULT_OK+" "+LAUNCH_LANGUAGE_ACTIVITY);
        if (requestCode == LAUNCH_LANGUAGE_ACTIVITY) {

            if(resultCode == Activity.RESULT_OK){
                //String result=data.getStringExtra("result");
                //callDistributorCheckActivity();
                //Log.d("LanguageActivity","result data "+result);


                Intent i = new Intent(this, DoYouHaveShopActivity.class);

                startActivityForResult(i, LAUNCH_ASK_SHOP);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
            //Log.d("LanguageActivity","result data 2" +Activity.RESULT_CANCELED);
        }

        else if(requestCode==LAUNCH_ASK_SHOP){

            if(Utility.getIsShop(this).equals("1") && showRefer.equals("yes")){
                Intent i = new Intent(this, DistributorDetailsActivity.class);
                i.putExtra("refer", referer);
                startActivityForResult(i, LAUNCH_DISTRIBUTOR_DETAILS);
            }
            else{
                if(!referer.isEmpty()){
                    Utility.setReferId(this,referer);
                }

                callOtpActivity(responseFinal);
            }


        }
        else if(requestCode==LAUNCH_DISTRIBUTOR_DETAILS){
            callOtpActivity(responseFinal);
        }
        else if (requestCode == RESOLVE_EMAIL) {
            Log.d("jsdgjasgjas",""+RESULT_OK+" ok "+resultCode+" data ");
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                Utility.setLocalEmail(this,credential.getId());
                Utility.setLocalName(this,credential.getName());
                Log.d("jsdgjasgjas","Response email "+credential.getId()+" Name "+credential.getName());


                String dataRes = "";

                Bundle bundle = data.getExtras();


                if (bundle != null) {
                    Set<String> keys = bundle.keySet();
                    Iterator<String> it = keys.iterator();

                    while (it.hasNext()) {
                        String key = it.next();
                        dataRes += "" + key + "=" + bundle.get(key) + "&";


                    }

                }

                Log.d("jsdgjasgjas","dataRes "+dataRes);
                // credential.getId(); <-- E.164 format phone number on 10.2.+ devices
            }
        }
    } //onActivityResult

}
