package in.forpay.fragment;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.FragmentContactUsBinding;
import in.forpay.model.response.GetContactDetailResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class ContactUsFragment extends Fragment {

    private FragmentContactUsBinding mBinding;

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }

    public ContactUsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {

        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(getActivity())); // key
        map1.put("imei",Utility.getImei(getActivity())); // imei
        map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));

        String request = Utility.mapWrapper(getActivity(),map1);

        getContact(request);
    }

    /**
     * Set data on UI
     */
    private void setData(GetContactDetailResponse response) {
       /* if (response == null || response.getData() == null
                || TextUtils.isEmpty(response.getData().getContactNumber())
                || TextUtils.isEmpty(response.getData().getContactEmail())) {
            mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);
            mBinding.linearLayoutContact.setVisibility(View.GONE);
            return;
        }
        mBinding.linearLayoutContact.setVisibility(View.VISIBLE);
        mBinding.textViewMobile.setText(response.getData().getContactNumber());
        mBinding.textViewEmail.setText(response.getData().getContactEmail());*/
    }

    /**
     * Get contact us detail
     */
    private void getContact(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_CONTACT_US, request, new CallbackListener() {
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
            mBinding.progressBar.setVisibility(View.GONE);
            if (Utility.isServerRespond(result)) {
                GetContactDetailResponse response = new Gson().fromJson(result, GetContactDetailResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    setData(response);
                } else {
                    Utility.showToast(getActivity(), response.getResText(),"");
                    mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);
                }
            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding),"");
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
                mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);
            }
        }
    }
}
