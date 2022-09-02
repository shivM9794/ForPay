package in.forpay.fragment.busbookingfragment;


import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.activity.busbooking.SearchFromStationActivity;
import in.forpay.databinding.FragmentBusBookWithDateAndPlaceBinding;
import in.forpay.model.CityList;
import in.forpay.model.busbookingModel.BusAvailableOnJourney;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusBookWithDateAndPlaceFragment extends Fragment implements View.OnClickListener {

    private static String TAG = BusBookWithDateAndPlaceFragment.class.getSimpleName();
    private FragmentBusBookWithDateAndPlaceBinding fragmentBusBookBinding;
    private ProgressHelper progressHelper;
    private SimpleDateFormat dateFormat, passDateFormat;
    private Calendar calendarInstance;
    private String dateOfJourney;
    private String noOfSeats = "1";
    private boolean isAcSelected = false;
    private boolean isNonAcSelected = false;
    private boolean isSeaterSelected = false;
    private boolean isSleeperSelected = false;
    private ArrayList<CityList.DataBean.BusStopsBean> myJourney = new ArrayList<>();

    public BusBookWithDateAndPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentBusBookBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bus_book_with_date_and_place, container, false);
        fragmentBusBookBinding.setFragmentBusBook(this);
        return fragmentBusBookBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressHelper = new ProgressHelper(getActivity());

        dateFormat = new SimpleDateFormat("dd MMM yy", Locale.getDefault());
        passDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        calendarInstance = Calendar.getInstance();
        Date date = new Date();
        date.setTime(calendarInstance.getTimeInMillis());
        String sourceDate = dateFormat.format(date);
        dateOfJourney = passDateFormat.format(date);

        fragmentBusBookBinding.txtSelectDate.setText(sourceDate);


        fragmentBusBookBinding.rgNoOfSeats.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = radioGroup.findViewById(i);
                setColor();
                String text = radioButton.getText().toString();
                noOfSeats = String.valueOf(text.charAt(0));


            }

        });


        fragmentBusBookBinding.imgBack.setOnClickListener(this);
        fragmentBusBookBinding.txtSourceStation.setOnClickListener(this);
        fragmentBusBookBinding.txtDestinationStation.setOnClickListener(this);
        fragmentBusBookBinding.imgSwap.setOnClickListener(this);
        fragmentBusBookBinding.imgSelectDate.setOnClickListener(this);
        fragmentBusBookBinding.txtSelectDate.setOnClickListener(this);
        fragmentBusBookBinding.llAc.setOnClickListener(this);
        fragmentBusBookBinding.llNonAc.setOnClickListener(this);
        fragmentBusBookBinding.llSeater.setOnClickListener(this);
        fragmentBusBookBinding.llSleeper.setOnClickListener(this);
        fragmentBusBookBinding.imgBusHistory.setOnClickListener(this);
        fragmentBusBookBinding.txtSearchBusAvailable.setOnClickListener(this);


    }

    private void setColor() {
        if (fragmentBusBookBinding.tvOneSeats.isChecked()) {
            fragmentBusBookBinding.tvOneSeats.setTextColor(getActivity().getResources().getColor(R.color.orange_new));
        } else {
            fragmentBusBookBinding.tvOneSeats.setTextColor(getActivity().getResources().getColor(R.color.comfort_default));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgBack:
                getActivity().onBackPressed();
                break;
            case R.id.imgBusHistory:
                BookingHistoryFragment bookingHistoryFragment = new BookingHistoryFragment();
                Bundle bundle2 = new Bundle();
                bookingHistoryFragment.setArguments(bundle2);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameContainerBusBooking, bookingHistoryFragment, BookingHistoryFragment.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.imgSelectDate:
            case R.id.txtSelectDate:
                showDatePickerDialog();
                break;

            case R.id.txtSourceStation:
                myJourney.clear();
                openSearchStationActivity(101);
                break;

            case R.id.txtDestinationStation:

                if (fragmentBusBookBinding.txtSourceStation.getText().toString().isEmpty()) {
                    Utility.showToastLatest(getActivity(), "Please choose from Station First", "ERROR");
                } else {
                    if (myJourney.size() == 2)
                        myJourney.remove(1);
                    openSearchStationActivity(102);
                }
                break;

            case R.id.imgSwap:

                if (myJourney.size() == 2) {
                    Collections.swap(myJourney, 0, 1);
                    fragmentBusBookBinding.txtSourceStation.setText(myJourney.get(0).getStopName());
                    fragmentBusBookBinding.txtDestinationStation.setText(myJourney.get(1).getStopName());
                }


                break;

            case R.id.ll_ac:

                if (fragmentBusBookBinding.ivAc.isSelected()) {
                    isAcSelected = false;
                    fragmentBusBookBinding.ivAc.setSelected(false);
                    fragmentBusBookBinding.ivAc.setColorFilter(ContextCompat.getColor(getActivity(), R.color.comfort_default), android.graphics.PorterDuff.Mode.SRC_IN);
                    // fragmentBusBookBinding.ivAc.setImageDrawable(getResources().getDrawable(R.drawable.ic_air_conditioner_disabled));
                    fragmentBusBookBinding.tvAc.setTextColor(getResources().getColor(R.color.tv_welcome_color));

                } else {
                    isAcSelected = true;
                    fragmentBusBookBinding.ivAc.setSelected(true);
                    fragmentBusBookBinding.ivAc.setColorFilter(ContextCompat.getColor(getActivity(), R.color.orange_new), android.graphics.PorterDuff.Mode.SRC_IN);
                    // fragmentBusBookBinding.ivAc.setImageDrawable(getResources().getDrawable(R.drawable.ic_air_conditioner));
                    fragmentBusBookBinding.tvAc.setTextColor(getResources().getColor(R.color.orange_new));

                }

                break;


            case R.id.ll_non_ac:

                if (fragmentBusBookBinding.ivNonAc.isSelected()) {
                    isNonAcSelected = false;
                    fragmentBusBookBinding.ivNonAc.setSelected(false);
                    fragmentBusBookBinding.ivNonAc.setColorFilter(ContextCompat.getColor(getActivity(), R.color.comfort_default), android.graphics.PorterDuff.Mode.SRC_IN);
                    //  fragmentBusBookBinding.ivNonAc.setImageDrawable(getResources().getDrawable(R.drawable.ic_fan_disabled));
                    fragmentBusBookBinding.tvNonAc.setTextColor(getResources().getColor(R.color.tv_welcome_color));

                } else {
                    isNonAcSelected = true;
                    fragmentBusBookBinding.ivNonAc.setSelected(true);
                    fragmentBusBookBinding.ivNonAc.setColorFilter(ContextCompat.getColor(getActivity(), R.color.orange_new), android.graphics.PorterDuff.Mode.SRC_IN);
                    //fragmentBusBookBinding.ivNonAc.setImageDrawable(getResources().getDrawable(R.drawable.ic_fan));
                    fragmentBusBookBinding.tvNonAc.setTextColor(getResources().getColor(R.color.orange_new));

                }

                break;


            case R.id.ll_seater:

                if (fragmentBusBookBinding.ivSeater.isSelected()) {
                    isSeaterSelected = false;
                    fragmentBusBookBinding.ivSeater.setSelected(false);
                    fragmentBusBookBinding.ivSeater.setColorFilter(ContextCompat.getColor(getActivity(), R.color.comfort_default), android.graphics.PorterDuff.Mode.SRC_IN);
                    //   fragmentBusBookBinding.ivSeater.setImageDrawable(getResources().getDrawable(R.drawable.ic_seater_disabled));
                    fragmentBusBookBinding.tvSeater.setTextColor(getResources().getColor(R.color.tv_welcome_color));

                } else {
                    isSeaterSelected = true;
                    fragmentBusBookBinding.ivSeater.setSelected(true);
                    fragmentBusBookBinding.ivSeater.setColorFilter(ContextCompat.getColor(getActivity(), R.color.orange_new), android.graphics.PorterDuff.Mode.SRC_IN);
                    // fragmentBusBookBinding.ivSeater.setImageDrawable(getResources().getDrawable(R.drawable.ic_seater));
                    fragmentBusBookBinding.tvSeater.setTextColor(getResources().getColor(R.color.orange_new));

                }

                break;

            case R.id.ll_sleeper:

                if (fragmentBusBookBinding.ivSleeper.isSelected()) {
                    isSleeperSelected = false;
                    fragmentBusBookBinding.ivSleeper.setSelected(false);
                    fragmentBusBookBinding.ivSleeper.setColorFilter(ContextCompat.getColor(getActivity(), R.color.comfort_default), android.graphics.PorterDuff.Mode.SRC_IN);
                    // fragmentBusBookBinding.ivSleeper.setImageDrawable(getResources().getDrawable(R.drawable.ic_sleeper_disabled));
                    fragmentBusBookBinding.tvSleeper.setTextColor(getResources().getColor(R.color.tv_welcome_color));

                } else {
                    isSleeperSelected = true;
                    fragmentBusBookBinding.ivSleeper.setSelected(true);
                    fragmentBusBookBinding.ivSleeper.setColorFilter(ContextCompat.getColor(getActivity(), R.color.orange_new), android.graphics.PorterDuff.Mode.SRC_IN);
                    // fragmentBusBookBinding.ivSleeper.setImageDrawable(getResources().getDrawable(R.drawable.ic_sleeper_enabled));
                    fragmentBusBookBinding.tvSleeper.setTextColor(getResources().getColor(R.color.orange_new));

                }

                break;

            case R.id.txtSearchBusAvailable:

                if (fragmentBusBookBinding.txtSourceStation.getText().toString().isEmpty() || fragmentBusBookBinding.txtDestinationStation.getText().toString().isEmpty()) {
                    Utility.showToastLatest(getActivity(), "Please Select City", "ERROR");
                    return;
                }

                // TODO you were here
                SearchBusAvailableOnRoute();

                break;


        }


    }

    private void SearchBusAvailableOnRoute() {


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(getActivity())); // key
        map1.put("imei", Utility.getImei(getActivity())); // imei
        map1.put("os", Utility.getOs(getActivity()));
        map1.put("versionCode", Utility.getVersionCode(getActivity())); // version code
        map1.put("sourceId", myJourney.get(0).getSourceId());
        map1.put("destinationId", myJourney.get(1).getSourceId());
        map1.put("dateOfJourny", this.dateOfJourney); //dateOfJourny -> Y-m-d , eg : 2020-01-30

        String request = Utility.mapWrapper(getActivity(), map1);

        getAvailableBusesOnRoute(request);
    }

    private void getAvailableBusesOnRoute(String request) {

        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.BUS_LIST, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect), "");
        }
    }

    private void parseResponse(String result) {

        if (isVisible()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {

                Log.e("SEARCH BUS ::", result);

                BusAvailableOnJourney busAvailableOnJourney = new Gson().fromJson(result, BusAvailableOnJourney.class);

                if (!busAvailableOnJourney.getResCode().equals("200")) {
                    Utility.showToast(getActivity(), busAvailableOnJourney.getResText(), busAvailableOnJourney.getResCode());
                } else {
                    showListOfAvailableBuses(busAvailableOnJourney.getData());
                }


            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding), "");
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
            }
        }
    }

    private void showListOfAvailableBuses(BusAvailableOnJourney.DataBean data) {

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.SOURCE_DESTINATION, myJourney);
        bundle.putParcelable(Constant.BUS_AVAILABLE_ON_ROUTE, data);
        bundle.putString(Constant.DATE_OF_JOURNEY, dateOfJourney);
        bundle.putString(Constant.NO_OF_SEATS, noOfSeats);
        bundle.putBoolean(Constant.IS_AC_FILTER_SELECTED, isAcSelected);
        bundle.putBoolean(Constant.IS_NON_AC_FILTER_SELECTED, isNonAcSelected);
        bundle.putBoolean(Constant.IS_SLEEPER_FILTER_SELECTED, isSleeperSelected);
        bundle.putBoolean(Constant.IS_SEATER_FILTER_SELECTED, isSeaterSelected);


        AvailableBusesFragment availableBusesFragment = new AvailableBusesFragment();
        availableBusesFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.frameContainerBusBooking, availableBusesFragment, AvailableBusesFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();


    }


    private void showDatePickerDialog() {

        DatePickerDialog startTime = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            dateOfJourney = passDateFormat.format(newDate.getTime());

            Date todayDate = new Date(Calendar.getInstance().getTime().getTime());


            setDateFromPicker(todayDate, newDate.getTime());


        }, calendarInstance.get(Calendar.YEAR), calendarInstance.get(Calendar.MONTH), calendarInstance.get(Calendar.DAY_OF_MONTH));

        startTime.show();

    }

    private void setDateFromPicker(Date todayDate, Date selectedDate) {


        if (selectedDate.before(todayDate)) {

            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getActivity(), "Please select today date onwards", Toast.LENGTH_LONG).show();
                }
            });


        } else {
            fragmentBusBookBinding.txtSelectDate.setText(dateFormat.format(selectedDate));
        }

    }

    private void openSearchStationActivity(int i) {

        if (i == 101) {
            startActivityForResult(new Intent(getActivity(), SearchFromStationActivity.class)
                    .putExtra(Constant.SOURCE_DESTINATION, Constant.SOURCE), i);
        } else if (i == 102) {
            startActivityForResult(new Intent(getActivity(), SearchFromStationActivity.class)
                    .putExtra(Constant.SOURCE_DESTINATION, Constant.DESTINATION)
                    .putExtra(Constant.SOURCE_CITY, myJourney.get(0)), i);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {

            if (Objects.requireNonNull(data).getParcelableExtra(Constant.SOURCE) != null) {
                CityList.DataBean.BusStopsBean busStopsBean = data.getParcelableExtra(Constant.SOURCE);
                fragmentBusBookBinding.txtSourceStation.setText(busStopsBean.getStopName());
                myJourney.clear();
                myJourney.add(busStopsBean);


                openSearchStationActivity(102);
            }


        } else if (requestCode == 102 && resultCode == RESULT_OK) {


            if (Objects.requireNonNull(data).getParcelableExtra(Constant.SOURCE) != null) {
                CityList.DataBean.BusStopsBean busStopsBean = data.getParcelableExtra(Constant.SOURCE);
                myJourney.add(busStopsBean);
                fragmentBusBookBinding.txtDestinationStation.setText(busStopsBean.getStopName());
            }
        }
    }
}
