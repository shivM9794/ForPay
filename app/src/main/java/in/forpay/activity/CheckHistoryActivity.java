package in.forpay.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.HistoryAdapter;
import in.forpay.databinding.ActivityCheckHistoryBinding;
import in.forpay.model.response.GetRequestHistoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class CheckHistoryActivity extends AppCompatActivity {

    private ActivityCheckHistoryBinding mBinding;
    private ProgressHelper progressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_check_history);
        mBinding.setActivity(this);

        init();
    }

    private void init() {

        progressHelper = new ProgressHelper(this);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setToolbar();


        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(this)); // key
        map1.put("imei",Utility.getImei(this)); // imei
        map1.put("versionCode",Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this,map1);


        getHistory(request);
    }

    /**
     * Set toolbar
     */
    private void setToolbar() {
        mBinding.mainCheckTitle.setText("History");
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
    }

    /**
     * Set adapter
     */
    private void setAdapter(ArrayList<GetRequestHistoryResponse.FundData> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        mBinding.recyclerView.setAdapter(new HistoryAdapter(this, list));
    }

    /**
     * Get history
     */
    private void getHistory(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_REQUEST_FUND_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseGetHistoryResponse(result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseGetHistoryResponse(String result) {
        if (!isDestroyed()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRequestHistoryResponse response = new Gson().fromJson(result, GetRequestHistoryResponse.class);
                if (response.getData().getResCode().equals(Constant.CODE_200)) {
                    //Utility.showToast(getActivity(), response.getData().getResText());
                    setAdapter(response.getData().getFundList());
                } else {
                    Utility.showToast(this, response.getData().getResText(),response.getData().getResCode());
                }
            } else {
                //Utility.showToast(this, getString(R.string.server_not_responding),"");
                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }
        }
    }
}
