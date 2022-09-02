package in.forpay.activity.balance;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.ActivityGiftVoucherDetailBinding;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.model.response.GiftVoucherResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class GiftVoucherDetailActivity extends AppCompatActivity {

    private ActivityGiftVoucherDetailBinding binding;
    private AppCompatActivity activity;
    private GiftVoucherResponse.ServiceBean model;
    private ProgressHelper progressHelper;
    private String mobile = "";
    private String amount = "0";
    private String pin = "";
    private boolean isFromHomeActivity;
    private boolean selfButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gift_voucher_detail);
        activity = this;
        progressHelper = new ProgressHelper(activity);

        isFromHomeActivity = getIntent().getBooleanExtra(Constant.IS_FROM_HOME_ACTIVITY, false);

        if (isFromHomeActivity) {
            //model=getIntent().getParcelableExtra(Constant.MAIN_RECHARGE_SERVICE_MODEL);
            model = getIntent().getParcelableExtra(Constant.SERVICE_ARRAY_LIST);
        } else {
            model = getIntent().getParcelableExtra(Constant.SERVICE_ARRAY_LIST);
        }
        init();


        binding.switchBtn.setOnClickListener(v -> {

            if (selfButton == false) {
                binding.linearLayoutSwitch.setVisibility(View.GONE);
                selfButton = true;

            } else {
                binding.linearLayoutSwitch.setVisibility(View.VISIBLE);
                selfButton = false;

            }


        });


    }

    private void init() {
        try {
            //binding.number.setText(Utility.getCustomerMobile(activity));
            binding.serviceNew.setText(model.getBrandName());
            Utility.imageLoader(activity, model.getImage(), binding.image);
            binding.off.setText(model.getDiscount() + " %");
            //Log.d("Amount data ",""+model.getInstruction());
            binding.instructionLabel.setText(model.getCategory());

            binding.backPress.setOnClickListener(view -> onBackPressed());
            binding.tos.setOnClickListener(v -> tosDialog());
            binding.proceed.setOnClickListener(view -> onClickRechargeNow());

            isTOSOpen();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void isTOSOpen() {
        if (TextUtils.isEmpty(model.getProductId())) {
            binding.tos.setVisibility(View.GONE);
            return;
        }

        int oId = Integer.parseInt(model.getProductId());
        if (oId > 94 && oId < 139) {
            binding.tos.setVisibility(View.VISIBLE);
        } else {
            binding.tos.setVisibility(View.GONE);
        }
        binding.tos.setVisibility(View.VISIBLE);
    }

    private void onClickRechargeNow() {
        if (validation()) {

            mobile = binding.editTextReceiverMobile.getText().toString().trim();
            amount = binding.editTextAmount.getText().toString().trim();
            pin = binding.editTextPin.getText().toString().trim();
            String receiverName = binding.editTextReceiverName.getText().toString().trim();
            String receiverEmail = binding.editTextReceiverEmail.getText().toString().trim();
            String receiverMessage = binding.editTextGiftMessage.getText().toString().trim();


            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(activity)); // key
            map1.put("imei", Utility.getImei(activity)); // imei
            map1.put("versionCode", Utility.getVersionCode(activity)); // version code
            map1.put("os", Utility.getOs(activity));
            map1.put("giftVoucherId", model.getProductId());
            map1.put("mobile", mobile);
            map1.put("amount", amount); // amount
            map1.put("pin", pin); // pin
            map1.put("receiverName", receiverName); // opvalue1
            map1.put("receiverEmail", receiverEmail); // opvalue2
            map1.put("receiverMessage", receiverMessage); // opvalue3
            map1.put("selfButton", selfButton + "");

            String request = Utility.mapWrapper(this, map1);


            rechargeNow(request);
            binding.editTextPin.setText("");
        }
    }

    private boolean validation() {

        String amount = binding.editTextAmount.getText().toString().trim();
        String pin = binding.editTextPin.getText().toString().trim();
        if (TextUtils.isEmpty(amount)) {
            Utility.showToast(activity, "Please enter amount", "");
            return false;
        } else if (TextUtils.isEmpty(pin)) {
            Utility.showToast(activity, "Please enter pin", "");
            return false;
        }
        return true;
    }

    private void rechargeNow(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
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

    /**
     * Parse response
     */
    private void parseResponse(String result) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

            if (response.getResCode().equals(Constant.CODE_200)) {

                Bundle bundle = new Bundle();
                bundle.putString("outputJson", response.getData().getOutputJson());
                bundle.putString("uniqId", response.getData().getUniqId());
                bundle.putString("serviceType", response.getData().getType());
                bundle.putString("offerDetails", "");
                bundle.putString("pin", pin);
                bundle.putString("amount", amount);
                bundle.putString("selectedMode", "");
                bundle.putString("mobile", mobile);
                bundle.putString("operatorId", model.getProductId());
                bundle.putString("coupon", "");
                bundle.putString("couponId", "");
                bundle.putString("extraData", response.getData().getExtraData());
                bundle.putParcelable(Constant.INSUFFICIENTDATA, response.getData().getInsufficientData());
                bundle.putParcelable(Constant.POPUPDATA, response.getData().getPopupData());


                Utility.openCheckOutActivity(activity, response.getData().getType(), bundle);

            }
            else {
                Utility.showToast(activity, response.getResText(), response.getResCode());
            }
        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding), "");
            startActivity(new Intent(this, ServerNotResponseActivity.class));
        }
    }

    private void tosDialog() {
        if (!TextUtils.isEmpty(model.getProductId())) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_tos);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.show();

            final ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
            final WebView webView = dialog.findViewById(R.id.webView);
            TextView textViewAgree = dialog.findViewById(R.id.textViewAgree);

            //final String url = "file:///android_asset/" + model.getId() + ".html";
            //final String url ="https://api.forpay.in/data/giftVoucherCondition/"+model.getProductId()+".html";
            final String url = "https://api.forpay.in/giftVoucher/giftVoucherCondition.php?productId=" + model.getProductId();
            //final String url="https://u.godigit.com/nsxv8ht";

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(url);
                    webView.setWebChromeClient(new WebChromeClient() {
                        public void onProgressChanged(WebView view, int progress) {
                            if (progress == 100) {
                                progressBar.setVisibility(View.GONE);
                                webView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }, 500);

            textViewAgree.setOnClickListener(view -> dialog.dismiss());
        } else {
            Utility.showToast(activity, "Please select brand first", "");
        }
    }
}