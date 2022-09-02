package in.forpay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.FragmentMissCallBinding;
import in.forpay.model.response.GetMissCallRecharge;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class Miss_CallFragment extends BottomSheetDialogFragment {

    private FragmentMissCallBinding binding;
    ProgressHelper progressHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_miss__call, container, false);

        binding.closeBtn.setOnClickListener(v -> dismiss());

        binding.btnRecharge.setOnClickListener(v -> recharge());
        return binding.getRoot();
    }

    private void recharge() {

        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(getActivity())); // key
        map1.put("imei", Utility.getImei(getActivity())); // imei
        map1.put("versionCode", Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));
        // map1.put("missCallNumber", binding.misscallNumber.getText().toString());

        String request = Utility.mapWrapper(getActivity(), map1);

        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_MISS_CALL_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse1(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect), "");
        }
    }

    private void parseResponse1(String result) {

        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetMissCallRecharge response = new Gson().fromJson(result, GetMissCallRecharge.class);

            if (response.getResCode().equals(Constant.CODE_200)) {


            } else {

            }
        }
    }
}