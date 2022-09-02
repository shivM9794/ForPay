package in.forpay.activity;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_NOTIFY_URL;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;
import static com.cashfree.pg.CFPaymentService.PARAM_PAYMENT_OPTION;
import static com.cashfree.pg.CFPaymentService.PARAM_UPI_VPA;
import static com.cashfree.pg.CFPaymentService.PARAM_WALLET_CODE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.cashfree.pg.CFPaymentService;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.payu.base.models.ErrorResponse;
import com.payu.base.models.PayUPaymentParams;
import com.payu.checkoutpro.PayUCheckoutPro;
import com.payu.checkoutpro.utils.PayUCheckoutProConstants;
import com.payu.ui.model.listeners.PayUCheckoutProListener;
import com.payu.ui.model.listeners.PayUHashGenerationListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.view.circulartimerview.CircularTimerListener;
import com.view.circulartimerview.CircularTimerView;
import com.view.circulartimerview.TimeFormatEnum;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import in.forpay.R;
import in.forpay.adapter.AddFundTransactionAdapter;
import in.forpay.databinding.ActivityAddFundNew2Binding;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.model.response.GetAddFundResponse;
import in.forpay.model.response.GetBalanceResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class AddFundNewActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback, PaymentResultListener {

    private ActivityAddFundNew2Binding mBinding;
    private String mAmount = "";
    String cusId, mid = " ";
    String response = "";
    private OxyMePref oxyMePref;
    String maxLimit = "2000";
    //Integer maxLimitInt=2000;
    private ProgressHelper progressHelper;
    ImageView textViewBack;
    private MainRechargeModel model;
    private Dialog gatewayDialog;
    private EditText upiId;
    ScrollView scrollView;
    LinearLayout upilay;
    SharedPreferences prefs;
    ArrayList<GetBalanceResponse.Data.PaymentGateway> paymentGateways1;
    ArrayList<GetBalanceResponse.Data.PaymentHistory> paymentHistory;
    private AddFundTransactionAdapter addFundTransactionAdapter;
    String serviceListLocation = "serviceList_Balance" + Constant.METHOD_REFRESH_BALANCE;
    final int UPI_PAYMENT = 0;
    String activityName = "";
    String urid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_fund_new2);
        mBinding.setActivity(this);
        textViewBack = findViewById(R.id.textViewBack);
        maxLimit = Utility.getGatewayLimit(this);
        oxyMePref = new OxyMePref(this);
        progressHelper = new ProgressHelper(this);
        activityName = getIntent().getStringExtra("from");
        if (activityName == null) {
            activityName = "AddFundActivity";
        }
        urid = getIntent().getStringExtra("urid");

        if (getIntent().getStringExtra("Amount") != null) {
            mBinding.editAmount.setText(getIntent().getStringExtra("Amount"));
            mBinding.editAmount.setEnabled(false);
            mBinding.editAmount.setTextColor(getResources().getColor(R.color.hint_text));
            mBinding.amount500.setEnabled(false);
            mBinding.amount1000.setEnabled(false);
            mBinding.amount1500.setEnabled(false);
            mBinding.amount2000.setEnabled(false);
        }

        updateBalance();
        // mBinding.textViewBalance.setText("₹ "+Utility.getBalance(this));

        mBinding.amount500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.editAmount.setText("500");
            }
        });

        mBinding.amount1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.editAmount.setText("1000");
            }
        });

        mBinding.amount1500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.editAmount.setText("1500");
            }
        });

        mBinding.amount2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.editAmount.setText("2000");
            }
        });

        mBinding.imageViewAddFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAmount = mBinding.editAmount.getText().toString();

                if (validation()) {

//                    Utility.getHomeUpdate(AddfundActivity.this,"", new HomeUpdateListener() {
//                        @Override
//                        public void onDone() {
                    parseHomeUpdateResponse();

//                        }
//                    });

//                    init("");
                } else {
                    //Utility.showToastLatest(AddfundActivity.this,"Please enter amount between 100 to "+maxLimitInt+" Rs","ERROR");
                }


            }
        });


        textViewBack.setOnClickListener(view -> onBackPressed());

    }

    private void updateBalance() {

        progressHelper.show();
        Utility.getServiceList(AddFundNewActivity.this, "Balance", Constant.METHOD_REFRESH_BALANCE, true, activityName, new HomeUpdateListener() {
            @Override
            public void onDone() {
                progressHelper.dismiss();
                GetBalanceResponse response = new Gson().fromJson(oxyMePref.getString(serviceListLocation), GetBalanceResponse.class);
                if (response != null) {
                    try {

                        paymentGateways1 = response.getData().getPaymentGateway();
                        mBinding.textViewBalance.setText("₹ " + response.getData().getBal());

                        if (response.getData().getShowPaymentHistory().equals("yes")) {
                            mBinding.llAddFundHistory.setVisibility(View.VISIBLE);
                            paymentHistory = response.getData().getPaymentHistory();
                            setFundAdapter();
                        }

                    } catch (Exception e) {
                        Utility.showToastLatest(AddFundNewActivity.this, e.toString(), "ERROR");

                    }
                } else {
                    Utility.showToastLatest(AddFundNewActivity.this, "Server not responding", "ERROR");
                }


            }
        });

    }

    private void setFundAdapter() {

        mBinding.recyclerTransactionDetails.setLayoutManager(new LinearLayoutManager(this));
        addFundTransactionAdapter = new AddFundTransactionAdapter(this, paymentHistory);
        mBinding.recyclerTransactionDetails.setAdapter(addFundTransactionAdapter);


    }

    private void parseRefreshBalanceResponse(String result) {

        if (Utility.isServerRespond(result)) {

            GetBalanceResponse response = new Gson().fromJson(result, GetBalanceResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {


                mBinding.textViewBalance.setText("₹ " + response.getData().getBal());


            }

        }
    }

    private void parseHomeUpdateResponse() {
        try {

            showPaymentGatewayDialog(paymentGateways1);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPaymentGatewayDialog(ArrayList<GetBalanceResponse.Data.PaymentGateway> paymentGateways1) {

        AlertDialog.Builder paymentGatewayDialog = new AlertDialog.Builder(AddFundNewActivity.this);
        View payment_gateway_lay = LayoutInflater.from(AddFundNewActivity.this).inflate(R.layout.payment_gateway_dialog, null);

        LinearLayout mainLinear = payment_gateway_lay.findViewById(R.id.mainLinear);

        TextView title = payment_gateway_lay.findViewById(R.id.title);

        scrollView = payment_gateway_lay.findViewById(R.id.scrollView);
        upilay = payment_gateway_lay.findViewById(R.id.upilay);

        upiId = payment_gateway_lay.findViewById(R.id.editupi);
        ImageView go = payment_gateway_lay.findViewById(R.id.upigo);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gatewayDialog != null)
                    gatewayDialog.dismiss();
                init("webUpi");
            }
        });

        paymentGatewayDialog.setCancelable(false);
        paymentGatewayDialog.setView(payment_gateway_lay);
        for (GetBalanceResponse.Data.PaymentGateway paymentGateway : paymentGateways1) {

            View line = new View(this);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
            );
            lineParams.setMargins(5, 0, 5, 0);
            line.setLayoutParams(lineParams);
            line.setBackgroundColor(Color.parseColor("#B3B3B3"));

            LinearLayout.LayoutParams innerLinerLayParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            innerLinerLayParams.setMargins(8, 8, 8, 8);

            LinearLayout innerl = new LinearLayout(AddFundNewActivity.this);
            innerl.setPadding(20, 20, 20, 20);
            innerl.setOrientation(LinearLayout.HORIZONTAL);
            innerl.setLayoutParams(innerLinerLayParams);

            ImageView image = new ImageView(AddFundNewActivity.this);
            LinearLayout.LayoutParams imagep = new LinearLayout.LayoutParams(96, 96);
            imagep.setMargins(8, 0, 0, 0);
            image.setLayoutParams(imagep);
            Glide.with(AddFundNewActivity.this)
                    .load(paymentGateway.getIconUrl())
                    .into(image);
            innerl.addView(image);

            LinearLayout.LayoutParams textviewLayParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textviewLayParams.setMargins(22, 0, 0, 0);
            textviewLayParams.gravity = Gravity.CENTER;

            LinearLayout titleLay = new LinearLayout(AddFundNewActivity.this);
            titleLay.setOrientation(LinearLayout.VERTICAL);
            titleLay.setLayoutParams(textviewLayParams);

            TextView textView = new TextView(AddFundNewActivity.this);
            textView.setText(paymentGateway.getName());
            textView.setTypeface(ResourcesCompat.getFont(AddFundNewActivity.this, R.font.poppins_semibold));
            titleLay.addView(textView);

            LinearLayout.LayoutParams subtextviewLayParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView subtextView = new TextView(AddFundNewActivity.this);
            //subtextView.setText(paymentGateway.getText());


            subtextView.setText(Html.fromHtml(paymentGateway.getText()), TextView.BufferType.SPANNABLE);
            subtextView.setLayoutParams(subtextviewLayParams);
            subtextView.setTypeface(ResourcesCompat.getFont(AddFundNewActivity.this, R.font.poppins_regular));
            subtextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            subtextView.setTextColor(getResources().getColor(R.color.orange));

            titleLay.addView(subtextView);

            innerl.addView(titleLay);
            innerl.setTag(paymentGateway.getType());

            mainLinear.addView(line);
            mainLinear.addView(innerl);


        }


        if (mainLinear.findViewWithTag("internalUpiApp") != null) {
            mainLinear.findViewWithTag("internalUpiApp").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    init("");
                    gatewayDialog.dismiss();

                }
            });
        }

        if (mainLinear.findViewWithTag("razorpay") != null) {

            mainLinear.findViewWithTag("razorpay").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    init("razorpay");
                    gatewayDialog.dismiss();

                }
            });
        }
        if (mainLinear.findViewWithTag("cashfree") != null) {

            mainLinear.findViewWithTag("cashfree").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    init("cashfree");
                    gatewayDialog.dismiss();

                }
            });
        }

        if (mainLinear.findViewWithTag("payu") != null) {

            mainLinear.findViewWithTag("payu").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    init("payu");
                    gatewayDialog.dismiss();

                }
            });
        }
        if (mainLinear.findViewWithTag("webUpiApp") != null) {
            mainLinear.findViewWithTag("webUpiApp").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    upilay.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                    title.setText("Enter UPI ID");


//                    gatewayDialog.dismiss();

//                    AlertDialog.Builder enterUpiId = new AlertDialog.Builder(AddfundActivity.this);
//                    enterUpiId.setTitle("Enter UPI ID");
//
//                    upiId = new EditText(AddfundActivity.this);
//                    upiId.setHint("user@upiprovider");
//
//                    enterUpiId.setView(upiId);
//                    enterUpiId.setCancelable(false);
//
//                    enterUpiId.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            init("webUpi");
//
//
//                        }
//                    });
//
//                    enterUpiId.setNegativeButton("Cancel", null);
//
//                    enterUpiId.show();


                }
            });
        }

        gatewayDialog = paymentGatewayDialog.create();

        gatewayDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {

                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return false;

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (scrollView != null && scrollView.getVisibility() == View.VISIBLE) {
                        gatewayDialog.dismiss();
                        Log.e("in", "1");
                        return true;
                    } else if (upilay != null && upilay.getVisibility() == View.VISIBLE) {
                        Log.e("in", "2");
                        upilay.setVisibility(View.GONE);
                        if (scrollView != null)
                            scrollView.setVisibility(View.VISIBLE);
                        return true;
                    } else {
                        Log.e("in", "3");
                        gatewayDialog.dismiss();

                        return false;
                    }

                } else
                    return false;
            }
        });

        gatewayDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(AddFundNewActivity.this, R.drawable.orange_border_white_fill_bg));

        gatewayDialog.show();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(AddFundNewActivity.this, mBinding.layoutFund);

    }

    public void onClickInputAmount(Integer value) {
        //Log.d("Clicked ","ok"+value);
        String amount = "" + value;
        mBinding.editAmount.setText(amount);
    }

    private Boolean validation() {

        int amount = 0;

        try {
            if (mBinding.editAmount.getText() != null) {
                amount = Integer.parseInt(mBinding.editAmount.getText().toString());
            }
        } catch (Exception e) {
            amount = 0;
        }

        if (amount < 1) {

            return false;
        } else {

            return true;
        }


    }


    private void init(String type) {
//        progressHelper = new ProgressHelper(this);

        mid = Utility.getGatewayMid(this);
        cusId = "noValue";


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("amount", mAmount);
        map1.put("activityName", activityName);
        map1.put("urid", urid);
        map1.put("location", Utility.getLatitude(AddFundNewActivity.this) + "," + Utility.getLongitude(AddFundNewActivity.this));


        if (type.equals("response")) {
            map1.put("gateway", Utility.getGatewayName(this));
            map1.put("orderId", Utility.getGatewayOrderid(this));
            map1.put("response", response);
            String request = Utility.mapWrapper(this, map1);
            paymentResponseRequest(request);
        } else if (type.equals("webUpi")) {

            map1.put("type", "webUpi");
            if (upiId != null)
                map1.put("vpa", upiId.getText().toString());


            String request = Utility.mapWrapper(this, map1);
            webUpiResponseRequest(request);


        } else {
            map1.put("type", type);
            String request = Utility.mapWrapper(this, map1);
            paymentRequest(request);
        }
    }

    private void webUpiResponseRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();


            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_ADDFUNDGATEWAY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            progressHelper.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.optString("resCode").equals(Constant.CODE_200)) {

                                    showTimmerDialog(jsonObject.getJSONObject("data").optString("orderId"));

                                } else
                                    Utility.showToastLatest(AddFundNewActivity.this, jsonObject.getString("resText"), "ERROR");
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }


                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void paymentResponseRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();


            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_ADDFUND_GATEWAY_RESPONSE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result, responseManager, "response");

                            //Log.d("TEST","request - "+result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void paymentRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();


            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_ADDFUNDGATEWAY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result, responseManager, "");

                            //Log.d("TEST","request - "+result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseResponse(String result, ResponseManager responseManager, String type) {
        if (!isDestroyed()) {
            progressHelper.dismiss();

            if (Utility.isServerRespond(result/*, responseManager*/)) {


                //Log.d("TEST","output  responseed "+result);
                try {
                    GetAddFundResponse response = new Gson().fromJson(result, GetAddFundResponse.class);

                    if (response.getResCode().equals(Constant.CODE_200)) {
                        if (type.equals("response")) {
                            //Log.d("Response ","received ");
                            Utility.showToastLatest(AddFundNewActivity.this,
                                    response.getResText(), "SUCCESS");
                            mBinding.textViewBalance.setText("Rs. " + response.getData().getBal());
                            Utility.setBalance(this, response.getData().getBal());
                            String mBal = response.getData().getAmount() + "";
                            Utility.showNewBalance(mBal, AddFundNewActivity.this);

                            if (activityName.equals("RechargeProcessActivity")) {
                                Intent intent = new Intent();
                                intent.putExtra("Added", true);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }

                        } else {
                            Utility.setGatewayName(this, response.getData().getGateway());
                            Utility.setGatewayOrderid(this, response.getData().getOrderId());

                            //Log.d("Received ", checkSum);
                            if (response.getData().getGateway().equals("paytm")) {
                                PaytmPGService service = PaytmPGService.getProductionService();
                                HashMap<String, String> params = new HashMap<>();
                                params.put("MID", response.getData().getMid());
                                params.put("ORDER_ID", response.getData().getOrderId());
                                params.put("CUST_ID", response.getData().getCusId());
                                params.put("CHANNEL_ID", response.getData().getChannelId());
                                params.put("TXN_AMOUNT", response.getData().getAmount());
                                params.put("WEBSITE", response.getData().getWebsite());

                                params.put("CALLBACK_URL", response.getData().getCbUrl());

                                params.put("INDUSTRY_TYPE_ID", response.getData().getIndType());
                                params.put("CHECKSUMHASH", response.getData().getCheckSum());

                                PaytmOrder order = new PaytmOrder(params);

                                service.initialize(order, null);
                                service.startPaymentTransaction(this, true, true,
                                        this);
                            } else if (response.getData().getGateway().equals("hyptoupi") || response.getData().getGateway().equals("razorpayupi") || response.getData().getGateway().equals("upi") || response.getData().getGateway().equals("bharatpe")) {
                                payUsingUpi(response.getData().getAmount(), response.getData().getUpiId(), "FORPAY RECHARGE", response.getData().getOrderId(), response.getData().getGateway());
                            } else if (response.getData().getGateway().equals("razorpay")) {
                                /*
                                Intent intent = new Intent(AddfundActivity.this, Razorpay.class);

                                intent.putExtra("webName", "privacy");
                                //startActivity(intent);
                                startActivityForResult(intent, 300);


                                 */
                                final Activity activity = this;

                                final Checkout co = new Checkout();

                                co.setKeyID(response.getData().getMid());


                                try {
                                    JSONObject options = new JSONObject();
                                    options.put("name", response.getData().getCusId());
                                    options.put("description", "FORPAY RECHARGE");
                                    //You can omit the image option to fetch the image from dashboard
                                    options.put("image", response.getData().getWebsite());
                                    options.put("currency", "INR");
                                    options.put("amount", response.getData().getAmount());
                                    options.put("order_id", response.getData().getCheckSum());

                                    JSONObject preFill = new JSONObject();
                                    preFill.put("email", response.getData().getEmail());
                                    preFill.put("contact", response.getData().getCusId());


                                    options.put("prefill", preFill);

                                    co.open(activity, options);
                                } catch (Exception e) {
                                    Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                                            .show();
                                    e.printStackTrace();
                                }
                            } else if (response.getData().getGateway().equals("cashfree")) {

                                CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
                                cfPaymentService.setOrientation(0);

                                if (response.getData().getIntentType().equals("upiIntent")) {
                                    cfPaymentService.upiPayment(AddFundNewActivity.this, getInputParams(response),
                                            response.getData().getCheckSum(), "PROD");
                                } else {
                                    cfPaymentService.doPayment(AddFundNewActivity.this,
                                            getInputParams(response),
                                            response.getData().getCheckSum(),
                                            "PROD",
                                            "#784BD2",
                                            "#FFFFFF",
                                            false);

                                }


                            } else if (response.getData().getGateway().equals("payu")) {

                                PayUPaymentParams.Builder builder = new PayUPaymentParams.Builder();
                                builder.setAmount(response.getData().getAmount())
                                        .setIsProduction(true)
                                        .setProductInfo(response.getData().getOrderId())
                                        .setKey(response.getData().getChannelId())
                                        .setPhone(response.getData().getCusId())
                                        .setTransactionId(response.getData().getOrderId())
                                        .setFirstName(response.getData().getUserName())
                                        .setEmail(response.getData().getEmail())
                                        .setSurl("https://forpay.in/paymentSuccess.php")
                                        .setFurl("https://forpay.in/paymentFailed.php")
                                        .setUserCredential(response.getData().getMid() + ":" + response.getData().getCusId());

                                PayUPaymentParams payUPaymentParams = builder.build();


                                PayUCheckoutPro.open(
                                        this,
                                        payUPaymentParams,
                                        new PayUCheckoutProListener() {

                                            @Override
                                            public void onPaymentSuccess(Object response) {
                                                //Cast response object to HashMap
                                                HashMap<String, Object> result = (HashMap<String, Object>) response;
                                                String payuResponse = (String) result.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE);
                                                String merchantResponse = (String) result.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE);


                                                response = payuResponse + "<!>" + merchantResponse;


                                                init("response");
                                            }

                                            @Override
                                            public void onPaymentFailure(Object response) {
                                                //Cast response object to HashMap
                                                HashMap<String, Object> result = (HashMap<String, Object>) response;
                                                String payuResponse = (String) result.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE);
                                                String merchantResponse = (String) result.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE);

                                                response = payuResponse + "<!>" + merchantResponse;


                                                init("response");
                                            }

                                            @Override
                                            public void onPaymentCancel(boolean isTxnInitiated) {
                                            }

                                            @Override
                                            public void onError(ErrorResponse errorResponse) {
                                                String errorMessage = errorResponse.getErrorMessage();
                                            }

                                            @Override
                                            public void setWebViewProperties(@Nullable WebView webView, @Nullable Object o) {
                                                //For setting webview properties, if any. Check Customized Integration section for more details on this
                                            }

                                            @Override
                                            public void generateHash(HashMap<String, String> valueMap, PayUHashGenerationListener hashGenerationListener) {
                                                String hashName = valueMap.get(PayUCheckoutProConstants.CP_HASH_NAME);
                                                String hashData = valueMap.get(PayUCheckoutProConstants.CP_HASH_STRING);
                                                if (!TextUtils.isEmpty(hashName) && !TextUtils.isEmpty(hashData)) {
                                                    //Do not generate hash from local, it needs to be calculated from server side only. Here, hashString contains hash created from your server side.
                                                    String hash = response.getData().getCheckSum();
                                                    HashMap<String, String> dataMap = new HashMap<>();
                                                    dataMap.put(hashName, hash);
                                                    hashGenerationListener.onHashGenerated(dataMap);
                                                }
                                            }
                                        }
                                );


                            } else {
                                Utility.showToastLatest(AddFundNewActivity.this, "No gateway available", "ERROR");
                            }
                        }


                    } else {
                        Utility.showToastLatest(AddFundNewActivity.this,
                                response.getResText(), "ERROR");
                    }
                } catch (Exception e) {
                    //Log.d("TEST","output  ex "+e.toString());

                    // Utility.showToast(this, "Data not received "+e.toString(),"");
                }

            } else {
                //Utility.showToast(this, getString(R.string.server_not_responding),"");
                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }


        }
    }

    private Map<String, String> getInputParams(GetAddFundResponse response) {

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = response.getData().getMid();
        String orderId = response.getData().getOrderId();
        String orderAmount = response.getData().getAmount();
        String orderNote = response.getData().getOrderId();
        String customerName = response.getData().getUserName();
        String customerPhone = response.getData().getCusId();
        String customerEmail = response.getData().getEmail();

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        params.put(PARAM_NOTIFY_URL, response.getData().getCbUrl());
        if (response.getData().getPaymentMethod().equals("upi")) {
            params.put(PARAM_PAYMENT_OPTION, "upi");
            params.put(PARAM_UPI_VPA, response.getData().getUpiId());// Put correct upi vpa here
        } else if (response.getData().getPaymentMethod().equals("wallet")) {
            params.put(PARAM_PAYMENT_OPTION, "wallet");
            params.put(PARAM_WALLET_CODE, response.getData().getVar1());
        }

        return params;
    }

    void payUsingUpi(String amount, String upiId, String name, String note, String gatewayName) {

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .appendQueryParameter("tr", Utility.getGatewayOrderid(this))
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // select if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Utility.showToastLatest(AddFundNewActivity.this, "No UPI app found, please install one to continue", "ERROR");
        }

    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {

        response = razorpayPaymentID;

        init("response");
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response1) {

        response = response1 + "&" + "resultCode=" + code;
        Log.d("response ", response1);

        init("response");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            //Log.d("Chatlist", "Data is " + data + " result code " + resultCode);
            String dataRes = "";

            Bundle bundle = data.getExtras();


            if (bundle != null) {
                Set<String> keys = bundle.keySet();
                Iterator<String> it = keys.iterator();

                while (it.hasNext()) {
                    String key = it.next();
                    dataRes += "" + key + "=" + bundle.get(key) + "&";


                }

            }

            response = data.getStringExtra("response") + "&" + "resultCode=" + resultCode + "&resultData=" + dataRes;

            init("response");
        } else {
            Utility.showToastLatest(AddFundNewActivity.this, "Transaction Failed , Request Code " + requestCode + " Result code " + resultCode, "FAILED");
        }

    }

    @Override

    public void onTransactionResponse(Bundle bundle) {
        Utility.showToastLatest(AddFundNewActivity.this, "Your payment is successfull", "SUCCESS");
        response = bundle.toString();
        try {
            byte[] dataRes = response.getBytes("UTF-8");
            response = Base64.encodeToString(dataRes, Base64.DEFAULT);
            // Log.d("response encode  ",response);
        } catch (Exception e) {
            //Log.d("Error encode ",e.toString());
        }
        init("response");


        //Log.d("Bundle is ",bundle.toString());


    }

    @Override
    public void networkNotAvailable() {
        Utility.showToastLatest(AddFundNewActivity.this, "Network not available", "FAILED");

        response = "Network not available-FAILED";
        try {
            byte[] dataRes = response.getBytes("UTF-8");
            response = Base64.encodeToString(dataRes, Base64.DEFAULT);
            // Log.d("response encode  ",response);
        } catch (Exception e) {
            //Log.d("Error encode ",e.toString());
        }
        init("response");


    }

    @Override
    public void clientAuthenticationFailed(String msg) {
        Utility.showToastLatest(AddFundNewActivity.this, "Authentication failed from server", "FAILED");

        response = "Authentication failed from server-FAILED";
        try {
            byte[] dataRes = response.getBytes("UTF-8");
            response = Base64.encodeToString(dataRes, Base64.DEFAULT);
            // Log.d("response encode  ",response);
        } catch (Exception e) {
            //Log.d("Error encode ",e.toString());
        }
        init("response");
    }

    @Override
    public void someUIErrorOccurred(String msg) {
        Utility.showToastLatest(AddFundNewActivity.this, "UI error occurred", "FAILED");

        response = "UI error occurred-FAILED";
        try {
            byte[] dataRes = response.getBytes("UTF-8");
            response = Base64.encodeToString(dataRes, Base64.DEFAULT);
            // Log.d("response encode  ",response);
        } catch (Exception e) {
            //Log.d("Error encode ",e.toString());
        }
        init("response");
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Utility.showToastLatest(AddFundNewActivity.this, "Error on loading web page", "FAILED");

        response = "Error on loading web page-FAILED";
        try {
            byte[] dataRes = response.getBytes("UTF-8");
            response = Base64.encodeToString(dataRes, Base64.DEFAULT);
            // Log.d("response encode  ",response);
        } catch (Exception e) {
            //Log.d("Error encode ",e.toString());
        }
        init("response");
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Utility.showToastLatest(AddFundNewActivity.this, "Back button pressed , Transaction cancelled ", "FAILED");
        response = "Back button pressed , Transaction cancelled-FAILED";
        try {
            byte[] dataRes = response.getBytes("UTF-8");
            response = Base64.encodeToString(dataRes, Base64.DEFAULT);
            // Log.d("response encode  ",response);
        } catch (Exception e) {
            //Log.d("Error encode ",e.toString());
        }
        init("response");
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Utility.showToastLatest(AddFundNewActivity.this, "Transaction Cancelled", "FAILED");
        response = "Transaction Cancelled-FAILED";
        try {
            byte[] dataRes = response.getBytes("UTF-8");
            response = Base64.encodeToString(dataRes, Base64.DEFAULT);
            // Log.d("response encode  ",response);
        } catch (Exception e) {
            //Log.d("Error encode ",e.toString());
        }
        init("response");
    }

    private Dialog timmerDialog;
    private CircularTimerView progressBar;

    private void showTimmerDialog(String orderId) {

        View timmerView = LayoutInflater.from(AddFundNewActivity.this).inflate(R.layout.timmer_layout, null);


        timmerView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timmerDialog != null)
                    timmerDialog.dismiss();
            }
        });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddFundNewActivity.this);
        alertDialog.setView(timmerView);

        alertDialog.setCancelable(false);

        progressBar = timmerView.findViewById(R.id.progress_circular);
        progressBar.setProgress(0);

        timmerDialog = alertDialog.create();
        timmerDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(AddFundNewActivity.this, R.drawable.orange_border_white_fill_bg));

        timmerDialog.show();

        progressBar.setCircularTimerListener(new CircularTimerListener() {
            @Override
            public String updateDataOnTick(long remainingTimeInMs) {

                prefs = AddFundNewActivity.this.getSharedPreferences(
                        "AutoSms", Context.MODE_PRIVATE);
                int second = (int) Math.ceil((remainingTimeInMs / 1000.f));

                if (second % 5 == 0) {

                    int timeNow = (int) ((System.currentTimeMillis() / 1000) % 3600);
                    int pastTime = prefs.getInt("time", 0);
                    int difference = (3600 + timeNow - pastTime) % 3600;

                    if (difference > 4) {
                        checkPaymentStatus(orderId);
                        Log.d("AddFundActivity ", " second " + second + " diff " + difference);

                        prefs.edit().putInt("time", timeNow).apply();
                    }
                }

                return getDurationString(second);
            }

            @Override
            public void onTimerFinished() {
                if (timmerDialog != null)
                    timmerDialog.dismiss();
                Utility.showToastLatest(AddFundNewActivity.this, "Timeout -could not get any response from user.", "ERROR");
                progressBar.setPrefix("");
                progressBar.setSuffix("");
                progressBar.setText("Time out!");
            }
        }, 5, TimeFormatEnum.MINUTES, 10);

        progressBar.startTimer();


    }

    private void checkPaymentStatus(String orderId) {


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("activityName", activityName);
        map1.put("type", "webUpi");
        map1.put("orderId", orderId);

        String request = Utility.mapWrapper(this, map1);

        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_ADDFUNDSTATUS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.optString("status").equals("SUCCESS")) {

                                    if (progressBar != null)
                                        progressBar.stopTimer();

                                    if (timmerDialog != null)
                                        timmerDialog.dismiss();
                                    mBinding.textViewBalance.setText("Rs. " + jsonObject.getString("bal"));
//                                    Utility.setBalance(AddfundActivity.this,jsonObject.getString("bal"));
                                    Utility.showToastLatest(AddFundNewActivity.this, jsonObject.optString("resText"), "SUCCESS");

                                    if (getIntent().getStringExtra("from").equals("RechargeProcessActivity")) {
                                        Intent intent = new Intent();
                                        intent.putExtra("Added", true);
                                        intent.putExtra("amount", mAmount);
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    }

                                }
//                                else if(jsonObject.optString("status").equals("REJECTED")){
//                                    if (timmerDialog!=null)
//                                        timmerDialog.dismiss();
//                                    mBinding.textViewBalance.setText(jsonObject.optString("bal"));
//                                    Utility.showToastLatest(AddfundActivity.this,jsonObject.optString("resText"),"ERROR");
//                                }
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }


                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    private String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

}