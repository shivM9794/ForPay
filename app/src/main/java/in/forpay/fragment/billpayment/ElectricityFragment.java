package in.forpay.fragment.billpayment;


import android.annotation.SuppressLint;
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
import in.forpay.databinding.FragmentElectricityBinding;
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

public class ElectricityFragment extends Fragment {

    private FragmentElectricityBinding mBinding;
    private DatabaseHelper databaseHelper;
    private int operatorSelect = -1;
    private String operatorId = "";
    private String opValue1 = "";
    private String mobile="";
    private String amount="0";
    private String pin="";
    private ProgressHelper progressHelper;
    private RechargeNowListener mListener;
    private String billamt;
    private String billname;

    public ElectricityFragment() {
    }

    @SuppressLint("ValidFragment")
    public ElectricityFragment(RechargeNowListener listener) {
        this.mListener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity()!=null) {

            Utility.setAppLocale(Utility.getDefaultLanguage(getActivity()),getActivity());

        }
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_electricity, container, false);
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
            map1.put("opValue1",opValue1); //
            map1.put("opValue2",""); //
            map1.put("txn_date",""); //



            String request = Utility.mapWrapper(getActivity(),map1);

            rechargeNow(request);
            mBinding.editTextPin.setText("");
        }
    }

    private void init() {
        progressHelper = new ProgressHelper(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        phoneNumber();
    }

    private void phoneNumber() {
        final ArrayList<ContactList> list = databaseHelper.getConactListTmp("","electricity");
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

        final ArrayList<GetLoginValidateResponse.Service> list = databaseHelper.getServiceList(Constant.SERVICE_ELECTRICITY);
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
    }



    /**
     * Get text for validation toast
     */
    private String getText(String id) {
        switch (id) {
            case "167": // Consumer Number & Subdivision Code
                return "Please enter Subdivision Code";

            case "51":
            case "142":
            case "173":
            case "143":
            case "170":
            case "62":
            case "64":
            case "148":
            case "160":
                return "Please enter K Number";

            case "166": // Consumer id
            case "52":
            case "57":
            case "179":
            case "59":
            case "60":
            case "149":
            case "75":
            case "174":
                return "Please enter Consumer Id";

            case "140": // Service Number
            case "141":
            case "71":
            case "72":
            case "74": // Service Number & City Name (new edit_shop text)
                return "Please enter Service Number";

            case "53": // Consumer Number
            case "54":
            case "55":
            case "56":
            case "168":
            case "145":
            case "146":
            case "169":
            case "61":
            case "147":
            case "65":
            case "150":
            case "151":
            case "153":
            case "67":
            case "69":
            case "154":
            case "156":
            case "157":
            case "73":
            case "158":
            case "159":
            case "161":
            case "163":
            case "164":
            case "165":
            case "66": // Consumer Number & Billing Unit (new edit_shop text)
                return "Please enter Consumer Number";

            case "144": // Account Number
            case "171":
            case "172":
            case "70": // Account Number & Cycle Number (new edit_shop text)
                return "Please enter Account Number";

            case "63": // Business Partner Number
                return "Please enter Business Partner Number";

            case "152": // CA Number
            case "155":
                return "Please enter Business CA Number";

            case "162": // Connection Number
                return "Please enter Connection Number";
            default:
                return "Please enter number";
        }
    }

    /**
     * Change hint of editText and add new edit_shop text if needed
     */
    private void changeHint(String id) {
        mBinding.otherConsumer.setVisibility(View.GONE);
        mBinding.viewOther.setVisibility(View.GONE);
        mBinding.editTextConsumerId.setText("");
        mBinding.editTextOther.setText("");
        opValue1 = "";
        switch (id) {
            case "51":
            case "142":
            case "173":
            case "143":
            case "170":
            case "62":
            case "64":
            case "148":
            case "160":
                mBinding.editTextConsumerId.setHint("K Number");
                break;

            case "166": // Consumer id
            case "52":
            case "57":
            case "179":
            case "59":
            case "60":
            case "149":
            case "75":
            case "174":
                mBinding.editTextConsumerId.setHint("Consumer Id");
                break;

            case "140": // Service Number
            case "141":
            case "71":
            case "72":
                mBinding.editTextConsumerId.setHint("Service Number");
                break;
            case "74": // Service Number & City Name (new edit_shop text)
                mBinding.editTextConsumerId.setHint("Service Number");
                mBinding.otherConsumer.setVisibility(View.VISIBLE);
                mBinding.viewOther.setVisibility(View.VISIBLE);
                mBinding.editTextOther.setHint("City Name");
                break;

            case "167": // Consumer Number & Subdivision Code
                mBinding.editTextConsumerId.setHint("Consumer Number");
                mBinding.otherConsumer.setVisibility(View.VISIBLE);
                mBinding.viewOther.setVisibility(View.VISIBLE);
                mBinding.editTextOther.setHint("Subdivision Code");
                break;

            case "53": // Consumer Number
            case "54":
            case "55":
            case "56":
            case "168":
            case "145":
            case "146":
            case "169":
            case "61":
            case "147":
            case "65":
            case "150":
            case "151":
            case "153":
            case "67":
            case "69":
            case "154":
            case "156":
            case "157":
            case "73":
            case "158":
            case "159":
            case "161":
            case "163":
            case "164":
            case "165":
                mBinding.editTextConsumerId.setHint("Consumer Number");
                break;
            case "66": // Consumer Number & Billing Unit (new edit_shop text)
                mBinding.editTextConsumerId.setHint("Consumer Number");
                mBinding.otherConsumer.setVisibility(View.VISIBLE);
                mBinding.viewOther.setVisibility(View.VISIBLE);
                mBinding.editTextOther.setHint("Billing Unit");
                break;

            case "144": // Account Number
            case "171":
            case "172":
                mBinding.editTextConsumerId.setHint("Account Number");
                break;
            case "70": // Account Number & Cycle Number (new edit_shop text)
                mBinding.editTextConsumerId.setHint("Account Number");
                mBinding.otherConsumer.setVisibility(View.VISIBLE);
                mBinding.viewOther.setVisibility(View.VISIBLE);
                mBinding.editTextOther.setHint("Cycle Number");
                break;

            case "63": // Business Partner Number
                mBinding.editTextConsumerId.setHint("Business Partner Number");
                break;

            case "152": // CA Number
            case "155":
                mBinding.editTextConsumerId.setHint("CA Number");
                break;

            case "162": // Connection Number
                mBinding.editTextConsumerId.setHint("Connection Number");
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
        opValue1 = mBinding.editTextOther.getText().toString().trim();
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
        } else {
            if (operatorId.equals("74")) {
                if (TextUtils.isEmpty(opValue1)) {
                    Utility.showToast(getActivity(), "Please enter city name","");
                    return false;
                }
            }
            if (operatorId.equals("66")) {
                if (TextUtils.isEmpty(opValue1)) {
                    Utility.showToast(getActivity(), "Please enter billing unit","");
                    return false;
                }
            }
            if (operatorId.equals("70")) {
                if (TextUtils.isEmpty(opValue1)) {
                    Utility.showToast(getActivity(), "Please enter cycle number","");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Refresh UI after recharge done
     */
    private void refreshUI() {
        mListener.onDone();
        /*
        operatorSelect = -1;
        operatorId = "";
        opValue1 = "";
        mBinding.textViewOperator.setText("Select operator");
        mBinding.editTextConsumerId.setText("");
        mBinding.editTextOther.setText("");
        mBinding.editTextAmount.setText("");
        */
        mBinding.editTextPin.setText("");

    }

    /**
     * Recharge now
     */
    private void rechargeNow(final String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            Log.e("BillPaymentRes",result);
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
                    intent.putExtra("type","electricity");
                    intent.putExtra("pin",pin);
                    intent.putExtra("extraAmount","");
                    intent.putExtra("opValue1",opValue1);
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
