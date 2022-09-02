package in.forpay.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.model.response.GetRequestHistoryResponse;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<GetRequestHistoryResponse.FundData> mList = new ArrayList<>();

    public HistoryAdapter(Activity activity, ArrayList<GetRequestHistoryResponse.FundData> list) {
        this.mActivity = activity;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.check_history_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.textViewId.setText(mList.get(position).getId());

        String amount = mActivity.getString(R.string.rupee) + mList.get(position).getAmount();
        viewHolder.textViewAmount.setText(amount);

        String status=mList.get(position).getStatus();
        switch (status){
            case "PENDING":
                viewHolder.textViewStatus.setText(mList.get(position).getStatus());
                viewHolder.textViewStatus.setBackgroundResource(R.drawable.border_pending);
                viewHolder.textViewStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.blue));
                break;
            case "APPROVED":
                viewHolder.textViewStatus.setText(mList.get(position).getStatus());
                viewHolder.textViewStatus.setBackgroundResource(R.drawable.border_primary);
                viewHolder.textViewStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.green_light));
                break;
            case "REJECTED":
                viewHolder.textViewStatus.setText(mList.get(position).getStatus());
                viewHolder.textViewStatus.setBackgroundResource(R.drawable.border_failed);
                viewHolder.textViewStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.red_light));
                break;
             default:

                viewHolder.textViewStatus.setText(mList.get(position).getStatus());
        }



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewId;
        TextView textViewAmount;
        TextView textViewStatus;

        ViewHolder(@NonNull View view) {
            super(view);

            textViewId = view.findViewById(R.id.textViewId);
            textViewAmount = view.findViewById(R.id.textViewAmount);
            textViewStatus = view.findViewById(R.id.textViewStatus);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    historyDialog(mList.get(getAdapterPosition()));
                }
            });
        }
    }

    /**
     * Show history dialog
     */
    private void historyDialog(GetRequestHistoryResponse.FundData data) {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_check_history);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        TextView textViewDate = dialog.findViewById(R.id.textViewDate);
        TextView textViewAccount = dialog.findViewById(R.id.textViewAccount);
        TextView textViewReference = dialog.findViewById(R.id.textViewReference);
        TextView textViewBank = dialog.findViewById(R.id.textViewBank);
        TextView textViewId = dialog.findViewById(R.id.textViewId);
        TextView textViewBefore = dialog.findViewById(R.id.textViewBefore);
        TextView textViewAfter = dialog.findViewById(R.id.textViewAfter);
        TextView textViewAmount = dialog.findViewById(R.id.textViewAmount);
        TextView textViewMode = dialog.findViewById(R.id.textViewMode);
        TextView textViewStatus = dialog.findViewById(R.id.textViewStatus);
        TextView textViewRemark = dialog.findViewById(R.id.textViewRemark);

        textViewDate.setText(data.getReqTime());
        textViewAccount.setText(data.getYourAccount());
        textViewReference.setText(data.getRefNumber());
        textViewBank.setText(data.getToBank());
        textViewId.setText(data.getId());
        textViewBefore.setText(data.getBeforeBalance());
        textViewAfter.setText(data.getAfterBalance());
        String amount = mActivity.getString(R.string.rupee) + data.getAmount();
        textViewAmount.setText(amount);
        textViewMode.setText(data.getMode());
        textViewStatus.setText(data.getStatus());
        textViewRemark.setText(data.getRemark());

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}