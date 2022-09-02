package in.forpay.activity.shop;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import in.forpay.R;
import in.forpay.databinding.ActivityLocationSelectorBinding;
import in.forpay.databinding.DialogConfirmLocationBinding;
import in.forpay.util.Constant;


public class LocationSelectorActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ActivityLocationSelectorBinding binding;
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    private static final int LOCATION_PICKER_ID = 78;
    private static final String FORM_VIEW_INDICATOR = "FormToFill";
    private String cityName, subLocality, currentAddress = "";
    private GoogleMap gMap;
    private GoogleApiClient googleApiClient;
    private Location lastKnownLocation;
    private int formToFill;
    private AppCompatActivity activity;

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationSelectorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;

        setupGoogleApiClient();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        formToFill = intent.getIntExtra(FORM_VIEW_INDICATOR, -1);

        binding.addLocation.setOnClickListener(view -> selectLocation());
        binding.backPress.setOnClickListener(view -> finish());
    }

    private void selectLocation() {
        LatLng selectedLocation = gMap.getCameraPosition().target;
        if (currentAddress.isEmpty() || currentAddress.equals("not Available")) {
            Toast.makeText(activity, "Please Pick location", Toast.LENGTH_SHORT).show();
        } else {
            openConfirmLocationDialog(selectedLocation);
        }
    }

    private void openConfirmLocationDialog(LatLng selectedLocation) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_confirm_location, null);
        DialogConfirmLocationBinding bind = DialogConfirmLocationBinding.bind(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bind.address.setText(currentAddress);
        bind.confirmAddress.setOnClickListener(v -> {
            Intent intent = new Intent();
            //intent.putExtra(FORM_VIEW_INDICATOR, formToFill);
            intent.putExtra(Constant.LOCATION_ADDRESS, bind.address.getText().toString());
            //intent.putExtra(Constant.LOCATION_LAT_LNG, selectedLocation);
            intent.putExtra(Constant.LOCATION_LATITUDE, Double.toString(selectedLocation.latitude));
            intent.putExtra(Constant.LOCATION_LONGITUDE, Double.toString(selectedLocation.longitude));
            intent.putExtra(Constant.LOCATION_CITY, cityName);
            intent.putExtra(Constant.LOCATION_SUB_LOCALITY, subLocality);
            setResult(Activity.RESULT_OK, intent);
            finish();

            dialog.dismiss();
        });

        dialog.show();
    }

    private void setupGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }
    }

    private void updateLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            return;
        }
        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        gMap.setMyLocationEnabled(true);

        if (lastKnownLocation != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 15f)
            );
            gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
        }
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
                                    currentAddress = address;
                                }
                            } else {
                                textView.setText("not Available");
                                currentAddress = "not Available";
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateLastLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
