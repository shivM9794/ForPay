package in.forpay.fragment.busbookingfragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.forpay.R;
import in.forpay.databinding.FragmentSelectSeatAndTravellersBinding;
import in.forpay.model.CityList;
import in.forpay.model.busbookingModel.BusAvailableOnJourney;
import in.forpay.model.busbookingModel.PassengerInfo;
import in.forpay.model.busbookingModel.SeatLayoutModel;
import in.forpay.util.Constant;
import in.forpay.util.PreferenceConnector;


public class SelectSeatAndTravellers extends Fragment implements View.OnClickListener {


    public FragmentSelectSeatAndTravellersBinding binding;
    public ViewPagerAdapter adapter;
    private ArrayList<CityList.DataBean.BusStopsBean> myJourneyList;
    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean> boardingPointsBeans;
    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean.DroppingPointsBean> droppingPointsBeans;
    private String dateOfJourney, noOfSeats, bookedId,busId;
    private ArrayList<PassengerInfo> passengerInfos;
    private String policy;

    public SelectSeatAndTravellers() {
        // Required empty public constructor
    }


    public static SelectSeatAndTravellers newInstance(String param1, String param2) {
        SelectSeatAndTravellers fragment = new SelectSeatAndTravellers();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_seat_and_travellers, container, false);
        binding.setSelectSeatTravellers(this);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBundleData();
        setViewPager();
        setTabView();
        setViewClickListeners();

    }

    private void setViewClickListeners() {

        binding.imgBack.setOnClickListener(this);
        binding.txtContinue.setOnClickListener(this);
    }


    private void getBundleData() {


        myJourneyList = getArguments().getParcelableArrayList(Constant.SOURCE_DESTINATION);
        dateOfJourney = getArguments().getString(Constant.DATE_OF_JOURNEY);
        noOfSeats = getArguments().getString(Constant.NO_OF_SEATS);
        bookedId = getArguments().getString(Constant.BOOKED_ID);
        busId= getArguments().getString(Constant.BUS_ID);

        boardingPointsBeans = getArguments().getParcelableArrayList(Constant.BOARDING);
        droppingPointsBeans = getArguments().getParcelableArrayList(Constant.DROPPING);
        policy = getArguments().getString(Constant.KEY_CANCELLATION_POLICY);


    }

    private void setViewPager() {


        Bundle bundle = new Bundle();
        bundle.putString(Constant.BOOKED_ID, bookedId);
        bundle.putString(Constant.NO_OF_SEATS, noOfSeats);
        bundle.putString(Constant.BUS_ID, busId);
        //Log.d("Bus clicked select"," bus id from seatbooking "+busId + "busid 2");

        SeatBookingFragment seatBookingFragment = new SeatBookingFragment();
        seatBookingFragment.setArguments(bundle);

        AddTravellersFragment addTravellersFragment = new AddTravellersFragment();
        addTravellersFragment.setArguments(bundle);


        adapter = new ViewPagerAdapter(this.getChildFragmentManager());
        adapter.addFragment(seatBookingFragment, "SeatBooking");
        adapter.addFragment(addTravellersFragment, "Travellers");
        binding.viewPager.setAdapter(adapter);


        binding.tabSelectSeat.setupWithViewPager(binding.viewPager);
    }


    private void setTabView() {

        for (int i = 0; i < 2; i++) {
            Objects.requireNonNull(binding.tabSelectSeat.getTabAt(i)).setCustomView(R.layout.custom_tab);

            TextView txtTitle = binding.tabSelectSeat.getTabAt(i).getCustomView().findViewById(R.id.txtTitle);
            TextView txtSubTitle = binding.tabSelectSeat.getTabAt(i).getCustomView().findViewById(R.id.txtSubTitle);

            if (i == 0) {
                txtTitle.setText(myJourneyList.get(0).getStopName() + " -> " + myJourneyList.get(1).getStopName());
                txtSubTitle.setText(dateOfJourney);
            } else {
                txtTitle.setText("Travellers");
                txtSubTitle.setText(noOfSeats + " Seats");
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                getActivity().onBackPressed();
                break;
            case R.id.txtContinue:
                selectBoardingDroppingPoints();
                // callBlockSeat();
                break;
        }
    }

    private void selectBoardingDroppingPoints() {

        SelectBoardingDroppingPoint selectBoardingDroppingPoint = new SelectBoardingDroppingPoint();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.BOARDING, boardingPointsBeans);
        bundle.putParcelableArrayList(Constant.DROPPING, droppingPointsBeans);
        bundle.putParcelableArrayList(Constant.PASSENGER_INFO, passengerInfos);
        bundle.putParcelableArrayList(Constant.SOURCE_DESTINATION, myJourneyList);
        bundle.putString(Constant.DATE_OF_JOURNEY, dateOfJourney);
        bundle.putString(Constant.NO_OF_SEATS, noOfSeats);
        bundle.putString(Constant.BOOKED_ID, bookedId);
        bundle.putString(Constant.BUS_ID,busId);

        selectBoardingDroppingPoint.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.frameContainerBusBooking, selectBoardingDroppingPoint, SelectBoardingDroppingPoint.class.getSimpleName())
                .addToBackStack(null)
                .commit();

    }

    private void callBlockSeat() {

      /*  ArrayList<String> list = new ArrayList<>();
        list.add("key=" + Utility.getToken(getActivity())); // key
        list.add("imei=" + Utility.getImei(getActivity())); // imei
        list.add("versionCode=" + Utility.getVersionCode(getActivity())); // version code
        list.add("bookingId=" + bookingID);
        list.add("bpId=" + bpId);
        list.add("dpId=" + dpId);
        list.add("customerMobile=" + customerMobile);
        list.add("passengerDetails=" + passengerDetails);*/
    }


    public void showTicketDetail(ArrayList<SeatLayoutModel.DataBean.SeatLayoutBean> busSeatsDetails, int noOfSeats) {


        binding.txtContinue.setEnabled(false);
        binding.txtContinue.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hint_color));


        if (busSeatsDetails.size() > 0) {
            //binding.linAmountToBePay.setVisibility(View.VISIBLE);
            binding.linSelectedSeat.setVisibility(View.VISIBLE);
        } else {
            //binding.linAmountToBePay.setVisibility(View.INVISIBLE);
            binding.linSelectedSeat.setVisibility(View.GONE);
        }


        StringBuilder stringBuilder = new StringBuilder();
        double customerPay = 0, youPay = 0;
        double totalDiscount = 0.00;
        for (int i = 0; i < busSeatsDetails.size(); i++) {

            if (i >= 1) {
                stringBuilder.append(",");
            }
            totalDiscount += Double.parseDouble(busSeatsDetails.get(i).getCommission());

            binding.txtTotalDiscount.setText(totalDiscount + "");
            stringBuilder = stringBuilder.append(busSeatsDetails.get(i).getName());

            customerPay += Double.parseDouble(busSeatsDetails.get(i).getFare());
            youPay += (Double.parseDouble(busSeatsDetails.get(busSeatsDetails.size() - 1).getFare()) - totalDiscount);
        }

        binding.txtSelectedSeat.setText("Selected Seat : " + stringBuilder.toString());
        binding.txtCustomerPay.setText(customerPay + "");
        binding.txtYouPay.setText(youPay + "");

        PreferenceConnector.writeString(getActivity(), PreferenceConnector.BUS_SEAT_AMOUNT, String.valueOf(youPay));
        PreferenceConnector.writeString(getActivity(), PreferenceConnector.BUS_SEAT_COMMISSION, String.valueOf(totalDiscount));

        SpannableString content = new SpannableString("Bus Operator has the right to change the seat allocations. \n By Tapping 'Continue' you agree to the Cancellation Policy.");
        content.setSpan(new MyClickableSpan(1), 100, 120, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.txtPolicyInfo.setText(content);
        binding.txtPolicyInfo.setMovementMethod(LinkMovementMethod.getInstance());


    }

    public void makeViewClickable(boolean b, ArrayList<PassengerInfo> passengerInfos) {

        if (b) {

            if (!binding.txtContinue.isEnabled()) {
                binding.txtContinue.setEnabled(true);
            }

            binding.txtContinue.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange_new));
            this.passengerInfos = passengerInfos;
        } else {

            binding.txtContinue.setEnabled(false);
            binding.txtContinue.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hint_color));

        }

    }

    public class MyClickableSpan extends ClickableSpan {

        int pos;

        MyClickableSpan(int position) {
            this.pos = position;
        }

        @Override
        public void onClick(View widget) {

            //if (passengerInfos.get(0).getMobileNumber() != null){

         /*       Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.google.com"));
                startActivity(intent);*/

            CancellationPolicyBottomDialog cancellationPolicyBottomDialog = new CancellationPolicyBottomDialog();
            Bundle bundle2 = new Bundle();
            bundle2.putString(Constant.KEY_CANCELLATION_POLICY, policy);
            cancellationPolicyBottomDialog.setArguments(bundle2);
            cancellationPolicyBottomDialog.show(getChildFragmentManager(), cancellationPolicyBottomDialog.getTag());


            //}


        }

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
