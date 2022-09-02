package in.forpay.activity.balance;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import in.forpay.R;
import in.forpay.activity.ContactActivity;
import in.forpay.activity.OffersActivity;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.adapter.AutoCompleteAdapter;
import in.forpay.adapter.balance.CouponOfferAdapter;
import in.forpay.adapter.balance.ViewPagerBalanceAdapter;
import in.forpay.database.DataPlansModel;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityMobileBinding;
import in.forpay.fragment.balance.MobileFragment;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.listener.ItemClickListener;
import in.forpay.listener.OfferListener;
import in.forpay.model.ContactList;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.model.request.OfferModel;
import in.forpay.model.response.GetLoginValidateResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import in.forpay.util.googleAd;

public class MobileActivity extends AppCompatActivity implements OfferListener, ItemClickListener, TextToSpeech.OnInitListener {

    private ActivityMobileBinding binding;
    private AppCompatActivity activity;
    private List<Fragment> mFragments;
    private ArrayList<ArrayList<MainRechargeModel.ServiceBean>> mList;
    private int index = 0;
    private DatabaseHelper databaseHelper;
    private ProgressHelper progressHelper;
    private ArrayList<MainRechargeModel.ServiceBean> serviceArrayList;
    private OxyMePref oxyMePref;
    private String number = "", operatorId = "";
    boolean isApiCalling;
    public static String selectedcircle;
    private int circleSelect = 0;
    private String circleId = "";
    String extraAmount = "0";
    public static String mobileoffersresponse;
    private String amount = "0";
    private String pin = "";
    private String mobile = "";
    private String type;
    private ArrayList<MainRechargeModel.ServiceBean> tempServiceArrayList = new ArrayList<>();
    private String title;
    ArrayList<OfferModel> dataBaseOfferArrayList = new ArrayList<>();
    private int viewPagerPosition = 0;
    private TextToSpeech textToSpeech;
    private CouponOfferAdapter couponOfferAdapter;
    private MainRechargeModel model;
    private String selectedCoupon = "";
    private String selectedCouponId = "";
    private String favMobile = "";
    private String inputData = "";
    ViewPagerBalanceAdapter adapter;
    private String topIconUrl = "";

    private static final int REQUEST_SELECT_CONTACT = 1;

    private HashMap<String, String> modeArray = new HashMap<String, String>();
    String defaultSelectedMode = "";
    String offerDetails = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mobile);
        activity = this;
        databaseHelper = new DatabaseHelper(activity);
        progressHelper = new ProgressHelper(activity);
        oxyMePref = new OxyMePref(activity);

        type = getIntent().getStringExtra(Constant.TYPE);
        title = getIntent().getStringExtra(Constant.TITLE_SHOW);
        inputData = getIntent().getStringExtra(Constant.INPUT_DATA);
        favMobile = getIntent().getStringExtra(Constant.FAV_MOBILE);

        binding.autoCompleteTextView.setText(favMobile);
        //Log.d("MobileActivity","Clicked "+type);
        try {
            AdView adView = new AdView(activity);

            googleAd gAd = new googleAd(activity, adView);
            if (gAd.getAd() != null) {
                binding.adView.setVisibility(View.VISIBLE);
                binding.adView.loadAd(gAd.getAd());
            }
        }
        catch (Exception e){

        }

        init();
        binding.modtabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.helptext.setText(modeArray.get(tab.getText()));
