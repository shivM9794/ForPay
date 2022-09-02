package in.forpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import in.forpay.R;
import in.forpay.activity.balance.GiftVoucherActivity;
import in.forpay.activity.balance.GiftVoucherDetailActivity;
import in.forpay.activity.kyc.AddKycActivity;
import in.forpay.activity.premiumplan.PremiumPlanActivity;
import in.forpay.activity.profile.ProfileActivity;
import in.forpay.activity.supportchat.SupportChatActivity;
import in.forpay.adapter.HomeCategorySearchModel;
import in.forpay.adapter.HomeDataCategoryAdapter;
import in.forpay.adapter.HomeSearchAdapter;
import in.forpay.adapter.ViewPagerAdapter;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityHomeNewBinding;
import in.forpay.databinding.FragmentLogoutBinding;
import in.forpay.fragment.HistoryFragment;
import in.forpay.fragment.HomeFragment1;
import in.forpay.fragment.balance.MainFragment;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.model.HomeSearchModel;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.model.request.NotificationModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.PreferenceConnector;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import in.forpay.webView.WebViewActivity;

public class HomeNewActivity extends AppCompatActivity {

    private ActivityHomeNewBinding mBinding;
    private boolean navigationFlag[] = new boolean[5];
    Bundle bundle;
    private ProgressHelper progressHelper;
    private ViewPagerAdapter adapter;
    private Activity activity;
    private List<Integer> listHomeDataCategoryIcon;
    private List<String> listHomeDataCategoryLable;
    private List<String> listHomeDataCategoryImgUrl;
    private List<String> listHomeDataCategoryActivityName;
    private List<String> listHomeDataCategoryActivityisNew;
    private HomeDataCategoryAdapter homeDataCategoryAdapter;
    private Double checkBalance;
    private DatabaseHelper databaseHelper;
    private ArrayList<NotificationModel> notificationModels = new ArrayList<>();
    private final int IMG_RESULT = 1009;
    private Uri uri;
    private String realPath;
    private TextView nav_data_version, nav_user_name, nav_user_number;
    private CircleImageView nav_profile_ico_set;
    private LinearLayout nav_btn_profile, nav_linearLayoutReffer, nav_LinearLayoutAddfund, nav_LinearLayoutPremiumPlan, nav_linearLayoutContactUs, nav_linearLayoutTos, nav_linearLayoutPrivacy, nav_linearLayoutChangeLanuage, nav_linearLayoutLogout, nav_linearLayoutChatSupport, nav_linearLayoutAddKyc;
    private Bitmap link;
    TextView textViewVersion;
    private OxyMePref oxyMePref;
    private HomeSearchAdapter homeSearchAdapter;
    private ArrayList<HomeSearchModel.ServiceListBean> homeSearchArrayList;
    private ArrayList<HomeCategorySearchModel> homeCategorySearchArrayList;
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;
    InstallStateUpdatedListener installStateUpdatedListener;
    private ArrayList<MainRechargeModel.NavigationMenu> navigationMenus = new ArrayList<>();
    String serviceListLocation = "serviceList_HomeUpdates" + Constant.METHOD_HOME_UPDATE;
    int AppView2 = 1;
    String fromActivity = "";
    Boolean fromWeb = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = HomeNewActivity.this;
        Utility.setAppLocale(Utility.getDefaultLanguage(this), this);
        AppView2 = Utility.getAppViewCount(activity);
        databaseHelper = new DatabaseHelper(activity);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_new);
        mBinding.setActivity(this);
        progressHelper = new ProgressHelper(this);
        oxyMePref = new OxyMePref(this);

        navSlider();
        //Log.d("Back to home ","yes");
        initNoti();
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            //mBinding.dataVersion.setText("Version "+ version);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            link = databaseHelper.getProfileImage();
            Glide.with(activity).load(link).error(R.drawable.ico_profile).into(mBinding.imgProfile);

        } catch (Exception e) {

        }

        mBinding.imgProfile.setOnClickListener(v -> onClickNavigation());
        initView();
        setTabLayout();

        setDrawerListener();
        disableNavigationViewScrollbars(mBinding.navView);
        mBinding.drawerLayout.setDrawerElevation(0);

        mBinding.closeDrawer.setOnClickListener(view -> {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        });

