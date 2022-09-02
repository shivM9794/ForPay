package in.forpay.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.forpay.R;
import in.forpay.model.response.GetPlayersResponse;
import in.forpay.util.Utility;

public class PlayersDetailsAdapter extends RecyclerView.Adapter<PlayersDetailsAdapter.ViewHolder> {
    private Activity mActivity;
    private ArrayList<GetPlayersResponse.Data> mList = new ArrayList<>();

    public PlayersDetailsAdapter(Activity mActivity, ArrayList<GetPlayersResponse.Data> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fpl_team1,parent,false);
        return new PlayersDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.playerName.setText(mList.get(position).getName());
        holder.playerRuns.setText(mList.get(position).getRun());
        holder.playerCategory.setText(mList.get(position).getPlayerRole());
        holder.playerPoints.setText(mList.get(position).getPoint());
        holder.playerWickets.setText(mList.get(position).getWicket());

        Utility.imageLoader(mActivity,mList.get(position).getProfileUrl(),holder.playerImg);

    }

    @Override
    public int getItemCount() {
       // Log.d("dgfgf","plg"+mList.size());
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView playerName,playerRuns,playerWickets,playerPoints,playerCategory;
        CircleImageView playerImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            playerName = itemView.findViewById(R.id.playerName);
            playerRuns = itemView.findViewById(R.id.playerRuns);
            playerWickets = itemView.findViewById(R.id.playerWickets);
            playerPoints = itemView.findViewById(R.id.playerPoints);
            playerCategory = itemView.findViewById(R.id.playerCategory);
            playerImg = itemView.findViewById(R.id.playerImg);
        }
    }
}
