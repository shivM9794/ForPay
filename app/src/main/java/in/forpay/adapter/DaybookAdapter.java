package in.forpay.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.LayoutYesterdaySummeryBinding;
import in.forpay.model.request.DayBook;

public class DaybookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Object> arrayList;

    public DaybookAdapter(Activity activity, ArrayList<Object> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
        //Log.d("database","adapter called"+arrayList.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.d("Database","viewhilder 1");
        View adView = LayoutInflater.from(activity).inflate(R.layout.layout_yesterday_summery, parent, false);
        return new YesterDayHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof YesterDayHolder){
            //Log.d("Database"," holder");
            YesterDayHolder holder1 = (YesterDayHolder)holder;
            DayBook dayBook = (DayBook)arrayList.get(position);

            holder1.adContainerViewBinding.txtProfit.setText("Profit "+dayBook.getProfit());
            holder1.adContainerViewBinding.txtPaidAmount.setText("Paid Amount "+dayBook.getCreditUsed());
            holder1.adContainerViewBinding.transactionSuccess.setText("Success "+dayBook.getSuccess());
            holder1.adContainerViewBinding.transactionPending.setText("Pending "+dayBook.getPending());
            holder1.adContainerViewBinding.transactionDisputed.setText("Disputed "+dayBook.getDisputed());
            holder1.adContainerViewBinding.transactionDate.setText(dayBook.getDate());


            //holder1.adContainerViewBinding.transactionAmount.setText(success);


        }


    }

    @Override
    public int getItemCount() {
        //Log.d("database","adapter size"+arrayList.size());
        return arrayList.size();
    }

    public  class YesterDayHolder extends RecyclerView.ViewHolder {
        LayoutYesterdaySummeryBinding adContainerViewBinding;

        public YesterDayHolder(@NonNull View itemView) {
            super(itemView);
            adContainerViewBinding = LayoutYesterdaySummeryBinding.bind(itemView);
        }
    }

}
