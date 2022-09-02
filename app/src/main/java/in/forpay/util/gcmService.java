package in.forpay.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import in.forpay.R;
import in.forpay.activity.HomeActivity;
import in.forpay.activity.InviteActivity;
import in.forpay.activity.RechargeDetailsActivity;
import in.forpay.activity.supportchat.SupportChatActivity;

public class gcmService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //Log.e("GCM","message received 1");
        super.onMessageReceived(remoteMessage);
        //Log.e("GCM","message received"); // this line not getting generated in log

        if(remoteMessage.getNotification()!=null) {
            String message = remoteMessage.getNotification().getBody();
            String title=remoteMessage.getNotification().getTitle();
            //Log.d("GCM","message "+message);




            String jsonData=remoteMessage.getData().get("activityData");
            Log.d("GCM", "Message data payload: " + remoteMessage.getData()+" jsonData "+jsonData);

            showNotification(jsonData,message,title);
        }
    }

    public void showNotification(String jsonData,String message,String title){


        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        JSONObject json=null;
        String orderId="";
        String clickable="";
        String activityValue="";
        try{

            json = new JSONObject(jsonData);
            clickable=json.getString("clickable");
            activityValue=json.getString("activity");
            orderId = json.getString("orderId");

        }
        catch (Exception e){

        }

        if(json!=null) {
            if (clickable.equals("1")) {
                    //Bundle b= new Bundle();


                //Utility.openActivity(getApplicationContext() , activityValue, b);
                if (activityValue.equals("rechargedetails")) {
                    try {


                        intent = new Intent(getApplicationContext(), RechargeDetailsActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("viewType", "rc");
                        intent.putExtra("remark", "");

                    } catch (Exception e) {

                    }
                }
                else if(activityValue.equals("playstoreRating")){

                    Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                    intent = new Intent(Intent.ACTION_VIEW, uri);

                }

                else if (activityValue.equals("chat")) {
                    intent = new Intent(getApplicationContext(), SupportChatActivity.class);

                }
                else if(activityValue.equals("inviteActivity")){
                    intent = new Intent(getApplicationContext(), InviteActivity.class);
                }



            }

        }


        intent.setAction("");
        intent.putExtra("notificationPayload", jsonData);

        // create a pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel channel = null;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            channel = new NotificationChannel("loggedInNotification","ForPay Notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if(manager!=null) {
                manager.createNotificationChannel(channel);
            }
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"loggedInNotification")
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.notification_ico)
                .setContentIntent(pendingIntent)
                .setLights(Color.RED, 3000, 3000)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setChannelId("loggedInNotification")

                ;


        NotificationManagerCompat manager =  NotificationManagerCompat.from(this);
        manager.notify(999,builder.build());



    }

}
