package in.forpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.adapter.DailySummeryAdapter;
import in.forpay.databinding.ActivityDailySummeryBinding;

public class DailySummeryActivity extends AppCompatActivity {


    private ActivityDailySummeryBinding binding;
    private Activity activity;
    private ArrayList<Object> dailySummeryList = new ArrayList<>();
    private DailySummeryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DailySummeryActivity.this;
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_daily_summery);

        binding.backBtn.setOnClickListener(v -> onBackPressed());

        getDailySummeryList();

    }

    private void getDailySummeryList() {
        dailySummeryList.clear();
        binding.dailySummeryList.setLayoutManager(new LinearLayoutManager(activity));
        adapter =new DailySummeryAdapter(activity,dailySummeryList);
        binding.dailySummeryList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
