package in.forpay.activity.CheckOut;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.OtpActivity;
import in.forpay.adapter.ItemPaymentSummaryAdapter;
import in.forpay.databinding.ActivityCheckoutPrepaidMobileRechargeBinding;
import in.forpay.fragment.RechargeHistoryFragment;
import in.forpay.model.response.GetCashBackOfferResponse;
import in.forpay.model.response.GetPaymentSummaryModel;
import in.forpay.model.response.GetRechargeNowResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import in.forpay.util.databaseTask;
import in.forpay.util.downloadTask;
import in.forpay.util.googleAd;


public class PrepaidMobileRecharge extends AppCompatActivity {
    Bundle bundle;
    private String serviceType;
    private String outputJson;
    private String uniqId;
    private AppCompatActivity activity;
    private String starSelected = "";
    private String deleteContact = "";
    private String offerDetails = "";
    private String selectedMode = "";
    private String mobile = "";
    private ActivityCheckoutPrepaidMobileRechargeBinding binding;
    private ProgressHelper progressHelper;
    private int RESULT_CODE = 1;
    String couponId = "";
    String couponName="";

    private ArrayList<GetPaymentSummaryModel> listData;


    private GetRechargeValidateResponse.PopupData popupData;
    private GetRechargeValidateResponse.InsufficientData insufficientData;

