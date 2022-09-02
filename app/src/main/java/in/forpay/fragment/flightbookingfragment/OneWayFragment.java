package in.forpay.fragment.flightbookingfragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import in.forpay.R;
import in.forpay.activity.flightbooking.AddTravellerFlight;
import in.forpay.activity.flightbooking.SearchFlightActivity;
import in.forpay.databinding.FragmentOneWayBinding;
import in.forpay.util.ProgressHelper;


public class OneWayFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private FragmentOneWayBinding fragmentOneWayBinding;
    private ProgressHelper progressHelper;
    int count = 0;

    public OneWayFragment() {

        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentOneWayBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_one_way, container, false);
        fragmentOneWayBinding.setFragmentOne(this);
        return fragmentOneWayBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressHelper = new ProgressHelper(getActivity());
        fragmentOneWayBinding.llClickable.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddTravellerFlight.class);
            startActivity(intent);

        });

        fragmentOneWayBinding.txtSearchFlight.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), SearchFlightActivity.class);
            startActivity(i);
        });

        fragmentOneWayBinding.txtDepartureDate.setOnClickListener(v -> {
            showDatePickerDialog();
            count = 1;
        });

        fragmentOneWayBinding.txtReturnDate.setOnClickListener(v -> {
            showDatePickerDialog();
            count = 2;
        });

    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (count == 1) {
            String date = dayOfMonth + "/" + month + "/" + year;
            fragmentOneWayBinding.txtDepartureDate.setText(date);

        }

        if (count == 2) {
            String date1 = dayOfMonth + "/" + month + "/" + year;
            fragmentOneWayBinding.txtReturnDate.setText(date1);
        }
    }
}


