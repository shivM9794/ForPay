package in.forpay.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityActiveDeviceBinding;
import in.forpay.model.request.GetKeyResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ActiveDeviceActivity extends AppCompatActivity {

    private ActivityActiveDeviceBinding mBinding;
    private String smsKey = "";
    private String mobile = "";
    private ProgressHelper progressHelper;

    String[] permissions = new String[]{
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_active_device);
        mBinding.setActivity(this);
        progressHelper = new ProgressHelper(this);
        Utility.checkLoggedUser(this);
        init();

        mBinding.verifySms.setOnClickListener(v -> {


            ClipboardManager cManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData cData = ClipData.newPlainText("text", smsKey);
            cManager.setPrimaryClip(cData);

            Toast.makeText(this, "Copied :)", Toast.LENGTH_SHORT).show();
        });

        mBinding.sendSms.setOnClickListener(v -> {
            Utility.showToast(this, "Sending Sms to " + mobile);
            //Log.d("GCasdsaM", "Permission clicked" + mobile + " message " + smsKey);
            //progressHelper.show();
            /*
            Uri uri = Uri.parse("smsto:"+mobile);
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra("sms_body", smsKey);
            startActivity(it);


             */


            //progressHelper.dismiss();

            //Log.d("GCasdsaM", "Permission not Granted" + mobile + " message " + smsKey);
            Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", mobile, null));
            smsIntent.putExtra("sms_body", smsKey);
            startActivity(smsIntent);


        });
    }

    private boolean checkAllPermissionsGranted() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissions != null) {
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

        return true;
    }

    /**
     * Click on active device button
     */
    public void onClickActiveDevice() {


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("email", Utility.getEmail(this)); // email
        map1.put("serialNumber", Utility.getSerialNumber()); // serialNumber
        map1.put("modelName", Utility.getDeviceModel()); // modelName
        map1.put("brandName", Utility.getDeviceBrand()); // brandName
        map1.put("deviceUniqueKey", Utility.getDeviceUniqueKey(this));


        String request = Utility.mapWrapper(this, map1);

        getKey(request);

    }

    private void init() {

    }

    /**
     * Check location permission
     */
    private void checkPermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

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
                .setPermissions(Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS)
                .check();
    }

    /**
     * Validation for required fields
     */

    /**
     * Get key
     */
    private void getKey(String request) {
        if (Utility.isNetworkConnected(ActiveDeviceActivity.this)) {
            //Log.d("TEST","-"+request);
            progressHelper.show();
            QueryManager.getInstance().postRequest(ActiveDeviceActivity.this,
                    Constant.METHOD_GET_KEY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result, ResponseManager responseManager) {
        if (!isDestroyed()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result/*, responseManager*/)) {
                GetKeyResponse response = new Gson().fromJson(result, GetKeyResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    smsKey = response.getSmsKey();
                    mBinding.linearLayout.setVisibility(View.VISIBLE);
//                    mobile = response.getMobile();
                    mBinding.mobileNumber.setText("+ " + response.getMobile());
                    mBinding.verifySms.setText(response.getSmsKey());
                    mBinding.sendSms.setVisibility(View.VISIBLE);

                } else {
                    Utility.showToast(ActiveDeviceActivity.this,
                            responseManager.getResText(), response.getResCode());
                }
            } else {
                Utility.showToast(ActiveDeviceActivity.this, getString(R.string.server_not_responding), "");

            }
        }
    }
}
