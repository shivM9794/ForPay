package in.forpay.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import in.forpay.R;
import in.forpay.activity.BalanceHistoryActivity;
import in.forpay.activity.BankDetailsActivity;
import in.forpay.activity.FundRequestActivity;
import in.forpay.activity.profile.ProfileActivity;
import in.forpay.activity.recharge.RechargeHistoryActivity;
import in.forpay.databinding.FragmentProfile3Binding;
import in.forpay.util.Utility;


public class ProfileFragment3 extends Fragment {

    private FragmentProfile3Binding binding;
    private Activity activity;

    public ProfileFragment3() {
        // Required empty public constructor
    }


    public static ProfileFragment3 newInstance() {
        return  new ProfileFragment3();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile3, container, false);



        binding.btnRechargeHistory.setOnClickListener(v -> startActivity(new Intent(activity, RechargeHistoryActivity.class)));
        binding.btnBalanceHistory.setOnClickListener(v -> startActivity(new Intent(activity, BalanceHistoryActivity.class)));
        binding.btnDetailOfBank.setOnClickListener(v -> startActivity(new Intent(activity, BankDetailsActivity.class)));
        binding.btnFundRequest.setOnClickListener(v -> startActivity(new Intent(activity, FundRequestActivity.class)));
        binding.btnEditProfile.setOnClickListener(v -> startActivity(new Intent(activity, ProfileActivity.class)));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        String customer_name = Utility.getCustomerName(activity);

        if(!customer_name.isEmpty()){
            binding.userName.setText(customer_name);
        }
        binding.userNumber.setText(Utility.getUsername(activity));
    }
}
