package in.forpay.fragment.apesnewfragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.askjeffreyliu.flexboxradiogroup.FlexBoxSingleCheckableGroup;
import com.google.android.flexbox.FlexWrap;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import in.forpay.R;
import in.forpay.activity.aeps.StartAepsApiActivity;
import in.forpay.listener.RechargeNowListener;
import in.forpay.model.AepsModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import in.forpay.webView.WebViewActivity;


public class WithdrawalNewFragment extends Fragment {

    private View mainView;
    private Activity activity;
    private RelativeLayout mainLay;
    private LinearLayout mainLinear;
    private final HashMap<String, String> packageIDOf = new HashMap<>();
    private final HashMap<String, String> bankID = new HashMap<>();

    private BroadcastReceiver otgreceiver;
    private final ArrayList<String> final_list = new ArrayList<>();


    private final Map<String, String> map1 = new HashMap<>();

    private ProgressHelper progressHelper;
    private RadioButton checkedg;
    private String pidOptXML = null;
    private TextView tabletitle, resText;
    private TableLayout tableLayout;
    private String infoData = "";
    private String infoPid = "";
    HashMap<String, String> pidXmlOf = new HashMap<>();

    private RechargeNowListener mListener;

    @SuppressLint("ValidFragment")

    public WithdrawalNewFragment(RechargeNowListener listener){
        this.mListener = listener;
    }


    public WithdrawalNewFragment(){

    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        activity = getActivity();
//        otgreceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                getDetail();
//            }
//        };
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
//        activity.registerReceiver(otgreceiver, filter);
//        progressHelper = new ProgressHelper(activity);
//        tabletitle = new TextView(activity);
//        resText = new TextView(activity);
//        tableLayout = new TableLayout(activity);
//
//    }

    public void getDetail() {
        UsbManager manager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();

//            manager.requestPermission(device, mPermissionIntent);
            String Model = device.getDeviceName();


            int DeviceID = device.getDeviceId();
            int Vendor = device.getVendorId();
            int Product = device.getProductId();
            int Class = device.getDeviceClass();
            int Subclass = device.getDeviceSubclass();

            setRadioChecked(device.getManufacturerName().toLowerCase());
        }
    }

    @Override
    public void onDestroy() {
        if (otgreceiver != null) {
            activity.unregisterReceiver(otgreceiver);
            otgreceiver = null;
        }
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_withdrawal_new, container, false);

        activity = getActivity();
        otgreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getDetail();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        activity.registerReceiver(otgreceiver, filter);
        progressHelper = new ProgressHelper(activity);
        tabletitle = new TextView(activity);
        resText = new TextView(activity);
        tableLayout = new TableLayout(activity);

        mainView = inflater.inflate(R.layout.fragment_withdrawal_new, container, false);
        activity = getActivity();
        mainLay = mainView.findViewById(R.id.mainLay);
        mainLinear = mainView.findViewById(R.id.mainLinear);

        if (getArguments() != null) {
            if (getArguments().getBoolean("isWithdraw"))
                postAepsResponseBeforeCapture("withdraw");
            else
                postAepsResponseBeforeCapture("ministatement");
        }

