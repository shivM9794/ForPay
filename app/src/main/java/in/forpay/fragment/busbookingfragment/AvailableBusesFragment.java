package in.forpay.fragment.busbookingfragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.adapter.busbookingadapter.BusesAvailableOnJourneyAdapter;
import in.forpay.databinding.FragmentAvailableBusesBinding;
import in.forpay.model.CityList;
import in.forpay.model.busbookingModel.BusAvailableOnJourney;
import in.forpay.network.CallbackListener;
import in.forpay.network.ItemClickListeners;
import in.forpay.network.PolicyClick;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.BusSorting;
import in.forpay.util.Constant;
import in.forpay.util.PreferenceConnector;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableBusesFragment extends Fragment implements View.OnClickListener, ItemClickListeners {


    private LinkedHashMap<String, Boolean> travelList;
    private LinkedHashMap<String, Boolean> boardingList;
    private LinkedHashMap<String, Boolean> droppingList;
    private FragmentAvailableBusesBinding availableBusesBinding;
    private Calendar nextDayCalender, previousDayCalender;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE,dd MMM", Locale.getDefault());
    private ProgressHelper progressHelper;
    private BusAvailableOnJourney.DataBean data;
    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> busStopsBeans;
    private ArrayList<CityList.DataBean.BusStopsBean> myJourneyList;
    private String dateOfJourney;
    private long currentDateInMillSeconds;
    private BusesAvailableOnJourneyAdapter adapter;
    private boolean isDepartureDescending = false;
    private boolean isDurationDescending = false;
    private boolean isPriceDescending = false;
    private boolean isCalledFirstTime = false;
    private String departure = "departure";
    private String duration = "duration";
    private String price = "price";
    private boolean isAcSelected = false;
    private boolean isNonAcSelected = false;
    private boolean isSeaterSelected = false;
    private boolean isSleeperSelected = false;

    private BroadcastReceiver openCloseDrawerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), "OpenCloseDrawerAndFilterData")) {
                if (intent.getStringExtra(Constant.FILTER_TAG_LIST) != null) {

                    String dataFilter = intent.getStringExtra(Constant.FILTER_TAG_LIST);
                    Gson gson = new Gson();

                    Type entityType = new TypeToken<LinkedHashMap<String, String>>() {
                    }.getType();
                    LinkedHashMap<String, String> travel = gson.fromJson(dataFilter, entityType);

                    if (travel.size() > 0) {
                        busStopsBeans.clear();
                        busStopsBeans.addAll(data.getBusStops());
                        filterAdapterItem(travel);
                    }


                } else if (intent.getBooleanExtra(Constant.CLEAR_FILTER_ITEM, false)) {

                    filterData();

                }

                if (adapter != null)
                    adapter.notifyDataSetChanged();

                availableBusesBinding.imgOpenRightDrawer.performClick();
            }

        }
    };


    public interface shareInterface {
        void onWhatsAppShare(int pos);
    }

    public AvailableBusesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        availableBusesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_available_buses, container, false);
        availableBusesBinding.setAvailableBusList(this);

        getBundleData();
        return availableBusesBinding.getRoot();
    }

    private void getBundleData() {

        assert getArguments() != null;
        myJourneyList = getArguments().getParcelableArrayList(Constant.SOURCE_DESTINATION);
        dateOfJourney = getArguments().getString(Constant.DATE_OF_JOURNEY);
        String noOfSeats = getArguments().getString(Constant.NO_OF_SEATS);
        data = getArguments().getParcelable(Constant.BUS_AVAILABLE_ON_ROUTE);

        isAcSelected = getArguments().getBoolean(Constant.IS_AC_FILTER_SELECTED);
        isNonAcSelected = getArguments().getBoolean(Constant.IS_NON_AC_FILTER_SELECTED);
        isSleeperSelected = getArguments().getBoolean(Constant.IS_SLEEPER_FILTER_SELECTED);
        isSeaterSelected = getArguments().getBoolean(Constant.IS_SEATER_FILTER_SELECTED);


        availableBusesBinding.txtSource.setText(myJourneyList.get(0).getStopName());
        PreferenceConnector.writeString(getContext(), PreferenceConnector.BUS_SOURCE, myJourneyList.get(0).getStopName());
        availableBusesBinding.txtDestination.setText(myJourneyList.get(1).getStopName());
        PreferenceConnector.writeString(getContext(), PreferenceConnector.BUS_DESTINATION, myJourneyList.get(1).getStopName());
        availableBusesBinding.txtNoSeats.setText(noOfSeats + " seats");

        availableBusesBinding.linPriceSeat.setOnClickListener(this);
        availableBusesBinding.linDeparture.setOnClickListener(this);
        availableBusesBinding.linDuration.setOnClickListener(this);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressHelper = new ProgressHelper(getActivity());
        availableBusesBinding.txtPreviousDate.setText(null);
        isCalledFirstTime = true;

        calendar.setTimeInMillis(Constant.getTimeInMilli(dateOfJourney, "yyyy-MM-dd"));
        setDate(calendar);

        busStopsBeans = new ArrayList<>();
        setClickListeners();

        filterData();
        setDataToAdapter();


    }

    private void setFilterViewToDrawer() {

        Gson gson = new Gson();
        String travel = gson.toJson(travelList);
        String boarding = gson.toJson(boardingList);
        String dropping = gson.toJson(droppingList);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.BUS_AVAILABLE_ON_ROUTE, busStopsBeans);
        bundle.putString(Constant.TRAVEL, travel);
        bundle.putString(Constant.BOARDING, boarding);
        bundle.putString(Constant.DROPPING, dropping);


        FilterFragment filterFragment = FilterFragment.getInstance(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameDrawer, filterFragment, FilterFragment.class.getSimpleName())
                .commit();


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(openCloseDrawerReceiver, new IntentFilter("OpenCloseDrawerAndFilterData"));

    }

    private void filterAdapterItem(LinkedHashMap<String, String> travel) {

        for (int i = 0; i < travel.size(); i++) {


            for (Map.Entry<String, String> s : travel.entrySet()) {

                if (s.getValue().contains("High to Low")) {
                    Collections.sort(busStopsBeans, new BusSorting.PriceDescending());

                    makeArrowVisibleHide(price);

                    if (isPriceDescending) {
                        isPriceDescending = false;
                        makeArrowUp(availableBusesBinding.imgArrowPrice);
                    }


                } else if (s.getValue().contains("Low to High")) {
                    Collections.sort(busStopsBeans, new BusSorting.PriceAscending());


                    makeArrowVisibleHide(price);
                    if (!isPriceDescending) {
                        isPriceDescending = true;
                        makeArrowDown(availableBusesBinding.imgArrowPrice);
                    }

                } else if (s.getValue().contains("Short to Long")) {
                    Collections.sort(busStopsBeans, new BusSorting.DurationAscending());
                    makeArrowVisibleHide(duration);


                    if (!isDurationDescending) {
                        isDurationDescending = true;
                        makeArrowDown(availableBusesBinding.imgArrowDuration);
                    }

                } else if (s.getValue().contains("6PM to 6AM")) {

                    ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> withFilter = new ArrayList<>();
                    for (int j = 0; j < busStopsBeans.size(); j++) {

                        long startTime = Constant.getTimeInMilli("06 PM", "hh a");
                        long endTime = Constant.getTimeInMilli("06 AM", "hh a");

                        long departureTime = Constant.getTimeInMilli(busStopsBeans.get(i).getDepartureTime(), "hh a");
                        long arrivalTime = Constant.getTimeInMilli(busStopsBeans.get(i).getArrivalTime(), "hh a");

                        if ((departureTime >= startTime && arrivalTime <= endTime)) {
                            withFilter.add(busStopsBeans.get(i));
                        }


                    }
                    if (withFilter.size() > 0) {
                        busStopsBeans.clear();
                        busStopsBeans.addAll(withFilter);
                    } else {
                        adapter.clearList(new ArrayList<>());

                    }

                    // Collections.sort(busStopsBeans,new BusSorting.BusTimeBetween6PMTO6AM());
                } else if (s.getValue().contains("6AM to 6PM")) {

                    ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> withFilter = new ArrayList<>();
                    for (int j = 0; j < busStopsBeans.size(); j++) {

                        long startTime = Constant.getTimeInMilli("06 AM", "hh a");
                        long endTime = Constant.getTimeInMilli("06 PM", "hh a");

                        long departureTime = Constant.getTimeInMilli(busStopsBeans.get(i).getDepartureTime(), "hh a");
                        long arrivalTime = Constant.getTimeInMilli(busStopsBeans.get(i).getArrivalTime(), "hh a");

                        if ((departureTime >= startTime && arrivalTime <= endTime)) {
                            withFilter.add(busStopsBeans.get(i));
                        }
                    }

                    if (withFilter.size() > 0) {
                        busStopsBeans.clear();
                        busStopsBeans.addAll(withFilter);
                    } else {
                        adapter.clearList(new ArrayList<>());

                    }


                    // Collections.sort(busStopsBeans,new BusSorting.BusTimeBetween6PMTO6AM());
                } else if (s.getValue().contains("AC Seater")) {

                    ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> list = getFilteredList("AC Seater");
                    if (list.size() > 0) {
                        busStopsBeans.clear();
                        busStopsBeans.addAll(list);
                    } else {
                        adapter.clearList(new ArrayList<>());

                    }

                } else if (s.getValue().contains("AC Sleeper")) {

                    ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> list = getFilteredList("AC Sleeper");
                    if (list.size() > 0) {
                        busStopsBeans.clear();
                        busStopsBeans.addAll(list);
                    } else {
                        adapter.clearList(new ArrayList<>());

                    }

                } else if (s.getValue().contains("Non-AC Seater")) {

                    ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> list = getFilteredList("Non-AC Seater");
                    if (list.size() > 0) {
                        busStopsBeans.clear();
                        busStopsBeans.addAll(list);
                    } else {
                        adapter.clearList(new ArrayList<>());

                    }
                } else if (s.getValue().contains("Non-AC Sleeper")) {

                    ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> list = getFilteredList("Non-AC Seater");
                    if (list.size() > 0) {
                        busStopsBeans.clear();
                        busStopsBeans.addAll(list);
                    } else {
                        adapter.clearList(new ArrayList<>());

                    }
                } else if (s.getKey().equals("Travel")) {
                    ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> list = getFilteredListBasedBusBoardingDroppingAndTravelName(s.getKey(), s.getValue());
                    if (list.size() > 0) {
                        busStopsBeans.clear();
                        busStopsBeans.addAll(list);
                    } else {
                        adapter.clearList(new ArrayList<>());

                    }
                } else if (s.getKey().equals("Boardings")) {
                    ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> list = getFilteredListBasedBusBoardingDroppingAndTravelName(s.getKey(), s.getValue());
                    if (list.size() > 0) {
                        busStopsBeans.clear();
                        busStopsBeans.addAll(list);
                    } else {
                        adapter.clearList(new ArrayList<>());

                    }
                } else if (s.getKey().equals("Droppings")) {
                    ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> list = getFilteredListBasedBusBoardingDroppingAndTravelName(s.getKey(), s.getValue());
                    if (list.size() > 0) {
                        busStopsBeans.clear();
                        busStopsBeans.addAll(list);
                    } else {
                        adapter.clearList(new ArrayList<>());

                    }


                }
            }

        }
    }

    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> getFilteredListBasedBusBoardingDroppingAndTravelName(String key, String value) {

        ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> withFilter = new ArrayList<>();
        boolean isDataAdded = false;
        for (int j = 0; j < busStopsBeans.size(); j++) {

            switch (key) {
                case "Travel":

                    if (busStopsBeans.get(j).getBusName().toString().toLowerCase().contains(value.toLowerCase().toString())) {
                        isDataAdded = true;
                        withFilter.add(busStopsBeans.get(j));
                    }


                    break;
                case "Boardings":
                    for (BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean boardingPointsBean : busStopsBeans.get(j).getBoardingPoints()) {

                        if (boardingPointsBean.getBpName().contains(value)) {
                            isDataAdded = true;
                            withFilter.add(busStopsBeans.get(j));
                        }
                    }


                    break;
                case "Droppings":
                    for (BusAvailableOnJourney.DataBean.BusStopsBean.DroppingPointsBean boardingPointsBean : busStopsBeans.get(j).getDroppingPoints()) {
                        if (boardingPointsBean.getDpName().contains(value)) {
                            isDataAdded = true;
                            withFilter.add(busStopsBeans.get(j));
                        }

                    }
                    break;
            }


        }

        if (!isDataAdded) {
            withFilter.clear();
        }


        return withFilter;


    }

    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> getFilteredList(String filterType) {

        ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> withFilter = new ArrayList<>();
        for (int j = 0; j < busStopsBeans.size(); j++) {

            if (busStopsBeans.get(j).getAmenities().getAc().contains(filterType)) {
                withFilter.add(busStopsBeans.get(j));
            }
        }

        return withFilter;
    }


    private void filterData() {


        busStopsBeans.addAll(data.getBusStops());
        isPriceDescending = false;
        availableBusesBinding.linPriceSeat.performClick();
        makeFilterListBasedOnData();
    }


    private void setDataToAdapter() {

        availableBusesBinding.recBusesAvailabelOnJourney.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BusesAvailableOnJourneyAdapter(getActivity(), busStopsBeans, this, new shareInterface() {

            @Override
            public void onWhatsAppShare(int pos) {

                ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> arrayList = new ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean>();
                arrayList.add(busStopsBeans.get(pos));


                shareBus(getActivity(),
                        arrayList,
                        myJourneyList.get(0).getStopName(),
                        myJourneyList.get(1).getStopName(), new HashMap<String, HashSet<String>>());
            }
        }, new PolicyClick() {
            @Override
            public void onItemClick(String policy) {
                CancellationPolicyBottomDialog cancellationPolicyBottomDialog = new CancellationPolicyBottomDialog();
                Bundle bundle2 = new Bundle();
                bundle2.putString(Constant.KEY_CANCELLATION_POLICY, policy);
                cancellationPolicyBottomDialog.setArguments(bundle2);
                cancellationPolicyBottomDialog.show(getChildFragmentManager(), cancellationPolicyBottomDialog.getTag());

            }
        });
        availableBusesBinding.recBusesAvailabelOnJourney.setAdapter(adapter);

    }

    private void setDate(Calendar calendar) {


        setCurrentDate(dateFormat, calendar);
        setPreviousDate(dateFormat, calendar);
        setNextDate(dateFormat, calendar);

        if (!isCalledFirstTime) {
            SimpleDateFormat passDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Date date = new Date();
            date.setTime(currentDateInMillSeconds);
            dateOfJourney = passDateFormat.format(Objects.requireNonNull(date));

            busStopsBeans.clear();
            adapter.notifyDataSetChanged();

            SearchBusAvailableOnRoute(dateOfJourney);
        } else {
            isCalledFirstTime = false;
        }


    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.clear();
    }

    private void setNextDate(SimpleDateFormat dateFormat, Calendar calendar) {


        availableBusesBinding.txtNextDate.setText(getDate(dateFormat, calendar, 1).toUpperCase());
    }

    private void setPreviousDate(SimpleDateFormat dateFormat, Calendar calendar) {
        availableBusesBinding.txtPreviousDate.setText(getDate(dateFormat, calendar, -1).toUpperCase());

    }

    private void setCurrentDate(SimpleDateFormat dateFormat, Calendar calendar) {
        availableBusesBinding.txtCurrentDate.setText(getDate(dateFormat, calendar, 0).toUpperCase());

    }

    private String getDate(SimpleDateFormat dateFormat, Calendar calendar, int i) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(calendar.getTimeInMillis());

        Date date = new Date();
        if (i == 1) {
            calendar1.add(Calendar.DATE, 1);//Adds a day
            nextDayCalender = calendar1;
        } else if (i == 0) {

            calendar1.add(Calendar.DATE, 0);
            currentDateInMillSeconds = calendar1.getTimeInMillis();

        } else if (i == -1) {

            calendar1.add(Calendar.DATE, -1);
            previousDayCalender = calendar1;//Goes to previous day

            if (calendar1.getTime() == Calendar.getInstance().getTime() || calendar1.getTime().before(Calendar.getInstance().getTime())) {

                if (!DateUtils.isToday(calendar1.getTime().getTime())) {
                    availableBusesBinding.txtPreviousDate.setEnabled(false);
                    return "";
                } else {
                    availableBusesBinding.txtPreviousDate.setEnabled(true);
                }


            } else {
                availableBusesBinding.txtPreviousDate.setEnabled(true);
            }

        }
        date.setTime(calendar1.getTimeInMillis());

        return dateFormat.format(date);

    }

    private void setClickListeners() {

        availableBusesBinding.imgBack.setOnClickListener(this);
        availableBusesBinding.imgOpenRightDrawer.setOnClickListener(this);
        availableBusesBinding.txtCurrentDate.setOnClickListener(this);
        availableBusesBinding.txtPreviousDate.setOnClickListener(this);
        availableBusesBinding.txtNextDate.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.imgOpenRightDrawer:
                if (availableBusesBinding.drawerFilter.isDrawerOpen(GravityCompat.END)) {
                    availableBusesBinding.drawerFilter.closeDrawer(GravityCompat.END);
                } else {
                    availableBusesBinding.drawerFilter.openDrawer(GravityCompat.END);
                }
                break;
            case R.id.txtPreviousDate:
                setDate(previousDayCalender);
                break;
            case R.id.txtNextDate:
                setDate(nextDayCalender);
                break;
            case R.id.linDeparture:
                makeArrowVisibleHide(departure);

                if (isDepartureDescending) {
                    isDepartureDescending = false;
                    makeArrowDown(availableBusesBinding.imgArrowDeparture);
                } else {
                    isDepartureDescending = true;
                    makeArrowUp(availableBusesBinding.imgArrowDeparture);
                }
                sortListOnDeparture(isDepartureDescending);

                break;
            case R.id.linDuration:

                makeArrowVisibleHide(duration);

                if (isDurationDescending) {
                    isDurationDescending = false;
                    makeArrowDown(availableBusesBinding.imgArrowDuration);
                } else {
                    isDurationDescending = true;
                    makeArrowUp(availableBusesBinding.imgArrowDuration);
                }
                sortListOnDuration(isDurationDescending);


                break;
            case R.id.linPriceSeat:

                makeArrowVisibleHide(price);

                if (isPriceDescending) {
                    isPriceDescending = false;
                    makeArrowDown(availableBusesBinding.imgArrowPrice);
                } else {
                    isPriceDescending = true;
                    makeArrowUp(availableBusesBinding.imgArrowPrice);
                }

                sortListOnPrice(isPriceDescending);


                break;

        }

    }

    private void sortListOnPrice(boolean isPriceDescending) {


        if (isPriceDescending) {
            Collections.sort(busStopsBeans, new BusSorting.PriceDescending());
        } else {
            Collections.sort(busStopsBeans, new BusSorting.PriceAscending());
        }


        if (isAcSelected || isSeaterSelected || isNonAcSelected || isSleeperSelected) {
            ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> tempList = applyDefaultFilter();
            busStopsBeans.clear();
            busStopsBeans.addAll(tempList);
        }


        if (adapter != null)
            adapter.notifyDataSetChanged();

    }

    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> applyDefaultFilter() {


        ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> tempList = new ArrayList<>();


        for (BusAvailableOnJourney.DataBean.BusStopsBean busStopItem : busStopsBeans) {

            boolean isAc = busStopItem.getAmenities().getAc().equals("yes");
            boolean isSleeper = busStopItem.getAmenities().getSleeper().equals("yes");


            if (isAcSelected) {
                if (isSeaterSelected) {
                    if (isAc && !isSleeper) {
                        tempList.add(busStopItem);
                    }
                } else if (isSleeperSelected) {
                    if (isAc && isSleeper) {
                        tempList.add(busStopItem);
                    }
                } else if (isAc) {
                    tempList.add(busStopItem);
                }
            } else if (isNonAcSelected) {
                if (this.isSeaterSelected) {
                    if (!isAc && !isSleeper) {
                        tempList.add(busStopItem);
                    }
                } else if (this.isSleeperSelected) {
                    if (!isAc && isSleeper) {
                        tempList.add(busStopItem);
                    }
                } else if (!isAc) {
                    tempList.add(busStopItem);
                }
            } else if (isSeaterSelected) {
                if (!isSleeper) {
                    tempList.add(busStopItem);
                }
            } else if (this.isSleeperSelected && isSleeper) {
                tempList.add(busStopItem);
            }


        }

        return tempList;

    }


    private void sortListOnDeparture(boolean isPriceDescending) {

        if (isPriceDescending) {
            Collections.sort(busStopsBeans, new BusSorting.DepartureDescending());
        } else {
            Collections.sort(busStopsBeans, new BusSorting.DepartureAscending());
        }


        if (adapter != null)
            adapter.notifyDataSetChanged();

    }

    private void sortListOnDuration(boolean isPriceDescending) {

        if (isPriceDescending) {
            Collections.sort(busStopsBeans, new BusSorting.DurationDescending());
        } else {
            Collections.sort(busStopsBeans, new BusSorting.DurationAscending());
        }


        if (adapter != null)
            adapter.notifyDataSetChanged();

    }

    private void makeFilterListBasedOnData() {

        travelList = new LinkedHashMap<>();
        boardingList = new LinkedHashMap<>();
        droppingList = new LinkedHashMap<>();

        for (BusAvailableOnJourney.DataBean.BusStopsBean busBeans : busStopsBeans) {
            travelList.put(busBeans.getBusName(), false);

            for (BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean boardingPointsBean : busBeans.getBoardingPoints()) {
                boardingList.put(boardingPointsBean.getBpName(), false);
            }

            for (BusAvailableOnJourney.DataBean.BusStopsBean.DroppingPointsBean droppingPointsBean : busBeans.getDroppingPoints()) {
                droppingList.put(droppingPointsBean.getDpName(), false);
            }
        }

        setFilterViewToDrawer();
    }


    private void makeArrowVisibleHide(String filterName) {
        if (filterName.equals(price)) {

            availableBusesBinding.imgArrowDuration.setAlpha(0.0f);
            availableBusesBinding.imgArrowDeparture.setAlpha(0.0f);
            availableBusesBinding.imgArrowPrice.setAlpha(1f);


        } else if (filterName.equals(departure)) {

            availableBusesBinding.imgArrowDuration.setAlpha(0.0f);
            availableBusesBinding.imgArrowDeparture.setAlpha(1f);
            availableBusesBinding.imgArrowPrice.setAlpha(0.0f);


        } else if (filterName.equals(duration)) {
            availableBusesBinding.imgArrowDuration.setAlpha(1f);
            availableBusesBinding.imgArrowDeparture.setAlpha(0.0f);
            availableBusesBinding.imgArrowPrice.setAlpha(0.0f);

        }
    }

    private void makeArrowUp(ImageView view) {
        Animation aniRotateClk = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_anti_clockwise);
        view.startAnimation(aniRotateClk);

    }

    private void makeArrowDown(ImageView view) {

        Animation aniRotateClk = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_clockwise);
        view.startAnimation(aniRotateClk);
    }


    private void SearchBusAvailableOnRoute(String dateOfJourney) {


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(getActivity())); // key
        map1.put("imei", Utility.getImei(getActivity())); // imei

        map1.put("os", Utility.getOs(getActivity()));

        map1.put("versionCode", Utility.getVersionCode(getActivity())); // version code
        map1.put("sourceId", myJourneyList.get(0).getSourceId());
        map1.put("destinationId", myJourneyList.get(1).getSourceId());
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


                BusAvailableOnJourney busAvailableOnJourney = new Gson().fromJson(result, BusAvailableOnJourney.class);

                if (!busAvailableOnJourney.getResCode().equals("200")) {
                    Utility.showToast(getActivity(), busAvailableOnJourney.getResText(), busAvailableOnJourney.getResCode());
                } else {

                    busStopsBeans.clear();
                    busStopsBeans.addAll(busAvailableOnJourney.getData().getBusStops());

                    if (isAcSelected || isSeaterSelected || isNonAcSelected || isSleeperSelected) {
                        ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> tempList = applyDefaultFilter();
                        busStopsBeans.clear();
                        busStopsBeans.addAll(tempList);
                    }


                }


                if (isPriceDescending) {
                    makeArrowUp(availableBusesBinding.imgArrowPrice);
                } else {
                    makeArrowDown(availableBusesBinding.imgArrowPrice);
                }

                sortListOnPrice(isPriceDescending);
                makeFilterListBasedOnData();

            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding), "");
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
            }
        }
    }

    @Override
    public void onItemClick(int pos) {

        Log.d("Bus clicked", " bus id " + busStopsBeans.get(pos).getBusId());
        String noOfSeats = getArguments().getString(Constant.NO_OF_SEATS);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.SOURCE_DESTINATION, myJourneyList);
        bundle.putString(Constant.BUS_ID, busStopsBeans.get(pos).getBusId());
        bundle.putString(Constant.DATE_OF_JOURNEY, dateOfJourney);
        bundle.putString(Constant.NO_OF_SEATS, noOfSeats);
        bundle.putParcelableArrayList(Constant.BOARDING, (ArrayList<? extends Parcelable>) busStopsBeans.get(pos).getBoardingPoints());
        bundle.putParcelableArrayList(Constant.DROPPING, (ArrayList<? extends Parcelable>) busStopsBeans.get(pos).getDroppingPoints());
        bundle.putString(Constant.KEY_CANCELLATION_POLICY, busStopsBeans.get(pos).getCancellationPolicy());


        SelectSeatAndTravellers selectSeatAndTravellers = new SelectSeatAndTravellers();
        selectSeatAndTravellers.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.frameContainerBusBooking, selectSeatAndTravellers, SelectSeatAndTravellers.class.getSimpleName())
                .addToBackStack(null)
                .commit();


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(openCloseDrawerReceiver);
    }

    public void shareBus(Context context,
                         ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> arrayList,
                         String str,
                         String str2,
                         HashMap<String, HashSet<String>> hashMap) {
        String phoneNo = PreferenceConnector.readString(context, PreferenceConnector.CUSTOMER_MOBILE, "");
        String firstName = PreferenceConnector.readString(context, PreferenceConnector.CUSTOMER_NAME, "");

        shareIntentsWhatsAppHistory(context, getShareDataBus(context, arrayList, firstName, phoneNo, str, str2, hashMap));
    }


    public void shareIntentsWhatsAppHistory(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("Share data flight size: ");
        sb.append(str.getBytes());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage(getPackageNameWhatApp(context));
        intent.putExtra(Intent.EXTRA_TEXT, str);


        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(context, Constant.WHATS_PP_ERROR, 0).show();
        }//Toast.makeText(context, Constant.OOPS_ERROR, 0).show();


    }

    public static boolean appInstalledOrNot(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }


    public static String getPackageNameWhatApp(Context context) {
        String str = "com.whatsapp";
        return appInstalledOrNot(context, str) ? str : "com.whatsapp.w4b";
    }


    private String getShareDataBus(Context context, ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> arrayList, String str, String str2, String str3, String str4, HashMap<String, HashSet<String>> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("🚌 *BUS Details*\n\n");
        sb.append("🛣️ *");
        sb.append(str3);
        sb.append("* ➡️ *");
        sb.append(str4);
        sb.append("*\n\n");
        String sb2 = sb.toString();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.size() > 1) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                sb3.append("*OPTION ");
                sb3.append(i + 1);
                sb3.append("*\n");
                sb2 = sb3.toString();
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(sb2);
            sb4.append(prepareData(context, (BusAvailableOnJourney.DataBean.BusStopsBean) arrayList.get(i), hashMap));
            sb2 = sb4.toString();
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb2);
        sb5.append("*To Book:📞  Call* on ");
        sb5.append(str2);
        return sb5.toString();
    }


    public String prepareData(Context context, BusAvailableOnJourney.DataBean.BusStopsBean busDetail, HashMap<String, HashSet<String>> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("_*");
        sb.append(busDetail.getBusName());
        sb.append("*_\n");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append("🚌 Bus Type: ");
        sb3.append(busDetail.getBusType());
        String str = "\n";
        sb3.append(str);
        String sb4 = sb3.toString();
        String str2 = " at ";
        if (hashMap != null) {
            String str3 = "boarding";
            if (hashMap.containsKey(str3)) {
                BusAvailableOnJourney.DataBean.BusStopsBean boardingPoint = getBoardingPoint((String) ((HashSet) hashMap.get(str3)).iterator().next(), new ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean>(Collections.singleton(busDetail)));
                if (boardingPoint != null) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(sb4);
                    sb5.append("🚗 Boarding Point: ");
                    sb5.append(boardingPoint.getBoardingPoints().get(0).getBpAddress());
                    sb5.append(str2);
                    sb5.append(boardingPoint.getBoardingPoints().get(0).getBpTime());
                    sb5.append(str);
                    sb4 = sb5.toString();
                }
            }
        }
        if (hashMap != null) {
            String str4 = "dropping";
            if (hashMap.containsKey(str4)) {
                BusAvailableOnJourney.DataBean.BusStopsBean boardingPoint2 = getBoardingPoint(((String) ((HashSet) hashMap.get(str4)).iterator().next()).trim(), new ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean>(Collections.singleton(busDetail)));
                if (boardingPoint2 != null) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(sb4);
                    sb6.append("🚗 Dropping Point: ");
                    sb6.append(boardingPoint2.getBoardingPoints().get(0).getBpName());
                    sb6.append(str2);
                    sb6.append(boardingPoint2.getBoardingPoints().get(0).getBpTime());
                    sb6.append(str);
                    sb4 = sb6.toString();
                }
            }
        }
        StringBuilder sb7 = new StringBuilder();
        sb7.append(sb4);
        sb7.append("🗓️ Departure Date/Time: ");
        sb7.append(busDetail.getDepartureTime());
        String str5 = ",";
        sb7.append(str5);
        sb7.append(busDetail.getDepartureTime());
        sb7.append(str);
        String sb8 = sb7.toString();
        StringBuilder sb9 = new StringBuilder();
        sb9.append(sb8);
        sb9.append("🗓️ Arrival Date/Time: ");
        sb9.append(busDetail.getArrivalTime());
        sb9.append(str5);
        sb9.append(busDetail.getArrivalTime());
        sb9.append(str);
        String sb10 = sb9.toString();
        StringBuilder sb11 = new StringBuilder();
        sb11.append(sb10);
        sb11.append("💺 Seats Available: ");
        sb11.append(busDetail.getAvlSeats());
        String str6 = "\n\n";
        sb11.append(str6);
        String sb12 = sb11.toString();
        StringBuilder sb13 = new StringBuilder();
        sb13.append(sb12);
        sb13.append("💳 Fare: ");
        sb13.append(context.getResources().getString(R.string.rupees));
        sb13.append((busDetail.getFares().get(0)));
        sb13.append(str6);
        return sb13.toString();
    }


    private BusAvailableOnJourney.DataBean.BusStopsBean getBoardingPoint(String str, ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (str.trim().equalsIgnoreCase(((BusAvailableOnJourney.DataBean.BusStopsBean) arrayList.get(i)).getBoardingPoints().get(0).getBpName().trim())) {
                return arrayList.get(i);
            }
        }
        return null;
    }

}
