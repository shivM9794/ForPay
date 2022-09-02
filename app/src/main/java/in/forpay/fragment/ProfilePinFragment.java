package in.forpay.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.FragmentProfilePinBinding;
import in.forpay.listener.RechargeNowListener;
import in.forpay.model.response.GetChangePasswordResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ProfilePinFragment extends Fragment {

    private RechargeNowListener mListener;
    private ProgressHelper progressHelper;
    private FragmentProfilePinBinding mBinding;


    public ProfilePinFragment() {


    }

    @SuppressLint("ValidFragment")

    public ProfilePinFragment(RechargeNowListener listener) {
        this.mListener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_pin, container, false);

        mBinding.setFragment(this);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_RECHARGE && resultCode == RESULT_OK && data != null) {
            if (data.getExtras() != null) {


            }
        }
    }

    private void init() {
        progressHelper = new ProgressHelper(getActivity());

        mBinding.submitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> map1 = new HashMap<>();

                map1.put("token", Utility.getToken(getActivity())); // key
                map1.put("imei", Utility.getImei(getActivity())); // imei
                map1.put("versionCode", Utility.getVersionCode(getActivity())); // version code
                map1.put("os", Utility.getOs(getActivity()));

                map1.put("newPin", mBinding.editTextNewPin.getText().toString().trim()); // newPass
                map1.put("confirmPin", mBinding.editTextReEnterPin.getText().toString().trim()); // ConfirmnewPass
                map1.put("otp", mBinding.editTextOtp.getText().toString().trim());

                String request = Utility.mapWrapper(getActivity(), map1);


                if (validation()) {
                    changePin(request);
                }
            }
        });


    }

    private boolean validation() {

        String newPassword = mBinding.editTextNewPin.getText().toString().trim();
        String reEnterPassword = mBinding.editTextReEnterPin.getText().toString().trim();

        if (TextUtils.isEmpty(newPassword)) {
            Utility.showToast(getActivity(), "Please enter new pin", "");
            return false;
        } else if (TextUtils.isEmpty(reEnterPassword)) {
            Utility.showToast(getActivity(), "Please enter Re-Enter pin", "");
            return false;
        } else if (!newPassword.equals(reEnterPassword)) {
            Utility.showToast(getActivity(), "Re enter pin does not match", "");
            return false;
        }
        return true;
    }


    private void changePin(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_CHANGE_PIN, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseChangePinResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect));
        }
    }

    private void parseChangePinResponse(String result) {
        if (isVisible()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetChangePasswordResponse response = new Gson().fromJson(result, GetChangePasswordResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    Utility.showToastLatest(getActivity(), response.getResText(), "SUCCESS");

                } else if (response.getResCode().equals(Constant.CODE_201)) {
                    mBinding.LapyoutOtp.setVisibility(View.VISIBLE);
                    Utility.showToast(getActivity(), response.getResText());
                } else {
                    Utility.showToastLatest(getActivity(), response.getResText(), "ERROR");
                }
            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding));
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
            }
        }
    }
}
