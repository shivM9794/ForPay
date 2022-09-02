package in.forpay.activity.flightbooking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import in.forpay.R;
import in.forpay.databinding.ActivityAddTravellerFlightBinding;
import in.forpay.util.ProgressHelper;

public class AddTravellerFlight extends AppCompatActivity {

    private ActivityAddTravellerFlightBinding binding;
    private Activity activity;
    private ProgressHelper progressHelper;
    int minteger = 1;
    int minteger1 = 1;
    int minteger2 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_traveller_flight);
        activity = this;
        progressHelper = new ProgressHelper(activity);

        basicMethod();


    }

    private void basicMethod() {

        binding.backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FlightBookingActivity.class);
            startActivity(intent);
            finish();
        });


    }

    public void increaseInteger(View view) {
        minteger = minteger + 1;
        display(minteger);

    }

    public void decreaseInteger(View view) {
        if (minteger > 1) {
            minteger = minteger - 1;
            display(minteger);
        }
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.integer_number);
        displayInteger.setText("" + number);
    }

    public void increaseInteger1(View view) {
        minteger1 = minteger1 + 1;
        display1(minteger1);

    }

    public void decreaseInteger1(View view) {
        if (minteger1 > 1) {
            minteger1 = minteger1 - 1;
            display1(minteger1);
        }
    }

    private void display1(int number) {
        TextView displayInteger1 = (TextView) findViewById(
                R.id.integer_number1);
        displayInteger1.setText("" + number);
    }

    public void increaseInteger2(View view) {
        minteger2 = minteger2 + 1;
        display2(minteger2);

    }

    public void decreaseInteger2(View view) {
        if (minteger2 > 1) {
            minteger2 = minteger2 - 1;
            display2(minteger2);
        }
    }

    private void display2(int number) {
        TextView displayInteger2 = (TextView) findViewById(
                R.id.integer_number2);
        displayInteger2.setText("" + number);
    }


    public void continueSearch(View view) {

        Intent i = new Intent(getApplicationContext(), FlightBookingActivity.class);
//        String strName = null;
//        i.putExtra("STRING_I_NEED", strName);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Adults" + minteger1 + "Children" + minteger + "Infants" + minteger2, Toast.LENGTH_SHORT).show();


    }
}