package in.forpay.activity.shop;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.forpay.R;
import in.forpay.adapter.shop.SelectDeliveryRageAdapter;
import in.forpay.adapter.shop.ShopDrawerAdapter;
import in.forpay.adapter.shop.ShopSearchAdapter;
import in.forpay.adapter.shop.ViewPagerShopAdapter;
import in.forpay.databinding.ActivityShopBinding;
import in.forpay.databinding.DialogEnterTrackingIdBinding;
import in.forpay.fragment.shop.mainshop.ShopListFragment;
import in.forpay.fragment.shop.mainshop.ShopMapFragment;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.DrawerModel;
import in.forpay.model.shop.ShopCategoryModel;
import in.forpay.model.shop.ShopCategorySearchModel;
import in.forpay.model.shop.ShopModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.GPSTracker;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ShopActivity extends AppCompatActivity implements ItemClickListener {

    private static final String TAG = "ShopListActivity";
    private ActivityShopBinding binding;
    private AppCompatActivity activity;
    private SelectDeliveryRageAdapter selectDeliveryRageAdapter;
    private ArrayList<Object> arrayList = new ArrayList<>();
    private ArrayList<Object> tempArrayList = new ArrayList<>();
    private ArrayList<ShopCategoryModel.DataBean> shopCategoryArrayList = new ArrayList<>();
    private ArrayList<ShopCategorySearchModel> shopCategorySearchArrayList = new ArrayList<>();
    private ShopSearchAdapter shopSearchAdapter;
    private ProgressHelper progressHelper;
    private String latitude = "", longitude = "", trackingId;
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    private String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private String Lat = "", Long = "";
    private GPSTracker locationTrack;
    private ArrayList<DrawerModel> drawerArrayList;
    private ShopDrawerAdapter drawerAdapter;
    private ArrayList<String> rateList = new ArrayList<>(Arrays.asList("Minimum Rating", "1+", "2+", "3+", "4+"));
    private boolean isClickOnClearFilter, isFromStartDelivery;
    private String shopId;
    private String shopOrderModel;
    private String destinationLatitude, destinationLongitude;
    private ViewPagerShopAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        progressHelper = new ProgressHelper(activity);


        init();
    }

    private void init() {
        getCurrentLocation();
        setupDrawer();

        setViewPagerData();
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        setPagerChangeListener();

        binding.addShop.setOnClickListener(v -> binding.drawerLayout.openDrawer(Gravity.LEFT));
        binding.filter.setOnClickListener(v -> binding.drawerLayout.openDrawer(Gravity.RIGHT));

        binding.search.setOnClickListener(v -> binding.searchLayout.setVisibility(View.VISIBLE));

        binding.apply.setOnClickListener(v -> filterData());

        binding.clearFilter.setOnClickListener(v -> {
            isClickOnClearFilter = true;
            binding.clearFilter.setTextColor(ContextCompat.getColor(activity, R.color.gray_shop));
        });

        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if (binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    if (binding.minimum.getText().toString().isEmpty() && binding.maximum.getText().toString().isEmpty() && binding.rate.getText().toString().equals("Minimum Rating")) {
                        binding.clearFilter.setTextColor(ContextCompat.getColor(activity, R.color.gray_shop));
                    } else {
                        binding.clearFilter.setTextColor(ContextCompat.getColor(activity, R.color.black));
                    }
                }
                binding.minimum.setError(null);
                binding.maximum.setError(null);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        setRatingAdapter();
    }

    private void setPagerChangeListener() {
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                try {
                    Fragment fragment = viewPagerAdapter.getFragment(position);
                    if (fragment instanceof ShopListFragment) {
                        /*binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        //binding.search.setVisibility(View.VISIBLE);*/
                    } else {
                        /*binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        if (binding.searchLayout.getVisibility()==View.VISIBLE){
                        }
                        binding.search.setVisibility(View.GONE);*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setViewPagerData() {
        viewPagerAdapter = new ViewPagerShopAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(new ShopListFragment(), "List");
        viewPagerAdapter.addFragment(new ShopMapFragment(), "Map");
        binding.viewPager.setAdapter(viewPagerAdapter);
    }

    private void filterData() {
        String minimum = binding.minimum.getText().toString();
        String maximum = binding.maximum.getText().toString();
        Float rating = null;
        if (!binding.rate.getText().toString().equals("Minimum Rating"))
            rating = Float.valueOf(binding.rate.getText().toString().replace("+", ""));

        if (isClickOnClearFilter) {
            binding.minimum.setText("");
            binding.maximum.setText("");
            binding.rateSpinner.setSelection(0);
            ((ShopListFragment) viewPagerAdapter.getFragment(0)).filterData(arrayList);
            ((ShopMapFragment) viewPagerAdapter.getFragment(1)).filterData(arrayList);
            isClickOnClearFilter = false;
            binding.clearFilter.setTextColor(ContextCompat.getColor(activity, R.color.gray_shop));
            binding.drawerLayout.closeDrawer(Gravity.RIGHT);
        } else if (minimum.isEmpty() && !maximum.isEmpty()) {
            binding.minimum.setError("Please Enter Minimum value");
        } else if (!minimum.isEmpty() && maximum.isEmpty()) {
            binding.maximum.setError("Please Enter Maximum value");


        } else {
            ArrayList<Object> arrayListFilter = new ArrayList();
            try {
                if (minimum.isEmpty() && rating == null) {
                    arrayListFilter = arrayList;
                } else {
                    for (Object items : arrayList) {
                        ShopModel.DataBean dataBean = ((ShopModel.DataBean) items);
                        String distance = dataBean.getDistance().substring(0, dataBean.getDistance().indexOf("."));
                        if (!distance.isEmpty() && !minimum.isEmpty() || !dataBean.getAvgRating().isEmpty()) {
                            float deliveryRange = Float.parseFloat(distance);
                            float rateDataBean = Float.parseFloat(dataBean.getAvgRating());
                            boolean checkDistanceInRange = false;
                            if (!minimum.isEmpty()) {
                                checkDistanceInRange = deliveryRange >= Integer.parseInt(minimum) && deliveryRange <= Integer.parseInt(maximum);
                            }
                            if (!minimum.isEmpty() && rating == null) {
                                if (checkDistanceInRange) {
                                    arrayListFilter.add(items);
                                }
                            } else if (!minimum.isEmpty()) {
                                if (checkDistanceInRange && rateDataBean > rating) {
                                    arrayListFilter.add(items);
                                }
                            } else if (rateDataBean > rating) {
                                arrayListFilter.add(items);
                            }
                        }
                    }
                    binding.clearFilter.setTextColor(ContextCompat.getColor(activity, R.color.black));
                }
                binding.drawerLayout.closeDrawer(Gravity.RIGHT);

            } catch (Exception e) {
                e.printStackTrace();
                binding.drawerLayout.closeDrawer(Gravity.RIGHT);
            }
            if (arrayListFilter.size() != 0) {
                ((ShopListFragment) viewPagerAdapter.getFragment(0)).filterData(arrayListFilter);
                ((ShopMapFragment) viewPagerAdapter.getFragment(1)).filterData(arrayListFilter);
            } else {
                Utility.showToast(activity, "No Matches Found", "");
            }
        }
    }

    private void setRatingAdapter() {
        selectDeliveryRageAdapter = new SelectDeliveryRageAdapter(activity, rateList);
        binding.rateSpinner.setAdapter(selectDeliveryRageAdapter);

        binding.rateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                binding.rate.setText(adapterView.getItemAtPosition(i).toString());

                if (i == 0) {
                    binding.rate.setTextColor(ContextCompat.getColor(activity, R.color.gray_shop));
                } else
                    binding.rate.setTextColor(ContextCompat.getColor(activity, R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onItemClick(int position, String tag) {
        binding.drawerLayout.closeDrawer(Gravity.LEFT);
        switch (tag) {
            case Constant.ADD_NEW_SHOP: {
                startActivity(new Intent(activity, Shop_CategoryActivity.class));
                break;
            }
            case Constant.EDIT_YOUR_SHOP: {
                startActivity(new Intent(activity, AddShopActivity.class)
                        .putExtra(Constant.IS_FROM_EDIT_YOUR_SHOP, true));
                break;
            }
            case Constant.SHOP_LOCAL: {
                startActivity(new Intent(activity, ShopActivity.class));
                break;
            }
            case Constant.MY_AVAILABLE_PRODUCT: {
                startActivity(new Intent(activity, MyProductActivity.class));
                break;
            }
            case Constant.MY_PURCHASE_ORDERS: {
                startActivity(new Intent(activity, MyPurchaseOrdersActivity.class));
                break;
            }
            case Constant.MY_SOLD_ORDERS: {
                startActivity(new Intent(activity, SoldOrderListActivity.class));
                break;
            }
            case Constant.GENERATE_NEW_ORDER: {
                startActivity(new Intent(activity, GenerateNewOrderActivity.class));
                break;
            }
            case Constant.MY_RATING: {
                startActivity(new Intent(activity, MyRatingActivity.class));
                break;
            }
            case Constant.MY_CHAT: {
                startActivity(new Intent(activity, ShopChatActivity.class));
                break;
            }
            case Constant.START_DELIVERY: {
                openEnterTrackingIdDialog();
                break;
            }
            default: {
                break;
            }
        }
    }

    private void openEnterTrackingIdDialog() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_enter_tracking_id, null);
        DialogEnterTrackingIdBinding bind = DialogEnterTrackingIdBinding.bind(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bind.ok.setOnClickListener(v -> {
            trackingId = bind.trackingId.getText().toString();
            //trackingId="NIKU7281785";
            if (!trackingId.isEmpty()) {
                isFromStartDelivery = true;
                getCurrentLocation();
                dialog.dismiss();
            } else bind.trackingId.setError("Please Enter Tracking ID");
        });

        dialog.show();
    }

    private void getShopList(String productId) {
        if (Utility.isNetworkConnected(this)) {



            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code
            map1.put("latitude",latitude);
            map1.put("longitude",longitude);
            if (productId != null && !productId.isEmpty())
                map1.put("productId", productId);
            String request = Utility.mapWrapper(this, map1);
            shopListRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void shopListRequest(String request) {
        if (Utility.isNetworkConnected(this)) {

            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_SEARCH_SHOP, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseShopListResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseShopListResponse(String response, ResponseManager responseManager) {

        progressHelper.dismiss();
        try {
            ShopModel model = new Gson().fromJson(response, ShopModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                Log.d("ShopListResponse", "response " + response);
                arrayList.clear();
                arrayList.addAll(model.getData());
                if (tempArrayList.size() == 0)
                    tempArrayList.addAll(arrayList);
                ((ShopListFragment) viewPagerAdapter.getFragment(0)).filterData(arrayList);
                ((ShopMapFragment) viewPagerAdapter.getFragment(1)).filterData(arrayList);

            } else {
                if (model.getData() == null) {
                    //Utility.inflateNoDataFoundLayout(activity,binding.inflateLayout,"No Shop Data Found");
                    ((ShopListFragment) viewPagerAdapter.getFragment(0)).noDataFound();
                    ((ShopMapFragment) viewPagerAdapter.getFragment(1)).noDataFound();
                } else
                    Utility.showToast(activity, model.getResText(), "");
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
        } else {
            getLatLong();
        }
    }

    public void getLatLong() {
        final GPSTracker gps = new GPSTracker(activity);
        if (gps.canGetLocation()) {
            Log.d(TAG, "LatLong: canGetLocation");
            latitude = Double.toString(gps.getLatitude());
            longitude = Double.toString(gps.getLongitude());

            if (isFromStartDelivery) {
                progressHelper.dismiss();
                isFromStartDelivery = false;
                startActivity(new Intent(activity, LocationDeliveryActivity.class)
                        .putExtra(Constant.SOURCE_LATITUDE, latitude)
                        .putExtra(Constant.SOURCE_LONGITUDE, longitude)
                        .putExtra(Constant.TRACKING_ID, trackingId)
                        .putExtra(Constant.IS_FROM_START_DELIVERY, true));
            } else {
                getShopList("");
                getShopCategoryList();
            }
        } else {
            Log.d(TAG, "LatLong: canNotGetLocation");
            progressHelper.dismiss();
            gps.showSettingsAlert();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.ACTIVITY_RESULT_GPS) {
            getLatLong();
        }
    }

    /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_PERMISSION_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                LatLong();
            else{
                Utility.showToast(activity, "Permission Denied", "");
                progressHelper.dismiss();
            }
        }
    }*/

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

    private void getShopCategoryList() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("appVersion", Utility.getVersionCode(this)); // version code

            String request = Utility.mapWrapper(this, map1);
            shopCategoryRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void shopCategoryRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_SHOP_CATEGORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseShopCategoryResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseShopCategoryResponse(String response, ResponseManager responseManager) {
        try {
            Log.d("ShopCategoryResponse", "response " + response);

            ShopCategoryModel shopCategory = new Gson().fromJson(response, ShopCategoryModel.class);
            if (shopCategory.getResCode().equals(Constant.CODE_200)) {
                shopCategoryArrayList = new ArrayList<>();
                shopCategorySearchArrayList = new ArrayList<>();
                shopCategoryArrayList.addAll(shopCategory.getData());
                for (ShopCategoryModel.DataBean model : shopCategoryArrayList) {
                    for (ShopCategoryModel.DataBean.SubCategoryBean subModel : model.getSubCategory())
                        shopCategorySearchArrayList.add(new ShopCategorySearchModel(subModel.getId(), subModel.getName(), subModel.getImgUrl()));
                }
                //adapter.notifyDataSetChanged();

                setShopSearchAdapter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setShopSearchAdapter() {
        shopSearchAdapter = new ShopSearchAdapter(activity, R.layout.adapter_shop_search, shopCategorySearchArrayList);
        binding.autoCompleteTextView.setThreshold(1);
        binding.autoCompleteTextView.setAdapter(shopSearchAdapter);

        binding.autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            ShopCategorySearchModel model = (ShopCategorySearchModel) adapterView.getItemAtPosition(i);

            /*ArrayList<Object> arrayListFilter=new ArrayList<>();
            for (Object arrayObject: arrayList){
                ShopModel.DataBean.ProductsBean productsBean= ((ShopModel.DataBean)arrayObject).getProducts().get(0);
                if (productsBean.getProductName().toLowerCase().trim().contains(model.getTitle().toLowerCase().trim())){
                    arrayListFilter.add(arrayObject);
                }
            }
            adapter.filter(arrayListFilter);*/

            getShopList(model.getId());
        });

        binding.close.setOnClickListener(v -> {
            binding.autoCompleteTextView.setText("");
            binding.searchLayout.setVisibility(View.GONE);
            //((ShopListFragment)viewPagerAdapter.getFragment(0)).filter(arrayList);
            ((ShopListFragment) viewPagerAdapter.getFragment(0)).filterData(arrayList);
            ((ShopMapFragment) viewPagerAdapter.getFragment(1)).filterData(arrayList);
            Utility.hideKeyboard(activity);
            arrayList.clear();
            arrayList.addAll(tempArrayList);
            ((ShopListFragment) viewPagerAdapter.getFragment(0)).filterData(arrayList);
            ((ShopMapFragment) viewPagerAdapter.getFragment(1)).filterData(arrayList);

        });
    }

    public void setupDrawer() {
        drawerArrayList = new ArrayList<>();

        drawerArrayList.add(new DrawerModel(R.drawable.add_new_shop, Constant.ADD_NEW_SHOP));
        drawerArrayList.add(new DrawerModel(R.drawable.edit_your_shop, Constant.EDIT_YOUR_SHOP));
        drawerArrayList.add(new DrawerModel(R.drawable.shop_local, Constant.SHOP_LOCAL));
        drawerArrayList.add(new DrawerModel(R.drawable.add_new_shop, Constant.MY_AVAILABLE_PRODUCT));
        drawerArrayList.add(new DrawerModel(R.drawable.my_purchase_oder, Constant.MY_PURCHASE_ORDERS));
        drawerArrayList.add(new DrawerModel(R.drawable.my_sold_orders, Constant.MY_SOLD_ORDERS));
        drawerArrayList.add(new DrawerModel(R.drawable.generate_new_order, Constant.GENERATE_NEW_ORDER));
        drawerArrayList.add(new DrawerModel(R.drawable.my_rating, Constant.MY_RATING));
        drawerArrayList.add(new DrawerModel(R.drawable.my_chat, Constant.MY_CHAT));
        drawerArrayList.add(new DrawerModel(R.drawable.cart, Constant.START_DELIVERY));
        binding.recyclerViewDrawer.setLayoutManager(new LinearLayoutManager(this));
        drawerAdapter = new ShopDrawerAdapter(activity, drawerArrayList, this);
        binding.recyclerViewDrawer.setAdapter(drawerAdapter);
    }
}