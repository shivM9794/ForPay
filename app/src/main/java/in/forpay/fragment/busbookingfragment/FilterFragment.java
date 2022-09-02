package in.forpay.fragment.busbookingfragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import in.forpay.R;
import in.forpay.adapter.busbookingadapter.FilterAdapter;
import in.forpay.databinding.FragmentFilterBinding;
import in.forpay.model.busbookingModel.BusAvailableOnJourney;
import in.forpay.util.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment implements View.OnClickListener {

    private FragmentFilterBinding binding;
    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean> busStopsBeanArrayList;

    private FilterAdapter filterAdapter;


    private TextView[] ids;
    private View[] views;
    private String[] sortByText = {"Price: High to Low", "Price: Low to High", " Duration: Short to Long"};
    private String[] filterByText = {"AC Seater", "AC Sleeper", "Non-AC Seater", "Non-AC Sleeper"};

    private LinkedHashMap<String, Boolean> sortByTag = new LinkedHashMap<>();
    private LinkedHashMap<String, Boolean> filterByTag = new LinkedHashMap<>();
    private LinkedHashMap<String, Boolean> travel = new LinkedHashMap<>();
    private LinkedHashMap<String, Boolean> boarding = new LinkedHashMap<>();
    private LinkedHashMap<String, Boolean> dropping = new LinkedHashMap<>();


    public static FilterFragment getInstance(Bundle bundle) {

        FilterFragment filterFragment = new FilterFragment();
        filterFragment.setArguments(bundle);
        return filterFragment;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false);
        binding.setFilterDrawerView(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getBundleData();
        initItem();
        setDefaultFilterItem();
        setViewClickListeners();


    }

    private void initItem() {

        ids = new TextView[]{binding.txtSortBy, binding.txtFilterBy, binding.txtTravels, binding.txtBoardings, binding.txtDroppings};
        // views = new View[]{binding.lineSortBy, binding.lineFilterBy, binding.lineTravels, binding.lineBoardings, binding.lineDroppings};

        for (int i = 0; i < sortByText.length; i++) {
            sortByTag.put(sortByText[i], false);
        }

        for (int i = 0; i < filterByText.length; i++) {
            filterByTag.put(filterByText[i], false);

        }

    }

    private void getBundleData() {

        busStopsBeanArrayList = Objects.requireNonNull(getArguments()).getParcelableArrayList(Constant.BUS_AVAILABLE_ON_ROUTE);
        String travelData = getArguments().getString(Constant.TRAVEL);
        String boardingData = getArguments().getString(Constant.BOARDING);
        String droppingData = getArguments().getString(Constant.DROPPING);
        Gson gson = new Gson();

        Type entityType = new TypeToken<LinkedHashMap<String, Boolean>>() {
        }.getType();
        travel = gson.fromJson(travelData, entityType);
        boarding = gson.fromJson(boardingData, entityType);
        dropping = gson.fromJson(droppingData, entityType);

    }

    private void setDefaultFilterItem() {

        makeViewSelectedUnSelected(binding.txtSortBy);

        binding.recFilterData.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recFilterData.setAdapter(new FilterAdapter(getActivity(), sortByTag));


    }

    private void makeViewSelectedUnSelected(TextView txtSortBy) {


        for (int i = 0; i < ids.length; i++) {
            if (txtSortBy.getId() == ids[i].getId()) {
                ids[i].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                ids[i].setBackground(getActivity().getDrawable(R.drawable.orange_round_filter));
                //views[i].setVisibility(View.VISIBLE);
            } else {
                ids[i].setTextColor(ContextCompat.getColor(getContext(), R.color.ic_color));
                ids[i].setBackground(getActivity().getDrawable(R.drawable.transparent_round));
                // views[i].setVisibility(View.GONE);
            }
        }

    }

    private void setViewClickListeners() {


        binding.imgDrawerClose.setOnClickListener(this);
        binding.txtSortBy.setOnClickListener(this);
        binding.txtFilterBy.setOnClickListener(this);

        binding.txtTravels.setOnClickListener(this);
        binding.txtBoardings.setOnClickListener(this);
        binding.txtDroppings.setOnClickListener(this);

        binding.txtReset.setOnClickListener(this);
        binding.txtApply.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgDrawerClose:
                sendBroadCast();
                break;
            case R.id.txtSortBy:
                makeViewSelectedUnSelected(binding.txtSortBy);
                binding.recFilterData.setAdapter(new FilterAdapter(getActivity(), sortByTag));
                break;
            case R.id.txtFilterBy:
                makeViewSelectedUnSelected(binding.txtFilterBy);
                binding.recFilterData.setAdapter(new FilterAdapter(getActivity(), filterByTag));
                break;
            case R.id.txtTravels:
                makeViewSelectedUnSelected(binding.txtTravels);
                binding.recFilterData.setAdapter(new FilterAdapter(getActivity(), travel));
                break;
            case R.id.txtBoardings:
                makeViewSelectedUnSelected(binding.txtBoardings);
                binding.recFilterData.setAdapter(new FilterAdapter(getActivity(), boarding));
                break;
            case R.id.txtDroppings:
                makeViewSelectedUnSelected(binding.txtDroppings);
                binding.recFilterData.setAdapter(new FilterAdapter(getActivity(), dropping));
                break;
            case R.id.txtApply:
                ApplyFilterToList();

                break;
            case R.id.txtReset:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .attach(this)
                        .commitAllowingStateLoss();

                Intent intent = new Intent("OpenCloseDrawerAndFilterData");
                intent.putExtra(Constant.CLEAR_FILTER_ITEM, true);
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

                break;
        }
    }

    private void ApplyFilterToList() {

        LinkedHashMap<String, LinkedHashMap<String, Boolean>> finalFilter = new LinkedHashMap<>();
        finalFilter.put("Sort By", sortByTag);
        finalFilter.put("Filter By", filterByTag);
        finalFilter.put("Travel", travel);
        finalFilter.put("Boardings", boarding);
        finalFilter.put("Droppings", dropping);

        LinkedHashMap<String, String> itemList = new LinkedHashMap<>();
        for (Map.Entry<String, LinkedHashMap<String, Boolean>> entry : finalFilter.entrySet()) {

            String key = entry.getKey();
            LinkedHashMap<String, Boolean> list = entry.getValue();

            for (Map.Entry<String, Boolean> item : list.entrySet()) {
                if (item.getValue()) {
                    itemList.put(key, item.getKey());
                    break;
                }
            }

        }


        Log.e("FinalFilter ::", itemList.size() + "");

        Gson gson = new Gson();
        String filterList = gson.toJson(itemList);
        Intent intent = new Intent("OpenCloseDrawerAndFilterData");
        if (itemList.size() == 0) {
            intent.putExtra(Constant.CLEAR_FILTER_ITEM, true);
        } else {
            intent.putExtra(Constant.FILTER_TAG_LIST, filterList);
        }
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);


    }

    private void sendBroadCast() {

        Intent intent = new Intent("OpenCloseDrawerAndFilterData");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }


}
