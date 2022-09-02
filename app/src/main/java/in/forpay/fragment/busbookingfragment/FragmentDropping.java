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

import in.forpay.R;
import in.forpay.adapter.busbookingadapter.DroppingAdapter;
import in.forpay.databinding.FragmentDroppingBinding;
import in.forpay.model.busbookingModel.BusAvailableOnJourney;
import in.forpay.network.ItemClickListeners;
import in.forpay.util.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDropping extends Fragment implements ItemClickListeners {

    FragmentDroppingBinding binding;
    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean.DroppingPointsBean> droppingPointsBeans;

    public BusAvailableOnJourney.DataBean.BusStopsBean.DroppingPointsBean droppingPoint;

    private DroppingAdapter adapter;

    public FragmentDropping() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dropping, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBundleData();
        setRecyclerView();
    }

    private void setRecyclerView() {

        binding.recDroppingPoint.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DroppingAdapter(getActivity(),droppingPointsBeans,this);
        binding.recDroppingPoint.setAdapter(adapter);
    }

    private void getBundleData() {

        droppingPointsBeans = getArguments().getParcelableArrayList(Constant.DROPPING);
    }

    @Override
    public void onItemClick(int pos) {


        binding.recDroppingPoint.post(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
            }
        });
        droppingPoint = droppingPointsBeans.get(pos);


        SelectBoardingDroppingPoint selectBoardingDroppingPoint = (SelectBoardingDroppingPoint) getActivity().getSupportFragmentManager().findFragmentById(R.id.frameContainerBusBooking);
        selectBoardingDroppingPoint.makeViewClickableAndGetPoint(true, droppingPoint);


    }
}
