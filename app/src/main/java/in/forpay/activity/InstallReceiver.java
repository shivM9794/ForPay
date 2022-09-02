package in.forpay.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.installreferrer.api.InstallReferrerClient;

import in.forpay.util.Constant;

public class InstallReceiver extends BroadcastReceiver {

    InstallReferrerClient referrerClient;

    Context context;
    public static final String KEY_UTM_SOURCE = "utm_source";
    public static final String KEY_UTM_CONTENT = "utm_content";
    public static final String KEY_UTM_CAMPAIGN = "utm_campaign";
    private String installReceiverRefer="step1";

    @Override
    public void onReceive (Context cont, final Intent intent) {

        context = cont;


        if (intent.getStringExtra("referrer") != null) {
            if(!intent.getStringExtra("referrer").equals("")){
                installReceiverRefer = intent.getStringExtra("referrer");
            }
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("installReceiver", installReceiverRefer);
        editor.apply();


    }

}
