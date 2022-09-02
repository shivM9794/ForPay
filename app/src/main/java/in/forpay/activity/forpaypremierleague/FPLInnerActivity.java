package in.forpay.activity.forpaypremierleague;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.FPLMatchAdapter;
import in.forpay.adapter.OneWayAdapter;
import in.forpay.adapter.PlayersDetailsAdapter;
import in.forpay.databinding.ActivityFplinnerBinding;
import in.forpay.fragment.FPLRules;
import in.forpay.fragment.MakeTeams;
import in.forpay.model.response.GetFPLResponse;
import in.forpay.model.response.GetMatchDetailsResponse;
import in.forpay.model.response.GetPlayersResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class FPLInnerActivity extends AppCompatActivity {

    private ActivityFplinnerBinding binding;
    private Activity activity;
    private ArrayList<GetPlayersResponse.Data> teamPlayers = new ArrayList<>();
    private PlayersDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fplinner);
        activity = this;

        binding.backBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(getApplicationContext(),FPLActivity.class);
//            startActivity(intent);
            finish();
        });

        inits();

//        show();
    }

    private void inits() {

        binding.txtTeam.setOnClickListener(v -> {
            MakeTeams makeTeams = new MakeTeams();
            makeTeams.show(getSupportFragmentManager(), "pop");
        });
    }

    /*private void show() {

        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this, map1);

        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_MATCH_DETAILS, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {

                    parsePlayerResponseData(result);
                }
            });
        }
    }

    private void parsePlayerResponseData(String result) {

        GetPlayersResponse response = new Gson().fromJson(result, GetPlayersResponse.class);
        if (response.getResCode().equals(Constant.CODE_200)) {


        } else {

            Utility.showToastLatest(this, response.getResText(), response.getResCode());

        }

        teamPlayers = response.getTeamPlayers();
        adapter();
    }

    private void adapter() {

        binding.teamRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlayersDetailsAdapter(activity,teamPlayers);
        binding.teamRecycler.setAdapter(adapter);
    }

     */


}