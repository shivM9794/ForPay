package in.forpay.activity.flightbooking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import in.forpay.R;
import in.forpay.databinding.ActivityBookingStatusBinding;

public class BookingStatusActivity extends AppCompatActivity {

    private ActivityBookingStatusBinding binding;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_status);
        activity = this;

        basicMethod();

    }

    private void basicMethod() {

        binding.backArrow.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), FlightHistoryActivity.class);
            startActivity(i);
            finish();
        });
    }
}