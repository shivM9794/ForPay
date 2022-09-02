package in.forpay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.forpay.R;
import in.forpay.model.response.GetProfileResponse;
import in.forpay.util.Utility;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder> {

    private Activity mActivity;
    ArrayList<GetProfileResponse.IncentiveHistory> mList = new ArrayList<>();

    public TransactionHistoryAdapter(Activity mActivity, ArrayList<GetProfileResponse.IncentiveHistory> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_transcation_history_cashback, viewGroup, false);
        return new TransactionHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {


//        viewHolder.amount.setText("₹ "+mList.get(position).getAmount());
        viewHolder.description.setText(mList.get(position).getDescription());
        viewHolder.date.setText(mList.get(position).getDate());
        viewHolder.commission_bal.setText("₹ " + mList.get(position).getAmount());
        viewHolder.commissiontv1.setText("₹ " + mList.get(position).getClosingBal());
        Utility.imageLoader(mActivity, mList.get(position).getIconUrl(), viewHolder.icon);


        //Log.d("TransactionHistoryAda","Data "+mList.get(position).getAmount());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView amount;
        private TextView description;
        private CircleImageView icon;
        private TextView commissiontv1;
        private TextView commission_bal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            amount = itemView.findViewById(R.id.amount);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            commissiontv1 = itemView.findViewById(R.id.commissiontv1);
            commission_bal = itemView.findViewById(R.id.commission_bal);


        }
    }
}
