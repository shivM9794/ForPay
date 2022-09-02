package in.forpay.fragment.recharge;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

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
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.FragmentPostpaidBinding;
import in.forpay.listener.RechargeNowListener;
import in.forpay.model.ContactList;
import in.forpay.model.SampleSearchModel;
import in.forpay.model.request.RechargeHistory;
import in.forpay.model.response.GetLoginResponse;
import in.forpay.model.response.GetLoginValidateResponse;
import in.forpay.model.response.GetRechargeNowResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
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

public class PostpaidFragment extends Fragment {

    private FragmentPostpaidBinding mBinding;
    private int operatorSelect = 0;
    private ProgressHelper progressHelper;
    private String operatorId = "";
    private String mobile="";
    private String amount="0";
    private String pin="";
    private DatabaseHelper databaseHelper;
    private RechargeNowListener mListener;
    private Activity activity;
    private ArrayList<Object> lastTransactionList = new ArrayList<>();
    private LastTransactionAdapter adapter;

    public PostpaidFragment() {
    }

    @SuppressLint("ValidFragment")
    public PostpaidFragment(RechargeNowListener listener) {
        this.mListener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_postpaid, container, false);
        mBinding.setFragment(this);
        activity = getActivity();
        databaseHelper = new DatabaseHelper(getActivity());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

    }


    private void init(String mobileSearch) {
        progressHelper = new ProgressHelper(activity);
        databaseHelper = new DatabaseHelper(activity);
        mBinding.transactionListRecycle.setLayoutManager(new LinearLayoutManager(activity));

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
    /**
     * Click on select operator
     */
    public void onClickOperator() {
        selectOperator();
    }

    /**
     * Click on offers
     */
    public void onClickOffers() {
        if (TextUtils.isEmpty(operatorId)) {
            Utility.showToast(getActivity(), "Please select operator first.","");
            return;
        }
        Intent intent = new Intent(getActivity(), OffersActivity.class);
        intent.putExtra("id", operatorId);
        startActivityForResult(intent, Constant.REQUEST_CODE_RECHARGE);
    }

    /**
     * Click on recharge now
     */
    public void onClickRechargeNow() {
        if (validation()) {

            mobile= mBinding.autoCompleteTextView.getText().toString().trim();
            amount=mBinding.editTextAmount.getText().toString().trim();
            pin=mBinding.editTextPin.getText().toString().trim();

            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            //String txnDate = sdf.format(new Date());


            Map<String,String> map1 = new HashMap<>();
            map1.put("token",Utility.getToken(getActivity())); // key
            map1.put("imei",Utility.getImei(getActivity())); // imei
            map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code
            map1.put("os", Utility.getOs(getActivity()));
            map1.put("mobile", mobile);
            map1.put("operatorId",operatorId); // operatorId
            map1.put("amount",amount);
            map1.put("pin",pin); // pin

            String request = Utility.mapWrapper(getActivity(),map1);

            rechargeNow(request);
            mBinding.editTextPin.setText("");
        }
    }

    private void init() {
        progressHelper = new ProgressHelper(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());

        mBinding.btnContactList.setOnClickListener(v -> startActivity(new Intent(getActivity(), ContactActivity.class)));
        phoneNumber();
        if(getContext()!=null) {

            AdView adView = new AdView(getContext());

            googleAd gAd = new googleAd(getContext(),adView);
            if(gAd.getAd()!=null) {
                mBinding.adView.loadAd(gAd.getAd());
            }

        }
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
            mBinding.autoCompleteTextView.setText(phoneNumber);

            Constant.CONTACT_NUMBER = "";
        }
    }

    /**
     * Get phone number from auto complete text view
     */
    private void phoneNumber() {
        final ArrayList<ContactList> list = Utility.getContactList(getActivity());
        if (list == null || list.size() == 0) {
            //Utility.showToast(getActivity(), "No contact found","");
            return;
        }
        final ArrayList<String> list1 = new ArrayList<>();
        for (ContactList contact : list) {
            String phoneTemp = contact.getPhoneNumber();
            phoneTemp = phoneTemp.replace("+91", "");

            String s = phoneTemp.substring(0, 1);
            if (s.equals("0")) {
                phoneTemp = phoneTemp.replaceFirst("0", "");
            }

            list1.add(phoneTemp + " " + contact.getName());
        }


        final ArrayList<ContactList> listTmp = databaseHelper.getConactListTmp("","postpaid");
        if (listTmp == null || listTmp.size() == 0) {

        }
        else{

            for (ContactList contact2 : listTmp) {
                String phoneTemp2 = contact2.getPhoneNumber();
                list1.add(phoneTemp2+" "+contact2.getName());

            }
        }
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
                list1);
        mBinding.autoCompleteTextView.setAdapter(adapter);
        mBinding.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String namePhone = mBinding.autoCompleteTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(namePhone)) {
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
        final ArrayList<GetLoginValidateResponse.Service> list = databaseHelper.getServiceList(Constant.SERVICE_POSTPAID);
        if (list != null && list.size() > 0) {
            final ArrayList<SampleSearchModel> items = new ArrayList<>();
            for (GetLoginValidateResponse.Service service : list) {
                items.add(new SampleSearchModel(service.getService()));
            }

            new SimpleSearchDialogCompat(getActivity(), "Choose an operator",
                    "Enter here", null, items,
                    new SearchResultListener<SampleSearchModel>() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat dialog,
                                               SampleSearchModel item, int pos) {
                            int position = items.indexOf(item);
                            operatorSelect = position;
                            operatorId = list.get(position).getId();
                            mBinding.textViewOperator.setText(item.getTitle());
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            Utility.showToast(getActivity(), getString(R.string.something_went_wrong),"");
        }
        /*final ArrayList<GetLoginResponse.Service> list = databaseHelper.getServiceList(Constant.SERVICE_POSTPAID);
        if (list != null && list.size() > 0) {
            ArrayList<String> listTemp = new ArrayList<>();
            for (GetLoginResponse.Service service : list) {
                listTemp.add(service.getService());
            }
            final String[] listArray = listTemp.toArray(new String[listTemp.size()]);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle("Choose an operator");
            mBuilder.setSingleChoiceItems(listArray, operatorSelect, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    operatorSelect = i;
                    operatorId = list.get(i).getId();
                    mBinding.textViewOperator.setText(listArray[i]);
                    dialogInterface.dismiss();
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        } else {
            Utility.showToast(getActivity(), getString(R.string.something_went_wrong));
        }*/
    }

    /**
     * Refresh UI after recharge done
     */
    private void refreshUI() {
        mListener.onDone();
        /*
        operatorSelect = 0;
        operatorId = "";
        mBinding.textViewOperator.setText("Select operator");
        mBinding.autoCompleteTextView.setText("");
        mBinding.editTextAmount.setText("");
        mBinding.editTextPin.setText("");
        */
        mBinding.editTextPin.setText("");
    }

    /**
     * Validation for all fields
     */
    private boolean validation() {
        String operator = mBinding.textViewOperator.getText().toString().trim();
        String number = mBinding.autoCompleteTextView.getText().toString().trim();
        String amount = mBinding.editTextAmount.getText().toString().trim();
        String pin = mBinding.editTextPin.getText().toString().trim();
        if (TextUtils.isEmpty(operator)
                || operator.equalsIgnoreCase("Select operator")) {
            Utility.showToast(getActivity(), "Please select operator","");
            return false;
        } else if (TextUtils.isEmpty(number)) {
            Utility.showToast(getActivity(), "Please enter mobile number","");
            return false;
        } else if (TextUtils.isEmpty(amount)) {
            Utility.showToast(getActivity(), "Please enter amount","");
            return false;
        } else if (TextUtils.isEmpty(pin)) {
            Utility.showToast(getActivity(), "Please enter pin","");
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
            Utility.showToast(getActivity(), getString(R.string.internet_connect),"");
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
                    intent.putExtra("service",response.getData().getBal());
                    intent.putExtra("billAmount",response.getData().getBillAmount());
                    intent.putExtra("billName",response.getData().getBillName());
                    intent.putExtra("beneficiaryAccountNumber",response.getData().getBeneficiaryAccountNumber());
                    intent.putExtra("beneficiaryName",response.getData().getBeneficiaryName());
                    intent.putExtra("type","recharge");
                    intent.putExtra("pin",pin);
                    intent.putExtra("extraAmount","0");
                    intent.putExtra("profit",response.getData().getProfit());
                    intent.putExtra("customerPay",response.getData().getCustomerPay());
                    intent.putExtra("serviceCharge",response.getData().getServiceCharge());
                    intent.putExtra("starSelected",response.getData().getStarSelected());
                    intent.putExtra("validateDetails",response.getData().getValidateDetails());
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



            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding), "");
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
            }
        }
    }

}
