package in.forpay.fragment.shop.mainshop;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.adapter.shop.ShopAdapter;
import in.forpay.databinding.FragmentShopMapBinding;
import in.forpay.listener.TaskLoadedCallback;
import in.forpay.model.shop.ShopModel;
import in.forpay.util.Constant;
import in.forpay.util.FetchURL;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class ShopMapFragment extends Fragment implements OnMapReadyCallback {

    private FragmentShopMapBinding binding;
    private Activity activity;
    private ArrayList<Object> arrayList=new ArrayList<>();
    private static final String TAG = "ShopMapFragment";
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    public ShopMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_shop_map, container, false);
        activity=getActivity();

        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        init();

        return binding.getRoot();
    }

    private void init() {
    }

    public void setData(){

    }

    public void filterData(ArrayList<Object> list){
        arrayList=list;
        mMap.clear();
        if (arrayList.size()==0) {
            binding.noDataFound.setVisibility(View.VISIBLE);
        }else {
            binding.noDataFound.setVisibility(View.GONE);
            try {
                for (int i = 0; i < this.arrayList.size(); i++) {

                    ShopModel.DataBean model = (ShopModel.DataBean) arrayList.get(i);
                    createMarker(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()), model.getShopName());
                }

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(Double.parseDouble(((ShopModel.DataBean)arrayList.get(0)).getLatitude()),Double.parseDouble(((ShopModel.DataBean)arrayList.get(0)).getLongitude())))
                        .zoom(0)
                        .bearing(0)
                        .tilt(45)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void noDataFound(){
        mMap.clear();
        binding.noDataFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
        mMap.setMyLocationEnabled(true);

        mMap.setOnMarkerClickListener(marker -> true);
    }

    private Marker createMarker(double latitude, double longitude, String title) {

        LinearLayout tv = (LinearLayout) this.getLayoutInflater().inflate(R.layout.layout_marker, null, false);
        TextView textView = tv.findViewById(R.id.title);
        textView.setText(title);

        tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        tv.setDrawingCacheEnabled(true);
        tv.buildDrawingCache();
        Bitmap bm = tv.getDrawingCache();

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(bm)));
    }
}