    private GetCashBackOfferResponse.Data getCashBackOfferResponse;
    private boolean isAdded = false;
    private String activityName="";
    private String addedAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_prepaid_mobile_recharge);
        activity = this;
        progressHelper = new ProgressHelper(activity);
        Log.d("PrepaidMobileRecharge", "error  no");
        if (getIntent().getBundleExtra("bundle") != null) {
            try {
                bundle = getIntent().getBundleExtra("bundle");
                outputJson = bundle.getString("outputJson");

                uniqId = bundle.getString("uniqId");

                serviceType = bundle.getString("serviceType");

                offerDetails = bundle.getString("offerDetails");

                popupData = bundle.getParcelable(Constant.POPUPDATA);

                insufficientData = bundle.getParcelable(Constant.INSUFFICIENTDATA);

                if (popupData != null) {

                    Utility.showPopup(activity, popupData);
                    Log.d("PrepaidMobileRecharge", "Popup Data not null");
                }
            } catch (Exception e) {
                Utility.showToastLatest(activity, "Error 1" + e.toString(), "ERROR");
                Log.d("PrepaidMobileRecharge", "error  1" + e.toString());
            }
        }


        try {
            JSONObject jsonObject = new JSONObject(outputJson);
            //JSONArray dataarray = jsonObject.getJSONArray("data");

            String amount = jsonObject.getString("amount");
            String validateDetails = jsonObject.getString("validateDetails");
            String billAmount = jsonObject.getString("billAmount");
            mobile = jsonObject.getString("mobile");
            String service = jsonObject.getString("service");
            String operatorIcon = jsonObject.getString("operatorIcon");
            String bal = jsonObject.getString("bal");

            String profit = jsonObject.getString("profit");
            String youPay = jsonObject.getString("youPay");
            String customerPay = jsonObject.getString("customerPay");
            String serviceCharge = jsonObject.getString("serviceCharge");
            String offerDetails2 = jsonObject.getString("offerDetails");
            String warningMessage = jsonObject.getString("warningMessage");
            String starSelected2 = jsonObject.getString("starSelected");
            selectedMode = jsonObject.getString("selectedMode");
            String showFavContactStar = jsonObject.getString("showFavContactStar");
            String cashbackCouponAvailable=jsonObject.getString("cashbackCouponAvailable");
            if(cashbackCouponAvailable.equals("yes")) {
                binding.LayoutcashbackCoupon.setVisibility(View.VISIBLE);
            }
            JSONArray jsonArray2 = jsonObject.getJSONArray("orderSummary");

            loadOrderDetails(jsonArray2);

            if (!offerDetails2.isEmpty()) {
                offerDetails = offerDetails2;
            }


            if (!showFavContactStar.equals("yes")) {
                binding.favContactStar.setVisibility(View.GONE);
            }

            binding.textViewRemark.setText(validateDetails);
            binding.availableBalance.setText("Rs "+bal);
            if (!offerDetails.isEmpty()) {

                binding.offerDetailsText.setText(Html.fromHtml(offerDetails), TextView.BufferType.SPANNABLE);

            }

            if (!warningMessage.isEmpty()) {

                binding.warningMessage.setText(Html.fromHtml(warningMessage), TextView.BufferType.SPANNABLE);

            }

            binding.warningMessage.setText(warningMessage);


            Utility.imageLoader(this, operatorIcon, binding.icon);


            if (!validateDetails.isEmpty()) {

                binding.textViewRemark.setText(Html.fromHtml(validateDetails), TextView.BufferType.SPANNABLE);

            }

            if (starSelected2.equals("yes")) {
                binding.favContactStar.setImageResource(R.drawable.starselected);
                starSelected = "yes";
            }
            LoadStar();


        } catch (Exception e) {
            Utility.showToastLatest(this, e.toString(), "ERROR");
        }


        try {
            AdView adView = new AdView(activity);

            googleAd gAd = new googleAd(activity, adView);
            if (gAd.getAd() != null) {
                binding.adView.setVisibility(View.VISIBLE);
                binding.adView.loadAd(gAd.getAd());
            }
        }
        catch (Exception e){

        }

        binding.textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();


            }
        });

        binding.textViewCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrepaidMobileRecharge.this, CouponPrepaidActivity.class);
                startActivityForResult(intent, RESULT_CODE);

            }
        });

        binding.textViewProcessPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!isAdded && insufficientData != null && insufficientData.getInsufficientBalance().equals("yes") && couponId.isEmpty())
                    Utility.showInsufficientDialog(activity, insufficientData.getAmount(), insufficientData.getLocationRequired(),uniqId);
                else {

                    Utility.getCurrentLocation(activity, false);


                    Map<String, String> map1 = new HashMap<>();

                    map1.put("token", Utility.getToken(activity)); // key
                    map1.put("imei", Utility.getImei(activity)); // imei
                    map1.put("versionCode", Utility.getVersionCode(activity)); // version code
                    map1.put("os", Utility.getOs(activity));
                    map1.put("starSelected", starSelected);
                    map1.put("urid", Utility.getRandomString(10));
                    map1.put("uniqid", uniqId);
                    map1.put("latitude", Utility.getLatitude(activity));
                    map1.put("longitude", Utility.getLongitude(activity));
                    map1.put("couponId", couponId);
                    map1.put("activityName",activityName);
                    map1.put("addedAmount",addedAmount);

                    String request = Utility.mapWrapper(activity, map1);
                    rechargeNow(request);

                }


            }
        });


        Utility.callPingData(activity);
    }

    private void applyCoupon(){

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("uniqid", uniqId);
        map1.put("couponId", couponId);

        String request = Utility.mapWrapper(activity, map1);
        validateCoupon(request);

    }

    private void validateCoupon(String request){

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_VALIDATE_COUPON, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            progressHelper.dismiss();
                            parseCouponResponse(result);
                        }
                    });


        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void parseCouponResponse(String result){
try {
    JSONObject jsonObject = new JSONObject(result);
    String resCode = jsonObject.getString("resCode");
    String resText=jsonObject.getString("resText");
    if(resCode.equals(Constant.CODE_200)) {
        JSONArray jsonArray2 = jsonObject.getJSONArray("orderSummary");

        loadOrderDetails(jsonArray2);
        binding.textViewCoupon.setText(couponName);
    }
    else{
        Utility.showToastLatest(activity,resText,resCode);
    }

}
catch (Exception e){
    Utility.showToastLatest(activity,e.toString(),"ERROR");
}
    }
    private void loadOrderDetails(JSONArray jsonArray){

        try {
            //JSONArray jsonArray2 = jsonObject.getJSONArray("orderSummary");


            listData = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                GetPaymentSummaryModel usModel = new GetPaymentSummaryModel();

                JSONObject obj = jsonArray.getJSONObject(i);

                usModel.setTitle(obj.getString("title"));
                usModel.setAmount(obj.getString("amount"));

                listData.add(usModel);

            }


            binding.recyclerOrderDetails.setLayoutManager(new LinearLayoutManager(activity));
            ItemPaymentSummaryAdapter itemPaymentSummaryAdapter2 = new ItemPaymentSummaryAdapter(activity, listData, "orderDetails");
            binding.recyclerOrderDetails.setAdapter(itemPaymentSummaryAdapter2);
            itemPaymentSummaryAdapter2.notifyDataSetChanged();
        }
        catch (Exception e){
            Utility.showToastLatest(this, e.toString(), "ERROR");
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Log.d("couponsdjs", "Activity for result called " + resultCode + "requestCode " + requestCode);
        if (resultCode == RESULT_OK) {

            //Log.d("couponsdjs", "Activity for result called " + resultCode + "id " + data.getStringExtra("Coupon_ID"));
            if (requestCode == 101) {
                if (data != null) {

                    isAdded = data.getBooleanExtra("Added", false);
                    addedAmount=data.getStringExtra("amount");

                    if (isAdded) {
                        activityName="AddFundNewActivity";
                        binding.textViewProcessPayment.performClick();

                    }
                    Log.d("PrepaidMobileRecharge", "recharge cliked" + resultCode);
                }
            } else if (requestCode == 1) {
                if (data != null) {
                    couponId = data.getStringExtra("Coupon_ID");
                    couponName=data.getStringExtra("Coupon_Name");
                    applyCoupon();
                }
            }
        }
    }

    private void rechargeNow(String request) {
        Log.d("PrepaidMobileRecharge", "recharge now clicked");

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
            Log.d("RechargeProcessActivity", "Result is " + result);
            if (Utility.isServerRespond(result)) {

                GetRechargeNowResponse response = new Gson().fromJson(result, GetRechargeNowResponse.class);

                Utility.showToast(activity, response.getResText(), response.getResCode());
                Utility.setBalance(activity, response.getData().getBal());
                if (!response.getData().getOrderId().isEmpty()) {
                    Bundle bundle2 = new Bundle();

                    Log.d("PrepaidMobileRecharge", "received orderId " + response.getData().getOrderId());
                    bundle2.putString("orderId", response.getData().getOrderId());


                    Utility.openTransactionDetailsActivity(activity, bundle2);

                        /*
                        Intent intent = new Intent(activity, RechargeDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        intent.putExtra("orderId", response.getData().getOrderId());

                        intent.putExtra("viewType", "");

                        startActivity(intent);

                         */
                    finish();


                } else {

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

    private void LoadStar() {


        binding.favContactStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (starSelected.equals("yes")) {
                    starSelected = "";
                    deleteContact = "yes";
                    Utility.showToast(activity, getString(R.string.title_removed_from_fav_contact), "ERROR");
                    binding.favContactStar.setImageResource(R.drawable.star);
                } else {
                    starSelected = "yes";
                    deleteContact = "";
                    Utility.showToastLatest(activity, getString(R.string.title_added_to_fav_contact), "SUCCESS");
                    binding.favContactStar.setImageResource(R.drawable.starselected);
                }
/*
                Map<String,String> map1 = new HashMap<>();

                map1.put("token",Utility.getToken(activity)); // key
                map1.put("imei",Utility.getImei(activity)); // imei
                map1.put("versionCode" , Utility.getVersionCode(activity)); // version code
                map1.put("os", Utility.getOs(activity));
                map1.put("starSelected" , starSelected);
                map1.put("deleteContact",deleteContact);
                map1.put("operatorId",operatorId);
                map1.put("favNumber",mobile);


                String request = Utility.mapWrapper(activity,map1);
                Utility.favContact(request,activity,progressHelper);

 */
            }
        });

    }
}
