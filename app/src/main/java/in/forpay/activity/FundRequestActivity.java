package in.forpay.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.FragmentFundRequestBinding;
import in.forpay.model.response.GetRequestFundResponse;
import in.forpay.model.response.GetRequestHistoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class FundRequestActivity extends AppCompatActivity {

    private FragmentFundRequestBinding mBinding;
    private ProgressHelper progressHelper;
    private String mMethod = "";
    private String mAccountNumber = "";
    private ArrayList<GetRequestHistoryResponse.Bank> bankList = new ArrayList<>();
    private Activity activity;

    public static FundRequestActivity newInstance() {
        return new FundRequestActivity();
    }

    public FundRequestActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = FundRequestActivity.this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_fund_request);
        init();
    }



    /**
     * Click on select history
     */
    public void onClickHistory() {
        Intent intent = new Intent(activity, CheckHistoryActivity.class);
        startActivity(intent);
    }


    /**
     * Click on select transfer method
     */
    public void onClickMethod() {
        selectMethod();
    }

    /**
     * Click on choose bank
     */
    public void onClickBank() {
        chooseBank();
    }

    /**
     * Click on request fund button
     */
    public void onClickRequestFund() {
        if (validation()) {


            Map<String,String> map1 = new HashMap<>();

            map1.put("token",Utility.getToken(activity)); // key
            map1.put("imei",Utility.getImei(activity)); // imei
            map1.put("versionCode",Utility.getVersionCode(activity)); // version code
            map1.put("os", Utility.getOs(activity));
            map1.put("amount",mBinding.editTextAmount.getText().toString().trim()); // amount
            map1.put("referenceNumber",mBinding.editTextReferenceNumber.getText().toString().trim()); // reference number

            map1.put("method",mMethod); // method
            map1.put("toAccount",mAccountNumber); // to Account
            String request = Utility.mapWrapper(this,map1);


            requestFund(request);
        }
    }

    private void init() {

        mBinding.btnRequestNow.setOnClickListener(v -> onClickRequestFund());
        mBinding.btnChooseBank.setOnClickListener(v -> onClickBank());

        mBinding.btnHistory.setOnClickListener(v -> onClickHistory());
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
        progressHelper = new ProgressHelper(activity);


        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(this)); // key
        map1.put("imei",Utility.getImei(this)); // imei
        map1.put("versionCode",Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this,map1);


        getHistory(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(activity,mBinding.fundLayout);
    }

    /**
     * Click on select time
     */
    private void selectDate(final TextView textView) {

        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; // your format
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                String date = sdf.format(calendar.getTime());
                textView.setText(date);
            }

        };
        DatePickerDialog dialog = new DatePickerDialog(activity, R.style.DatePickerDialogTheme, date,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
        // Set min date limit
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    /**
     * Select method
     */
    private void selectMethod() {
        final CharSequence[] list = {"NEFT", "IMPS", "UPI", "Same Bank Transfer"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        mBuilder.setTitle("Choose an transfer method");
        mBuilder.setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String method = (String) list[i];

                if (method.equalsIgnoreCase("Same Bank Transfer")) {
                    mMethod = "same";
                } else {
                    mMethod = method.toLowerCase();
                }

                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    /**
     * Choose bank
     */
    private void chooseBank() {
        if (bankList != null && bankList.size() > 0) {

            final ArrayList<String> list = new ArrayList<>();
            for (GetRequestHistoryResponse.Bank bank : bankList) {
                list.add(bank.getAccountNo());
            }

            final String[] array = list.toArray(new String[list.size()]);
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
            mBuilder.setTitle("Choose the bank");
            mBuilder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mAccountNumber = list.get(i);
                    mBinding.textViewBank.setText(mAccountNumber);

                    dialogInterface.dismiss();
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        } else {
            Utility.showToast(activity, "Something went wrong try after sometime","");
        }

    }

    /**
     * Validation for all fields
     */
    private boolean validation() {

        String amount = mBinding.editTextAmount.getText().toString().trim();
        String referenceNumber = mBinding.editTextReferenceNumber.getText().toString().trim();


        if (TextUtils.isEmpty(amount)) {
            Utility.showToast(activity, "Please enter amount","");
            return false;
        } else if (TextUtils.isEmpty(referenceNumber)) {
            Utility.showToast(activity, "Please enter reference number","");
            return false;
        }   else if (TextUtils.isEmpty(mAccountNumber)
                || mAccountNumber.equalsIgnoreCase("Choose Bank")) {
            Utility.showToast(activity, "Please choose bank","");
            return false;
        }
        return true;
    }

    /**
     * Refresh UI
     */
    private void refreshUI() {
        mMethod = "";
        mAccountNumber = "";
        mBinding.editTextAmount.setText("");
        mBinding.editTextReferenceNumber.setText("");

        mBinding.textViewBank.setText("Choose Bank");

    }

    /**
     * Request fund
     */
    private void requestFund(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_REQUEST_FUND, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseRequestFundResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseRequestFundResponse(String result) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRequestFundResponse response = new Gson().fromJson(result, GetRequestFundResponse.class);
                if (response.getData().getResCode().equals(Constant.CODE_200)) {
                    Utility.showToast(activity, response.getData().getResText(),response.getData().getResCode());
                    refreshUI();
                } else {
                    Utility.showToast(activity, response.getData().getResText(),response.getData().getResCode());
                }
            } else {
                //Utility.showToast(activity, getString(R.string.server_not_responding),"");
                startActivity(new Intent(activity, ServerNotResponseActivity.class));
            }
    }

    /**
     * Get history
     */
    private void getHistory(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_REQUEST_FUND_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseGetHistoryResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseGetHistoryResponse(String result) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRequestHistoryResponse response = new Gson().fromJson(result, GetRequestHistoryResponse.class);
                if (response.getData().getResCode().equals(Constant.CODE_200)) {
                    bankList = response.getData().getBankList();
                    //Utility.showToast(activity, response.getData().getResText());
                } else {
                    //Utility.showToast(activity, response.getData().getResText());
                }
            } else {
                //Utility.showToast(activity, getString(R.string.server_not_responding));
            }
    }
}
