package in.forpay.activity.bbps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.ActivityBbpsSearchTransactionBinding;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.Utility;


public class SearchTransaction extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Activity activity;
    private ActivityBbpsSearchTransactionBinding mBinding;

    private static final String[] paths = {"Search By Mobile", "Search By BBPS Transaction Id"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = SearchTransaction.this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bbps_search_transaction);

        mBinding.backBtn.setOnClickListener(v -> finish());


        init();
    }

    private void init() {

        mBinding.btnSearchNow.setOnClickListener(v -> searchTransaction());
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(SearchTransaction.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinner.setAdapter(adapter);
        mBinding.spinner.setOnItemSelectedListener(SearchTransaction.this);
    }

    private void searchTransaction(){


        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));

        String request = Utility.mapWrapper(this,map1);



        requestCheckStatus(request);
    }

    private void requestCheckStatus(String request) {
        if (Utility.isNetworkConnected(activity)) {

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_BBPS_SEARCH_TRANSACTION, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseRequestCheckStatus(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }
    private void parseRequestCheckStatus(String result) {

        if (Utility.isServerRespond(result)) {
            Log.e("ress",result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                initUI(jsonObject.getJSONArray("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(activity, ServerNotResponseActivity.class));
        }
    }
    private void initUI(JSONArray statusarray) throws JSONException {

        mBinding.mainLinear.removeAllViews();

        TableLayout tableLayout = new TableLayout(activity);

        tableLayout.setPadding(8,8,8,8);


        for (int i=0;i<statusarray.length();i++) {


            TableRow row = new TableRow(activity);

            TextView tv = rowData(statusarray.getJSONObject(i).optString("label"));

            EditText et = new EditText(SearchTransaction.this);

            et.setText(statusarray.getJSONObject(i).optString("value"));
            et.setEnabled(false);

            row.addView(tv);
            row.addView(et);

            tableLayout.addView(row);
        }

        mBinding.mainLinear.addView(tableLayout);



    }

    private TextView rowData(String text) {
        TextView textView = new TextView(SearchTransaction.this);
        textView.setText(text);
        textView.setPadding(20,20,20,20);
        textView.setLayoutParams(new TableRow.LayoutParams(1));
        textView.setTextColor(getResources().getColor(R.color.black_txt_Color));
        return textView;
    }
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Log.d("BBPSActivity","Clicked "+position);
        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                mBinding.linearLayoutSearchByMobile.setVisibility(View.VISIBLE);
                mBinding.linearLayoutSearchByBbps.setVisibility(View.GONE);
                break;
            case 1:
                mBinding.linearLayoutSearchByBbps.setVisibility(View.VISIBLE);
                mBinding.linearLayoutSearchByMobile.setVisibility(View.GONE);
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
