package in.forpay.activity.smsrecharge;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.SMSRechargeAdapter;
import in.forpay.databinding.ActivitySmsrechargeBinding;
import in.forpay.model.response.GetSmsRechargeResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class SMSRechargeActivity extends AppCompatActivity {

    private ActivitySmsrechargeBinding binding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private SMSRechargeAdapter rechargeAdapter;
    private ArrayList<GetSmsRechargeResponse.Data> dataArrayList = new ArrayList<>();
    TextView recharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_smsrecharge);
        activity = this;
        progressHelper = new ProgressHelper(this);

        binding.backBtn.setOnClickListener(v -> finish());

        show(Boolean.TRUE);
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
            QueryManager.getInstance().postRequest(this, Constant.METHOD_SMS_RECHARGE, request, new CallbackListener() {
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

        GetSmsRechargeResponse response = new Gson().fromJson(result, GetSmsRechargeResponse.class);
        if (response.getResCode().equals(Constant.CODE_200)) {

            binding.rechargeMsg.setText(Html.fromHtml(response.getRemark()), TextView.BufferType.SPANNABLE);



        } else {
            Utility.showToastLatest(activity, response.getResText(), response.getResCode());

        }

        dataArrayList = response.getSmsOperator();
        rechargeAdapter();
    }

    private void rechargeAdapter() {
        binding.smsrechargeRecycler.setLayoutManager(new LinearLayoutManager(this));
        rechargeAdapter = new SMSRechargeAdapter(activity, dataArrayList);
        binding.smsrechargeRecycler.setAdapter(rechargeAdapter);
    }
}