package in.forpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.DailyEarningAdapter;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityDailyEarningBinding;
import in.forpay.model.request.DayBook;
import in.forpay.model.response.GetDaybookResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class DailyEarningActivity extends AppCompatActivity {

    private ActivityDailyEarningBinding binding;
    private Activity activity;
    private ArrayList<Object> dailyEarningList = new ArrayList<>();
    private DailyEarningAdapter adapter;
    private DatabaseHelper databaseHelper;
    private ProgressHelper progressHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DailyEarningActivity.this;
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_daily_earning);

        binding.earningRecycle.setLayoutManager(new LinearLayoutManager(activity));
        progressHelper = new ProgressHelper(activity);
        databaseHelper = new DatabaseHelper(activity);
        searchDaybook();
        binding.backBtn.setOnClickListener(v -> onBackPressed());
        binding.btnFromDate.setOnClickListener(v -> {
            selectDate(binding.btnFromDate);
        });

        binding.btnToDate.setOnClickListener(v -> {
            selectDate(binding.btnToDate);
        });

        //init();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(activity,binding.appbarLayout);
    }

    private void searchDaybook(){
        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));

        String request = Utility.mapWrapper(activity,map1);


        requestSummary(request);
    }

    private void requestSummary(String request){

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_DAYBOOK, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseUpdateSummaryResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }

    private void parseUpdateSummaryResponse(String result) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetDaybookResponse response = new Gson().
                    fromJson(result, GetDaybookResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {
                refreshSummaryList(response.getDataList());
            } else {
                Utility.showToast(activity, response.getResText(),response.getResCode());
            }
        } else {
            Utility.showToastLatest(activity, getString(R.string.server_not_responding),"");

        }
    }


    private void refreshSummaryList(ArrayList<GetDaybookResponse.Data> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        ArrayList<DayBook> rhList = new ArrayList<>();

        for (GetDaybookResponse.Data data : list) {


            rhList.add(new DayBook(data.getOperatorId(), data.getSuccess(), data.getPending(),
                    data.getDisputed(), data.getCreditUsed(), data.getCusCreditUsed(), data.getDate(),data.getProfit()));
        }

        setAdapter(rhList);
    }

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

    private void init() {


    }

    private void setAdapter(ArrayList<DayBook> list) {
        if (list == null || list.size() == 0) {
            //Log.d("DaybookEarning","No record 1");
            Utility.inflateNoDataFoundLayout(activity, binding.inflateLayout, "No Result Found");
            return;
        }
        //Log.d("DaybookEarning","List found");
        dailyEarningList.clear();
        dailyEarningList.addAll(list);
        adapter =new DailyEarningAdapter(activity,dailyEarningList);
        binding.earningRecycle.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
