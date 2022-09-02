package in.forpay.fragment.billpayment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.RechargeProcessActivity;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.adapter.AutoCompleteAdapter;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.FragmentWaterBinding;
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
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class WaterFragment extends Fragment {

    private FragmentWaterBinding mBinding;
    private DatabaseHelper databaseHelper;
    private int operatorSelect = -1;
    private String operatorId = "";
    private String opValue1 = "";
    private String opValue2 = "";
    private String dob = "";
    private String mobile="";
    private String amount="0";
    private String pin="";
    private ProgressHelper progressHelper;
    private RechargeNowListener mListener;
    private String billamt;
    private String billname;
    public WaterFragment() {
    }

    @SuppressLint("ValidFragment")
    public WaterFragment(RechargeNowListener listener) {
        this.mListener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_water, container, false);
        mBinding.setFragment(this);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        mBinding.linearLayoutbill.setVisibility(View.GONE);



        mBinding.editTextConsumerId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                mBinding.linearLayoutbill.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        mBinding.editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                mBinding.linearLayoutbill.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        mBinding.editTextPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                mBinding.linearLayoutbill.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    /**
     * Click on select operator
     */
    public void onClickOperator() {
        mBinding.linearLayoutbill.setVisibility(View.GONE);

        selectOperator();
    }

    /**
     * Click on recharge now
     */
    public void onClickRechargeNow() {
        if (validation()) {

            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            //String txnDate = sdf.format(new Date());

            //key||imei||versionCode||token||mobile||operatorId||amount||pin||opvalue1||opvalue2||txn_date
            mobile= mBinding.editTextConsumerId.getText().toString().trim();
            amount=mBinding.editTextAmount.getText().toString().trim();
            pin=mBinding.editTextPin.getText().toString().trim();


            Map<String,String> map1 = new HashMap<>();
            map1.put("token",Utility.getToken(getActivity())); // key
            map1.put("imei",Utility.getImei(getActivity())); // imei
            map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code
            map1.put("os", Utility.getOs(getActivity()));
            map1.put("mobile", mobile);
            map1.put("operatorId",operatorId); // operatorId
            map1.put("amount",amount);
            map1.put("pin",pin); // pin
            map1.put("opValue1",""); //
            map1.put("opValue2",""); //
            map1.put("txn_date",""); //

            String request = Utility.mapWrapper(getActivity(),map1);

            rechargeNow(request);
        }
    }

    private void init() {
        progressHelper = new ProgressHelper(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        phoneNumber();
    }


    private void phoneNumber() {
        final ArrayList<ContactList> list = databaseHelper.getConactListTmp("","water");
        if (list == null || list.size() == 0) {
            //Utility.showToast(getActivity(), "No contact found","");
            return;
        }
        final ArrayList<String> list1 = new ArrayList<>();
        for (ContactList contact : list) {
            String phoneTemp = contact.getPhoneNumber();
            phoneTemp = phoneTemp.replace("+91", "");
            if(phoneTemp.length()>5) {
                String s = phoneTemp.substring(0, 1);
                if (s.equals("0")) {
                    phoneTemp = phoneTemp.replaceFirst("0", "");
                }

                list1.add(phoneTemp + " " + contact.getName());
            }
        }

        AutoCompleteAdapter adapter = new AutoCompleteAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
                list1);
        mBinding.editTextConsumerId.setAdapter(adapter);

        mBinding.editTextConsumerId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String namePhone = mBinding.editTextConsumerId.getText().toString().trim();
                if (!TextUtils.isEmpty(namePhone)) {
                    int position = list1.indexOf(namePhone);
                    String phoneNumber=list.get(position).getPhoneNumber();
                    try {
                        String[] separated = phoneNumber.split(":");
                        phoneNumber = separated[0];
                        mBinding.editTextConsumerId.setText(phoneNumber);
                    }
                    catch (Exception e){

                    }
                }
            }
        });
    }
    /**
     * Show selection dialog for operator
     */
    private void selectOperator() {

        final ArrayList<GetLoginValidateResponse.Service> list = databaseHelper.getServiceList(Constant.SERVICE_WATER);
        if (list != null && list.size() > 0) {
            final ArrayList<SampleSearchModel> items = new ArrayList<>();
            for (GetLoginValidateResponse.Service service : list) {
                items.add(new SampleSearchModel(service.getService()));
            }

            new SimpleSearchDialogCompat(getActivity(), getString(R.string.title_select_operator),
                    "Enter here", null, items,
                    new SearchResultListener<SampleSearchModel>() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat dialog,
                                               SampleSearchModel item, int pos) {
                            int position = items.indexOf(item);
                            operatorSelect = position;
                            operatorId = list.get(position).getId();
                            changeHint(operatorId);
                            mBinding.textViewOperator.setText(item.getTitle());
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            Utility.showToast(getActivity(), getString(R.string.something_went_wrong),"");
        }
        /*final ArrayList<GetLoginResponse.Service> list = databaseHelper.getServiceList(Constant.SERVICE_GAS);
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
                    changeHint(operatorId);
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
     * Get text for validation toast
     */
    private String getText(String id) {
        switch (id) {
            case "47": // Customer Id
            case "175":
                return "Please enter Customer Id";
            case "176": // Customer Number
                return "Please enter K Number";
            case "177": // BP Number
                return "Please enter K Number";
            case "198": // Account Number
                return "Please enter Consumer Number (Last 7 Digits)";
            default:
                return "Please enter Number";
        }
    }

    /**
     * Change hint of editText and add new edit_shop text if needed
     */
    private void changeHint(String id) {
        mBinding.editTextConsumerId.setText("");
        switch (id) {
            case "47": // Customer Id
            case "175":
                mBinding.editTextConsumerId.setHint("Customer Id");
                break;
            case "176": // Customer Number
                mBinding.editTextConsumerId.setHint("K Number");
                break;
            case "177": // BP Number
                mBinding.editTextConsumerId.setHint("K Number");
                break;
            case "178": // Account Number
                mBinding.editTextConsumerId.setHint("Last 7 digit Consumer Number");
                break;
        }
    }

    /**
     * Validation for all fields
     */
    private boolean validation() {
        String operator = mBinding.textViewOperator.getText().toString().trim();
        String number = mBinding.editTextConsumerId.getText().toString().trim();
        String amount = mBinding.editTextAmount.getText().toString().trim();
        String pin = mBinding.editTextPin.getText().toString().trim();
        if (TextUtils.isEmpty(operator)
                || operator.equalsIgnoreCase(getString(R.string.title_select_operator))) {
            Utility.showToast(getActivity(), getString(R.string.error_select_operator),"");
            return false;
        } else if (TextUtils.isEmpty(number)) {
            Utility.showToast(getActivity(), getText(operatorId),"");
            return false;
        }
        /*
        else if (TextUtils.isEmpty(amount)) {
            Utility.showToast(getActivity(), "Please enter amount","");
            return false;
        }
        */
        else if (TextUtils.isEmpty(pin)) {
            Utility.showToast(getActivity(), getString(R.string.hint_pin),"");
            return false;
        }
        return true;
    }

    /**
     * Refresh UI after recharge done
     */
    private void refreshUI() {
        mListener.onDone();
        operatorSelect = -1;
        operatorId = "";
        mBinding.textViewOperator.setText(getString(R.string.title_select_operator));
        mBinding.editTextConsumerId.setText("");
        mBinding.editTextAmount.setText("");
        mBinding.editTextPin.setText("");
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


                            try {
                                billamt=new JSONObject(result).getJSONObject("data").getString("billAmount");

                                billname=new JSONObject(result).getJSONObject("data").getString("billName");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                            mBinding.billnametv.setText("Name : "+billname);
                            mBinding.bilamttv.setText("Bill Amount : "+billamt);


                            mBinding.linearLayoutbill.setVisibility(View.VISIBLE);
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
                    intent.putExtra("type","landline");
                    intent.putExtra("pin",pin);
                    intent.putExtra("extraAmount","");
                    intent.putExtra("opValue1",opValue1);
                    intent.putExtra("opValue2",opValue2);
                    intent.putExtra("dob",dob);
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
