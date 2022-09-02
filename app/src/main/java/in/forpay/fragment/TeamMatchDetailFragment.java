package in.forpay.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.forpaypremierleague.FPLMatchDetailsActivity;
import in.forpay.databinding.FragmentTeamMatchDetailBinding;
import in.forpay.databinding.MatchDetailLayoutBinding;
import in.forpay.event.BaseEvent;
import in.forpay.event.PlayerSelectEvent;
import in.forpay.model.response.GetMatchDetailResponse;
import in.forpay.model.response.TeamModel;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class TeamMatchDetailFragment extends Fragment {
    private final GetMatchDetailResponse response;
    private String groupId;
    private final List<TeamModel> list;
    private String result;
    FragmentTeamMatchDetailBinding binding;
    private ProgressHelper progressHelper;
    Activity activity;
    private int teamType = 0;
    private String isJoined="";
    private String onlinePlayer="";
    private String contestClosed="";
    private int fpl_captain=0;
    private int fpl_powerBatsman=0;
    private int fpl_bowler=0;
    private int fpl_powerBowler=0;
    private int fpl_batsman=0;

    public TeamMatchDetailFragment(List<TeamModel> teamModelList1, String result, String groupId, int teamType,String isJoined,String onlinePlayer,String contestClosed) {
        list = teamModelList1;
        this.result = result;
        response = new Gson().fromJson(result, GetMatchDetailResponse.class);
        this.groupId = groupId;
        this.teamType = teamType;
        this.isJoined=isJoined;
        this.onlinePlayer=onlinePlayer;
        this.contestClosed=contestClosed;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeamMatchDetailBinding.inflate(inflater, container, false);
        activity = getActivity();
        progressHelper = new ProgressHelper(activity);
        init();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        binding.fabTxt.setText(onlinePlayer + " Player");
        if (response.getIsJoined().equals("1") || response.getContestClosed().equals("1"))
            binding.btnMakeTeam.setVisibility(View.GONE);

        TeamMatchDetailAdapter adapter = new TeamMatchDetailAdapter(getActivity(), list);
        binding.rcvmatchdetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcvmatchdetail.setHasFixedSize(true);
        binding.rcvmatchdetail.setAdapter(adapter);
        binding.fabRs.setOnClickListener(v -> {
            FPLResult group = new FPLResult(groupId);
            group.show(getChildFragmentManager(), "pop");
        });
        binding.fabTxt.setOnClickListener(v -> {
            FPLPlayerList group = new FPLPlayerList(groupId);
            group.show(getChildFragmentManager(), "pop");
        });

    }


    private class TeamMatchDetailAdapter extends RecyclerView.Adapter<TeamMatchDetailAdapter.ViewHolder> {
        Activity activity;
        private List<TeamModel> list;

        public TeamMatchDetailAdapter(FragmentActivity activity, List<TeamModel> list) {
            this.activity = activity;
            this.list = list;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.match_detail_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PopupMenu popupMenu = new PopupMenu(activity, holder.binding.threedotimg);
            Glide.with(activity).load(list.get(position).getProfileUrl()).into(holder.binding.playericon);
            holder.binding.pname.setText(list.get(position).getName());
            if (list.get(position).getPoint().isEmpty()) {
                holder.binding.ppoint.setVisibility(View.GONE);
            } else {
                holder.binding.ppoint.setVisibility(View.VISIBLE);
            }


            if(isJoined.equals("1") || contestClosed.equals("1")){
                holder.binding.ppoint.setVisibility(View.VISIBLE);
                holder.binding.threedotimg.setVisibility(View.GONE);
                holder.binding.runwicket.setVisibility(View.VISIBLE);
                holder.binding.power.setText(list.get(position).getPlayerRole());
            }
            else{
                holder.binding.ppoint.setVisibility(View.GONE);
                holder.binding.runwicket.setVisibility(View.GONE);
                holder.binding.power.setText(list.get(position).getPlayerRole());
            }
            holder.binding.ppoint.setText(list.get(position).getPoint());
            holder.binding.runwicket.setText("Run " + list.get(position).getRun() + ", Wicket - " + list.get(position).getWicket());
            //holder.binding.power.setText(list.get(position).getPlayerRole());

            String roleName = "";
            List<TeamModel.SelectionOption> selectionOption = list.get(position).getSelectionOption();
            if (list.get(position).isSelected() || (list.get(position).getPlayerRole() != null && !list.get(position).getPlayerRole().isEmpty())) {
                popupMenu.getMenu().add(Menu.NONE, 0, 0, "Remove");
            }

            for (TeamModel.SelectionOption option : selectionOption) {
                popupMenu.getMenu().add(option.getTitle());
                if (list.get(position).getPlayerRole() != null && !list.get(position).getPlayerRole().isEmpty() && option.getKey().equals(list.get(position).getPlayerRole())){
                    roleName = option.getTitle();
                }
            }

            if (list.get(position).isSelected() /*|| (list.get(position).getPlayerRole() != null && !list.get(position).getPlayerRole().isEmpty())*/) {
                //When user already selected
                /* if (list.get(position).getPlayerRole() != null && !list.get(position).getPlayerRole().isEmpty()){
                    list.get(position).setSelected(true);
                    list.get(position).setSelectedRoleKey(list.get(position).getPlayerRole());
                    //When player role come from Api
                    list.get(position).setSelectedRole(roleName);
                    EventBus.getDefault().post(new PlayerSelectEvent(list.get(position), teamType, list.get(position).id, false, false));
                }*/
                holder.binding.power.setText(list.get(position).getSelectedRole());
                holder.binding.bgofplayer.setBackgroundResource(R.color.green_new_match);
            } else
                holder.binding.bgofplayer.setBackgroundResource(R.color.white);

            holder.binding.threedotimg.setOnClickListener(v -> {
                    //popupMenu.getMenu().add(Menu.NONE, 1, 1, "Share");
                    //popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    //Log.d("Checking_efedfdef1","clicked 1");
                    if (menuItem.getTitle().equals(list.get(position).getSelectedRole())) {
                        list.get(position).setSelected(false);
                    }

                    if (list.get(position).getPlayerRole() != null && !list.get(position).getPlayerRole().isEmpty()){
                        list.get(position).setPlayerRole("");
                    }

                    String roleKey = "";
                    for (TeamModel.SelectionOption option : selectionOption) {
                        if (option.getTitle().equals(menuItem.getTitle().toString())) {
                            roleKey = option.getKey();
                        }
                    }
                    SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.STORAGE_NAME, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    int increment=1;
                    if(menuItem.getTitle().equals("Remove")){
                        increment=-1;
                        roleKey=list.get(position).getSelectedRoleKey();
                    }
                    Log.d("Checking_efedfdef1","clicked 2 , role "+roleKey+" title "+menuItem.getTitle()+" "+list.get(position).getSelectedRoleKey()+" increment "+increment);

                    if(roleKey.equals("captain")){
                        fpl_captain++;
                        //Log.d("Checking_efedfdef1","captain increased");


                        editor.putInt("fpl_captain", sharedPreferences.getInt("fpl_captain", 0)+increment);


                    }
                    else if(roleKey.equals("batsman")){
                        fpl_batsman++;
                        editor.putInt("fpl_batsman", sharedPreferences.getInt("fpl_batsman", 0)+increment);

                    }
                    else if(roleKey.equals("powerBatsman")){
                        fpl_powerBatsman++;
                        editor.putInt("fpl_powerBatsman", sharedPreferences.getInt("fpl_powerBatsman", 0)+increment);

                    }
                    else if(roleKey.equals("bowler")){
                        fpl_bowler++;
                        editor.putInt("fpl_bowler", sharedPreferences.getInt("fpl_bowler", 0)+increment);

                    }
                    else if (roleKey.equals("powerBowler")){
                        fpl_powerBowler++;
                        editor.putInt("fpl_powerBowler", sharedPreferences.getInt("fpl_powerBowler", 0)+increment);

                    }


                    editor.apply();


                    //Check Roles Already added or not
                    int captainCount = 0;
                    int bowlerCount = 0;
                    int powerBowlerCount = 0;
                    int batsmanCount = 0;
                    int powerBatsmanCount = 0;

                    for (Map.Entry<String, TeamModel> entry: FPLMatchDetailsActivity.commanTeamList.entrySet()) {
                        TeamModel teamModel1 = entry.getValue();


                        switch (teamModel1.getSelectedRoleKey()) {
                            case "captain":
                                captainCount++;
                                updateCount("captain",captainCount);
                                break;
                            case "batsman":
                                batsmanCount++;
                                updateCount("batsman",captainCount);
                                break;
                            case "powerBatsman":
                                powerBatsmanCount++;
                                updateCount("powerBatsman",captainCount);
                                break;
                            case "bowler":
                                bowlerCount++;
                                updateCount("bowler",captainCount);
                                break;
                            case "powerBowler":
                                powerBowlerCount++;
                                updateCount("powerBowler",captainCount);
                                break;
                        }

                    }


                    TeamModel teamCommonModel = FPLMatchDetailsActivity.commanTeamList.get(list.get(position).getId());
                    if (FPLMatchDetailsActivity.commanTeamList.size() < 11 || menuItem.getTitle().equals("Remove") || teamCommonModel != null) {
                        TeamModel teamModel = null;

                        if (teamType == 1) {
                            teamModel = FPLMatchDetailsActivity.team1List.get(list.get(position).getId());
                        } else if (teamType == 2) {
                            teamModel = FPLMatchDetailsActivity.team2List.get(list.get(position).getId());
                        }

                        if (teamModel != null) {
                            if (menuItem.getTitle().equals("Remove")) {
                                if (teamType == 1) {
                                    FPLMatchDetailsActivity.team1List.remove(list.get(position).getId());
                                } else if (teamType == 2){
                                    FPLMatchDetailsActivity.team2List.remove(list.get(position).getId());
                                }
                                FPLMatchDetailsActivity.commanTeamList.remove(list.get(position).getId());
                                holder.binding.bgofplayer.setBackgroundResource(0);
                                list.get(position).setSelected(false);
                                holder.binding.power.setText("");
                                EventBus.getDefault().post(new PlayerSelectEvent(list.get(position), teamType, list.get(position).id, true, true));
                            } else {
                                if (!checkPlayersCount(captainCount, batsmanCount, bowlerCount, powerBatsmanCount, powerBowlerCount, roleKey)) {
                                    return false;
                                }

                                teamModel.setSelectedRoleKey(roleKey);
                                teamModel.setSelectedRole(menuItem.getTitle().toString());
                                holder.binding.power.setText(menuItem.getTitle());
                                list.get(position).setSelected(true);
                                list.get(position).setSelectedRole(menuItem.getTitle().toString());
                                list.get(position).setSelectedRoleKey(roleKey);
                                holder.binding.bgofplayer.setBackgroundResource(R.color.green_new_match);
                                EventBus.getDefault().post(new PlayerSelectEvent(list.get(position), teamType, list.get(position).id, false, true));
                            }
                        } else {
                           /* if (teamType == 1 && FPLMatchDetailsActivity.team1List.size() > 5) {
                                return false;
                            }
                            if (teamType == 2 && FPLMatchDetailsActivity.team2List.size() > 5) {
                                return false;
                            }*/

                            if (!checkPlayersCount(captainCount, batsmanCount, bowlerCount, powerBatsmanCount, powerBowlerCount, roleKey)) {
                                return false;
                            }

                            holder.binding.power.setText(menuItem.getTitle());
                            list.get(position).setSelected(true);
                            list.get(position).setSelectedRole(menuItem.getTitle().toString());
                            list.get(position).setSelectedRoleKey(roleKey);
                            holder.binding.bgofplayer.setBackgroundResource(R.color.green_new_match);
                            EventBus.getDefault().post(new PlayerSelectEvent(list.get(position), teamType, list.get(position).id, false, true));
                        }
                        notifyItemChanged(holder.getAbsoluteAdapterPosition());
                    } else {
                        Toast.makeText(activity, "You have already Selected 11 Players", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                });

                popupMenu.show();
            });
        }

        private void updateCount(String role,int count){
            //Log.d("Checking_efedfdef1","role swicth "+role+" count "+count);
        }
        private Boolean checkPlayersCount(int captain, int batsman, int bowler, int powerBatsman, int powerBowler, String role) {

            SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.STORAGE_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            switch (role) {
                case "captain":
                    if (captain >= 1) {
                        Utility.showToast(activity, "You have Already Selected 1 Captain");
                        editor.putInt("fpl_captain", sharedPreferences.getInt("fpl_captain", 0)-1);
                        editor.apply();
                        return false;
                    }
                    break;
                case "batsman":
                    if (batsman >= 3) {
                        Utility.showToast(activity, "You have Already Selected 3 Batsman");
                        editor.putInt("fpl_batsman", sharedPreferences.getInt("fpl_batsman", 0)-1);
                        editor.apply();
                        return false;
                    }
                    break;
                case "powerBatsman":
                    if (powerBatsman >= 2) {
                        Utility.showToast(activity, "You have Already Selected 2 Power Batsman");
                        editor.putInt("fpl_powerBatsman", sharedPreferences.getInt("fpl_powerBatsman", 0)-1);
                        editor.apply();
                        return false;
                    }
                    break;
                case "bowler":
                    if (bowler >= 3) {
                        Utility.showToast(activity, "You have Already Selected 3 Bowler");
                        editor.putInt("fpl_bowler", sharedPreferences.getInt("fpl_bowler", 0)-1);
                        editor.apply();
                        return false;
                    }
                    break;
                case "powerBowler":
                    if (powerBowler >= 2) {
                        Utility.showToast(activity, "You have Already Selected 2 Power Bowler");
                        editor.putInt("fpl_powerBowler", sharedPreferences.getInt("fpl_powerBowler", 0)-1);
                        editor.apply();
                        return false;
                    }
                    break;
            }
            return true;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            MatchDetailLayoutBinding binding;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                binding = MatchDetailLayoutBinding.bind(itemView);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayerEvent(BaseEvent event) {

    }
}