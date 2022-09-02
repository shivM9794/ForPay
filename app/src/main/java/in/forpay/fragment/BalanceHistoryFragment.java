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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
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
import in.forpay.adapter.BalanceHistoryAdapter;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.FragmentBalanceHistoryBinding;
import in.forpay.model.request.BalanceHistory;
import in.forpay.model.response.GetBalanceHistoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class BalanceHistoryFragment extends Fragment {

    private FragmentBalanceHistoryBinding mBinding;
    private ProgressHelper progressHelper;
    private String mFromDate = "";
    private String mToDate = "";
    private String mType = "all";
    private String mUsername="";
    private DatabaseHelper databaseHelper;

    public static BalanceHistoryFragment newInstance() {
        return new BalanceHistoryFragment();
    }

    public BalanceHistoryFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_balance_history, container, false);

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


    public void onClickSearch() {

        String mobileSearch=mBinding.editTextMobile.getText().toString().trim();

        init(mobileSearch);

    }

    /**
     * Click on apply filter
     */
    public void onClickFilter() {
        filterDialog();
    }

    private void init(String mobileSearch) {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(mobileSearch.equals("")){

            progressHelper = new ProgressHelper(getActivity());

            mFromDate = Utility.getCurrentDate();
            mToDate = Utility.getCurrentDate();
            getHistory();
        }
        else{



            ArrayList<BalanceHistory> list = databaseHelper.getBalanceHistoryList(mobileSearch,"");
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

                setAdapter(list);

        }

    }

    /**
     * Set adapter
     */
    private void setAdapter(ArrayList<BalanceHistory> list) {
        if (list == null || list.size() == 0) {
            return;
        }



        mBinding.recyclerView.setAdapter(new BalanceHistoryAdapter(getActivity(), list));
    }

    /**
     * Show filter dialog
     */

    private void convertBalanceList(ArrayList<GetBalanceHistoryResponse.Data> list) {
        if (list == null || list.size() == 0) {
            Utility.inflateNoDataFoundLayout(getActivity(), mBinding.inflateLayout, "No Result Found");
            return;
        }else {
            mBinding.inflateLayout.removeAllViews();
            mBinding.inflateLayout.addView(mBinding.recyclerView);
        }

        ArrayList<BalanceHistory> rhList = new ArrayList<>();

        for (GetBalanceHistoryResponse.Data data : list) {
            //Log.d("DATA - ",data.getMobile());
            rhList.add(new BalanceHistory(data.getDate(),data.getOpeningBal(),data.getClosingBal(),data.getType(),
                    data.getAmount(),data.getOrderId(),data.getStatus(),data.getOptId(),data.getMobile(),data.getCreditUsed(),
                    data.getService(), data.getBankAccount(),data.getBankName(),data.getMarginAmount(),data.getOperatorId(),data.getOperatorIcon()));

            BalanceHistory model= new BalanceHistory(data.getDate(),data.getOpeningBal(),data.getClosingBal(),data.getType(),
                    data.getAmount(),data.getOrderId(),data.getStatus(),data.getOptId(),data.getMobile(),data.getCreditUsed(),
                    data.getService(), data.getBankAccount(),data.getBankName(),data.getMarginAmount(),data.getOperatorId(),data.getOperatorIcon());
            try{
                databaseHelper.insertAuditData(model);
            }
            catch (Exception e){

            }

        }
        setAdapter(rhList);
    }


    private void filterDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_apply_filter_balance_history);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        TextView textViewSearch = dialog.findViewById(R.id.textViewSearch);
        final TextView textViewStartDate = dialog.findViewById(R.id.textViewStartDate);
        final TextView textViewToDate = dialog.findViewById(R.id.textViewToDate);


        textViewStartDate.setText(Utility.getCurrentDate());
        textViewToDate.setText(Utility.getCurrentDate());

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        textViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(textViewStartDate, 1);
            }
        });

        textViewToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(textViewToDate, 2);
            }
        });


        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Search Clicked","");
                if (validationForFilter()) {
                    dialog.dismiss();
                    getHistory();
                }

            }
        });
    }

    /**
     * Click on select time
     */
    private void selectDate(final TextView textView, final int which) {

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
                if (which == 1) {
                    mFromDate = date;
                } else {
                    mToDate = date;
                }
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
     * Select type
     */
    private void selectType(final TextView textView) {

        final String[] array = {"All", "Balance", "Commission"};

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Choose an type");
        mBuilder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mType = array[i].toLowerCase();
                textView.setText(array[i]);
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    /**
     * Validation for all field when apply filter
     */
    private boolean validationForFilter() {
        if (TextUtils.isEmpty(mFromDate)) {
            Utility.showToast(getActivity(), "Please select from date","");
            return false;
        } else if (TextUtils.isEmpty(mToDate)) {
            Utility.showToast(getActivity(), "Please select to date","");
            return false;
        }
        return true;
    }

    /**
     * Get history
     */
    private void getHistory() {


        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(getActivity())); // key
        map1.put("imei",Utility.getImei(getActivity())); // imei
        map1.put("versionCode",Utility.getVersionCode(getActivity())); // version code
        map1.put("os", Utility.getOs(getActivity()));
        map1.put("fromDate" , mFromDate);
        map1.put("toDate" , mToDate);

        String request = Utility.mapWrapper(getActivity(),map1);

        getBalanceHistory(request);
    }

    /**
     * Get balance history after apply filter
     */
    private void getBalanceHistory(String request) {
        if (Utility.isNetworkConnected(getActivity())) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(getActivity(),
                    Constant.METHOD_BALANCE_HISTORY, request, new CallbackListener() {
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
                GetBalanceHistoryResponse response = new Gson().fromJson(result, GetBalanceHistoryResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    Utility.showToast(getActivity(), response.getResText(),response.getResCode());
                    databaseHelper = new DatabaseHelper(getActivity());
                    databaseHelper.deleteTable(databaseHelper.TABLE_AUDIT);
                    convertBalanceList(response.getDataList());


                } else {
                    Utility.showToast(getActivity(), response.getResText(),response.getResCode());
                }
            } else {
                //Utility.showToast(getActivity(), getString(R.string.server_not_responding),"");
                startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
            }
        }
    }
}
