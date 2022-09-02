package in.forpay.fragment.recharge;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ContactActivity;
import in.forpay.activity.OffersActivity;
import in.forpay.activity.RechargeProcessActivity;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.adapter.AutoCompleteAdapter;
import in.forpay.adapter.LastTransactionAdapter;
import in.forpay.database.DataPlansModel;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.FragmentMobileBinding;
import in.forpay.listener.RechargeNowListener;
import in.forpay.model.ContactList;
import in.forpay.model.SampleSearchModel;
import in.forpay.model.request.RechargeHistory;
import in.forpay.model.response.GetLoginResponse;
import in.forpay.model.response.GetLoginValidateResponse;
import in.forpay.model.response.GetRechargeNowResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.model.response.GetServiceListResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import in.forpay.util.googleAd;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

import static android.app.Activity.RESULT_OK;

public class MobileFragment extends Fragment {

    private FragmentMobileBinding mBinding;
    private int operatorSelect = -1;
    private ProgressHelper progressHelper;
    private String operatorId = "";
    private String mobile="";
    private String amount="0";
    private String pin="";
    private String profit,customerPay="";
    private String checkPre;
    private AdView adView;

    private DatabaseHelper databaseHelper;
    private RechargeNowListener mListener;

    public static String selectedcircle;

    public static String mobileoffersresponse;
    String extraAmount="0";
    private Activity activity;
    private ArrayList<Object> lastTransactionList = new ArrayList<>();
    private LastTransactionAdapter adapter;
    public TextView selectCircle;
    public MobileFragment() {
    }

