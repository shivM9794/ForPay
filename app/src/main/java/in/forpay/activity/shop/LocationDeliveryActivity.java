package in.forpay.activity.shop;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityLocationDeliveryBinding;
import in.forpay.listener.TaskLoadedCallback;
import in.forpay.model.shop.StartDeliveryModel;
import in.forpay.model.shop.TrackingOrderModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.FetchURL;
import in.forpay.util.Utility;

public class LocationDeliveryActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, OnMapReadyCallback, TaskLoadedCallback {

    private ActivityLocationDeliveryBinding binding;
    private Activity activity;
    private static LatLng source = null;
    private static LatLng destination = null;
    private static LatLng parcel = null;
    private GoogleMap mMap;
    private MarkerOptions place1, place2, place3;
    private Polyline currentPolyline;
    private SupportMapFragment mapFragment;
    private boolean isFirstTime = true;
    private static final String TAG = "LocationShopActivity";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private String trackingId,orderId;
    private double latitude, longitude;
    private Handler handler;
    private Marker parcelMark, destinationMark,sourceMark;
    private BitmapDescriptor parcelImg,sourceImg,destinationImg;
    private boolean isFromStartDelivery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationDeliveryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;

        trackingId = getIntent().getStringExtra(Constant.TRACKING_ID);
        orderId = getIntent().getStringExtra(Constant.ORDER_ID);
        isFromStartDelivery=getIntent().getBooleanExtra(Constant.IS_FROM_START_DELIVERY,false);
        try {
            latitude = Double.parseDouble(getIntent().getStringExtra(Constant.SOURCE_LATITUDE));
            longitude = Double.parseDouble(getIntent().getStringExtra(Constant.SOURCE_LONGITUDE));
            parcel = new LatLng(latitude, longitude);
        }catch (Exception e){
            e.printStackTrace();
        }

        binding.trackingId.setText(trackingId!=null ? trackingId: "Track Order");

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        binding.backPress.setOnClickListener(view -> finish());

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        parcelImg = BitmapDescriptorFactory.fromResource(R.drawable.parcel_img);
        sourceImg = BitmapDescriptorFactory.fromResource(R.drawable.source_img);
        destinationImg = BitmapDescriptorFactory.fromResource(R.drawable.destination_img);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        MarkerOptions sourceMarkOption = new MarkerOptions()
                .position(source == null ? new LatLng(70, 70) : source)
                .icon(sourceImg)
                .anchor(0.5f, 1);
        sourceMark=mMap.addMarker(sourceMarkOption);

        MarkerOptions parcelMarkOption = new MarkerOptions()
                .position(parcel == null ? new LatLng(70, 70) : parcel)
                .icon(parcelImg)
                .anchor(0.5f, 1);
        parcelMark = mMap.addMarker(parcelMarkOption);

