package in.forpay.adapter.supportchatAdapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.activity.forpaypremierleague.FPLChooseModeActivity;
import in.forpay.activity.forpaypremierleague.FPLMatchDetailsActivity;
import in.forpay.model.response.GetFPLResponse;
import in.forpay.model.response.GetPublicGroupResponse;

public class PublicGroupAdapter extends RecyclerView.Adapter<PublicGroupAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<GetPublicGroupResponse.Data> mList = new ArrayList<>();
    private String tname;
    private String matchAllModel;
    GetFPLResponse.Data response;

    public PublicGroupAdapter(Activity mActivity, ArrayList<GetPublicGroupResponse.Data> mList, String tname, String matchAllModel) {
        this.mActivity = mActivity;
        this.mList = mList;
        this.tname = tname;
        response = new Gson().fromJson(matchAllModel, GetFPLResponse.Data.class);
        this.matchAllModel = matchAllModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_group_match_new, parent, false);
        return new PublicGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.team1.setText(response.getTeam1());
        holder.team2.setText(response.getTeam2());
        holder.txtmatchid.setText("Match " + response.getMatchId());
        holder.groupname.setText(Html.fromHtml(mList.get(position).getGroupName()), TextView.BufferType.SPANNABLE);
        holder.matchpoint.setText(Html.fromHtml(mList.get(position).getJoiningPoints()), TextView.BufferType.SPANNABLE);
        holder.matchplayer.setText(Html.fromHtml(mList.get(position).getPlayers()), TextView.BufferType.SPANNABLE);

        holder.cardViewRunningMatch.setOnClickListener(v -> {
            mActivity.startActivity(new Intent(mActivity, FPLMatchDetailsActivity.class)
                    .putExtra("matchAllModel", matchAllModel)
                    .putExtra("groupId", mList.get(position).getGroupId()));
        });

    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView groupname, matchpoint, matchplayer, team1, team2, txtmatchid;
        CardView cardViewRunningMatch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            groupname = itemView.findViewById(R.id.group_name);
            txtmatchid = itemView.findViewById(R.id.txt_match);
            team1 = itemView.findViewById(R.id.txt_team1pgm);
            team2 = itemView.findViewById(R.id.txt_team2pgm);
            matchpoint = itemView.findViewById(R.id.match_points);
            matchplayer = itemView.findViewById(R.id.match_player);
            cardViewRunningMatch = itemView.findViewById(R.id.cardViewPublicGroup);

        }
    }
}
