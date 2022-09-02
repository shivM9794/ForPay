package in.forpay.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.database.DatabaseHelper;
import in.forpay.model.request.RechargeHistory;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class RechargeHistoryAdapter2 extends RecyclerView.Adapter<RechargeHistoryAdapter2.ViewHolder> {

    private Activity mActivity;
    private ArrayList<RechargeHistory> mList = new ArrayList<>();
    private ProgressHelper progressHelper;
    private DatabaseHelper databaseHelper;
    private String mIsShop = "";

    public RechargeHistoryAdapter2(Activity activity, ArrayList<RechargeHistory> list, String isShop) {
        this.mActivity = activity;
        this.mList = list;
        this.progressHelper = new ProgressHelper(mActivity);
        this.mIsShop = isShop;
    }

    public ArrayList<RechargeHistory> getmList() {
        return mList;
    }

    public void addList(ArrayList<RechargeHistory> rechargeHistories, String isShop) {
        if (rechargeHistories != null && !rechargeHistories.isEmpty()) {

            Log.e("before", "" + mList.size());
            Log.e("new", "" + rechargeHistories.size());
            mList.addAll(rechargeHistories);

            Log.e("after", "" + mList.size());
            notifyDataSetChanged();

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recharge_history_item2, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        String status = mList.get(position).getStatus();


        switch (status) {
            case "FAILED":
                viewHolder.orderStatus.setText("Your order is failed");
                viewHolder.orderStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.red_light));

                break;
            case "PENDING":

                viewHolder.orderStatus.setText("Your order is pending");

                viewHolder.orderStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.yellow_light));

                break;
            case "SUCCESS":
                viewHolder.orderStatus.setText("Your order is successful");
                viewHolder.orderStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.green_light));

                break;
            case "DISPUTED":
                viewHolder.orderStatus.setText("Your order is Disputed");
                viewHolder.orderStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.blue_light));

                break;
            default:
                //viewHolder.textViewRefund.setVisibility(View.GONE);
                //viewHolder.textViewReprocess.setVisibility(View.GONE);
                break;
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("orderId", mList.get(position).getOrderId());
                bundle.putString("viewType", "cc");
                Utility.openTransactionDetailsActivity(mActivity, bundle);
            }
        });

        String dateTime = mList.get(position).getReqTime();
        viewHolder.textViewDatetime.setText(dateTime);


        String orderId = mList.get(position).getOrderId();
        viewHolder.orderId.setText("Order Id " + orderId);

        String mobile = mList.get(position).getMobile();

        String amount = mActivity.getString(R.string.rupee) + mList.get(position).getAmount();

        viewHolder.orderPrice.setText(amount);

        String operator = mList.get(position).getService();


        viewHolder.orderDescription.setText("Transaction for " + operator + " on " + mobile);
        Utility.imageLoader(mActivity, mList.get(position).getOperatorIcon(), viewHolder.operatorIcon);


        String profitView = mList.get(position).getProfit();

        viewHolder.commissiontv.setText(profitView);

        Log.d("Profitxxcc", "Profit " + profitView);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout LinearLayoutCopy, LLItemView;
        private CardView cardView;
        private TextView orderId;

        private TextView orderDescription;
        private TextView textViewDatetime, orderPrice, orderStatus, commissiontv;

        public ImageView operatorIcon;


        ViewHolder(@NonNull View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            orderId = view.findViewById(R.id.orderId);

            orderDescription = view.findViewById(R.id.orderDescription);
            textViewDatetime = view.findViewById(R.id.textViewDatetime);
            orderPrice = view.findViewById(R.id.orderPrice);
            orderStatus = view.findViewById(R.id.orderStatus);
            operatorIcon = view.findViewById(R.id.operatorIcon);
            commissiontv = view.findViewById(R.id.commissiontv);
            LinearLayoutCopy = view.findViewById(R.id.LinearLayoutCopy);
            LLItemView = view.findViewById(R.id.LLItemView);


            LLItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putString("orderId", mList.get(getAbsoluteAdapterPosition()).getOrderId());
                    bundle.putString("viewType", "cc");
                    Utility.openTransactionDetailsActivity(mActivity, bundle);
                }
            });


        }
    }


}
