package in.forpay.activity;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import in.forpay.R;

import in.forpay.adapter.LoginHistoryAdapter;

import in.forpay.databinding.ActivityLoginHistoryBinding;
import in.forpay.model.LoginHistoryModel;
import in.forpay.model.response.GetLoginHistoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class LoginHistoryActivity extends AppCompatActivity {

    private ActivityLoginHistoryBinding mBinding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private LoginHistoryAdapter adapter;
    private ArrayList<LoginHistoryModel> listData = new ArrayList<>();

    public static LoginHistoryActivity newInstance() {
        return new LoginHistoryActivity();
    }

    public LoginHistoryActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = LoginHistoryActivity.this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_history);
        progressHelper = new ProgressHelper(activity);
        init();
    }


    private void init() {
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());


        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));

        String request = Utility.mapWrapper(this,map1);


        getLoginHistory(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    /**
     * Set data on UI
     */
    private void setData(ArrayList<GetLoginHistoryResponse.DeviceList> list) {
        if (list.size() == 0) {
            Utility.inflateNoDataFoundLayout(activity, mBinding.inflateLayout, "No Result Found");
            return;
        }

        listData = new ArrayList<>();

        for (GetLoginHistoryResponse.DeviceList data: list){
            LoginHistoryModel usModel = new LoginHistoryModel();
            usModel.setActiveTime(data.getActiveTime());
            usModel.setImei(data.getImei());
            usModel.setLoginTime(data.getLoginTime());


            listData.add(usModel);
        }

        mBinding.deviceListRecycle.setLayoutManager(new LinearLayoutManager(activity));
        adapter =new LoginHistoryAdapter(activity,listData);
        mBinding.deviceListRecycle.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void getLoginHistory(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_LOGINHISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
        progressHelper.dismiss();
        //mBinding.progressBar.setVisibility(View.GONE);
        if (Utility.isServerRespond(result)) {
            GetLoginHistoryResponse response = new Gson().fromJson(result, GetLoginHistoryResponse.class);

            if (response.getResCode().equals(Constant.CODE_200)) {
                if(response.getDeviceList().size()>0){
                    //Log.d("Database ","here1");
                    setData(response.getDeviceList());

                }

            } else {
                if (response.getResCode().equals("129")){
                    Utility.inflateNoDataFoundLayout(activity, mBinding.inflateLayout, "No Result Found");
                }else {
                    Utility.showToast(activity, response.getResText(),"");
                }

            }
        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(this, ServerNotResponseActivity.class));

        }
    }
}
