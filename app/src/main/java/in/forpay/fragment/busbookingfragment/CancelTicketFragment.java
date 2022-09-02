package in.forpay.fragment.busbookingfragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.busbookingadapter.CancelTicketAdapter;
import in.forpay.databinding.FragmentCancelTicketBinding;
import in.forpay.model.busbookingModel.BusBookDetail;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class CancelTicketFragment extends Fragment {

    private ArrayList<String> cancelTicketSeatList = new ArrayList<>();
    private FragmentCancelTicketBinding binding;
    private Activity activity;
    private CancelTicketAdapter adapter;
    private String bookedId, booking_time, cancellationPolicy;
    private ProgressHelper progressHelper;
    private double booking_amount;
    private ArrayList<BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean> passengerDetails = new ArrayList<>();

    public CancelTicketFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        activity = getActivity();
        progressHelper = new ProgressHelper(activity);
        passengerDetails = getArguments().getParcelableArrayList(Constant.PassengerDetails);
        bookedId = getArguments().getString(Constant.BOOKED_ID);
        booking_time = getArguments().getString(Constant.BOOKING_TIME);
        cancellationPolicy = getArguments().getString(Constant.KEY_CANCELLATION_POLICY);
        booking_amount = getArguments().getDouble(Constant.BOOKING_AMOUNT);

        Utility.getCancellation_2(getContext(), cancellationPolicy, R.layout.item_cancellation_policy_new, binding.llCancellation, booking_amount, booking_time);

        setAdapter(passengerDetails);

        binding.imgBack.setOnClickListener(view1 -> activity.onBackPressed());

        binding.txtCancelTicketDetails.setOnClickListener(view1 -> {
            if (cancelTicketSeatList.size() > 0)
                cancelTicket();
            else
                Utility.showToast(getContext(), "Select One Tickets to Cancel");
        });

    }

    private void cancelTicket() {
        StringBuilder seatNumbers = new StringBuilder();

        for (int i = 0; i < cancelTicketSeatList.size(); i++) {

            if (i > 1) {
                seatNumbers.append(",");
            }
            seatNumbers.append(cancelTicketSeatList.get(i));

        }

        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(getActivity())); // key
        map1.put("imei",Utility.getImei(getActivity())); // imei
        map1.put("versionCode" , Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));

        map1.put("bookedId" , bookedId); // version code
        map1.put("seatNumbers" , seatNumbers.toString());
        map1.put("amount" , binding.txtAmountToBeRefunded.getText().toString());

        String request = Utility.mapWrapper(getActivity(),map1);


        getBusTicketCancel(request);

    }

    private void getBusTicketCancel(String request) {

        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.CANCEL_TICKET, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseCancelTicketResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect), "");
        }

    }

    private void parseCancelTicketResponse(String result) {
        progressHelper.dismiss();

        try {
            JSONObject jsonObject = new JSONObject(result);
            //JSONObject innerObj = jsonObject.getJSONObject("data");
            if(jsonObject.getString("resCode").equals("200")){
                binding.txtAmountToBeRefunded.setVisibility(View.VISIBLE);
                binding.txtAmountToBeRefunded.setText(jsonObject.getString("refundableAmount")+" Rs");
                binding.txtCancelTicketDetails.setText("Cancel Ticket");
            }

            Utility.showToast(getActivity(), jsonObject.getString("resText"), jsonObject.getString("resCode"));

        }
        catch (Exception e){

        }

      /*  CancelTicketFragment cancelTicketFragment = new CancelTicketFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.PassengerDetails, passengerDetails);
        bundle.putString(Constant.BOOKING_ID, bookingId);
        cancelTicketFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.frameContainerBusBooking, cancelTicketFragment, ShowBookingDetailsFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();*/


    }

    private void setAdapter(ArrayList<BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean> passengerDetails) {

        binding.recTravelers.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new CancelTicketAdapter(activity, passengerDetails, new CancelTicketAdapter.getCancelTicketItemList() {
            @Override
            public void onItemClick(boolean isItemAddedOrRemoved, BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean passengerDetailsBean) {
                if (isItemAddedOrRemoved) {
                    cancelTicketSeatList.add(passengerDetailsBean.getSeatNumber());
                } else {
                    cancelTicketSeatList.remove(passengerDetailsBean.getSeatNumber());

                }

                if (cancelTicketSeatList.size() > 0) {
                    binding.txtCancelTicketDetails.setEnabled(true);
                } else {
                    binding.txtCancelTicketDetails.setEnabled(false);

                }

            }

        });
        binding.recTravelers.setAdapter(adapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cancel_ticket, container, false);

        return binding.getRoot();
    }
}