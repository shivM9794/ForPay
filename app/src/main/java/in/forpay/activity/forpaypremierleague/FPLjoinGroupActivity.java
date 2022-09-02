package in.forpay.activity.forpaypremierleague;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityFpljoinGroupBinding;
import in.forpay.model.response.GetFPLResponse;
import in.forpay.model.response.GetJoinGroupResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class FPLjoinGroupActivity extends AppCompatActivity {
    ActivityFpljoinGroupBinding binding;
    String gid = "";
    private ProgressHelper progressHelper;
    Activity activity;
    private ArrayList<GetFPLResponse.Data> data;
    private String matchAllModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fpljoin_group);
        activity = this;
        progressHelper = new ProgressHelper(activity);

        matchAllModel = getIntent().getExtras().getString("matchAllModel");

        binding.backBtn.setOnClickListener(v -> finish());
        binding.joinbtn.setOnClickListener(v -> {
            gid = binding.edtgid.getText().toString();
            if (gid.isEmpty())
                Toast.makeText(FPLjoinGroupActivity.this, "all fields are required", Toast.LENGTH_SHORT).show();
            else
                callJoinApi(true);
        });
    }

    private void callJoinApi(Boolean showProgressbar) {

        if (showProgressbar) {
            progressHelper.show();
        }

        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("latitude", Utility.getLatitude(activity));
        map1.put("longitude", Utility.getLatitude(activity));
        map1.put("activityName", "");
        map1.put("groupId", gid);

        String request = Utility.mapWrapper(this, map1);

        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_JOIN_GROUP, request, new CallbackListener() {
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
        GetJoinGroupResponse response = new Gson().fromJson(result, GetJoinGroupResponse.class);
        if (response.getResCode().equals(Constant.CODE_200)) {
            String matchId = response.getMatchId();
            startActivity(new Intent(activity, FPLMatchDetailsActivity.class).putExtra("matchAllModel",matchAllModel)
            .putExtra("groupId",gid));
            finish();

        }
        else {

            Utility.showToastLatest(activity, response.getResText(), response.getResCode());
        }
    }
}