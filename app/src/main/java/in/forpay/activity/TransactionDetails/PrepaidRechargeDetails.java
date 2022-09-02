package in.forpay.activity.TransactionDetails;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import in.forpay.R;
import in.forpay.adapter.ItemPaymentSummaryAdapter;
import in.forpay.databinding.ActivityPrepaidMobileRechargeDetailsBinding;
import in.forpay.model.response.GetPaymentSummaryModel;
import in.forpay.model.response.GetRechargeHistoryResponse;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class PrepaidRechargeDetails extends AppCompatActivity {


    private ActivityPrepaidMobileRechargeDetailsBinding binding;
    private Activity activity;
    private Bundle bundle;
    private String status = "";
    private String orderId = "";
    volatile boolean activityStopped = false;
    private Handler handler;
    private Runnable runnable;
    final int[] runTime = {0};
    SharedPreferences sharedPreferences;
    PowerManager pm;
    File imagePath;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private ArrayList<GetPaymentSummaryModel> listData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utility.checkLoggedUser(this);
        sharedPreferences = getSharedPreferences(Constant.STORAGE_NAME, Context.MODE_PRIVATE);


        activity = PrepaidRechargeDetails.this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_prepaid_mobile_recharge_details);


        if (getIntent().getBundleExtra("bundle") != null) {
            bundle = getIntent().getBundleExtra("bundle");
            String jsonData = bundle.getString("jsonData");
            String orderId = bundle.getString("orderId");
            loadData(jsonData);


        }

        binding.ivCut.setOnClickListener(v -> {
            finish();
        });


//        verifyStoragePermission(this);
        binding.shareReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                takeScreenShot(getWindow().getDecorView());


                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    check();
                    return;
                }
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

                Bitmap bm = getScreenShot(rootView);

                store(bm, "screenshot.png");

//                linearLayout.setDrawingCacheEnabled(true);
//                linearLayout.buildDrawingCache();
                //Bitmap bm = linearLayout.getDrawingCache();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                //String pathhh = MediaStore.Images.Media.insertImage(getContentResolver(), compressedBitmap, "", null);

                Intent intent = new Intent(Intent.ACTION_SEND);
                Bitmap loadedImage = compressedBitmap;
                String path = MediaStore.Images.Media.insertImage(PrepaidRechargeDetails.this.getContentResolver(), compressedBitmap, "", null);
                Uri screenshotUri = Uri.parse(path);

                intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                intent.setType("image/*");

                startActivity(Intent.createChooser(intent, "Share image via..."));


            }
        });
    }

    public static void store(Bitmap bm, String fileName) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }


    private void takeScreenShot(View view) {

        Date date = new Date();
        CharSequence format = DateFormat.format("MM-dd-yyyy_hh:mm:ss", date);
        try {
            File mainDir = new File(
                    this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FilShare");

            if (!mainDir.exists()) {
                try {
                    boolean mkdir = mainDir.mkdirs();

                } catch (Exception e) {

                }

            } else {

            }

            String path = mainDir + "-" + format + ".png";

            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            File imageFile = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //shareScreenShot(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shareScreenShot(File imageFile) {

        Uri uri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + "." + getLocalClassName() + ".provider", imageFile);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Recepit");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            this.startActivity(Intent.createChooser(intent, "Share With"));
        } catch (ActivityNotFoundException e) {
//            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }


    private static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


    private void checkStatus() {


        switch (status) {
            case "FAILED":

                binding.linearLayout1.setBackgroundColor(getResources().getColor(R.color.failed));

                Glide.with(this)
                        .load(R.drawable.ic_cancel_circle)
                        .into(binding.verifiedLogo1);

                break;
            case "PENDING":

                binding.linearLayout1.setBackgroundColor(getResources().getColor(R.color.pending));

                Glide.with(this)
                        .load(R.drawable.ic_baseline_warning_24)
                        .into(binding.verifiedLogo1);
                break;
            case "SUCCESS":

                binding.linearLayout1.setBackgroundColor(getResources().getColor(R.color.success));
                Glide.with(this)
                        .load(R.drawable.ic_check_circle)
                        .into(binding.verifiedLogo1);
                break;
            case "DISPUTED":
                Glide.with(this)
                        .load(R.drawable.ic_baseline_warning_24)
                        .into(binding.verifiedLogo1);
                binding.linearLayout1.setBackgroundColor(getResources().getColor(R.color.pending));
                break;
            default:

                break;
        }

    }

    private void loadData(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            status = jsonObject.getString("status");
            String statusTitle = jsonObject.getString("statusTitle");
            String transactionDiscription = jsonObject.getString("transactionDiscription");
            String payment_summary_title = jsonObject.getString("payment_summary_title");

            String operatorIcon = jsonObject.getString("operatorIcon");
            String rechargeTime = jsonObject.getString("rechargeTime");
            String middleIcon = jsonObject.getString("middleIcon");
            String tickIcon = jsonObject.getString("tickIcon");

            JSONArray jsonArray = jsonObject.getJSONArray("paymentSummary");


            listData = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                GetPaymentSummaryModel usModel = new GetPaymentSummaryModel();

                JSONObject obj = jsonArray.getJSONObject(i);

                usModel.setTitle(obj.getString("title"));
                usModel.setAmount(obj.getString("amount"));

                listData.add(usModel);

            }
            binding.recyclerPaymentSummary.setLayoutManager(new LinearLayoutManager(activity));
            ItemPaymentSummaryAdapter itemPaymentSummaryAdapter = new ItemPaymentSummaryAdapter(activity, listData, "");
            binding.recyclerPaymentSummary.setAdapter(itemPaymentSummaryAdapter);
            itemPaymentSummaryAdapter.notifyDataSetChanged();

            JSONArray jsonArray2 = jsonObject.getJSONArray("orderSummary");


            listData = new ArrayList<>();
            for (int i = 0; i < jsonArray2.length(); i++) {
                GetPaymentSummaryModel usModel = new GetPaymentSummaryModel();

                JSONObject obj = jsonArray2.getJSONObject(i);

                usModel.setTitle(obj.getString("title"));
                usModel.setAmount(obj.getString("amount"));

                listData.add(usModel);

            }


            binding.recyclerOrderDetails.setLayoutManager(new LinearLayoutManager(activity));
            ItemPaymentSummaryAdapter itemPaymentSummaryAdapter2 = new ItemPaymentSummaryAdapter(activity, listData, "orderDetails");
            binding.recyclerOrderDetails.setAdapter(itemPaymentSummaryAdapter2);
            itemPaymentSummaryAdapter2.notifyDataSetChanged();
            checkStatus();


            if (!middleIcon.isEmpty()) {
                binding.middleImageLinear.setVisibility(View.VISIBLE);
                binding.middleImage.setVisibility(View.VISIBLE);
                Utility.imageLoader(activity, middleIcon, binding.middleImage);
            }
            binding.txtStatusTitle.setText(statusTitle);
            binding.txtTransactionDescription.setText(transactionDiscription);
            binding.paymentSummaryTitle.setText(payment_summary_title);
//            Utility.imageLoader(activity,tickIcon,binding.verifiedLogo);
//            Utility.imageLoader(activity,tickIcon,binding.verifiedLogo1);
//            Utility.imageLoader(activity,tickIcon,binding.icon);
/*
            binding.label1Title.setText(label_1_title);
            binding.remarkTitle.setText(remarkTitle);
            binding.remark.setText(Html.fromHtml(remark), TextView.BufferType.SPANNABLE);
            binding.orderDetails.setText(Html.fromHtml(orderTitle), TextView.BufferType.SPANNABLE);
            binding.transactionTitle.setText(Html.fromHtml(transactionTitle), TextView.BufferType.SPANNABLE);

            binding.remarkTitle.setText(remarkTitle);

 */
            Utility.imageLoader(activity, operatorIcon, binding.icon);
            binding.txtRechargeDate.setText(rechargeTime);


        } catch (Exception e) {
            Utility.showToastLatest(activity, e.toString(), "ERROR");
        }
    }


