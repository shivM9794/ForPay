package in.forpay.activity.busbooking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.HomeNewActivity;
import in.forpay.adapter.busbookingadapter.AdapterPassengerDetail;
import in.forpay.databinding.ActivityBusBookingDetailsBinding;
import in.forpay.fragment.busbookingfragment.BusRouteActivity;
import in.forpay.model.busbookingModel.BusAvailableOnJourney;
import in.forpay.model.busbookingModel.BusBookDetail;
import in.forpay.model.busbookingModel.PassengerInfo;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class BusBookingDetails extends AppCompatActivity {

    private ActivityBusBookingDetailsBinding mBinding;
    ProgressHelper progressHelper;
    Activity activity;

    private ArrayList<String> cancelTicketSeatList = new ArrayList<>();
    private String bookedId, busId, blocked_id, booking_time, cancellation_policy, ticket_id, booked_status, viewType, viewFrom;

    private AdapterPassengerDetail adapterPassengerDetail;
    private double booking_amount;
    ArrayList<PassengerInfo> passengerInfos;
    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean> boardingPointsBeans;
    private ArrayList<BusAvailableOnJourney.DataBean.BusStopsBean.DroppingPointsBean> droppingPointsBeans;
    private ArrayList<BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean> passengerDetails = new ArrayList<>();
    private BusAvailableOnJourney.DataBean.BusStopsBean.BoardingPointsBean boardingPoint;
    private BusAvailableOnJourney.DataBean.BusStopsBean.DroppingPointsBean droppingPoint;
    private BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean passengerDetailsBean;
    private boolean isSeatAvailable = false;

    LinearLayout llMainBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_booking_details);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bus_booking_details);
        activity=BusBookingDetails.this;
        progressHelper=new ProgressHelper(activity);
        Bundle getBundle = getIntent().getBundleExtra("bundle");
        bookedId=getBundle.getString("orderId");


        setViewClickListeners();

        mBinding.sharebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (activity != null) {
                    shareData();
                }
            }
        });
        if(bookedId!=null) {
            getBookingDetail(bookedId);
        }
    }


    private void getBookingDetail(String id) {



        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("bookedId", id);


        String request = Utility.mapWrapper(activity, map1);


        getBookSeatDetailResponse(request);
    }

    private void getBookSeatDetailResponse(String request) {

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.BUS_BOOKED_DETAIL, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void parseResponse(String result) {
        progressHelper.dismiss();
        Log.d("busTicketDetails", result);
        BusBookDetail busBookDetail = new Gson().fromJson(result, BusBookDetail.class);

        try {
            if (busBookDetail.getResCode() != null && busBookDetail.getResCode().equals("200")) {

                String status = busBookDetail.getData().getBookingDetails().getStatus();

                if (status == null || status.isEmpty()) {
                    status = "aa";
                }


                if (status.equalsIgnoreCase("PENDING")) {
                    mBinding.btnConfirm.setVisibility(View.VISIBLE);
                    //binding.btnBusRoute.setVisibility(View.GONE);
                    mBinding.txtCancelTicket.setVisibility(View.GONE);

                } else {

                    mBinding.title.setText("Booked Ticket Details");
                    mBinding.btnConfirm.setVisibility(View.GONE);
                    //binding.btnBusRoute.setVisibility(View.VISIBLE);
                    if(!busBookDetail.getSeatAvailableForCancel().equals("0")) {
                        mBinding.txtCancelTicket.setVisibility(View.VISIBLE);
                    }

                }


                if (status.equalsIgnoreCase("PENDING")) {
                    isSeatAvailable = true;
                } else {
                    isSeatAvailable = false;
                }
                mBinding.txtBoardingName.setText(busBookDetail.getData().getBookingDetails().getSourceName());
                mBinding.txtBoardingPoint.setText(busBookDetail.getData().getBookingDetails().getBpAddress());

                mBinding.txtTravelsName.setText(busBookDetail.getData().getBookingDetails().getBusName());
                mBinding.txtTravelsType.setText(busBookDetail.getData().getBookingDetails().getBusType());
                String contact_no = busBookDetail.getData().getBookingDetails().getBusContactNumber();

                if (contact_no.isEmpty()) {
                    mBinding.txtTravelsContact.setVisibility(View.GONE);
                } else {
                    mBinding.txtTravelsContact.setVisibility(View.VISIBLE);
                    mBinding.txtTravelsContact.setText("Contact :- " + contact_no);
                }

                if(!bookedId.isEmpty()){
                    mBinding.pnrNumber.setVisibility(View.VISIBLE);
                    mBinding.pnrNumber.setText("PNR " + bookedId);
                }


                mBinding.txtDroppingName.setText(busBookDetail.getData().getBookingDetails().getDestinationName());
                mBinding.txtDroppingPoint.setText(busBookDetail.getData().getBookingDetails().getDpName());

                mBinding.txtBoardingTime.setText(busBookDetail.getData().getBookingDetails().getBoardingTime());
                mBinding.txtBoardingDate.setText(busBookDetail.getData().getBookingDetails().getBoardingDate());

                mBinding.txtDroppingTime.setText(busBookDetail.getData().getBookingDetails().getDroppingTime());
                mBinding.txtDroppingDate.setText(busBookDetail.getData().getBookingDetails().getDroppingDate());
                String startTime = "00:00";
                int minutes = Integer.parseInt(busBookDetail.getData().getBookingDetails().getTravelTime());
                int h = minutes / 60 + Integer.parseInt(startTime.substring(0, 1));
                int m = minutes % 60 + Integer.parseInt(startTime.substring(3, 4));
                String journeyDuration = h + "hr " + m + "mins";
                // String journeyDuration = getTimeDiff(boarding_time, dropping_time);
                mBinding.txtJourneyDuration.setText(busBookDetail.getData().getBookingDetails().getTravelDuration());
                booking_amount = Double.parseDouble(busBookDetail.getData().getBookingDetails().getCustomerPaid());
                mBinding.txtCustomerPay.setText("Rs." + busBookDetail.getData().getBookingDetails().getCustomerPaid());
                mBinding.txtTotalDiscount.setText("Rs." + busBookDetail.getData().getBookingDetails().getCommission());
                mBinding.txtYouPay.setText("Rs." + busBookDetail.getData().getBookingDetails().getPayableAmount());
                booking_time = busBookDetail.getData().getBookingDetails().getBookingTime();
                cancellation_policy = busBookDetail.getData().getBookingDetails().getCancellationPolicy();
                ArrayList<BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean> passengerDetailsBeans = new ArrayList<>(busBookDetail.getData().getBookingDetails().getPassengerDetails());


                passengerDetails.addAll(busBookDetail.getData().getBookingDetails().getPassengerDetails());
                setDataToView(passengerDetailsBeans);
            } else {

                Utility.showToastLatest(activity, busBookDetail.getResText(), "ERROR");
            }
        }
        catch (Exception e){

            Log.d("busTicketDetails", e.toString());
        }

    }




    private void setDataToView(ArrayList<BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean> passengerDetailsBeans) {

        mBinding.recPassengerDetail.setLayoutManager(new LinearLayoutManager(activity));
        adapterPassengerDetail = new AdapterPassengerDetail(activity, passengerDetailsBeans, new getCancelTicketItemList() {

            @Override
            public void onItemClick(boolean isItemAddedOrRemoved, BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean passengerDetailsBean, int pos) {
                if (isItemAddedOrRemoved) {
                    cancelTicketSeatList.add(passengerDetailsBean.getSeatNumber());
                } else {
                    cancelTicketSeatList.remove(passengerDetailsBean.getSeatNumber());

                }
            }
        });
        mBinding.recPassengerDetail.setAdapter(adapterPassengerDetail);

    }



    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }


    private void checkPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                //Utility.showToast(PersonalDetailActivity.this, "Location permission is necessary");
            }
        };

        TedPermission.with(activity)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                        "Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }

    public void check() {
        if (ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            //requestStoragePermission();
            checkPermission();
        }
    }

    public static void store(Bitmap bm, String fileName) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareData() {


        if (activity != null) {
            View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);

            Bitmap bm = getScreenShot(rootView);

            store(bm, "screenshot.png");

            llMainBus.setDrawingCacheEnabled(true);
            llMainBus.buildDrawingCache();
            //Bitmap bm = linearLayout.getDrawingCache();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
            byte[] byteArray = bytes.toByteArray();
            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            //String pathhh = MediaStore.Images.Media.insertImage(getContentResolver(), compressedBitmap, "", null);

            Intent intent = new Intent(Intent.ACTION_SEND);
            Bitmap loadedImage = compressedBitmap;
            String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), compressedBitmap, "", null);
            Uri screenshotUri = Uri.parse(path);

            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            String message = "Book Booking Details";

            intent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(intent, "Share image via..."));

        }

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtCancelTicket:
                parseCancelTicketResponse();
               /* if (cancelTicketSeatList.size() > 0)
                    cancelTicket();
                else
                    Utility.showToast(getContext(), "Select One Tickets to Cancel");*/
                break;
            case R.id.imgBack:
                Intent intent = new Intent(activity, HomeNewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;


        }

    }


    private void setViewClickListeners() {

        mBinding.imgBack.setOnClickListener(this::onClick);
        mBinding.txtCancelTicket.setOnClickListener(this::onClick);
        mBinding.btnConfirm.setOnClickListener(view -> {
            if (isSeatAvailable) {
                if (booked_status != null && booked_status.equalsIgnoreCase("PENDING")) {
                    busBookApi(bookedId);
                } else {
                    busBookApi(bookedId);
                }
            }


        });
        mBinding.btnBusRoute.setOnClickListener(view -> {

            Intent intent = new Intent(activity, BusRouteActivity.class);
            intent.putExtra(Constant.TICKET_ID, bookedId);
            startActivity(intent);

        });
    }
    private void busBookApi(String bookedId) {

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("operatorId","521");
        map1.put("bookedId", bookedId); //

        String request = Utility.mapWrapper(activity, map1);


        getBookSeatResponse(request);

    }

    private void getBookSeatResponse(String request) {
// {"data":{"bookingId":"joDQ5896","bookedId":"joDQ5896"},"resCode":"200","resText":"SUCCESS"}
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseBookTicketResponse(result);

                        }
                    });
        } else {
            progressHelper.dismiss();
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }


    }

    private void parseBookTicketResponse(String result) {
        if (result != null) {

            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

                if (response.getResCode().equals(Constant.CODE_200)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("outputJson", response.getData().getOutputJson());
                    bundle.putString("uniqId", response.getData().getUniqId());
                    bundle.putString("serviceType", response.getData().getType());
                    bundle.putString("extraData", response.getData().getExtraData());
                    bundle.putParcelable(Constant.INSUFFICIENTDATA, response.getData().getInsufficientData());
                    bundle.putParcelable(Constant.POPUPDATA, response.getData().getPopupData());
                    bundle.putString("offerDetails", "");

                    Utility.openCheckOutActivity(activity, response.getData().getType(), bundle);


                } else {
                    Utility.showToast(activity, response.getResText(), response.getResCode());
                }

            } else {
                Utility.showToast(this, getString(R.string.server_not_responding), "");
            }

        }
    }
    private void parseResponseBusBook(String result) {

        progressHelper.dismiss();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject innerObj = jsonObject.getJSONObject("data");

            if (jsonObject.getString("resCode").equals("200")) {

                String bookedId = innerObj.getString("bookedId");

                this.bookedId = bookedId;

                Utility.showToastLatest(activity, jsonObject.getString("resText"), "SUCCESS");

                blocked_id = "";
                mBinding.btnConfirm.setVisibility(View.GONE);
                mBinding.txtCancelTicket.setVisibility(View.VISIBLE);
                //getBookingDetail(bookedId);

            } else {
                Utility.showToastLatest(activity, jsonObject.getString("resText"), jsonObject.getString("resCode"));
            }


        } catch (Exception e) {
            e.printStackTrace();
            progressHelper.dismiss();
        }


    }

    private void parseCancelTicketResponse() {
        progressHelper.dismiss();

        Intent intent = new Intent(activity, ActivityCancelBusTicket.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.PassengerDetails, passengerDetails);
        bundle.putString(Constant.BOOKED_ID, bookedId);
        bundle.putString(Constant.KEY_CANCELLATION_POLICY, cancellation_policy);
        bundle.putString(Constant.BOOKING_TIME, booking_time);
        bundle.putDouble(Constant.BOOKING_AMOUNT, booking_amount);
        intent.putExtra("bundleData", bundle);
        activity.startActivity(intent);
        /*
        CancelTicketFragment cancelTicketFragment = new CancelTicketFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.PassengerDetails, passengerDetails);
        bundle.putString(Constant.BOOKED_ID, bookedId);
        bundle.putString(Constant.KEY_CANCELLATION_POLICY, cancellation_policy);
        bundle.putString(Constant.BOOKING_TIME, booking_time);
        bundle.putDouble(Constant.BOOKING_AMOUNT, booking_amount);
        cancelTicketFragment.setArguments(bundle);

        try {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameContainerBusBooking, cancelTicketFragment, CancelTicketFragment.class.getSimpleName())
                    .addToBackStack(null)
                    .commit();

        } catch (Exception e) {

        }



         */
    }


    public interface getCancelTicketItemList {

        void onItemClick(boolean isItemAddedOrRemoved, BusBookDetail.DataBean.BookingDetailsBean.PassengerDetailsBean passengerDetailsBean, int pos);
    }

}
