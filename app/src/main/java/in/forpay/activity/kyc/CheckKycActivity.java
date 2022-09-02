package in.forpay.activity.kyc;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import in.forpay.R;
import in.forpay.activity.shop.LocationAddActivity;
import in.forpay.databinding.ActivityCheckKycBinding;
import in.forpay.model.KycData;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class CheckKycActivity extends AppCompatActivity {


    ActivityCheckKycBinding binding;
    ProgressHelper progressHelper;
    Activity activity;
    ArrayList<String> final_list = new ArrayList<>();
    ArrayList<String> arealist = new ArrayList<>();
    ArrayAdapter<String> dataAdapter;
    EditText selectLocation;
    String latitude="",longitude="",address="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_check_kyc);
        activity=this;
        progressHelper=new ProgressHelper(this);
        getKycUpdate();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        
    }

    private void getKycUpdate() {
        progressHelper.show();

        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(this)); // key
        map1.put("imei",Utility.getImei(this)); // imei
        map1.put("versionCode",Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this,map1);

        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_KYC, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {
                    progressHelper.dismiss();
                    parseKycResponseData(result);
                }
            });
        }
    }

    private void parseKycResponseData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int resCode = jsonObject.getInt("resCode");
            String resMessage = jsonObject.getString("resText");
            initUi(jsonObject.getJSONArray("kycData"));
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUi(JSONArray kycdata){

        ArrayList<KycData> kycDataArrayList = new ArrayList<>();

        for(int i=0;i<kycdata.length();i++){



            LinearLayout secondaryLay = new LinearLayout(CheckKycActivity.this);

            secondaryLay.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            secondaryLay.setOrientation(LinearLayout.HORIZONTAL);
            secondaryLay.setPadding(0,(int) getResources().getDimension(R.dimen._5sdp),0,(int) getResources().getDimension(R.dimen._5sdp));

            try {


                KycData kycData = new Gson().fromJson(String.valueOf(kycdata.getJSONObject(i)), KycData.class);
                kycDataArrayList.add(kycData);



                if(kycData.getInputType().equals("numeric") || kycData.getInputType().equals("alpha") || kycData.getInputType().equals("alphanumeric")) {

                    View base = LayoutInflater.from(this).inflate(R.layout.text_field_material, null);


                    TextInputLayout textInputLayout = base.findViewById(R.id.TextInputLayout);//new TextInputLayout(this, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
                    textInputLayout.setGravity(Gravity.CENTER);
                    textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                    textInputLayout.setBoxCornerRadii(5, 5, 5, 5);
                    TextInputEditText editText = base.findViewById(R.id.TextInputEditText);//new TextInputEditText(emailTextInputLayout.getContext());

                    editText.setTag(kycData.getName());

                    if(Integer.parseInt(kycData.getMinLength())>0){
                        textInputLayout.setHint(kycData.getPlaceHolder()+"*");
                    }else
                        textInputLayout.setHint(kycData.getPlaceHolder());

                    if(!kycData.getValue().isEmpty() || !kycData.getValue().toString().equals("")){
                        editText.setText(kycData.getValue());
                        editText.setEnabled(false);
                        editText.setTextColor(getResources().getColor(R.color.hint_text));
                    }



                    if (kycData.getInputType().equals("numeric")) {
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    } else if (kycData.getInputType().equals("alpha")){
                        editText.setFilters(new InputFilter[] {Utility.getEditTextFilter(),Utility.filterMaxLenght(kycData.getMaxLength())});
                    }


                    if (!kycData.getInputType().equals("alpha"))
                        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(kycData.getMaxLength()))});
                    if (!kycData.getLine().equals("singleLine")) {
                        editText.setSingleLine(false);
                        editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                    } else
                        editText.setSingleLine(true);

                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    binding.mainLinear.addView(textInputLayout);
                }
                else if (kycData.getInputType().equals("radio")){

                    RadioGroup radioGroup = new RadioGroup(this);
                    radioGroup.setOrientation(LinearLayout.HORIZONTAL);
                    radioGroup.setTag(kycData.getName());
                    radioGroup.setGravity(Gravity.CENTER);


                    for (int pos=0;pos< kycData.getValueArray().length;pos++) {

                        RadioButton radioButton = new RadioButton(this);
                        radioButton.setText(kycData.getValueArray()[pos]);
                        switch (pos) {
                            case 0:
                                radioButton.setId(R.id.male);
                                break;
                            case 1:
                                radioButton.setId(R.id.female);
                                break;
                            case 2:
                                radioButton.setId(R.id.other);
                                break;
                        }
                        if(!kycData.getValue().isEmpty() || !kycData.getValue().equals("")) {
                            if(radioButton.getText().toString().equals(kycData.getValue()))
                                radioButton.setChecked(true);
                            radioButton.setTextColor(getResources().getColor(R.color.hint_text));

                        }
                        radioGroup.addView(radioButton);
                    }




                    LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0,30,0,30);
                    radioGroup.setLayoutParams(layoutParams);


                    binding.mainLinear.addView(radioGroup);

                }
                else if (kycData.getInputType().equals("date"))
                {

                    View base = LayoutInflater.from(this).inflate(R.layout.text_field_material, null);


                    TextInputLayout TextInputLayout = base.findViewById(R.id.TextInputLayout);//new TextInputLayout(this, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);

                    TextInputLayout.setGravity(Gravity.CENTER);
                    TextInputLayout.setHint(kycData.getPlaceHolder());
                    TextInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                    TextInputLayout.setBoxCornerRadii(5, 5, 5, 5);
                    TextInputEditText edittext = base.findViewById(R.id.TextInputEditText);//new TextInputEditText(emailTextInputLayout.getContext());

                    edittext.setTag(kycData.getName());

                    edittext.setFocusable(false);
                    edittext.setClickable(true);

                    final Calendar myCalendar = Calendar.getInstance();


                    if(Integer.parseInt(kycData.getMinLength())>0){
                        TextInputLayout.setHint(kycData.getPlaceHolder()+"*");
                    }else
                        TextInputLayout.setHint(kycData.getPlaceHolder());

                    if(!kycData.getValue().isEmpty() || !kycData.getValue().equals("")){
                        edittext.setText(kycData.getValue());
                        edittext.setEnabled(false);
                        edittext.setTextColor(getResources().getColor(R.color.hint_text));
                    }
                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            if(kycData.getValue().isEmpty() || kycData.getValue().equals("")){
                                String myFormat = "yyyy/MM/dd"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                                edittext.setText(sdf.format(myCalendar.getTime()));
                            }else {
                                edittext.setText(kycData.getValue());
                            }
                        }

                    };

                    edittext.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            new DatePickerDialog(CheckKycActivity.this, date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });
                    binding.mainLinear.addView(TextInputLayout);

                }
                else if(kycData.getInputType().equals("select")){



                    Spinner spinner = new Spinner(this);

                    spinner.setPrompt(kycData.getPlaceHolder());

                    spinner.setTag(kycData.getName());


                    if (!kycData.getValue().isEmpty() || !kycData.getValue().toString().equals("")){
                        arealist.add(kycData.getValue());

                    }else
                        arealist.add("Area Based on Pin");

                    dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arealist);

                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(dataAdapter);

                    LinearLayout.LayoutParams layoutParams =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    LinearLayout.LayoutParams layoutParamsSpinner =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    layoutParams.setMargins(20,20,20,20);

                    spinner.setLayoutParams(layoutParamsSpinner);