//    public static void store(Bitmap bm, String fileName) {
//        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
//        File dir = new File(dirPath);
//        if (!dir.exists())
//            dir.mkdirs();
//        File file = new File(dirPath, fileName);
//        try {
//            FileOutputStream fOut = new FileOutputStream(file);
//            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
//            fOut.flush();
//            fOut.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public static Bitmap getScreenShot(View view) {
//        View screenView = view.getRootView();
//        screenView.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
//        screenView.setDrawingCacheEnabled(false);
//        return bitmap;
//
//    }


    private void checkStatusBackground() {


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!activityStopped) {
                    boolean isScreenOn = true;
                    handler.postDelayed(this, 5000);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                        if (pm != null) {
                            isScreenOn = pm.isInteractive();
                            //Log.d("PrepaidMobileRecharge", "Screen is " + isScreenOn);
                        }
                    }
                    //Log.d("PrepaidMobileRecharge", "status is  " + status +"Runtime "+runTime[0]);

                    if (status.equals("SUCCESS") && runTime[0] > 0) {
                        isScreenOn = false;
                        handler.removeCallbacksAndMessages(null);
                    }

                    if (status.equals("FAILED")) {
                        isScreenOn = false;
                        handler.removeCallbacksAndMessages(null);
                    }

                    if (isScreenOn) {
                        Utility.getOrderDetails(activity, bundle, "recentHistory");
                        String historyData = sharedPreferences.getString("recentHistory", "");

                        try {
                            GetRechargeHistoryResponse response = new Gson().fromJson(historyData, GetRechargeHistoryResponse.class);
                            String jsonData = response.getDataList().get(0).getJsonData();

                            loadData(jsonData);
                            Log.d("PrepaidMobileRecharge", "History Data  ");


                        } catch (Exception e) {

                        }
                        runTime[0]++;

                    }
                }
            }
        };

        handler.postDelayed(runnable, 5000);

    }


    public void onPause() {

        super.onPause();
        //Log.d("Chatlist","App paused");
        activityStopped = true;
    }

    public void onResume() {
        super.onResume();
        //Log.d("Chatlist","App Resumed");
        activityStopped = false;
        checkStatusBackground();
    }
}
