package in.forpay.activity.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.FragmentProfile2Binding;
import in.forpay.fragment.ProfilePasswordFragment;
import in.forpay.fragment.ProfilePinFragment;
import in.forpay.fragment.ProfileProfileFragment;
import in.forpay.listener.RechargeNowListener;
import in.forpay.model.response.GetProfileResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ProfileActivity extends AppCompatActivity {

    private FragmentProfile2Binding mBinding;
    private ProgressHelper progressHelper;
    private Activity activity;
    private int viewpagerPosition;

    public static ProfileActivity newInstance() {
        //Log.d("new fra","yes");
        return new ProfileActivity();
    }

    public ProfileActivity() {
        //Log.d("new fra","yes");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ProfileActivity.this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_profile2);


        viewpagerPosition = getIntent().getIntExtra(Constant.POSITION, 0);
        //Log.d("new fra","yes");
        init();

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("new fra","yes");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(activity, mBinding.appbarLayout);
    }

    private void init() {
        //Log.d("new fra","yes");

        progressHelper = new ProgressHelper(activity);

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this, map1);

        mBinding.backBtn.setOnClickListener(v -> onBackPressed());

        getProfileDetails(request);

        //setupViewPager(mBinding.viewPager);
        //mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        mBinding.viewPager.setCurrentItem(viewpagerPosition);

        mBinding.profile.setOnClickListener(view -> {
            mBinding.viewPager.setCurrentItem(0);
            profileSelect();
        });

        mBinding.pin.setOnClickListener(view -> {
            mBinding.viewPager.setCurrentItem(1);
            pinSelect();
        });

        mBinding.password.setOnClickListener(view -> {
            mBinding.viewPager.setCurrentItem(2);
            passwordSelect();
        });

        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                switch (position) {
                    case 0:
                        profileSelect();
                        break;
                    case 1:
                        pinSelect();
                        break;
                    case 2:
                        passwordSelect();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
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

    private void profileSelect() {
        mBinding.profileTriangle.setVisibility(View.VISIBLE);
        mBinding.pinTriangle.setVisibility(View.INVISIBLE);
        mBinding.passwordTriangle.setVisibility(View.INVISIBLE);

        setColor(mBinding.profile, R.color.orange_new);
        setColor(mBinding.profileTriangle, R.color.orange_new);

        setColor(mBinding.pin, R.color.green_new);
        setColor(mBinding.pinTriangle, R.color.green_new);

        setColor(mBinding.password, R.color.green_new);
        setColor(mBinding.passwordTriangle, R.color.green_new);
    }

    private void pinSelect() {
        mBinding.profileTriangle.setVisibility(View.INVISIBLE);
        mBinding.pinTriangle.setVisibility(View.VISIBLE);
        mBinding.passwordTriangle.setVisibility(View.INVISIBLE);


        setColor(mBinding.profile, R.color.green_new);
        setColor(mBinding.profileTriangle, R.color.green_new);

        setColor(mBinding.pin, R.color.orange_new);
        setColor(mBinding.pinTriangle, R.color.orange_new);

        setColor(mBinding.password, R.color.green_new);
        setColor(mBinding.passwordTriangle, R.color.green_new);
    }

    private void passwordSelect() {
        mBinding.profileTriangle.setVisibility(View.INVISIBLE);
        mBinding.pinTriangle.setVisibility(View.INVISIBLE);
        mBinding.passwordTriangle.setVisibility(View.VISIBLE);

        setColor(mBinding.profile, R.color.green_new);
        setColor(mBinding.profileTriangle, R.color.green_new);

        setColor(mBinding.pin, R.color.green_new);
        setColor(mBinding.pinTriangle, R.color.green_new);

        setColor(mBinding.password, R.color.orange_new);
        setColor(mBinding.passwordTriangle, R.color.orange_new);
    }

    private void setColor(View view, int color) {
        view.getBackground().setColorFilter(getResources().getColor(color), PorterDuff.Mode.SRC_ATOP);
        view.getBackground().setColorFilter(getResources().getColor(color), PorterDuff.Mode.SRC_ATOP);
    }

    private void getProfileDetails(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_PROFILE, request, new CallbackListener() {
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


    private void parseResponse(String result) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetProfileResponse response = new Gson().fromJson(result, GetProfileResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {

                Utility.setCustomerEmail(activity, response.getData().getEmail());
                Utility.setCustomerName(activity, response.getData().getName());
                Utility.setCustomerMobile(activity, response.getData().getMobile());
                Utility.setCustomerPan(activity, response.getData().getPan());
                Utility.setCustomerPincode(activity, response.getData().getPincode());
                Utility.setCustomerRefferUrl(activity, response.getData().getReferralUrl());
                Utility.setCustomerReferCount(activity, response.getData().getReferCount());
                Utility.setCustomerHomeLandmark(activity, response.getData().getHomeLandmark());


                Utility.setBalance(activity, response.getData().getBal());
                Utility.setCommissionBalance(activity, response.getData().getCommissionBal());

                setupViewPager(mBinding.viewPager);
            } else {
                Utility.showToast(activity, response.getResText(), response.getResCode());
                //mBinding.updateProfileTextView.setClickable(false);

            }
        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(this, ServerNotResponseActivity.class));
            //mBinding.updateProfileTextView.setClickable(false);

        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        adapter.addFragment(new ProfileProfileFragment(new RechargeNowListener() {
            @Override
            public void onDone() {

            }
        }), "Profile");

        adapter.addFragment(new ProfilePinFragment(new RechargeNowListener() {
            @Override
            public void onDone() {

            }
        }), "Pin");

        adapter.addFragment(new ProfilePasswordFragment(new RechargeNowListener() {
            @Override
            public void onDone() {

            }
        }), "Password");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
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
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
