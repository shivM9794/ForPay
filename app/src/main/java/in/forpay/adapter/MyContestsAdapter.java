package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.activity.forpaypremierleague.FPLMatchDetailsActivity;
import in.forpay.databinding.MyContestBinding;
import in.forpay.model.response.GetFPLMyContestResponse;
import in.forpay.model.response.GetFPLResponse;

public class MyContestsAdapter extends RecyclerView.Adapter<MyContestsAdapter.ViewHolder> {
    private Activity activity;
    private List<GetFPLMyContestResponse.DataBean> mList = new ArrayList<>();

    public MyContestsAdapter(Activity activity, List<GetFPLMyContestResponse.DataBean> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_contest, parent, false);
        return new MyContestsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyContestBinding bind = holder.binding;
        GetFPLMyContestResponse.DataBean dataBean = mList.get(position);
        String matchId = "Match " + dataBean.getMatchId();

        String groupName = "Group Name : " + dataBean.getGroupName();

        String subPoints =  dataBean.getPointsEarned();
        if (dataBean.getPointsEarned().isEmpty()) {
            //subPoints = "";
        }
        String points = subPoints;

        String rank = "Rank - #" + dataBean.getRank();
        
        bind.txtMatch.setText(matchId);
        bind.txtTeam1.setText(dataBean.getTeam1());
        bind.txtTeam2.setText(dataBean.getTeam2());
        bind.txtGname.setText(groupName);
        bind.txtDate.setText(Html.fromHtml(dataBean.getMatchDate()));
        bind.txtRank.setText(rank);
        bind.txtPoint.setText(points);

        bind.cardView.setOnClickListener(v -> {
            activity.startActivity(new Intent(activity, FPLMatchDetailsActivity.class).putExtra("matchAllModel","")
                    .putExtra("groupId",dataBean.getGroupId()));


        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MyContestBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = MyContestBinding.bind(itemView);
        }
    }
}
