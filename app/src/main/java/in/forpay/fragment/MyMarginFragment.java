package in.forpay.fragment;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.adapter.MyMarginAdapter;
import in.forpay.databinding.FragmentMyMarginBinding;
import in.forpay.model.response.GetMyMarginResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class MyMarginFragment extends Fragment {

    private FragmentMyMarginBinding mBinding;
    private MyMarginAdapter mAdapter;

    public static MyMarginFragment newInstance() {
        return new MyMarginFragment();
    }

    public MyMarginFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_margin, container, false);
        mBinding.setFragment(this);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        editTextFilter();
        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(getActivity())); // key
        map1.put("imei",Utility.getImei(getActivity())); // imei
        map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));


        String request = Utility.mapWrapper(getActivity(),map1);
        getMyMargin(request);
    }

    /**
     * Filter from edit_shop text
     */
    private void editTextFilter() {
        mBinding.editTestSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                mAdapter.getFilter().filter(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * Set adapter
     */
    private void setAdapter(GetMyMarginResponse response) {
        if (response == null || response.getDataList() == null
                || response.getDataList().size() == 0) {
            mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);
            return;
        }
        mBinding.editTestSearch.setVisibility(View.VISIBLE);
        mBinding.textViewNoDataFound.setVisibility(View.GONE);
        mBinding.recyclerView.setAdapter(mAdapter = new MyMarginAdapter(getActivity(), response.getDataList()));
    }

    /**
     * Get my margin
     */
    private void getMyMargin(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_MY_MARGIN, request, new CallbackListener() {
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
                GetMyMarginResponse response = new Gson().fromJson(result, GetMyMarginResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    setAdapter(response);
                } else {
                    Utility.showToast(getActivity(), response.getResText(),response.getResCode());
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
