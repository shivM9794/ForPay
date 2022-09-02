package in.forpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.ItemPaymentSummaryAdapter;
import in.forpay.adapter.NotificationListAdapter;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityNotificationBinding;
import in.forpay.listener.Listener;
import in.forpay.model.request.NotificationModel;
import in.forpay.model.response.GetPaymentSummaryModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;
    private Activity activity;
    private NotificationListAdapter adapter;
    private ArrayList<NotificationModel> listData = new ArrayList<>();
    private ProgressHelper progressHelper;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = NotificationActivity.this;
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_notification);
        progressHelper = new ProgressHelper(activity);
        binding.backBtn.setOnClickListener(v -> onBackPressed());

        init();
    }

    private void getNotificationList() {
/*
        binding.notificationList.setLayoutManager(new LinearLayoutManager(activity));
        adapter =new NotificationListAdapter(activity,list);
        binding.notificationList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

 */
    }


    private void init() {
        progressHelper = new ProgressHelper(activity);
        databaseHelper = new DatabaseHelper(activity);


        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));


        String request = Utility.mapWrapper(activity,map1);

        getNotificationHistory(request);

    }

    private void getNotificationHistory(String request){


        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_NOTIFICATION, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            progressHelper.dismiss();
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }

    private void parseResponse(String result){


        try {
            JSONObject jsonObject = new JSONObject(result);
            String resCode = jsonObject.getString("resCode");
            String resText=jsonObject.getString("resText");
            if(resCode.equals(Constant.CODE_200)) {
                JSONArray jsonArray2 = jsonObject.getJSONArray("notifications");

                loadOrderDetails(jsonArray2);
            }
            else{
                Utility.showToastLatest(activity,resText,resCode);
            }

        }
        catch (Exception e){
            Utility.showToastLatest(activity,e.toString(),"ERROR");
        }
    }


    private void loadOrderDetails(JSONArray jsonArray){

        try {
            //JSONArray jsonArray2 = jsonObject.getJSONArray("orderSummary");


            listData = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                NotificationModel usModel = new NotificationModel();

                JSONObject obj = jsonArray.getJSONObject(i);
                usModel.setId(obj.getString("id"));
                usModel.setDetail(obj.getString("details"));

                usModel.setDate(obj.getString("date"));

                usModel.setType(obj.getString("type"));
                usModel.setClickable(obj.getString("clickable"));
                usModel.setActivityValue(obj.getString("activityValue"));

                usModel.setActivityData(obj.getString("activityData"));
                usModel.setStatus(obj.getString("status"));

                listData.add(usModel);

            }


            setAdapter(listData);
        }
        catch (Exception e){
            Utility.showToastLatest(this, e.toString(), "ERROR");
        }

    }
    /**
     * Set adapter
     */
    private void setAdapter(ArrayList<NotificationModel> list) {
        Log.d("notifiifdd","size "+list.size());
        if (list == null || list.size() == 0) {
            return;
        }


        binding.notificationList.setLayoutManager(new LinearLayoutManager(activity));
        NotificationListAdapter notificationListAdapter = new NotificationListAdapter(activity, listData);
        binding.notificationList.setAdapter(notificationListAdapter);
        notificationListAdapter.notifyDataSetChanged();
        //binding.notificationList.setAdapter(new NotificationListAdapter(activity, list));
    }
}
