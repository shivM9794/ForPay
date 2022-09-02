package in.forpay.activity.flightbooking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import in.forpay.R;
import in.forpay.databinding.ActivityFlightHistoryBinding;

public class FlightHistoryActivity extends AppCompatActivity {

    private ActivityFlightHistoryBinding binding;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_flight_history);
        activity = this;
        basicMethods();
    }

    private void basicMethods() {

        binding.backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FlightBookingActivity.class);
            startActivity(intent);
            finish();
        });

        binding.recyclerFlightHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), BookingStatusActivity.class);
                startActivity(intent1);
                finish();
            }
        });
    }
}