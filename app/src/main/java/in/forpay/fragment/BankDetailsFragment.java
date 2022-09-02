package in.forpay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.adapter.BankDetailsAdapter;
import in.forpay.databinding.FragmentBankDetailsBinding;
import in.forpay.model.response.GetBankDetailsResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class BankDetailsFragment extends Fragment {

    private FragmentBankDetailsBinding mBinding;
    private ProgressHelper progressHelper;

    public static BankDetailsFragment newInstance() {
        return new BankDetailsFragment();
    }

    public BankDetailsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bank_details, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        progressHelper = new ProgressHelper(getActivity());
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(getActivity())); // key
        map1.put("imei",Utility.getImei(getActivity())); // imei
        map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code

        String request = Utility.mapWrapper(getActivity(),map1);

        getBankDetails(request);
    }

    /**
     * Set adapter
     */
    private void setAdapter(ArrayList<GetBankDetailsResponse.Data> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        mBinding.recyclerView.setAdapter(new BankDetailsAdapter(getActivity(),list));
    }

    /**
     * Get bank details
     */
    private void getBankDetails(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_GET_BANK_DETAILS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect));
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
        if (isVisible()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetBankDetailsResponse response = new Gson().fromJson(result, GetBankDetailsResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    setAdapter(response.getDataList());
                } else {
                    Utility.showToast(getActivity(), response.getResText(),response.getResCode());
                }
            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding),"");
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
            }
        }
    }
}
