package in.forpay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import in.forpay.activity.psa.RegisterMobileActivity;
import in.forpay.databinding.FragmentPsaAgentBinding;
import in.forpay.model.response.GetPanAgentResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class PanAgentFragment extends Fragment {

    private FragmentPsaAgentBinding mBinding;
    private ProgressHelper progressHelper;
    public static PanAgentFragment newInstance() {
        return new PanAgentFragment();
    }

    public PanAgentFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_psa_agent, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressHelper = new ProgressHelper(getActivity());
        init();
    }

    private void init() {
        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(getActivity())); // key
        map1.put("imei",Utility.getImei(getActivity())); // imei
        map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));


        String request = Utility.mapWrapper(getActivity(),map1);
        getAgentStatus(request);
    }


    /**
     * Get contact us detail
     */
    private void getAgentStatus(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_PSA_AGENT_STATUS, request, new CallbackListener() {
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
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetPanAgentResponse response = new Gson().fromJson(result, GetPanAgentResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    Utility.showToastLatest(getActivity(),response.getResText(),"SUCCESS");

                    Intent intent = new Intent(getActivity(), RegisterMobileActivity.class);
                            intent.putExtra("psaId",response.getData().getPsaId());
                    intent.putExtra("psaAnswer",response.getPsaAnswer());
                    intent.putExtra("psaQuestion",response.getPsaQuestion());
                    startActivity(intent);
                    if(getActivity()!=null) {
                        getActivity().onBackPressed();
                    }



                }
                else if (response.getResCode().equals(Constant.CODE_123)) {

                    Utility.showToast(getActivity(), response.getResText(),response.getResCode());

                    //Log.d("Amount ius ",response.getPsaAmount());
                    Utility.setPsaAmount(getActivity(),response.getPsaAmount());

                    Intent intent = new Intent(getActivity(), RegisterMobileActivity.class);
                    intent.putExtra("psaId",response.getData().getPsaId());
                    startActivity(intent);
                    if(getActivity()!=null) {
                        getActivity().onBackPressed();
                    }
                }
                else {
                    Utility.showToast(getActivity(), response.getResText(),"");

                }
            } else {

                //Utility.showToast(getActivity(), getString(R.string.server_not_responding),"");
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));

            }
        }
    }
}
