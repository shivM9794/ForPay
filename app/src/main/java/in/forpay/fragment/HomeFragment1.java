package in.forpay.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.activity.balance.GiftVoucherActivity;
import in.forpay.activity.balance.GiftVoucherDetailActivity;
import in.forpay.adapter.HomeBannerAdapter;
import in.forpay.adapter.balance.ViewPagerBalanceAdapter;
import in.forpay.databinding.FragmentHomeBinding;
import in.forpay.fragment.balance.MainRechargeFragment;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.model.request.RechargeHistory;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class HomeFragment1 extends Fragment implements ItemClickListener {

    private FragmentHomeBinding mBinding;
    Bundle bundle;
    private List<Fragment> mFragments;
    private int index = 0;
    private int itemSize=12;
    private Activity activity;
    private ProgressHelper progressHelper;
    private ArrayList<Object> arrayList = new ArrayList<>();
    private ArrayList<MainRechargeModel.MainMenuBean> arrayList1;
    private ArrayList<MainRechargeModel.HomeBannerListBean> arrayListHomeBanner = new ArrayList<>();
    private OxyMePref oxyMePref;

    public static HomeFragment1 newInstance() {
        return new HomeFragment1();
    }

    private static int k = 0;
    private String from = "";
    private MainRechargeModel model;
    private ArrayList<ArrayList<MainRechargeModel.MainMenuBean>> mList;

    String serviceListLocation = "serviceList_HomeUpdates" + Constant.METHOD_HOME_UPDATE;
    private int mInterval = 20000;
    private Handler mHandler;


    public HomeFragment1() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mBinding.setFragment(this);
        oxyMePref = new OxyMePref(activity);
        if (getActivity() != null) {
            Utility.setAppLocale(Utility.getDefaultLanguage(getActivity()), getActivity());

        }
        initView();
        //Log.d("Database","view 1");
        //Utility.fillSlider(mBinding,getActivity());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressHelper = new ProgressHelper(getActivity());
        init();
        Log.d("HomeActivity", "View Created");
    }

    private void initView() {


        init("");

    }

    @Override
    public void onResume() {
        super.onResume();
        setBalance();
        showItems();

    }


    private void init(String mobileSearch) {
        progressHelper = new ProgressHelper(activity);

        mHandler = new Handler();

    }


    private void showItems() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation));
                //parseHomeUpdateResponse(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE));
            }
        }, 150); // After 3 seconds


    }


    private void parseHomeUpdateResponse(String response) {
        try {
            mBinding.viewScroll.setVisibility(View.GONE);
            mBinding.dotsIndicator.setVisibility(View.GONE);

            arrayListHomeBanner.clear();
            MainRechargeModel model = new Gson().fromJson(response, MainRechargeModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                //Log.d("HomeActivity","response 2");

                if (model.getHomeBannerList() != null) {
                    if (model.getHomeBannerList().size() > 0) {
                        Log.d("HomeBannerData","response "+model.getHomeBannerList().size()+" Location "+serviceListLocation);
                        mBinding.viewScroll.setVisibility(View.VISIBLE);
                        if (model.getHomeBannerList().size() > 1) {
                            mBinding.dotsIndicator.setVisibility(View.VISIBLE);
                        }
                        arrayListHomeBanner.addAll(model.getHomeBannerList());
                        setAdapterBanner();

                    }

                }
                setFragments(model);
                setUpViewPager();
            }

        } catch (Exception e) {
            Log.d("HomeActivity", "response error " + e.toString());
            e.printStackTrace();
        }
    }

    private void setAdapterBanner() {


        if (arrayListHomeBanner.size() != 0) {

            Log.d("HomeActivity", "Size is " + arrayListHomeBanner.size());
            mBinding.dotsIndicator.setCount(arrayListHomeBanner.size());
        }
        mBinding.viewScroll.setOrientation(DSVOrientation.HORIZONTAL);
        HomeBannerAdapter homeBannerAdapter = new HomeBannerAdapter(activity, arrayListHomeBanner, mBinding.dotsIndicator, this);
        mBinding.viewScroll.setAdapter(homeBannerAdapter);
        mBinding.viewScroll.setItemTransitionTimeMillis(750);


        mBinding.viewScroll.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(1f)
                .setPivotX(Pivot.X.LEFT) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER) // CENTER is a default one
                .build());
        mBinding.viewScroll.fling(0, 0);
        mBinding.dotsIndicator.setVisibility(View.GONE);
        countDownTimer.start();
    }


    private CountDownTimer countDownTimer = new CountDownTimer(3000, 100) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (k >= arrayListHomeBanner.size()) {
                k = 0;
                mBinding.viewScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        // Call smooth scroll
                        mBinding.viewScroll.smoothScrollToPosition(0);
                        mBinding.dotsIndicator.setSelection(0);
                    }
                });
                countDownTimer.start();
            } else {
                mBinding.viewScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        // Call smooth scroll
                        try {
                            mBinding.viewScroll.smoothScrollToPosition(k);
                            mBinding.dotsIndicator.setSelection(k);
                            k = k + 1;
                        } catch (Exception e) {

                        }
                    }
                });
                countDownTimer.start();
            }
        }
    };

