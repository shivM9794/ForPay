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
import in.forpay.model.response.GetBankDetailsResponse;

public class BankDetailsAdapter extends RecyclerView.Adapter<BankDetailsAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<GetBankDetailsResponse.Data> mList = new ArrayList<>();

    public BankDetailsAdapter(Activity mActivity, ArrayList<GetBankDetailsResponse.Data> list) {
        this.mActivity = mActivity;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bank_details_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.textViewAccountName.setText(mList.get(position).getAcName());
        viewHolder.textViewAccountNumber.setText(mList.get(position).getAccountNo());
        viewHolder.textViewBankName.setText(mList.get(position).getBankName());
        viewHolder.textViewBranchName.setText(mList.get(position).getBranchName());
        viewHolder.textViewIFSCCode.setText(mList.get(position).getIfscCode());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewAccountName;
        private TextView textViewAccountNumber;
        private TextView textViewBankName;
        private TextView textViewBranchName;
        private TextView textViewIFSCCode;

        ViewHolder(@NonNull View view) {
            super(view);
            textViewAccountName = view.findViewById(R.id.textViewAccountName);
            textViewAccountNumber = view.findViewById(R.id.textViewAccountNumber);
            textViewBankName = view.findViewById(R.id.textViewBankName);
            textViewBranchName = view.findViewById(R.id.textViewBranchName);
            textViewIFSCCode = view.findViewById(R.id.textViewIFSCCode);
        }
    }
}