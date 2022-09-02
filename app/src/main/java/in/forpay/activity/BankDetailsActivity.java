package in.forpay.activity;

import android.app.Activity;
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
import in.forpay.adapter.BankDetailsAdapter;
import in.forpay.databinding.FragmentBankDetailsBinding;
import in.forpay.model.response.GetBankDetailsResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class BankDetailsActivity extends AppCompatActivity {

    private FragmentBankDetailsBinding mBinding;
    private ProgressHelper progressHelper;
    private Activity activity;

    public static BankDetailsActivity newInstance() {
        return new BankDetailsActivity();
    }

    public BankDetailsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = BankDetailsActivity.this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_bank_details);
        init();

    }



    private void init() {
        progressHelper = new ProgressHelper(activity);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());


        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));

        String request = Utility.mapWrapper(this,map1);

        getBankDetails(request);
    }

    /**
     * Set adapter
     */
    private void setAdapter(ArrayList<GetBankDetailsResponse.Data> list) {
        if (list == null || list.size() == 0) {
            Utility.inflateNoDataFoundLayout(activity, mBinding.inflateLayout, "No Result Found");
            return;
        }
        mBinding.recyclerView.setAdapter(new BankDetailsAdapter(activity,list));
    }

    /**
     * Get bank details
     */
    private void getBankDetails(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_GET_BANK_DETAILS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect));
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetBankDetailsResponse response = new Gson().fromJson(result, GetBankDetailsResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    setAdapter(response.getDataList());
                } else {
                    Utility.showToast(activity, response.getResText(),response.getResCode());
                }
            } else {
                //Utility.showToast(activity, getString(R.string.server_not_responding),"");
                startActivity(new Intent(activity, ServerNotResponseActivity.class));
            }
    }
}
