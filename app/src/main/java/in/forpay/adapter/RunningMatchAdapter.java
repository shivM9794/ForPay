package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
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
import in.forpay.activity.forpaypremierleague.FPLChooseModeNewActivity;
import in.forpay.activity.forpaypremierleague.FPLInnerActivity;
import in.forpay.model.response.GetFPLResponse;

public class RunningMatchAdapter extends RecyclerView.Adapter<RunningMatchAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<GetFPLResponse.Data> mList = new ArrayList<>();

    public RunningMatchAdapter(Activity mActivity, ArrayList<GetFPLResponse.Data> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.running_matchnew, parent, false);
        return new RunningMatchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.matchDate.setText(Html.fromHtml(mList.get(position).getMatchTime()) + ", " + Html.fromHtml(mList.get(position).getMatchDate()), TextView.BufferType.SPANNABLE);
        holder.remarks.setText(Html.fromHtml(mList.get(position).getRemark()), TextView.BufferType.SPANNABLE);
        holder.team1.setText(mList.get(position).getTeam1());
        holder.team2.setText(mList.get(position).getTeam2());

        holder.cardViewRunningMatch.setOnClickListener(v -> {
             Intent intent = new Intent(mActivity, FPLChooseModeNewActivity.class)
                    .putExtra("tname", Html.fromHtml(mList.get(position).getMatchTitle()).toString())
                    .putExtra("matchid", mList.get(position).getMatchId())
                    .putExtra("matchModel", new Gson().toJson(mList.get(position).getPublicGdata()))
                    .putExtra("canJoin",mList.get(position).getCanJoin())
                    .putExtra("matchAllModel", new Gson().toJson(mList.get(position)));

            mActivity.startActivity(intent);

            Log.d("sdfsdss",""+new Gson().toJson(mList.get(position)));
        });

    }

    @Override
    public int getItemCount() {
        // Log.d("dsfsf","fgfg"+mList.size());
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView remarks, matchDate, team1, team2;
        CardView cardViewRunningMatch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            matchDate = itemView.findViewById(R.id.dateofrm);
            team1 = itemView.findViewById(R.id.txt_team1rm);
            team2 = itemView.findViewById(R.id.txt_team2rm);
            remarks = itemView.findViewById(R.id.remarks);
            cardViewRunningMatch = itemView.findViewById(R.id.cardViewRunningMatch);

        }
    }
}
