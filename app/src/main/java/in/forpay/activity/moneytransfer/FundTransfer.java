package in.forpay.activity.moneytransfer;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityFundTransferBinding;
import in.forpay.model.response.GetCustomerHistoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class FundTransfer extends AppCompatActivity {

    private ProgressHelper progressHelper;
    private GetCustomerHistoryResponse mResponse;
    private boolean isFlag;
    private String mBeneficiaryId = "";
    private Activity activity;
    private String latitude="",longitude="";
    private ActivityFundTransferBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = FundTransfer.this;
        mBinding = DataBindingUtil.setContentView(activity, R.layout.activity_fund_transfer);
        progressHelper=new ProgressHelper(activity);

        init();
    }

    private void init(){


    }

    private void checkDetails(){

            progressHelper.show();
            Map<String,String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei",Utility.getImei(this)); // imei
            map1.put("versionCode",Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));


            String request = Utility.mapWrapper(this,map1);


            if (Utility.isNetworkConnected(this)) {
                QueryManager.getInstance().postRequest(this, Constant.METHOD_CASHBACK_TO_COUPON, request, new CallbackListener() {
                    @Override
                    public void onResult(Exception e, String result, ResponseManager responseManager) {
                        progressHelper.dismiss();
                        parseCheckResponseResponseData(result);
                    }
                });
            }


    }

    private void parseCheckResponseResponseData(String result){


    }
}
