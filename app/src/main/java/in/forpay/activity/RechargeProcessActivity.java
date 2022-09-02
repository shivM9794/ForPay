package in.forpay.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import in.forpay.R;
import in.forpay.activity.bbps.ComplaintStatusActivity;
import in.forpay.activity.shop.LocationAddActivity;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityRechargeProcess1Binding;
import in.forpay.fragment.RechargeHistoryFragment;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.model.request.RechargeHistory;
import in.forpay.model.response.GetRechargeNowResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class RechargeProcessActivity extends AppCompatActivity {

    TextView textViewRemark;
    TextView textViewMobile;
    LinearLayout textViewRemarkLabel;
    TextView textViewAmount;
    LinearLayout textViewAmountLabel, couponLayout;
    TextView textViewProcessPayment, textViewCoupon;
    ImageView favContactStar;

    TextView textViewPayAmount;
    LinearLayout textViewPayAmountLabel;

    TextView textViewCusPayAmount;
    LinearLayout textViewCusPayAmountLabel;

    TextView textViewProfit;
    LinearLayout textViewProfitLabel;

    TextView textViewServiceCharge;
    LinearLayout textViewServiceChargeLabel;

    TextView textViewCurrentBal;
    ImageView textViewBack, operatoriv;
    LinearLayout textViewCurrentBalLabel, LLWarning;
    private ProgressHelper progressHelper;
    private DatabaseHelper databaseHelper;
    private String amount, serviceCharge, starSelected = "";

    private String beneficiaryId = "";

    private ArrayList<MainRechargeModel.ServiceBean.ParamsBean> paramsBeanArrayList;
    private ArrayList<String> valueArrayList;

    private String topIconUrl = "";
    private String selectedMode = "";
    private String validateDetails = "";

    private ActivityRechargeProcess1Binding binding;

    private GetRechargeValidateResponse.PopupData popupData;

    private GetRechargeValidateResponse.InsufficientData insufficientData;

    private boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recharge_process1);
        LLWarning = findViewById(R.id.LLWarning);
        init();

        try {
            Utility.subscribeGcm(this);
        } catch (Exception e) {

        }

        paramsBeanArrayList = getIntent().getParcelableArrayListExtra(Constant.PARAMS_ARRAY_LIST);
        valueArrayList = getIntent().getStringArrayListExtra(Constant.VALUE_ARRAY_LIST);
        topIconUrl = getIntent().getStringExtra("topIconUrl");

        ImageView bbpsIcon = findViewById(R.id.bbps_icon);

        if (topIconUrl != null) {
            if (!topIconUrl.isEmpty()) {
                Glide.with(this)
                        .load(topIconUrl)
                        .into(bbpsIcon);
                bbpsIcon.setVisibility(View.VISIBLE);
            }
        }

        textViewBack = findViewById(R.id.textViewBack);
        textViewRemark = findViewById(R.id.textViewRemark);
        textViewMobile = findViewById(R.id.textViewMobile);
        //textViewRemarkLabel=findViewById(R.id.textViewRemarkLabel);
        //textViewRemarkLabel.setVisibility(View.GONE);


        //textViewAmount=findViewById(R.id.textViewAmount);
        //textViewAmountLabel=findViewById(R.id.textViewAmountLabel);
        //textViewAmountLabel.setVisibility(View.GONE);


        textViewProcessPayment = findViewById(R.id.textViewProcessPayment);
        favContactStar = findViewById(R.id.favContactStar);

        textViewCurrentBal = findViewById(R.id.textViewCurrentBal);
        operatoriv = findViewById(R.id.operatoriv);
        //textViewCurrentBalLabel=findViewById(R.id.textViewCurrentBalLabel);
        //textViewCurrentBalLabel.setVisibility(View.GONE);


        textViewPayAmount = findViewById(R.id.textViewPayAmount);
        //textViewPayAmountLabel=findViewById(R.id.textViewPayAmountLabel);
        //textViewPayAmountLabel.setVisibility(View.GONE);

        textViewCusPayAmount = findViewById(R.id.textViewCusPayAmount);
        //textViewCusPayAmountLabel=findViewById(R.id.textViewCusPayAmountLabel);
        //textViewCusPayAmountLabel.setVisibility(View.GONE);


        textViewProfit = findViewById(R.id.textViewProfit);
        //textViewProfitLabel=findViewById(R.id.textViewProfitLabel);
        //textViewProfitLabel.setVisibility(View.GONE);

        textViewServiceCharge = findViewById(R.id.textViewServiceCharge);
        //textViewServiceChargeLabel=findViewById(R.id.textViewServiceChargeLabel);
        //textViewServiceChargeLabel.setVisibility(View.GONE);

        couponLayout = findViewById(R.id.couponLayout);
        textViewCoupon = findViewById(R.id.textViewCoupon);

        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });


        amount = getIntent().getStringExtra("amount");
        if (getIntent().getStringExtra("selectedMode") != null) {
            selectedMode = getIntent().getStringExtra("selectedMode");
        }

        Bundle bundle = getIntent().getExtras();

        popupData = bundle.getParcelable(Constant.POPUPDATA);
        insufficientData = bundle.getParcelable(Constant.INSUFFICIENTDATA);
        final String beneficiaryId = getIntent().getStringExtra("beneficiaryId");
        final String mobile = getIntent().getStringExtra("mobile");
        final String operatorId = getIntent().getStringExtra("operatorId");
        final String creditUsed = getIntent().getStringExtra("creditUsed");
        final String bal = getIntent().getStringExtra("bal");
        final String service = getIntent().getStringExtra("service");
        final String billAmount = getIntent().getStringExtra("billAmount");
        final String billName = getIntent().getStringExtra("billName");
        final String beneficiaryAccountNumber = getIntent().getStringExtra("beneficiaryAccountNumber");
        final String beneficiaryName = getIntent().getStringExtra("beneficiaryName");
        final String type = getIntent().getStringExtra("type");
        final String pin = getIntent().getStringExtra("pin");
        final String extraAmount = getIntent().getStringExtra("extraAmount");
        final String dob = getIntent().getStringExtra("dob");
        final String opvalue1 = getIntent().getStringExtra("opValue1");
        final String opvalue2 = getIntent().getStringExtra("opValue2");
        final String opvalue3 = getIntent().getStringExtra("opValue3");
        final String opvalue4 = getIntent().getStringExtra("opValue4");
        final String opvalue5 = getIntent().getStringExtra("opValue5");
        final String circleId = getIntent().getStringExtra("circleId");
        final String customerPay = getIntent().getStringExtra("customerPay");
        final String profit = getIntent().getStringExtra("profit");
        final String coupon = getIntent().getStringExtra("coupon");
        final String couponId = getIntent().getStringExtra("couponId");
        validateDetails = getIntent().getStringExtra("validateDetails");
        starSelected = getIntent().getStringExtra("starSelected");
        serviceCharge = getIntent().getStringExtra("serviceCharge");

        if (starSelected.equals("yes")) {


            favContactStar.setImageResource(R.drawable.starselected);
        } else {

            favContactStar.setImageResource(R.drawable.star);
        }


        try {
            Log.d("Chatlist", "No Exception ");
            if (billName != null) {
                if (billName.length() > 3) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(RechargeProcessActivity.this);
                    databaseHelper.insertContactTmp(mobile, billName, type);
                }
            }
        } catch (Exception e) {
            Log.d("Chatlist", "Exception " + e);
        }


        if (amount.isEmpty() || amount.equals("0")) {
            amount = billAmount;
        }
        String remark = "";
        remark = "Processing payment for consumer " + mobile + " of amount " + amount + " Rs";
        String amountText = "Amount " + amount + " Rs";


        if (type.equals("mobile")) {
            remark = "Processing recharge for mobile " + mobile + " of amount " + amount + " Rs";
            if (!billName.isEmpty()) {
                remark = "Processing recharge for mobile " + mobile + " of amount " + amount + " Rs , Consumer name is " + billName;
            }


        } else if (type.equals("dth")) {
            remark = "Processing recharge for dth " + mobile + " of amount " + amount + " Rs";
            if (!billName.isEmpty()) {
                remark = "Processing recharge for dth " + mobile + " of amount " + amount + " Rs , Consumer name is " + billName;
            }

        } else if (type.equals("postpaid")) {
            remark = "Processing recharge for postpaid " + mobile + " of amount " + amount + " Rs";
            if (!billName.isEmpty()) {
                remark = "Processing recharge for postpaid " + mobile + " of amount " + amount + " Rs , Consumer name is " + billName;
            }

        } else if (type.equals("electricity") || type.equals("gas") || type.equals("water") || type.equals("insurance") || type.equals("broadband")) {
            amountText = "Bill Amount " + amount + " Rs";
            remark = "Processing bill payment for consumer " + mobile + " of amount " + amount + " Rs";
            if (!billName.isEmpty()) {
                remark = "Processing bill payment for consumer " + mobile + " of amount " + amount + " Rs , Consumer name is " + billName;
            }

        } else if (type.equals("landline")) {
            remark = "Processing bill payment for landline " + mobile + " of amount " + amount + " Rs";
            if (!billName.isEmpty()) {
                remark = "Processing bill payment for landline " + mobile + " of amount " + amount + " Rs , Consumer name is " + billName;
            }

        } else if (type.equals("moneytransfer") || type.equals("fundtransfer")) {
            remark = "Fund transfer processing for account " + beneficiaryAccountNumber + " of amount " + amount + " Rs with Beneficiary Name " + beneficiaryName;


        } else if (type.equals("digitalgiftvoucher")) {
            remark = "Gift Voucher will be send on email " + beneficiaryAccountNumber + " of amount " + amount + " Rs";


        }

        textViewMobile.setText(mobile);

        if (!remark.isEmpty()) {

            //textViewRemark.setText(remark);
            Spannable spannable = new SpannableString(remark);
            try {
                if (billName != null) {
                    spannable.setSpan(new ForegroundColorSpan(Color.RED), remark.indexOf(billName), remark.indexOf(billName) + billName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (mobile != null) {
                    spannable.setSpan(new ForegroundColorSpan(Color.BLUE), remark.indexOf(mobile), remark.indexOf(mobile) + mobile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (amount != null) {
                    spannable.setSpan(new ForegroundColorSpan(Color.BLUE), remark.indexOf(amount), remark.indexOf(amount) + amount.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

                // textViewRemark.setText(spannable);
            } catch (Exception e) {
                // textViewRemark.setText(remark);
            }

            textViewRemark.setText(Html.fromHtml(validateDetails), TextView.BufferType.SPANNABLE);

            //textViewRemarkLabel.setVisibility(View.VISIBLE);
        }
        //textViewAmount.setText(amountText);
        //textViewAmountLabel.setVisibility(View.VISIBLE);
        textViewPayAmount.setText(creditUsed + " Rs");
        //textViewPayAmountLabel.setVisibility(View.VISIBLE);
        textViewCurrentBal.setText(bal);
        //textViewCurrentBalLabel.setVisibility(View.VISIBLE);
        textViewCusPayAmount.setText(customerPay + " Rs");
        //textViewCusPayAmountLabel.setVisibility(View.VISIBLE);
        textViewProfit.setText(profit + " Rs");
        //textViewProfitLabel.setVisibility(View.VISIBLE);
        if (!serviceCharge.equals("0.00") && !serviceCharge.equals("0")) {
            textViewServiceCharge.setText(serviceCharge + " Rs");

            //textViewServiceChargeLabel.setVisibility(View.VISIBLE);

        }

        if (coupon != null) {
            if (!coupon.isEmpty()) {
                couponLayout.setVisibility(View.VISIBLE);
                textViewCoupon.setText(coupon);
            }
        }

        Utility.imageLoader(RechargeProcessActivity.this, "https://api.forpay.in/image/operator/" + operatorId + ".png", operatoriv);

        favContactStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (starSelected.equals("yes")) {
                    starSelected = "";
                    Utility.showToast(RechargeProcessActivity.this, getString(R.string.title_removed_from_fav_contact), "ERROR");
                    favContactStar.setImageResource(R.drawable.star);
                } else {
                    starSelected = "yes";

                    Utility.showToastLatest(RechargeProcessActivity.this, getString(R.string.title_added_to_fav_contact), "SUCCESS");
                    favContactStar.setImageResource(R.drawable.starselected);
                }
            }
        });

        textViewProcessPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!isAdded && insufficientData != null && insufficientData.getInsufficientBalance().equals("yes"))
                    showInsufficientDialog();
                else {

                    Utility.getCurrentLocation(RechargeProcessActivity.this, false);
                    try {
                        DatabaseHelper databaseHelper = new DatabaseHelper(RechargeProcessActivity.this);
                        databaseHelper.insertContactTmp(mobile, billName, type);
                    } catch (Exception e) {

                    }

                    Map<String,String> map1 = new HashMap<>();

                    map1.put("token",Utility.getToken(RechargeProcessActivity.this)); // key
                    map1.put("imei",Utility.getImei(RechargeProcessActivity.this)); // imei
                    map1.put("versionCode",Utility.getVersionCode(RechargeProcessActivity.this)); // version code
                    map1.put("os", Utility.getOs(RechargeProcessActivity.this));
                    map1.put("starSelected", starSelected);


                    if (paramsBeanArrayList == null) {
                        map1.put("selectedMode", selectedMode);
                        map1.put("mobile" , mobile); // mobile
                        map1.put("operatorId" , operatorId); // operatorId
                        map1.put("amount" , amount); // amount
                        map1.put("pin" , pin); // pin
                        map1.put("opValue1" , opvalue1); // opvalue1
                        map1.put("opValue2" , opvalue2); // opvalue2
                        map1.put("opValue3" , opvalue3); // txn_date
                        map1.put("opValue4" , opvalue4);
                        map1.put("opValue5" , opvalue5);
                        map1.put("circleId" , circleId);
                        map1.put("dob" , dob);
                        map1.put("coupon" , coupon);
                        map1.put("couponId" , couponId);
                        map1.put("extraAmount" , extraAmount);
                        map1.put("gcmToken" , Utility.getGcmToken(RechargeProcessActivity.this));
                        map1.put("latitude" , Utility.getLatitude(RechargeProcessActivity.this));
                        map1.put("longitude" , Utility.getLongitude(RechargeProcessActivity.this));
                        try {
                            map1.put("beneficiaryId" , beneficiaryId);
                        } catch (Exception e) {

                        }
                    } else {
                        map1.put("selectedMode" , selectedMode);
                        map1.put("mobile" , mobile); // mobile
                        map1.put("operatorId",  operatorId); // operatorId
                        map1.put("amount" , amount); // amount
                        map1.put("pin" , pin); // pin
                        map1.put("circleId" , circleId);
                        map1.put("extraAmount" , extraAmount);
                        map1.put("coupon" , coupon);
                        map1.put("couponId" , couponId);
                        map1.put("gcmToken" , Utility.getGcmToken(RechargeProcessActivity.this));
                        try {
                            map1.put("beneficiaryId" , beneficiaryId);
                        } catch (Exception e) {

                        }
                        map1.put("latitude" , Utility.getLatitude(RechargeProcessActivity.this));
                        map1.put("longitude" , Utility.getLongitude(RechargeProcessActivity.this));

                        for (int i = 0; i < paramsBeanArrayList.size(); i++) {
                            map1.put(paramsBeanArrayList.get(i).getName() ,  valueArrayList.get(i));
                        }
                    }

                    String request = Utility.mapWrapper(RechargeProcessActivity.this,map1);
                    rechargeNow(request, operatorId);

                }

            }
        });


        if (popupData != null)
            showPopup();


    }


    /**
     * Recharge now
     */
    private void rechargeNow(String request, String operatorId) {
        if (Utility.isNetworkConnected(RechargeProcessActivity.this)) {
            progressHelper.show();
            if (operatorId.equals("94")) {
                QueryManager.getInstance().postRequest(RechargeProcessActivity.this,
                        Constant.METHOD_SEND_FUND, request, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {
                                parseResponse(result);
                            }
                        });
            } else {
                QueryManager.getInstance().postRequest(RechargeProcessActivity.this,
                        Constant.METHOD_RECHARGE_NOW, request, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {
                                parseResponse(result);
                            }
                        });
            }

        } else {
            Utility.showToast(RechargeProcessActivity.this, getString(R.string.internet_connect), "");
        }
    }


    /**
     * Parse response
     */
    private void parseResponse(String result) {
        if (!isDestroyed()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {

                GetRechargeNowResponse response = new Gson().fromJson(result, GetRechargeNowResponse.class);
                if (response.getResCode().equals(Constant.CODE_200) || response.getResCode().equals(Constant.CODE_201)) {
                    Utility.showToast(RechargeProcessActivity.this, response.getResText(), response.getResCode());
                    Utility.setBalance(RechargeProcessActivity.this, response.getData().getBal());

                    Intent intent = new Intent(RechargeProcessActivity.this, RechargeDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    intent.putExtra("orderId", response.getData().getOrderId());
                    intent.putExtra("remark", "");
                    intent.putExtra("viewType", "");

                    startActivity(intent);
                    finish();

                } else {
                    if (response.getData().getStatus().equals(Constant.FAILED)) {
                        if (!response.getData().getOrderId().equals("0")) {

                            Intent intent = new Intent(RechargeProcessActivity.this, RechargeDetailsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                            intent.putExtra("orderId", response.getData().getOrderId());
                            intent.putExtra("remark", response.getResText());

                            intent.putExtra("viewType", "");
                            startActivity(intent);
                            finish();
                        }
                    }
                    Utility.showToast(RechargeProcessActivity.this, response.getResText(), response.getResCode());
                }


            } else {
                //Utility.showToast(RechargeProcessActivity.this, getString(R.string.server_not_responding), "");
                startActivity(new Intent(this, ServerNotResponseActivity.class));
                RechargeHistoryFragment fragment = new RechargeHistoryFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, fragment);
                transaction.commitAllowingStateLoss();

            }
        }
    }


    private boolean isAssetExists(String pathInAssetsDir) {
        AssetManager assetManager = RechargeProcessActivity.this.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(pathInAssetsDir);
            if (null != inputStream) {
                return true;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return false;
    }

    private void init() {
        progressHelper = new ProgressHelper(RechargeProcessActivity.this);
        if (Utility.getDeviceScreenSize(this) <= 520) {
            //LLWarning.setVisibility(View.GONE);
        }


    }

    private void showPopup() {

        final Dialog dialog = new Dialog(RechargeProcessActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);

        TextView title = dialog.findViewById(R.id.title);

        LinearLayout linearLayout = dialog.findViewById(R.id.mainLinear);

        title.setText(popupData.getTitle());

        initPopUpUI(popupData.getDataList(), linearLayout);

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void initPopUpUI(ArrayList<GetRechargeValidateResponse.DataList> dataList, LinearLayout linearLayout) {


        for (int i = 0; i < dataList.size(); i++) {


            View secondLay = LayoutInflater.from(RechargeProcessActivity.this).inflate(R.layout.item_dialog_popup, null);

            TextView label = secondLay.findViewById(R.id.label);
            TextView value = secondLay.findViewById(R.id.value);

            label.setText(dataList.get(i).getLabel());
            value.setText(dataList.get(i).getValue());

            linearLayout.addView(secondLay);
        }


    }


    private void showInsufficientDialog() {

        if (insufficientData.getInsufficientBalance().equals("yes")) {

            String msg = "You don't have sufficient balance in your wallet. Require to add " + insufficientData.getAmount() + " more.";

            AlertDialog.Builder alert = new AlertDialog.Builder(RechargeProcessActivity.this);
            View wv = LayoutInflater.from(RechargeProcessActivity.this).inflate(R.layout.error_dialog, null, false);
            Button gotit = wv.findViewById(R.id.back_btn);
            TextView emoji = wv.findViewById(R.id.emoji);

            emoji.setText("\uD83D\uDCB8");

            gotit.setText("Add Now");
            TextView textView = wv.findViewById(R.id.error_msg);
            if (!msg.isEmpty())
                textView.setText(msg);
            alert.setView(wv);
            alert.setCancelable(false);
            final Dialog dialog = alert.create();

            dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(RechargeProcessActivity.this, R.drawable.orange_border_white_fill_bg));
            dialog.show();

            gotit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    openAddFund();
                }
            });
        }
    }

    private void openAddFund() {
        Intent intent = new Intent(RechargeProcessActivity.this, AddFundNewActivity.class);
        intent.putExtra("from", "RechargeProcessActivity");
        intent.putExtra("Amount", insufficientData.getAmount());
        if (insufficientData.getLocationRequired().equals("yes")) {

            if (Build.VERSION.SDK_INT >= 23) {

                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Utility.getLatLong(RechargeProcessActivity.this, false);
                        //Log.d(TAG, "checkPermission: onPermissionGranted");
                        Utility.getCurrentLocation(RechargeProcessActivity.this, true);
                        startActivityForResult(intent, 101);

                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        //Log.d(TAG, "checkPermission: onPermissionDenied");
                        Utility.showToast(RechargeProcessActivity.this, "Permission Denied", "");
                        Utility.getCurrentLocation(RechargeProcessActivity.this, false);
                        startActivityForResult(intent, 101);
                        //progressHelper.dismiss();
                    }
                };

                TedPermission.with(RechargeProcessActivity.this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                                "Please turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                        .check();
                //Log.d(TAG, "VERSION: Build.VERSION.SDK_INT >= 23");
            }


        } else
            startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                if (data != null) {

                    isAdded = data.getBooleanExtra("Added", false);

                    if (isAdded)
                        binding.textViewProcessPayment.performClick();
                }
            }
        }
    }

}
