package in.forpay.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

import in.forpay.R;
import in.forpay.model.request.BalanceHistory;
import in.forpay.util.Utility;

public class BalanceHistoryAdapter extends RecyclerView.Adapter<BalanceHistoryAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<BalanceHistory> mList = new ArrayList<>();

    public BalanceHistoryAdapter(Activity activity, ArrayList<BalanceHistory> list) {
        this.mActivity = activity;
        this.mList = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.balance_history_item1, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.textViewDate.setText(mList.get(position).getDate());

        String openingBalance = mActivity.getString(R.string.rupee) + mList.get(position).getOpeningBal();
        viewHolder.textViewOpeningBalance.setText(openingBalance);

        String closingBalance = mActivity.getString(R.string.rupee) + mList.get(position).getClosingBal();
        viewHolder.textViewClosingBalance.setText(closingBalance);

        viewHolder.textViewOrderId.setText(mList.get(position).getOrderId());

        viewHolder.textViewStatus.setText(mList.get(position).getStatus());

        viewHolder.amountLabel.setText("Debit Amount: ");

        // viewHolder.viewLine.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.red_light));
        viewHolder.textViewStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.red_light));
        viewHolder.textViewStatus.setText("DEBITED");
        if (!mList.get(position).getAmount().contains("-")) {
            viewHolder.amountLabel.setText("Credit Amount: ");
            //  viewHolder.viewLine.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.green_light));
            viewHolder.textViewStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.green_light));
            viewHolder.textViewStatus.setText("CREDITED");
        }

        viewHolder.textViewAmount.setText(mList.get(position).getAmount());
        viewHolder.textViewMobile.setText(mList.get(position).getMobile());

        //viewHolder.textViewType.setText(mList.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewDate, textViewOrderId, textViewStatus, amountLabel, textViewAmount, textViewMobile;
        private TextView textViewOpeningBalance;
        private TextView textViewClosingBalance;
        private TextView textViewType;


        ViewHolder(@NonNull View view) {
            super(view);
            textViewDate = view.findViewById(R.id.textViewDate);
            textViewOpeningBalance = view.findViewById(R.id.textViewOpeningBalance);
            textViewClosingBalance = view.findViewById(R.id.textViewClosingBalance);
            textViewOrderId = view.findViewById(R.id.textViewOrderId);
            textViewStatus = view.findViewById(R.id.textViewStatus);
            amountLabel = view.findViewById(R.id.amountLabel);
            textViewAmount = view.findViewById(R.id.textViewAmount);
            textViewMobile = view.findViewById(R.id.textViewMobile);

            //textViewType = view.findViewById(R.id.textViewType);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mList.get(getAdapterPosition()).getOrderId().isEmpty()) {

                        historyDialog(mList.get(getAdapterPosition()));
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId", mList.get(getAdapterPosition()).getOrderId());
                        bundle.putString("viewType", "cc");
                        Utility.openTransactionDetailsActivity(mActivity, bundle);
                    }

                }
            });
        }
    }

    /**
     * Show history dialog
     */
    private void historyDialog(BalanceHistory data) {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_balance_history1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        TextView textViewOrderId = dialog.findViewById(R.id.textViewOrderId);

        TextView textViewTransId = dialog.findViewById(R.id.textViewTransId);
        TextView textViewStatus = dialog.findViewById(R.id.textViewStatus);
        TextView textViewMobile = dialog.findViewById(R.id.textViewMobile);
        TextView textViewService = dialog.findViewById(R.id.textViewService);
        TextView textViewAmount = dialog.findViewById(R.id.textViewAmount);
        TextView textViewBankAccount = dialog.findViewById(R.id.textViewBankAccount);
        ImageView operatoriv = dialog.findViewById(R.id.operatoriv);
        LinearLayout llStatus = dialog.findViewById(R.id.LLStatus);

        textViewOrderId.setText(data.getOrderId());

        textViewTransId.setText(data.getOptId());
        textViewStatus.setText(data.getStatus());


        textViewMobile.setText(data.getMobile());
        textViewService.setText(data.getService());
        textViewAmount.setText("  \u20B9 " + data.getAmount());
        textViewBankAccount.setText(data.getBankAccount());

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

        switch (data.getStatus()) {
            case "FAILED":
                llStatus.setBackgroundResource(R.drawable.border_failed);
                textViewStatus.setTextColor(mActivity.getResources().getColor(R.color.failed));
                break;
            case "SUCCESS":

                llStatus.setBackgroundResource(R.drawable.border_primary);
                textViewStatus.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                break;
            case "PENDING":
                llStatus.setBackgroundResource(R.drawable.border_pending);
                textViewStatus.setTextColor(mActivity.getResources().getColor(R.color.pending));
                break;
            case "DISPUTED":
                llStatus.setBackgroundResource(R.drawable.border_primary);
                textViewStatus.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                break;

            default:
                break;
        }


        Utility.imageLoader(mActivity, data.getOperatorIcon(), operatoriv);

    }

    private boolean isAssetExists(String pathInAssetsDir) {
        AssetManager assetManager = mActivity.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(pathInAssetsDir);
            if (null != inputStream) {
                return true;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return false;
    }
}
