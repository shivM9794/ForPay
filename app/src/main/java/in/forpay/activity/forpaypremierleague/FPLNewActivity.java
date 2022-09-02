package in.forpay.activity.forpaypremierleague;

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
import in.forpay.adapter.RunningMatchAdapter;
import in.forpay.databinding.ActivityFplnewBinding;
import in.forpay.fragment.FPLMyConstest;
import in.forpay.fragment.FPLRules;
import in.forpay.model.response.GetFPLResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class FPLNewActivity extends AppCompatActivity {

    private ActivityFplnewBinding binding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private ArrayList<GetFPLResponse.Data> data = new ArrayList<>();
    private RunningMatchAdapter runningMatchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fplnew);

        activity = this;
        progressHelper = new ProgressHelper(activity);

        show(Boolean.TRUE);

        binding.backBtn.setOnClickListener(v -> finish());
        binding.btnPlayFriends.setOnClickListener(v -> {
            startActivity(new Intent(activity, FPLjoinGroupActivity.class).putExtra("matchAllModel", ""));


        });

        inits();


    }

    private void inits() {

        binding.txtRules.setOnClickListener(v -> {
            FPLRules fplRules = new FPLRules();
            fplRules.show(getSupportFragmentManager(), "pop");
        });
        binding.myContest.setOnClickListener(v -> {
            myContest(true);
        });
    }

    private void myContest(Boolean showProgressbar) {

        if (showProgressbar) {
            progressHelper.show();
        }
        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("latitude", Utility.getLatitude(activity));
        map1.put("longitude", Utility.getLongitude(activity));
        map1.put("activityName", "");

        String request = Utility.mapWrapper(activity, map1);
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_FPL_MYCONTESTS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            progressHelper.dismiss();
                            FPLMyConstest fplRules = new FPLMyConstest(activity, result);
                            fplRules.show(getSupportFragmentManager(), "pop");
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
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
            QueryManager.getInstance().postRequest(this, Constant.METHOD_FORPAY_PREMIER_LEAGUE, request, new CallbackListener() {
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
        if (Utility.isServerRespond(result)) {
            GetFPLResponse response = new Gson().fromJson(result, GetFPLResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {

                data = response.getData();
                runningMatchAdapter();
            } else {
                Utility.showToastLatest(activity, response.getResText(), response.getResCode());

            }
        } else {
            Utility.showToastLatest(activity, "Please try again", "ERROR");
        }

    }

    private void runningMatchAdapter() {
        binding.runningMatchRecycler.setLayoutManager(new LinearLayoutManager(this));
        runningMatchAdapter = new RunningMatchAdapter(activity, data);
        binding.runningMatchRecycler.setAdapter(runningMatchAdapter);
    }
}