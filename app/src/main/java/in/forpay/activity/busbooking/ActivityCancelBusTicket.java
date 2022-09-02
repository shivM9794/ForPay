package in.forpay.activity.busbooking;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.busbookingadapter.CancelTicketAdapter;
import in.forpay.databinding.ActivityBusCancelTicketBinding;
import in.forpay.model.busbookingModel.BusBookDetail;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ActivityCancelBusTicket extends AppCompatActivity {

    ActivityBusCancelTicketBinding mBinding;

    private ArrayList<String> cancelTicketSeatList = new ArrayList<>();

    private Activity activity;
    private CancelTicketAdapter adapter;
    private String bookedId, booking_time, cancellationPolicy;
    private ProgressHelper progressHelper;
    private Double booking_amount;
    private ArrayList<BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean> passengerDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_cancel_ticket);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bus_cancel_ticket);

        activity = ActivityCancelBusTicket.this;
        progressHelper = new ProgressHelper(activity);
        Bundle getBundle=getIntent().getBundleExtra("bundleData");

        passengerDetails = getBundle.getParcelableArrayList(Constant.PassengerDetails);
        bookedId = getBundle.getString(Constant.BOOKED_ID);
        booking_time = getBundle.getString(Constant.BOOKING_TIME);
        cancellationPolicy = getBundle.getString(Constant.KEY_CANCELLATION_POLICY);
        booking_amount = getBundle.getDouble(Constant.BOOKING_AMOUNT);

        Utility.getCancellation_2(activity, cancellationPolicy, R.layout.item_cancellation_policy_new, mBinding.llCancellation, booking_amount, booking_time);

        setAdapter(passengerDetails);

        mBinding.imgBack.setOnClickListener(view1 -> activity.onBackPressed());

        mBinding.txtCancelTicketDetails.setOnClickListener(view1 -> {
            if (cancelTicketSeatList.size() > 0)
                cancelTicket();
            else
                Utility.showToast(activity, "Select One Tickets to Cancel");
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

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode" , Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));

        map1.put("bookedId" , bookedId); // version code
        map1.put("seatNumbers" , seatNumbers.toString());
        map1.put("amount" , mBinding.txtAmountToBeRefunded.getText().toString());

        String request = Utility.mapWrapper(activity,map1);


        getBusTicketCancel(request);

    }

    private void getBusTicketCancel(String request) {

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.CANCEL_TICKET, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseCancelTicketResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }

    }

    private void parseCancelTicketResponse(String result) {
        progressHelper.dismiss();
        mBinding.txtViewAmountToBeRefunded.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject(result);
            //JSONObject innerObj = jsonObject.getJSONObject("data");
            if(jsonObject.getString("resCode").equals("200")){
                mBinding.txtAmountToBeRefunded.setVisibility(View.GONE);
                mBinding.txtAmountToBeRefunded.setText(jsonObject.getString("refundableAmount"));
                mBinding.txtViewAmountToBeRefunded.setVisibility(View.VISIBLE);
                mBinding.txtViewAmountToBeRefunded.setText(jsonObject.getString("refundableAmount")+" Rs Will Be Refunded");
                mBinding.txtCancelTicketDetails.setText("Cancellation Now "+jsonObject.getString("refundableAmount")+" RS");
            }

            Utility.showToast(activity, jsonObject.getString("resText"), jsonObject.getString("resCode"));

        }
        catch (Exception e){

        }


    }

    private void setAdapter(ArrayList<BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean> passengerDetails) {

        mBinding.recTravelers.setLayoutManager(new LinearLayoutManager(activity));

        adapter = new CancelTicketAdapter(activity, passengerDetails, new CancelTicketAdapter.getCancelTicketItemList() {
            @Override
            public void onItemClick(boolean isItemAddedOrRemoved, BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean passengerDetailsBean) {
                if (isItemAddedOrRemoved) {

                    cancelTicketSeatList.add(passengerDetailsBean.getSeatNumber());
                } else {
                    cancelTicketSeatList.remove(passengerDetailsBean.getSeatNumber());

                }

                if (cancelTicketSeatList.size() > 0) {
                    mBinding.txtCancelTicketDetails.setVisibility(View.VISIBLE);
                    mBinding.txtCancelTicketDetails.setEnabled(true);
                } else {
                    mBinding.txtCancelTicketDetails.setVisibility(View.GONE);
                    mBinding.txtCancelTicketDetails.setEnabled(false);

                }

            }

        });
        mBinding.recTravelers.setAdapter(adapter);

    }

}
