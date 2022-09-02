package in.forpay.activity.CheckOut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityCheckoutBeneficiaryValidateBinding;
import in.forpay.fragment.RechargeHistoryFragment;
import in.forpay.model.response.GetRechargeNowResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class BeneficiaryValidate extends AppCompatActivity {


    private Activity activity;
    ProgressHelper progressHelper;
    Bundle bundle;
    private String serviceType;
    private String outputJson;
    private String uniqId;
    private String deleteContact="";
    private String offerDetails="";

    private String mobile="";
    ImageView textViewBack;

    private GetRechargeValidateResponse.PopupData popupData;

    private GetRechargeValidateResponse.InsufficientData insufficientData;

    private boolean isAdded = false;
    private ActivityCheckoutBeneficiaryValidateBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PrepaidMobileRecharge", "1");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_beneficiary_validate);
        activity = this;
        progressHelper = new ProgressHelper(activity);
        if (getIntent().getBundleExtra("bundle") != null) {
            try {
                bundle = getIntent().getBundleExtra("bundle");
                outputJson = bundle.getString("outputJson");
                uniqId = bundle.getString("uniqId");
                serviceType = bundle.getString("serviceType");


                popupData = bundle.getParcelable(Constant.POPUPDATA);
                insufficientData = bundle.getParcelable(Constant.INSUFFICIENTDATA);

                if (popupData != null) {

                    Utility.showPopup(activity, popupData);
                    Log.d("PrepaidMobileRecharge", "Popup Data not null");
                }
            }
            catch (Exception e){
                Utility.showToastLatest(this,"Error 1"+e.toString(),"ERROR");
                Log.d("PrepaidMobileRecharge", "Error 1 "+e.toString());
            }
        }


        try {
            JSONObject jsonObject = new JSONObject(outputJson);

            String validateDetails=jsonObject.getString("validateDetails");
            mobile=jsonObject.getString("mobile");
            String operatorIcon=jsonObject.getString("operatorIcon");
            String bal=jsonObject.getString("bal");
            offerDetails = jsonObject.getString("offerDetails");

            String youPay=jsonObject.getString("youPay");
            String serviceCharge=jsonObject.getString("serviceCharge");
            String warningMessage=jsonObject.getString("warningMessage");


            Log.d("PrepaidMobileRecharge", "process 1 ");
            binding.textViewCurrentBal.setText(bal);
            binding.textViewPayAmount.setText(youPay);
            binding.textViewIFSCCode.setText(jsonObject.getString("ifscCode"));
            binding.accountNumber.setText(jsonObject.getString("accountNumber"));

            Log.d("PrepaidMobileRecharge", "process 2 ");
            if (!offerDetails.isEmpty()) {

                binding.offerDetailsText.setText(Html.fromHtml(offerDetails), TextView.BufferType.SPANNABLE);

            }

            Log.d("PrepaidMobileRecharge", "process 3 ");

            if (!warningMessage.isEmpty()) {

                binding.warningMessage.setText(Html.fromHtml(warningMessage), TextView.BufferType.SPANNABLE);

            }

            Log.d("PrepaidMobileRecharge", "process 4 ");

            binding.warningMessage.setText(warningMessage);

            Utility.imageLoader(this, operatorIcon, binding.operatoriv);


            Log.d("PrepaidMobileRecharge", "process 5 ");




            if (!validateDetails.isEmpty()) {

                binding.textViewRemark.setText(Html.fromHtml(validateDetails), TextView.BufferType.SPANNABLE);

            }


            Log.d("PrepaidMobileRecharge", "process 4 ");

        }
        catch (Exception e){
            Utility.showToastLatest(this,"Error 2"+e.toString(),"ERROR");
            Log.d("PrepaidMobileRecharge", "Error 2 "+e.toString());
        }


        textViewBack = findViewById(R.id.textViewBack);


        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });


        binding.textViewProcessPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!isAdded && insufficientData != null && insufficientData.getInsufficientBalance().equals("yes"))
                    Utility.showInsufficientDialog(activity,insufficientData.getAmount(),insufficientData.getLocationRequired(),uniqId);
                else {

                    Utility.getCurrentLocation(activity, false);


                    Map<String,String> map1 = new HashMap<>();

                    map1.put("token",Utility.getToken(activity)); // key
                    map1.put("imei",Utility.getImei(activity)); // imei
                    map1.put("versionCode",Utility.getVersionCode(activity)); // version code
                    map1.put("os", Utility.getOs(activity));
                    map1.put("urid",Utility.getRandomString(10));
                    map1.put("uniqid" , uniqId);
                    map1.put("latitude" , Utility.getLatitude(activity));
                    map1.put("longitude" , Utility.getLongitude(activity));

                    String request = Utility.mapWrapper(activity,map1);
                    rechargeNow(request);

                }

            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("PrepaidMobileRecharge","Activity for result called "+resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                if (data != null) {

                    isAdded = data.getBooleanExtra("Added", false);

                    if (isAdded)
                        binding.textViewProcessPayment.performClick();

                    Log.d("PrepaidMobileRecharge","recharge cliked"+resultCode);
                }
            }
        }
    }

    private void rechargeNow(String request){
        Log.d("PrepaidMobileRecharge","recharge now clicked");

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_RECHARGE_NOW, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });


        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void parseResponse(String result) {
        if (!isDestroyed()) {
            progressHelper.dismiss();
            Log.d("RechargeProcessActivity","Result is "+result);
            if (Utility.isServerRespond(result)) {

                GetRechargeNowResponse response = new Gson().fromJson(result, GetRechargeNowResponse.class);

                Utility.showToast(activity, response.getResText(), response.getResCode());
                Utility.setBalance(activity, response.getData().getBal());
                if(!response.getData().getOrderId().isEmpty()) {
                    Bundle bundle2 = new Bundle();

                    Log.d("PrepaidMobileRecharge","received orderId "+response.getData().getOrderId());
                    bundle2.putString("orderId",response.getData().getOrderId());
                    bundle2.putString("viewType","");


                    Utility.openTransactionDetailsActivity(activity,bundle2);

                        /*
                        Intent intent = new Intent(activity, RechargeDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        intent.putExtra("orderId", response.getData().getOrderId());

                        intent.putExtra("viewType", "");

                        startActivity(intent);

                         */
                    finish();


                }
                else{

                    Utility.showToast(activity, response.getResText(), response.getResCode());

                }


            } else {
                Utility.showToast(activity, getString(R.string.server_not_responding), "");

                RechargeHistoryFragment fragment = new RechargeHistoryFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, fragment);
                transaction.commitAllowingStateLoss();

            }
        }
    }

}
