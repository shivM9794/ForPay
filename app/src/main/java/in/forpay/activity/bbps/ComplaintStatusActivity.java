package in.forpay.activity.bbps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import in.forpay.databinding.ActivityBbpsComplaintStatusBinding;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ComplaintStatusActivity extends AppCompatActivity {

    private ActivityBbpsComplaintStatusBinding mBinding;
    private ProgressHelper progressHelper;


    private Activity activity;

    public static RaiseComplaint newInstance() {
        return new RaiseComplaint();
    }

    public ComplaintStatusActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ComplaintStatusActivity.this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bbps_complaint_status);
        mBinding.editTextTransactionId.setSaveEnabled(true);
        init();
    }



    /**
     * Click on request fund button
     */
    public void checkStatus() {
        if (validation()) {

            Map<String,String> map1 = new HashMap<>();

            map1.put("token",Utility.getToken(activity)); // key
            map1.put("imei",Utility.getImei(activity)); // imei
            map1.put("versionCode",Utility.getVersionCode(activity)); // version code
            map1.put("os", Utility.getOs(activity));
            map1.put("transactionId",mBinding.editTextTransactionId.getText().toString().trim()); // amount

            String request = Utility.mapWrapper(this,map1);


            requestCheckStatus(request);
        }
    }

    private void init() {

        mBinding.btnRequestNow.setOnClickListener(v -> checkStatus());

        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
        progressHelper = new ProgressHelper(activity);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(activity,mBinding.fundLayout);
    }


    /**
     * Validation for all fields
     */
    private boolean validation() {

        String transactionId = mBinding.editTextTransactionId.getText().toString().trim();

        if (TextUtils.isEmpty(transactionId)) {
            Utility.showToast(activity, "Please enter Complaint Reference Number","");
            return false;
        }
        return true;
    }

    /**
     * Refresh UI
     */
    private void refreshUI() {


    }

    /**
     * Request fund
     */
    private void requestCheckStatus(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_COMPLAIN_STATUS, request, new CallbackListener() {
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

    /**
     * Parse response
     */
    private void parseRequestCheckStatus(String result) {
        progressHelper.dismiss();
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

            EditText et = new EditText(ComplaintStatusActivity.this);

            et.setText(statusarray.getJSONObject(i).optString("value"));
            et.setEnabled(false);

            row.addView(tv);
            row.addView(et);

            tableLayout.addView(row);
        }

        mBinding.mainLinear.addView(tableLayout);



    }

    private TextView rowData(String text) {
        TextView textView = new TextView(ComplaintStatusActivity.this);
        textView.setText(text);
        textView.setPadding(20,20,20,20);
        textView.setLayoutParams(new TableRow.LayoutParams(1));
        textView.setTextColor(getResources().getColor(R.color.black_txt_Color));
        return textView;
    }



}
