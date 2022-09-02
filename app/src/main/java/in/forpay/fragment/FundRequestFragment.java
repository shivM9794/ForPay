package in.forpay.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.CheckHistoryActivity;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.FragmentFundRequestBinding;
import in.forpay.model.response.GetRequestFundResponse;
import in.forpay.model.response.GetRequestHistoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class FundRequestFragment extends Fragment {

    private FragmentFundRequestBinding mBinding;
    private ProgressHelper progressHelper;
    private String mMethod = "";
    private String mAccountNumber = "";
    private ArrayList<GetRequestHistoryResponse.Bank> bankList = new ArrayList<>();

    public static FundRequestFragment newInstance() {
        return new FundRequestFragment();
    }

    public FundRequestFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fund_request, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    /**
     * Click on select history
     */
    public void onClickHistory() {
        Intent intent = new Intent(getActivity(), CheckHistoryActivity.class);
        startActivity(intent);
    }

    /**
     * Click on select date
     */

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

            map1.put("token",Utility.getToken(getActivity())); // key
            map1.put("imei",Utility.getImei(getActivity())); // imei
            map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code
            map1.put("os", Utility.getOs(getActivity()));
            map1.put("referenceNumber" , mBinding.editTextReferenceNumber.getText().toString().trim());
            map1.put("method" , mMethod);
            map1.put("amount",mBinding.editTextAmount.getText().toString().trim()); // amount
            map1.put("toAccount",mAccountNumber); // to Account

            String request = Utility.mapWrapper(getActivity(),map1);




            requestFund(request);
        }
    }

    private void init() {
        progressHelper = new ProgressHelper(getActivity());



        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(getActivity())); // key
        map1.put("imei",Utility.getImei(getActivity())); // imei
        map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));

        String request = Utility.mapWrapper(getActivity(),map1);



        getHistory(request);
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
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, date,
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
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Choose an transfer method");
        mBuilder.setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


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
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle("Choose an bank");
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
            Utility.showToast(getActivity(), "Something went wrong try after sometime","");
        }

    }

    /**
     * Validation for all fields
     */
    private boolean validation() {

        String amount = mBinding.editTextAmount.getText().toString().trim();
        String referenceNumber = mBinding.editTextReferenceNumber.getText().toString().trim();

        if (TextUtils.isEmpty(amount)) {
            Utility.showToast(getActivity(), "Please enter amount","");
            return false;
        } else if (TextUtils.isEmpty(referenceNumber)) {
            Utility.showToast(getActivity(), "Please enter reference number","");
            return false;
        } else if (TextUtils.isEmpty(mAccountNumber)
                || mAccountNumber.equalsIgnoreCase("Choose Bank")) {
            Utility.showToast(getActivity(), "Please choose bank","");
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
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_REQUEST_FUND, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseRequestFundResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseRequestFundResponse(String result) {
        if (isVisible()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRequestFundResponse response = new Gson().fromJson(result, GetRequestFundResponse.class);
                if (response.getData().getResCode().equals(Constant.CODE_200)) {
                    Utility.showToast(getActivity(), response.getData().getResText(),response.getData().getResCode());
                    refreshUI();
                } else {
                    Utility.showToast(getActivity(), response.getData().getResText(),response.getData().getResCode());
                }
            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding),"");
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
            }
        }
    }

    /**
     * Get history
     */
    private void getHistory(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_REQUEST_FUND_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseGetHistoryResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseGetHistoryResponse(String result) {
        if (isVisible()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRequestHistoryResponse response = new Gson().fromJson(result, GetRequestHistoryResponse.class);
                if (response.getData().getResCode().equals(Constant.CODE_200)) {
                    bankList = response.getData().getBankList();
                    //Utility.showToast(getActivity(), response.getData().getResText());
                } else {
                    //Utility.showToast(getActivity(), response.getData().getResText());
                }
            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding));
            }
        }
    }
}
