package in.forpay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.model.response.GetMatchDetailsResponse;

public class Team1Adapter extends RecyclerView.Adapter<Team1Adapter.ViewHolder> {

    private Activity activity;
    private ArrayList<GetMatchDetailsResponse.Data> mList = new ArrayList<>();

    public Team1Adapter(Activity activity, ArrayList<GetMatchDetailsResponse.Data> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fpl_team1,parent,false);
        return new Team1Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.playerName.setText(mList.get(position).getName());
        holder.playerRuns.setText(mList.get(position).getRun());
        holder.playerWickets.setText(mList.get(position).getWicket());
        holder.playerPoints.setText(mList.get(position).getPoint());
        holder.playerCategory.setText(mList.get(position).getPlayerRole());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView playerName,playerRuns,playerWickets,playerPoints,playerCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.playerName);
            playerRuns = itemView.findViewById(R.id.playerRuns);
            playerWickets = itemView.findViewById(R.id.playerWickets);
            playerPoints = itemView.findViewById(R.id.playerPoints);
            playerCategory = itemView.findViewById(R.id.playerCategory);

        }
    }
}