//                    spinner.setPadding(10,10,10,10);

                    LinearLayout linearLayout = new LinearLayout(this);

                    linearLayout.setLayoutParams(layoutParams);

                    linearLayout.setPadding(0,20,0,20);

                    linearLayout.setBackground(getResources().getDrawable(R.drawable.bg_spinner));

                    linearLayout.addView(spinner);

                    linearLayout.setGravity(Gravity.CENTER);
                    binding.mainLinear.addView(linearLayout);

                }

                else if (kycData.getInputType().equals("map")){

                    View mapselector = LayoutInflater.from(CheckKycActivity.this).inflate(R.layout.map_selector,null);
                    selectLocation = mapselector.findViewById(R.id.selectLocation);
                    ImageView locationImage = mapselector.findViewById(R.id.locationImg);

                    if (kycData.getValue().isEmpty())
                        selectLocation.setHint(kycData.getPlaceHolder());
                    else {
                        selectLocation.setText(kycData.getValue());
                        selectLocation.setTextColor(getResources().getColor(R.color.hint_text));

                    }
                    selectLocation.setTag(kycData.getName());

                    selectLocation.setFocusable(false);
                    selectLocation.setClickable(true);


                    locationImage.setOnClickListener(view -> {
                        if (kycData.getValue().isEmpty())
                            openAddLocation();
                    });

                    selectLocation.setOnClickListener(view -> {
                        if (kycData.getValue().isEmpty())
                            openAddLocation();
                    });

                    binding.mainLinear.addView(mapselector);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        TextInputEditText pincode = binding.mainLinear.findViewWithTag("pincode");

        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(pincode.getText().toString().length()==6){
                    getAreaFromPin(pincode.getText().toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        Button confirm = findViewById(R.id.btnconfirm); //new Button(activity);

        confirm.setText("Confirm");
        confirm.setVisibility(View.VISIBLE);
//        confirm.setTextColor(activity.getResources().getColor(R.color.orange));

//        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        relativeParams.addRule(RelativeLayout.BELOW);
//        relativeParams.setMargins(12,0,12,10);



        confirm.setBackground(activity.getResources().getDrawable(R.drawable.orange_border_white_fill_bg));


//        binding.mainLay.addView(confirm, relativeParams);
        
        
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> list = new ArrayList<>();
                list.add("key=" + Utility.getToken(CheckKycActivity.this));
                list.add("imei=" + Utility.getDeviceIMEI(CheckKycActivity.this));
                list.add("versionCode=" + Utility.getVersionCode(CheckKycActivity.this));
                for (KycData kycData: kycDataArrayList){
                    View addedView =binding.mainLay.findViewWithTag(kycData.getName());

                    if (addedView!=null) {
                        if (addedView instanceof TextInputEditText) {
                            Log.e("value of textfield minl", ((TextInputEditText) addedView).getText().toString() + " " + kycData.getMinLength());

                            if (((TextInputEditText) addedView).getText().toString().length() < Integer.parseInt(kycData.getMinLength())) {
                                Toasty.error(CheckKycActivity.this, "Please input valid " + kycData.getPlaceHolder()).show();

                                return;

                            } else
                                list.add(kycData.getName() + "=" + ((TextInputEditText) addedView).getText().toString());

                        }
                        else if (addedView instanceof RadioGroup) {

                            String gender = "";

                            if (((RadioGroup) addedView).getCheckedRadioButtonId() == R.id.male)
                                gender = "Male";
                            else if (((RadioGroup) addedView).getCheckedRadioButtonId() == R.id.female)
                                gender = "Female";
                            else if (((RadioGroup) addedView).getCheckedRadioButtonId() == R.id.other)
                                gender = "Other";

                            Log.e("value of radio", gender);
                            list.add(kycData.getName() + "=" + gender);
                        }
                        else if (addedView instanceof Spinner) {

                            String areaa = ((Spinner) addedView).getSelectedItem().toString();

                            if (areaa.equals("Area Based on Pin")) {
                                return;
                            } else
                                list.add(kycData.getName() + "=" + areaa);

                        }
                        else if (addedView instanceof EditText) {
                            Log.e("value of textfield minl", ((EditText) addedView).getText().toString() + " " + kycData.getMinLength());

                            if (((EditText) addedView).getText().toString().length() < Integer.parseInt(kycData.getMinLength())) {
                                Toasty.error(CheckKycActivity.this, "Please input valid " + kycData.getPlaceHolder()).show();
                                return;

                            } else {
                                String shopadd = latitude+","+longitude+" | "+Utility.getLatitude(CheckKycActivity.this)+","+Utility.getLongitude(CheckKycActivity.this)
                                        +" | "+address;
                                list.add(kycData.getName() + "=" + shopadd);
                            }

                        }
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    list.forEach(pram -> Log.e("param",pram));
                }
                updateKyc(list);


            }
        });
        

    }

    private void getAreaFromPin(String pincode) {
        progressHelper.show();
        arealist.clear();

        Log.e("pinc",pincode);

        ArrayList<String> list = new ArrayList<>();
        list.add("key=" + Utility.getToken(this));
        list.add("imei=" + Utility.getDeviceIMEI(this));
        list.add("versionCode=" + Utility.getVersionCode(this));
        list.add("pincode="+pincode);

        String request = Utility.dataWrapper(list);

        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_PINCODE, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {
                    progressHelper.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("pinArray");

                        for (int i=0;i<jsonArray.length();i++){

                            arealist.add(jsonArray.getJSONObject(i).optString("name").trim());

                        }
                        dataAdapter.notifyDataSetChanged();

                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                }
            });
        }
    }

    private void updateKyc(ArrayList<String> list) {
        progressHelper.show();


        String request = Utility.dataWrapper(list);

        Log.e("request",request);

        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, Constant.METHOD_KYC_UPDATE, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {
                    progressHelper.dismiss();
                    Log.e("res",result);
                    if(!result.isEmpty()) {
                        try {
                            JSONObject obj = new JSONObject(result);
                            if (obj.getString("resCode").equals(Constant.CODE_200)) {

                                Intent intent =new Intent(CheckKycActivity.this,AddKycActivity.class);
                               startActivity(intent);
                            }
                            else{
                                Utility.showToastLatest(CheckKycActivity.this,obj.getString("resText"),"ERROR");
                            }
                        }
                        catch (Exception e1){
                            Utility.showToastLatest(CheckKycActivity.this,"Wrong response "+e1.toString(),"ERROR");
                        }

                    }

                }
            });
        }

    }

    private void openAddLocation() {
        startActivityForResult(new Intent(activity, LocationAddActivity.class),Constant.ACTIVITY_RESULT_ADD_LOCATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode==Constant.ACTIVITY_RESULT_ADD_LOCATION){

                if (data!=null){
                    //String city = data.getStringExtra(Constant.LOCATION_CITY);
                    //String subLocality = data.getStringExtra(Constant.LOCATION_SUB_LOCALITY);
//                    address = data.getStringExtra(Constant.LOCATION_ADDRESS);
                    latitude=data.getStringExtra(Constant.LOCATION_LATITUDE);
                    longitude=data.getStringExtra(Constant.LOCATION_LONGITUDE);
                    address=data.getStringExtra(Constant.LOCATION_ADDRESS);

                    if (selectLocation!=null)
                        selectLocation.setText(data.getStringExtra(Constant.LOCATION_ADDRESS));
                }
            }
        }
    }

}