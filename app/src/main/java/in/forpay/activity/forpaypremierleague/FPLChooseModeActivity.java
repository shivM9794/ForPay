package in.forpay.activity.forpaypremierleague;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.RunningMatchAdapter;
import in.forpay.adapter.supportchatAdapter.PublicGroupAdapter;
import in.forpay.databinding.ActivityFplactivityBinding;
import in.forpay.databinding.ActivityFplchooseModeBinding;
import in.forpay.fragment.FPLCreateGroup;
import in.forpay.model.response.GetFPLResponse;
import in.forpay.model.response.GetPublicGroupResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class FPLChooseModeActivity extends AppCompatActivity {
    private ActivityFplchooseModeBinding binding;
    private Activity activity;
    private String listData;
    private ArrayList<GetPublicGroupResponse.Data> data = new ArrayList<>();
    private ProgressHelper progressHelper;
    private PublicGroupAdapter publicGroupAdapter;
    private String tname, matchid,canJoin;
    private String matchAllModel;
    private GetFPLResponse.Data response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fplchoose_mode);
        activity = this;
        progressHelper = new ProgressHelper(activity);

        tname = getIntent().getExtras().getString("tname");
        listData = getIntent().getExtras().getString("matchModel");
        canJoin=getIntent().getExtras().getString("canJoin");
        //Log.d("fhdsad","canJoin"+canJoin);
        if(canJoin.equals("0")){
            binding.cardViewRunningMatch.setVisibility(View.GONE);
        }

        matchid = getIntent().getExtras().getString("matchid");
        matchAllModel = getIntent().getExtras().getString("matchAllModel");

        response = new Gson().fromJson(matchAllModel, GetFPLResponse.Data.class);

        data = new Gson().fromJson(listData, new TypeToken<List<GetPublicGroupResponse.Data>>() {
        }.getType());
        Log.d(".............. ", "onCreate: " + data);
//        binding.currentTeams.setText(tname);

        binding.txtMatchid.setText("Match "+response.getMatchId());
        binding.txtTeam1.setText(response.getTeam1());
        binding.txtTeam2.setText(response.getTeam2());

        binding.backBtn.setOnClickListener(v -> finish());
        binding.btnPlayFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, FPLjoinGroupActivity.class).putExtra("matchAllModel", matchAllModel));
            }
        });
        binding.btnCreategroup.setOnClickListener(v -> {
            FPLCreateGroup group = new FPLCreateGroup(matchid);
            group.show(getSupportFragmentManager(), "pop");
        });
        runningMatchAdapter();
    }


    private void runningMatchAdapter() {
        binding.publicGroupRecycler.setLayoutManager(new LinearLayoutManager(this));
        publicGroupAdapter = new PublicGroupAdapter(activity, data, tname, matchAllModel);
        binding.publicGroupRecycler.setAdapter(publicGroupAdapter);
    }

}