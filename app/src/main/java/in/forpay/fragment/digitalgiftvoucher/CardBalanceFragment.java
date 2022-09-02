package in.forpay.fragment.digitalgiftvoucher;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.FragmentCardBalanceBinding;
import in.forpay.model.response.GetCardBalanceResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class CardBalanceFragment extends Fragment {

    private ProgressHelper progressHelper;

    private FragmentCardBalanceBinding mBinding;

    public static CardBalanceFragment newInstance() {
        return new CardBalanceFragment();
    }

    public CardBalanceFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_balance, container, false);
        mBinding.setFragment(this);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    /**
     * Click on select balance button
     */
    public void onClickCheckBalance() {
        if (validation()) {

            Map<String,String> map1 = new HashMap<>();

            map1.put("token",Utility.getToken(getActivity())); // key
            map1.put("imei",Utility.getImei(getActivity())); // imei
            map1.put("versionCode" , Utility.getVersionCode(getActivity())); // version code
            map1.put("os", Utility.getOs(getActivity()));
            map1.put("cardNumber",mBinding.editTextCardNumber.getText().toString().trim()); // cardNumber
            map1.put("cardPin",mBinding.editTextCardPin.getText().toString().trim()); // cardPin



            String request = Utility.mapWrapper(getActivity(),map1);




            checkBalance(request);
        }
    }

    private void init() {
        progressHelper = new ProgressHelper(getActivity());
    }

    /**
     * Validation for all fields
     */
    private boolean validation() {
        String number = mBinding.editTextCardNumber.getText().toString().trim();
        String pin = mBinding.editTextCardPin.getText().toString().trim();

        if (TextUtils.isEmpty(number)) {
            Utility.showToast(getActivity(), "Please enter card number","");
            return false;
        } else if (TextUtils.isEmpty(pin)) {
            Utility.showToast(getActivity(), "Please enter card pin","");
            return false;
        }
        return true;
    }

    /**
     * Check balance
     */
    private void checkBalance(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_CARD_BALANCE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
        if (isVisible()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetCardBalanceResponse response = new Gson().fromJson(result, GetCardBalanceResponse.class);
                if (response.getData().getResCode().equals(Constant.CODE_200)) {
                    Utility.showToast(getActivity(), response.getData().getResText(),response.getData().getResCode());
                    String balance = "Your card balance is: " +
                            getString(R.string.rupee) + response.getData().getCardBal();
                    mBinding.textViewCardBalance.setText(balance);
                } else {
                    Utility.showToast(getActivity(), response.getData().getResText(),response.getData().getResCode());
                }
            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding),"");
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
            }
        }
    }
}
