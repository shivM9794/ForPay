package in.forpay.activity.moneytransfer;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.FragmentMoneyTransferBinding;
import in.forpay.model.response.GetAddBeneficiaryResponse;
import in.forpay.model.response.GetCustomerHistoryResponse;
import in.forpay.model.response.GetVerifyBeneficiaryResponse;
import in.forpay.model.response.GetVerifyIfscResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.GPSTracker;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class MoneyTransferActivity extends AppCompatActivity {

    /*911010032736304
    ifsc code - UTIB0000613*/

    private FragmentMoneyTransferBinding mBinding;
    private ProgressHelper progressHelper;
    private GetCustomerHistoryResponse mResponse;
    private boolean isFlag;
    private String mBeneficiaryId = "";
    private Activity activity;
    private String latitude = "", longitude = "";

    public static MoneyTransferActivity newInstance() {
        return new MoneyTransferActivity();
    }

    public MoneyTransferActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = MoneyTransferActivity.this;
        mBinding = DataBindingUtil.setContentView(activity, R.layout.fragment_money_transfer);
        init();
    }


    /**
     * Click on search
     */
    public void onClickSearch() {

        Utility.getCurrentLocation(this, true);
        if (Utility.getLatitude(this).isEmpty()) {
            Utility.showToast(activity, "To use this function you must need to allow location service", "");
        } else {
            if (validation()) {

                Map<String, String> map1 = new HashMap<>();

                map1.put("token", Utility.getToken(this)); // key
                map1.put("imei", Utility.getImei(this)); // imei
                map1.put("versionCode", Utility.getVersionCode(this)); // version code
                map1.put("os", Utility.getOs(this));
                map1.put("mobile", mBinding.editTextMobile.getText().toString().trim());
                map1.put("latitude", Utility.getLatitude(this));
                map1.put("longitude", Utility.getLongitude(this));
                String request = Utility.mapWrapper(this, map1);

                getCustomerDetail(request);
            }
        }
    }


    private void checkPermission() {

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                getLatLong();
                //Log.d(TAG, "checkPermission: onPermissionGranted");

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                //Log.d(TAG, "checkPermission: onPermissionDenied");
                Utility.showToast(activity, "Permission Denied", "");
                //progressHelper.dismiss();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                        "Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();
    }

    public void getLatLong() {
        final GPSTracker gps = new GPSTracker(activity);
        if (gps.canGetLocation()) {
            //Log.d(TAG, "LatLong: canGetLocation");
            latitude = Double.toString(gps.getLatitude());
            longitude = Double.toString(gps.getLongitude());


        } else {
            //Log.d(TAG, "LatLong: canNotGetLocation");
            //progressHelper.dismiss();
            gps.showSettingsAlert();
        }
    }

    /**
     * Click on add beneficiary
     */
    public void onClickAddBeneficiary() {
        addBeneficiaryDialog();
    }

    /**
     * Click on beneficiary list
     */
    public void onClickBeneficiaryList() {
        if (mResponse == null || mResponse.getData() == null
                || mResponse.getData().getBeneficiaryList() == null
                || mResponse.getData().getBeneficiaryList().size() == 0) {
            return;
        }
        Intent intent = new Intent(activity, BeneficiaryListActivity.class);
        intent.putParcelableArrayListExtra("list", mResponse.getData().getBeneficiaryList());
        intent.putExtra("mobile", mBinding.editTextMobile.getText().toString().trim());
        startActivity(intent);
    }

    private void init() {
        progressHelper = new ProgressHelper(activity);
        mBinding.btnSearch.setOnClickListener(v -> onClickSearch());
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
        mBinding.btnAddBackancy.setOnClickListener(v -> onClickAddBeneficiary());
        mBinding.btnBackancyList.setOnClickListener(v -> onClickBeneficiaryList());
        mBinding.btnAddBackancy2.setOnClickListener(v -> onClickAddBeneficiary());
        mBinding.btnBackancyList2.setOnClickListener(v -> onClickBeneficiaryList());

        mBinding.editTextMobile.setText(Utility.getUsername(this));
        mBinding.editTextMobile.setFocusable(false);

    }

    /**
     * Set data on UI
     */
    private void setData(GetCustomerHistoryResponse.Details details) {
        if (details == null) {
            return;
        }
        mBinding.linearLayoutDetail.setVisibility(View.VISIBLE);
        mBinding.textViewMobile.setText(mBinding.editTextMobile.getText().toString().trim());
        mBinding.textViewName.setText(details.getName());
        mBinding.textViewLimit.setText(details.getLimit());
        mBinding.textViewBalance.setText(details.getBalance());
    }

    /**
     * Show add beneficiary dialog
     */
    private void addBeneficiaryDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        if (!dialog.isShowing()) {
            dialog.setContentView(R.layout.dialog_add_beneficiary);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.show();

            final EditText editTextAccountName = dialog.findViewById(R.id.editTextAccountName);
            final EditText editTextMobile = dialog.findViewById(R.id.editTextMobile);
            final EditText editTextAccountNumber = dialog.findViewById(R.id.editTextAccountNumber);
            final EditText editTextIfscCode = dialog.findViewById(R.id.editTextIfscCode);
            final EditText editTextPin = dialog.findViewById(R.id.editTextPin);
            final EditText editTextOtp = dialog.findViewById(R.id.editTextOTP);
            final LinearLayout otp_layout = dialog.findViewById(R.id.otp_layout);
            final View viewLine = dialog.findViewById(R.id.viewLineOTP);

            TextView textViewAddNow = dialog.findViewById(R.id.textViewAddNow);
            TextView textViewVerifyNow = dialog.findViewById(R.id.textViewVerifyNow);

            textViewAddNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = editTextAccountName.getText().toString().trim();
                    String mobile = editTextMobile.getText().toString().trim();
                    String accNumber = editTextAccountNumber.getText().toString().trim();
                    String ifscCode = editTextIfscCode.getText().toString().trim();
                    String pin = editTextPin.getText().toString().trim();
                    String otp = editTextOtp.getText().toString().trim();
                    Log.d("TEST", "otp -" + otp);
                    if (otp == "" || otp.isEmpty() == true) {
                        isFlag = false;
                        //Log.d("TEST","false -"+otp);
                    } else {
                        isFlag = true;
                        //Log.d("TEST","true -"+otp);
                    }
                    if (validationAddBeneficiary(name, mobile, accNumber, ifscCode, pin, otp)) {
                        if (isFlag) {


                            Map<String, String> map1 = new HashMap<>();

                            map1.put("token", Utility.getToken(activity)); // key
                            map1.put("imei", Utility.getImei(activity)); // imei
                            map1.put("versionCode", Utility.getVersionCode(activity)); // version code
                            map1.put("os", Utility.getOs(activity));
                            map1.put("mobile", mBinding.editTextMobile.getText().toString().trim());
                            map1.put("otp", editTextOtp.getText().toString().trim());
                            map1.put("beneficiaryId", mBeneficiaryId);
                            String request = Utility.mapWrapper(activity, map1);
                            verifyBeneficiary(request, dialog);
                        } else {


                            Map<String, String> map1 = new HashMap<>();

                            map1.put("token", Utility.getToken(activity)); // key
                            map1.put("imei", Utility.getImei(activity)); // imei
                            map1.put("versionCode", Utility.getVersionCode(activity)); // version code
                            map1.put("os", Utility.getOs(activity));
                            map1.put("mobile", mBinding.editTextMobile.getText().toString().trim());
                            map1.put("beneficiaryName", editTextAccountName.getText().toString().trim());
                            map1.put("beneficiaryMobile", editTextMobile.getText().toString().trim());
                            map1.put("beneficiaryAccountNumber", editTextAccountNumber.getText().toString().trim());
                            map1.put("ifscCode", editTextIfscCode.getText().toString().trim()); // ifsccode
                            String request = Utility.mapWrapper(activity, map1);

                            addBeneficiary(request, otp_layout, viewLine);
                        }
                    }
                }
            });

            textViewVerifyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String accNumber = editTextAccountNumber.getText().toString().trim();
                    String ifscCode = editTextIfscCode.getText().toString().trim();
                    String pin = editTextPin.getText().toString().trim();

                    if (validationVerifyNow(accNumber, ifscCode, pin)) {

                        Map<String, String> map1 = new HashMap<>();

                        map1.put("token", Utility.getToken(activity)); // key
                        map1.put("imei", Utility.getImei(activity)); // imei
                        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
                        map1.put("os", Utility.getOs(activity));
                        map1.put("mobile", mBinding.editTextMobile.getText().toString().trim());
                        map1.put("accountNumber", accNumber);
                        map1.put("ifscCode", ifscCode);
                        map1.put("pin", pin);

                        String request = Utility.mapWrapper(activity, map1);

                        agreePopUp(request, editTextAccountName);
                    }

                }
            });
        }
    }

    /**
     * Show pop up for agree
     */
    private void agreePopUp(final String request, final EditText editText) {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_valid_ifsc);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView textViewAgree = dialog.findViewById(R.id.textViewAgree);

        textViewAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                verifyIfscCode(request, editText);
            }
        });

        dialog.show();
    }

    /**
     * Validation for all fields
     */
    private boolean validation() {
        String mobile = mBinding.editTextMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            Utility.showToast(activity, "Please enter customer number", "");
            return false;
        }
        return true;
    }

    /**
     * Validation for all fields of add beneficiary pop up
     */
    private boolean validationAddBeneficiary(String name, String mobile, String accNumber
            , String IfscCode, String pin, String otp) {

        if (TextUtils.isEmpty(name)) {
            Utility.showToast(activity, "Please enter a name", "");
            return false;
        } else if (TextUtils.isEmpty(mobile)) {
            Utility.showToast(activity, "Please enter a mobile number", "");
            return false;
        } else if (TextUtils.isEmpty(accNumber)) {
            Utility.showToast(activity, "Please enter a account number", "");
            return false;
        } else if (TextUtils.isEmpty(IfscCode)) {
            Utility.showToast(activity, "Please enter a IFSC code", "");
            return false;
        } else if (TextUtils.isEmpty(pin)) {
            Utility.showToast(activity, "Please enter a pin", "");
            return false;
        } else if (TextUtils.isEmpty(otp) && isFlag) {
            Utility.showToast(activity, "Please enter a otp", "");
            return false;
        }

        return true;
    }

    /**
     * Validation for verify now
     */
    private boolean validationVerifyNow(String accNumber, String IfscCode, String pin) {

        if (TextUtils.isEmpty(accNumber)) {
            Utility.showToast(activity, "Please enter a account number", "");
            return false;
        } else if (TextUtils.isEmpty(IfscCode)) {
            Utility.showToast(activity, "Please enter a IFSC code", "");
            return false;
        } else if (TextUtils.isEmpty(pin)) {
            Utility.showToast(activity, "Please enter a pin", "");
            return false;
        }
        return true;
    }

    /**
     * Add new data in recharge history table
     */
    /**
     * Get customer detail
     */
    private void getCustomerDetail(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_CUSTOMER_DETAIL, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseCustomerDetailResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    /**
     * Parse response
     */
    private void parseCustomerDetailResponse(String result) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            mResponse = new Gson().fromJson(result,
                    GetCustomerHistoryResponse.class);
            if (mResponse.getResCode().equals(Constant.CODE_200)) {
                Utility.showToast(activity, mResponse.getResText(), mResponse.getResCode());
                setData(mResponse.getData().getDetails());
            } else if (mResponse.getResCode().equals(Constant.CODE_123)) {
                Utility.showToast(activity, mResponse.getResText(), mResponse.getResCode());
                Intent intent = new Intent(activity, RegisterMobileActivity.class);
                intent.putExtra("mobile", mBinding.editTextMobile.getText().toString().trim());
                startActivity(intent);
            } else {
                Utility.showToast(activity, mResponse.getResText(), mResponse.getResCode());
            }
        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(this, ServerNotResponseActivity.class));
        }
    }

    /**
     * Add beneficiary before get otp
     */
    private void addBeneficiary(String request, final LinearLayout editText, final View viewLine) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_ADD_BENEFICIARY_B_OTP, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseAddBeneficiaryResponse(result, editText, viewLine);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    /**
     * Parse response
     */
    private void parseAddBeneficiaryResponse(String result, LinearLayout editText, View viewLine) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetAddBeneficiaryResponse response = new Gson().fromJson(result,
                    GetAddBeneficiaryResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {
                isFlag = true;
                mBeneficiaryId = response.getData().getBeneficiaryId();
                editText.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
                Utility.showToast(activity, response.getResText(), response.getResCode());
            } else {
                Utility.showToast(activity, response.getResText(), response.getResCode());
            }
        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(this, ServerNotResponseActivity.class));
        }
    }

    /**
     * Add beneficiary after get otp
     */
    private void verifyBeneficiary(String request, final Dialog dialog) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_ADD_BENEFICIARY_A_OTP, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseVerifyBeneficiaryResponse(result, dialog);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    /**
     * Parse response
     */
    private void parseVerifyBeneficiaryResponse(String result, Dialog dialog) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetVerifyBeneficiaryResponse response = new Gson().fromJson(result,
                    GetVerifyBeneficiaryResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {
                mBeneficiaryId = "";


                EditText editTextOtp2 = dialog.findViewById(R.id.editTextOTP);
                editTextOtp2.setText("");
                //dialog.dismiss();
                Utility.showToast(activity, response.getResText(), response.getResCode());
            } else {
                Utility.showToast(activity, response.getResText(), response.getResCode());
            }
        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(this, ServerNotResponseActivity.class));
        }
    }

    /**
     * Verify ifsc code
     */
    private void verifyIfscCode(String request, final EditText editText) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_VERIFY_IFSC_CODE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseVerifyIfscCodeResponse(result, editText);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    /**
     * Parse response
     */
    private void parseVerifyIfscCodeResponse(String result, EditText editText) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetVerifyIfscResponse response = new Gson().fromJson(result,
                    GetVerifyIfscResponse.class);
            String status = response.getData().getStatus();
            if (response.getResCode().equals(Constant.CODE_200) || (status.equalsIgnoreCase(Constant.SUCCESS)
                    || status.equalsIgnoreCase(Constant.PENDING)
                    || status.equalsIgnoreCase(Constant.FAILED))) {

                editText.setText(response.getData().getBeneficiaryName());
                Utility.showToast(activity, response.getResText(), response.getResCode());
            } else {
                Utility.showToast(activity, response.getResText(), response.getResCode());
            }
        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(this, ServerNotResponseActivity.class));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(activity, mBinding.linearLayoutSearch);
    }
}