        return mainView;
    }


    private void postAepsResponseBeforeCapture(String postData) {


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("responseFor", postData); // startDate

        String request = Utility.mapWrapper(getActivity(), map1);

        if (in.forpay.util.Utility.isNetworkConnected(activity)) {

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_AEPS_DETAILS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            Log.e("res", result);


                            try {
                                JSONObject jsonObject = new JSONObject(result);

                                if (jsonObject.optString("resCode").equals(Constant.CODE_200)) {

//                                    Toasty.error(activity,jsonObject.getJSONArray("fieldArray").getJSONObject(0).getString("name")).show();

                                    JSONArray fieldArray = jsonObject.getJSONArray("fieldArrayWidthdrawal");
//
                                    initUI(fieldArray);

                                }

                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }

//                            if(!result.isEmpty()) {
//
//                                try {
//                                    JSONObject jsonObject = new JSONObject(result);
//
//                                    if (jsonObject.optString("resCode").equals(Constant.CODE_200)) {
//
//                                        JSONArray fieldArray = jsonObject.getJSONArray("fieldArray");
//
//                                        initUI(fieldArray);
//
//                                    }
//
//
//                                } catch (JSONException jsonException) {
//                                    jsonException.printStackTrace();
//                                }
//
//                            }else Log.e("result","empty "+result);

                        }
                    });
        } else {
            in.forpay.util.Utility.showToast(activity, getString(R.string.internet_connect), "");
        }


    }


    private void initUI(JSONArray fieldArray) throws JSONException {

        ArrayList<AepsModel> aepsModels = new ArrayList<>();
        for (int i = 0; i < fieldArray.length(); i++) {

            try {
                AepsModel aeps = new Gson().fromJson(fieldArray.getString(i).trim(), AepsModel.class);
                aepsModels.add(aeps);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        mainLinear.removeAllViews();
        for (AepsModel aepsModel : aepsModels) {

            if (aepsModel.getInputType().equals("number")) {

                TextInputLayout textInputLayout = new TextInputLayout(activity, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
                textInputLayout.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 24, 24, 0);

                textInputLayout.setLayoutParams(params);
                textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
                textInputLayout.setBoxCornerRadii(5, 5, 5, 5);
                TextInputEditText editText = new TextInputEditText(textInputLayout.getContext());
                textInputLayout.addView(editText);

                editText.setTag(aepsModel.getName());

                if (Integer.parseInt(aepsModel.getMinLength()) > 0) {
                    textInputLayout.setHint(aepsModel.getPlaceHolder() + "*");
                } else
                    textInputLayout.setHint(aepsModel.getPlaceHolder());

                if (!aepsModel.getValue().isEmpty() || !aepsModel.getValue().toString().equals("")) {
                    editText.setText(aepsModel.getValue());
                    editText.setEnabled(false);
                }


                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setFilters(new InputFilter[]{Utility.filterMaxLenght(aepsModel.getMaxLength())});


                if (!aepsModel.getLine().equals("singleLine")) {
                    editText.setSingleLine(false);
                    editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                } else
                    editText.setSingleLine(true);

                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                mainLinear.addView(textInputLayout);
            } else if (aepsModel.getInputType().equals("select")) {

                if (aepsModel.getName().equals("bank")) {

                    LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    titleParams.setMargins(12, 18, 0, 0);

                    TextView title = new TextView(activity);
                    title.setText(aepsModel.getPlaceHolder());
                    title.setLayoutParams(titleParams);

                    ArrayList<String> banksNames = new ArrayList<>();

                    SearchableSpinner searchableSpinner = new SearchableSpinner(new ContextThemeWrapper(activity, R.style.spinner_style), null, 0);

                    searchableSpinner.setTag(aepsModel.getName());

                    searchableSpinner.setTitle(aepsModel.getPlaceHolder());

                    for (AepsModel.ValueArray valueArray : aepsModel.getValueArray()) {
                        banksNames.add(valueArray.getName());
                        bankID.put(valueArray.getName(), valueArray.getId());
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, banksNames);

                    searchableSpinner.setAdapter(arrayAdapter);

                    mainLinear.addView(title);
                    mainLinear.addView(searchableSpinner);


                } else {

                    FlexBoxSingleCheckableGroup radioGroup = new FlexBoxSingleCheckableGroup(activity);
                    radioGroup.setPadding(16, 16, 16, 12);
//                    radioGroup.setOrientation(LinearLayout.HORIZONTAL);
                    radioGroup.setTag(aepsModel.getName());
                    radioGroup.setFlexWrap(FlexWrap.WRAP);
//                    radioGroup.setGravity(Gravity.CENTER);


                    for (int pos = 0; pos < aepsModel.getValueArray().size(); pos++) {

                        packageIDOf.put(aepsModel.getValueArray().get(pos).getName(), aepsModel.getValueArray().get(pos).getId());
                        pidXmlOf.put(aepsModel.getValueArray().get(pos).getName(), aepsModel.getValueArray().get(pos).getPidXml());

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(8, 8, 8, 8);
                        RadioButton radioButton = (RadioButton) LayoutInflater.from(activity).inflate(R.layout.select_tablay, null);
                        radioButton.setText(aepsModel.getValueArray().get(pos).getName());
                        radioButton.setLayoutParams(layoutParams);
                        radioButton.setId(View.generateViewId());
                        radioGroup.addView(radioButton);
                    }


                    radioGroup.setOnCheckedChangeListener(new FlexBoxSingleCheckableGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(FlexBoxSingleCheckableGroup group, int checkedId) {
                            RadioButton checked = mainLay.findViewById(checkedId);
                            checkedg = checked;

                            if (checked.isChecked()) {

                                if (!Utility.isPackageInstalled(packageIDOf.get(checked.getText()), activity.getPackageManager())) {
                                    showInstallationDialig(checked);
                                }
                            }

                        }
                    });

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, 30, 0, 30);
                    radioGroup.setLayoutParams(layoutParams);

                    mainLinear.addView(radioGroup);

//
//
//                    View tablay = LayoutInflater.from(activity).inflate(R.layout.select_tablay, null);
//
//                    TabLayout tabLayout = tablay.findViewById(R.id.modtab_layout);
//
//                    tabLayout.setTag(aepsModel.getName());
//
//
//                    for (AepsModel.ValueArray valueArray : aepsModel.getValueArray()) {
//                        tabLayout.addTab(tabLayout.newTab().setText(valueArray.getName()));
//                    }
//
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    params.setMargins(0, 24, 24, 0);
//                    tablay.setLayoutParams(params);
//                    mainLinear.addView(tablay);
                }

            }

        }


        LinearLayout checkboxLay = new LinearLayout(activity);
        checkboxLay.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 12, 48, 0);
        checkboxLay.setLayoutParams(params);

        CheckBox toscheck = new CheckBox(activity);
        toscheck.setTag("tos");
        toscheck.setChecked(true);

        TextView textViewTos = new TextView(activity);

        String tosText = "Privacy Policy";
        String conditionText = "Conditions";
        String tos = "I Accept " + tosText + " and " + conditionText + ".";


        SpannableString spannableString = new SpannableString(tos);
        ClickableSpan clickableSpanTos = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Log.e("tos", "called");

                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra("url", "https://forpay.in/privacy.php");
                intent.putExtra("webName", "privacy");
                startActivity(intent);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

        };

        ClickableSpan clickableSpanCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtra("url", "https://forpay.in/tos.php");
                intent.putExtra("webName", "tos");
                startActivity(intent);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

        };

        spannableString.setSpan(clickableSpanTos, tos.indexOf(tosText), tos.indexOf(tosText) + tosText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(clickableSpanCondition, tos.indexOf(conditionText), tos.indexOf(conditionText) + conditionText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewTos.setText(spannableString);

        textViewTos.setMovementMethod(LinkMovementMethod.getInstance());
        textViewTos.setHighlightColor(Color.TRANSPARENT);

        checkboxLay.addView(toscheck);
        checkboxLay.addView(textViewTos);
        mainLinear.addView(checkboxLay);


        Button scanbtn = mainView.findViewById(R.id.scanbtn);//new Button(activity);
        scanbtn.setVisibility(View.VISIBLE);
//        scanbtn.setId(buttonId);

        scanbtn.setText("Start Scan");
        scanbtn.setTextColor(activity.getResources().getColor(R.color.white));

        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        scanbtn.setBackground(activity.getResources().getDrawable(R.drawable.apes_btn_bg));


//        mainLay.addView(scanbtn, relativeParams);

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton checked = null;
                final_list.clear();

                if (toscheck.isChecked()) {
                    if (checkedg != null) {
                        for (AepsModel aepsModel : aepsModels) {
                            View addedView = mainLay.findViewWithTag(aepsModel.getName());

                            if (addedView instanceof TextInputEditText) {
                                map1.put(aepsModel.getName(), ((TextInputEditText) addedView).getText().toString());
                                if (((TextInputEditText) addedView).getText().toString().length() < Integer.parseInt(aepsModel.getMinLength())) {
                                    Toasty.error(activity, "Please input valid " + aepsModel.getPlaceHolder()).show();
                                    return;
                                }
                                if (aepsModel.getName().equals("aadharNumber")) {
                                    if (!Utility.isValidAadhar(((TextInputEditText) addedView).getText().toString())) {
                                        Toasty.error(activity, "Please input valid " + aepsModel.getPlaceHolder()).show();
                                        return;
                                    }

                                }
                                Log.e(aepsModel.getName(), "=" + ((TextInputEditText) addedView).getText().toString());

                            } else if (addedView instanceof FlexBoxSingleCheckableGroup) {
                                if (aepsModel != null && aepsModel.getName() != null) {
                                    checked = mainLay.findViewById(((FlexBoxSingleCheckableGroup) addedView).getCheckedRadioButtonId());
                                    //Log.e(aepsModel.getName(), "=" + checked.getText() + " " + packageIDOf.get(checked.getText().toString()));
                                    if (aepsModel.getName() != null && checked != null) {
                                        map1.put(aepsModel.getName(), checked.getText().toString());
                                    }
                                }
                            } else if (addedView instanceof SearchableSpinner) {

                                String bankName = ((SearchableSpinner) addedView).getSelectedItem().toString();

                                map1.put(aepsModel.getName(), bankID.get(bankName));
                                map1.put("bankName=", bankName);

                            }

                            if (aepsModels.get(aepsModels.size() - 1).getName().equals(aepsModel.getName())) {
                                if (checked != null)
                                    startScanning(true, packageIDOf.get(checked.getText().toString()), pidXmlOf.get(checked.getText().toString()));
                            }
                        }
                    } else
                        Toasty.error(activity, "Please Insert a device first").show();
                } else
                    Toasty.error(activity, "Please accept Terms & Conditions.").show();

            }
        });

        getDetail();


    }

    private void setRadioChecked(String name) {

        FlexBoxSingleCheckableGroup group = mainLay.findViewWithTag("bioDevice");

        int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            View o = group.getChildAt(i);
            if (o instanceof RadioButton) {
                if (((RadioButton) o).getText().toString().toLowerCase().contains(name) || name.contains(((RadioButton) o).getText().toString().toLowerCase())) {
                    ((RadioButton) o).setChecked(true);
                    startScanning(false, packageIDOf.get(((RadioButton) o).getText()), pidXmlOf.get(((RadioButton) o).getText()));
                    break;
                }
            }
        }

    }

    private void startScanning(boolean isCapture, String packageName, String pidXml) {
        String deviceName = checkedg.toString();
        if (Utility.isPackageInstalled(packageName, activity.getPackageManager()) || !deviceName.isEmpty()) {

            final Intent intent2 = new Intent();
            if (isCapture)
                intent2.setAction(Utility.RD_SERVICE_CAPTURE);
            else
                intent2.setAction(Utility.RD_SERVICE_INFO);
            intent2.setPackage(packageName);
            pidOptXML = pidXml;
            intent2.putExtra("PID_OPTIONS", pidOptXML);
            if (isCapture) {
                try {
                    startActivityForResult(intent2, 2);
                    progressHelper.show();
                } catch (Exception e) {
                    Utility.showToastLatest(activity, "No package found with name " + packageName, "");
                }
            } else {
                startActivityForResult(intent2, 1);

            }
        } else {
            showInstallationDialig(checkedg);
            //Utility.showToastLatest(activity,"package "+checkedg.getText(),"");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (progressHelper.isShowing())
                progressHelper.dismiss();

            if (data != null) {
                String pidDataXML = data.getStringExtra("PID_DATA");
                if (pidDataXML != null) {
                    String base64 = "";
                    String basePid = "";
                    try {

                        byte[] dataString = Base64.encode(pidDataXML.getBytes(), Base64.NO_WRAP);
                        base64 = new String(dataString);

                        if (pidOptXML == null)
                            return;
                        byte[] dataPid = Base64.encode(pidOptXML.getBytes(), Base64.NO_WRAP);
                        basePid = new String(dataPid);

                        //base64 = Base64.encodeToString(dataString, Base64.NO_WRAP);

                    } catch (Exception e) {
                        Toasty.error(activity, "Encode error").show();
                    }


                    //Utility.showToastLatest(activity,pidDataXML,"ERROR");
                    //Utility.writeLog(activity,pidDataXML);
//            if (pidDataXML != null) {

//                Toast.makeText(activity, "" + pidDataXML, Toast.LENGTH_SHORT).show();
                    postAepsResponseAfterCapture(base64, basePid);
                } else {
                    Toasty.error(activity, "Not received data from device").show();
                }
            }
        }
        if (requestCode == 1) {
            if (data != null) {
                String pidDataXML = data.getStringExtra("RD_SERVICE_INFO");
                if (pidDataXML != null) {

                    String base64 = "";
                    String basePid = "";
                    try {

                        byte[] dataString = Base64.encode(pidDataXML.getBytes(), Base64.NO_WRAP);
                        base64 = new String(dataString);

                        if (pidOptXML == null)
                            return;
                        byte[] dataPid = Base64.encode(pidOptXML.getBytes(), Base64.NO_WRAP);
                        basePid = new String(dataPid);

                        //base64 = Base64.encodeToString(dataString, Base64.NO_WRAP);

                    } catch (Exception e) {
                        Toasty.error(activity, "Encode error").show();
                    }


                    //Utility.showToastLatest(activity,pidDataXML,"ERROR");
                    //Utility.writeLog(activity,pidDataXML);

//                Toast.makeText(activity, "" + pidDataXML, Toast.LENGTH_SHORT).show();
                    infoData = base64;
                    infoPid = basePid;
//                    postAepsResponseAfterCapture(base64,basePid);

                } else {
                    Toasty.error(activity, "data null").show();
                }
            }
        }
    }

    private void postAepsResponseAfterCapture(String postData, String basePid) {
        progressHelper.show();


        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));

        if (getArguments() != null) {
            if (getArguments().getBoolean("isWithdraw"))
                map1.put("txnMode", "withdraw");

            else
                map1.put("txnMode", "ministatement");
        }
        map1.put("response", postData);
        map1.put("pidXml", basePid);
        map1.put("infoResponse", infoData);
        map1.put("infoPidXml", infoPid);


        String request = Utility.mapWrapper(activity, map1);
        if (in.forpay.util.Utility.isNetworkConnected(activity)) {

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_AEPS_TRANSACTION, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            if (progressHelper.isShowing())
                                progressHelper.dismiss();


                            try {
                                JSONObject jsonObject = new JSONObject(result);

                                if (jsonObject.optString("resCode").equals(Constant.CODE_200)) {
//                                    Toasty.success(activity,"Done").show();

                                    StartAepsApiActivity.showMsgDialog(activity, jsonObject.getString("resText"), "Result");
                                    addStatement(jsonObject.getJSONArray("data"), jsonObject.getString("resText"));
                                } else
                                    StartAepsApiActivity.showMsgDialog(activity, jsonObject.getString("resText"), "Result");


                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }

                        }
                    });
        } else {
            in.forpay.util.Utility.showToast(activity, getString(R.string.internet_connect), "");
        }


    }


    private void addStatement(JSONArray statements, String resTextValue) {
        tableLayout.removeAllViews();


        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleParams.setMargins(8, 8, 8, 2);
        titleParams.addRule(RelativeLayout.BELOW, R.id.scrollView);
        int titleId = View.generateViewId();

        RelativeLayout.LayoutParams resTextParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        resTextParams.setMargins(8, 8, 8, 2);
        int resTextId = View.generateViewId();
        resText.setId(resTextId);
        resText.setText(resTextValue);
        resText.setTextColor(activity.getResources().getColor(R.color.red));
        resText.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins_regular));

        tabletitle.setId(titleId);
        tabletitle.setText("Mini-Statement: ");
        tabletitle.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins_bold));


