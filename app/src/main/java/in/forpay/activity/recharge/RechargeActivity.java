package in.forpay.activity.recharge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.FragmentRechargeBinding;
import in.forpay.fragment.recharge.DTHFragment;
import in.forpay.fragment.recharge.MetroCardFragment;
import in.forpay.fragment.recharge.MobileFragment;
import in.forpay.fragment.recharge.PostpaidFragment;
import in.forpay.listener.RechargeNowListener;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class RechargeActivity extends AppCompatActivity {

    private FragmentRechargeBinding mBinding;

    private ProgressHelper progressHelper;
    private Activity activity;
    private String tabCheck;
    private DatabaseHelper databaseHelper;

    public static RechargeActivity newInstance() {
        return new RechargeActivity();
    }

    public RechargeActivity() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = RechargeActivity.this;
        mBinding = DataBindingUtil.setContentView(activity, R.layout.fragment_recharge);
        Constant.OFFER_DATA_PLAN1 = "";
        Constant.OFFER_DATA_PLAN2 = "";
        if (activity != null) {

            Utility.setAppLocale(Utility.getDefaultLanguage(activity), activity);

        }
        progressHelper = new ProgressHelper(activity);
        init();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void init() {
        tabCheck = getIntent().getStringExtra("tabItem");
        assert tabCheck != null;

        setupViewPager(mBinding.viewPager);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        if (tabCheck.equals("metroData")) {
            mBinding.viewPager.setCurrentItem(3);
        }

        mBinding.backBtn.setOnClickListener(v -> onBackPressed());

    }


    /**
     * Set view pager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new MobileFragment(new RechargeNowListener() {
            @Override
            public void onDone() {
                //Log.d("Chatlist","Now visible");
            }
        }), getString(R.string.title_mobile));

        adapter.addFragment(new DTHFragment(new RechargeNowListener() {
            @Override
            public void onDone() {

            }
        }), getString(R.string.title_dth));

        adapter.addFragment(new PostpaidFragment(new RechargeNowListener() {
            @Override
            public void onDone() {

            }
        }), getString(R.string.title_postpaid));
        if (!Utility.getIsMetroRechargeDisabled(activity).equals("1")) {
            adapter.addFragment(new MetroCardFragment(new RechargeNowListener() {
                @Override
                public void onDone() {

                }
            }), getString(R.string.title_metro));
        }
        viewPager.setAdapter(adapter);



        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            if(title.equals("MOBILE")) {
                Log.d("Chatlist", "Mobile ");
               // progressHelper.show();
            }
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

            View fragmentView = fragment.getView();


            final int[] runLoop={0};
            //boolean runLoop = true;
            if(title.equals("MOBILE")) {
                Log.d("Chatlist", "Mobile Check ");
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        if(runLoop[0]==0) {

                            handler.postDelayed(this, 100);
                            View fragmentView = fragment.getView();


                            if (fragmentView != null && fragmentView.getGlobalVisibleRect(new Rect())) {
                                Log.d("Chatlist", "Now visible " + runLoop[0]);
                                runLoop[0]=1;
                               // progressHelper.dismiss();
                                // fragment is visible
                            } else {
                                Log.d("Chatlist", "Now Not visible " + runLoop[0]);
                                runLoop[0]=0;
                                // fragment is not visible
                            }

                        }
                    }
                };
                handler.post(runnable);
                //handler.postDelayed(runnable, 100);
            }


        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(activity, mBinding.rechargeLayout);
    }


}


