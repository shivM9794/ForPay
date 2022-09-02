package in.forpay.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.FragmentTshirtBottomSheetBinding;
import in.forpay.model.response.GetAddressResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class TshirtBottomSheet extends BottomSheetDialogFragment {

    private FragmentTshirtBottomSheetBinding binding;
    ProgressHelper progressHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tshirt_bottom_sheet, container, false);
        progressHelper = new ProgressHelper(getActivity());

        binding.txtProceed.setOnClickListener(v -> initReq());

        binding.closeBtn.setOnClickListener(v -> dismiss());

        binding.editTextName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 11) {
                    if (before == 0 && count == 1) {
                        veriyfyName();
                    } else {

                    }

                }

            }
        });


        return binding.getRoot();
    }

    private void veriyfyName() {
//        Log.e("cal", "true");
        progressHelper.show();

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(getActivity())); // key
        map1.put("imei", Utility.getImei(getActivity())); // imei
        map1.put("versionCode", Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));
        map1.put("customerName", binding.editTextName.getText().toString());
        map1.put("customerMobile", binding.editTextMobile.getText().toString());
        map1.put("flatNumber", binding.editTextAddress.getText().toString());
        map1.put("customerArea", binding.editTextArea.getText().toString());
        map1.put("landmark", binding.editTextLandmark.getText().toString());
        map1.put("pinCode", binding.editTextPinCode.getText().toString());


        String request = Utility.mapWrapper(getActivity(), map1);


        if (in.forpay.util.Utility.isNetworkConnected(getContext())) {

            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_SHOP_ADDRESS_LIST, request, new CallbackListener() {
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
        map1.put("customerName", binding.editTextName.getText().toString());
        map1.put("customerMobile", binding.editTextMobile.getText().toString());
        map1.put("flatNumber", binding.editTextAddress.getText().toString());
        map1.put("customerArea", binding.editTextArea.getText().toString());
        map1.put("landmark", binding.editTextLandmark.getText().toString());
        map1.put("pinCode", binding.editTextPinCode.getText().toString());

        String request = Utility.mapWrapper(getActivity(), map1);

        if (in.forpay.util.Utility.isNetworkConnected(getContext())) {

            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_SHOP_ADD_ADDRESS, request, new CallbackListener() {
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

    private void parseResponseVerification(String result) {


        GetAddressResponse response = new Gson().fromJson(result, GetAddressResponse.class);

//        binding.editTextAccountName.setText(response.getAccountName());


    }

    private void parseResponse(String result) {


        GetAddressResponse response = new Gson().fromJson(result, GetAddressResponse.class);

        Utility.showToastLatest(getActivity(), response.getResText(), response.getResCode());


    }
}