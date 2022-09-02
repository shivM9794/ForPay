package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.RechargeDetailsActivity;
import in.forpay.activity.supportchat.SupportChatActivity;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.LayoutNotificationListBinding;
import in.forpay.model.request.NotificationModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<NotificationModel> arrayList;
    private DatabaseHelper databaseHelper;


    public NotificationListAdapter(Activity activity, ArrayList<NotificationModel> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
        Log.d("notifiifdd","size 4 "+arrayList.size());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(activity).inflate(R.layout.layout_notification_list, parent, false);
        return new NotificationHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("notifiifdd","size 5 "+arrayList.size());
        if(holder instanceof NotificationHolder){
            Log.d("notifiifdd","size 6 "+arrayList.size());
            NotificationModel notificationModel = (NotificationModel)arrayList.get(position);
            NotificationHolder holder1 = (NotificationHolder)holder;
            if(notificationModel.getStatus().equals("0")){
                holder1.adContainerViewBinding.notificationDetail.setTypeface(holder1.adContainerViewBinding.notificationDetail.getTypeface(), Typeface.BOLD);
            }
            if(notificationModel.getType().equals("SUCCESS")){
                holder1.adContainerViewBinding.layoutView.setBackground(ContextCompat.getDrawable(activity, R.drawable.successrectangle));
            }
            else if(notificationModel.getType().equals("FAILED")){
                holder1.adContainerViewBinding.layoutView.setBackground(ContextCompat.getDrawable(activity, R.drawable.failedrectangle));
            }

            holder1.adContainerViewBinding.notificationDetail.setText(notificationModel.getDetail());
            holder1.adContainerViewBinding.notificationDate.setText(notificationModel.getDate());



        }


    }


    @Override
    public int getItemCount() {
        Log.d("notifiifdd","size 2 "+arrayList.size());

        return arrayList.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        LayoutNotificationListBinding adContainerViewBinding;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            adContainerViewBinding = LayoutNotificationListBinding.bind(itemView);
            Log.d("notifiifdd","size 3 "+arrayList.size());
            adContainerViewBinding.layoutView.setOnClickListener(v -> {
                NotificationModel notificationModel = arrayList.get(getAdapterPosition());
                databaseHelper = new DatabaseHelper(activity);

                adContainerViewBinding.notificationDetail.setTypeface(Typeface.create(adContainerViewBinding.notificationDetail.getTypeface(), Typeface.NORMAL));
                JSONObject json=null;
                String orderId="";
                String clickable="";
                String activityValue="";
                try{

                    json = new JSONObject(notificationModel.getActivityData());
                    clickable=json.getString("clickable");
                    activityValue=json.getString("activity");
                    orderId = json.getString("orderId");

                }
                catch (Exception e){

                }

                if(json!=null) {
                    if (clickable.equals("1")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId",orderId);
                        bundle.putString("viewType","");

                        bundle.putString("remark","");
                        Utility.openActivity(activity,activityValue,bundle);
                        /*
                        if (activityValue.equals("rechargedetails")) {
                            try {


                                Intent intent = new Intent(activity, RechargeDetailsActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("viewType", "rc");
                                intent.putExtra("remark", "");
                                activity.startActivity(intent);
                            } catch (Exception e) {

                            }
                        } else if (notificationModel.getActivityValue().equals("chat")) {
                            Intent intent = new Intent(activity, SupportChatActivity.class);
                            activity.startActivity(intent);
                        }
                        Log.d("notifiifdd", "clicked notifucation " + notificationModel.getActivityData() + "Activity " + notificationModel.getActivityValue());
*/

                    }

                }
                updateNotification(arrayList.get(getAbsoluteAdapterPosition()).getId());

            });

        }
    }

    private void updateNotification(String notiId){

        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("id",notiId);


        String request = Utility.mapWrapper(activity,map1);

        if (Utility.isNetworkConnected(activity)) {

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_UPDATE_NOTIFICATION, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                        }
                    });
        } else {

        }
    }

}
