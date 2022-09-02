package in.forpay.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityRechargeDetailsBinding;
import in.forpay.fragment.busbookingfragment.ShowBookingDetailsFragment;
import in.forpay.model.request.RechargeHistory;
import in.forpay.model.response.GetRechargeHistoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;
import in.forpay.util.googleAd;

import static in.forpay.util.Utility.dataWrapper;
import static in.forpay.util.Utility.getCurrentDate;
import static in.forpay.util.Utility.getDeviceIMEI;
import static in.forpay.util.Utility.getToken;
import static in.forpay.util.Utility.getVersionCode;

public class RechargeDetailsActivity extends AppCompatActivity {


    private ActivityRechargeDetailsBinding binding;
    private Activity activity;
    TextView rechargedontv,rechargedontime;
    TextView orderidtv;
    TextView operatortv;
    TextView mobilenumtv;
    TextView rechargeamttv;
    TextView transactionidtv;

    ImageView operatoriv;

    ImageView statusiv;

    TextView statustv;
    TextView accountnametv;


    LinearLayout remarkLabel,bankaccounttvLabel,accountnametvLabel,orderidtvLabel,creditusedLabel,paidAmountLabel;
    RelativeLayout linearLayout1,linearLayout;
    LinearLayout balancebg;
    TextView textViewRefund;


    private AdView adView;
    TextView creditused;
    TextView marginamount;
    TextView bankaccounttv,paidAmount,bankAccountTextView;
    String dirPath="";
    TextView remark;
    String orderId,viewType="";
    String mobile,TransId,amount,reqTime,operatorName,status="PENDING",beneficiaryAccountNumber,beneficiaryName
            ,remarkResponse,strcreditused,strmarginamount,cusCreditUsed,operatorId,opValue1,opValue2,opValue3,opValue4,opValue5,ccf,paymentMode="";

    private DatabaseHelper databaseHelper;
    private ProgressHelper progressHelper;
    private Handler handler;
    private Runnable runnable;
    final int[] runTime={0};
    volatile boolean activityStopped = false;
    PowerManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utility.checkLoggedUser(this);

        activity = RechargeDetailsActivity.this;
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_recharge_details);



//        adView = new AdView(this);
//
//        googleAd gAd = new googleAd(this,adView);
//        if(gAd.getAd()!=null) {
//            binding.adView.loadAd(gAd.getAd());
//        }

        //balancebg=findViewById(R.id.balancebg);

        progressHelper=new ProgressHelper(RechargeDetailsActivity.this);

        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout = findViewById(R.id.linearLayout);

        remarkLabel=findViewById(R.id.remarkLabel);
        bankaccounttvLabel=findViewById(R.id.bankaccounttvLabel);
        accountnametvLabel=findViewById(R.id.accountnametvLabel);
        orderidtvLabel=findViewById(R.id.orderidtvLabel);
        creditusedLabel=findViewById(R.id.creditusedLabel);
        paidAmountLabel=findViewById(R.id.paidAmountLabel);
       //statusiv = findViewById(R.id.statusiv);
        statustv = findViewById(R.id.statustv);
        rechargedontv = findViewById(R.id.rechargedontv);
        rechargedontime = findViewById(R.id.rechargedontime);
        orderidtv = findViewById(R.id.orderidtv);
        operatortv = findViewById(R.id.operatortv);
        mobilenumtv = findViewById(R.id.mobilenumtv);
        rechargeamttv = findViewById(R.id.rechargeamttv);
        transactionidtv = findViewById(R.id.transactionidtv);
        paidAmount=findViewById(R.id.paidAmount);

        operatoriv = findViewById(R.id.operatoriv);

        creditused = findViewById(R.id.creditused);
        //marginamount = findViewById(R.id.marginamount);
        bankaccounttv = findViewById(R.id.bankaccounttv);
        bankAccountTextView=findViewById(R.id.bankAccountTextView);
        accountnametv = findViewById(R.id.accountnametv);
        remark=findViewById(R.id.remark);
        textViewRefund = findViewById(R.id.textViewRefund);


        binding.back.setOnClickListener(v -> onBackPressed());


        orderId = getIntent().getStringExtra("orderId");
        remarkResponse = getIntent().getStringExtra("remark");
        viewType=getIntent().getStringExtra("viewType");

