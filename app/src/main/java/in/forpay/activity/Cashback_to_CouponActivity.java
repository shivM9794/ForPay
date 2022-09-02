package in.forpay.activity;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.TransactionHistoryAdapter;
import in.forpay.databinding.ActivityCashbackToCoupon2Binding;
import in.forpay.model.response.GetProfileResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import in.forpay.webView.WebViewActivity;

public class Cashback_to_CouponActivity extends AppCompatActivity {


    private ActivityCashbackToCoupon2Binding binding;
    Activity activity;
    ProgressHelper progressHelper;
    Boolean showProgressbar = false;
    String TAG = "CashbackToCouponActivity";
    private TransactionHistoryAdapter adapter;
    private ArrayList<GetProfileResponse.IncentiveHistory> mList;
    int minteger = 1;
    double remainBal = 0;
    double remainingBal = 0;
    private static final int REQUEST_SELECT_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cashback_to_coupon2);
        activity = this;
        progressHelper = new ProgressHelper(this);

        init(true);
        basicMethods();
    }

    private void basicMethods() {
        binding.textViewBack.setOnClickListener(v -> {
            finish();
        });

        binding.questionIcon.setOnClickListener(view -> {
            openTos();
        });

        binding.textViewTos.setOnClickListener(view -> {
            openTos();
        });

        binding.idSubmit.setOnClickListener(view -> {
            if (remainingBal >= 0) {
                submit();
            } else {
                Utility.showToast(activity, "Remaining balance should be greater than 0", "ERROR");
            }

        });

        binding.increase.setOnClickListener(view -> {
            Log.d(TAG, "Increase clicked ");
            increaseInteger();
        });

        binding.minus.setOnClickListener(view -> {
            decreaseInteger();
            Log.d(TAG, "Minus clicked ");
        });

        binding.editTextAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    changeRemainBal();
                }

            }
        });


        binding.edittextQuantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {

                    changeRemainBal();
                }

            }
        });

        binding.contactIcon.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);


        });


    }


    private void changeRemainBal() {
        try {
            remainingBal = remainBal - (parseInt(binding.edittextQuantity.getText().toString()) * parseDouble(binding.editTextAmount.getText().toString()));
            binding.remainingBal.setText("" + remainingBal);
        } catch (Exception e) {
            Log.d(TAG, "cashback error  " + e.toString());
        }
    }

    private void openTos() {

        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", "https://forpay.in/cashbackPolicy.php?language" + Utility.getDefaultLanguage(activity));
        intent.putExtra("webName", "CashbackPolicy");
        startActivity(intent);
    }

    private void submit() {

        progressHelper.show();

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("quantity", binding.edittextQuantity.getText().toString());
        map1.put("method", "confirm");
        map1.put("couponAmount", binding.editTextAmount.getText().toString());
        map1.put("toMobile", binding.editTextMobile.getText().toString());
        map1.put("couponName", binding.editTextCouponName.getText().toString());
        map1.put("couponDescription", binding.editTextMessage.getText().toString());

        String request = Utility.mapWrapper(this, map1);


        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_CASHBACK_TO_COUPON, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {
                    progressHelper.dismiss();
                    parseSubmitResponseData(result);
                }
            });
        }

    }

    private void parseSubmitResponseData(String result) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            String resCode = jsonObject.getString("resCode");
            String resText = jsonObject.getString("resText");

            if (resCode.equals(Constant.CODE_200)) {
                Utility.showToastLatest(activity, resText, resCode);
                init(false);
            } else {
                Utility.showToastLatest(activity, resText, "ERROR");
            }

        } catch (Exception e) {
            Utility.showToastLatest(activity, e.toString(), "ERROR");
        }


    }

    private void init(Boolean showProgressbar) {

        if (showProgressbar) {
            progressHelper.show();

        }


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this, map1);


        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_PROFILE, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {

                    if (showProgressbar) {
                        progressHelper.dismiss();
                    }
                    parseCashbackResponseData(result);
                }
            });
        }
    }


    private void parseCashbackResponseData(String result) {

        GetProfileResponse response = new Gson().fromJson(result, GetProfileResponse.class);
//        response.getData().getName();
        if (response.getResCode().equals(Constant.CODE_200)) {
            String oldMobile = binding.editTextMobile.getText().toString();
            binding.edittextQuantity.setText(minteger + "");
            remainBal = parseDouble(response.getData().getCommissionBal());
            binding.textviewRupees.setText("Points " + response.getData().getCommissionBal());
            binding.editTextAmount.setText("10");
            if (oldMobile.equals("") || oldMobile.isEmpty()) {
                binding.editTextMobile.setText(response.getData().getMobile());

            }
        } else {
            Utility.showToastLatest(activity, response.getResText(), response.getResCode());

        }
        mList = response.getIncentiveHistory();
        setAdapter();

    }

    private void setAdapter() {

        binding.transactionRecycler.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new TransactionHistoryAdapter(activity, mList);
        binding.transactionRecycler.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.d("wjdfhgdjsf", "mobile " + data.getStringExtra("mobile"));
            binding.editTextMobile.setText(data.getStringExtra("mobile"));
        }

    }

    public void increaseInteger() {

        minteger = minteger + 1;
        display(minteger);
    }

    public void decreaseInteger() {

        if (minteger > 1) {
            minteger = minteger - 1;
            display(minteger);
        }
    }

    private void display(int number) {
        Log.d(TAG, "Display clicked " + number);
        binding.edittextQuantity.setText("" + number);
    }
}