        MarkerOptions destinationMarkOption = new MarkerOptions()
                .position(destination == null ? new LatLng(70, 70) : destination)
                .icon(destinationImg)
                .anchor(0.5f, 1);
        destinationMark=mMap.addMarker(destinationMarkOption);

    }

    private void startDelivery(double latitude, double longitude) {
        if (Utility.isNetworkConnected(activity)) {


            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));
            map1.put("trackingId",trackingId);
            map1.put("latitude",""+latitude);
            map1.put("longitude",""+longitude);

            String request = Utility.mapWrapper(this, map1);
            startDeliveryRequest(request);

        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void startDeliveryRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_START_DELIVERY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            parseStartDeliveryResponse(result, responseManager);
                        }
                    });
        }else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseStartDeliveryResponse(String response, ResponseManager responseManager) {
        try {
            Log.d("StartDeliveryResponse", "response " + response);

            StartDeliveryModel model = new Gson().fromJson(response, StartDeliveryModel.class);

            if (model.getResCode().equals(Constant.CODE_200)) {

                source = new LatLng(Double.parseDouble(model.getStartLat()), Double.parseDouble(model.getStartLong()));
                parcel = new LatLng(Double.parseDouble(model.getSourceLat()), Double.parseDouble(model.getSourceLong()));
                destination = new LatLng(Double.parseDouble(model.getDestinationLat()), Double.parseDouble(model.getDestinationLong()));

                setMarkerAndRouteDraw();
            }else {
                onBackPressed();
                Utility.showToast(activity, model.getResText(), "");
            }
        } catch (Exception ea) {
            ea.printStackTrace();
        }
    }

    private void trackOrder(String latitude, String longitude) {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));
            map1.put("fromLatitude", ""+latitude);
            map1.put("fromLongitude",""+longitude);
            map1.put("orderId", orderId);


            String request = Utility.mapWrapper(this, map1);
            trackOrderRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void trackOrderRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_TRACK_ORDER, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseTrackOrderResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseTrackOrderResponse(String response, ResponseManager responseManager) {
        try {
            Log.d("TrackOrderResponse","response "+response);

            TrackingOrderModel trackingOrderModel= new Gson().fromJson(response, TrackingOrderModel.class);

            if (trackingOrderModel.getResCode().equals(Constant.CODE_200)) {
                TrackingOrderModel.DataBean model =trackingOrderModel.getData();

                source = new LatLng(Double.parseDouble(model.getStartLat()), Double.parseDouble(model.getStartLong()));
                parcel = new LatLng(Double.parseDouble(model.getFromLatitude()), Double.parseDouble(model.getFromLongitude()));
                destination = new LatLng(Double.parseDouble(model.getToLatitude()), Double.parseDouble(model.getToLongitude()));

                setMarkerAndRouteDraw();
            }else {
                onBackPressed();
                Utility.showToast(activity, trackingOrderModel.getResText(), "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMarkerAndRouteDraw(){
        if (isFirstTime) {
            CameraPosition googlePlex = CameraPosition.builder()
                    .target(parcel)
                    .zoom(15)
                    .bearing(0)
                    .tilt(45)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);

            MarkerOptions sourceMarkOption = new MarkerOptions()
                    .position(source)
                    .icon(sourceImg)
                    .anchor(0.5f, 1);
            sourceMark=mMap.addMarker(sourceMarkOption);

            MarkerOptions parcelMarkOption = new MarkerOptions()
                    .position(parcel)
                    .icon(parcelImg)
                    .anchor(0.5f, 1);
            parcelMark = mMap.addMarker(parcelMarkOption);


            MarkerOptions destinationMarkOption = new MarkerOptions()
                    .position(destination)
                    .icon(destinationImg)
                    .anchor(0.5f, 1);
            destinationMark=mMap.addMarker(destinationMarkOption);


            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            callMethod();
        } else {
            sourceMark.remove();
            parcelMark.remove();
            destinationMark.remove();

            MarkerOptions sourceMarkOption = new MarkerOptions()
                    .position(source)
                    .icon(sourceImg)
                    .anchor(0.5f, 1);
            sourceMark=mMap.addMarker(sourceMarkOption);

            MarkerOptions parcelMarkOption = new MarkerOptions()
                    .position(parcel)
                    .icon(parcelImg)
                    .anchor(0.5f, 1);
            parcelMark = mMap.addMarker(parcelMarkOption);


            MarkerOptions destinationMarkOption = new MarkerOptions()
                    .position(destination)
                    .icon(destinationImg)
                    .anchor(0.5f, 1);
            destinationMark=mMap.addMarker(destinationMarkOption);

        }
    }
    private void callMethod() {
        if (isFirstTime) {
            //code to draw path on map
            place1 = new MarkerOptions().position(source).title("Location 1");
            place2 = new MarkerOptions().position(parcel).title("Location 2");
            place3 = new MarkerOptions().position(destination).title("Location 3");
            mapFragment.getMapAsync(this);
            isFirstTime = false;

            new FetchURL(activity).execute(getUrl(place1.getPosition(), place2.getPosition(), place3.getPosition(), "driving"), "driving");
        }
    }


    private String getUrl(LatLng origin, LatLng parcel, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        //Parcel
        String str_parcel = "waypoints=" + parcel.latitude + "," + parcel.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_parcel + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startLocationUpdates();

                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (mLocation == null) {
                    startLocationUpdates();
                }

                if (mLocation!=null) {
                    if (isFromStartDelivery)
                        startDelivery(mLocation.getLatitude(), mLocation.getLongitude());
                    else
                        trackOrder(Double.toString(mLocation.getLatitude()), Double.toString(mLocation.getLongitude()));
                    Log.e(TAG,"Api is Calling after 10 second");
                }else {
                    Toast.makeText(activity, "Location not Detected", Toast.LENGTH_SHORT).show();
                }
                handler.postDelayed(this,10000);
            }
        },10);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Updated Location: " + location.getLatitude() + "," + location.getLongitude());

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }
}
