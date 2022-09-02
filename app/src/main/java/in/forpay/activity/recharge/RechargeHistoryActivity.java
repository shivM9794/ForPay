package in.forpay.activity.recharge;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.adapter.RechargeHistoryAdapter2;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityRechargeHistoryBinding;
import in.forpay.model.request.RechargeHistory;
import in.forpay.model.response.GetRechargeHistoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class RechargeHistoryActivity extends AppCompatActivity {

    private ActivityRechargeHistoryBinding mBinding;
    private DatabaseHelper databaseHelper;
    private ProgressHelper progressHelper;
    private int operatorSelect = -1;
    private String operatorId = "";
    private String mobile = "";
    private String[] orderStatusList = {"All", "Disputed", "Failed", "Pending", "Success"};
    private int orderStatusSelect = -1;
    private String orderStatus = "all";
    private Activity activity;
    private int mInterval = 20000;
    private Handler mHandler;
    private String startDate = "", endDate = "";
    private String isShop = "";

    public static RechargeHistoryActivity newInstance() {
        return new RechargeHistoryActivity();
    }

    public RechargeHistoryActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = RechargeHistoryActivity.this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recharge_history);

        progressHelper = new ProgressHelper(activity);
        databaseHelper = new DatabaseHelper(activity);
        databaseHelper.deleteRechargeHistoryTable();


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

//                if (!string.isEmpty()) {

                stopListening();

                init(string);

