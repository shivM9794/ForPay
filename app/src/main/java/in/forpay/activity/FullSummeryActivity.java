package in.forpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.adapter.DaybookAdapter;
import in.forpay.databinding.ActivityFullSummeryBinding;

public class FullSummeryActivity extends AppCompatActivity {

    private ActivityFullSummeryBinding binding;
    private Activity activity;
    private ArrayList<Object> summeryList = new ArrayList<>();
    private DaybookAdapter yesterDaySummeryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = FullSummeryActivity.this;
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_full_summery);

        binding.backBtn.setOnClickListener(v -> onBackPressed());

        getYesterDaySummery();
    }

    private void getYesterDaySummery() {
        summeryList.clear();
        binding.fullSummeryRecycle.setLayoutManager(new LinearLayoutManager(activity));
        yesterDaySummeryAdapter=new DaybookAdapter(activity,summeryList);
        binding.fullSummeryRecycle.setAdapter(yesterDaySummeryAdapter);
        yesterDaySummeryAdapter.notifyDataSetChanged();
    }
}
