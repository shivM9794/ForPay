package in.forpay.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBindings;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.forpaypremierleague.FPLMatchDetailsActivity;
import in.forpay.adapter.MatchRulesAdapter;
import in.forpay.databinding.FragmentFPLCreategroupBinding;
import in.forpay.databinding.FragmentFPLRulesBinding;
import in.forpay.model.response.GetFPLCreateGroup;
import in.forpay.model.response.GetFPLResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Util;
import in.forpay.util.Utility;


public class FPLCreateGroup extends BottomSheetDialogFragment {

    private FragmentFPLCreategroupBinding binding;
    ProgressHelper progressHelper;
    Activity activity;
    String gname = "",  point = "10", gtype = "";
    String matchId = "";

    public FPLCreateGroup(String str) {
        matchId = str;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_f_p_l_creategroup, container, false);
        progressHelper = new ProgressHelper(getActivity());
        activity = getActivity();
        init();

        return binding.getRoot();


    }

    private void init() {
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonprivate:
                        gtype = "1";
                        break;
                    case R.id.radioButtonpublic:
                        gtype = "0";
                        break;

                }

            }
        });
        binding.subbtn.setOnClickListener(v ->

        {


            //point = binding.edtpoints.getText().toString();
            gname = binding.edtgname.getText().toString();

            if (point.isEmpty() || gname.isEmpty() || gtype.isEmpty())
                Toast.makeText(activity, "All fields are required", Toast.LENGTH_SHORT).show();
            else
                callCreateApi(true);
        });


        binding.rgNoOfPoint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                //Log.d("jsdfjgsjss", "Checked " + radioButton.getText());
                String text = radioButton.getText().toString();
                point = text;

                //setColor();

            }
        });
    }


    private void setColor() {

        if (binding.tv10.isChecked()) {
            binding.tv10.setTextColor(activity.getResources().getColor(R.color.orange_new));
        } else {
            binding.tv10.setTextColor(activity.getResources().getColor(R.color.comfort_default));
        }
    }
    private void parseRulesResponse(String result) {

        GetFPLCreateGroup response = new Gson().fromJson(result, GetFPLCreateGroup.class);
        if (response.getResCode().equals(Constant.CODE_200)) {



        } else {

            Utility.showToastLatest(getActivity(), response.getResText(), response.getResCode());

        }

    }

    private void callCreateApi(Boolean showProgressbar) {

        if (showProgressbar) {
            progressHelper.show();

        }

        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("latitude", Utility.getLatitude(activity));
        map1.put("longitude", Utility.getLongitude(activity));
        map1.put("matchId", matchId);
        map1.put("groupName", gname);
        map1.put("groupType", gtype);
        map1.put("points", point);

        String request = Utility.mapWrapper(activity, map1);

        if (Utility.isNetworkConnected(activity)) {
            QueryManager.getInstance().postRequest(activity, Constant.METHOD_CREATE_GROUP, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {

                    if (showProgressbar) {
                        progressHelper.dismiss();
                    }
                    parseCashbackResponseData(result);
                }
            });
        }
    }

    private void parseCashbackResponseData(String result) {

        GetFPLCreateGroup response = new Gson().fromJson(result, GetFPLCreateGroup.class);
        if (response.getResCode().equals(Constant.CODE_200)) {
            //Utility.showToastLatest(activity, response.getResText(), response.getResCode());
//            data = response.getData();
//            runningMatchAdapter();

            startActivity(new Intent(activity, FPLMatchDetailsActivity.class).putExtra("matchAllModel","")
                    .putExtra("groupId",response.getGroupId()));

            activity.finish();
        } else {
            Utility.showToastLatest(activity, response.getResText(), response.getResCode());

        }


    }


}