/*
        String TransId = getIntent().getStringExtra("TransId");
        String amount = getIntent().getStringExtra("amount");
        String mobile = getIntent().getStringExtra("mobile");
        String reqTime = getIntent().getStringExtra("reqTime");
        String operatorName = getIntent().getStringExtra("operatorName");
        String status = getIntent().getStringExtra("status");
        String beneficiaryAccountNumber= getIntent().getStringExtra("beneficiaryAccountNumber");
        String beneficiaryName=getIntent().getStringExtra("beneName");
*/

        init();








        textViewRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.disputeOrder(RechargeDetailsActivity.this,"2",orderId);
                init();
            }
        });



        binding.refreshbtn.setOnClickListener(view -> {


            init();

        });
        binding.sharebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    check();
                    return;
                }
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

                Bitmap bm= getScreenShot(rootView);

                store(bm, "screenshot.png");

                linearLayout.setDrawingCacheEnabled(true);
                linearLayout.buildDrawingCache();
                //Bitmap bm = linearLayout.getDrawingCache();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                //String pathhh = MediaStore.Images.Media.insertImage(getContentResolver(), compressedBitmap, "", null);

                Intent intent = new Intent(Intent.ACTION_SEND);
                Bitmap loadedImage = compressedBitmap;
                String path = MediaStore.Images.Media.insertImage(RechargeDetailsActivity.this.getContentResolver(), compressedBitmap, "", null);
                Uri screenshotUri = Uri.parse(path);

                intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                intent.setType("image/*");
                String message="Recharge Successfully Done for " + mobile + "\nRecharge Amount " + "Rs. " + amount + "\nTransaction Id : " + TransId +"\nOperator : "+operatorName;
                if(status.equals("PENDING")){
                    message="Recharge Successfully Requested for " + mobile + "\nRecharge Amount " + "Rs. " + amount + "\nTransaction Id : " + TransId +"\nOperator : "+operatorName;

                }
                else if(status.equals("SUCCESS")){
                    message="Recharge Successfully Done for " + mobile + "\nRecharge Amount " + "Rs. " + amount + "\nTransaction Id : " + TransId +"\nOperator : "+operatorName;

                }
                else if(status.equals("FAILED")){
                    message="Recharge FAILED  for " + mobile + "\nRecharge Amount " + "Rs. " + amount + "\nTransaction Id : " + TransId +"\nOperator : "+operatorName;

                }
                else if(status.equals("DISPUTED")){
                    message="Recharge DISPUTED  for " + mobile + "\nRecharge Amount " + "Rs. " + amount + "\nTransaction Id : " + TransId;

                }

                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(intent, "Share image via..."));


            }
        });

    }


    public static void store(Bitmap bm, String fileName){
        final  String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
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


    public void check() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            //requestStoragePermission();
            checkPermission();
        }
    }

    public void init(){
//        databaseHelper = new DatabaseHelper(RechargeDetailsActivity.this);
//        ArrayList<RechargeHistory> list = databaseHelper.getRechargeHistoryList(orderId,"orderId");\\

        lastFiveTransaction(activity,orderId);


    }

    private void checkStatusBackground(){

        int totalRunTime=0;
        if(status.equals("PENDING")){
            totalRunTime=30;
        }

        else if(status.equals("DISPUTED") || status.equals("SUCCESS")){
            totalRunTime=3;
        }

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
                            //Log.d("Chatlist", "Screen is " + isScreenOn);
                        }
                    }


                    if(status.equals("SUCCESS") && runTime[0]>2){
                        isScreenOn=false;
                    }

                    if(status.equals("FAILED")){
                        isScreenOn=false;
                    }

                    if (isScreenOn) {

                        runTime[0]++;
                        Log.d("Chatlist","id -"+orderId+" run "+runTime[0]);
                        init();
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

    public void onResume(){
        super.onResume();
        //Log.d("Chatlist","App Resumed");
        activityStopped = false;
        checkStatusBackground();
    }
    private boolean isAssetExists(String pathInAssetsDir) {
        AssetManager assetManager = RechargeDetailsActivity.this.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(pathInAssetsDir);
            if (null != inputStream) {
                return true;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return false;
    }

    private void checkPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                //Utility.showToast(PersonalDetailActivity.this, "Location permission is necessary");
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                        "Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
            Toast.makeText(this, "Permission needed to save images and videos", Toast.LENGTH_SHORT).show();
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
    }

    private void lastFiveTransaction(final Activity activity,String orderId) {

        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("orderId",orderId); // amount
        map1.put("limit","10"); // to Account
        String request = Utility.mapWrapper(this,map1);


        if (Utility.isNetworkConnected(activity)) {

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_RECHARGE_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            //Log.e("response his",result);

                            binding.linearLayout1.setVisibility(View.VISIBLE);
                            GetRechargeHistoryResponse response = new Gson().fromJson(result, GetRechargeHistoryResponse.class);

                            if(response!=null) {
                                if (response.getResCode().equals(Constant.CODE_200) || response.getResCode().equals(Constant.CODE_129)) {
                                    convertRechargeList(response.getDataList());
                                }
                            }
                        }
                    });

        }

    }

    private void convertRechargeList(ArrayList<GetRechargeHistoryResponse.Data> dList) {
        if (dList == null || dList.size()==0) {
            return;
        }

        ArrayList<RechargeHistory> list = new ArrayList<>();

        for (GetRechargeHistoryResponse.Data data : dList) {

            //Log.d("database - ",data.getMobile());
            RechargeHistory model = new RechargeHistory(data.getOrderId(), data.getStatus(),
                    data.getService(), data.getTransId(), data.getMobile(), data.getAmount(),
                    data.getCreditUsed(), data.getCusCreditUsed(), data.getMarginAmount(), data.getBeneficiaryAccountNumber(),
                    data.getBeneficiaryName(), data.getReqTime(), data.getOperatorId(),data.getProfit(),data.getOpValue1(),data.getOpValue2(),data.getOpValue3(),
                    data.getOpValue4(),data.getOpValue5(),data.getCcf(),data.getPaymentMode(),data.getVisibleCustomerCopy(),data.getOperatorIcon());
            model.setBottomIconUrl(data.getBottomIconUrl());


//            if (databaseHelper == null) {
//                databaseHelper = new DatabaseHelper(activity);
//            }
//            databaseHelper.insertRechargeHistory(model);

            list.add(model);
        }


        mobile= list.get(0).getMobile();
        TransId=list.get(0).getTransId();
        amount=list.get(0).getAmount();
        reqTime=list.get(0).getReqTime();
        operatorName=list.get(0).getService();
        status=list.get(0).getStatus();
        beneficiaryAccountNumber=list.get(0).getBeneficiaryAccountNumber();
        beneficiaryName=list.get(0).getBeneficiaryName();
        operatorId=list.get(0).getOperatorId();


        strcreditused = list.get(0).getCreditUsed();
        strmarginamount = list.get(0).getMarginAmount();
        cusCreditUsed=list.get(0).getCusCreditUsed();
        opValue1=list.get(0).getOpValue1();
        opValue2=list.get(0).getOpValue1();
        opValue3=list.get(0).getOpValue1();
        opValue4=list.get(0).getOpValue1();
        opValue5=list.get(0).getOpValue1();
        ccf=list.get(0).getCcf();
        paymentMode=list.get(0).getPaymentMode();
        if(!list.get(0).getBottomIconUrl().isEmpty()) {
            Glide.with(this)
                    .load(list.get(0).getBottomIconUrl())
                    .into(binding.bbpsIcon);
            binding.bbpsIcon.setVisibility(View.VISIBLE);
            binding.bbpsBillerLabel.setVisibility(View.VISIBLE);
            binding.bbpsBillDateTxt.setText(beneficiaryName);
            binding.bbpsId.setText(opValue1);
            binding.bbpsBillDateTxt.setText(opValue2);
            binding.bbpsBillPeriodText.setText(opValue3);
            binding.bbpsBillNumberText.setText(opValue4);
            binding.bbpsDueDateText.setText(opValue5);
            binding.bbpsCcfText.setText(ccf);
            binding.bbpsPaymentModeText.setText(paymentMode);

        }


        accountnametvLabel.setVisibility(View.GONE);
        if(!beneficiaryName.isEmpty()){
            accountnametv.setText(beneficiaryName);
            accountnametvLabel.setVisibility(View.VISIBLE);
        }

        //Log.e("Account 1"," Account numner -"+beneficiaryAccountNumber+" order id"+orderId);

        statustv.setText(status);
        remarkLabel.setVisibility(View.GONE);
        if(!remarkResponse.isEmpty()){
            remark.setText(remarkResponse);
            remarkLabel.setVisibility(View.VISIBLE);
        }




        //String strbankaccounttv = getIntent().getStringExtra("bankaccounttv");

        creditused.setText(strcreditused);
        //marginamount.setText(strmarginamount);

        bankaccounttvLabel.setVisibility(View.GONE);
        if(!beneficiaryAccountNumber.isEmpty()){
            bankaccounttv.setText(beneficiaryAccountNumber);
            bankaccounttvLabel.setVisibility(View.VISIBLE);
            if(beneficiaryAccountNumber.contains("@")){
                bankAccountTextView.setText("Email ");
            }

        }

        //bankaccounttv.setText(strbankaccounttv);




        rechargedontv.setText(Utility.convertDateWithSlash(reqTime));
        rechargedontime.setText( Utility.convertTime(reqTime));
        transactionidtv.setText(TransId);
        paidAmount.setText(cusCreditUsed+" RS");
        if(status.equals("FAILED")){
            paidAmountLabel.setVisibility(View.GONE);
        }
        orderidtvLabel.setVisibility(View.GONE);
        textViewRefund.setVisibility(View.GONE);
        creditusedLabel.setVisibility(View.GONE);
        if(viewType.equals("rc")){
            orderidtv.setText(orderId);
            orderidtvLabel.setVisibility(View.VISIBLE);
            textViewRefund.setVisibility(View.GONE);
            creditusedLabel.setVisibility(View.VISIBLE);
        }
        else{
            paidAmountLabel.setVisibility(View.VISIBLE);
        }
        Log.d("RechargeDetailsActivity","view type "+viewType);



        mobilenumtv.setText(mobile);
        rechargeamttv.setText("Rs. " + amount);
        operatortv.setText(operatorName);


        switch (status) {
            case "FAILED":
               /* Glide.with(RechargeDetailsActivity.this).load(R.drawable.failedicon).into(statusiv);

                balancebg.setBackgroundResource(R.drawable.failedrectangle);*/
                linearLayout1.setBackgroundColor(getResources().getColor(R.color.failed));
                rechargeamttv.setTextColor(getResources().getColor(R.color.failed));
//                statustv.setTextColor(getResources().getColor(R.color.failed));
                Glide.with(this)
                        .load(R.drawable.ic_cancel_circle)
                        .into(binding.statuslogo);
                binding.statustitle.setText("Sorry!");

                break;
            case "PENDING":
               /* Glide.with(RechargeDetailsActivity.this).load(R.drawable.pendingicon).into(statusiv);

                balancebg.setBackgroundResource(R.drawable.pendingrectangle);*/
                linearLayout1.setBackgroundColor(getResources().getColor(R.color.pending));
                rechargeamttv.setTextColor(getResources().getColor(R.color.pending));
//                statustv.setTextColor(getResources().getColor(R.color.pending));
                Glide.with(this)
                        .load(R.drawable.ic_baseline_warning_24)
                        .into(binding.statuslogo);
                binding.statustitle.setText("Please Wait!");
                break;
            case "SUCCESS":
                /*Glide.with(RechargeDetailsActivity.this).load(R.drawable.successicon).into(statusiv);

                balancebg.setBackgroundResource(R.drawable.successrectangle);*/
                linearLayout1.setBackgroundColor(getResources().getColor(R.color.success));
                rechargeamttv.setTextColor(getResources().getColor(R.color.success));
//                statustv.setTextColor(getResources().getColor(R.color.success));
                Glide.with(this)
                        .load(R.drawable.ic_check_circle)
                        .into(binding.statuslogo);
                binding.statustitle.setText("Thank You!");
                break;
            case "DISPUTED":
                /*Glide.with(RechargeDetailsActivity.this).load(R.drawable.pendingicon).into(statusiv);

                balancebg.setBackgroundResource(R.drawable.pendingrectangle);*/

//                rechargeamttv.setTextColor(getResources().getColor(R.color.pending));
                Glide.with(this)
                        .load(R.drawable.ic_baseline_warning_24)
                        .into(binding.statuslogo);
                statustv.setTextColor(getResources().getColor(R.color.pending));
                linearLayout1.setBackgroundColor(getResources().getColor(R.color.pending));
                binding.statustitle.setText("Please Wait!");
                break;
            default:
                /*Glide.with(RechargeDetailsActivity.this).load(R.drawable.successicon).into(statusiv);
                linearLayout1.setBackgroundColor(getResources().getColor(R.color.success));
                balancebg.setBackgroundResource(R.drawable.successrectangle);*/
                rechargeamttv.setTextColor(getResources().getColor(R.color.success));
//                statustv.setTextColor(getResources().getColor(R.color.success));
                break;
        }
        binding.statusmsg.setText("Your order "+orderId+" is "+status);

        Utility.imageLoader(RechargeDetailsActivity.this,"https://api.forpay.in/image/operator/"+operatorId+".png",operatoriv);


        Log.d("RechargeDetailsAct"," name "+operatorName+" run "+runTime[0]);
        try {
            if (operatorName.equalsIgnoreCase("Bus") && !TransId.isEmpty() && runTime[0]==0) {

                ShowBookingDetailsFragment showBookingDetailsFragment = new ShowBookingDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BOOKED_ID, TransId);

                bundle.putString("viewType", viewType);
                bundle.putString("viewFrom", "rechargeDetails");


                showBookingDetailsFragment.setArguments(bundle);


                RechargeDetailsActivity.this.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentBusDetailsHolder, showBookingDetailsFragment, ShowBookingDetailsFragment.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();


            }
        }
        catch (Exception e){

        }

    }

}
