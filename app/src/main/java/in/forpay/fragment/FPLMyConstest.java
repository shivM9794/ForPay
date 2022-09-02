package in.forpay.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.MatchRulesAdapter;
import in.forpay.adapter.MyContestsAdapter;
import in.forpay.databinding.FragmentFPLMycontestsBinding;
import in.forpay.databinding.FragmentFPLRulesBinding;
import in.forpay.model.response.GetFPLMyContestResponse;
import in.forpay.model.response.GetFPLResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class FPLMyConstest extends BottomSheetDialogFragment {

    private FragmentFPLMycontestsBinding binding;
    ProgressHelper progressHelper;
    private List<GetFPLMyContestResponse.DataBean> dataList = new ArrayList<>();
    private MyContestsAdapter matchRulesAdapter;
    Activity activity;
    private String result = "";

    public FPLMyConstest(Activity activity, String result) {
        this.activity = activity;
        this.result = result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_f_p_l_mycontests, container, false);
        progressHelper = new ProgressHelper(getActivity());
        activity = getActivity();
        parseRulesResponse(result);
        return binding.getRoot();
    }


    private void parseRulesResponse(String result) {
        Log.d("......... ", "parseRulesResponse: "+result);
        GetFPLMyContestResponse response = new Gson().fromJson(result, GetFPLMyContestResponse.class);
        if (response.getResCode().equals(Constant.CODE_200)) {
            dataList = response.getData();
            matchRulesAdapter();
        } else {
            dismiss();
            Utility.showToastLatest(getActivity(), response.getResText(), response.getResCode());

        }
    }

    private void matchRulesAdapter() {

        binding.rcvmycontests.setLayoutManager(new LinearLayoutManager(getActivity()));
        matchRulesAdapter = new MyContestsAdapter(getActivity(), dataList);
        binding.rcvmycontests.setAdapter(matchRulesAdapter);
    }


}