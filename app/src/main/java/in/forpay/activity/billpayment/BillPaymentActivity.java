package in.forpay.activity.billpayment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.activity.AddfundActivity;
import in.forpay.databinding.ActivityBillPaymentBinding;
import in.forpay.fragment.billpayment.ElectricityFragment;
import in.forpay.fragment.billpayment.GasFragment;
import in.forpay.fragment.billpayment.InsuranceFragment;
import in.forpay.fragment.billpayment.LandLineFragment;
import in.forpay.fragment.billpayment.WaterFragment;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.listener.Listener;
import in.forpay.listener.RechargeNowListener;
import in.forpay.model.QuickMenu;
import in.forpay.model.response.GetBalanceResponse;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.Utility;

public class BillPaymentActivity extends AppCompatActivity {

    private ActivityBillPaymentBinding mBinding;
    private Activity activity;

    private OxyMePref oxyMePref;

    String serviceListLocation = "serviceList_Balance" + Constant.METHOD_REFRESH_BALANCE;
    public static BillPaymentActivity newInstance() {
        return new BillPaymentActivity();
    }

    public BillPaymentActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = BillPaymentActivity.this;
        oxyMePref = new OxyMePref(activity);

        if(activity!=null) {

            Utility.setAppLocale(Utility.getDefaultLanguage(activity),activity);

        }
        mBinding = DataBindingUtil.setContentView(activity, R.layout.activity_bill_payment);
        init();
    }



    @Override
    public void onResume() {
        super.onResume();
        setBalance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(activity,mBinding.billPaymentLayout);
    }

    /**
     * Click on refresh icon
     */
    public void onClickRefresh() {
        Utility.refreshBalance(activity, mBinding.imageViewRefresh, new Listener() {
            @Override
            public void onRefreshBalance() {
                setBalance();
            }
        });
    }

    private void init() {
        setupViewPager(mBinding.viewPager);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
        mBinding.imageViewRefresh.setOnClickListener(v -> onClickRefresh());
    }



    /**
     * Set balance
     */
    private void setBalance() {
        // Stop animating the image
        mBinding.imageViewRefresh.setAnimation(null);


        Utility.getServiceList(activity, "Balance", Constant.METHOD_REFRESH_BALANCE, true,"BillPayment", new HomeUpdateListener() {
            @Override
            public void onDone() {
                GetBalanceResponse response = new Gson().fromJson(oxyMePref.getString(serviceListLocation), GetBalanceResponse.class);
                if(response!=null) {
                    try{

                        mBinding.textViewBalance.setText("₹ " + response.getData().getBal());

                        String b = getString(R.string.rupee) + " " + response.getData().getBal();
                        mBinding.textViewBalance.setText(b);
                        String cb = getString(R.string.rupee) + " " + response.getData().getCommissionBal();
                        mBinding.textViewCommissionBalance.setText(cb);
                    }
                    catch (Exception e){
                        Utility.showToastLatest(activity,e.toString(),"ERROR");

                    }
                }
                else{
                    Utility.showToastLatest(activity,"Server not responding","ERROR");
                }

            }
        });


    }

    /**
     * Set view pager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ElectricityFragment(new RechargeNowListener() {
            @Override
            public void onDone() {
                setBalance();
            }
        }), getString(R.string.title_electricty));

        adapter.addFragment(new GasFragment(new RechargeNowListener() {
            @Override
            public void onDone() {
                setBalance();
            }
        }), getString(R.string.title_gas));

        adapter.addFragment(new WaterFragment(new RechargeNowListener() {
            @Override
            public void onDone() {
                setBalance();
            }
        }), getString(R.string.title_water));

        adapter.addFragment(new InsuranceFragment(new RechargeNowListener() {
            @Override
            public void onDone() {
                setBalance();
            }
        }), getString(R.string.title_insurance));

        adapter.addFragment(new LandLineFragment(new RechargeNowListener() {
            @Override
            public void onDone() {
                setBalance();
            }
        }), getString(R.string.title_landline));

        viewPager.setAdapter(adapter);
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
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    class QuickMenuAdapter extends RecyclerView.Adapter<QuickMenuAdapter.ViewHolder> {

        private Activity mActivity;
        private ArrayList<QuickMenu> mList = new ArrayList<>();

        QuickMenuAdapter(Activity activity, ArrayList<QuickMenu> list) {
            this.mActivity = activity;
            this.mList = list;
        }

        @NonNull
        @Override
        public QuickMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.quick_menu_item, viewGroup, false);
            return new QuickMenuAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull QuickMenuAdapter.ViewHolder viewHolder, int position) {
            viewHolder.imageView.setImageResource(mList.get(position).getImage());
            viewHolder.textView.setText(mList.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageView;
            private TextView textView;

            ViewHolder(@NonNull View view) {
                super(view);
                imageView = view.findViewById(R.id.imageView);
                textView = view.findViewById(R.id.textView);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mActivity != null) {
                            startActivity(new Intent(activity,BillPaymentActivity.class));
                            //((HomeActivity) mActivity).onClickFromRecharge(getAdapterPosition(), 2);
                        }
                    }
                });
            }
        }
    }

}
