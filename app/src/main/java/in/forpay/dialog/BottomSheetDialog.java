package in.forpay.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityBottomSheetDialogBinding;
import in.forpay.model.response.GetAddPayoutResponse;
import in.forpay.model.response.GetPayoutBankDetails;
import in.forpay.model.response.GetPayoutBankVerify;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class BottomSheetDialog extends BottomSheetDialogFragment {


    ActivityBottomSheetDialogBinding binding;
    ProgressHelper progressHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_bottom_sheet_dialog, container, false);

        progressHelper = new ProgressHelper(getActivity());


        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initReq();
            }
        });

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();


            }
        });


        binding.editTextIfscCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 11){
                    if(before==0 && count==1){
                        veriyfyName();
                    }
                    else{

                    }

                }

            }
        });


        return binding.getRoot();
    }

    private void veriyfyName(){
        Log.e("cal", "true");
        progressHelper.show();

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(getActivity())); // key
        map1.put("imei", Utility.getImei(getActivity())); // imei
        map1.put("versionCode", Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));
        map1.put("ifscCode",binding.editTextIfscCode.getText().toString());
        map1.put("accountNumber",binding.editTextAccountNumber.getText().toString());


        String request = Utility.mapWrapper(getActivity(), map1);


        if (in.forpay.util.Utility.isNetworkConnected(getContext())) {

            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_PAYOUT_BANK_VERIFY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            progressHelper.dismiss();
                            parseResponseVerification(result);


                        }
                    });
        } else {
            in.forpay.util.Utility.showToast(getContext(), getString(R.string.internet_connect), "");
        }


    }
    private void initReq() {

        Log.e("cal", "true");
        progressHelper.show();

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(getActivity())); // key
        map1.put("imei", Utility.getImei(getActivity())); // imei
        map1.put("versionCode", Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));
        map1.put("ifscCode",binding.editTextIfscCode.getText().toString());
        map1.put("accountNumber",binding.editTextAccountNumber.getText().toString());
        map1.put("name",binding.editTextAccountName.getText().toString());

        String request = Utility.mapWrapper(getActivity(), map1);

        if (in.forpay.util.Utility.isNetworkConnected(getContext())) {

            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_PAYOUT_BANK_ADD, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            progressHelper.dismiss();
                            parseResponse(result);


                        }
                    });
        } else {
            in.forpay.util.Utility.showToast(getContext(), getString(R.string.internet_connect), "");
        }

    }

    private void parseResponseVerification(String result){


        GetPayoutBankVerify response = new Gson().fromJson(result, GetPayoutBankVerify.class);
        binding.editTextAccountName.setText(response.getAccountName());


    }
    private void parseResponse(String result) {


        GetAddPayoutResponse response = new Gson().fromJson(result, GetAddPayoutResponse.class);

            Utility.showToastLatest(getActivity(), response.getResText(), response.getResCode());


    }
}
