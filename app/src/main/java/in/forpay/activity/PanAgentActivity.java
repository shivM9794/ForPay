package in.forpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.psa.RegisterMobileActivity;
import in.forpay.databinding.FragmentPsaAgentBinding;
import in.forpay.model.response.GetPanAgentResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class PanAgentActivity extends AppCompatActivity {

    private FragmentPsaAgentBinding mBinding;
    private ProgressHelper progressHelper;
    private Activity activity;

    public PanAgentActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = PanAgentActivity.this;
        mBinding = DataBindingUtil.setContentView(activity, R.layout.fragment_psa_agent);
        progressHelper = new ProgressHelper(activity);
        init();
    }


    private void init() {

        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("mobile", Utility.getUsername(activity));
        String request = Utility.mapWrapper(this,map1);

        getAgentStatus(request);
    }


    /**
     * Get contact us detail
     */
    private void getAgentStatus(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_PSA_AGENT_STATUS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetPanAgentResponse response = new Gson().fromJson(result, GetPanAgentResponse.class);
                if (response.getResCode().equals(Constant.CODE_200) ){ //|| response.getResCode().equals("122")) {
                    Utility.showToastLatest(activity,response.getResText(),"SUCCESS");

                    Intent intent = new Intent(activity, RegisterMobileActivity.class);
                            intent.putExtra("psaId",response.getData().getPsaId());
                            intent.putExtra("psaAnswer",response.getPsaAnswer());
                            intent.putExtra("psaQuestion",response.getPsaQuestion());
                    startActivity(intent);
                    if(activity!=null) {
                        activity.onBackPressed();
                    }



                }
                else if (response.getResCode().equals(Constant.CODE_123)) {

                    Utility.showToast(activity, response.getResText(),response.getResCode());

                    //Log.d("Amount ius ",response.getPsaAmount());
                    Utility.setPsaAmount(activity,response.getPsaAmount());

                    Intent intent = new Intent(activity, RegisterMobileActivity.class);
                    intent.putExtra("psaId",response.getData().getPsaId());
                    intent.putExtra("psaAnswer",response.getPsaAnswer());
                    intent.putExtra("psaQuestion",response.getPsaQuestion());
                    startActivity(intent);
                    if(activity!=null) {
                        activity.onBackPressed();
                    }
                }
                else {
                    Utility.showToast(activity, response.getResText(),"");

                }
            } else {

                //Utility.showToast(activity, getString(R.string.server_not_responding),"");
                startActivity(new Intent(this, ServerNotResponseActivity.class));

            }
    }
}
