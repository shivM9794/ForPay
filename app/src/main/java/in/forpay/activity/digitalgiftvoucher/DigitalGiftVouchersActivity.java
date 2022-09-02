package in.forpay.activity.digitalgiftvoucher;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.RechargeProcessActivity;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.FragmentDigitalGiftVoucherBinding;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.listener.Listener;
import in.forpay.model.SampleSearchModel;
import in.forpay.model.request.RechargeHistory;
import in.forpay.model.response.GetBalanceResponse;
import in.forpay.model.response.GetLoginValidateResponse;
import in.forpay.model.response.GetRechargeNowResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class DigitalGiftVouchersActivity extends AppCompatActivity {

    private FragmentDigitalGiftVoucherBinding mBinding;
    private DatabaseHelper databaseHelper;
    private int operatorSelect = -1;
    private String operatorId = "";
    private ProgressHelper progressHelper;
    private String serviceType = "";
    private String mobile="";
    private String amount="0";
    private String pin,opValue1,opValue2,opValue3,opValue4,opValue5,extraAmount,serviceCharge="";
    private Activity activity;
    private String degital_title;

    private OxyMePref oxyMePref;

    String serviceListLocation = "serviceList_Balance" + Constant.METHOD_REFRESH_BALANCE;
    public DigitalGiftVouchersActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DigitalGiftVouchersActivity.this;

        oxyMePref = new OxyMePref(activity);
        mBinding = DataBindingUtil.setContentView(activity, R.layout.fragment_digital_gift_voucher);

        mBinding.editTextSenderName.setText(Utility.getCustomerName(activity));
        mBinding.editTextSenderMobile.setText(Utility.getUsername(activity));
        init();
    }


    @Override
    public void onResume() {
        super.onResume();
        setBalance();
    }

    /**
     * Click on refresh icon
     */
    public void onClickRefresh() {
        Utility.refreshBalance(activity, mBinding.imageViewRefresh, new Listener() {
            @Override
            public void onRefreshBalance() {
                setBalance();
            }
        });
    }

    /**
     * Click on select operator
     */
    public void onClickOperator() {
        selectOperator();
    }

    /**
     * Click on tos
     */
    public void onClickTOS() {
        tosDialog();
    }

    private void init() {
        progressHelper = new ProgressHelper(activity);
        databaseHelper = new DatabaseHelper(activity);
        getBundle();

        mBinding.textViewOperator.setOnClickListener(v -> selectOperator());
        mBinding.imageViewRefresh.setOnClickListener(v -> onClickRefresh());
        mBinding.textViewTOS.setOnClickListener(v -> onClickTOS());
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
        onClickRechargeNow();
    }

    /**
     * Get data from bundle
     */
    private void getBundle() {
        if (getIntent() != null) {
            serviceType = getIntent().getStringExtra(Constant.STATE_FROM);
            degital_title = getIntent().getStringExtra(Constant.TITLE_SHOW);
            mBinding.digitalTitle.setText(degital_title);
        }
    }

    /**
     * Click on recharge now button
     */
    private void onClickRechargeNow() {
        mBinding.textViewRechargeNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {

                    mobile= mBinding.editTextReceiverMobile.getText().toString().trim();
                    amount=mBinding.editTextAmount.getText().toString().trim();
                    pin=mBinding.editTextPin.getText().toString().trim();
                    opValue1=mBinding.editTextSenderName.getText().toString().trim();
                    opValue2=mBinding.editTextSenderMobile.getText().toString().trim();
                    opValue3=mBinding.editTextReceiverName.getText().toString().trim();
                    opValue4=mBinding.editTextReceiverEmail.getText().toString().trim();
                    opValue5=mBinding.editTextGiftMessage.getText().toString().trim();


                    Map<String,String> map1 = new HashMap<>();
                    map1.put("token",Utility.getToken(activity)); // key
                    map1.put("imei",Utility.getImei(activity)); // imei
                    map1.put("versionCode",Utility.getVersionCode(activity)); // version code
                    map1.put("os", Utility.getOs(activity));
                    map1.put("mobile", mobile);
                    map1.put("operatorId",operatorId); // operatorId
                    map1.put("amount",amount); // amount
                    map1.put("pin",pin); // pin
                    map1.put("opValue1",opValue1); // opvalue1
                    map1.put("opValue2",opValue2); // opvalue2
                    map1.put("opValue3",opValue3); // opvalue3
                    map1.put("opValue4",opValue4); // opvalue4
                    map1.put("opValue5",opValue5); // opvalue5
                    map1.put("extraAmount",extraAmount);

                    String request = Utility.mapWrapper(activity,map1);


                    rechargeNow(request);
                    mBinding.editTextPin.setText("");
                }
            }
        });
    }


    /**
     * Set balance
     */
    private void setBalance() {
        // Stop animating the image
        mBinding.imageViewRefresh.setAnimation(null);


        Utility.getServiceList(activity, "Balance", Constant.METHOD_REFRESH_BALANCE, true,"DigitalGiftVoucherActivity", new HomeUpdateListener() {
            @Override
            public void onDone() {
                GetBalanceResponse response = new Gson().fromJson(oxyMePref.getString(serviceListLocation), GetBalanceResponse.class);
                if(response!=null) {
                    try{

                        mBinding.textViewBalance.setText("₹ " + response.getData().getBal());

                        String b = getString(R.string.rupee) + " " + response.getData().getBal();
                        mBinding.textViewBalance.setText(b);
                        String cb = getString(R.string.rupee) + " " + response.getData().getCommissionBal();
                        mBinding.textViewCommissionBalance.setText(cb);
                    }
                    catch (Exception e){
                        Utility.showToastLatest(activity,e.toString(),"ERROR");

                    }
                }
                else{
                    Utility.showToastLatest(activity,"Server not responding","ERROR");
                }

            }
        });


    }

    /**
     * Show selection dialog for operator
     */

    private void selectOperator() {
        Log.d("HomeActivity","op "+serviceType);
        final ArrayList<GetLoginValidateResponse.Service> list = databaseHelper.getServiceList(serviceType);
        Log.d("HomeActivity","op 2");
        if (list != null && list.size() > 0) {
            final ArrayList<SampleSearchModel> items = new ArrayList<>();
            for (GetLoginValidateResponse.Service service : list) {
                items.add(new SampleSearchModel(service.getService()));
            }

            new SimpleSearchDialogCompat(activity, "Choose an brand",
                    "Enter here", null, items,
                    new SearchResultListener<SampleSearchModel>() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat dialog,
                                               SampleSearchModel item, int pos) {
                            int position = items.indexOf(item);
                            operatorSelect = position;
                            operatorId = list.get(position).getId();
                            mBinding.textViewOperator.setText(item.getTitle());
                            isTOSOpen();
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            Utility.showToast(activity, getString(R.string.something_went_wrong),"");
        }
    }



    /**
     * Validation for all fields
     */
    private boolean validation() {
        String operator = mBinding.textViewOperator.getText().toString().trim();
        String senderName = mBinding.editTextSenderName.getText().toString().trim();
        String senderMobile = mBinding.editTextSenderMobile.getText().toString().trim();
        String receiverName = mBinding.editTextReceiverName.getText().toString().trim();
        String receiverMobile = mBinding.editTextReceiverMobile.getText().toString().trim();
        String receiverEmail = mBinding.editTextReceiverEmail.getText().toString().trim();
        String giftMessage = mBinding.editTextGiftMessage.getText().toString().trim();
        String amount = mBinding.editTextAmount.getText().toString().trim();
        String pin = mBinding.editTextPin.getText().toString().trim();

        if (TextUtils.isEmpty(operator)
                || operator.equalsIgnoreCase("Select brand")) {
            Utility.showToast(activity, "Please select brand","");
            return false;
        } else if (TextUtils.isEmpty(senderName)) {
            Utility.showToast(activity, "Please enter sender name","");
            return false;
        } else if (TextUtils.isEmpty(senderMobile)) {
            Utility.showToast(activity, "Please enter sender mobile","");
            return false;
        } else if (TextUtils.isEmpty(receiverName)) {
            Utility.showToast(activity, "Please enter receiver name","");
            return false;
        } else if (TextUtils.isEmpty(receiverMobile)) {
            Utility.showToast(activity, "Please enter receiver mobile","");
            return false;
        } else if (TextUtils.isEmpty(receiverEmail)) {
            Utility.showToast(activity, "Please enter receiver email","");
            return false;
        } else if (TextUtils.isEmpty(giftMessage)) {
            Utility.showToast(activity, "Please enter gift message","");
            return false;
        } else if (TextUtils.isEmpty(amount)) {
            Utility.showToast(activity, "Please enter amount","");
            return false;
        } else if (TextUtils.isEmpty(pin)) {
            Utility.showToast(activity, "Please enter pin","");
            return false;
        }
        return true;
    }

    /**
     * Show dialog for tos
     */
    private void tosDialog() {
        if (!TextUtils.isEmpty(operatorId)) {
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

            //final String url = "file:///android_asset/" + operatorId + ".html";
            final String url ="https://api.forpay.in/giftVoucher/giftVoucherCondition.php?productId="+operatorId;
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

            textViewAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else {
            Utility.showToast(activity, "Please select brand first","");
        }
    }

    /**
     * Check condition for TOS pop up open or not
     */
    private void isTOSOpen() {
        if (TextUtils.isEmpty(operatorId)) {
            mBinding.textViewTOS.setVisibility(View.GONE);
            return;
        }
        int oId = Integer.parseInt(operatorId);
        if (oId > 94 && oId < 139) {
            mBinding.textViewTOS.setVisibility(View.VISIBLE);
        } else {
            mBinding.textViewTOS.setVisibility(View.GONE);
        }
        //mBinding.textViewTOS.setVisibility(View.VISIBLE);
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
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRechargeValidateResponse response = new Gson().fromJson(result,GetRechargeValidateResponse.class);

                if(response.getResCode().equals(Constant.CODE_200)){

                    Intent intent = new Intent(activity, RechargeProcessActivity.class);
                    intent.putExtra("amount",amount);
                    intent.putExtra("mobile",mobile);
                    intent.putExtra("operatorId",operatorId);
                    intent.putExtra("creditUsed",response.getData().getCreditUsed());
                    intent.putExtra("bal",response.getData().getBal());
                    intent.putExtra("service",response.getData().getBal());
                    intent.putExtra("billAmount",response.getData().getBillAmount());
                    intent.putExtra("billName",response.getData().getBillName());
                    intent.putExtra("beneficiaryAccountNumber",response.getData().getBeneficiaryAccountNumber());
                    intent.putExtra("beneficiaryName",response.getData().getBeneficiaryName());
                    intent.putExtra("type","digitalgiftvoucher");
                    intent.putExtra("pin",pin);
                    intent.putExtra("extraAmount",extraAmount);
                    intent.putExtra("opValue1",opValue1);
                    intent.putExtra("opValue2",opValue2);
                    intent.putExtra("opValue3",opValue3);
                    intent.putExtra("opValue4",opValue4);
                    intent.putExtra("opValue5",opValue5);
                    intent.putExtra("serviceCharge",response.getData().getServiceCharge());
                    intent.putExtra("customerPay",response.getData().getCustomerPay());
                    intent.putExtra("profit",response.getData().getProfit());
                    intent.putExtra("starSelected",response.getData().getStarSelected());
                    intent.putExtra("validateDetails",response.getData().getValidateDetails());

                    try{
                        startActivity(intent);
                    }
                    catch (NullPointerException e){
                        Utility.showToastLatest(activity, e.toString(), "ERROR");
                    }
                    catch (Exception e){
                        Utility.showToastLatest(activity, e.toString(), "ERROR");
                    }






                }
                else{
                    Utility.showToast(activity, response.getResText(), response.getResCode());
                }


            } else {
                //Utility.showToast(activity, getString(R.string.server_not_responding), "");
                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }
    }
}
