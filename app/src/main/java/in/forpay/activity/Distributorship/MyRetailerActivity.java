package in.forpay.activity.Distributorship;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.MyRetailerAdapter;
import in.forpay.databinding.ActivityMyRetailerBinding;
import in.forpay.model.response.GetMyRetailerResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class MyRetailerActivity extends AppCompatActivity {

    private ActivityMyRetailerBinding binding;
    private Activity activity;
    ProgressHelper progressHelper;
    private ArrayList<GetMyRetailerResponse.DataList> dataLists = new ArrayList<>();
    private MyRetailerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_retailer);
        activity = this;
        progressHelper = new ProgressHelper(this);

        binding.backBtn.setOnClickListener(v -> finish());

        show(Boolean.TRUE);

        Utility.getCurrentLocation(activity, true);
    }
    private void show(Boolean showProgressbar) {

        if (showProgressbar) {
            progressHelper.show();

        }
        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this, map1);


        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_DIS_MYRETAILER, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {

                    if (showProgressbar) {
                        progressHelper.dismiss();
                    }
                    parseCashbackResponseData(result);
                }
            });
        }

    }

    private void parseCashbackResponseData(String result) {

        GetMyRetailerResponse response = new Gson().fromJson(result, GetMyRetailerResponse.class);
        if (response.getResCode().equals(Constant.CODE_200)) {

        }
        else {
            Utility.showToastLatest(activity, response.getResText(), response.getResCode());

        }

        dataLists = response.getDataList();
        retailerAdapter();
    }

    private void retailerAdapter() {

        binding.recyclerRetailerDetails.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new MyRetailerAdapter(activity, dataLists);
        binding.recyclerRetailerDetails.setAdapter(adapter);
    }


    public void claimRetailer(View view) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MyRetailerActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.retailer_bottomsheet, (LinearLayout) findViewById(R.id.bottomSheetContainer));
        bottomSheetView.findViewById(R.id.closeBtn).setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetView.findViewById(R.id.submitRetailer).setOnClickListener(v -> {

            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("versionCode", Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));
            map1.put("latitude", Utility.getLatitude(activity));
            map1.put("longitude", Utility.getLongitude(activity));
            EditText editOtp=bottomSheetView.findViewById(R.id.retailerMobileOTP);
            EditText editEmail=bottomSheetView.findViewById(R.id.retailerEmail);
            EditText editMobile=bottomSheetView.findViewById(R.id.retailerMobile);
            String otp=editOtp.getText().toString();
            String retailerEmail=editEmail.getText().toString();
            String retailerMobile=editMobile.getText().toString();
            map1.put("otp",otp);
            map1.put("retailerEmail",retailerEmail);
            map1.put("retailerMobile",retailerMobile);
            String request = Utility.mapWrapper(this, map1);

            progressHelper.show();
            if (Utility.isNetworkConnected(this)) {
                QueryManager.getInstance().postRequest(this, Constant.METHOD_DIS_CAILM_MYRETAILER, request, new CallbackListener() {
                    @Override
                    public void onResult(Exception e, String result, ResponseManager responseManager) {
                        progressHelper.dismiss();
                        parseClaimResponseData(result,bottomSheetView);
                    }
                });
            }




        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void parseClaimResponseData(String result,View bottomSheetView){
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.getString("resCode").equals("101")){
                bottomSheetView.findViewById(R.id.retailerOTPTextView).setVisibility(View.VISIBLE);
                bottomSheetView.findViewById(R.id.retailerMobileOTP).setVisibility(View.VISIBLE);
                Utility.showToastLatest(activity,jsonObject.getString("resText"),jsonObject.getString("resCode"));
            }
            else{
                 Utility.showToastLatest(activity,jsonObject.getString("resText"),jsonObject.getString("resCode"));

            }
        }
        catch (Exception e){

        }
    }
}