//        RelativeLayout.LayoutParams padp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        padp.setMargins(2,2,2,2);


        tableLayout.setPadding(8, 8, 8, 8);

        TableRow head = new TableRow(activity);

//        TextView d = new TextView(activity);
////        d.setLayoutParams(padp);
//        d.setText("Description");
//
//        head.addView(d);
//
//        TextView am = new TextView(activity);
//        am.setText("Amount");
////        am.setLayoutParams(padp);
//
//        head.addView(am);
//
//        TextView bl = new TextView(activity);
//        bl.setText("Balance");
////        bl.setLayoutParams(padp);
//
//        head.addView(bl);
////        head.setPadding(2,2,2,2);


        for (int i = 0; i < statements.length(); i++) {
            TableRow row = new TableRow(activity);
            TextView balance = new TextView(activity);
            balance.setText("old");
            try {
                JSONObject dobject = statements.getJSONObject(i);
                Iterator<String> keys = dobject.keys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    if (i == 0) {
                        if (!key.equals("balance")) {
                            TextView heading = rowData(key.toUpperCase());
                            heading.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins_bold));
                            head.addView(heading);
                        }

                        if (!keys.hasNext()) {
                            TextView heading = rowData("balance".toUpperCase());
                            heading.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins_bold));
                            head.addView(heading);
                        }
                    }

                    if (!key.equals("balance")) {
                        TextView tv = rowData(dobject.optString(key));
                        tv.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins_semibold));
                        row.addView(tv);
                    } else {
                        TextView tv = rowData(dobject.optString(key));
                        tv.setTypeface(ResourcesCompat.getFont(activity, R.font.poppins_semibold));
                        balance = tv;
                    }

                    if (!keys.hasNext())
                        row.addView(balance);
                }
                if (i == 0) {
                    tableLayout.addView(head);
                }

                tableLayout.addView(row);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeParams.addRule(RelativeLayout.BELOW, titleId);
        relativeParams.addRule(RelativeLayout.ABOVE, R.id.scanbtn);
        relativeParams.setMargins(8, 8, 8, 8);
