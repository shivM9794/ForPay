package in.forpay.fragment.busbookingfragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.FragmentSelectBoardingDroppingPointBinding;
import in.forpay.model.CityList;
import in.forpay.model.busbookingModel.BusAvailableOnJourney;
import in.forpay.model.busbookingModel.PassengerInfo;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.PreferenceConnector;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectBoardingDroppingPoint extends Fragment implements View.OnClickListener {

    FragmentSelectBoardingDroppingPointBinding binding;

    ArrayList<PassengerInfo> passengerInfos;
    private ViewPagerAdapter adapter;
    private ArrayList<CityList.DataBean.BusStopsBean> myJourneyList;
    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean> boardingPointsBeans;
    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean.DroppingPointsBean> droppingPointsBeans;
    private BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean boardingPoint;
    BusAvailableOnJourney.DataBean.BusStopsBean.DroppingPointsBean droppingPoint;
    private String dateOfJourney, noOfSeats, busId;

    private ProgressHelper progressHelper;

    public SelectBoardingDroppingPoint() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_boarding_dropping_point, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBundleData();
        setTabLayoutAndViewPager();
        setViewClickListeners();
    }

    private void setViewClickListeners() {
        binding.txtContinue.setOnClickListener(this);
        binding.imgBack.setOnClickListener(this);

    }

    private void setTabLayoutAndViewPager() {


        adapter = new ViewPagerAdapter(this.getChildFragmentManager());

        FragmentBoarding boardingView = new FragmentBoarding();
        FragmentDropping droppingView = new FragmentDropping();

        Bundle bundleBoarding = new Bundle();
        bundleBoarding.putParcelableArrayList(Constant.BOARDING, boardingPointsBeans);
        boardingView.setArguments(bundleBoarding);

        Bundle bundleDropping = new Bundle();
        bundleDropping.putParcelableArrayList(Constant.DROPPING, droppingPointsBeans);
        droppingView.setArguments(bundleDropping);


        adapter.addFragment(boardingView, "BoardingPoints");
        adapter.addFragment(droppingView, "DroppingPoints");
        binding.viewPagerBoardingDropping.setAdapter(adapter);


        binding.tabBoardingDropping.setupWithViewPager(binding.viewPagerBoardingDropping);

    }

    private void getBundleData() {

        myJourneyList = getArguments().getParcelableArrayList(Constant.SOURCE_DESTINATION);
        dateOfJourney = getArguments().getString(Constant.DATE_OF_JOURNEY);
        noOfSeats = getArguments().getString(Constant.NO_OF_SEATS);
        busId = getArguments().getString(Constant.BUS_ID);
        boardingPointsBeans = getArguments().getParcelableArrayList(Constant.BOARDING);
        droppingPointsBeans = getArguments().getParcelableArrayList(Constant.DROPPING);
        passengerInfos = getArguments().getParcelableArrayList(Constant.PASSENGER_INFO);

        progressHelper = new ProgressHelper(getActivity());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtContinue:
                callBlockSeatApi();
                break;
            case R.id.imgBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }

    }

    private void callBlockSeatApi() {
//Name;Age;Gender;SeatNumber|Name2;Age2;Genger2;SeatNumber

        String total_amount = PreferenceConnector.readString(getActivity(), PreferenceConnector.BUS_SEAT_AMOUNT, "");
        StringBuilder passengerDetail = new StringBuilder();
        for (int i = 0; i < passengerInfos.size(); i++) {

            if (i > 0) {
                passengerDetail.append("|");
            }

            passengerDetail.append(passengerInfos.get(i).getFirstName()).append("_")
                    .append(passengerInfos.get(i).getLastName())
                    .append(";")
                    .append(passengerInfos.get(i).getAge())
                    .append(";")
                    .append(passengerInfos.get(i).getGender())
                    .append(";")
                    .append(passengerInfos.get(i).getSeatNo());

        }



        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(getActivity())); // key
        map1.put("imei",Utility.getImei(getActivity())); // imei
        map1.put("versionCode" , Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));
        map1.put("busId" , busId); //
        map1.put("bpId" , boardingPoint.getBpId());
        map1.put("dpId" , droppingPoint.getDpId());
        map1.put("customerMobile" , passengerInfos.get(0).getMobileNumber());
        map1.put("passengerDetails" , passengerDetail.toString());
        map1.put("amount" , total_amount);

        String request = Utility.mapWrapper(getActivity(),map1);


        getBlockSeatResponse(request);
    }

    /**
     * Get block seat response
     */
    //{"data":{"bookingId":"25461","blockedId":"5888ceEk"},"resCode":"200","resText":"SUCCESS"}
    private void getBlockSeatResponse(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.BUS_SEAT_BLOCK, request, new CallbackListener() {
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

        progressHelper.dismiss();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject innerObj = jsonObject.getJSONObject("data");

            if (jsonObject.getString("resCode").equals("200")) {
                String bookingId = innerObj.getString("bookingId");


                ShowBookingDetailsFragment showBookingDetailsFragment = new ShowBookingDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BOOKED_ID, bookingId);

                bundle.putString("viewType","rc");
                bundle.putParcelableArrayList(Constant.PASSENGER_INFO, passengerInfos);
               /* bundle.putParcelableArrayList(Constant.BOARDING, boardingPointsBeans);
                bundle.putParcelableArrayList(Constant.DROPPING, droppingPointsBeans);
                bundle.putParcelableArrayList(Constant.PASSENGER_INFO, passengerInfos);*/
                bundle.putParcelable(Constant.BOARDING_POINT, boardingPoint);
                bundle.putParcelable(Constant.DROPPING_POINT, droppingPoint);

                showBookingDetailsFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainerBusBooking, showBookingDetailsFragment, ShowBookingDetailsFragment.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();

                //   busBookApi(bookingId, blockedId);

            } else {
                progressHelper.dismiss();
                Utility.showToast(getContext(), jsonObject.getString("resText"), jsonObject.getString("resCode"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
            progressHelper.dismiss();
        }

    }


    public void makeViewClickable(boolean b, BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean boardingPoint) {

        if (binding.txtContinue.isEnabled()) {
            binding.txtContinue.setEnabled(false);
        }

        this.boardingPoint = boardingPoint;

    }


    public void makeViewClickableAndGetPoint(boolean b, BusAvailableOnJourney.DataBean.BusStopsBean.DroppingPointsBean droppingPoint) {

        this.droppingPoint = droppingPoint;
        if (!binding.txtContinue.isEnabled()) {
            binding.txtContinue.setEnabled(true);
        }
        binding.txtContinue.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green_new));
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        Fragment getFragment(int pos) {
            return mFragmentList.get(pos);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
