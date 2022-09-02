package in.forpay.activity.bbps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.ActivityBbpsRaiseComplaintBinding;
import in.forpay.model.BbpsStatusModel;
import in.forpay.model.response.GetBbpsStatusResponse;
import in.forpay.model.response.GetRequestFundResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class RaiseComplaint extends AppCompatActivity {

    private ActivityBbpsRaiseComplaintBinding mBinding;
    private ProgressHelper progressHelper;
    private String mIssueType = "";
    private String mIssueTypeDetails="";
    private ArrayList<String> complaintlist = new ArrayList<>();
    private ArrayAdapter<String> dataAdapter;
    private HashMap<String,String> typeofcomplain = new HashMap<>();

    private ArrayList<GetBbpsStatusResponse.Issues> issueList = new ArrayList<>();
    private Activity activity;

    public static RaiseComplaint newInstance() {
        return new RaiseComplaint();
    }

    public RaiseComplaint() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = RaiseComplaint.this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bbps_raise_complaint);

        Utility.changeStatusBarColor(RaiseComplaint.this,R.color.white);

        init();
    }



    public void bbpsStatusRequest(){

       Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));

        String request = Utility.mapWrapper(this,map1);



        requestBbpsStatus(request);

    }


    private void requestBbpsStatus(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_BBPS_STATUS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseRequestBbpsStatus(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }

    private void parseRequestBbpsStatus(String result) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                initUI(jsonObject.getJSONArray("inputField"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("ress",result);
        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(activity, ServerNotResponseActivity.class));
        }
    }


    private void initUI(JSONArray inputfields){
        Type userListType = new TypeToken<ArrayList<BbpsStatusModel>>(){}.getType();

        ArrayList<BbpsStatusModel> fields =new Gson().fromJson(inputfields.toString(), userListType);

        for (BbpsStatusModel field : fields){
            if(field.getInputType().equals("numeric") || field.getInputType().equals("alpha") || field.getInputType().equals("alphanumeric"))
            {

                View base = LayoutInflater.from(this).inflate(R.layout.text_field_material, null);


                TextInputLayout textInputLayout = base.findViewById(R.id.TextInputLayout);//new TextInputLayout(this, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
                textInputLayout.setGravity(Gravity.CENTER);
                textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                textInputLayout.setBoxCornerRadii(5, 5, 5, 5);
                TextInputEditText editText = base.findViewById(R.id.TextInputEditText);//new TextInputEditText(emailTextInputLayout.getContext());

                editText.setTag(field.getName());

                if(Integer.parseInt(field.getMinLength())>0){
                    textInputLayout.setHint(field.getPlaceHolder()+"*");
                }else
                    textInputLayout.setHint(field.getPlaceHolder());

                if(!field.getValue().isEmpty() || !field.getValue().toString().equals("")){
                    editText.setText(field.getValue());
                    editText.setEnabled(false);
                }



                if (field.getInputType().equals("numeric")) {
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (field.getInputType().equals("alpha")){
                    editText.setFilters(new InputFilter[] {Utility.getEditTextFilter(),Utility.filterMaxLenght(field.getMaxLength())});
                }


                if (!field.getInputType().equals("alpha"))
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(field.getMaxLength()))});
                if (!field.getLine().equals("singleLine")) {
                    editText.setSingleLine(false);
                    editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                } else
                    editText.setSingleLine(true);

                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                mBinding.mainLinear.addView(textInputLayout);
            }
            else if(field.getInputType().equals("select"))
            {


                LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                titleParams.setMargins(8,32,8,0);

                TextView title = new TextView(activity);
                title.setText(field.getPlaceHolder());
                title.setLayoutParams(titleParams);

                SearchableSpinner spinner = new SearchableSpinner(new ContextThemeWrapper(activity, R.style.spinner_style), null, 0);

                spinner.setTitle(field.getPlaceHolder());

                spinner.setTag(field.getName());


                for (BbpsStatusModel.ValueArray v: field.getValueArray()){
                    complaintlist.add(v.getValue());
                    typeofcomplain.put(v.getValue(),v.getType());
                }

                dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, complaintlist);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(dataAdapter);

                mBinding.mainLinear.addView(title);
                mBinding.mainLinear.addView(spinner);

            }

            else if (field.getInputType().equals("date"))
            {

                View base = LayoutInflater.from(this).inflate(R.layout.text_field_material, null);


                TextInputLayout TextInputLayout = base.findViewById(R.id.TextInputLayout);//new TextInputLayout(this, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);

                TextInputLayout.setGravity(Gravity.CENTER);
                TextInputLayout.setHint(field.getPlaceHolder());
                TextInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                TextInputLayout.setBoxCornerRadii(5, 5, 5, 5);
                TextInputEditText edittext = base.findViewById(R.id.TextInputEditText);//new TextInputEditText(emailTextInputLayout.getContext());

                edittext.setTag(field.getName());

                edittext.setFocusable(false);
                edittext.setClickable(true);

                final Calendar myCalendar = Calendar.getInstance();


                if(Integer.parseInt(field.getMinLength())>0){
                    TextInputLayout.setHint(field.getPlaceHolder()+"*");
                }else
                    TextInputLayout.setHint(field.getPlaceHolder());

                if(!field.getValue().isEmpty() || !field.getValue().equals("")){
                    edittext.setText(field.getValue());
                    edittext.setEnabled(false);
                }
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        if(field.getValue().isEmpty() || field.getValue().equals("")){
                            String myFormat = "yyyy/MM/dd"; //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                            edittext.setText(sdf.format(myCalendar.getTime()));
                        }else {
                            edittext.setText(field.getValue());
                        }
                    }

                };

                edittext.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(RaiseComplaint.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                mBinding.mainLinear.addView(TextInputLayout);

            }

        }

        Button confirm = findViewById(R.id.btnconfirm);

        confirm.setText("Make Complaint");
        confirm.setVisibility(View.VISIBLE);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Map<String,String> map1 = new HashMap<>();

                map1.put("token",Utility.getToken(RaiseComplaint.this)); // key
                map1.put("imei",Utility.getImei(RaiseComplaint.this)); // imei
                map1.put("versionCode",Utility.getVersionCode(RaiseComplaint.this)); // version code
                map1.put("os", Utility.getOs(RaiseComplaint.this));




                for (BbpsStatusModel field: fields){
                    View addedView =mBinding.mainLinear.findViewWithTag(field.getName());

                    if (addedView!=null) {
                        if (addedView instanceof TextInputEditText) {

                            if (((TextInputEditText) addedView).getText().toString().length() < Integer.parseInt(field.getMinLength())) {
                                Toasty.error(RaiseComplaint.this, "Please input valid " + field.getPlaceHolder()).show();
                                return;

                            } else

                            map1.put(field.getName(), ((TextInputEditText) addedView).getText().toString());

                        }
                        else if (addedView instanceof SearchableSpinner) {

                            String complain = ((SearchableSpinner) addedView).getSelectedItem().toString();

                            if (complain.equals(field.getPlaceHolder())) {
                                Toasty.error(RaiseComplaint.this,"Please Select a reason first!").show();
                                return;
                            } else

                            map1.put(field.getName(), typeofcomplain.get(complain));

                        }
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //list.forEach(pram -> Log.e("param",pram));

                }

                raiseComplain(map1);


            }
        });


    }


    private void raiseComplain(Map<String,String> map1) {
        progressHelper.show();

        String request = Utility.mapWrapper(RaiseComplaint.this,map1);


        Log.e("request",request);

        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_RAISE_COMPLAINT, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {
                    progressHelper.dismiss();
                    Log.e("res",result);
//                    if(!result.isEmpty()) {
//                        try {
//                            JSONObject obj = new JSONObject(result);
//                            if (obj.getString("resCode").equals(Constant.CODE_200)) {
//
//                            }
//                            else{
//                                Utility.showToastLatest(RaiseComplaint.this,obj.getString("resText"),"ERROR");
//                            }
//                        }
//                        catch (Exception e1){
//                            Utility.showToastLatest(RaiseComplaint.this,"Wrong response "+e1.toString(),"ERROR");
//                        }
//
//                    }

                }
            });
        }
    }



    /**
     * Click on choose bank
     */
