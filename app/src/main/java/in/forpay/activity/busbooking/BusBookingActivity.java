package in.forpay.activity.busbooking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import in.forpay.R;
import in.forpay.databinding.ActivityBusBookingBinding;
import in.forpay.fragment.busbookingfragment.BusBookWithDateAndPlaceFragment;

public class BusBookingActivity extends AppCompatActivity {


    ActivityBusBookingBinding busBookingBinding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_booking);

        busBookingBinding = DataBindingUtil.setContentView(this, R.layout.activity_bus_booking);
        busBookingBinding.setActivity(this);


        loadFragment();
    }

    private void loadFragment() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainerBusBooking,new BusBookWithDateAndPlaceFragment(),BusBookWithDateAndPlaceFragment.class.getSimpleName())
                .commit();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

}