//        mBinding.floatButton.setOnClickListener(view -> {
//            startActivity(new Intent(activity, ProductSuggestionActivity.class));
//        });

        searchData();
        setHomeSearchAdapter();

    }

    private void initListeningUpdate() {
        initListening();
    }

    private void initListening() {
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(@NonNull InstallState state) {

                if (state.installStatus() == InstallStatus.DOWNLOADED) {
                    //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                    popupSnackbarForCompleteUpdate();
                } else if (state.installStatus() == InstallStatus.INSTALLED) {
                    if (mAppUpdateManager != null) {
                        mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                    }

                } else {
                    Log.e("HomeActivity", "InstallStateUpdatedListener: state: " + state.installStatus());
                }

            }
        };


    }

    private void checkForUpdate() {


        mAppUpdateManager = AppUpdateManagerFactory.create(HomeNewActivity.this);

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        Task<AppUpdateInfo> appUpdateInfoTask = mAppUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/)) {

                Log.e("HomeActivity", "available " + appUpdateInfo.availableVersionCode());

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/, this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    Log.e("HomeActivity", e.toString());
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e("HomeActivity", "checkForAppUpdateAvailability: something else");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mBinding.tabLayoutMain.getSelectedTabPosition() != 0)
            mBinding.tabLayoutMain.selectTab(mBinding.tabLayoutMain.getTabAt(0));

