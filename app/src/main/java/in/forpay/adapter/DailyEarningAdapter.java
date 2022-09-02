package in.forpay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.LayoutDailyEarningBinding;
import in.forpay.model.request.DayBook;

public class DailyEarningAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Object> arrayList;

    public DailyEarningAdapter(Activity activity, ArrayList<Object> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(activity).inflate(R.layout.layout_daily_earning, parent, false);
        return new DailyEarningHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof DailyEarningHolder){
            //Log.d("Database"," holder");
            DailyEarningHolder holder1 = (DailyEarningHolder)holder;
            DayBook dayBook = (DayBook)arrayList.get(position);

            holder1.adContainerViewBinding.txtProfit.setText("Net Profit "+dayBook.getProfit()+" "+activity.getResources().getString(R.string.rupees));
            holder1.adContainerViewBinding.txtPaidAmount.setText("Paid Amount "+dayBook.getCreditUsed());
            holder1.adContainerViewBinding.transactionSuccess.setText("Success "+dayBook.getSuccess());
            holder1.adContainerViewBinding.transactionPending.setText("Pending "+dayBook.getPending());
            holder1.adContainerViewBinding.transactionDisputed.setText("Disputed "+dayBook.getDisputed());
            holder1.adContainerViewBinding.cusCreditUsed.setText("Paid "+dayBook.getCusCreditUsed());
            holder1.adContainerViewBinding.transactionDate.setText(dayBook.getDate());


            //holder1.adContainerViewBinding.transactionAmount.setText(success);


        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class DailyEarningHolder extends RecyclerView.ViewHolder {
        LayoutDailyEarningBinding adContainerViewBinding;

        public DailyEarningHolder(@NonNull View itemView) {
            super(itemView);
            adContainerViewBinding = LayoutDailyEarningBinding.bind(itemView);
        }
    }

}
