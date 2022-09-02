package in.forpay.activity.shop;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.downloader.internal.DownloadTask;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import in.forpay.R;
import in.forpay.databinding.ActivityLocationAddBinding;
import in.forpay.util.Constant;
import in.forpay.util.Utility;


public class LocationAddActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private ActivityLocationAddBinding binding;
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    private static final String FORM_VIEW_INDICATOR = "FormToFill";
    private String  cityName,subLocality;
    private GoogleMap gMap;
//    private GoogleApiClient googleApiClient;
    private Location lastKnownLocation;
    private int formToFill;
    private AppCompatActivity activity;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLocationAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;

        Utility.getCurrentLocation(activity,true);
//        setupGoogleApiClient();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        formToFill = intent.getIntExtra(FORM_VIEW_INDICATOR, -1);

        binding.addLocation.setOnClickListener(view -> selectLocation());
        binding.backPress.setOnClickListener(view -> finish());
    }

    private void selectLocation() {

        try {
            LatLng selectedLocation = gMap.getCameraPosition().target;
            String selectedAddress = binding.currentAddress.getText().toString();
            if (selectedAddress.equals("Add your Location") || selectedAddress.equals("not Available")) {
                Toast.makeText(activity, "Please Pick location", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
//            intent.putExtra(FORM_VIEW_INDICATOR, formToFill);
                intent.putExtra(Constant.LOCATION_ADDRESS, selectedAddress);
//            intent.putExtra(Constant.LOCATION_LAT_LNG, selectedLocation);
                intent.putExtra(Constant.LOCATION_LATITUDE, Double.toString(selectedLocation.latitude));
                intent.putExtra(Constant.LOCATION_LONGITUDE, Double.toString(selectedLocation.longitude));
                intent.putExtra(Constant.LOCATION_CITY, cityName);
                intent.putExtra(Constant.LOCATION_SUB_LOCALITY, subLocality);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
        catch (Exception e){
            Utility.showToastLatest(activity, "Location Not Available","ERROR");
        }
    }

//    private void setupGoogleApiClient() {
//        if (googleApiClient == null) {
//            googleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .addApi(Places.GEO_DATA_API)
//                    .build();
//        }
//    }

    private void updateLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            return;
        }
        LocationServices.getFusedLocationProviderClient(LocationAddActivity.this).getLastLocation().addOnSuccessListener(LocationAddActivity.this,location -> {
            if (location!=null) {
                lastKnownLocation = location;
                gMap.setMyLocationEnabled(true);

                if (lastKnownLocation != null) {
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15f)
                    );
                    gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
                }
                else{
                    Double lati=0.0;
                    Double _long=0.0;
                    try {

                        lati = Double.parseDouble(Utility.getLatitude(this)); //
                        _long = Double.parseDouble(Utility.getLongitude(this)); //
                    } catch (NumberFormatException e) {
                        // p did not contain a valid double
                    }

                    gMap.addMarker(new MarkerOptions().position(new LatLng(lati,_long)).title("Choose Location"));
                }
            }
            else{
                Double lati=0.0;
                Double _long=0.0;
                try {

                    lati = Double.parseDouble(Utility.getLatitude(this)); //
                    _long = Double.parseDouble(Utility.getLongitude(this)); //
                } catch (NumberFormatException e) {
                    // p did not contain a valid double
                }

                gMap.addMarker(new MarkerOptions().position(new LatLng(lati,_long)).title("Choose Location"));
            }
        }); // .FusedLocationApi.getLastLocation(googleApiClient);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        updateLastLocation();
        setupMapOnCameraChange();

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLastLocation();
            } else {
                // TODO: 23/6/2020 Tell user to use GPS
            }
        }
    }

    private void setupMapOnCameraChange() {
        gMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng center = gMap.getCameraPosition().target;
                fillAddress(binding.currentAddress, center);
            }
        });
    }

    private void fillAddress(final TextView textView, final LatLng latLng) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                    final List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!addresses.isEmpty()) {
                                if (addresses.size() > 0) {
                                    String address = addresses.get(0).getAddressLine(0);
                                    cityName = addresses.get(0).getLocality();
                                    subLocality = addresses.get(0).getSubLocality();
                                    textView.setText(address);
                                }
                            } else {
                                textView.setText("not Available");
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        updateLastLocation();
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
}
