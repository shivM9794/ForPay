package in.forpay.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.adapter.RechargeHistoryAdapter1;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityRechargeHistoryBinding;
import in.forpay.model.OrderLocal;
import in.forpay.model.SampleSearchModel;
import in.forpay.model.request.RechargeHistory;
import in.forpay.model.response.GetLoginResponse;
import in.forpay.model.response.GetRechargeHistoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class RechargeHistoryFragment extends Fragment {

    private ActivityRechargeHistoryBinding mBinding;
    private DatabaseHelper databaseHelper;
    private ProgressHelper progressHelper;
    private int operatorSelect = -1;
    private String operatorId = "";
    private String[] orderStatusList = {"All", "Disputed", "Failed", "Pending", "Success"};
    private int orderStatusSelect = -1;
    private String orderStatus = "all";
    private String isShop="";

    public static RechargeHistoryFragment newInstance() {
        return new RechargeHistoryFragment();
    }

    public RechargeHistoryFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_recharge_history,
                container, false);


        mBinding.editTextMobile.addTextChangedListener(new TextWatcher() {
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
                //Log.e("textchanged 1",string);

                if (!string.isEmpty()) {

                    init(string);

                }


            }
        });


        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        init("");
    }


    public void onBackPressed() {


    }

    /**
     * Click on apply filter
     */
    public void onClickFilter() {
        filterDialog();
    }


    public void onClickSearch() {

        String mobileSearch=mBinding.editTextMobile.getText().toString().trim();

        init(mobileSearch);

    }
    /**
     * Click on refresh icon
     */
    public void onClickRefresh() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(getActivity());
        }
        ArrayList<OrderLocal> orderIdList = databaseHelper.getOrderIdListToCheck("","");
        ArrayList<String> listTemp = new ArrayList<>();

        if (orderIdList != null && orderIdList.size() > 0) {

            for (OrderLocal orderLocal : orderIdList) {
                String status = orderLocal.getStatus();
                if (status.equalsIgnoreCase(Constant.PENDING)
                        || status.equalsIgnoreCase(Constant.DISPUTED)) {
                    listTemp.add(orderLocal.getOrderId());
                }
            }

            String orderId = TextUtils.join(",", listTemp);

            if (TextUtils.isEmpty(orderId)) {
                return;
            }


            Map<String,String> map1 = new HashMap<>();

            map1.put("token",Utility.getToken(getActivity())); // key
            map1.put("imei",Utility.getImei(getActivity())); // imei
            map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code
            map1.put("os", Utility.getOs(getActivity()));

            map1.put("orderId",orderId); // email

            String request = Utility.mapWrapper(getActivity(),map1);



            updateOrder(request);
        } else {
            if(orderIdList.size()==0){
                Utility.showToast(getActivity(), "No pending or disputed order found","");
            }
            else{
                Utility.showToast(getActivity(), "Try after sometime","");
            }

        }
    }

    private void init(String mobileSearch) {
        progressHelper = new ProgressHelper(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        ArrayList<RechargeHistory> list = databaseHelper.getRechargeHistoryList(mobileSearch,"");

        int sizeOf =list.size();
        if(sizeOf==0){
            //Utility.showToastLatest(getActivity(),"No Result found","ERROR");
            mBinding.recyclerView.setVisibility(View.GONE);
            Utility.inflateNoDataFoundLayout(getActivity(), mBinding.inflateLayout, "No Result Found");
        }
        else{
            Utility.showToastLatest(getActivity(),sizeOf+" Result found","SUCCESS");
            mBinding.recyclerView.setVisibility(View.VISIBLE);
        }

        setAdapter(list,isShop);
    }

    /**
     * Set adapter
     */
    private void setAdapter(ArrayList<RechargeHistory> list,String isShop) {
        if (list == null || list.size() == 0) {
            return;
        }

        mBinding.recyclerView.setAdapter(new RechargeHistoryAdapter1(getActivity(), list,isShop));
    }

    /**
     * Show filter dialog
     */
    private void filterDialog() {
        operatorSelect = -1;
        operatorId = "";
        orderStatusSelect = -1;
        orderStatus = "all";

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_apply_filter);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        TextView textViewSearch = dialog.findViewById(R.id.textViewSearch);

        final TextView textViewStartDate = dialog.findViewById(R.id.textViewStartDate);
        final TextView textViewEndDate = dialog.findViewById(R.id.textViewEndDate);
        final EditText editTextNumber = dialog.findViewById(R.id.editTextNumber);
        LinearLayout linearLayoutOperator = dialog.findViewById(R.id.linearLayoutOperator);
        final TextView textViewOperator = dialog.findViewById(R.id.textViewOperator);
        LinearLayout linearLayoutStatus = dialog.findViewById(R.id.linearLayoutStatus);
        final TextView textViewStatus = dialog.findViewById(R.id.textViewStatus);

        textViewStartDate.setText(Utility.getCurrentDate());
        textViewEndDate.setText(Utility.getCurrentDate());

        textViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(textViewStartDate);
            }
        });

        textViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(textViewEndDate);
            }
        });

        linearLayoutOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectOperator(textViewOperator);
            }
        });

        linearLayoutStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOrderStatus(textViewStatus);
            }
        });

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDate = textViewStartDate.getText().toString().trim();
                String endDate = textViewEndDate.getText().toString().trim();
                String number = editTextNumber.getText().toString().trim();

                if (validationForFilter(startDate, endDate, number)) {


                    Map<String,String> map1 = new HashMap<>();

                    map1.put("token",Utility.getToken(getActivity())); // key
                    map1.put("imei",Utility.getImei(getActivity())); // imei
                    map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code
                    map1.put("os", Utility.getOs(getActivity()));
                    map1.put("startDate",startDate); // startDate
                    map1.put("endDate",endDate); // endDate
                    map1.put("operatorId",operatorId); // operatorId
                    map1.put("mobile",number); // mobile
                    map1.put("status",orderStatus); // status
                    map1.put("limit","200");

                    String request = Utility.mapWrapper(getActivity(),map1);




                    dialog.dismiss();
                    getRechargeHistory(request);
                }
            }
        });
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
     * Show selection dialog for operator
     */
    /*
    private void selectOperator(final TextView textView) {

        final ArrayList<GetLoginResponse.Service> list = databaseHelper.getServiceList(Constant.SERVICE_ALL);
        if (list != null && list.size() > 0) {
            ArrayList<SampleSearchModel> items = new ArrayList<>();
            for (GetLoginResponse.Service service : list) {
                items.add(new SampleSearchModel(service.getService()));
            }

            new SimpleSearchDialogCompat(getActivity(), "Choose an operator",
                    "Enter here", null, items,
                    new SearchResultListener<SampleSearchModel>() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat dialog,
                                               SampleSearchModel item, int position) {
                            operatorSelect = position;
                            operatorId = list.get(position).getId();
                            textView.setText(item.getTitle());
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            Utility.showToast(getActivity(), getString(R.string.something_went_wrong),"");
        }


    }



     */
    /**
     * Show selection dialog for order status
     */
    private void selectOrderStatus(final TextView textView) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Choose an operator");
        mBuilder.setSingleChoiceItems(orderStatusList, orderStatusSelect, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                textView.setText(orderStatusList[i]);
                orderStatusSelect = i;
                orderStatus = orderStatusList[i].toLowerCase();
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    /**
     * Validation for all field when apply filter
     */
    private boolean validationForFilter(String startDate, String endDate, String number) {
        if (TextUtils.isEmpty(startDate)
                || startDate.equalsIgnoreCase("Select from date")) {
            Utility.showToast(getActivity(), "Please select start date","");
            return false;
        } else if (TextUtils.isEmpty(endDate)
                || endDate.equalsIgnoreCase("Select end date")) {
            Utility.showToast(getActivity(), "Please select end date","");
            return false;
        }
        /*
        else if (TextUtils.isEmpty(number)) {
            Utility.showToast(getActivity(), "Please enter number");
            return false;
        }
        */
        return true;
    }

    /**
     * Get recharge history after apply filter
     */
    private void getRechargeHistory(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_RECHARGE_HISTORY, request, new CallbackListener() {
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

            if (Utility.isServerRespond(result)) {
                //databaseTask task = new databaseTask(getActivity());
                //task.execute(result,"rechargehistory");
                GetRechargeHistoryResponse response = new Gson().fromJson(result, GetRechargeHistoryResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    Utility.showToast(getActivity(), response.getResText(),response.getResCode());

                } else {
                    Utility.showToast(getActivity(), response.getResText(),response.getResCode());
                }
            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding),"");
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
            }
            progressHelper.dismiss();
        }
    }

    /**
     * Update order status
     */
    private void updateOrder(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_UPDATE_STATUS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseUpdateOrderResponse(result);
                        }
                    });
        } else {
            Utility.showToast(getActivity(), getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseUpdateOrderResponse(String result) {
        if (isVisible()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRechargeHistoryResponse response = new Gson().
                        fromJson(result, GetRechargeHistoryResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    Utility.showToast(getActivity(), response.getResText(),response.getResCode());

                } else {
                    if (response.getResCode().equals("129")){
                        Utility.inflateNoDataFoundLayout(getActivity(), mBinding.inflateLayout, "No Result Found");
                    }else {
                        Utility.showToast(getActivity(), response.getResText(),response.getResCode());
                    }

                }
            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding),"");
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
            }
        }
    }


}
