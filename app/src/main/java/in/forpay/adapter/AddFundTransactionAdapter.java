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

import in.forpay.R;
import in.forpay.model.response.GetBalanceResponse;

public class AddFundTransactionAdapter extends RecyclerView.Adapter<AddFundTransactionAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<GetBalanceResponse.Data.PaymentHistory> mList = new ArrayList<>();

    public AddFundTransactionAdapter(Activity mActivity, ArrayList<GetBalanceResponse.Data.PaymentHistory> mList) {
        this.mActivity = mActivity;
        this.mList = mList;

//        Log.d("AddNewDetails", "details_transaction" + mList.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_aadfund_transactiondetails, parent, false);
        return new AddFundTransactionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textViewOrderId.setText(mList.get(position).getOrderId());
        holder.textViewRefNo.setText(mList.get(position).getRefNumber());
        holder.textViewDate.setText(mList.get(position).getDate());
        holder.textViewClosingBalance.setText("₹ "+mList.get(position).getAmount());
        holder.textViewAmount.setText(mList.get(position).getMethod());



    }

    @Override
    public int getItemCount() {

//        Log.d("AddNewDetails1", "details_transaction1" + mList.size());
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textOrderID, textViewOrderId, amountLabelRef, textViewRefNo, textViewDate, textViewAmount, textViewClosingBalance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textOrderID = itemView.findViewById(R.id.textOrderID);
            textViewOrderId = itemView.findViewById(R.id.textViewOrderId);
            amountLabelRef = itemView.findViewById(R.id.amountLabelRef);
            textViewRefNo = itemView.findViewById(R.id.textViewRefNo);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewClosingBalance = itemView.findViewById(R.id.textViewClosingBalance);
        }
    }
}