    @SuppressLint("ValidFragment")
    public MobileFragment(RechargeNowListener listener) {
        this.mListener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile, container, false);
        mBinding.setFragment(this);
        databaseHelper = new DatabaseHelper(getActivity());
        Log.d("Chatlist","Fragment 1");



        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Chatlist","Fragment 2");
        init();



    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Log.d("Chatlist","Activity Created");


    }



    private void init(String mobileSearch) {
        progressHelper = new ProgressHelper(activity);
        databaseHelper = new DatabaseHelper(activity);
        mBinding.transactionListRecycle.setLayoutManager(new LinearLayoutManager(activity));
        selectCircle = activity.findViewById(R.id.textViewCircle);

        mBinding.noRecentData.setVisibility(View.GONE);
        mBinding.transactionListRecycle.setVisibility(View.GONE);
        /*
        ArrayList<RechargeHistory> list = databaseHelper.getRechargeHistoryList(mobileSearch,"");

        int sizeOf =list.size();
        if(sizeOf==0){
            mBinding.noRecentData.setVisibility(View.VISIBLE);
            mBinding.transactionListRecycle.setVisibility(View.GONE);
        }
        else{
            mBinding.noRecentData.setVisibility(View.GONE);
            mBinding.transactionListRecycle.setVisibility(View.VISIBLE);
        }

        setAdapter(list);

         */
    }

    /**
     * Set adapter
     */
    private void setAdapter(ArrayList<RechargeHistory> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        lastTransactionList.clear();
        lastTransactionList.addAll(list);
        adapter=new LastTransactionAdapter(activity,lastTransactionList,mBinding.autoCompleteTextView,mBinding.textViewOperator,mBinding.textViewCircle,mBinding.editTextAmount);
        mBinding.transactionListRecycle.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_RECHARGE && resultCode == RESULT_OK && data != null) {
            if (data.getExtras() != null) {
                String amount = data.getExtras().getString("amount");
                String details = data.getExtras().getString("details");


                String extraoffer = data.getExtras().getString("extraoffer");
                String commission = data.getExtras().getString("commission");


                mBinding.editTextAmount.setText(amount);
                mBinding.offerdetails.setText(details);
                if(extraoffer.equals("0") || extraoffer.equals("")){
                 //   mBinding.commissiontv.setText(commission);
                    extraAmount="0";

                }
                else{
                    extraAmount=extraoffer;

                   // mBinding.commissiontv.setText(commission + " + " + extraoffer);

                }
                mBinding.btnBrowsePlan.setVisibility(View.GONE);
                mBinding.offerlayoutrl.setVisibility(View.VISIBLE);
                mBinding.commissionLapyout.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Click on select operator
     */
    public void onClickOperator() {
        selectOperator();
    }


    public void onClickCircle() {
        chooseCircle();
    }


    /**
     * Click on offers
     */
    public void onClickOffers() {

        if (TextUtils.isEmpty(operatorId)) {
            Utility.showToast(getActivity(), "Please select operator first.", "");
            return;
        }

        if (mBinding.textViewCircle.getText().toString().equalsIgnoreCase("Select Circle"))
        {
            Utility.showToast(getActivity(), "Please select circle first.", "");
            return;
        }
        Intent intent = new Intent(getActivity(), OffersActivity.class);
        intent.putExtra("id", operatorId);
        intent.putExtra("from","mobile");
        startActivityForResult(intent, Constant.REQUEST_CODE_RECHARGE);
    }

    private void getOfferNew(final String result) {

        databaseHelper = new DatabaseHelper(activity);
        databaseHelper.deleteDataPlansTable();


        try {


            JSONObject jsonObject = new JSONObject(result);
            JSONArray dataarray = jsonObject.getJSONArray("data");
            Log.e("DataInst", dataarray.length() + "");

            for (int j = 0; j < dataarray.length(); j++) {

                JSONObject jsonObject1 = dataarray.getJSONObject(j);

                String type = jsonObject1.getString("type");
                String amount = jsonObject1.getString("amount");
                String detail = jsonObject1.getString("detail");
                String validity = jsonObject1.getString("validity");
                String talktime = jsonObject1.getString("talktime");
                String extraOffer = jsonObject1.getInt("extraOffer") + "";
                String commission = jsonObject1.getString("commission");
                String data = jsonObject1.getString("data");

                try {
                    databaseHelper.insertDataPlans(new DataPlansModel(type, amount, detail, validity, talktime, extraOffer, commission, data));
                    //Log.e("DataInst", j + "");

                    //Utility.showToast(getActivity(), "inserted "+j , "");
                } catch (Exception e3) {
                    //Utility.showToast(getActivity(), e3.toString() , "");
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Click on recharge now
     */
    public void onClickRechargeNow() {
        if (validation()) {
            mobile= mBinding.autoCompleteTextView.getText().toString().trim();
            amount=mBinding.editTextAmount.getText().toString().trim();
            pin=mBinding.editTextPin.getText().toString().trim();
            circleSelect = -1;
            mBinding.textViewCircle.setText("Select Circle");


            Map<String,String> map1 = new HashMap<>();
            map1.put("token",Utility.getToken(getActivity())); // key
            map1.put("imei",Utility.getImei(getActivity())); // imei
            map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code
            map1.put("os", Utility.getOs(getActivity()));
            map1.put("mobile", mobile);
            map1.put("operatorId",operatorId); // operatorId
            map1.put("amount",amount);
            map1.put("pin",pin); // pin

            map1.put("extraAmount",extraAmount); //


            String request = Utility.mapWrapper(getActivity(),map1);
            rechargeNow(request);
            mBinding.editTextPin.setText("");
        }
    }

    private void init() {
        Log.d("Chatlist","Fragment 3");

        mBinding.btnContactList.setOnClickListener(v -> startActivity(new Intent(getActivity(), ContactActivity.class)));

        mBinding.btnBrowsePlan.setOnClickListener(v -> onClickOffers());
        mBinding.commissionLapyout.setOnClickListener(v -> onClickOffers());
        progressHelper = new ProgressHelper(getActivity());
        progressHelper.show();
        //Log.d("Chatlist","Fragment 4");
        databaseHelper = new DatabaseHelper(getActivity());

        phoneNumber();
        progressHelper.dismiss();
        //Log.d("Chatlist","Fragment 6");

        mBinding.offerlayoutrl.setVisibility(View.GONE);
        mBinding.commissionLapyout.setVisibility(View.GONE);

        mBinding.editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                String string = s.toString();
                Log.e("textchanged 1",string);

                if (string != "" && string != null) {

                    if (databaseHelper.getDataPlans(mBinding.editTextAmount.getText().toString()) == null || databaseHelper.getDataPlans(mBinding.editTextAmount.getText().toString()).size() > 0) {
                        Log.e("textchabged", String.valueOf(databaseHelper.getDataPlans(mBinding.editTextAmount.getText().toString()).get(0).getDetail()));

                        mBinding.offerdetails.setText("" + String.valueOf(databaseHelper.getDataPlans(mBinding.editTextAmount.getText().toString()).get(0).getDetail()));

                        if(databaseHelper.getDataPlans(mBinding.editTextAmount.getText().toString()).get(0).getExtraOffer().equals("0") || databaseHelper.getDataPlans(mBinding.editTextAmount.getText().toString()).get(0).getExtraOffer().equals("")){
                            mBinding.commissiontv.setText(databaseHelper.getDataPlans(mBinding.editTextAmount.getText().toString()).get(0).getCommission());

                        }
                        else{
                            extraAmount=databaseHelper.getDataPlans(mBinding.editTextAmount.getText().toString()).get(0).getExtraOffer();

                            mBinding.commissiontv.setText(databaseHelper.getDataPlans(mBinding.editTextAmount.getText().toString()).get(0).getCommission() + " + " + extraAmount);

                        }
                        mBinding.offerlayoutrl.setVisibility(View.VISIBLE);
                        mBinding.commissionLapyout.setVisibility(View.VISIBLE);


                    } else {

                        extraAmount="0";

                        mBinding.offerdetails.setText("Offer Details");

                        mBinding.commissiontv.setText("0 + 0");

                        mBinding.btnBrowsePlan.setVisibility(View.VISIBLE);
                        mBinding.offerlayoutrl.setVisibility(View.GONE);
                        mBinding.commissionLapyout.setVisibility(View.GONE);

                    }


                }


            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        //init("");
        if(!Constant.CONTACT_NUMBER.equals("")){
            String phoneNumber=Constant.CONTACT_NUMBER;
            phoneNumber = phoneNumber.replace(" ", "");
            phoneNumber = phoneNumber.replace("+91", "");
            phoneNumber = phoneNumber.replace("+ 91", "");
            phoneNumber = phoneNumber.replaceAll("\\s+", "");
            phoneNumber=phoneNumber.replaceAll("[^0-9]", "");
            mBinding.autoCompleteTextView.setText(phoneNumber);
            Constant.CONTACT_NUMBER = "";
        }


        Log.d("Chatlist","Fragment 7");
        if(getContext()!=null) {
            Log.d("Chatlist","Fragment 8");
            adView = new AdView(getContext());

            googleAd gAd = new googleAd(getContext(),adView);
            if(gAd.getAd()!=null) {
                mBinding.adView.loadAd(gAd.getAd());
            }
            Log.d("Chatlist","Fragment 9");

        }
    }

    /**
     * Get phone number from auto complete text view
     */
    private void phoneNumber() {
        //Log.d("Chatlist","Fragment Phone 1");
        final ArrayList<ContactList> list = Utility.getContactList(getActivity());
        if (list == null || list.size() == 0) {
            //Utility.showToast(getActivity(), "No contact found", "");
            return;
        }
        //Log.d("Chatlist","Fragment Phone 2");
        final ArrayList<String> list1 = new ArrayList<>();

        for (ContactList contact : list) {
            String phoneTemp = contact.getPhoneNumber();
            phoneTemp = phoneTemp.replace(" ", "");
            phoneTemp = phoneTemp.replace("+91", "");
            phoneTemp = phoneTemp.replace("+ 91", "");
            phoneTemp = phoneTemp.replaceAll("\\s+", "");
            list1.add(phoneTemp + " " + contact.getName());
        }


        final ArrayList<ContactList> listTmp = databaseHelper.getConactListTmp("","recharge");
        if (listTmp == null || listTmp.size() == 0) {

        }
        else{

            for (ContactList contact2 : listTmp) {
                String phoneTemp2 = contact2.getPhoneNumber();
                list1.add(phoneTemp2+" "+contact2.getName());

            }
        }
        //Log.d("Chatlist","Fragment Phone 3");

        AutoCompleteAdapter adapter = new AutoCompleteAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
                list1);
        /*mBinding.autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, list1));*/
        mBinding.autoCompleteTextView.setAdapter(adapter);
        mBinding.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String namePhone = mBinding.autoCompleteTextView.getText().toString().trim();

                if (!TextUtils.isEmpty(namePhone)) {

                    //Log.d("HomeActivity","phone "+namePhone);
                    int position = list1.indexOf(namePhone);

                    try {

                        String phoneNumber = list.get(position).getPhoneNumber();

                        phoneNumber = phoneNumber.replace(" ", "");
                        phoneNumber = phoneNumber.replace("+91", "");
                        phoneNumber = phoneNumber.replace("+ 91", "");
                        phoneNumber = phoneNumber.replaceAll("\\s+", "");
                        phoneNumber=phoneNumber.replaceAll("[^0-9]", "");

                        mBinding.autoCompleteTextView.setText(phoneNumber);
                    } catch (Exception e) {
                        //Log.d("HomeActivity","e "+e);
                        if(listTmp!=null) {
                            try{
                                String phoneNumber = listTmp.get(position - list.size()).getPhoneNumber();
                                phoneNumber = phoneNumber.replace(" ", "");
                                phoneNumber = phoneNumber.replace("+91", "");
                                phoneNumber = phoneNumber.replace("+ 91", "");
                                phoneNumber = phoneNumber.replaceAll("\\s+", "");
                                phoneNumber=phoneNumber.replaceAll("[^0-9]", "");
                                mBinding.autoCompleteTextView.setText(phoneNumber);

                            }
                            catch (Exception e2){

                            }


                        }
                    }


                }
            }
        });
    }

    /**
     * Show selection dialog for operator
     */
    private void selectOperator() {

        final ArrayList<GetLoginValidateResponse.Service> list = databaseHelper.getServiceList(Constant.SERVICE_MOBILE);
        if (list != null && list.size() > 0) {
            final ArrayList<SampleSearchModel> items = new ArrayList<>();
            for (GetLoginValidateResponse.Service service : list) {
                items.add(new SampleSearchModel(service.getService()));
            }

            new SimpleSearchDialogCompat(getActivity(), "Choose an operator",
                    "Enter here", null, items, new SearchResultListener<SampleSearchModel>() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat dialog,
                                               SampleSearchModel item, int pos) {
                            int position = items.indexOf(item);
                            operatorSelect = position;
                            operatorId = list.get(position).getId();
                            mBinding.textViewOperator.setText(item.getTitle());

                            dialog.dismiss();
                         //   mBinding.offerlayoutrl.setVisibility(View.GONE);
                         //   mBinding.commissionLapyout.setVisibility(View.GONE);


                            circleSelect = -1;
                            mBinding.textViewCircle.setText("Select Circle");


                        }
                    }).show();
        } else {
            Utility.showToast(getActivity(), getString(R.string.something_went_wrong), "");

            ArrayList<String> list2 = new ArrayList<>();
            list2.add("key="+Utility.getToken(getActivity())); // key
            list2.add("imei="+Utility.getDeviceIMEI(getActivity())); // imei
            list2.add("versionCode="+Utility.getVersionCode(getActivity())); // version code


            String request2 = Utility.dataWrapper(list2);


            if (Utility.isNetworkConnected(getActivity())) {
                progressHelper.show();
                QueryManager.getInstance().postRequest(getActivity(),
                        Constant.METHOD_SERVICELIST, request2, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {

                                parseServiceListResponse(result);
                                //Log.d("Result is ",""+result);

                            }
                        });

            }
            else{
                Utility.showToast(activity, activity.getString(R.string.internet_connect),"");
            }



        }
        databaseHelper.deleteDataPlansTable();
    }

    private void parseServiceListResponse(String response){
        try {
            progressHelper.dismiss();
        }
        catch (Exception e){

        }
        if (Utility.isServerRespond(response)) {
            GetServiceListResponse serviceList = new Gson().fromJson(response,GetServiceListResponse.class);
            if(serviceList!=null) {
                if (serviceList.getResCode().equals("200")){

                    if (serviceList.getServiceList() != null
                            && serviceList.getServiceList().size() > 0) {
                        Utility.showToastLatest(getContext(),""+serviceList.getServiceList().size()+" Service found ","SUCCESS");
                        DatabaseHelper db = new DatabaseHelper(getContext());

                        for (GetServiceListResponse.Service model : serviceList.getServiceList()) {
                            db.insertServiceList(model);
                        }

                    }
                }
                else{
                    Utility.showToastLatest(getActivity(), "No service found","ERROR");
                }
            }
        }
        else{
            //Utility.showToast(getActivity(), getString(R.string.server_not_responding));
            startActivity(new Intent(activity, ServerNotResponseActivity.class));
        }
    }

    private int circleSelect = 0;
    private String circleId = "";


    private void chooseCircle() {



        final ArrayList<GetLoginValidateResponse.Circle> list = Utility.getCircleList(getActivity());
        if (list != null && list.size() > 0) {
            ArrayList<String> listTemp = new ArrayList<>();
            for (GetLoginValidateResponse.Circle circle : list) {
                listTemp.add(circle.getCircle());
            }
            final String[] listArray = listTemp.toArray(new String[listTemp.size()]);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle("Choose an circle");
            mBuilder.setSingleChoiceItems(listArray, circleSelect, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    circleSelect = i;
                    circleId = list.get(i).getId();
                    mBinding.textViewCircle.setText(listArray[i]);
                    dialogInterface.dismiss();

                    Map<String,String> map1 = new HashMap<>();

                    map1.put("token",Utility.getToken(getActivity())); // key
                    map1.put("imei",Utility.getImei(getActivity())); // imei
                    map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code
                    map1.put("os", Utility.getOs(getActivity()));
                    map1.put("operatorId" , operatorId); // operatorId
                    map1.put("circleId" , circleId); // circleId
                    map1.put("mobile" , mobile); // mobile

                    String request = Utility.mapWrapper(getActivity(),map1);


                    Log.e("CircleSelectFrag", request);
                    selectedcircle = request;
                  //  mBinding.offerlayoutrl.setVisibility(View.GONE);
                 //   mBinding.commissionLapyout.setVisibility(View.GONE);
                    getOffer(request);
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        } else {
            Utility.showToast(getActivity(), getString(R.string.something_went_wrong), "");
        }
        databaseHelper.deleteDataPlansTable();
    }


    private void getOffer(final String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();

            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_GET_OFFER, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            Log.e("resultresponse", result);
                            mobileoffersresponse=result;
                            Constant.OFFER_DATA_PLAN1 = "";


                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray dataarray = jsonObject.getJSONArray("data");
                                Log.e("DataInst", dataarray.length() + "");
                                Utility.showToastLatest(getActivity()," "+dataarray.length()+" Offer found ","SUCCESS");

                                for (int j = 0; j < dataarray.length(); j++) {

                                    JSONObject jsonObject1 = dataarray.getJSONObject(j);

                                    String type = jsonObject1.getString("type");
                                    String amount = jsonObject1.getString("amount");
                                    String detail = jsonObject1.getString("detail");
                                    String validity = jsonObject1.getString("validity");
                                    String talktime = jsonObject1.getString("talktime");
                                    String extraOffer = jsonObject1.getInt("extraOffer") + "";
                                    String commission = jsonObject1.getString("commission");
                                    String data = jsonObject1.getString("data");

                                    try {
                                        databaseHelper.insertDataPlans(new DataPlansModel(type, amount, detail, validity, talktime, extraOffer, commission,data));
                                        //Log.e("DataInst", j + "");

                                        //Utility.showToast(getActivity(), "inserted "+j , "");
                                    }
                                    catch (Exception e3){
                                        //Utility.showToast(getActivity(), e3.toString() , "");
                                    }
                                         }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


                            if (isVisible()) {
                                progressHelper.dismiss();
                            }
//                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect), "");
        }
    }


    /**
     * Refresh UI after recharge done
     */
    private void refreshUI() {
        mListener.onDone();

        /*
                operatorSelect = -1;
        operatorId = "";
        mBinding.textViewOperator.setText("Select operator");
        mBinding.autoCompleteTextView.setText("");
        mBinding.editTextAmount.setText("");
        */
        mBinding.editTextPin.setText("");
        databaseHelper.deleteDataPlansTable();
    }

    /**
     * Validation for all fields
     */
    private boolean validation() {
        String operator = mBinding.textViewOperator.getText().toString().trim();
        String number = mBinding.autoCompleteTextView.getText().toString().trim();
        amount = mBinding.editTextAmount.getText().toString().trim();
        pin = mBinding.editTextPin.getText().toString().trim();
        if (TextUtils.isEmpty(operator)
                || operator.equalsIgnoreCase("Select operator")) {
            Utility.showToast(getActivity(), "Please select operator", "");
            return false;
        } else if (TextUtils.isEmpty(number)) {
            Utility.showToast(getActivity(), "Please enter mobile number", "");
            return false;
        } else if (TextUtils.isEmpty(amount)) {
            Utility.showToast(getActivity(), "Please enter amount", "");
            return false;
        } else if (TextUtils.isEmpty(pin)) {
            Utility.showToast(getActivity(), "Please enter pin", "");
            return false;
        }
        return true;
    }


    /**
     * Recharge now
     */
    private void rechargeNow(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect), "");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
        if (isVisible()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRechargeValidateResponse response = new Gson().fromJson(result,GetRechargeValidateResponse.class);

                if(response.getResCode().equals(Constant.CODE_200)){


                    Intent intent = new Intent(getActivity(), RechargeProcessActivity.class);
                    intent.putExtra("amount",amount);
                    intent.putExtra("mobile",mobile);
                    intent.putExtra("operatorId",operatorId);
                    intent.putExtra("creditUsed",response.getData().getCreditUsed());
                    intent.putExtra("bal",response.getData().getBal());
                    intent.putExtra("service",response.getData().getService());
                    intent.putExtra("billAmount",response.getData().getBillAmount());
                    intent.putExtra("billName",response.getData().getBillName());
                    intent.putExtra("beneficiaryAccountNumber",response.getData().getBeneficiaryAccountNumber());
                    intent.putExtra("beneficiaryName",response.getData().getBeneficiaryName());
                    intent.putExtra("type","recharge");
                    intent.putExtra("pin",pin);
                    intent.putExtra("extraAmount",extraAmount);
                    intent.putExtra("profit",response.getData().getProfit());
                    intent.putExtra("customerPay",response.getData().getCustomerPay());
                    intent.putExtra("serviceCharge",response.getData().getServiceCharge());
                    intent.putExtra("starSelected",response.getData().getStarSelected());

                            try{
                                startActivity(intent);
                            }
                            catch (NullPointerException e){
                                Utility.showToastLatest(getActivity(), e.toString(), "ERROR");
                            }
                            catch (Exception e){
                                Utility.showToastLatest(getActivity(), e.toString(), "ERROR");
                            }






                }
                else{
                    Utility.showToast(getActivity(), response.getResText(), response.getResCode());
                }

                /*
                GetRechargeNowResponse response = new Gson().fromJson(result, GetRechargeNowResponse.class);
                if (response.getData().getResCode().equals(Constant.CODE_200)) {
                    Utility.showToast(getActivity(), response.getData().getResText(), response.getData().getResCode());
                    Utility.setBalance(getActivity(), response.getData().getBal());
                    Utility.setCommissionBalance(getActivity(), response.getData().getCommissionBal());
                    addOrderId(response.getData());
                    refreshUI();
                } else if (response.getData().getResCode().equals(Constant.CODE_201)) {
                    Utility.showToast(getActivity(), response.getData().getResText(), response.getData().getResCode());
                    Utility.setBalance(getActivity(), response.getData().getBal());
                    Utility.setCommissionBalance(getActivity(), response.getData().getCommissionBal());
                    addOrderId(response.getData());
                    refreshUI();
                } else {
                    if (response.getData().getStatus().equals(Constant.FAILED)) {
                        if (!response.getData().getOrderId().equals("0")) {
                            addOrderId(response.getData());
                        }
                    }
                    Utility.showToast(getActivity(), response.getData().getResText(), response.getData().getResCode());
                }
                */

            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding), "");
                startActivity(new Intent(activity, ServerNotResponseActivity.class));
            }
        }
    }

}
