package in.forpay;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

//import com.balsikandar.crashreporter.CrashReporter;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;

import net.gotev.uploadservice.UploadServiceConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;

public class MyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .build();
        PRDownloader.initialize(this, config);
        createNotificationChannel();

        UploadServiceConfig .initialize(this,"upload_video",true);

       File logsdir = new File(Environment.getExternalStorageDirectory() + File.separator
                + getResources().getString(R.string.app_name) + File.separator + "Logs");

        try {
            if (!logsdir.exists()) {
                logsdir.mkdirs();
            }

            //CrashReporter.initialize(this, logsdir.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void createNotificationChannel(){

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("upload_video", "upload service", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.enableLights(true);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
