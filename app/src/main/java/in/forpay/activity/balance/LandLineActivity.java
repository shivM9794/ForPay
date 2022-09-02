package in.forpay.activity.balance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.adapter.balance.CouponOfferAdapter;
import in.forpay.adapter.balance.LandLinePlaceHolderAdapter;
import in.forpay.adapter.balance.ViewPagerBalanceAdapter;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityLandLineBinding;
import in.forpay.fragment.balance.LandLineFragment;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.listener.ItemClickListener;
import in.forpay.listener.OfferListener;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.model.response.GetServiceList;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class LandLineActivity extends AppCompatActivity implements ItemClickListener, OfferListener {

    private ActivityLandLineBinding binding;
    private AppCompatActivity activity;
    private ArrayList<GetServiceList.ServiceBean.ParamsBean> tempArrayList;
    private List<Fragment> mFragments;
    private ArrayList<ArrayList<GetServiceList.ServiceBean>> mList;
    private int index = 0;
    private ArrayList<GetServiceList.ServiceBean> serviceArrayList;
    private DatabaseHelper databaseHelper;
    private ProgressHelper progressHelper;
    private String operatorId = "";
    private String bbpsId = "";
    private LandLinePlaceHolderAdapter landLinePlaceHolderAdapter;
    private ArrayList<GetServiceList.ServiceBean> tempServiceArrayList = new ArrayList<>();
    private ArrayList<String> valueArrayList = new ArrayList<>();
    private String title;
    private String pin = "";
    private String type, search_type;
    private String mobile = "";
    private String amount = "0";
    private int viewPagerPosition = 0;
    private GetServiceList model;
    private OxyMePref oxyMePref;
    private String inputData = "";
    private String favMobile, inputValue1, inputValue2 = "";
    private String selectedCoupon = "";
    private String selectedCouponId = "";
    private String topIconUrl = "";
    private CouponOfferAdapter couponOfferAdapter;
    private String TAG = "LandlineActivity";
    Boolean fromWeb = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_land_line);
        activity = this;
        databaseHelper = new DatabaseHelper(activity);
        progressHelper = new ProgressHelper(activity);
        oxyMePref = new OxyMePref(activity);

        title = getIntent().getStringExtra(Constant.TITLE_SHOW);
        search_type = getIntent().getStringExtra("search_type");
        if (search_type == null) {
            search_type = "";
        }
        type = getIntent().getStringExtra(Constant.TYPE);
        inputData = getIntent().getStringExtra(Constant.INPUT_DATA);
        inputValue1 = getIntent().getStringExtra(Constant.FAV_INPUTVALUE1);
        inputValue2 = getIntent().getStringExtra(Constant.FAV_INPUTVALUE2);


        favMobile = getIntent().getStringExtra(Constant.FAV_MOBILE);
        //Log.d(TAG, "LandlineActivity clicked " + type);
        if (type.equals("bbpsFasttag")) {
          //  fromWeb = true;
        }

        //parseHomeUpdateResponse(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE));

        //Log.d("response is ",""+oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE));
        init();

    }

    private void init() {

        progressHelper.show();

        Utility.getServiceList(activity, type, Constant.METHOD_SERVICE_LIST, fromWeb,"LandLineActivity", new HomeUpdateListener() {
            @Override
            public void onDone() {
                progressHelper.dismiss();
                //parseHomeUpdateResponse(oxyMePref.getString(Constant.SERVICE_LIST_RESPONSE));
                String serviceListLocation = "serviceList_" + type + Constant.METHOD_SERVICE_LIST;
                parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation));
            }
        });


        binding.title.setText(title);

        binding.dotsIndicator.setVisibility(View.VISIBLE);
        binding.placeHolderLayout.setVisibility(View.GONE);
        //binding.frameLayout.setVisibility(View.GONE);

        binding.backPress.setOnClickListener(view -> onBackPressed());

        binding.proceed.setOnClickListener(view -> onClickRechargeNow());

        binding.clear.setOnClickListener(view -> {
            if (!binding.edtSearch.getText().toString().isEmpty()) {
                binding.edtSearch.setText("");
                Utility.hideKeyboard(activity);
            }
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    filterData(s.toString());
                    binding.viewPager.disableScroll(true);
                } else {
                    binding.noDataFound.setVisibility(View.GONE);
                    // binding.dotsIndicator.setVisibility(View.VISIBLE);
                    binding.middleLayout.setVisibility(View.VISIBLE);
                    binding.placeHolderLayout.setVisibility(View.GONE);

                    binding.dotsIndicator.setVisibility(View.VISIBLE);

                    binding.viewPager.disableScroll(false);

                    if (mList != null) {
                        for (int i = 0; i < mList.size(); i++) {
                            setFilterData(i, mList.get(i), "", "");
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPagerPosition = position;

            }

            @Override
            public void onPageSelected(int position) {
                binding.viewPager.reMeasureCurrentPage(binding.viewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void parseHomeUpdateResponse(String response) {
        try {
            model = new Gson().fromJson(response, GetServiceList.class);
            if (model.getResCode().equals(Constant.CODE_200) && model.getService() != null) {
                Log.d("HomeUpdateResponse", "response " + response);
                tempArrayList = new ArrayList<>();
                serviceArrayList = new ArrayList<>();

                for (GetServiceList.ServiceBean serviceBean : model.getService()) {
                    if (serviceBean.getServiceType().equalsIgnoreCase(type)) {
                        serviceArrayList.add(serviceBean);
                    }
                }

                if (serviceArrayList == null || serviceArrayList.size() < 12) {
                    binding.searchLayout.setVisibility(View.GONE);
                }


                setFragments();
                setUpViewPager();
                setAdapter();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setAdapterComplete() {
        setPreFilledFromHomeSearch();
    }

    private void setPreFilledFromHomeSearch() {
        if (inputData != null) {
            if (!inputData.isEmpty()) {
                binding.viewPager.disableScroll(true);

                Log.e("ipd", inputData);
                ArrayList<GetServiceList.ServiceBean> filteredList = new ArrayList<>();
                for (GetServiceList.ServiceBean serviceBean : serviceArrayList) {
                    if (serviceBean.getServiceName().toLowerCase().contains(inputData.toLowerCase())) {
                        filteredList.add(serviceBean);
                        operatorId = serviceBean.getId();
                        bbpsId = serviceBean.getBbpsId();
                        setFilterData(viewPagerPosition, filteredList, operatorId, bbpsId);
                        if (serviceBean.getIsBillFetch().equals("0")) {
                            binding.amtLayout.setVisibility(View.VISIBLE);
                        } else {
                            binding.amtLayout.setVisibility(View.GONE);
                        }
                        onOperatorSelect();

                        //  binding.dotsIndicator.setVisibility(View.GONE);
                        break;
                    }
                }
            }
        }
    }


    private void setFilterData(int viewPagerPosition, ArrayList<GetServiceList.ServiceBean> filterData, String operatorId, String bbpsId) {
        Fragment fragment = mFragments.get(viewPagerPosition);
        if (fragment instanceof LandLineFragment) {
            ((LandLineFragment) fragment).setFilterData(viewPagerPosition, filterData, operatorId, bbpsId);
        }
    }

    private void filterData(String text) {
        try {
            ArrayList<GetServiceList.ServiceBean> filteredList2 = new ArrayList<>();


            for (GetServiceList.ServiceBean item : serviceArrayList) {
                if (item.getServiceName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList2.add(item);
                    Log.d("Filter data ", "" + item);
                }
            }

            setFilterData(viewPagerPosition, filteredList2, "", "");

            if (filteredList2.size() == 0) {
                binding.noDataFound.setVisibility(View.VISIBLE);
                binding.middleLayout.setVisibility(View.GONE);
            } else {
                binding.noDataFound.setVisibility(View.GONE);
                binding.middleLayout.setVisibility(View.VISIBLE);
            }
            // binding. .setVisibility(View.GONE);
            binding.placeHolderLayout.setVisibility(View.GONE);

            binding.dotsIndicator.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        binding.recyclerViewPlaceHolder.setLayoutManager(new LinearLayoutManager(activity));
        landLinePlaceHolderAdapter = new LandLinePlaceHolderAdapter(activity, tempArrayList, this, favMobile, inputValue1, inputValue2);
        binding.recyclerViewPlaceHolder.setAdapter(landLinePlaceHolderAdapter);
    }

    private void setFragments() {
        mFragments = new ArrayList<>();
        mList = new ArrayList<>();
        index = 0;

        if (serviceArrayList == null || serviceArrayList.size() == 0) {
            Utility.inflateNoDataFoundLayout(activity, binding.middleLayout, "No Service Found");
            return;
        }

        if (serviceArrayList.size() <= 8) {
            mFragments.add(new LandLineFragment().getInstance(index, serviceArrayList));
            mList.add(serviceArrayList);
            return;
        }

        for (int i = 0; i < serviceArrayList.size(); i++) {
            tempServiceArrayList.add(serviceArrayList.get(i));

            if (tempServiceArrayList.size() == 9) {
                addFragment(tempServiceArrayList);
            } else if (serviceArrayList.size() / 9 == index && tempServiceArrayList.size() == serviceArrayList.size() % 9) {
                addFragment(tempServiceArrayList);
            }
        }
    }

    private void addFragment(ArrayList<GetServiceList.ServiceBean> arrayList) {
        mFragments.add(new LandLineFragment().getInstance(index, arrayList));
        mList.add(arrayList);
        this.tempServiceArrayList = new ArrayList<>();
        index++;
    }

    private void setUpViewPager() {
        ViewPagerBalanceAdapter adapter = new ViewPagerBalanceAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mFragments);
        binding.viewPager.setAdapter(adapter);
        if (!search_type.equals("home_search")) {
            binding.dotsIndicator.setVisibility(View.VISIBLE);
            binding.dotsIndicator.setViewPager(binding.viewPager);

        } else {
            binding.dotsIndicator.setVisibility(View.GONE);
        }

        if (favMobile != null || inputValue1 != null)
            binding.dotsIndicator.setVisibility(View.GONE);
    }

    public void setOnDataListener(int position, String tag, int viewPagerPosition, String tag2) {
        tempArrayList.clear();
        valueArrayList.clear();
        operatorId = tag;
        bbpsId = tag2;


        //this.viewPagerPosition=viewPagerPosition;

        if (operatorId.equals("")) {
            binding.placeHolderLayout.setVisibility(View.GONE);

            binding.dotsIndicator.setVisibility(View.VISIBLE);
            if (binding.edtSearch.getText().toString().isEmpty()) {
                binding.viewPager.disableScroll(false);
                //   binding.dotsIndicator.setVisibility(View.VISIBLE);
            }

            if (inputData != null) {
                if (!inputData.isEmpty()) {
                    for (int i = 0; i < mList.size(); i++) {
                        setFilterData(i, mList.get(i), "", "");
                    }
                }
            }
            //binding.operator.setVisibility(View.INVISIBLE);
        } else {
            onOperatorSelect();
        }
        landLinePlaceHolderAdapter.setItems(operatorId);
    }

    private void onOperatorSelect() {
        //binding.operator.setVisibility(View.INVISIBLE);
        index = 0;
        for (GetServiceList.ServiceBean bean : serviceArrayList) {
            if (bean.getId().equals(operatorId)) {
                //Glide.with(activity).load(bean.getImgUrl()).into(binding.image);
                binding.titleOperator.setText(bean.getServiceName());
                //binding.select.setVisibility(View.VISIBLE);

                if (bean.getParams() == null || bean.getParams().size() == 0)
                    return;

                for (GetServiceList.ServiceBean.ParamsBean paramsBean : bean.getParams()) {
                    tempArrayList.add(paramsBean);
                    valueArrayList.add("");
                    if (landLinePlaceHolderAdapter != null)
                        landLinePlaceHolderAdapter.notifyDataSetChanged();
                }
            }
        }

        binding.viewPager.disableScroll(true);
        //  binding.dotsIndicator.setVisibility(View.GONE);

        binding.placeHolderLayout.setVisibility(View.VISIBLE);
        binding.dotsIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(int position, String tag) {
        valueArrayList.set(position, tag);
    }

    public void onClickRechargeNow() {

        pin = binding.editTextPin.getText().toString().trim();


        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("mobile", mobile);
        map1.put("amount", amount); // amount
        map1.put("pin", pin); // pin


        for (int i = 0; i < tempArrayList.size(); i++) {

            int minLength = 0;
            int maxLength = 0;
            try {
                minLength = Integer.parseInt(tempArrayList.get(i).getMinLength());
                maxLength = Integer.parseInt(tempArrayList.get(i).getMaxLength());
            } catch (Exception e) {

            }

            if (valueArrayList.get(i).isEmpty()) {
                if(minLength!=0) {
                    Utility.showToast(activity, "Please Enter" + " " + tempArrayList.get(i).getPlaceHolder(), "");
                    return;
                }
            } else if (valueArrayList.get(i).length() < minLength || valueArrayList.get(i).length() > maxLength) {
                Utility.showToast(activity, "Minimum Length" + " " + minLength + " and Max Length " + maxLength + " Required for " + tempArrayList.get(i).getPlaceHolder(), "");
                return;
            }
            map1.put(tempArrayList.get(i).getName(), valueArrayList.get(i));
        }
        if (pin.isEmpty()) {
            Utility.showToast(activity, "Please Enter 4 Digit Pin", "");
            return;
        }

        map1.put("operatorId", operatorId); // operatorId

        map1.put("bbpsId", bbpsId);

        map1.put("pin", pin); // pin

        if (!binding.editTextAmt.getText().toString().isEmpty())
            map1.put("amount", binding.editTextAmt.getText().toString());


        String request = Utility.mapWrapper(this, map1);


        rechargeNow(request);
    }

    private void rechargeNow(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest((Activity) activity,
                    Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {

            Log.e("ress", result);

            GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

            if (response.getResCode().equals(Constant.CODE_200)) {
                Bundle bundle = new Bundle();
                bundle.putString("outputJson", response.getData().getOutputJson());
                bundle.putString("uniqId", response.getData().getUniqId());
                bundle.putString("serviceType", response.getData().getType());
                bundle.putString("offerDetails", "");
                bundle.putString("pin", pin);
                bundle.putString("selectedMode", "");
                bundle.putString("operatorId", operatorId);
                bundle.putString("coupon", selectedCoupon);
                bundle.putString("couponId", selectedCouponId);
                bundle.putString("extraData", response.getData().getExtraData());
                bundle.putParcelable(Constant.INSUFFICIENTDATA, response.getData().getInsufficientData());
                bundle.putParcelable(Constant.POPUPDATA, response.getData().getPopupData());


                for (int i = 0; i < tempArrayList.size(); i++) {
                    if (tempArrayList.get(i).getName().equalsIgnoreCase("amount")) {
                        bundle.putString("amount", tempArrayList.get(i).getName());
                    } else {
                        bundle.putString("amount", amount);
                    }
                    if (tempArrayList.get(i).getName().equalsIgnoreCase("mobile")) {
                        bundle.putString("mobile", tempArrayList.get(i).getName());
                    } else {
                        bundle.putString("mobile", mobile);
                    }
                }
                bundle.putString("mobile", response.getData().getMobile());


                Utility.openCheckOutActivity(activity, response.getData().getType(), bundle);

            } else {
                Utility.showToast(activity, response.getResText(), response.getResCode());
            }
        } else {
            //Utility.showToast(activity, activity.getString(R.string.server_not_responding), "");
            startActivity(new Intent(this, ServerNotResponseActivity.class));
        }
    }

    @Override
    public void onSelect(String amount, String details, String extraoffer, String commission) {

        binding.edtSearch.setText("");

        Utility.hideKeyboard(activity);
    }

    public void setTopIcon(String url) {
        topIconUrl = url;
        Glide.with(this)
                .load(url)
                .into(binding.bbpsIcon);
    }

    public void setAmountVisibility(int visibility) {
        binding.amtLayout.setVisibility(visibility);
    }
}