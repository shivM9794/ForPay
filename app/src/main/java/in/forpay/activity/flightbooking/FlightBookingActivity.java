package in.forpay.activity.flightbooking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;

import in.forpay.R;
import in.forpay.adapter.OneWayAdapter;
import in.forpay.databinding.ActivityFlightBookingBinding;
import in.forpay.util.ProgressHelper;

public class FlightBookingActivity extends AppCompatActivity {

    private ActivityFlightBookingBinding mBinding;
    private Activity activity;
    private ProgressHelper progressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_flight_booking);
        activity = this;
        progressHelper = new ProgressHelper(activity);
        addtabLayout();
        mBinding.backArrow.setOnClickListener(v -> finish());

        mBinding.flightHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FlightHistoryActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void addtabLayout() {

        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("One-Way"));
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("Round"));
        mBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final OneWayAdapter adapter = new OneWayAdapter(getSupportFragmentManager(), this, mBinding.tabLayout.getTabCount());
        mBinding.viewPager.setAdapter(adapter);
        mBinding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tabLayout));

    }
}