//                Toasty.info(MobileActivity.this,modeArray.get(tab.getText())).show();
                defaultSelectedMode = tab.getText().toString();

                if (defaultSelectedMode.equals("Fast"))
                    binding.commissionLapyout.setVisibility(View.INVISIBLE);
                else
                    binding.commissionLapyout.setVisibility(View.VISIBLE);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void init() {
        progressHelper.show();

        /*
        Utility.getHomeUpdate(activity, "", type, new HomeUpdateListener() {
            @Override
            public void onDone() {
                progressHelper.dismiss();
                parseHomeUpdateResponse(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE));

            }
        });

         */


        Utility.getServiceList(activity, type, Constant.METHOD_HOME_UPDATE, false,"MobileActivity", new HomeUpdateListener() {
            @Override
            public void onDone() {
                progressHelper.dismiss();
                String serviceListLocation = "serviceList_" + type + Constant.METHOD_HOME_UPDATE;
                parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation));
                //parseHomeUpdateResponse(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE));

            }
        });


        binding.bottomLayout.setVisibility(View.GONE);


        binding.title.setText("Select Operator");

        if (type.equalsIgnoreCase("metroRecharge") || type.equalsIgnoreCase("bbpsPostpaidMobileRecharge")) {
            binding.textViewCircle.setVisibility(View.GONE);
            binding.editTextAmount.setTypeface(ResourcesCompat.getFont(activity, R.font.sf_regular));
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            binding.editTextAmount.setLayoutParams(lp);
            binding.offerImg.setImageResource(R.drawable.ic_ruppies2);
            binding.offerImg.setEnabled(false);
            binding.offerdetails.setVisibility(View.GONE);
            //binding.pinLayout.setVisibility(View.GONE);
        }

        if (type.equalsIgnoreCase("dthRecharge")) {

        }

        phoneNumber();

        binding.backPress.setOnClickListener(view -> onBackPressed());
        binding.btnContactList.setOnClickListener(v -> {

            //startActivity(new Intent(activity, ContactActivity.class).putExtra(Constant.IS_FROM_NEW_RECHARGE, true);

            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        });

        binding.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (type.equals("prepaidMobileRecharge")) {
                    if (filterNumber(editable.toString()).length() == 10) {
                        if (!isApiCalling) {
                            isApiCalling = true;
                            getDataPlan();
                        }
                    }
                }
            }
        });

        binding.editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                String string = s.toString();
                Log.e("textchanged 1", string);

                if (!string.equals("") && string != null) {

                    if (databaseHelper.getDataPlans(string) == null || databaseHelper.getDataPlans(string).size() > 0) {
                        Log.e("textchabged", String.valueOf(databaseHelper.getDataPlans(string).get(0).getDetail()));

                        binding.offerdetails.setText("" + String.valueOf(databaseHelper.getDataPlans(string).get(0).getDetail()));
                        binding.offerdetails.setVisibility(View.VISIBLE);
                        offerDetails = String.valueOf(databaseHelper.getDataPlans(string).get(0).getDetail());

                        if (databaseHelper.getDataPlans(string).get(0).getExtraOffer().equals("0") || databaseHelper.getDataPlans(string).get(0).getExtraOffer().equals("")) {
                            binding.commissiontv.setText(databaseHelper.getDataPlans(string).get(0).getCommission());

                        } else {
                            extraAmount = databaseHelper.getDataPlans(string).get(0).getExtraOffer();

                            binding.commissiontv.setText(databaseHelper.getDataPlans(string).get(0).getCommission() + " + " + extraAmount);

                        }
                        if (!defaultSelectedMode.equals("Fast"))
                            binding.commissionLapyout.setVisibility(View.VISIBLE);

                    } else {

                        extraAmount = "0";

                        binding.offerdetails.setText("");

                        binding.commissiontv.setText("0 + 0");

                        binding.offerImg.setVisibility(View.VISIBLE);
                        binding.commissionLapyout.setVisibility(View.INVISIBLE);

                    }
                }
            }
        });

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
                    binding.dotsIndicator.setVisibility(View.VISIBLE);
                    binding.inflateLayout.setVisibility(View.VISIBLE);
                    binding.bottomLayout.setVisibility(View.GONE);

                    binding.viewPager.disableScroll(false);

                    if (mList != null) {
                        for (int i = 0; i < mList.size(); i++) {
                            setFilterData(i, mList.get(i), "");
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

        binding.textViewCircle.setOnClickListener(view -> chooseCircle());
        binding.offerImg.setOnClickListener(view -> onClickOffers());
        binding.commissionLapyout.setOnClickListener(v -> onClickOffers());

        binding.recharge.setOnClickListener(view -> {
            callRechargeApi();
        });

        textToSpeech = new TextToSpeech(this, this);
        binding.textToSpeech.setOnClickListener(view -> {
            setText(binding.autoCompleteTextView.getText().toString());
        });
    }

    private void parseHomeUpdateResponse(String response) {
        try {
            model = new Gson().fromJson(response, MainRechargeModel.class);
            if (model.getResCode().equals(Constant.CODE_200) && model.getService() != null) {
                Log.d("HomeUpdateResponse", "response " + response);


                serviceArrayList = new ArrayList<>();

                for (MainRechargeModel.ServiceBean serviceBean : model.getService()) {
                    if (serviceBean.getType().equalsIgnoreCase(type)) {
                        serviceArrayList.add(serviceBean);
                    }
                }

                if (serviceArrayList == null || serviceArrayList.size() < 12) {
                    binding.searchLayout.setVisibility(View.GONE);
                }

                setFragments();
                setUpViewPager();

                /*if (inputData==null || inputData.isEmpty()) {
                    setFragments();
                    setUpViewPager();
                }else {
                    setPreFilledFromHomeSearch();
                }*/
                setCouponAdapter();
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
                ArrayList<MainRechargeModel.ServiceBean> filteredList = new ArrayList<>();
                for (MainRechargeModel.ServiceBean serviceBean : serviceArrayList) {
                    if (serviceBean.getService().toLowerCase().contains(inputData.toLowerCase())) {
                        filteredList.add(serviceBean);
                        operatorId = serviceBean.getId();
                        /*mFragments = new ArrayList<>();
                        mFragments.add(new MobileFragment().getInstance(index, filteredList));
                        setUpViewPager();*/
                        setFilterData(viewPagerPosition, filteredList, operatorId);
                        onOperatorSelect();
                        binding.dotsIndicator.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void setCouponAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        binding.recyclerView.setAdapter(couponOfferAdapter);
    }

    @Override
    public void onItemClick(int position, String tag) {

        couponOfferAdapter.setItems(selectedCouponId);

    }

    private void setFilterData(int viewPagerPosition, ArrayList<MainRechargeModel.ServiceBean> filterData, String operatorId) {
        Fragment fragment = mFragments.get(viewPagerPosition);
        if (fragment instanceof MobileFragment) {
            ((MobileFragment) fragment).setFilterData(viewPagerPosition, filterData, operatorId);
        }
    }

    private void filterData(String text) {
        try {
            ArrayList<MainRechargeModel.ServiceBean> filteredList2 = new ArrayList<>();

            for (MainRechargeModel.ServiceBean item : serviceArrayList) {
                if (item.getService().toLowerCase().contains(text.toLowerCase()) || item.getType().toLowerCase().contains(text.toLowerCase())) {
                    filteredList2.add(item);
                    Log.d("Filter data ", "" + item);
                }
            }

            setFilterData(viewPagerPosition, filteredList2, "");

            if (filteredList2.size() == 0) {
                binding.noDataFound.setVisibility(View.VISIBLE);
                binding.inflateLayout.setVisibility(View.GONE);
            } else {
                binding.noDataFound.setVisibility(View.GONE);
                binding.inflateLayout.setVisibility(View.VISIBLE);
            }
            binding.dotsIndicator.setVisibility(View.GONE);
            binding.bottomLayout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callRechargeApi() {
        if (validation()) {
            mobile = binding.autoCompleteTextView.getText().toString().trim();
            amount = binding.editTextAmount.getText().toString().trim();
            pin = binding.editTextPin.getText().toString().trim();
            circleSelect = -1;
            binding.textViewCircle.setText("Select Circle");


            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(activity)); // key
            map1.put("imei", Utility.getImei(activity)); // imei
            map1.put("versionCode", Utility.getVersionCode(activity)); // version code
            map1.put("os", Utility.getOs(activity));
            map1.put("mobile", mobile); // mobile
            map1.put("operatorId", operatorId); // operatorId
            map1.put("amount", amount); // amount
            map1.put("pin", pin); // pin
            map1.put("extraAmount", extraAmount);
            map1.put("selectedMode", defaultSelectedMode);


            String request = Utility.mapWrapper(this, map1);

            rechargeNow(request);
            binding.editTextPin.setText("");
        }
    }

    private void setFragments() {
        mFragments = new ArrayList<>();
        mList = new ArrayList<>();
        index = 0;

        if (serviceArrayList == null || serviceArrayList.size() == 0) {
            Utility.inflateNoDataFoundLayout(activity, binding.inflateLayout, "No Service Found");
            return;
        }
        if (serviceArrayList.size() <= 8) {
            mFragments.add(new MobileFragment().getInstance(index, serviceArrayList));
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

    private void addFragment(ArrayList<MainRechargeModel.ServiceBean> arrayList) {
        mFragments.add(new MobileFragment().getInstance(index, arrayList));
        mList.add(arrayList);
        this.tempServiceArrayList = new ArrayList<>();
        index++;
    }

    private void setUpViewPager() {
        adapter = new ViewPagerBalanceAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mFragments);
        binding.viewPager.setAdapter(adapter);
        binding.dotsIndicator.setViewPager(binding.viewPager);
    }

    public void setOnDataListener(int position, String tag) {
        operatorId = tag;

        if (operatorId.equals("")) {
            binding.bottomLayout.setVisibility(View.GONE);
            binding.viewPager.disableScroll(false);
            binding.dotsIndicator.setVisibility(View.VISIBLE);

            if (inputData != null) {
                if (!inputData.isEmpty()) {
                    for (int i = 0; i < mList.size(); i++) {
                        setFilterData(i, mList.get(i), "");
                    }
                }
            }
            /*if (inputData != null) {
                if (!inputData.isEmpty()){
                    //getSupportFragmentManager().beginTransaction().remove(mFragments.get(0)).commit();
                    adapter.removeFragment(0);
                    setFragments();
                    setUpViewPager();
                    getSupportFragmentManager().beginTransaction().detach(mFragments.get(0)).attach(mFragments.get(0)).addToBackStack(null).commit();
                }
            }*/

        } else {
            onOperatorSelect();
        }
    }

    private void onOperatorSelect() {
        Log.d("Operator selected type", "" + type);
        if (type.equals("dthRecharge")) {
            binding.autoCompleteTextView.setHint(getString(R.string.title_dth_number));
        }
        circleSelect = -1;
        binding.textViewCircle.setText(getString(R.string.title_select_circle));

        binding.bottomLayout.setVisibility(View.VISIBLE);

        for (MainRechargeModel.ServiceBean bean : serviceArrayList) {
            if (bean.getId().equals(operatorId)) {
                //Glide.with(activity).load(bean.getImgUrl()).into(binding.image);
                binding.titleOperator.setText(bean.getService());
                //binding.select.setVisibility(View.VISIBLE);
            }
        }

        binding.viewPager.disableScroll(true);
        binding.dotsIndicator.setVisibility(View.GONE);
        databaseHelper.deleteDataPlansTable();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Constant.CONTACT_NUMBER.equals("")) {
            binding.autoCompleteTextView.setText(filterNumber(Constant.CONTACT_NUMBER));
            Constant.CONTACT_NUMBER = "";
            //getDataPlan();
        }
    }

    private String filterNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replace(" ", "");
        phoneNumber = phoneNumber.replace("+91", "");
        phoneNumber = phoneNumber.replace("+ 91", "");
        phoneNumber = phoneNumber.replaceAll("\\s+", "");
        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        number = phoneNumber;
        return phoneNumber;
    }

    private void phoneNumber() {
        if (type.equals("dthRecharge")) {
            final ArrayList<ContactList> list = databaseHelper.getConactListTmp("", "dth");
            if (list == null || list.size() == 0) {
                //Utility.showToast(getActivity(), "No contact found","");
                return;
            }
            final ArrayList<String> list1 = new ArrayList<>();

            for (ContactList contact : list) {
                String phoneTemp = contact.getPhoneNumber();
                phoneTemp = phoneTemp.replace("+91", "");
                if (phoneTemp.length() > 5) {
                    String s = phoneTemp.substring(0, 1);
                    if (s.equals("0")) {
                        phoneTemp = phoneTemp.replaceFirst("0", "");
                    }

                    list1.add(phoneTemp + " " + contact.getName());
                }
            }
            setContactAdapter(list, list1, list);
        } else if (type.equals("prepaidMobileRecharge")) {
            final ArrayList<ContactList> list = Utility.getContactList(activity);
            if (list == null || list.size() == 0) {
                //Utility.showToast(getActivity(), "No contact found", "");
                return;
            }
            //Log.d("Chatlist","Fragment Phone 2");
            final ArrayList<String> list1 = new ArrayList<>();

            for (ContactList contact : list) {
                String phoneTemp = contact.getPhoneNumber();
                phoneTemp = phoneTemp.replace(" ", "");
                phoneTemp = phoneTemp.replace("+91", "");
                phoneTemp = phoneTemp.replace("+ 91", "");
                phoneTemp = phoneTemp.replaceAll("\\s+", "");
                list1.add(phoneTemp + " " + contact.getName());
            }


            final ArrayList<ContactList> listTmp = databaseHelper.getConactListTmp("", "recharge");
            if (listTmp == null || listTmp.size() == 0) {

            } else {

                for (ContactList contact2 : listTmp) {
                    String phoneTemp2 = contact2.getPhoneNumber();
                    list1.add(phoneTemp2 + " " + contact2.getName());

                }
            }
            setContactAdapter(list, list1, listTmp);
        } else if (type.equals("bbpsPostpaidMobileRecharge")) {
            final ArrayList<ContactList> list = Utility.getContactList(activity);
            if (list == null || list.size() == 0) {
                //Utility.showToast(getActivity(), "No contact found","");
                return;
            }
            final ArrayList<String> list1 = new ArrayList<>();
            for (ContactList contact : list) {
                String phoneTemp = contact.getPhoneNumber();
                phoneTemp = phoneTemp.replace("+91", "");

                String s = "";
                if (phoneTemp.length() > 1)
                    s = phoneTemp.substring(0, 1);
                if (s.equals("0")) {
                    phoneTemp = phoneTemp.replaceFirst("0", "");
                }

                list1.add(phoneTemp + " " + contact.getName());
            }


            final ArrayList<ContactList> listTmp = databaseHelper.getConactListTmp("", "bbpsPostpaidMobileRecharge");
            if (listTmp == null || listTmp.size() == 0) {

            } else {

                for (ContactList contact2 : listTmp) {
                    String phoneTemp2 = contact2.getPhoneNumber();
                    list1.add(phoneTemp2 + " " + contact2.getName());

                }
            }
            setContactAdapter(list, list1, listTmp);
        }
    }

    private void setContactAdapter(ArrayList<ContactList> list, ArrayList<String> list1, ArrayList<ContactList> listTmp) {
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(activity,
                android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
                list1);
        /*binding.autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, list1));*/
        binding.autoCompleteTextView.setAdapter(adapter);
        binding.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String namePhone = binding.autoCompleteTextView.getText().toString().trim();



                if (!TextUtils.isEmpty(namePhone)) {

                    //Log.d("HomeActivity","phone "+namePhone);
                    int position = list1.indexOf(namePhone);

                    try {

                        String phoneNumber = list.get(position).getPhoneNumber();

                        phoneNumber = phoneNumber.replace(" ", "");
                        phoneNumber = phoneNumber.replace("+91", "");
                        phoneNumber = phoneNumber.replace("+ 91", "");
                        phoneNumber = phoneNumber.replaceAll("\\s+", "");
                        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");

                        binding.autoCompleteTextView.setText(phoneNumber);
                    } catch (Exception e) {
                        //Log.d("HomeActivity","e "+e);
                        if (listTmp != null) {
                            try {
                                String phoneNumber = listTmp.get(position - list.size()).getPhoneNumber();
                                phoneNumber = phoneNumber.replace(" ", "");
                                phoneNumber = phoneNumber.replace("+91", "");
                                phoneNumber = phoneNumber.replace("+ 91", "");
                                phoneNumber = phoneNumber.replaceAll("\\s+", "");
                                phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
                                binding.autoCompleteTextView.setText(phoneNumber);

                            } catch (Exception e2) {

                            }
                        }
                    }
                }
            }
        });
    }

    private void chooseCircle() {

        final ArrayList<GetLoginValidateResponse.Circle> list = Utility.getCircleList(activity);
        if (list != null && list.size() > 0) {
            ArrayList<String> listTemp = new ArrayList<>();
            for (GetLoginValidateResponse.Circle circle : list) {
                listTemp.add(circle.getCircle());
            }
            final String[] listArray = listTemp.toArray(new String[listTemp.size()]);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
            mBuilder.setTitle("Choose an circle");
            mBuilder.setSingleChoiceItems(listArray, circleSelect, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    circleSelect = i;
                    circleId = list.get(i).getId();
                    binding.textViewCircle.setText(listArray[i]);
                    dialogInterface.dismiss();

                    setPingRequest(true);
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        } else {
            Utility.showToast(activity, getString(R.string.something_went_wrong), "");
        }
    }

    private void setPingRequest(boolean isGetOfferCall) {

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("operatorId", operatorId); // operatorId
        map1.put("circleId", circleId); // circleId
        map1.put("mobile", binding.autoCompleteTextView.getText().toString()); // mobile

        String request = Utility.mapWrapper(this, map1);

        Log.e("CircleSelectFrag", request);
        selectedcircle = request;
        //  binding.offerlayoutrl.setVisibility(View.GONE);
        //   binding.commissionLapyout.setVisibility(View.GONE);
        if (isGetOfferCall)
            getOffer(request);
    }

    public void onClickOffers() {

        if (TextUtils.isEmpty(operatorId)) {
            Utility.showToast(activity, "Please select operator first.", "");
            return;
        }

        if (binding.textViewCircle.getText().toString().equalsIgnoreCase("Select Circle")) {
            Utility.showToast(activity, "Please select circle first.", "");
            return;
        }

//        if(defaultSelectedMode.equals("Fast"))
//            return;

        Intent intent = new Intent(activity, OffersActivity.class);
        intent.putExtra("id", operatorId);
        intent.putExtra("from", type.equals("prepaidMobileRecharge") ? "prepaidMobileRecharge" : "dthRecharge");
        startActivityForResult(intent, Constant.REQUEST_CODE_RECHARGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_RECHARGE && resultCode == RESULT_OK && data != null) {
            if (data.getExtras() != null) {
                String amount = data.getExtras().getString("amount");
                String details = data.getExtras().getString("details");


                String extraoffer = data.getExtras().getString("extraoffer");
                String commission = data.getExtras().getString("commission");


                binding.editTextAmount.setText(amount);
                binding.offerdetails.setText(details);
                binding.offerdetails.setVisibility(View.VISIBLE);
                if (extraoffer.equals("0") || extraoffer.equals("")) {
                    //   binding.commissiontv.setText(commission);
                    extraAmount = "0";

                } else {
                    extraAmount = extraoffer;

                    // binding.commissiontv.setText(commission + " + " + extraoffer);

                }
                /*binding.btnBrowsePlan.setVisibility(View.GONE);
                binding.offerlayoutrl.setVisibility(View.VISIBLE);
                binding.commissionLapyout.setVisibility(View.VISIBLE);*/
                binding.commissionLapyout.setVisibility(View.VISIBLE);
            }
        }
        else if(requestCode==REQUEST_SELECT_CONTACT && resultCode==RESULT_OK){
            if (data != null) {
                binding.autoCompleteTextView.setText(data.getStringExtra("mobile"));

            }

        }
    }

    private void getDataPlan() {
        if (Utility.isNetworkConnected(this)) {


            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("versionCode", Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));
            map1.put("operatorId", operatorId); // operatorId
            map1.put("circleId", circleId); // circleId
            map1.put("mobile", binding.autoCompleteTextView.getText().toString()); // mobile

            String request = Utility.mapWrapper(this, map1);

            getOffer(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void getOffer(final String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            binding.modtabLayout.removeAllTabs();

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_GET_OFFER, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            //Log.e("DataInst", "1"+result);
                            mobileoffersresponse = result;
                            if (type.equals("prepaidMobileRecharge"))
                                Constant.OFFER_DATA_PLAN1 = "";
                            else
                                Constant.OFFER_DATA_PLAN2 = "";
                            //Log.e("DataInst", "2");
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                try {

                                    for (int i = 0; i < jsonObject.getJSONArray("modeArray").length(); i++) {
                                        JSONObject mode = jsonObject.getJSONArray("modeArray").getJSONObject(i);
                                        String key = mode.keys().next();
                                        modeArray.put(key, mode.getString(key));
                                        binding.modtabLayout.addTab(binding.modtabLayout.newTab().setText(key));
                                    }
                                    defaultSelectedMode = jsonObject.getString("defaultSelectedMode");
                                    //Log.e("defaultselected",defaultSelectedMode);
                                } catch (Exception e3) {

                                }


                                if (binding.modtabLayout.getTabCount() > 0) {
                                    binding.modeTabView.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < binding.modtabLayout.getTabCount(); i++) {
                                        TabLayout.Tab tab = binding.modtabLayout.getTabAt(i);
                                        Log.e("select", defaultSelectedMode + " " + tab.getText().toString() + " are "
                                                + tab.getText().toString().equals(defaultSelectedMode));
                                        if (tab.getText().toString().equals(defaultSelectedMode)) {
                                            tab.select();
                                            binding.commissionLapyout.setVisibility(View.INVISIBLE);
                                            break;
                                        }
                                    }

                                }

                                JSONArray dataarray = jsonObject.getJSONArray("data");
                                String selectedCircle = jsonObject.getString("selectedCircle");
                                Log.e("DataInst", dataarray.length() + "");
                                Utility.showToastLatest(activity, " " + dataarray.length() + " Offer found ", "SUCCESS");

                                for (int j = 0; j < dataarray.length(); j++) {

                                    JSONObject jsonObject1 = dataarray.getJSONObject(j);

                                    String type = jsonObject1.getString("type");
                                    String amount = jsonObject1.getString("amount");
                                    String detail = jsonObject1.getString("detail");
                                    String validity = jsonObject1.getString("validity");
                                    String talktime = jsonObject1.getString("talktime");
                                    String extraOffer = jsonObject1.getInt("extraOffer") + "";
                                    String commission = jsonObject1.getString("commission");
                                    String data = jsonObject1.getString("data");

                                    try {
                                        databaseHelper.insertDataPlans(new DataPlansModel(type, amount, detail, validity, talktime, extraOffer, commission, data));
                                        //Log.e("DataInst", j + "");

                                        //Utility.showToast(getActivity(), "inserted "+j , "");
                                    } catch (Exception e3) {
                                        //Utility.showToast(getActivity(), e3.toString() , "");
                                    }
                                }
                                try {
                                    JSONArray circlesarray = jsonObject.getJSONArray("circles");
                                    for (int j = 0; j < circlesarray.length(); j++) {

                                        JSONObject jsonObject1 = circlesarray.getJSONObject(j);
                                        String id = jsonObject1.getString("id");
                                        String circle = jsonObject1.getString("circle");

                                        if (selectedCircle.equals(id)) {
                                            circleSelect = j;
                                            circleId = id;
                                            binding.textViewCircle.setText(circle);
                                            setPingRequest(false);
                                        }
                                    }

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                                Log.e("DataInst", "3" + e1.toString());
                            }
                            progressHelper.dismiss();
                            isApiCalling = false;
//                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private boolean validation() {
        String number = binding.autoCompleteTextView.getText().toString().trim();
        amount = binding.editTextAmount.getText().toString().trim();
        pin = binding.editTextPin.getText().toString().trim();
        if (TextUtils.isEmpty(operatorId)
                || operatorId.equalsIgnoreCase("Select operator")) {
            Utility.showToast(activity, "Please select operator", "");
            return false;
        } else if (TextUtils.isEmpty(number)) {
            Utility.showToast(activity, "Please enter mobile number", "");
            return false;
        } else if (TextUtils.isEmpty(amount)) {
            Utility.showToast(activity, "Please enter amount", "");
            return false;
        } else if (TextUtils.isEmpty(pin)) {
            Utility.showToast(activity, "Please enter pin", "");
            return false;
        }
        return true;
    }

    private void rechargeNow(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
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

    /**
     * Parse response
     */
    private void parseResponse(String result) {
        Log.e("ress", result);
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

            if (response.getResCode().equals(Constant.CODE_200)) {
                Bundle bundle = new Bundle();
                bundle.putString("outputJson", response.getData().getOutputJson());
                bundle.putString("uniqId", response.getData().getUniqId());
                bundle.putString("serviceType", response.getData().getType());
                bundle.putString("offerDetails", offerDetails);
                bundle.putString("pin", pin);
                bundle.putString("amount", amount);
                bundle.putString("selectedMode", defaultSelectedMode);
                bundle.putString("mobile", mobile);
                bundle.putString("operatorId", operatorId);
                bundle.putString("coupon", selectedCoupon);
                bundle.putString("couponId", selectedCouponId);
                bundle.putString("extraData", response.getData().getExtraData());
                bundle.putParcelable(Constant.INSUFFICIENTDATA, response.getData().getInsufficientData());
                bundle.putParcelable(Constant.POPUPDATA, response.getData().getPopupData());


                Utility.openCheckOutActivity(activity, response.getData().getType(), bundle);


            } else {
                Utility.showToast(activity, response.getResText(), response.getResCode());
            }

                /*
                GetRechargeNowResponse response = new Gson().fromJson(result, GetRechargeNowResponse.class);
                if (response.getData().getResCode().equals(Constant.CODE_200)) {
                    Utility.showToast(getActivity(), response.getData().getResText(), response.getData().getResCode());
                    Utility.setBalance(getActivity(), response.getData().getBal());
                    Utility.setCommissionBalance(getActivity(), response.getData().getCommissionBal());
                    addOrderId(response.getData());
                    refreshUI();
                } else if (response.getData().getResCode().equals(Constant.CODE_201)) {
                    Utility.showToast(getActivity(), response.getData().getResText(), response.getData().getResCode());
                    Utility.setBalance(getActivity(), response.getData().getBal());
                    Utility.setCommissionBalance(getActivity(), response.getData().getCommissionBal());
                    addOrderId(response.getData());
                    refreshUI();
                } else {
                    if (response.getData().getStatus().equals(Constant.FAILED)) {
                        if (!response.getData().getOrderId().equals("0")) {
                            addOrderId(response.getData());
                        }
                    }
                    Utility.showToast(getActivity(), response.getData().getResText(), response.getData().getResCode());
                }
                */

        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding), "");
            startActivity(new Intent(this, ServerNotResponseActivity.class));
        }
    }

    @Override
    public void onSelect(String amount, String details, String extraoffer, String commission) {
        binding.editTextAmount.setText(amount);
        binding.offerdetails.setText(details);
        binding.offerdetails.setVisibility(View.VISIBLE);
        if (extraoffer.equals("0") || extraoffer.equals("")) {
            //   binding.commissiontv.setText(commission);
            extraAmount = "0";
        } else {
            extraAmount = extraoffer;
            // binding.commissiontv.setText(commission + " + " + extraoffer);

        }
        binding.edtSearch.setText("");
        Utility.hideKeyboard(activity);
    }

    public void setText(final String s) {
        try {
            if (s.isEmpty())
                return;
            final int[] i = new int[1];
            i[0] = 0;
            final int length = s.length();
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    char c = s.charAt(i[0]);
                    Log.d("Strange", "" + c);
                    speakOut(String.valueOf(c));
                    i[0]++;
                }
            };

            final Timer timer = new Timer();
            TimerTask taskEverySplitSecond = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                    if (i[0] == length - 1) {
                        timer.cancel();
                    }
                }
            };
            timer.schedule(taskEverySplitSecond, 1, 700);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "Language not supported", Toast.LENGTH_SHORT).show();
            } else {
                binding.textToSpeech.setEnabled(true);
            }

        } else {
            //Toast.makeText(getApplicationContext(), "Init failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void speakOut(String first) {

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

                final String keyword = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Started" + keyword, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDone(String s) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Done ", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String s) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(first, TextToSpeech.QUEUE_FLUSH, params, "Dummy String");
        }
    }

    public void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    public void setTopIcon(String url) {
        topIconUrl = url;
        Glide.with(this)
                .load(url)
                .into(binding.bbpsIcon);

    }
}