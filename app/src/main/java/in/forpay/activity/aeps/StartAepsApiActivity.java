package in.forpay.activity.aeps;


import static in.forpay.util.Utility.getCurrentLocation;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.databinding.ActivityAepsApiBinding;
import in.forpay.fragment.TransactionCommissionFragment;
import in.forpay.fragment.WithdrawFragment;
import in.forpay.fragment.aeps.MiniStatement;
import in.forpay.util.ProgressHelper;

public class StartAepsApiActivity extends AppCompatActivity {


    private Activity activity;

    private ActivityAepsApiBinding binding;
    private ProgressHelper progressHelper;

    private Fragment widrawFrag, miniFrag, commisionFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = StartAepsApiActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_aeps_api);

        init();

    }


    private void init() {
        progressHelper = new ProgressHelper(this);
        setToolbar();
        binding.viewPager.setOffscreenPageLimit(2);
        setupViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
//        postAepsResponseAfterCapture("");

        getCurrentLocation(activity, true);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(widrawFrag = new WithdrawFragment(), "Withdrawl Money", true);
        adapter.addFragment(miniFrag = new MiniStatement(), "Mini Statement", false);
        adapter.addFragment(commisionFrag = new TransactionCommissionFragment(), "Commission", false);

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

    private List<String> selectedPositions = new ArrayList<>();

    private void setToolbar() {
        binding.backBtn.setOnClickListener(v -> onBackPressed());
        binding.offerTitle.setText("AEPS");
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

        void addFragment(Fragment fragment, String title, boolean isWithdraw) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isWithdraw", isWithdraw);
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