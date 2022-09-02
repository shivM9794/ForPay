package in.forpay.fragment.recharge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.forpay.R;
import in.forpay.databinding.FragmentRechargeBinding;
import in.forpay.listener.Listener;
import in.forpay.listener.RechargeNowListener;
import in.forpay.model.QuickMenu;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class RechargeFragment extends Fragment {

    private FragmentRechargeBinding mBinding;

    private ProgressHelper progressHelper;

    public static RechargeFragment newInstance() {
        return new RechargeFragment();
    }

    public RechargeFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recharge, container, false);
        //mBinding.setFragment(this);
        if(getActivity()!=null) {

            Utility.setAppLocale(Utility.getDefaultLanguage(getActivity()),getActivity());

        }
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Click on refresh icon
     */


    /*public void onClickTranfer(){

        // key||imei||version||token||mobile||acountNumber||ifscCode||pin
        ArrayList<String> list = new ArrayList<>();
        list.add("key="+Utility.getDeviceActiveKey(getActivity())); // key
        list.add("imei="+Utility.getDeviceIMEI(getActivity())); // imei
        list.add("versionCode="+Utility.getVersionCode(getActivity())); // version
        list.add("token="+Utility.getToken(getActivity())); // token


        String request = Utility.dataWrapper(list);
        agreePopUp(request);

    }*/








    private void init() {
        initRecyclerView();
        setupViewPager(mBinding.viewPager);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        progressHelper = new ProgressHelper(getActivity());
    }

    /**
     * Set balance
     */


    /**
     * Init recycler view for quick menu
     */
    private void initRecyclerView() {
        /*mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        ArrayList<QuickMenu> list = new ArrayList<>();
        list.add(new QuickMenu(R.drawable.ic_phone, getString(R.string.icon_title_recharge)));
        list.add(new QuickMenu(R.drawable.ic_account, getString(R.string.icon_title_moneytransfer)));
        list.add(new QuickMenu(R.drawable.ic_payment, getString(R.string.icon_title_bill_payment)));
        list.add(new QuickMenu(R.drawable.ic_recharge_history, getString(R.string.icon_title_recharge_history)));
        list.add(new QuickMenu(R.drawable.ic_payment, getString(R.string.icon_title_balance_history)));

        mBinding.recyclerView.setAdapter(new QuickMenuAdapter(getActivity(), list));*/
    }

    /**
     * Set view pager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new MobileFragment(new RechargeNowListener() {
            @Override
            public void onDone() {

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
        if(!Utility.getIsMetroRechargeDisabled(getActivity()).equals("1")) {
            adapter.addFragment(new MetroCardFragment(new RechargeNowListener() {
                @Override
                public void onDone() {

                }
            }), getString(R.string.title_metro));
        }
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
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.quick_menu_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
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
                          //  ((HomeActivity) mActivity).onClickFromRecharge(getAdapterPosition(),1);
                        }
                    }
                });
            }
        }
    }
}


