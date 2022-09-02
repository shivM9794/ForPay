package in.forpay.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.MatchRulesAdapter;
import in.forpay.databinding.FragmentFPLRulesBinding;
import in.forpay.model.response.GetAddressResponse;
import in.forpay.model.response.GetFPLResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class FPLRules extends BottomSheetDialogFragment {

    private FragmentFPLRulesBinding binding;
    ProgressHelper progressHelper;
    private ArrayList<GetFPLResponse.Rules> dataList = new ArrayList<>();
    private MatchRulesAdapter matchRulesAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_f_p_l_rules, container, false);
        progressHelper = new ProgressHelper(getActivity());

        init(false);
        return binding.getRoot();


    }

    private void init(Boolean showProgressbar) {

        if (showProgressbar) {
            progressHelper.show();
        }
        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(getActivity())); // key
        map1.put("imei", Utility.getImei(getActivity())); // imei
        map1.put("versionCode", Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));

        String request = Utility.mapWrapper(getActivity(), map1);
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_FORPAY_PREMIER_LEAGUE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            progressHelper.dismiss();
                            parseRulesResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect), "");
        }
    }

    private void parseRulesResponse(String result) {

        GetFPLResponse response = new Gson().fromJson(result, GetFPLResponse.class);
        if (response.getResCode().equals(Constant.CODE_200)) {


        } else {

            Utility.showToastLatest(getActivity(), response.getResText(), response.getResCode());

        }

        dataList = response.getRules();
        matchRulesAdapter();
    }

    private void matchRulesAdapter() {

        binding.contestRulesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        matchRulesAdapter = new MatchRulesAdapter(getActivity(), dataList);
        binding.contestRulesRecycler.setAdapter(matchRulesAdapter);
    }


}