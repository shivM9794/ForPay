package in.forpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.adapter.RecentTransactionAdapter;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityRecentTransactionBinding;
import in.forpay.model.request.RechargeHistory;
import in.forpay.util.ProgressHelper;

public class RecentTransactionActivity extends AppCompatActivity {

    private ActivityRecentTransactionBinding binding;
    private Activity activity;
    private ArrayList<Object> arrayList = new ArrayList<>();
    private RecentTransactionAdapter recentTransactionAdapter;
    private DatabaseHelper databaseHelper;
    private ProgressHelper progressHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = RecentTransactionActivity.this;
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_recent_transaction);
        progressHelper = new ProgressHelper(activity);
        binding.backBtn.setOnClickListener(v -> onBackPressed());

        init("");
    }



    private void init(String mobileSearch) {
        progressHelper = new ProgressHelper(activity);
        databaseHelper = new DatabaseHelper(activity);
        binding.recentRecycle.setLayoutManager(new LinearLayoutManager(activity));

        ArrayList<RechargeHistory> list = databaseHelper.getRechargeHistoryList(mobileSearch,"");

        int sizeOf =list.size();
        if(sizeOf==0){
            binding.noRecentData.setVisibility(View.VISIBLE);
            binding.recentRecycle.setVisibility(View.GONE);
        }
        else{
            binding.noRecentData.setVisibility(View.GONE);
            binding.recentRecycle.setVisibility(View.VISIBLE);
        }

        setAdapter(list);
    }

    /**
     * Set adapter
     */
    private void setAdapter(ArrayList<RechargeHistory> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        arrayList.clear();
        arrayList.addAll(list);
        recentTransactionAdapter=new RecentTransactionAdapter(activity,arrayList);
        binding.recentRecycle.setAdapter(recentTransactionAdapter);
        recentTransactionAdapter.notifyDataSetChanged();
    }
}
