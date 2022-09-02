package in.forpay.activity.shop;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RatingBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import in.forpay.R;
import in.forpay.databinding.ActivityOrderProcessingBinding;
import in.forpay.databinding.DialogDispatchBinding;
import in.forpay.databinding.DialogEnterDeliveryCodeBinding;
import in.forpay.model.shop.GenerateTrackingIdModel;
import in.forpay.model.shop.OrderStatusModel;
import in.forpay.model.shop.ShopCategoryModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class OrderProcessingActivity extends AppCompatActivity {

    private ActivityOrderProcessingBinding binding;
    private AppCompatActivity activity;
    private ProgressHelper progressHelper;
    private String orderId;
    private OrderStatusModel model;
    private ArrayList<OrderStatusModel.DataBean.StatusArrayBean> arrayList;
    private OrderStatusModel.DataBean.StatusArrayBean statusArrayBean;
    private String trackingId,deliveryType,name,deliveryExpectedDays,deliveryCode,rating="";
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderProcessingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);

        orderId=getIntent().getStringExtra(Constant.ORDER_ID);

        init();
    }

    private void init() {
        orderStatus();

        binding.backPress.setOnClickListener(v->onBackPressed());

        binding.accepted.setOnClickListener(v-> {
            if (binding.paid.isChecked()) {
                status = "ACCEPTED";
                updateOrderStatus();
            }else
                binding.accepted.setChecked(false);
        });
        binding.processing.setOnClickListener(v-> {
            if (binding.accepted.isChecked()) {
                status = "PROCESSING";
                updateOrderStatus();
            }else
                binding.processing.setChecked(false);
        });
        binding.dispatched.setOnClickListener(v-> {
            if (binding.processing.isChecked()) {
                generateTrackingId();
            }
            else
                binding.dispatched.setChecked(false);
        });
        binding.delivered.setOnClickListener(v-> {
            if (binding.dispatched.isChecked())
                openDeliveryCodeDialog();
            else
                binding.delivered.setChecked(false);
        });

        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating= String.valueOf(v);
            }
        });

        binding.submit.setOnClickListener(v-> rateUser());
    }

    private void orderStatus() {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("orderId",orderId);

            String request = Utility.mapWrapper(this, map1);
            orderStatusRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void orderStatusRequest(String request) {
        if (Utility.isNetworkConnected(this)) {

            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_ORDER_STATUS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseOrderStatusResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseOrderStatusResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            model=new Gson().fromJson(response, OrderStatusModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                Log.d("OrderStatusResponse","response "+response);
                arrayList=new ArrayList<>();
                if (model.getData().getStatusArray()!=null)
                    arrayList.addAll(model.getData().getStatusArray());

                for (OrderStatusModel.DataBean.StatusArrayBean bean: arrayList){
                    if (bean.getStatus().equalsIgnoreCase("PENDING")){
                        if (bean.getChecked().equalsIgnoreCase("yes"))
                            binding.pending.setChecked(true);
                    }else if (bean.getStatus().equalsIgnoreCase("PAID")){
                        if (bean.getChecked().equalsIgnoreCase("yes"))
                            binding.paid.setChecked(true);
                    }else if (bean.getStatus().equalsIgnoreCase("ACCEPTED")){
                        if (bean.getChecked().equalsIgnoreCase("yes"))
                            binding.accepted.setChecked(true);
                    }else if (bean.getStatus().equalsIgnoreCase("PROCESSING")){
                        if (bean.getChecked().equalsIgnoreCase("yes"))
                            binding.processing.setChecked(true);
                    }else if (bean.getStatus().equalsIgnoreCase("DISPATCHED")){
                        if (bean.getChecked().equalsIgnoreCase("yes"))
                            binding.dispatched.setChecked(true);
                    }else if (bean.getStatus().equalsIgnoreCase("DELIVERED")){
                        if (bean.getChecked().equalsIgnoreCase("yes"))
                            binding.delivered.setChecked(true);
                    }
                }

                try {
                    binding.deliveredDate.setText(Utility.convertDate(model.getData().getDeliveredTime()));

                    if (!model.getData().getRating().isEmpty())
                        binding.ratingBar.setRating(Float.parseFloat(model.getData().getRating()));

                    binding.review.setText(model.getData().getRatingText());
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                Utility.showToast(activity, model.getResText(), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOrderStatus() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("orderId",orderId);
            map1.put("status",(status==null ?statusArrayBean.getStatus(): status));
            map1.put("deliveryType",(deliveryType==null ? model.getData().getDeliveryType(): deliveryType));
            map1.put("name",(name==null ? "":name));
            map1.put("trackingId",(trackingId==null ? model.getData().getTrackingId() : trackingId));
            map1.put("deliveryExpectedDays",(deliveryExpectedDays==null ? model.getData().getDeliveryExpectedDays() :deliveryExpectedDays));
            map1.put("deliveryCode",(deliveryCode==null ? "" :deliveryCode));
            map1.put("deliveryCode",(deliveryCode==null ? "" :deliveryCode));

            String request = Utility.mapWrapper(this, map1);

            updateOrderStatusRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void updateOrderStatusRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_UPDATE_ORDER_STATUS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseUpdateOrderStatusResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    @SuppressLint("LongLogTag")
    private void parseUpdateOrderStatusResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            OrderStatusModel model=new Gson().fromJson(response, OrderStatusModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                Log.d("UpdateOrderStatusResponse","response "+response);
                if (status!=null) {
                    switch (status) {
                        case "ACCEPTED":
                            binding.accepted.setChecked(true);
                            break;
                        case "PROCESSING":
                            binding.processing.setChecked(true);
                            break;
                        case "DISPATCHED":
                            binding.dispatched.setChecked(true);
                            break;
                        case "DELIVERED":
                            binding.delivered.setChecked(true);
                            break;
                    }
                }
            } else {
                Utility.showToast(activity, model.getResText(), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDispatchDialog(){
        View view= LayoutInflater.from(activity).inflate(R.layout.dialog_dispatch,null);
        DialogDispatchBinding bind=DialogDispatchBinding.bind(view);
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (trackingId!=null) {
            bind.trackingNumberDeliveryBoy.setText(trackingId);
            bind.trackingNumberCourier.setText(trackingId);
        }

        bind.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.deliveryBoyRadioBtn:{
                        bind.deliveryBoyLayout.setVisibility(View.VISIBLE);
                        bind.courierLayout.setVisibility(View.GONE);
                        deliveryType="DELIVERY BOY";
                        break;
                    }
                    case R.id.courierRadioBtn:{
                        bind.courierLayout.setVisibility(View.VISIBLE);
                        bind.deliveryBoyLayout.setVisibility(View.GONE);
                        deliveryType="COURIER";
                        break;
                    }
                }
            }
        });

        bind.submit.setOnClickListener(v->{
            if (deliveryType.equals("DELIVERY BOY")) {
                if (bind.personName.getText().toString().isEmpty())
                    bind.personName.setError("Please Enter Person Name");
                else if(bind.trackingNumberDeliveryBoy.getText().toString().isEmpty())
                    bind.trackingNumberDeliveryBoy.setError("Please Enter Tracking Number");
                else if (bind.deliveryExpectedDays.getText().toString().isEmpty())
                    bind.deliveryExpectedDays.setError("Please Enter DeliveryExpectedDays");
                else {
                    name = bind.personName.getText().toString();
                    trackingId = bind.trackingNumberDeliveryBoy.getText().toString();
                    deliveryExpectedDays = bind.deliveryExpectedDays.getText().toString();
                }
            }
            else {
                if (bind.courierName.getText().toString().isEmpty())
                    bind.courierName.setError("Please Enter Courier Name");
                else if (bind.trackingNumberCourier.getText().toString().isEmpty())
                    bind.trackingNumberCourier.setError("Please Enter Tracking Number");
                else {
                    name = bind.courierName.getText().toString();
                    trackingId=bind.trackingNumberCourier.getText().toString();
                }
            }
            status="DISPATCHED";
            updateOrderStatus();
            dialog.dismiss();
        });

        bind.cancel.setOnClickListener(v->{
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openDeliveryCodeDialog(){
        View view= LayoutInflater.from(activity).inflate(R.layout.dialog_enter_delivery_code,null);
        DialogEnterDeliveryCodeBinding bind=DialogEnterDeliveryCodeBinding.bind(view);
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bind.submit.setOnClickListener(v->{
            deliveryCode=bind.deliveryCode.getText().toString();
            if (!deliveryCode.isEmpty()){
                status="DELIVERED";
                dialog.dismiss();
                updateOrderStatus();

            }else {
                bind.deliveryCode.setError("Please Enter Delivery Code");
            }
        });

        bind.cancel.setOnClickListener(v->{
            dialog.dismiss();
        });

        dialog.show();
    }

    private void generateTrackingId() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("orderId",orderId);

            String request = Utility.mapWrapper(this, map1);

            generateTrackingIdRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void generateTrackingIdRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_GENERATE_TRACKING_ID, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseGenerateTrackingIdResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    @SuppressLint("LongLogTag")
    private void parseGenerateTrackingIdResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            GenerateTrackingIdModel model=new Gson().fromJson(response, GenerateTrackingIdModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                Log.d("GenerateTrackingIdResponse","response "+response);
                trackingId=model.getTrackingId();

                openDispatchDialog();

            } else {
                Utility.showToast(activity, model.getResText(), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rateUser() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("orderId",orderId);
            map1.put("rating",rating);
            map1.put("ratingText",binding.review.getText().toString());

            String request = Utility.mapWrapper(this, map1);


            rateUserRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void rateUserRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_RATE_USER, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseRateUserResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseRateUserResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("RateUserResponse","response "+response);

            ShopCategoryModel shopCategory= new Gson().fromJson(response, ShopCategoryModel.class);
            if (shopCategory.getResCode().equals(Constant.CODE_200)) {
                onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}