//        ScrollView scrollView= new ScrollView(activity);
//        scrollView.setVerticalScrollBarEnabled(true);

        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(activity);
        horizontalScrollView.addView(tableLayout);
        LinearLayout.LayoutParams hparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        horizontalScrollView.setHorizontalScrollBarEnabled(true);
        horizontalScrollView.setLayoutParams(hparams);
//        scrollView.addView(horizontalScrollView);

        mainLinear.addView(resText, resTextParams);
        mainLinear.addView(tabletitle, titleParams);

        mainLinear.addView(horizontalScrollView, relativeParams);


    }

    private TextView rowData(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(5, 5, 5, 5);
        //textView.setBackgroundResource(R.drawable.table_border);
        return textView;
    }

    private void showInstallationDialig(RadioButton checked) {
        AlertDialog.Builder gotoPlaystore = new AlertDialog.Builder(activity);
        gotoPlaystore.setMessage("Required Application not available, download it to countinue.");

        gotoPlaystore.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (checkedg != null) {
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageIDOf.get(checked.getText()))));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageIDOf.get(checked.getText()))));
                    }
                }
            }
        });

        gotoPlaystore.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (checked != null) {
                    //checked.setChecked(false);
                }
            }
        });
        gotoPlaystore.setTitle("Device");
        gotoPlaystore.setCancelable(false);

        gotoPlaystore.show();

    }
}