//                }


            }
        });
        init("");
    }


    /**
     * Click on apply filter
     */
    public void onClickFilter() {
        filterDialog();
    }


    public void onClickSearch() {

        String mobileSearch = mBinding.editTextMobile.getText().toString().trim();

        stopListening();
        init(mobileSearch);

    }

    /**
     * Click on refresh icon
     */
    public void onClickRefresh() {

        lastTenTransaction(activity, true);

//        if (databaseHelper == null)
//        {
//            databaseHelper = new DatabaseHelper(activity);
//        }
//        ArrayList<OrderLocal> orderIdList = databaseHelper.getOrderIdListToCheck("","");


//
//        ArrayList<String> listTemp = new ArrayList<>();
//
//        if (orderIdList != null && orderIdList.size() > 0) {
//
//            for (OrderLocal orderLocal : orderIdList) {
//                String status = orderLocal.getStatus();
//                if (status.equalsIgnoreCase(Constant.PENDING)
//                        || status.equalsIgnoreCase(Constant.DISPUTED)) {
//                    listTemp.add(orderLocal.getOrderId());
//                }
//            }
//
//            String orderId = TextUtils.join(",", listTemp);
//
//            if (TextUtils.isEmpty(orderId)) {
//                return;
//            }
//
//            // key||imei||version||token||orderId
//            ArrayList<String> list = new ArrayList<>();
//            list.add("key="+Utility.getToken(activity)); // key
//            list.add("imei="+Utility.getDeviceIMEI(activity)); // imei
//            list.add("versionCode="+Utility.getVersionCode(activity)); // version
//
//            list.add("orderId="+orderId); // orderId
//
//            String request = Utility.dataWrapper(list);
//
//            updateOrder(request);
//        } else {
//            if(orderIdList.size()==0){
//                Utility.showToast(activity, "No pending or disputed order found","");
//            }
//            else{
//                Utility.showToast(activity, "Try after sometime","");
//            }
//
//        }


    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
//                lastFiftyTransaction(activity);
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    private void init(String mobileSearch) {
        mBinding.btnFilter.setOnClickListener(v -> onClickFilter());
        mBinding.btnRefresh.setOnClickListener(v -> onClickRefresh());

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
        mBinding.btnSearch.setOnClickListener(v -> onClickSearch());

        if (mobileSearch.equals("")) {

            lastTenTransaction(activity, true);
        } else {

            ArrayList<RechargeHistory> list = databaseHelper.getRechargeHistoryList(mobileSearch, "");
            int sizeOf = list.size();


            if (sizeOf == 0) {
                //Utility.showToastLatest(activity,"No Result found","ERROR");
                mBinding.recyclerView.setVisibility(View.GONE);
                //Utility.inflateNoDataFoundLayout(activity, mBinding.inflateLayout, "No Result Found");
            } else {
                Utility.showToastLatest(activity, sizeOf + " Result found", "SUCCESS");
                mBinding.recyclerView.setVisibility(View.VISIBLE);
            }

            setAdapter(list, "");

        }


        mHandler = new Handler();
//        startListening();

        mBinding.refreshLayout.setRefreshHeader(new ClassicsHeader(activity));
        mBinding.refreshLayout.setRefreshFooter(new ClassicsFooter(activity));

        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                lastTenTransaction(activity, true);
            }
        });

        mBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                lastTenTransaction(activity, false);
            }
        });

    }

    /**
     * Add to adapter
     */
    private void addAdapter(ArrayList<RechargeHistory> list, String isShop) {

        if (mBinding.refreshLayout.isLoading())
            mBinding.refreshLayout.finishLoadMore();

        if (list == null || list.size() == 0) {
            Log.e("return", "called");
            return;
        } else
            ((RechargeHistoryAdapter2) mBinding.recyclerView.getAdapter()).addList(list, isShop);

    }

    /**
     * Set adapter
     */
    private void setAdapter(ArrayList<RechargeHistory> list, String isShop) {
        if (list == null) {
            return;
        }

        mBinding.recyclerView.setAdapter(new RechargeHistoryAdapter2(activity, list, isShop));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(activity, mBinding.linearLayoutSearch);
    }

    /**
     * Show filter dialog
     */
    private void filterDialog() {
        operatorSelect = -1;
        operatorId = "";
        orderStatusSelect = -1;
        orderStatus = "all";
        mobile = "";
        startDate = "";
        endDate = "";

        final Dialog dialog = new Dialog(activity);
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
                //  selectOperator(textViewOperator);
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
                    //key||imei||version||token||startDate||endDate||operatorId||mobile||status
                    mobile = number;
                    RechargeHistoryActivity.this.startDate = startDate;
                    RechargeHistoryActivity.this.endDate = endDate;


                    Map<String, String> map1 = new HashMap<>();

                    map1.put("token", Utility.getToken(activity)); // key
                    map1.put("imei", Utility.getImei(activity)); // imei
                    map1.put("versionCode", Utility.getVersionCode(activity)); // version code
                    map1.put("os", Utility.getOs(activity));
                    map1.put("startDate", startDate);
                    map1.put("endDate", endDate);
                    map1.put("operatorId", operatorId); // operatorId
                    map1.put("mobile", number); // mobile
                    map1.put("status", orderStatus); // status
                    map1.put("limit", "10");


                    String request = Utility.mapWrapper(activity, map1);


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
        DatePickerDialog dialog = new DatePickerDialog(activity, R.style.DatePickerDialogTheme, date,
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

            new SimpleSearchDialogCompat(activity, "Choose an operator",
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
            Utility.showToast(activity, getString(R.string.something_went_wrong),"");
        }


    }
*/

    /**
     * Show selection dialog for order status
     */
    private void selectOrderStatus(final TextView textView) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
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
            Utility.showToast(activity, "Please select start date", "");
            return false;
        } else if (TextUtils.isEmpty(endDate)
                || endDate.equalsIgnoreCase("Select end date")) {
            Utility.showToast(activity, "Please select end date", "");
            return false;
        }
        /*
        else if (TextUtils.isEmpty(number)) {
            Utility.showToast(activity, "Please enter number");
            return false;
        }
        */
        return true;
    }

    /**
     * Convert server response list into recharge history list
     */
    private void convertRechargeList(ArrayList<GetRechargeHistoryResponse.Data> list) {
        if (list == null || list.size() == 0) {
            Utility.inflateNoDataFoundLayout(activity, mBinding.inflateLayout, "No Result Found");
            return;
        } else {
//            mBinding.inflateLayout.removeAllViews();
//            mBinding.inflateLayout.addView(mBinding.recyclerView);
        }

        ArrayList<RechargeHistory> rhList = new ArrayList<>();

        for (GetRechargeHistoryResponse.Data data : list) {
            //Log.d("DATA - ",data.getMobile());
            rhList.add(new RechargeHistory(data.getOrderId(), data.getStatus(), data.getService(),
                    data.getTransId(), data.getMobile(), data.getAmount(), data.getCreditUsed(), data.getCusCreditUsed(),
                    data.getMarginAmount(), data.getBeneficiaryAccountNumber(), data.getBeneficiaryName(),
                    data.getReqTime(), data.getOperatorId(), data.getProfit(), data.getOpValue1(), data.getOpValue2(), data.getOpValue3(),
                    data.getOpValue4(), data.getOpValue5(), data.getCcf(), data.getPaymentMode(), data.getVisibleCustomerCopy(), data.getOperatorIcon()));

            RechargeHistory model = new RechargeHistory(data.getOrderId(), data.getStatus(),
                    data.getService(), data.getTransId(), data.getMobile(), data.getAmount(),
                    data.getCreditUsed(), data.getCusCreditUsed(), data.getMarginAmount(), data.getBeneficiaryAccountNumber(),
                    data.getBeneficiaryName(), data.getReqTime(), data.getOperatorId(), data.getProfit(), data.getOpValue1(), data.getOpValue2(), data.getOpValue3(),
                    data.getOpValue4(), data.getOpValue5(), data.getCcf(), data.getPaymentMode(), data.getVisibleCustomerCopy(), data.getOperatorIcon());

            databaseHelper.insertRechargeHistory(model);
        }
        setAdapter(rhList, isShop);
    }

    /**
     * Refresh list
     */
    private void refreshList(ArrayList<GetRechargeHistoryResponse.Data> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        ArrayList<RechargeHistory> rhList = new ArrayList<>();

        for (GetRechargeHistoryResponse.Data data : list) {
            rhList.add(new RechargeHistory(data.getOrderId(), data.getStatus(), data.getService(),
                    data.getTransId(), data.getMobile(), data.getAmount(), data.getCreditUsed(), data.getCusCreditUsed(),
                    data.getMarginAmount(), data.getBeneficiaryAccountNumber(), data.getBeneficiaryName(),
                    data.getReqTime(), data.getOperatorId(), data.getProfit(), data.getOpValue1(), data.getOpValue2(), data.getOpValue3(),
                    data.getOpValue4(), data.getOpValue5(), data.getCcf(), data.getPaymentMode(), data.getVisibleCustomerCopy(), data.getOperatorIcon()));
        }

        ArrayList<RechargeHistory> listTemp = databaseHelper.getRechargeHistoryList("", "");
        if (listTemp != null && listTemp.size() > 0) {
            for (RechargeHistory model : listTemp) {
                String oId = model.getOrderId();
                for (RechargeHistory model1 : rhList) {
                    String oId1 = model1.getOrderId();
                    String creditUsed1 = model1.getCreditUsed();
                    String cusCreditUsed1 = model1.getCusCreditUsed();
                    if (oId.equals(oId1)) {
                        String status = model1.getStatus();
                        databaseHelper.updateRechargeHistory(oId, status, model1.getTransId(), creditUsed1, cusCreditUsed1);
                        //databaseHelper.updateOrderIdData(oId, status);
                    }
                }
            }
            ArrayList<RechargeHistory> listTemp2 = databaseHelper.getRechargeHistoryList("", "");
            setAdapter(listTemp2, isShop);
        }
    }


    private void getRecentTransactoins(ArrayList<RechargeHistory> list, boolean refresh, String isShop) {

        ArrayList<RechargeHistory> newlist = new ArrayList<>(); //databaseHelper.getRechargeHistoryList(mobileSearch,"");


        int sizeOf = list.size();
        if (sizeOf == 0) {
            //Utility.showToastLatest(activity,"No Result found","ERROR");
            mBinding.recyclerView.setVisibility(View.GONE);
            //Utility.inflateNoDataFoundLayout(activity, mBinding.inflateLayout, "No Result Found");
        } else {

            if (!mBinding.editTextMobile.getText().toString().isEmpty()) {

                String search = mBinding.editTextMobile.getText().toString().toLowerCase();


                for (RechargeHistory rechargeHistory : list) {


                    if (rechargeHistory.getStatus().toLowerCase().contains(search)
                            || rechargeHistory.getBeneficiaryName().toLowerCase().contains(search)
                            || rechargeHistory.getBeneficiaryAccountNumber().toLowerCase().contains(search)
                            || rechargeHistory.getOperatorId().toLowerCase().contains(search)
                            || rechargeHistory.getService().toLowerCase().contains(search)
                            || rechargeHistory.getMobile().toLowerCase().contains(search)
                            || rechargeHistory.getOrderId().toLowerCase().contains(search)) {

                        newlist.add(rechargeHistory);

                    }

                }

            } else {
                newlist = list;
            }


            //Utility.showToastLatest(activity,sizeOf+" Result found","SUCCESS");
            mBinding.recyclerView.setVisibility(View.VISIBLE);

        }

        if (refresh) {

            if (mBinding.refreshLayout.isRefreshing())
                mBinding.refreshLayout.finishRefresh();

            setAdapter(newlist, isShop);
        } else {
            Log.e("add adapter", "" + newlist.size());
            addAdapter(newlist, isShop);
        }

    }


    /**
     * Get last 10 transactions
     */
    private void lastTenTransaction(final Activity activity, boolean refresh) {

        int pageno = 1;

        if (!refresh) {
            if (mBinding.recyclerView.getAdapter() != null && mBinding.recyclerView.getAdapter().getItemCount() != 0) {

                if (mBinding.recyclerView.getAdapter().getItemCount() % 10 == 0)
                    pageno = (((RechargeHistoryAdapter2) mBinding.recyclerView.getAdapter()).getmList().size() / 10) + 1;
                else
                    pageno = (((RechargeHistoryAdapter2) mBinding.recyclerView.getAdapter()).getmList().size() / 10) + 2;
            }
        }

        Log.e("pageno", "" + pageno);

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));

        map1.put("operatorId", operatorId); // operatorId
        if (!mobile.isEmpty())
            map1.put("mobile", mobile); // mobile
        map1.put("status", orderStatus); // status

        map1.put("startDate", startDate); // startDate
        map1.put("endDate", endDate); // endDate

        map1.put("limit", "10");
        map1.put("pageNumber", "" + pageno);

        String request = Utility.mapWrapper(activity, map1);

        progressHelper.show();


        if (Utility.isNetworkConnected(activity)) {

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_RECHARGE_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            progressHelper.dismiss();

                            Log.e("res", result);


                            GetRechargeHistoryResponse response = new Gson().fromJson(result, GetRechargeHistoryResponse.class);

                            if (response != null) {
                                if (response.getResCode().equals(Constant.CODE_200)) {
                                    if (response.getDataList().size() != 0)
                                        convertRechargeListMain(activity, response.getDataList(), refresh, response.getIsShop());
                                } else {
                                    if (refresh) {
                                        if (response.getResCode().equals(Constant.CODE_129)) {
                                            Utility.inflateNoDataFoundLayout(activity, mBinding.inflateLayout, "No Result Found");
                                        } else {
                                            Utility.showToast(activity, response.getResText(), response.getResCode());
                                        }
                                    } else {
                                        if (mBinding.refreshLayout.isLoading())
                                            mBinding.refreshLayout.finishLoadMore();
                                    }
                                }
                            }
                        }
                    });

        }

    }

    /**
     * Convert server response list into recharge history list
     */
    private void convertRechargeListMain(Activity activity, ArrayList<GetRechargeHistoryResponse.Data> list, boolean refresh, String isShop) {
        if (list == null) {
            return;
        }

        ArrayList<RechargeHistory> rhList = new ArrayList<>();
        boolean haspending = false;

        for (GetRechargeHistoryResponse.Data data : list) {

            //Log.d("database - ",data.getMobile());
            RechargeHistory model = new RechargeHistory(data.getOrderId(), data.getStatus(),
                    data.getService(), data.getTransId(), data.getMobile(), data.getAmount(),
                    data.getCreditUsed(), data.getCusCreditUsed(), data.getMarginAmount(), data.getBeneficiaryAccountNumber(),
                    data.getBeneficiaryName(), data.getReqTime(), data.getOperatorId(), data.getProfit(), data.getOpValue1(), data.getOpValue2(), data.getOpValue3(),
                    data.getOpValue4(), data.getOpValue5(), data.getCcf(), data.getPaymentMode(), data.getVisibleCustomerCopy(), data.getOperatorIcon());


            if (databaseHelper == null) {
                databaseHelper = new DatabaseHelper(activity);
            }
            databaseHelper.insertRechargeHistory(model);
            if (model.getStatus().equals("PENDING"))
                haspending = true;

            rhList.add(model);
        }
        if (!haspending)
            stopListening();


        getRecentTransactoins(rhList, refresh, isShop);

    }


    /**
     * Get recharge history after apply filter
     */
    private void getRechargeHistory(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_RECHARGE_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
        if (Utility.isServerRespond(result)) {
            //databaseTask task = new databaseTask(activity);
            //task.execute(result,"rechargehistory");
            GetRechargeHistoryResponse response = new Gson().fromJson(result, GetRechargeHistoryResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {
                Utility.showToast(activity, response.getResText(), response.getResCode());
                convertRechargeList(response.getDataList());
            } else {

                Utility.showToast(activity, response.getResText(), response.getResCode());

            }
        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(this, ServerNotResponseActivity.class));
        }
        progressHelper.dismiss();
    }

    /**
     * Update order status
     */
    private void updateOrder(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_UPDATE_STATUS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseUpdateOrderResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    /**
     * Parse response
     */
    private void parseUpdateOrderResponse(String result) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetRechargeHistoryResponse response = new Gson().
                    fromJson(result, GetRechargeHistoryResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {
                Utility.showToast(activity, response.getResText(), response.getResCode());
                refreshList(response.getDataList());
            } else {
                Utility.showToast(activity, response.getResText(), response.getResCode());
            }
        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(this, ServerNotResponseActivity.class));
        }
    }

    void startListening() {
        mStatusChecker.run();
    }

    void stopListening() {
        mHandler.removeCallbacks(mStatusChecker);
    }


    @Override
    protected void onDestroy() {
        stopListening();
        super.onDestroy();
    }
}
