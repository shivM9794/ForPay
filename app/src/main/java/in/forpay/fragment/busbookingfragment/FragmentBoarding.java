package in.forpay.fragment.busbookingfragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Objects;

import in.forpay.R;
import in.forpay.adapter.busbookingadapter.BoardingAdapter;
import in.forpay.databinding.FragmentBoardingBinding;
import in.forpay.model.busbookingModel.BusAvailableOnJourney;
import in.forpay.network.ItemClickListeners;
import in.forpay.util.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBoarding extends Fragment implements ItemClickListeners {


    private FragmentBoardingBinding binding;
    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean> boardingPointsBeans;
    private BoardingAdapter boardingAdapter;


    public BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean boardingPoint;
    public FragmentBoarding() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_boarding, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBundleData();
        setRecyclerView();
    }

    private void setRecyclerView() {

        binding.recBoardingPoint.setLayoutManager(new LinearLayoutManager(getActivity()));
        boardingAdapter = new BoardingAdapter(getActivity(),boardingPointsBeans,this);
        binding.recBoardingPoint.setAdapter(boardingAdapter);

    }

    private void getBundleData() {

        assert getArguments() != null;
        boardingPointsBeans = getArguments().getParcelableArrayList(Constant.BOARDING);

    }

    @Override
    public void onItemClick(int pos) {

        SelectBoardingDroppingPoint selectBoardingDroppingPoint = (SelectBoardingDroppingPoint) Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.frameContainerBusBooking);
        assert selectBoardingDroppingPoint != null;
        selectBoardingDroppingPoint.binding.viewPagerBoardingDropping.setCurrentItem(1);

        binding.recBoardingPoint.post(new Runnable() {
            @Override
            public void run() {

                boardingAdapter.notifyDataSetChanged();
            }
        });

        boardingPoint = boardingPointsBeans.get(pos);
        selectBoardingDroppingPoint.makeViewClickable(false,boardingPoint);
    }
}
