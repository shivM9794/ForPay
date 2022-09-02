package in.forpay.activity.shop;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.forpay.R;
import in.forpay.databinding.ActivitySoldOrderDetailBinding;
import in.forpay.databinding.DialogDeliveryCodeBinding;
import in.forpay.databinding.DialogPayNowBinding;
import in.forpay.model.shop.ShopCategoryModel;
import in.forpay.model.shop.SoldOrderDetailModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.GPSTracker;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class SoldOrderDetailActivity extends AppCompatActivity {

    private ActivitySoldOrderDetailBinding binding;
    private AppCompatActivity activity;
    private ProgressHelper progressHelper;
    private String cityName="",address="",latitude="",longitude="",deliveryCode,deliveryLatitude,deliveryLongitude;
    private ArrayList<Object> arrayList;
    private SoldOrderDetailModel.DataBean model;
    private SoldOrderDetailModel.DataBean.OrderHistoryBean orderHistory;
    private ArrayList<SoldOrderDetailModel.DataBean.OrderHistoryBean.ItemsBean> items;
    private int position;
    private String orderId,image,deliveryCharge,totalCost;
    private String TAG="SoldOrderDetailActivity";
    private boolean isFromSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySoldOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);

        orderId=getIntent().getStringExtra(Constant.ORDER_ID);
        position=getIntent().getIntExtra(Constant.POSITION,0);
        isFromSeller=getIntent().getBooleanExtra(Constant.IS_FROM_SELLER,false);

        init();
    }

    private void init() {
        getShopOrderDetail();

        binding.deliveryCode.setOnClickListener(v->openDeliveryCodeDialog());
        binding.trackOrder.setOnClickListener(v->getCurrentLocation());
        binding.locationSelector.setOnClickListener(v->{
            if (isFromSeller){
                startActivity(new Intent(activity, LocationShopActivity.class)
                        .putExtra(Constant.DESTINATION_LATITUDE,deliveryLatitude)
                        .putExtra(Constant.DESTINATION_LONGITUDE,deliveryLongitude)
                        .putExtra(Constant.SHOP_NAME,""));
            }else {
                startActivityForResult((new Intent(activity,LocationSelectorActivity.class)),Constant.ACTIVITY_RESULT_LOCATION_SELECTOR);
            }
        });

        if (isFromSeller)
            binding.payNow.setEnabled(false);
        binding.payNow.setOnClickListener(v->openPayNowDialog());
        binding.backPress.setOnClickListener(v->onBackPressed());

        binding.add.setOnClickListener(v->{
            startActivity(new Intent(activity,DetailsOrderActivity.class)
                    .putExtra(Constant.ITEMS_LIST,items)
                    .putExtra(Constant.SHOP_IMAGE,image)
                    .putExtra(Constant.DELIVERY_CHARGE,deliveryCharge)
                    .putExtra(Constant.TOTAL_COST,totalCost)
            .putExtra(Constant.IS_FROM_ORDER_DETAIL,true));
        });
    }

    private void getShopOrderDetail() {
        if (Utility.isNetworkConnected(this)) {


            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("orderId",orderId);

            String request = Utility.mapWrapper(this, map1);


            shopOrderDetailRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void shopOrderDetailRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_SHOP_ORDER_DETAILS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseShopOrderDetailResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseShopOrderDetailResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("ShopOrderDetailResponse","response "+response);

            SoldOrderDetailModel models= new Gson().fromJson(response, SoldOrderDetailModel.class);

            if (models.getResCode().equals(Constant.CODE_200)) {
                /*arrayList=new ArrayList<>();
                arrayList.add(models.getData());*/

                model=models.getData();
                orderHistory=models.getData().getOrderHistory();
                items=new ArrayList<>();
                if (models.getData().getOrderHistory()!=null) {
                    items.addAll(models.getData().getOrderHistory().getItems());
                    binding.itemName.setText(items.get(0).getItemName());
                    binding.amount.setText("Rs. " + items.get(0).getAmount());
                }

                binding.shopName.setText(model.getShopName());
                binding.trackingNumber.setText(model.getTrackingId());
                binding.deliveryLocation.setText(model.getDeliveryAddress());
                binding.deliveryAt.setText(model.getDeliveredTime());
                binding.deliveryBy.setText(model.getDeliveryType());
                deliveryCode=model.getDeliveryCode();
                binding.deliveryCode.setText(deliveryCode);
                image=model.getImgUrl();
                Utility.imageLoader(activity,image,binding.mainImage);

                deliveryLatitude=models.getData().getLatitude();
                deliveryLongitude=models.getData().getLongitude();

                binding.orderId.setText(orderHistory.getOrderId());
                deliveryCharge=orderHistory.getDeliveryCharge();
                totalCost=orderHistory.getTotalCost();
                binding.deliveryCharge.setText(deliveryCharge);
                binding.netCharge.setText(totalCost);
                binding.orderTime.setText(orderHistory.getOrderTime());
                binding.address.setText(orderHistory.getAddress());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode==Constant.ACTIVITY_RESULT_LOCATION_SELECTOR) {
            if (data!=null){
                //String city = data.getStringExtra(Constant.LOCATION_CITY);
                //String subLocality = data.getStringExtra(Constant.LOCATION_SUB_LOCALITY);
                address = data.getStringExtra(Constant.LOCATION_ADDRESS);
                latitude=data.getStringExtra(Constant.LOCATION_LATITUDE);
                longitude=data.getStringExtra(Constant.LOCATION_LONGITUDE);
                setDeliveryLocation(latitude,longitude);
            }
        }
        else if (requestCode==Constant.ACTIVITY_RESULT_GPS){
            getLatLong();
        }
    }

    private void openDeliveryCodeDialog(){
        View view= LayoutInflater.from(activity).inflate(R.layout.dialog_delivery_code,null);
        DialogDeliveryCodeBinding bind=DialogDeliveryCodeBinding.bind(view);
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (deliveryCode!=null)
            bind.deliveryCode.setText(deliveryCode);
        bind.ok.setOnClickListener(v->dialog.dismiss());

        dialog.show();
    }

    private void setDeliveryLocation(String latitude, String longitude) {
        if (Utility.isNetworkConnected(this)) {


            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("orderId",orderId);
            map1.put("latitude",latitude);
            map1.put("longitude",longitude);
            map1.put("address",address);

            String request = Utility.mapWrapper(this, map1);


            setDeliveryLocationRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void setDeliveryLocationRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_SET_DELIVERY_LOCATION, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseSetDeliveryLocationResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseSetDeliveryLocationResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("SetDeliveryLocation","response "+response);

            ShopCategoryModel model= new Gson().fromJson(response, ShopCategoryModel.class);

            if (model.getResCode().equals(Constant.CODE_200)) {
                binding.deliveryLocation.setText(address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPayNowDialog(){
        View view= LayoutInflater.from(activity).inflate(R.layout.dialog_pay_now,null);
        DialogPayNowBinding bind=DialogPayNowBinding.bind(view);
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bind.ok.setOnClickListener(v->{
            payOrder();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void payOrder() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("orderId",orderId);

            String request = Utility.mapWrapper(this, map1);


            payOrderRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void payOrderRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_PAY_ORDER, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parsePayOrderResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parsePayOrderResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("ShopOrderDetailResponse","response "+response);

            ShopCategoryModel model= new Gson().fromJson(response, ShopCategoryModel.class);

            if (model.getResCode().equals(Constant.CODE_200)) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCurrentLocation() {
        progressHelper.show();
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
            Log.d(TAG, "VERSION: Build.VERSION.SDK_INT >= 23");
        }
        else {
            getLatLong();
        }
    }
    public void getLatLong(){
        progressHelper.dismiss();
        final GPSTracker gps = new GPSTracker(activity);
        if (gps.canGetLocation()) {
            Log.d(TAG, "LatLong: canGetLocation");
            latitude = Double.toString(gps.getLatitude());
            longitude = Double.toString(gps.getLongitude());

            startActivity(new Intent(activity,LocationDeliveryActivity.class)
                    .putExtra(Constant.SOURCE_LATITUDE,latitude)
                    .putExtra(Constant.SOURCE_LONGITUDE,longitude)
                    .putExtra(Constant.ORDER_ID,orderId));
        }
        else {
            Log.d(TAG, "LatLong: canNotGetLocation");
            progressHelper.dismiss();
            gps.showSettingsAlert();
        }
    }

    private void checkPermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                getLatLong();
                Log.d(TAG, "checkPermission: onPermissionGranted");

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Log.d(TAG, "checkPermission: onPermissionDenied");
                Utility.showToast(activity, "Permission Denied", "");
                progressHelper.dismiss();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                        "Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }
}