@Override
    public void onItemClick(int position, String tag) {
    Bundle bundle1 = new Bundle();
    bundle1.putString(Constant.INPUT_DATA,arrayListHomeBanner.get(position).getInputData());
        switch (arrayListHomeBanner.get(position).getActivity()) {

            case "giftVoucher12111":
                if (arrayListHomeBanner.get(position).getInputData().isEmpty()) {
                    startActivity(new Intent(activity, GiftVoucherActivity.class)
                            .putExtra(Constant.TYPE, "digitalvoucher"));
                } else {

                    parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation), arrayListHomeBanner.get(position).getInputData());
                    //parseHomeUpdateResponse(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE), arrayListHomeBanner.get(position).getInputData());
                }
                break;

            default:

                Utility.openActivity(activity, arrayListHomeBanner.get(position).getActivity(), bundle1);
                break;


        }
    }

    /**
     * Set adapter
     */


    private void parseHomeUpdateResponse(String response, String inputData) {
        try {
            MainRechargeModel model = new Gson().fromJson(response, MainRechargeModel.class);
            if (model.getResCode().equals(Constant.CODE_200) && model.getService() != null) {
                Log.d("HomeUpdateResponse", "response " + response);
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

    private void setAdapter(ArrayList<RechargeHistory> list) {
        if (list == null || list.size() == 0) {
            return;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void init() {

        arrayList = new ArrayList<>();
/*
        if (oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE).isEmpty()) {
            Utility.getHomeUpdate(activity, "", "plus", new HomeUpdateListener() {
                @Override
                public void onDone() {
                    parseHomeUpdateResponse(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE));
                }
            });
        } else {
            parseHomeUpdateResponse(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE));
        }


 */
        long diffHomeUpdates=oxyMePref.getLong(Constant.HOME_UPDATE_API_TIME_MILLISECOND,0);
        long currentTime=System.currentTimeMillis();
        long diffTime=(currentTime-diffHomeUpdates)/1000;
        boolean fromWeb=false;


        if(diffTime>36){
            fromWeb=true;
        }
        String serviceListLocation = "serviceList_HomeUpdates" + Constant.METHOD_HOME_UPDATE;
        if (oxyMePref.getString(serviceListLocation) == null || oxyMePref.getString(serviceListLocation).isEmpty() || fromWeb==true) {
            Utility.getServiceList(activity, "HomeUpdates", Constant.METHOD_HOME_UPDATE, fromWeb,"HomeFragment1", new HomeUpdateListener() {
                @Override
                public void onDone() {

                    parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation));
                    oxyMePref.putLong(Constant.HOME_UPDATE_API_TIME_MILLISECOND, System.currentTimeMillis());

                }
            });
        } else {
            parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation));
        }
        mBinding.viewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                mBinding.viewPager1.reMeasureCurrentPage(mBinding.viewPager1.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        new Thread(new Runnable() {
            public void run() {
                //getContactList();
            }
        }).start();
    }


    /**
     * Set balance
     */
    private void setBalance() {
        // Stop animating the image


    }


    private void setUpViewPager() {
        ViewPagerBalanceAdapter adapter = new ViewPagerBalanceAdapter(getFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mFragments);
        mBinding.viewPager1.setAdapter(adapter);
        mBinding.dotsIndicator1.setViewPager(mBinding.viewPager1);
    }


    private void setFragments(MainRechargeModel model) {
        mFragments = new ArrayList<>();
        mList = new ArrayList<>();
        arrayList1 = new ArrayList<>();
        index = 0;
        Log.d("HomeActivity", "response size " + model.getMainMenu().size());
        if (model.getMainMenu().size() <= itemSize) {
            mFragments.add(new MainRechargeFragment().getInstance(index, (ArrayList<MainRechargeModel.MainMenuBean>) model.getMainMenu(), from));
            mList.add((ArrayList<MainRechargeModel.MainMenuBean>) model.getMainMenu());
            //Log.d("HomeActivity","response 3 ");
            return;
        }

        for (MainRechargeModel.MainMenuBean a : model.getMainMenu()) {
            arrayList1.add(a);
            //Log.d("HomeActivity","response 4 ");
            if (arrayList1.size() == itemSize) {

                addFragment(arrayList1);
            } else if (model.getMainMenu().size() / itemSize == index && arrayList1.size() == model.getMainMenu().size() % itemSize) {
                addFragment(arrayList1);
            }
        }
    }

    private void addFragment(ArrayList<MainRechargeModel.MainMenuBean> arrayList) {
        mFragments.add(new MainRechargeFragment().getInstance(index, arrayList, from));
        mList.add(arrayList);
        this.arrayList1 = new ArrayList<>();
        index++;
    }


}
