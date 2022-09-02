package in.forpay.activity.aeps;

import static in.forpay.util.Utility.getCurrentLocation;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

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
import in.forpay.databinding.ActivityStartAepsNewBinding;
import in.forpay.fragment.TransactionCommissionFragment;
import in.forpay.fragment.WithdrawFragment;
import in.forpay.fragment.aeps.MiniStatement;
import in.forpay.util.ProgressHelper;

public class StartAepsNewActivity extends AppCompatActivity {

    private ActivityStartAepsNewBinding binding;
    private Activity activity;
    private ProgressHelper progressHelper;

    private Fragment widrawFrag, miniFrag, commisionFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_aeps_new);
        activity = this;

        init();

    }

    private void init() {
        progressHelper = new ProgressHelper(this);
        setToolbar();
        binding.viewPager.setOffscreenPageLimit(2);
        setupViewPager(binding.viewPager);
//        binding.tabLayout.setCurrentItem(binding.viewPager);
//        postAepsResponseAfterCapture("");

        getCurrentLocation(activity, true);

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


    private void setupViewPager(ViewPager viewPager) {
        StartAepsNewActivity.ViewPagerAdapter adapter = new StartAepsNewActivity.ViewPagerAdapter(getSupportFragmentManager());
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