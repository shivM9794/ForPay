package in.forpay.activity.forpaypremierleague;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityFplmatchDetailsBinding;
import in.forpay.event.PlayerSelectEvent;
import in.forpay.fragment.TeamMatchDetailFragment;
import in.forpay.model.MakeTeamModel;
import in.forpay.model.Player;
import in.forpay.model.response.GetFPLResponse;
import in.forpay.model.response.GetMatchDetailResponse;
import in.forpay.model.response.TeamModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class FPLMatchDetailsActivity extends AppCompatActivity {
    ActivityFplmatchDetailsBinding binding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private String matchAllModel;
    //private GetFPLResponse.Data mList;
    private final List<TeamMatchDetailFragment> fragmentList = new ArrayList<TeamMatchDetailFragment>();
    private String groupId;
    public static final HashMap<String, TeamModel> commanTeamList = new HashMap<>();
    public static final HashMap<String, TeamModel> team1List = new HashMap<>();
    public static HashMap<String, TeamModel> team2List = new HashMap<>();
    private String joiningPoints="";
    private String referUrl="";
    int counter1,counter2,counter3,counter4,counter5;
    private String installReceiver="";
    private String showMatchingLayout="";

    boolean mStopHandler = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fplmatch_details);
        activity = this;
        commanTeamList.clear();
        team1List.clear();
        team2List.clear();
        //matchAllModel = getIntent().getExtras().getString("matchAllModel");
        groupId = getIntent().getExtras().getString("groupId");

        //mList = new Gson().fromJson(matchAllModel, GetFPLResponse.Data.class);

        progressHelper = new ProgressHelper(activity);
        binding.backBtn.setOnClickListener(v -> finish());

        callMatchApi(true);

        binding.btnMakeTeam.setOnClickListener(v -> callMakeTeamApi(true));
        binding.btnShare.setOnClickListener(v -> {
            {
                String message = "I want to play FPL with you!\n" +
                        "Room Code: " + groupId +"\n" +
                        "Joining Point: " + joiningPoints +"\n" +
                        "\nFPL Contest >Play With Friends > Enter Room code." +
                        "\nPlease install:\n" +
                        "Android:"+referUrl;

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(intent);
            }
        });
    }

    private void selection() {

        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("fpl_captain", 0);
        editor.putInt("fpl_batsman",0);
        editor.putInt("fpl_bowler",0);
        editor.putInt("fpl_powerBatsman",0);
        editor.putInt("fpl_powerBowler",0);
        editor.apply();
        Handler mHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int captain =sharedPreferences.getInt("fpl_captain", 0);


                if (captain==1){
                    binding.txtCaptain.setBackgroundResource(R.color.green_new);
                }
                else{
                    binding.txtCaptain.setBackgroundResource(R.drawable.fpl_player_selection);
                }
                int bowler = sharedPreferences.getInt("fpl_bowler",0);
                if (bowler==3){
                    binding.txtBowler.setBackgroundResource(R.color.green_new);
                }
                else{
                    binding.txtBowler.setBackgroundResource(R.drawable.fpl_player_selection);
                }
                int batsman = sharedPreferences.getInt("fpl_batsman",0);
                if (batsman==3){
                    binding.txtBatsman.setBackgroundResource(R.color.green_new);
                }
                else{
                    binding.txtBatsman.setBackgroundResource(R.drawable.fpl_player_selection);
                }
                int powerBatsman = sharedPreferences.getInt("fpl_powerBatsman",0);
                if (powerBatsman==2){
                    binding.txtPowerBatsman.setBackgroundResource(R.color.green_new);
                }
                else{
                    binding.txtPowerBatsman.setBackgroundResource(R.drawable.fpl_player_selection);
                }
                int powerBowler = sharedPreferences.getInt("fpl_powerBowler",0);
                if (powerBowler==2){
                    binding.txtPowerBowler.setBackgroundResource(R.color.green_new);
                }
                else{
                    binding.txtPowerBowler.setBackgroundResource(R.drawable.fpl_player_selection);
                }
                Log.d("Checking_efedfdef","captain "+captain);
                if (!mStopHandler) {
                    mHandler.postDelayed(this, 500);
                }
            }
        };

        mHandler.post(runnable);
    }

    private void callMakeTeamApi(Boolean showProgressbar) {

        if (showProgressbar) {
            progressHelper.show();
        }
        MakeTeamModel model = new MakeTeamModel();

        model.setToken(Utility.getToken(activity));
        model.setImei(Utility.getImei(activity));
        model.setVersionCode(Utility.getVersionCode(activity));
        model.setOs(Utility.getOs(activity));
        model.setLatitude(Utility.getLatitude(activity));
        model.setLongitude(Utility.getLatitude(activity));
        model.setActivityName("");
        model.setGroupId(groupId);
        ArrayList<Player> list = new ArrayList<>();

        for (Map.Entry<String, TeamModel> entry : commanTeamList.entrySet()) {
            String id = entry.getKey();
            TeamModel teamModel = entry.getValue();
            list.add(new Player(id, teamModel.getSelectedRoleKey()));
        }

        model.setPlayers(list);

       /* Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("latitude", Utility.getLatitude(activity));
        map1.put("longitude", Utility.getLatitude(activity));
        map1.put("activityName", "");
        map1.put("groupId", groupId);


        map1.put("players", new Gson().toJson(list));*/

        String request = Utility.mapWrapperJson(activity, new Gson().toJson(model));

        if (Utility.isNetworkConnected(activity)) {
            QueryManager.getInstance().postRequest(activity, Constant.METHOD_FPL_MAKETEAM, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {

                    if (showProgressbar) {
                        progressHelper.dismiss();
                    }
                    parseCashbackResponseDatamt(result);
                }
            });
        }
    }

    private void parseCashbackResponseDatamt(String result) {
        Log.d("............. ", "parseCashbackResponseData: " + result);
        try {
            JSONObject object = new JSONObject(result);
            String resText = object.getString("resText");
            String rescode = object.getString("resCode");

            if (resText.equals(Constant.CODE_200)) {
                Utility.showToastLatest(activity, resText, rescode);
//            data = response.getData();
//            runningMatchAdapter();
            } else {
                Utility.showToastLatest(activity, resText, rescode);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

        mStopHandler=true;
    }

    private void callMatchApi(Boolean showProgressbar) {

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
        map1.put("groupId", groupId);


        String request = Utility.mapWrapper(this, map1);

        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_MATCH_DETAILS, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {

                    if (showProgressbar) {
                        progressHelper.dismiss();
                    }
                    if (result != null && !result.isEmpty()) {
                        parseCashbackResponseData(result);
                    }
                }
            });
        }
    }

    private void parseCashbackResponseData(String result) {

        GetMatchDetailResponse response = new Gson().fromJson(result, GetMatchDetailResponse.class);
        Log.d("................", "parseCashbackResponseData: " + response);

        showMatchingLayout=response.getShowMatchingLayout();
        joiningPoints=response.getJoiningPoint();
        referUrl=response.getReferUrl();
        List<TeamModel> teamModelList1 = new ArrayList<>();
        List<TeamModel> teamModelList2 = new ArrayList<>();
        if (response.teamPlayers.get(response.getTeam1Name()) != null) {
            teamModelList1 = response.teamPlayers.get(response.getTeam1Name());
        } else {
            //teamModelList1 = response.teamPlayers.get("Lucknow Super Giants");
        }

        if (response.teamPlayers.get(response.getTeam2Name()) != null) {
            teamModelList2 = response.teamPlayers.get(response.getTeam2Name());
        } else {
            //teamModelList2 = response.teamPlayers.get("Mumbai Indians");
        }

        List<String> list = new ArrayList<>();
        TeamMatchDetailFragment fragment = new TeamMatchDetailFragment(teamModelList1, result, groupId, 1,response.isJoined,response.getOnlinePlayer(),response.getContestClosed());
        TeamMatchDetailFragment fragment1 = new TeamMatchDetailFragment(teamModelList2, result, groupId, 2,response.isJoined,response.getOnlinePlayer(),response.getContestClosed());
        fragmentList.clear();
        list.add(response.getTeam1Name());
        list.add(response.getTeam2Name());
        fragmentList.add(fragment);
        fragmentList.add(fragment1);
        teamModelAdapter adapter = new teamModelAdapter(getSupportFragmentManager(), fragmentList, list);
        binding.vpagermatchdetail.setAdapter(adapter);
        binding.tablayoutmatchdetail.setupWithViewPager(binding.vpagermatchdetail);
        if (response.getIsJoined().equals("1") || response.getContestClosed().equals("1")) {
            binding.btnMakeTeam.setVisibility(View.GONE);
            binding.fabTxtTeam1.setVisibility(View.GONE);
            binding.fabTxtTeam2.setVisibility(View.GONE);
        }
        binding.txtpoint.setText("Point - " + (response.getJoiningPoint().isEmpty() ? "0" : response.getJoiningPoint()));
        binding.txtRank.setText("Rank - #" + response.getRank());
        binding.todayTeamMatchTitle.setText("Room Code - " + groupId);
        binding.txtScore.setText("Score - " + response.getTotalPoint());

        /*int teamOneCount = 0;
        if (teamModelList1 != null) {
            for (TeamModel teamModel : teamModelList1) {
                if (teamModel.getPlayerRole() != null && !teamModel.getPlayerRole().isEmpty()) {
                    teamOneCount++;
                }
            }
        }

        int teamTwoCount = 0;
        if (teamModelList2 != null) {
            for (TeamModel teamModel : teamModelList2) {
                if (teamModel.getPlayerRole() != null && !teamModel.getPlayerRole().isEmpty()) {
                    teamTwoCount++;
                }
            }
        }

        binding.fabTxtTeam1.setText(String.valueOf(teamOneCount));
        binding.fabTxtTeam2.setText(String.valueOf(teamTwoCount));*/


        if(showMatchingLayout.equals("yes")) {
            selection();
            binding.selection.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayerEvent(PlayerSelectEvent event) {
        // Do something
        if (!event.isRemove()) {
            commanTeamList.put(event.getId(), event.getTeamModel());
            if (event.getTeam() == 1) {
                team1List.put(event.getId(), event.getTeamModel());
            } else {
                team2List.put(event.getId(), event.getTeamModel());
            }
        }

        if (event.isCountUpdate()) {
            binding.fabTxtTeam1.setText(String.valueOf(team1List.size()));
            binding.fabTxtTeam2.setText(String.valueOf(team2List.size()));
        }
    }
    public void onPause() {

        super.onPause();
        mStopHandler=true;
    }
}


class teamModelAdapter extends FragmentPagerAdapter {

    private List<TeamMatchDetailFragment> fragmentList;
    private List<String> list;

    public teamModelAdapter(@NonNull FragmentManager fm, List<TeamMatchDetailFragment> fragmentList, List<String> list) {
        super(fm);
        this.fragmentList = fragmentList;
        this.list = list;
    }

    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}