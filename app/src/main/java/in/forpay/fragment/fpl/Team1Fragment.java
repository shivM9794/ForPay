package in.forpay.fragment.fpl;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.Team1Adapter;
import in.forpay.databinding.FragmentTeam1Binding;
import in.forpay.model.response.GetFPLResponse;
import in.forpay.model.response.GetMatchDetailsResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class Team1Fragment extends Fragment {

    private FragmentTeam1Binding binding;
    ProgressHelper progressHelper;
    private ArrayList<GetMatchDetailsResponse.Data> dataList = new ArrayList<>();
    private Team1Adapter team1Adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_team1,container,false);
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
                    Constant.METHOD_MATCH_DETAILS, request, new CallbackListener() {
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

        GetMatchDetailsResponse response = new Gson().fromJson(result, GetMatchDetailsResponse.class);
        if (response.getResCode().equals(Constant.CODE_200)) {


        } else {

            Utility.showToastLatest(getActivity(), response.getResText(), response.getResCode());

        }

        dataList = response.getTeamPlayers();
        team1Adapter();
    }

    private void team1Adapter() {

        binding.recyclerTeam1.setLayoutManager(new LinearLayoutManager(getActivity()));
        team1Adapter = new Team1Adapter(getActivity(),dataList);
        binding.recyclerTeam1.setAdapter(team1Adapter);
    }
}