//        onClickRefresh2();
        if (AppView2 % 50 == 0) {
            Log.d("AppCount", "" + AppView2);
            initListeningUpdate();
            checkForUpdate();
        }

    }

    private void popupSnackbarForCompleteUpdate() {

        try {
            Snackbar snackbar =
                    Snackbar.make(
                            findViewById(R.id.coordinatorLayout_main),
                            "New app is ready!",
                            Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("Install", view -> {
                if (mAppUpdateManager != null) {
                    mAppUpdateManager.completeUpdate();
                }
            });


            snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();
        } catch (Exception e) {

        }

    }


    private void searchData() {
        mBinding.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mBinding.autoCompleteTextView.isPerformingCompletion()) {
                    // An item has been selected from the list. Ignore.
                } else {
                    if (!editable.toString().isEmpty()) {
                        callHomeSearch(editable.toString());
                    }
                }
            }
        });
    }

    private void callHomeSearch(String keyword) {
        if (Utility.isNetworkConnected(activity)) {


            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("versionCode", Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));
            map1.put("keyword", keyword);

            String request = Utility.mapWrapper(this, map1);
            callHomeSearchRequest(request);

        } else {
            Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
        }
    }

    private void callHomeSearchRequest(String request) {
        if (Utility.isNetworkConnected(activity)) {
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_HOME_SEARCH, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseHomeSearchResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
        }
    }

    private void parseHomeSearchResponse(String response, ResponseManager responseManager) {
        try {
            HomeSearchModel model = new Gson().fromJson(response, HomeSearchModel.class);
            if (model.getServiceList().size() > 0) {
                Log.d("HomeSearchClicked", "response " + response + " search " + model.getServiceList().get(0).getOnClickActivity());
                homeSearchArrayList = new ArrayList<>();
                homeCategorySearchArrayList = new ArrayList<>();
                homeSearchArrayList.addAll(model.getServiceList());

                for (HomeSearchModel.ServiceListBean data : model.getServiceList()) {
                    homeCategorySearchArrayList.add(new HomeCategorySearchModel(data.getName()));
                }

                setHomeSearchItemClick();
            } else {
                Utility.showToast(activity, model.getResText(), "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHomeSearchItemClick() {
        homeSearchAdapter.setData(homeCategorySearchArrayList);
        homeSearchAdapter.notifyDataSetChanged();
        mBinding.autoCompleteTextView.showDropDown();

        mBinding.autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.TITLE_SHOW, homeSearchArrayList.get(i).getName());
            bundle.putString(Constant.INPUT_DATA, homeSearchArrayList.get(i).getInputData());
            bundle.putString(Constant.ONCLICKACTIVITY, homeSearchArrayList.get(i).getOnClickActivity());

            switch (homeSearchArrayList.get(i).getOnclick()) {

                case "giftVoucher1212":
                    if (homeSearchArrayList.get(i).getInputData().isEmpty()) {
                        startActivity(new Intent(activity, GiftVoucherActivity.class)
                                .putExtra(Constant.TYPE, "digitalvoucher"));
                    } else {
                        //parseHomeUpdateResponse(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE), homeSearchArrayList.get(i).getInputData());
                        parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation), homeSearchArrayList.get(i).getInputData());
                    }
                    break;

                default:
                    Log.d("GiftVoucherAcitysfsfs", "inputData " + homeSearchArrayList.get(i).getInputData() + " - " + homeSearchArrayList.get(i).getOnClickActivity());

                    Utility.openActivity(HomeNewActivity.this, homeSearchArrayList.get(i).getOnClickActivity(), bundle);
                    break;
            }


            mBinding.autoCompleteTextView.setText("");
            mBinding.autoCompleteTextView.clearFocus();
        });
    }

    private void parseHomeUpdateResponse(String response, String inputData) {
        try {
            MainRechargeModel model = new Gson().fromJson(response, MainRechargeModel.class);
            if (model.getResCode().equals(Constant.CODE_200) && model.getService() != null) {
                Log.d("HomeActivity", "response " + response);
                ArrayList<MainRechargeModel.ServiceBean> serviceBeanArrayList = new ArrayList<>();

                for (MainRechargeModel.ServiceBean serviceBean : model.getService()) {
                    if (serviceBean.getService().toLowerCase().contains(inputData.toLowerCase())) {
                        serviceBeanArrayList.add(serviceBean);
                    }
                }
                if (serviceBeanArrayList.size() == 0) {
                    startActivity(new Intent(activity, GiftVoucherActivity.class)
                            .putExtra(Constant.TYPE, "digitalvoucher"));
                } else if (serviceBeanArrayList.size() == 1) {
                    startActivity(new Intent(activity, GiftVoucherDetailActivity.class)
                            .putExtra(Constant.IS_FROM_HOME_ACTIVITY, true)
                            .putExtra(Constant.MAIN_RECHARGE_SERVICE_MODEL, serviceBeanArrayList.get(0)));
                } else {
                    startActivity(new Intent(activity, GiftVoucherActivity.class)
                            .putExtra(Constant.IS_FROM_HOME_ACTIVITY, true)
                            .putExtra(Constant.TYPE, "digitalvoucher")
                            .putExtra(Constant.INPUT_DATA, inputData));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHomeSearchAdapter() {
        homeCategorySearchArrayList = new ArrayList<>();
        homeSearchAdapter = new HomeSearchAdapter(activity, R.layout.adapter_home_search, homeCategorySearchArrayList);
        mBinding.autoCompleteTextView.setThreshold(1);
        mBinding.autoCompleteTextView.setAdapter(homeSearchAdapter);
    }

    private void setDrawerListener() {
        mBinding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.
                Log.e("HomeActivity", "onDrawerSlide");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
                // If you have 2 drawers (left and right) you can distinguish
                // them by using id of the drawerView. int id = drawerView.getId();
                // id will be your layout's id: for example R.id.left_drawer
                Log.e("HomeActivity", "onDrawerOpened");
                mBinding.closeDrawer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Called when a drawer has settled in a completely closed state.
                Log.e("HomeActivity", "onDrawerClosed");
                mBinding.closeDrawer.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
                Log.e("HomeActivity", "onDrawerStateChanged");

                if (newState == DrawerLayout.STATE_SETTLING) {
                    if (!mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        // Drawer started opening
                        Log.e("HomeActivity", "started opening");
                    } else {
                        // Drawer started closing
                        Log.e("HomeActivity", "started closing");
                    }
                }
            }
        });
    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    private void selectProfileImage() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMG_RESULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == IMG_RESULT && resultCode == RESULT_OK && null != data) {
                uri = data.getData();
                realPath = Utility.getRealPathFromURIPath(uri, activity);
                Glide.with(activity).load(uri).error(R.drawable.profile_placeholder).into(nav_profile_ico_set);
                Glide.with(activity).load(uri).error(R.drawable.profile_placeholder).into(mBinding.imgProfile);
                try {
                    databaseHelper.insertImage(realPath);
                } catch (Exception e) {

                }

            }
            if (requestCode == RC_APP_UPDATE) {
                if (resultCode != RESULT_OK) {
                    Log.e("HomeActivity", "onActivityResult: app download failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void navSlider() {
//        View header = mBinding.navView.getHeaderView(0);

        View newheader = LayoutInflater.from(this).inflate(R.layout.nav_header_main_1, null);

        LinearLayout linearLayoutHeader = newheader.findViewById(R.id.linearlaynhead);

        if (oxyMePref.getString(serviceListLocation) == null || oxyMePref.getString(serviceListLocation).isEmpty() || fromWeb) {
            fromWeb = true;
            Utility.getServiceList(HomeNewActivity.this, "HomeUpdates", Constant.METHOD_HOME_UPDATE, fromWeb, "HomeActivity", new HomeUpdateListener() {
                @Override
                public void onDone() {
                    navSlider();
                }
            });

        } else {


            MainRechargeModel model = new Gson().fromJson(oxyMePref.getString(serviceListLocation), MainRechargeModel.class);
            if (model.getNavigationMenu() != null) {
                for (MainRechargeModel.NavigationMenu menuItem : model.getNavigationMenu()) {

                    Log.e("Natigation", menuItem.getName() + menuItem.getUrl());

                    View item = LayoutInflater.from(this).inflate(R.layout.btnlay_nav_header, null);
                    TextView title = item.findViewById(R.id.title_nav_item);
                    ImageView icon = item.findViewById(R.id.icon_nav_item);
                    ImageView new_icon = item.findViewById(R.id.new_service_nav);
                    title.setText(menuItem.getName());
                    linearLayoutHeader.addView(item);

                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            progressHelper.dismiss();
                            if (menuItem.getOpenIn().equals("activity")) {
                                Utility.openActivity(HomeNewActivity.this, menuItem.getUrl(), null);
                            } else {
                                Intent intent = new Intent(HomeNewActivity.this, WebViewActivity.class);
                                intent.putExtra("url", menuItem.getUrl());
                                intent.putExtra("webName", menuItem.getName());
                                startActivity(intent);
                            }
                        }
                    });

                    Glide.with(HomeNewActivity.this)
                            .load(menuItem.getIconUrl())
                            .fitCenter()
                            .into(icon);
                    if (menuItem.getIsNew().equals("yes")) {
                        Glide.with(HomeNewActivity.this)
                                .load(R.drawable.new_n)
                                .into(new_icon);
                    }

                }
            }
            nav_data_version = new TextView(HomeNewActivity.this);
            nav_data_version.setPadding(18, 18, 18, 18);
            nav_data_version.setTextColor(getResources().getColor(R.color.black));
            Typeface face = ResourcesCompat.getFont(this, R.font.poppins_regular);
            nav_data_version.setTypeface(face);
            linearLayoutHeader.addView(nav_data_version);


            if (model.getOfferUrl() != null) {
                if (!model.getOfferUrl().isEmpty()) {
                    if (AppView2 % 100 == 0) {
                        Utility.showImageDialog(activity, model.getOfferUrl(), 2000);
                    }
                }
            }
            mBinding.navView.addHeaderView(newheader);


            nav_btn_profile = newheader.findViewById(R.id.btn_profile);
//        nav_data_version = header.findViewById(R.id.data_version);
//        nav_linearLayoutChangeLanuage = header.findViewById(R.id.linearLayoutChangeLanuage);
//        nav_linearLayoutContactUs = header.findViewById(R.id.linearLayoutContactUs);
//        nav_linearLayoutLogout = header.findViewById(R.id.linearLayoutLogout);
//        nav_linearLayoutPrivacy = header.findViewById(R.id.linearLayoutPrivacy);
//        nav_linearLayoutReffer = header.findViewById(R.id.linearLayoutReffer);
//        nav_LinearLayoutAddfund = header.findViewById(R.id.linearLayoutAddfund);
//        nav_LinearLayoutPremiumPlan = header.findViewById(R.id.linearLayoutPremiumPlan);
            nav_user_number = newheader.findViewById(R.id.nav_user_number);
            nav_user_name = newheader.findViewById(R.id.nav_user_name);
//        nav_linearLayoutTos = header.findViewById(R.id.linearLayoutTos);
            nav_profile_ico_set = newheader.findViewById(R.id.profile_ico_set);
//        nav_linearLayoutChatSupport = header.findViewById(R.id.linearLayoutChatSupport);
//        nav_linearLayoutAddKyc = header.findViewById(R.id.linearLayoutAddKyc);
            try {
                link = databaseHelper.getProfileImage();
                Glide.with(activity).load(link).error(R.drawable.profile_placeholder).into(nav_profile_ico_set);

            } catch (Exception e) {
//
            }

            nav_profile_ico_set.setOnClickListener(v -> {
                selectProfileImage();
            });

            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = pInfo.versionName;
                //nav_data_version.setText("Version " + version);
                nav_data_version.setText(getResources().getString(R.string.title_app_version) + " V" + version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            nav_btn_profile.setOnClickListener(v -> {
                progressHelper.show();
                onClickNavigationItem(7, 0);
            });
        }
    }

    private void initView() {
        addFragment(HomeFragment1.newInstance(), Constant.FRAGMENT_HOME, "Home", "");

        mBinding.commanRecyclerviewTopservice.setNestedScrollingEnabled(false);

        mBinding.commanRecyclerviewTopservice.setFocusable(false);
        mBinding.commanRecyclerviewTopservice.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        mBinding.btnNotification.setOnClickListener(v -> startActivity(new Intent(activity, NotificationActivity.class)));


//        mBinding.shop.setOnClickListener(v -> startActivity(new Intent(activity, ShopActivity.class)));

//        mBinding.floatButton.setOnClickListener(view -> startActivity(new Intent(activity, BbpsComplaint.class)));

        fillData();

    }


    private void fillData() {

        listHomeDataCategoryIcon = new ArrayList<>();
        listHomeDataCategoryLable = new ArrayList<>();
        listHomeDataCategoryImgUrl = new ArrayList<>();
        listHomeDataCategoryActivityName = new ArrayList<>();
        listHomeDataCategoryActivityisNew = new ArrayList<>();

        MainRechargeModel model;

        String response = oxyMePref.getString(serviceListLocation);
        Log.d("HomeNewActivity", "fill data 1");

        model = new Gson().fromJson(response, MainRechargeModel.class);
        if (model != null && model.getResCode().equals(Constant.CODE_200)) {
            for (MainRechargeModel.QuickMenuBean item : model.getQuickMenu()) {

                listHomeDataCategoryIcon.add(R.drawable.ic_recharge2);
                listHomeDataCategoryLable.add(item.getName());
                listHomeDataCategoryImgUrl.add(item.getImgurl());
                listHomeDataCategoryActivityName.add(item.getType());
                listHomeDataCategoryActivityisNew.add(item.getIsNew());
                Log.d("HomeNewActivity", "fill data 2" + item.getImgurl() + " " + item.getType());


            }
        }

        homeDataCategoryAdapter = new HomeDataCategoryAdapter(activity, listHomeDataCategoryIcon, listHomeDataCategoryLable, listHomeDataCategoryImgUrl, listHomeDataCategoryActivityName, listHomeDataCategoryActivityisNew, progressHelper);
        mBinding.commanRecyclerviewTopservice.setAdapter(homeDataCategoryAdapter);

    }


    private void setTabLayout() {
        try {
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
            mBinding.mainPager.setAdapter(adapter);
            mBinding.tabLayoutMain.setupWithViewPager(mBinding.mainPager);
            customTab();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void customTab() {

        try {

            mBinding.tabLayoutMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    View view = tab.getCustomView();
                    assert view != null;
                    if (tab.getPosition() != 2) {
                        ImageView imageView = view.findViewById(R.id.tab_icon);
                        imageView.setColorFilter(ContextCompat.getColor(activity, R.color.tab_select_txt));
                        TextView tabText = view.findViewById(R.id.tab_title);
                        tabText.setTextColor(ContextCompat.getColor(activity, R.color.tab_select_txt));
                    }
                    if (tab.getPosition() == 0) {
                        replaceFragment(HomeFragment1.newInstance(), Constant.FRAGMENT_HOME, "Home", "");
                    } else if (tab.getPosition() == 1) {
                        progressHelper.show();
                        startActivity(new Intent(activity, ProfileActivity.class));
                        // replaceFragment(ProfileFragment3.newInstance(), Constant.FRAGMENT_PROFILE3, getString(R.string.icon_title_profile2),"");
                    } else if (tab.getPosition() == 2) {
                        //replaceFragment2(HomeFragment.newInstance(), Constant.FRAGMENT_HOME, "Home", "PlusHome");
                        replaceFragment2(MainFragment.newInstance(), Constant.FRAGMENT_HOME, "Home", "PlusHome");
                    } else if (tab.getPosition() == 3) {
                        replaceFragment(HistoryFragment.newInstance(), Constant.FRAGMENT_HISTORY, "History", "");
                    } else if (tab.getPosition() == 4) {
                        logoutDialog(activity);
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View view = tab.getCustomView();
                    assert view != null;
                    if (tab.getPosition() != 2) {
                        ImageView imageView = view.findViewById(R.id.tab_icon);
                        if (imageView != null)
                            imageView.setColorFilter(ContextCompat.getColor(activity, R.color.home_new));
                        TextView tabText = view.findViewById(R.id.tab_title);
                        if (tabText != null)
                            tabText.setTextColor(ContextCompat.getColor(activity, R.color.home_new));
                    } else if (tab.getPosition() == 4) {

                    } else if (tab.getPosition() == 2) {

                    } else if (tab.getPosition() != 4) {
                        ImageView imageView = view.findViewById(R.id.tab_icon);
                        if (imageView != null)
                            imageView.setColorFilter(ContextCompat.getColor(activity, R.color.home_new));
                        TextView tabText = view.findViewById(R.id.tab_title);
                        if (tabText != null)
                            tabText.setTextColor(ContextCompat.getColor(activity, R.color.home_new));
                    }

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    View view = tab.getCustomView();
                    assert view != null;
                    if (tab.getPosition() != 2) {
                        ImageView imageView = view.findViewById(R.id.tab_icon);
                        imageView.setColorFilter(ContextCompat.getColor(activity, R.color.tab_select_txt));
                        TextView tabText = view.findViewById(R.id.tab_title);
                        tabText.setTextColor(ContextCompat.getColor(activity, R.color.tab_select_txt));
                    }
                    if (tab.getPosition() == 0) {
                        replaceFragment(HomeFragment1.newInstance(), Constant.FRAGMENT_HOME, "Home", "");
                    } else if (tab.getPosition() == 1) {
                        progressHelper.show();
                        startActivity(new Intent(activity, ProfileActivity.class));
                        // replaceFragment(ProfileFragment3.newInstance(), Constant.FRAGMENT_PROFILE3, getString(R.string.icon_title_profile2),"");
                    } else if (tab.getPosition() == 2) {
                        replaceFragment2(MainFragment.newInstance(), Constant.FRAGMENT_HOME, "Home", "PlusHome");
                    } else if (tab.getPosition() == 3) {
                        replaceFragment(HistoryFragment.newInstance(), Constant.FRAGMENT_HISTORY, "History", "");
                    } else if (tab.getPosition() == 4) {
                        logoutDialog(activity);
                    }

                }
            });

            LinearLayout tabOne = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_layout, null);
            ImageView tabImage = tabOne.findViewById(R.id.tab_icon);
            TextView tabText = tabOne.findViewById(R.id.tab_title);
            tabText.setText(getResources().getString(R.string.tab_home));
            tabText.setTextColor(ContextCompat.getColor(activity, R.color.orange));
            tabImage.setImageResource(R.drawable.home_new_1);
            tabImage.setColorFilter(ContextCompat.getColor(activity, R.color.orange));
            Objects.requireNonNull(mBinding.tabLayoutMain.getTabAt(0)).setCustomView(tabOne);


            LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_layout, null);
            ImageView tabImage1 = tabTwo.findViewById(R.id.tab_icon);
            tabImage1.setImageResource(R.drawable.profile_new_1);
            TextView tabText3 = tabTwo.findViewById(R.id.tab_title);
            tabText3.setText(getResources().getString(R.string.tab_profile));
            Objects.requireNonNull(mBinding.tabLayoutMain.getTabAt(1)).setCustomView(tabTwo);

            LinearLayout tabFour = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_layout2, null);
            ImageView tabImage3 = tabFour.findViewById(R.id.tab_icon);
            tabImage3.setImageResource(R.drawable.tab_add_sign1);
            Objects.requireNonNull(mBinding.tabLayoutMain.getTabAt(2)).setCustomView(tabFour);

            LinearLayout tabFive = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_layout, null);
            ImageView tabImage4 = tabFive.findViewById(R.id.tab_icon);
            tabImage4.setImageResource(R.drawable.history_new_1);
            TextView tabText4 = tabFive.findViewById(R.id.tab_title);
            tabText4.setText(getResources().getString(R.string.tab_history));
            Objects.requireNonNull(mBinding.tabLayoutMain.getTabAt(3)).setCustomView(tabFive);

            LinearLayout tabSix = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_layout, null);
            ImageView tabImage5 = tabSix.findViewById(R.id.tab_icon);
            tabImage5.setImageResource(R.drawable.logout_new_1);
            TextView tabText5 = tabSix.findViewById(R.id.tab_title);
            tabText5.setText(getResources().getString(R.string.tab_logout));
            Objects.requireNonNull(mBinding.tabLayoutMain.getTabAt(4)).setCustomView(tabSix);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Resume ", "yes");
        init();

        initNoti();
        //Utility.showRating(this);
        int AppView = Utility.getAppViewCount(this);
        Utility.setAppViewCount(this, AppView + 1);
        if (AppView % 100 == 0 && AppView != 0 && Utility.getRatings(this) == 0) {
            //Utility.showRateNow(this);
        }

        if (Constant.PROFILE_ADDED) {
            Constant.PROFILE_ADDED = false;
            try {
                Bitmap link = databaseHelper.getProfileImage();
                View header = mBinding.navView.getHeaderView(0);
                nav_profile_ico_set = header.findViewById(R.id.profile_ico_set);
                Glide.with(activity).load(link).error(R.drawable.ico_profile).into(mBinding.imgProfile);
                Glide.with(activity).load(link).error(R.drawable.ico_profile).into(nav_profile_ico_set);

            } catch (Exception e) {

            }
        }

    }


    boolean doublePressed = false;

    @Override
    public void onBackPressed() {
        Log.d("Back pressed ", "yes " + getSupportFragmentManager().getBackStackEntryCount());
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 1) {
            if (doublePressed) {
                finish();
            }

            if (!doublePressed) {
                //Utility.showBackPressToast(this, "Please click BACK again to exit");
                Utility.showToast(this, "Please click BACK again to exit", "");
                doublePressed = true;
            }
        } else {
            super.onBackPressed();
            int AppView = Utility.getAppViewCount(this);
            Utility.setAppViewCount(this, AppView + 1);
            if (AppView % 100 == 0 && AppView != 0 && Utility.getRatings(this) == 0) {
                //Utility.showRateNow(this);
            }

        }
    }

    /**
     * Click on navigation icon
     */
    public void onClickNavigation() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mBinding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }


    /**
     * Click on navigation item
     */
    public void onClickNavigationItem(final int position, final int subPosition) {

        if ((position == 2 && subPosition == 0)) {
            onClickNavigation();
        } else {
            onClickNavigation();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                switch (position) {
                    case 1: //Add fund
                        progressHelper.dismiss();
                        expandView();
                        startActivity(new Intent(HomeNewActivity.this, AddFundNewActivity.class));
                        break;
                    case 3: //Contact us
                        progressHelper.dismiss();
                        expandView();
                        startActivity(new Intent(HomeNewActivity.this, ContactUsActivity.class));
                        break;

                    case 4: //Tos
                        progressHelper.dismiss();
                        expandView();
                        Intent intent = new Intent(HomeNewActivity.this, WebViewActivity.class);
                        intent.putExtra("url", "https://forpay.in/tos.php");
                        intent.putExtra("webName", "tosNew");
                        startActivity(intent);
                        break;

                    case 5: //Privacy Policy
                        progressHelper.dismiss();
                        expandView();
                        Intent intent2 = new Intent(HomeNewActivity.this, WebViewActivity.class);
                        intent2.putExtra("url", "https://forpay.in/tos.php");
                        intent2.putExtra("webName", "policyNew");
                        startActivity(intent2);
                        break;

                    case 6: //Change Language
                        progressHelper.dismiss();
                        expandView();
                        startActivity(new Intent(activity, LanguageChangeActivity.class));
                        break;

                    case 2: // Logout
                        expandView();
                        logoutDialog(activity);
                        break;
                    case 7:
                        progressHelper.dismiss();
                        expandView();
                        startActivity(new Intent(HomeNewActivity.this, ProfileActivity.class));
                        break;
                    case 8:
                        progressHelper.dismiss();
                        expandView();
                        startActivity(new Intent(HomeNewActivity.this, InviteActivity.class));
                        break;

                    case 9:
                        progressHelper.dismiss();
                        expandView();
                        startActivity(new Intent(HomeNewActivity.this, SupportChatActivity.class));
                        break;

                    case 10:
                        progressHelper.dismiss();
                        expandView();
                        startActivity(new Intent(HomeNewActivity.this, AddKycActivity.class));
                        break;
                    case 11:
                        progressHelper.dismiss();
                        expandView();
                        startActivity(new Intent(HomeNewActivity.this, PremiumPlanActivity.class));
                }
            }
        }, 200);
    }

    private void initNoti() {


        mBinding.notificationCount.setVisibility(View.GONE);

    }

    private void init() {
        if (nav_user_name != null) {
            String customer_name = Utility.getCustomerName(this);

            if (!customer_name.isEmpty()) {
                nav_user_name.setText("Hi!  " + customer_name);
            }
            nav_user_number.setText(Utility.getUsername(this));
        }


    }


    /**
     * Set visibility
     */
    private void expandView() {


    }


    public void logoutDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.fragment_logout, null);
        builder.setView(view);
        FragmentLogoutBinding bind = DataBindingUtil.bind(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        assert bind != null;
        bind.btnLogout.setOnClickListener(v -> {
            dialog.dismiss();
            logout("");
        });

        bind.btnLogoutAll.setOnClickListener(v -> {
            dialog.dismiss();
            logout("all");
        });
        dialog.show();
    }

    private void logout(String type) {
        String imei = PreferenceConnector.readString(activity, PreferenceConnector.IMEI, "");
        String token = Utility.getToken(activity);
        String activeKey = PreferenceConnector.readString(activity,
                PreferenceConnector.ACTIVE_KEY, "");
        String mobile = Utility.getUsername(activity);

        PreferenceConnector.clear(activity);
        PreferenceConnector.writeString(activity, PreferenceConnector.IMEI, imei);
        PreferenceConnector.writeString(activity, PreferenceConnector.ACTIVE_KEY, activeKey);
        PreferenceConnector.writeString(activity, PreferenceConnector.USER_NAME, mobile);
        // Delete db table
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        databaseHelper.deleteServiceTypeTable(); // Delete service type table
        //databaseHelper.deleteOrderIdTable(); // Delete order id table
        databaseHelper.deleteRechargeHistoryTable(); // Delete recharge history table
        oxyMePref.clear();


        if (activity != null) {

            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(activity)); // key
            map1.put("imei", Utility.getImei(activity)); // imei
            map1.put("versionCode", Utility.getVersionCode(activity)); // version code
            map1.put("os", Utility.getOs(activity));
            map1.put("type", type); // amount

            String request = Utility.mapWrapper(this, map1);

            if (Utility.isNetworkConnected(activity)) {
                progressHelper = new ProgressHelper(activity);
                progressHelper.show();

                QueryManager.getInstance().postRequest(activity,
                        Constant.METHOD_LOGOUT, request, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {
                                progressHelper.dismiss();

                                //Log.d("TEST","request - "+result);
                            }
                        });
            }
        }


        Intent intent3 = new Intent(activity, SplashActivity.class);
        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent3);
    }


    /**
     * Add fragment
     */
    private void addFragment(@NonNull Fragment fragment,
                             @NonNull String fragmentTag,
                             String title, String value) {


        bundle = new Bundle();

        bundle.putString("userType", value);
        bundle.putString(Constant.STATE_FROM, value);

        fragment.setArguments(bundle);
        View fragmentView = fragment.getView();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment, fragmentTag)
                .addToBackStack(fragmentTag)

                .commit();

        //Log.d("Commit "," f "+fragmentTag);
    }

    private void replaceFragment(@NonNull Fragment fragment,
                                 @NonNull String fragmentTag,
                                 String title, String value) {
        bundle = new Bundle();
        bundle.putString("userType", value);
        bundle.putString(Constant.STATE_FROM, value);
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment, fragmentTag)
                .commit();
        //Log.d("Commit "," f "+fragmentTag);
        progressHelper.dismiss();

    }

    private void replaceFragment2(@NonNull Fragment fragment,
                                  @NonNull String fragmentTag,
                                  String title, String value) {
        bundle = new Bundle();

        bundle.putString("userType", value);
        bundle.putString(Constant.STATE_FROM, value);

        fragment.setArguments(bundle);

        startActivity(new Intent(activity, PlusActivity.class));
    }

    @Override
    protected void onPause() {
        progressHelper.dismiss();
        super.onPause();
    }

    @Override
    protected void onStop() {
        progressHelper.dismiss();
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
