package in.forpay.activity;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.StockHistoryAdapter;
import in.forpay.databinding.FragmentStockHistoryBinding;
import in.forpay.model.response.GetStockHistoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class StockHistoryActivity extends AppCompatActivity {

    private FragmentStockHistoryBinding mBinding;
    private ProgressHelper progressHelper;
    private String mFromDate = "";
    private String mToDate = "";
    private String mType = "all";
    private String mUsername="";
    private Activity activity;
     public static StockHistoryActivity newInstance() {
        return new StockHistoryActivity();
    }

    public StockHistoryActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = StockHistoryActivity.this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_stock_history);
        init();
    }



    /**
     * Click on apply filter
     */
    public void onClickFilter() {
        filterDialog();
    }

    private void init() {
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
        mBinding.btnStockFilter.setOnClickListener(v -> onClickFilter());
        progressHelper = new ProgressHelper(activity);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        mFromDate = Utility.getCurrentDate();
        mToDate = Utility.getCurrentDate();
        getHistory();
    }

    /**
     * Set adapter
     */
    private void setAdapter(ArrayList<GetStockHistoryResponse.Data> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        mBinding.recyclerView.setAdapter(new StockHistoryAdapter(activity, list));
    }

    /**
     * Show filter dialog
     */
    private void filterDialog() {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_apply_filter_stock_history);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);
        TextView textViewSearch = dialog.findViewById(R.id.textViewSearch);
        final TextView textViewStartDate = dialog.findViewById(R.id.textViewStartDate);
        final TextView textViewEndDate = dialog.findViewById(R.id.textViewEndDate);


        textViewStartDate.setText(Utility.getCurrentDate());
        textViewEndDate.setText(Utility.getCurrentDate());

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

        textViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(textViewEndDate, 2);
            }
        });

        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDate = textViewStartDate.getText().toString().trim();
                String endDate = textViewEndDate.getText().toString().trim();


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
        DatePickerDialog dialog = new DatePickerDialog(activity, R.style.DatePickerDialogTheme, date,
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


        final String[] array = {"All", "Due", "Partially Due", "Paid"};


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
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
            Utility.showToast(activity, "Please select from date","");
            return false;
        } else if (TextUtils.isEmpty(mToDate)) {
            Utility.showToast(activity, "Please select to date","");
            return false;
        }
        return true;
    }

    /**
     * Get history
     */
    private void getHistory() {

        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("fromDate",mFromDate); // amount
        map1.put("toDate",mToDate); // reference number

        String request = Utility.mapWrapper(this,map1);

        getStockHistory(request);
    }

    /**
     * Get balance history after apply filter
     */
    private void getStockHistory(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_STOCK_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect));
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetStockHistoryResponse response = new Gson().fromJson(result, GetStockHistoryResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    Utility.showToast(activity, response.getResText());
                    setAdapter(response.getDataList());
                } else {
                    Utility.showToast(activity, response.getResText());
                }
            } else {
                //Utility.showToast(activity, getString(R.string.server_not_responding));
                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }
    }
}
