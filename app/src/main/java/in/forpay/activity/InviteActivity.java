package in.forpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityInviteBinding;
import in.forpay.listener.RechargeNowListener;
import in.forpay.model.response.GetProfileResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class InviteActivity extends AppCompatActivity {

    private ActivityInviteBinding binding;
    private Activity activity;
    private RechargeNowListener mListener;
    private ProgressHelper progressHelper;

    public InviteActivity(RechargeNowListener listener)
    {this.mListener=listener;}

    public InviteActivity() {
        // Required empty public constructor
    }

    public static InviteActivity newInstance(){
        return new InviteActivity();
    }


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
        // Inflate the layout for this fragment
        activity = InviteActivity.this;
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_invite);
        progressHelper=new ProgressHelper(activity);
        init();
        progressHelper.show();
        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(this)); // key
        map1.put("imei",Utility.getImei(this)); // imei
        map1.put("versionCode",Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this,map1);

        binding.backBtn.setOnClickListener(v -> onBackPressed());

        binding.textCommissionBalance.setOnClickListener(view -> {
            //Utility.showToastLatest(activity,"Incentive Balance","SUCCESS");

            Intent intent = new Intent(activity, Cashback_to_CouponActivity.class);

            activity.startActivity(intent);
            finish();
            //agreePopUp(request);
        });


        getProfileDetails(request);

    }

    private void getProfileDetails(String request) {
        if (Utility.isNetworkConnected(activity)) {
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_PROFILE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            progressHelper.dismiss();
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

                binding.shareCode.setText(Utility.getCustomerRefferUrl(activity));
                binding.textCurrentReffer.setText("Current refer: "+Utility.getCustomerReferCount(activity));

                binding.textCommissionBalance.setText(response.getData().getCommissionBal()+" Points");

                Utility.setBalance(activity, response.getData().getBal());
                Utility.setCommissionBalance(activity,response.getData().getCommissionBal());
                //mBinding2.refferTextView.setText("Earn upto 30000 rs per month.\nYou will get commission when your reffered user use our service\n");

                if(response.getOfferUrl()!=null){
                    Utility.showImageDialog(activity,response.getOfferUrl(),2000);
                }

            } else {
                Utility.showToast(activity, response.getResText(),response.getResCode());
                //mBinding.updateProfileTextView.setClickable(false);

            }
        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(activity, ServerNotResponseActivity.class));
            //mBinding.updateProfileTextView.setClickable(false);

        }
    }

    private void agreePopUp(final String request) {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_valid_commissiontransfer);
        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);


            TextView textViewAgree = dialog.findViewById(R.id.textViewAgree);
            if(textViewAgree!=null) {
                textViewAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        transferBalance(request);
                    }
                });

                dialog.show();
            }
        }
    }

    private void transferBalance(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_BALANCE_TRANSFER, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseTransferBalanceResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }


    private void parseTransferBalanceResponse(String result) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            try{
                JSONObject response = new JSONObject(result);

                //String status = response.getData().getStatus();
                if (response.getString("resCode").equals(Constant.CODE_200)) {

                    binding.textCommissionBalance.setText("0 Rs");

                }
                Utility.showToast(activity, response.getString("resText"),response.getString("resCode"));
            }
            catch (Exception e){
                Utility.showToast(activity, "Please try after some time","");
            }

        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(activity, ServerNotResponseActivity.class));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_RECHARGE && resultCode == RESULT_OK && data != null) {
            if (data.getExtras() != null) {

            }
        }
    }

    private void init() {
        String msg = getResources().getString(R.string.referText);
        binding.shareCode.setText(Utility.getCustomerRefferUrl(activity));
        binding.textCurrentReffer.setText("Current refer: "+Utility.getCustomerReferCount(activity));
        binding.btnCopy.setOnClickListener(v -> Utility.copyClipboard(activity,msg+Utility.getCustomerRefferUrl(activity)));
        binding.btnWhatsApp.setOnClickListener(v -> Utility.whatsAppShare(activity,msg+Utility.getCustomerRefferUrl(activity)));
        binding.btnFacebook.setOnClickListener(v -> Utility.FaceBookShare(activity,msg+Utility.getCustomerRefferUrl(activity)));
        binding.btnOther.setOnClickListener(v -> Utility.shareAll(activity,msg+Utility.getCustomerRefferUrl(activity)));
    }

}
