package in.forpay.activity.premiumplan;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.RechargeProcessActivity;
import in.forpay.databinding.ActivityPremiumPlanBinding;
import in.forpay.model.response.GetProfileResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class PremiumPlanActivity extends AppCompatActivity {

    private ActivityPremiumPlanBinding binding;
    private AppCompatActivity activity;
//    private int[] list={R.drawable.premium_three_img,R.drawable.premium_one_img,R.drawable.leveldetails,R.drawable.premium_two_img};
    private ArrayList<GetProfileResponse.PremiumBanner> list;
    private String premiumHeader;
    private String mPin;
    private ProgressHelper progressHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utility.checkLoggedUser(this);
        /*
        if(Utility.getToken(this).isEmpty()){
            Intent intent = new Intent(this,
                    LoginActivity1.class);
            startActivity(intent);
            finish();
        }
*/
        binding= ActivityPremiumPlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper = new ProgressHelper(this);

        init();

        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(this)); // key
        map1.put("imei",Utility.getImei(this)); // imei
        map1.put("versionCode",Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this,map1);
        getProfileDetails(request);
    }

    private void getProfileDetails(String request) {
        if (Utility.isNetworkConnected(activity)) {
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_PROFILE, request, new CallbackListener() {
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

    private void parseResponse(String result) {

        if (Utility.isServerRespond(result)) {
            GetProfileResponse response = new Gson().fromJson(result, GetProfileResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {

                Utility.setCustomerEmail(activity,response.getData().getEmail());
                Utility.setCustomerName(activity,response.getData().getName());
                Utility.setCustomerMobile(activity,response.getData().getMobile());
                Utility.setCustomerPan(activity,response.getData().getPan());
                Utility.setCustomerPincode(activity,response.getData().getPincode());
                Utility.setCustomerRefferUrl(activity,response.getData().getReferralUrl());
                Utility.setCustomerReferCount(activity,response.getData().getReferCount());
                Utility.setCustomerHomeLandmark(activity,response.getData().getHomeLandmark());

               premiumHeader=response.getPremiumHeader();
               list=response.getPremiumBanner();

//               binding.title.setText(premiumHeader);


                setAdapter();



                if(response.getMlm()!=null){
                    if(response.getMlm().equals("yes")){
                        //binding.joinNow.setVisibility(View.GONE);
                        binding.joinNow.setText("Already Joined");
                        binding.joinNow.setClickable(false);

                    }
                }

            } else {
                Utility.showToast(activity, response.getResText(),response.getResCode());
                //mBinding.updateProfileTextView.setClickable(false);

            }
        } else {
            Utility.showToast(activity, getString(R.string.server_not_responding),"");
            //mBinding.updateProfileTextView.setClickable(false);

        }
    }
    private void init() {

        binding.backPress.setOnClickListener(view -> onBackPressed());


        binding.joinNow.setOnClickListener(v -> {

            mPin="0000";

            Map<String,String> map1 = new HashMap<>();
            map1.put("token",Utility.getToken(activity)); // key
            map1.put("imei",Utility.getImei(activity)); // imei
            map1.put("versionCode",Utility.getVersionCode(activity)); // version code
            map1.put("os", Utility.getOs(activity));
            map1.put("mobile", Utility.getUsername(this));
            map1.put("operatorId","196"); // operatorId

            map1.put("pin",mPin); // pin


            String request = Utility.mapWrapper(this,map1);


            if (Utility.isNetworkConnected(this)) {
                progressHelper.show();
                QueryManager.getInstance().postRequest(this,
                        Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {
                                parseResponseRecharge(result);
                            }
                        });
            } else {
                Utility.showToast(this, getString(R.string.internet_connect), "");
            }

        });


    }

    private void parseResponseRecharge(String result) {
        if (!isDestroyed()) {
            if (progressHelper.isShowing())
                progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

                if(response.getResCode().equals(Constant.CODE_200)){

                    Bundle bundle = new Bundle();
                    bundle.putString("outputJson", response.getData().getOutputJson());
                    bundle.putString("uniqId", response.getData().getUniqId());
                    bundle.putString("serviceType", response.getData().getType());
                    bundle.putString("offerDetails", "");
                    bundle.putString("pin", "0000");
                    bundle.putString("amount", response.getData().getBillAmount());
                    bundle.putString("selectedMode", "");
                    bundle.putString("mobile", response.getData().getMobile());
                    bundle.putString("operatorId", response.getData().getOperatorId());
                    bundle.putString("extraData", response.getData().getExtraData());
                    bundle.putString("coupon", "");
                    bundle.putString("couponId", "");
                    bundle.putParcelable(Constant.INSUFFICIENTDATA, response.getData().getInsufficientData());
                    bundle.putParcelable(Constant.POPUPDATA, response.getData().getPopupData());


                    Utility.openCheckOutActivity(activity, response.getData().getType(), bundle);





                }
                else{
                    Utility.showToast(this, response.getResText(), response.getResCode());
                }



            } else {
                Utility.showToast(this, getString(R.string.server_not_responding), "");
            }
        }
    }
    private void setAdapter() {

        binding.viewPager.startAutoScroll();
        binding.viewPager.setInterval(5000);
        binding.viewPager.setCycle(true);
        binding.viewPager.setStopScrollWhenTouch(true);


        PagerAdapter adapter = new PremiumPlanAdapter(activity,premiumHeader,list);
        binding.viewPager.setAdapter(adapter);

    }
}