package in.forpay.network;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import in.forpay.R;
import in.forpay.util.Constant;
import in.forpay.util.CountingRequestBody;
import in.forpay.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class QueryManager implements CountingRequestBody.UploadCallbacks {

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(35, TimeUnit.SECONDS)
            .writeTimeout(35, TimeUnit.SECONDS)
            .readTimeout(35, TimeUnit.SECONDS)
            .build();

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public String filePath = "";
    public String fileType = "";
    private CountingRequestBody countingRequestBody;

    private String notification_id = "id";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder notificationBuilder;

    private static volatile QueryManager instance = null;
    private ProgressDialog progressBar;
    private static Activity mActivity;

    // private constructor
    private QueryManager() {
    }

    public static QueryManager getInstance() {
        if (instance == null) {
            synchronized (QueryManager.class) {
                // Double select
                if (instance == null) {
                    instance = new QueryManager();
                }
            }
        }
        return instance;
    }

    public static QueryManager getInstance(Activity activity) {
        mActivity = activity;
        if (instance == null) {
            synchronized (QueryManager.class) {
                // Double select
                if (instance == null) {
                    instance = new QueryManager();
                }
            }
        }
        return instance;
    }

    public void postRequest(Activity activity, String method, String json, final CallbackListener callback) {
        Log.d("psaresult", "request " + method + " - " + json);
        if (Utility.isNetworkConnected(activity)) {

            MultipartBody.Builder builder = new MultipartBody.Builder();
            if (json == null) {
                json = "";
            }

            File file = null;
            String fileName = "";

            if (!filePath.isEmpty()) {
                file = new File(filePath);
                fileName = file.getName();
                if (fileType.equalsIgnoreCase("Video")) {
                    Log.d("UploadFile", "postRequest START BODY REQ: ");
                    progressBar = new ProgressDialog(activity);
                    countingRequestBody = new CountingRequestBody(file, this, "video/*");
//                    createUploadFileNotification(activity);
                }

               /* if (fileType.equalsIgnoreCase("Audio")) {
                    Log.d("UploadFile", "postRequest START BODY REQ: ");
                    progressBar = new ProgressDialog(activity);
                    countingRequestBody = new CountingRequestBody(file, this,"audio/*");
//                    createUploadFileNotification(activity);
                }*/
            }

            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("data", json);


            if (file != null) {
                if (fileType.equalsIgnoreCase("Image")) {
                    final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/*");
                    builder.addFormDataPart("image", fileName, RequestBody.create(MEDIA_TYPE_IMAGE, file)).build();
                } else if (fileType.equalsIgnoreCase("shopImage")) {
                    final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/*");
                    builder.addFormDataPart("shopImage", fileName, RequestBody.create(MEDIA_TYPE_IMAGE, file)).build();
                } else if (fileType.equalsIgnoreCase("Video")) {
                    final MediaType MEDIA_TYPE_VIDEO = MediaType.parse("video/*");
//                    builder.addFormDataPart("kyc", fileName, RequestBody.create(MEDIA_TYPE_VIDEO, file)).build();
                    builder.addFormDataPart("kyc", fileName, countingRequestBody).build();
                } else if (fileType.equalsIgnoreCase("Audio")) {
                    final MediaType MEDIA_TYPE_AUDIO = MediaType.parse("audio/*");
//                    builder.addFormDataPart("kyc", fileName, RequestBody.create(MEDIA_TYPE_VIDEO, file)).build();
                    builder.addFormDataPart("audio", fileName, RequestBody.create(MEDIA_TYPE_AUDIO, file)).build();
                }
            }

            RequestBody requestBody = builder.build();
            //Utility.showToast(activity,"Url is "+Constant.BASE_URL+method);
            Request request = new Request.Builder()
                    .url(Constant.BASE_URL + method)

                    .post(requestBody)
                    .build();
            Log.d("psaresult", "json - " + json + " url " + Constant.BASE_URL + method);

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                    Log.d("psaresult", "failed " + e);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResult(e, "", null);
                            filePath = "";
                            fileType = "";
                        }
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {


                    final String result = Utility.decodeString(response.body().string());
                    //final String result = response.body().string();
                    Log.d("psaresult", "onresponse 2 - " + result);
                    // result=Utility.decodeString(result);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Gson gson = new Gson();
                                ResponseManager responseManager = gson.fromJson(result, ResponseManager.class);
                                callback.onResult(null, result, responseManager);
                                filePath = "";
                                fileType = "";
                            } catch (Exception e) {
                                //Log.d("TEST","Exception found - "+e.toString());
                                callback.onResult(e, "", null);
                                filePath = "";
                                fileType = "";
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        Log.d("UploadFile", "onProgressUpdate: " + percentage);
//        updateNotification(percentage);
        progressBar.setProgress(percentage);
    }

    @Override
    public void onError() {
        Log.d("UploadFile", "onError: ");
        if (mActivity != null) {
            Utility.showToastLatest(mActivity, "Something went wrong while upload video. please try again", "ERROR");
        }
    }

    @Override
    public void onFinish() {
        Log.d("UploadFile", "onFinish: ");
        fileType = "";
        progressBar.hide();
        progressBar.dismiss();
//        downloadCompleteNotification();
    }

    @Override
    public void uploadStart() {
        Log.d("UploadFile", "uploadStart: ");
        showUploadProgress();
    }

    private void createUploadFileNotification(Activity activity) {
        mNotificationManager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(notification_id, "an", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Video Uploading..");
            notificationChannel.setSound(null, null);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.enableVibration(false);
        }

        notificationBuilder = new NotificationCompat.Builder(activity, notification_id)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setProgress(0, 0, true)
                .setContentTitle(activity.getResources().getString(R.string.app_name))
                .setContentText("Video Uploading..")
                .setDefaults(0).setAutoCancel(true);

        mNotificationManager.notify(0, notificationBuilder.build());

    }

    private void updateNotification(int downloadItemProgress) {
        notificationBuilder.setProgress(100, downloadItemProgress, false);
        notificationBuilder.setContentText("Upload : " + downloadItemProgress + "%");
        mNotificationManager.notify(0, notificationBuilder.build());
    }

    private void downloadCompleteNotification() {
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setOngoing(false);
        notificationBuilder.setContentText("Video upload successfully");
        notificationBuilder.setTicker("Video upload successfully");
        notificationBuilder.setOngoing(false);
        mNotificationManager.notify(0, notificationBuilder.build());
    }

    private void showUploadProgress() {
        progressBar.setMessage("KYC Video Uploading...");
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.setMax(100);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.show();
    }

}
