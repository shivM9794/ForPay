package in.forpay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.LayoutLastTransactionBinding;

public class DailySummeryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Object> arrayList;

    public DailySummeryAdapter(Activity activity, ArrayList<Object> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(activity).inflate(R.layout.layout_last_transaction, parent, false);
        return new TransactionHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class TransactionHolder extends RecyclerView.ViewHolder {
        LayoutLastTransactionBinding adContainerViewBinding;

        public TransactionHolder(@NonNull View itemView) {
            super(itemView);
            adContainerViewBinding = LayoutLastTransactionBinding.bind(itemView);
        }
    }

}