//    public void onClickIssues() {
//        chooseIssues();
//    }

    /**
     * Click on request fund button
     */
//    public void onClickRequestFund() {
//        if (validation()) {
//            // key||imei||version||token||amount||reference number||transaction date
//            // ||your account number||method||to Account||remark
//            ArrayList<String> list = new ArrayList<>();
//            list.add("key="+Utility.getToken(activity)); // key
//            list.add("imei="+Utility.getDeviceIMEI(activity)); // imei
//            list.add("versionCode="+Utility.getVersionCode(activity)); // version code
//
//            list.add("transactionId="+mBinding.editTextTransactionId.getText().toString().trim()); // amount
//            list.add("transactionDate="+mBinding.editTextTxnDate.getText().toString().trim()); // reference number
//            list.add("description="+mBinding.editTextTxnIssue.getText().toString().trim());
//
//            list.add("type="+mIssueType); // method
//
//            String request = Utility.dataWrapper(list);
//
//            requestFund(request);
//        }
//    }

    private void init() {

//        mBinding.btnRequestNow.setOnClickListener(v -> onClickRequestFund());
//        mBinding.selectViewIssue.setOnClickListener(v -> onClickIssues());

        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
        progressHelper = new ProgressHelper(activity);

        bbpsStatusRequest();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(activity,mBinding.fundLayout);
    }


    /**
     * Choose bank
     */
//    private void chooseIssues() {
//        if (issueList != null && issueList.size() > 0) {
//
//            final ArrayList<String> listType = new ArrayList<>();
//            final ArrayList<String> listValue = new ArrayList<>();
//            for (GetBbpsStatusResponse.Issues issues : issueList) {
//                listValue.add(issues.getValue());
//                listType.add(issues.getType());
//            }
//
//            final String[] array = listValue.toArray(new String[listValue.size()]);
//            AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
//            mBuilder.setTitle("Choose Issue");
//            mBuilder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    mIssueType = listType.get(i);
//                    mBinding.selectViewIssue.setText(listValue.get(i));
//
//                    dialogInterface.dismiss();
//                }
//            });
//
//            AlertDialog mDialog = mBuilder.create();
//            mDialog.show();
//        } else {
//            Utility.showToast(activity, "Something went wrong try after sometime","");
//        }
//
//    }

    /**
     * Validation for all fields
     */
//    private boolean validation() {
//
//        String transactionId = mBinding.editTextTransactionId.getText().toString().trim();
//        String txnDate = mBinding.editTextTxnDate.getText().toString().trim();
//
//
//        if (TextUtils.isEmpty(transactionId)) {
//            Utility.showToast(activity, "Please enter transactionId","");
//            return false;
//        } else if (TextUtils.isEmpty(txnDate)) {
//            Utility.showToast(activity, "Please enter txnDate ","");
//            return false;
//        }
//        return true;
//    }

    /**
     * Refresh UI
     */
    private void refreshUI() {


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


}
