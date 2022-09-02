package in.forpay.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.model.response.GetFPLResponse;

public class MatchRulesAdapter extends RecyclerView.Adapter<MatchRulesAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<GetFPLResponse.Rules> mList = new ArrayList<>();

    public MatchRulesAdapter(Activity activity, ArrayList<GetFPLResponse.Rules> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contest_rules, parent, false);
        return new MatchRulesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.matchRules.setText(Html.fromHtml(mList.get(position).getRule()), TextView.BufferType.SPANNABLE);

    }

    @Override
    public int getItemCount() {
        //Log.d("dsfsfr","fgfg"+mList.size());
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView matchRules;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            matchRules = itemView.findViewById(R.id.matchRules);
        }
    }
}
