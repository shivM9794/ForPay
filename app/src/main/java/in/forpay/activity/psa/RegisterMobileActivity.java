package in.forpay.activity.psa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.RechargeProcessActivity;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.ActivityRegisterPsaAgentBinding;
import in.forpay.model.response.GetPanAgentResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import in.forpay.util.psaListAdapter;

public class RegisterMobileActivity extends AppCompatActivity {

    private ActivityRegisterPsaAgentBinding mBinding;
    private ProgressHelper progressHelper;
    private String mOTP = "";
    private String mPin,couponNumber="";
    Boolean isPsaAvailable=false;
    String psaId="";
    String[] psaAnswer;
    String[] psaQuestion;
    ExpandableListAdapter listAdapter;
    HashMap <String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register_psa_agent);
        mBinding.setActivity(this);
        psaId=getIntent().getStringExtra("psaId");
        psaQuestion=getIntent().getStringArrayExtra("psaQuestion");
        psaAnswer=getIntent().getStringArrayExtra("psaAnswer");


        if(psaAnswer!=null && psaQuestion!=null){


            listDataChild = new HashMap<String, List<String>>();

            for (int i =0; i<psaQuestion.length;i++){

                listDataChild.put(psaQuestion[i], Collections.singletonList(psaAnswer[i]));

            }



            listAdapter = new psaListAdapter(this, this, Arrays.asList(psaQuestion), listDataChild);

            mBinding.expandableListView.setAdapter(listAdapter);

        }

        mBinding.llPsaIdRegister.setVisibility(View.VISIBLE);
        mBinding.llPsaId.setVisibility(View.GONE);
        if(psaId.length()>6){
            mBinding.llPsaIdRegister.setVisibility(View.GONE);
            mBinding.llPsaId.setVisibility(View.VISIBLE);
            mBinding.textViewPsaId.setText(psaId);
            isPsaAvailable=true;
        }

        init();

        mBinding.registerAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRegister(false);
            }
        });

        mBinding.BuyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            addRegisterDialog("buyCoupon");
            }
        });

        mBinding.forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetPassword();
            }
        });
    }

    /**
     * Click on back button
     */
    public void onClickBack() {
        onBackPressed();
    }

    /**
     * Click on register button
     */

    public void addRegisterDialog(String dialogType){

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        if(dialogType==""){
            dialog.setContentView(R.layout.dialog_psa_register);
            TextView infoTextView = dialog.findViewById(R.id.infoTextView);
            if(infoTextView!=null) {
                infoTextView.setText(Utility.getPsaAmount(this) + " " + getString(R.string.label_psa_confirmation));
            }
            TextView textViewAgree = dialog.findViewById(R.id.textViewAgree);
            textViewAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean agree=true;
                    onClickRegister(agree);
                    dialog.dismiss();
                }
            });
        }
        else{
            dialog.setContentView(R.layout.dialog_buy_psacoupan);
            TextView textViewBuyCouponn = dialog.findViewById(R.id.textViewBuyCouponn);
            textViewBuyCouponn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editViewNumberCoupon = dialog.findViewById(R.id.editViewNumberCoupon);
                    EditText editViewPin = dialog.findViewById(R.id.editViewPin);
                    couponNumber=editViewNumberCoupon.getText().toString();
                    String pin=editViewPin.getText().toString();
                    mPin=pin;

                    onClickBuyCoupon(couponNumber,pin);
                    editViewPin.setText("");
                    dialog.dismiss();
                }
            });
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.show();
    }

    public void resetPassword(){


        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(this)); // key
        map1.put("imei",Utility.getImei(this)); // imei
        map1.put("versionCode",Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("mobile",Utility.getUsername(this));
        map1.put("psaId",psaId);


        String request = Utility.mapWrapper(this,map1);

        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_PSA_AGENT_PASSWORD_RESET, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponseReset(result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }

    }

    private void parseResponseReset(String result) {
        if (!isDestroyed()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetPanAgentResponse response = new Gson().fromJson(result, GetPanAgentResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    Utility.showToastLatest(this,response.getResText(),"SUCCESS");

                }

                else {
                    Utility.showToastLatest(this, response.getResText(),"ERROR");

                }
            } else {

                //Utility.showToast(this, getString(R.string.server_not_responding),"");
                startActivity(new Intent(this, ServerNotResponseActivity.class));

            }
        }
    }

    public void onClickBuyCoupon(String couponNumber,String pin){



        Map<String,String> map1 = new HashMap<>();
        map1.put("token",Utility.getToken(this)); // key
        map1.put("imei",Utility.getImei(this)); // imei
        map1.put("versionCode",Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("mobile", Utility.getUsername(this));
        map1.put("operatorId","139"); // operatorId
        map1.put("extraAmount","");
        map1.put("pin",pin); // pin
        map1.put("opValue1",couponNumber);


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

    }

    private void parseResponseRecharge(String result) {
        if (!isDestroyed()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRechargeValidateResponse response = new Gson().fromJson(result,GetRechargeValidateResponse.class);

                if(response.getResCode().equals(Constant.CODE_200)){



                    Bundle bundle = new Bundle();
                    bundle.putString("outputJson", response.getData().getOutputJson());
                    bundle.putString("uniqId", response.getData().getUniqId());
                    bundle.putString("serviceType", response.getData().getType());
                    bundle.putString("offerDetails", "");
                    bundle.putString("pin", mPin);
                    bundle.putString("amount", response.getData().getBillAmount());
                    bundle.putString("selectedMode", "");
                    bundle.putString("mobile", response.getData().getMobile());
                    bundle.putString("operatorId", response.getData().getOperatorId());
                    bundle.putString("extraData", response.getData().getExtraData());
                    bundle.putString("coupon", "");
                    bundle.putString("couponId", "");
                    bundle.putParcelable(Constant.INSUFFICIENTDATA, response.getData().getInsufficientData());
                    bundle.putParcelable(Constant.POPUPDATA, response.getData().getPopupData());


                    Utility.openCheckOutActivity(this, response.getData().getType(), bundle);




                }
                else{
                    Utility.showToast(this, response.getResText(), response.getResCode());
                }


            } else {
                //Utility.showToast(this, getString(R.string.server_not_responding), "");
                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }
        }
    }
    public void onClickRegister(Boolean agree) {
        mOTP=mBinding.editTextOtp.getText().toString();

        if(!mOTP.equals("") && !agree){
            //Log.d("clicked","yes ");

            addRegisterDialog("");
        }
        else {

            mPin="0000";
            Map<String,String> map1 = new HashMap<>();
            map1.put("token",Utility.getToken(this)); // key
            map1.put("imei",Utility.getImei(this)); // imei
            map1.put("versionCode",Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));
            map1.put("mobile", Utility.getUsername(this));
            map1.put("otp",mOTP); // pin
            map1.put("operatorId","192"); // operatorId
            map1.put("pin",mPin); // operatorId


            String request = Utility.mapWrapper(this,map1);
            customerRegister(request);
        }

    }

    private void init() {
        progressHelper = new ProgressHelper(this);
        mBinding.editTextMobile.setText(Utility.getUsername(this));
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
        setToolbar();
    }

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
        DatePickerDialog dialog = new DatePickerDialog(this, R.style.DatePickerDialogTheme, date,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
        // Set min date limit
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    /**
     * Set toolbar
     */
    private void setToolbar() {
        mBinding.mainTitle.setText("Register Mobile Psa");
        if(isPsaAvailable){
            mBinding.mainTitle.setText("UTI Psa Agent");
        }
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
    }

    /**
     * Validation for all fields
     */

    /**
     * Customer register
     */
    private void customerRegister(String request) {
        Log.d("psaresult","request "+request);
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            //parseResponse(result);
                            parseResponseRecharge(result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
        Log.d("psaresult",""+result);
        if (!isDestroyed()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetPanAgentResponse response = new Gson().fromJson(result,
                        GetPanAgentResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    mBinding.layoutOtp.setVisibility(View.GONE);
                    mBinding.editTextOtp.setText("");

                    mBinding.adharLayoutLine.setVisibility(View.GONE);


                    Utility.showToast(this, response.getResText(),response.getResCode());
                    finish();
                }

                else if(response.getResCode().equals(Constant.CODE_178)){
                    mBinding.layoutOtp.setVisibility(View.VISIBLE);
                    mBinding.adharLayoutLine.setVisibility(View.VISIBLE);

                }

                else {
                    mBinding.editTextOtp.setText("");
                    Utility.showToast(this, response.getResText(),"");
                }
            } else {
                //Utility.showToast(this, getString(R.string.server_not_responding),"");
                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }
        }
    }
}
