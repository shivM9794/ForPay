package in.forpay.activity.newaeps;

import static in.forpay.util.Utility.getCurrentLocation;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityStartNewAepsApiBinding;
import in.forpay.fragment.TransactionCommissionFragment;
import in.forpay.fragment.WithdrawFragment;
import in.forpay.fragment.aeps.MiniStatement;
import in.forpay.listener.RechargeNowListener;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class StartNewAepsApiActivity extends AppCompatActivity {


    private ActivityStartNewAepsApiBinding binding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private int viewpagerPosition;
    private Fragment widrawFrag, miniFrag, commisionFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_new_aeps_api);
        activity = this;
        viewpagerPosition = getIntent().getIntExtra(Constant.POSITION, 0);

        init();

    }

    private void init() {

        progressHelper = new ProgressHelper(this);
        setupViewPager(binding.viewPager);
        binding.viewPager.setOffscreenPageLimit(2);
        setToolbar();
        getCurrentLocation(activity, true);
        binding.viewPager.setCurrentItem(viewpagerPosition);



        binding.profile.setOnClickListener(view -> {
            binding.viewPager.setCurrentItem(0);
            withdrawalSelect();
        });

        binding.pin.setOnClickListener(view -> {
            binding.viewPager.setCurrentItem(1);
            ministatement();
        });

        binding.password.setOnClickListener(view -> {
            binding.viewPager.setCurrentItem(2);
            commission();
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                switch (position) {
                    case 0:
                        withdrawalSelect();
                        break;
                    case 1:
                        ministatement();
                        break;
                    case 2:
                        commission();
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



    @Override
    public void finish() {
        super.finish();
    }

    private void withdrawalSelect() {
        binding.profileTriangle.setVisibility(View.VISIBLE);
        binding.pinTriangle.setVisibility(View.INVISIBLE);
        binding.passwordTriangle.setVisibility(View.INVISIBLE);

        setColor(binding.profile, R.color.orange_new);
        setColor(binding.profileTriangle, R.color.orange_new);

        setColor(binding.pin, R.color.green_new);
        setColor(binding.pinTriangle, R.color.green_new);

        setColor(binding.password, R.color.green_new);
        setColor(binding.passwordTriangle, R.color.green_new);
    }

    private void ministatement() {
        binding.profileTriangle.setVisibility(View.INVISIBLE);
        binding.pinTriangle.setVisibility(View.VISIBLE);
        binding.passwordTriangle.setVisibility(View.INVISIBLE);


        setColor(binding.profile, R.color.green_new);
        setColor(binding.profileTriangle, R.color.green_new);

        setColor(binding.pin, R.color.orange_new);
        setColor(binding.pinTriangle, R.color.orange_new);

        setColor(binding.password, R.color.green_new);
        setColor(binding.passwordTriangle, R.color.green_new);
    }

    private void commission() {
        binding.profileTriangle.setVisibility(View.INVISIBLE);
        binding.pinTriangle.setVisibility(View.INVISIBLE);
        binding.passwordTriangle.setVisibility(View.VISIBLE);
        setColor(binding.profile, R.color.green_new);
        setColor(binding.profileTriangle, R.color.green_new);
        setColor(binding.pin, R.color.green_new);
        setColor(binding.pinTriangle, R.color.green_new);
        setColor(binding.password, R.color.orange_new);
        setColor(binding.passwordTriangle, R.color.orange_new);
    }

    private void setColor(View view, int color) {
        view.getBackground().setColorFilter(getResources().getColor(color), PorterDuff.Mode.SRC_ATOP);
        view.getBackground().setColorFilter(getResources().getColor(color), PorterDuff.Mode.SRC_ATOP);
    }

    private List<String> selectedPositions = new ArrayList<>();

    private void setToolbar() {

        binding.backBtn.setOnClickListener(v -> onBackPressed());
        binding.offerTitle.setText("AEPS");
    }

    private void setupViewPager(ViewPager viewPager) {
        StartNewAepsApiActivity.ViewPagerAdapter adapter = new StartNewAepsApiActivity.ViewPagerAdapter(getSupportFragmentManager(), 0);

        adapter.addFragment(new WithdrawFragment(new RechargeNowListener() {
            @Override
            public void onDone() {

            }
        }), "WithdrawFragment");

        adapter.addFragment(new MiniStatement(new RechargeNowListener() {
            @Override
            public void onDone() {

            }
        }), "MiniStatement");

        adapter.addFragment(new TransactionCommissionFragment(new RechargeNowListener() {
            @Override
            public void onDone() {

            }
        }), "TransactionCommissionFragment");

        viewPager.setAdapter(adapter);
    }


    public static void showMsgDialog(Context context, String messageTxt, String argTitle) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(argTitle)
                .setMessage(messageTxt)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
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
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}