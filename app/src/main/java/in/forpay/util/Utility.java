package in.forpay.util;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.iamhabib.ratingrequestlibrary.RatingRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Time;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import in.forpay.R;
import in.forpay.about.About_UsActivity;
import in.forpay.activity.ActiveDeviceActivity;
import in.forpay.activity.AddFundNewActivity;
import in.forpay.activity.BalanceHistoryActivity;
import in.forpay.activity.BankDetailsActivity;
import in.forpay.activity.BeneficiaryValidationActivity;
import in.forpay.activity.BuyTShirtActivity;
import in.forpay.activity.CheckOut.CouponPrepaidActivity;
import in.forpay.activity.CheckOut.PrepaidMobileRecharge;
import in.forpay.activity.ContactUsActivity;
import in.forpay.activity.DailyEarningActivity;
import in.forpay.activity.Distributorship.DistributorNewActivity;
import in.forpay.activity.Distributorship.MyRetailerActivity;
import in.forpay.activity.FavContactActivity;
import in.forpay.activity.FundRequestActivity;
import in.forpay.activity.InviteActivity;
import in.forpay.activity.LanguageChangeActivity;
import in.forpay.activity.LoginHistoryActivity;
import in.forpay.activity.PanAgentActivity;
import in.forpay.activity.PayoutNewActivity;
import in.forpay.activity.PlusActivity;
import in.forpay.activity.QrCodeDecoder;
import in.forpay.activity.QrPaymentGenerator;
import in.forpay.activity.ScratchCard;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.activity.SplashActivity;
import in.forpay.activity.TransactionDetails.PrepaidRechargeDetails;
import in.forpay.activity.Webview.WebViewBrowser;
import in.forpay.activity.aeps.StartAepsApiActivity;
import in.forpay.activity.aeps.StartAepsNewActivity;
import in.forpay.activity.balance.CreditCardBillPayment;
import in.forpay.activity.balance.DmtActivity;
import in.forpay.activity.balance.EWalletActivity;
import in.forpay.activity.balance.GiftVoucherActivity;
import in.forpay.activity.balance.LandLineActivity;
import in.forpay.activity.balance.MobileActivity;
import in.forpay.activity.bbps.BbpsComplaint;
import in.forpay.activity.busbooking.BusBookingActivity;
import in.forpay.activity.busbooking.BusBookingDetails;
import in.forpay.activity.flightbooking.FlightBookingActivity;
import in.forpay.activity.forpaypremierleague.FPLActivity;
import in.forpay.activity.forpaypremierleague.FPLNewActivity;
import in.forpay.activity.kyc.AadhaarKycActivity;
import in.forpay.activity.kyc.CheckKycActivity;
import in.forpay.activity.moneytransfer.MoneyTransferActivity;
import in.forpay.activity.newaeps.StartNewAepsApiActivity;
import in.forpay.activity.premiumplan.PremiumPlanActivity;
import in.forpay.activity.profile.ProfileActivity;
import in.forpay.activity.recharge.RechargeActivityNew;
import in.forpay.activity.shop.ShopActivity;
import in.forpay.activity.smsrecharge.SMSRechargeActivity;
import in.forpay.activity.supportchat.SupportChatActivity;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.AdapterNoDataFoundBinding;
import in.forpay.databinding.DialogToastErrorBinding;
import in.forpay.databinding.FragmentLogoutBinding;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.listener.Listener;
import in.forpay.model.ContactList;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.model.response.GetBalanceResponse;
import in.forpay.model.response.GetLoginValidateResponse;
import in.forpay.model.response.GetRechargeHistoryResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.model.response.GetRefundReprocessResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;

public class Utility {

    private static DatabaseHelper databaseHelper;
    private static ProgressHelper progressHelper;
    private static Boolean requestDone = false;
    private static int currentPage = 0;
    private static List<String> images2;
    public static final String RD_SERVICE_CAPTURE = "in.gov.uidai.rdservice.fp.CAPTURE";
    public static final String RD_SERVICE_INFO = "in.gov.uidai.rdservice.fp.INFO";


    public static void locationPermissionDialog(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {

            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    getLatLong(activity, false);
                    //Log.d(TAG, "checkPermission: onPermissionGranted");
                    getCurrentLocation(activity, true);

                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    //Log.d(TAG, "checkPermission: onPermissionDenied");
                    Utility.showToast(activity, "Permission Denied", "");
                    Utility.getCurrentLocation(activity, false);

                    //progressHelper.dismiss();
                }
            };

            TedPermission.with(activity)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                            "Please turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                    .check();
            //Log.d(TAG, "VERSION: Build.VERSION.SDK_INT >= 23");
        }

    }


    public static void setLocalEmail(Activity activity, String email) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_EMAIL, email);
    }

    public static String getLocalEmail(Activity activity) {
        return PreferenceConnector.readString(activity, PreferenceConnector.USER_EMAIL, "");
    }


    public static void setFirstApiRespose(Activity activity, String name) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.FIRST_API_RESPONSE, name);
    }

    public static String getFirstApiResponse(Activity activity) {
        return PreferenceConnector.readString(activity, PreferenceConnector.FIRST_API_RESPONSE, "");
    }

    public static void setLocalName(Activity activity, String name) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_LOCAL_NAME, name);
    }

    public static String getLocalName(Activity activity) {
        return PreferenceConnector.readString(activity, PreferenceConnector.USER_LOCAL_NAME, "");
    }

    public static void showNewBalance(String bal, Activity activity) {

        Dialog balanced;
        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
        View wv = activity.getLayoutInflater().inflate(R.layout.balance_dialog, null, false);
        TextView newbal = wv.findViewById(R.id.newbal);

        newbal.setText("₹" + bal);

        alert.setView(wv);

        balanced = alert.create();
        balanced.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        balanced.show();
        // Hide after some seconds
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (balanced.isShowing()) {
                        balanced.dismiss();
                    }
                } catch (Exception e) {

                }
            }
        };

        balanced.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 2000);

    }


    public static void getOrderDetails(Activity activity, Bundle bundle, String storageName) {
        String orderId = bundle.getString("orderId");
        String viewType = bundle.getString("viewType");
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.STORAGE_NAME, Context.MODE_PRIVATE);

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("orderId", orderId);
        map1.put("viewType", viewType);


        String request = Utility.mapWrapper(activity, map1);

        if (Utility.isNetworkConnected(activity)) {
            //progressHelper = new ProgressHelper(activity);
            //progressHelper.show();

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_RECHARGE_HISTORY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(storageName, result);
                            editor.apply();
                        }
                    });
        }

    }


    public static void showInsufficientDialog(Activity activity, String amount, String locationRequired, String urid) {

        String msg = "You don't have sufficient balance in your wallet. Require to add " + amount + " more.";

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        View wv = LayoutInflater.from(activity).inflate(R.layout.error_dialog, null, false);
        Button gotit = wv.findViewById(R.id.back_btn);
        TextView emoji = wv.findViewById(R.id.emoji);

        emoji.setText("\uD83D\uDCB8");

        gotit.setText("Add Now");
        TextView textView = wv.findViewById(R.id.error_msg);
        if (!msg.isEmpty())
            textView.setText(msg);
        alert.setView(wv);
        alert.setCancelable(false);
        final Dialog dialog = alert.create();

        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.orange_border_white_fill_bg));
        dialog.show();

        gotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                openAddFund(activity, amount, locationRequired, urid);
            }
        });

    }


    public static void showPopup(Activity activity, GetRechargeValidateResponse.PopupData popupData) {

        if (popupData != null && popupData.getDataList() != null) {
            if (popupData.getDataList().size() > 0) {
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_popup);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView textViewCancel = dialog.findViewById(R.id.textViewCancel);

                TextView title = dialog.findViewById(R.id.title);

                LinearLayout linearLayout = dialog.findViewById(R.id.mainLinear);

                title.setText(popupData.getTitle());

                initPopUpUI(popupData.getDataList(), linearLayout, activity);

                textViewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        }

    }


    public static void openCheckOutActivity(Activity activity, String value, Bundle bundle) {

        Intent intent;
        switch (value) {
            case "prepaidMobileRecharge":
            case "dthRecharge":
            case "beneficiaryValidation":
                intent = new Intent(activity, PrepaidMobileRecharge.class);
                intent.putExtra("bundle", bundle);
                activity.startActivity(intent);
                break;

            default:
                intent = new Intent(activity, PrepaidMobileRecharge.class);
                intent.putExtra("bundle", bundle);
                activity.startActivity(intent);
                break;
        }
    }


    public static void openTransactionDetailsActivity(Activity activity, Bundle bundle) {

        String orderId = bundle.getString("orderId");
        String viewType = bundle.getString("viewType");
        String value = "";

        Log.d("PrepaidMobileRecharge", "orderId " + orderId);
        if (activity != null) {

            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(activity)); // key
            map1.put("imei", Utility.getImei(activity)); // imei
            map1.put("versionCode", Utility.getVersionCode(activity)); // version code
            map1.put("os", Utility.getOs(activity));
            map1.put("orderId", orderId);
            map1.put("viewType", viewType);


            String request = Utility.mapWrapper(activity, map1);


            if (Utility.isNetworkConnected(activity)) {
                progressHelper = new ProgressHelper(activity);
                progressHelper.show();

                QueryManager.getInstance().postRequest(activity,
                        Constant.METHOD_RECHARGE_HISTORY, request, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {
                                progressHelper.dismiss();

                                openTransactionDetailsInHisActivity(result, activity);

                            }
                        });
            }
        }


    }


    public static void openTransactionDetailsInHisActivity(String result, Activity activity) {

        try {
            GetRechargeHistoryResponse response = new Gson().fromJson(result, GetRechargeHistoryResponse.class);
            SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.STORAGE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("recentHistory", result);
            editor.apply();
            if (response.getDataList().size() >= 1) {
                Log.d("PrepaidMobileRecharge", "response 1 is " + result);
                String value = response.getDataList().get(0).getServiceType();
                String jsonData = response.getDataList().get(0).getJsonData();
                Log.d("PrepaidMobileRecharge", "response 1 value " + value);

                Intent intent;
                Bundle bundle = new Bundle();
                bundle.putString("jsonData", jsonData);
                bundle.putString("orderId", response.getDataList().get(0).getOrderId());

                if(value.equals("bus") && response.getDataList().get(0).getStatus().equals("FAILED")){
                    value="prepaidMobileRecharge";
                }

                switch (value) {
                    case "prepaidMobileRecharge":
                    case "dthRecharge":
                    case "beneficiaryValidation":
                        intent = new Intent(activity, PrepaidRechargeDetails.class);
                        intent.putExtra("bundle", bundle);
                        activity.startActivity(intent);
                        break;
                    case "bus":
                        intent = new Intent(activity, BusBookingDetails.class);
                        intent.putExtra("bundle", bundle);
                        activity.startActivity(intent);
                        break;
                    default:
                        intent = new Intent(activity, PrepaidRechargeDetails.class);
                        intent.putExtra("bundle", bundle);
                        activity.startActivity(intent);
                        break;

                }
            } else {
                Utility.showToastLatest(activity, response.getResText(), response.getResCode());
            }
        } catch (Exception e) {

        }

    }

    public static void initPopUpUI(ArrayList<GetRechargeValidateResponse.DataList> dataList, LinearLayout linearLayout, Activity activity) {

        try {
            for (int i = 0; i < dataList.size(); i++) {


                View secondLay = LayoutInflater.from(activity).inflate(R.layout.item_dialog_popup, null);

                TextView label = secondLay.findViewById(R.id.label);
                TextView value = secondLay.findViewById(R.id.value);

                label.setText(dataList.get(i).getLabel());
                value.setText(dataList.get(i).getValue());

                linearLayout.addView(secondLay);
            }
        } catch (Exception e) {

        }

    }


    public static void openAddFund(Activity activity, String amount, String locationRequired, String urid) {
        Intent intent = new Intent(activity, AddFundNewActivity.class);
        intent.putExtra("from", "RechargeProcessActivity");
        intent.putExtra("Amount", amount);
        intent.putExtra("urid", urid);
        if (locationRequired.equals("yes")) {

            if (Build.VERSION.SDK_INT >= 23) {

                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Utility.getLatLong(activity, false);
                        //Log.d(TAG, "checkPermission: onPermissionGranted");
                        Utility.getCurrentLocation(activity, true);
                        activity.startActivityForResult(intent, 101);

                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        //Log.d(TAG, "checkPermission: onPermissionDenied");
                        Utility.showToast(activity, "Permission Denied", "");
                        Utility.getCurrentLocation(activity, false);
                        activity.startActivityForResult(intent, 101);
                        //progressHelper.dismiss();
                    }
                };

                TedPermission.with(activity)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                                "Please turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                        .check();
                //Log.d(TAG, "VERSION: Build.VERSION.SDK_INT >= 23");
            }


        } else
            activity.startActivityForResult(intent, 101);
    }

    public static void openActivity(Activity activity, String value, Bundle bundle) {

        //Log.d("sdhfksjf","Clicked "+value);
        Intent intent;
        switch (value) {
            case "qrcodeDecoder":
                intent = new Intent(activity, QrCodeDecoder.class);
                intent.putExtra("bundle", bundle);
                activity.startActivity(intent);
                break;
            case "scratchCard":
                intent = new Intent(activity, ScratchCard.class);
                intent.putExtra("bundle", bundle);
                activity.startActivity(intent);
                break;
            case "shopActivity":
                intent = new Intent(activity, ShopActivity.class);
                intent.putExtra("bundle", bundle);
                activity.startActivity(intent);
                break;

            case "myRetailer":
                intent = new Intent(activity, MyRetailerActivity.class);
                intent.putExtra("bundle", bundle);
                activity.startActivity(intent);
                break;

            case "webActivity":
                intent = new Intent(activity, WebViewBrowser.class);
                intent.putExtra("bundle", bundle);
                activity.startActivity(intent);
                break;

            case "acceptQrPayment":
                activity.startActivity(new Intent(activity, QrPaymentGenerator.class));
                break;

            case "buyTshirt":
                activity.startActivity(new Intent(activity, BuyTShirtActivity.class));
                break;

            case "beneficiaryValidation":
                activity.startActivity(new Intent(activity, BeneficiaryValidationActivity.class));
                break;

            case "favContact":
                activity.startActivity(new Intent(activity, FavContactActivity.class));
                break;

            case "balanceHistory":
                activity.startActivity(new Intent(activity, BalanceHistoryActivity.class));
                break;

            case "bankDetails":
                activity.startActivity(new Intent(activity, BankDetailsActivity.class));
                break;

            case "fundRequest":
                activity.startActivity(new Intent(activity, FundRequestActivity.class));
                break;

            case "loginHistory":
                activity.startActivity(new Intent(activity, LoginHistoryActivity.class));
                break;

            case "aepsActivity":
                activity.startActivity(new Intent(activity, StartAepsApiActivity.class));
                break;

            case "aepsNewActivity":
                activity.startActivity(new Intent(activity, StartAepsNewActivity.class));
                break;

            case "plusActivity":
                intent = new Intent(activity, PlusActivity.class);
                intent.putExtra("bundle", bundle);
                activity.startActivity(intent);
                break;

            case "profile":
                activity.startActivity(new Intent(activity, ProfileActivity.class).putExtra(Constant.POSITION, 0));
                break;

            case "pin":
                activity.startActivity(new Intent(activity, ProfileActivity.class).putExtra(Constant.POSITION, 1));
                break;

            case "password":
                activity.startActivity(new Intent(activity, ProfileActivity.class).putExtra(Constant.POSITION, 2));
                break;

            case "bus":
                activity.startActivity(new Intent(activity, BusBookingActivity.class));
                break;

            case "rechargeHistory":
                activity.startActivity(new Intent(activity, RechargeActivityNew.class));
                break;

            case "dmt":
                activity.startActivity(new Intent(activity, DmtActivity.class));
                break;

            case "moneyTransfer":
                activity.startActivity(new Intent(activity, MoneyTransferActivity.class));
                break;
            case "dailySummary":
                activity.startActivity(new Intent(activity, DailyEarningActivity.class));
                break;

            case "pan":
                activity.startActivity(new Intent(activity, PanAgentActivity.class));
                break;

            case "digitalGiftVoucher":
                intent = new Intent(activity, GiftVoucherActivity.class);
                intent.putExtra("bundle", bundle);
                activity.startActivity(intent);
                break;

            case "metro":
                activity.startActivity(new Intent(activity, MobileActivity.class).putExtra(Constant.TYPE, "metro"));
                break;

            case "bbpsGasBillPayment":
            case "bbpsLandlinePostpaid":
            case "bbpsInsurance":
            case "bbpsWater":
            case "bbpsElectricity":
            case "bbpsBroadbandPostpaid":
            case "bbpsFasttag":
            case "bbpsPostpaidMobileRecharge":
            case "bbpsLpg":
            case "bbpsMetro":
            case "bbpsCreditCard":
            case "bbpsLoanPayment":
            case "bbpsLifeInsurance":
            case "bbpsHealthInsurance":
            case "bbpsCableTvRecharge":
            case "bbpsHousingSociety":
            case "bbpsMunicipalService":
            case "bbpsHospital":
            case "bbpsSubscription":
            case "bbpsEducationFees":

                intent = new Intent(activity, LandLineActivity.class);
                intent.putExtra(Constant.TYPE, value);
                if (bundle != null) {
                    intent.putExtra(Constant.TITLE_SHOW, bundle.getString(Constant.TITLE_SHOW))
                            .putExtra(Constant.INPUT_DATA, bundle.getString(Constant.INPUT_DATA))
                            .putExtra(Constant.FAV_MOBILE, bundle.getString(Constant.FAV_MOBILE))
                            .putExtra(Constant.FAV_INPUTVALUE1, bundle.getString(Constant.FAV_INPUTVALUE1))
                            .putExtra(Constant.FAV_INPUTVALUE2, bundle.getString(Constant.FAV_INPUTVALUE2));
                }
                activity.startActivity(intent);

                break;

            case "prepaidMobileRecharge":
            case "dthRecharge":


                intent = new Intent(activity, MobileActivity.class);
                intent.putExtra(Constant.TYPE, value);
                if (bundle != null) {
                    intent.putExtra(Constant.TITLE_SHOW, bundle.getString(Constant.TITLE_SHOW))
                            .putExtra(Constant.INPUT_DATA, bundle.getString(Constant.INPUT_DATA))
                            .putExtra(Constant.FAV_MOBILE, bundle.getString(Constant.FAV_MOBILE))
                            .putExtra(Constant.FAV_INPUTVALUE1, bundle.getString(Constant.FAV_INPUTVALUE1))
                            .putExtra(Constant.FAV_INPUTVALUE2, bundle.getString(Constant.FAV_INPUTVALUE2));
                }
                activity.startActivity(intent);


                //showToast(activity,"Input title -"+Constant.INPUT_DATA+" Value "+bundle.getString(Constant.INPUT_DATA));
                break;

            case "ewalletRecharge":
                intent = new Intent(activity, EWalletActivity.class);
                intent.putExtra(Constant.TYPE, value);
                if (bundle != null) {
                    intent.putExtra(Constant.TITLE_SHOW, bundle.getString(Constant.TITLE_SHOW))
                            .putExtra(Constant.INPUT_DATA, bundle.getString(Constant.INPUT_DATA))
                            .putExtra(Constant.FAV_MOBILE, bundle.getString(Constant.FAV_MOBILE))
                            .putExtra(Constant.FAV_INPUTVALUE1, bundle.getString(Constant.FAV_INPUTVALUE1))
                            .putExtra(Constant.FAV_INPUTVALUE2, bundle.getString(Constant.FAV_INPUTVALUE2));
                }
                activity.startActivity(intent);
                break;

            case "flightBooking":
                intent = new Intent(activity, FlightBookingActivity.class);
                activity.startActivity(intent);
                break;


            case "smsRecharge":
                intent = new Intent(activity, SMSRechargeActivity.class);
                activity.startActivity(intent);
                break;

            case "myCoupons":
                intent = new Intent(activity, CouponPrepaidActivity.class);
                activity.startActivity(intent);
                break;

            case "fplContest":
                intent = new Intent(activity, FPLNewActivity.class);
                activity.startActivity(intent);
                break;


            case "social":
                intent = new Intent(activity, About_UsActivity.class);
                activity.startActivity(intent);
                break;

            case "logout":
                Utility.logoutDialog(activity);
                break;


            case "addFund": //Add fund
                activity.startActivity(new Intent(activity, AddFundNewActivity.class));
                break;
            case "contactUs": //Contact us
                activity.startActivity(new Intent(activity, ContactUsActivity.class));
                break;

            case "changeLanguage": //Change Language
                activity.startActivity(new Intent(activity, LanguageChangeActivity.class));
                break;

//            case "":
//                activity.startActivity(new Intent(activity, ProfileActivity.class));
//                break;
            case "referAndEarn":
                activity.startActivity(new Intent(activity, InviteActivity.class));
                break;

            case "distributorship":
                activity.startActivity(new Intent(activity, DistributorNewActivity.class));
                break;
            case "creditCardBillPayment":
                activity.startActivity(new Intent(activity, CreditCardBillPayment.class));
                break;
            case "chatSupport":
                activity.startActivity(new Intent(activity, SupportChatActivity.class));
                break;

            case "addKyc":
                activity.startActivity(new Intent(activity, CheckKycActivity.class));
                break;
            case "aadhaarKyc":
                activity.startActivity(new Intent(activity, AadhaarKycActivity.class));
                break;

            case "premiumPlan":
                activity.startActivity(new Intent(activity, PremiumPlanActivity.class));
                break;
            case "payout":
                activity.startActivity(new Intent(activity, PayoutNewActivity.class));
                break;
            case "bbps":
                activity.startActivity(new Intent(activity, BbpsComplaint.class));
                break;
            case "activateDevice":
                activity.startActivity(new Intent(activity, ActiveDeviceActivity.class));
                break;

        }
    }

    public static void logoutDialog(final Activity activity) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.fragment_logout, null);
        builder.setView(view);
        FragmentLogoutBinding bind = DataBindingUtil.bind(view);
        final androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        assert bind != null;
        bind.btnLogout.setOnClickListener(v -> {
            dialog.dismiss();
            logout("", activity);
        });

        bind.btnLogoutAll.setOnClickListener(v -> {
            dialog.dismiss();
            logout("all", activity);
        });
        dialog.show();
    }


    private static void logout(String type, Activity activity) {
        String imei = PreferenceConnector.readString(activity, PreferenceConnector.IMEI, "");
        String token = Utility.getToken(activity);
        String activeKey = PreferenceConnector.readString(activity,
                PreferenceConnector.ACTIVE_KEY, "");
        String mobile = Utility.getUsername(activity);

        PreferenceConnector.clear(activity);
        PreferenceConnector.writeString(activity, PreferenceConnector.IMEI, imei);
        PreferenceConnector.writeString(activity, PreferenceConnector.ACTIVE_KEY, activeKey);
        PreferenceConnector.writeString(activity, PreferenceConnector.USER_NAME, mobile);
        // Delete db table
        DatabaseHelper databaseHelper = new DatabaseHelper(activity);
        databaseHelper.deleteServiceTypeTable(); // Delete service type table
        //databaseHelper.deleteOrderIdTable(); // Delete order id table
        databaseHelper.deleteRechargeHistoryTable(); // Delete recharge history table
        OxyMePref oxyMePref = new OxyMePref(activity);
        oxyMePref.clear();


        if (activity != null) {

            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(activity)); // key
            map1.put("imei", Utility.getImei(activity)); // imei
            map1.put("versionCode", Utility.getVersionCode(activity)); // version code
            map1.put("os", Utility.getOs(activity));
            map1.put("type", type); // cardNumber


            String request = Utility.mapWrapper(activity, map1);


            if (Utility.isNetworkConnected(activity)) {
                progressHelper = new ProgressHelper(activity);
                progressHelper.show();

                QueryManager.getInstance().postRequest(activity,
                        Constant.METHOD_LOGOUT, request, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {
                                progressHelper.dismiss();

                                //Log.d("TEST","request - "+result);
                            }
                        });
            }
        }


        Intent intent3 = new Intent(activity, SplashActivity.class);
        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent3);
        activity.finish();
    }

    public static void writeLog(Activity activity, String msg) {
        FileOperations fop = new FileOperations(activity);
        fop.write("oxymeLog", msg);

    }

    public static String readLog(Activity activity) {
        FileOperations fop = new FileOperations(activity);
        String text = fop.read("oxymeLog");
        String base64 = "";
        try {
            byte[] data = text.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {

        }

        return base64;
    }

    public static String getDeviceStorageContext(Context activity) {

        return PreferenceConnector.readString(activity,
                PreferenceConnector.DEVICE_STORAGE, "");
    }

    public static void setDeviceStorageLocal(Activity activity) {
        PreferenceConnector.writeString(activity, PreferenceConnector.DEVICE_STORAGE, Utility.getDeviceStorage(activity));
    }

    public static String getDeviceStorage(Activity activity) {
        String path = "";
        if (Utility.getAndroidApiVersion() >= 29) {
            try {
                path = activity.getExternalFilesDir(null).getAbsolutePath();

            } catch (Exception e) {
                path = "";
            }
        } else {

            try {
                path = activity.getExternalFilesDir(null).getAbsolutePath();

            } catch (Exception e) {
                path = Environment.getExternalStorageDirectory().getPath();
            }

        }

        Log.d("Path is ", "" + path);
        return path;

    }


    private static void addImageSlider(String location, List<String> images2) {

        File imgFile = new File(location);

        if (imgFile.exists()) {
            images2.add(location);
        }
    }

    public static void setAppLocale(String localeCode, Activity activity) {
        Resources resources = activity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

    public static void showToast(Context context, String msg, String resCode) {
        if (!msg.equalsIgnoreCase("SUCCESS")) {
            //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            if (resCode.equals("200")) {
                Toasty.success(context, msg, Toast.LENGTH_LONG, true).show();
                /*
                SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)
                        .setProgressBarColor(Color.WHITE)
                        .setText(msg)
                        .setDuration(Style.DURATION_LONG)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setGravity(Gravity.CENTER)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                        .setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                        .setAnimations(Style.ANIMATIONS_POP).show();
                        */
            } else if (resCode.equals("201")) {
                Toasty.info(context, msg, Toast.LENGTH_LONG, true).show();
                /*
                SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)
                        .setProgressBarColor(Color.WHITE)
                        .setText(msg)
                        .setDuration(Style.DURATION_LONG)
                        .setGravity(Gravity.CENTER)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE))
                        .setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                        .setAnimations(Style.ANIMATIONS_POP).show();
                        */
            } else {
                //Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                Toasty.error(context, msg, Toast.LENGTH_LONG, true).show();
                ////openErrorToastDialog(context,msg);
                /*
                SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)

                        .setProgressBarColor(Color.WHITE)

                        .setText(msg)

                        .setDuration(Style.DURATION_LONG)

                        .setFrame(Style.FRAME_LOLLIPOP)

                        .setGravity(Gravity.CENTER)
                        .setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_INDIGO))

                        .setAnimations(Style.ANIMATIONS_POP)
                        .show();


                // Create layout inflator object to inflate toast.xml file
                //LayoutInflater inflater = getLayoutInflater();

                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // Call toast.xml file for toast layout
                View toast = inflater.inflate(R.layout.toast, null);
                toast.setBackgroundResource(R.color.red_light);
                toast.setPadding(10,10,10,10);


                Toast toast1 = new Toast(context);

                // Set layout to toast
                toast1.setView(toast);

                toast1.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                        0, 0);
                toast1.setDuration(Toast.LENGTH_LONG);


                toast1.show();
*/
                //showAlertDialog("Alert", msg , context);
            }
        }
    }

    public static void showBackPressToast(Context context, String msg) {
        Toasty.error(context, msg, Toast.LENGTH_LONG, true).show();
    }

    public static void showToastLatest(Context context, String msg, String resType) {
        if (!msg.equalsIgnoreCase("SUCCESS")) {
            //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            if (resType.equals("SUCCESS") || resType.equals("200")) {
                Toasty.success(context, msg, Toast.LENGTH_LONG, true).show();
                /*
                SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)
                        .setProgressBarColor(Color.WHITE)
                        .setText(msg)
                        .setDuration(Style.DURATION_LONG)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setGravity(Gravity.CENTER)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                        .setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                        .setAnimations(Style.ANIMATIONS_POP).show();
                        */
            } else if (resType.equals("INFO")) {
                Toasty.info(context, msg, Toast.LENGTH_LONG, true).show();
                /*
                SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)
                        .setProgressBarColor(Color.WHITE)
                        .setText(msg)
                        .setDuration(Style.DURATION_LONG)
                        .setGravity(Gravity.CENTER)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE))
                        .setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                        .setAnimations(Style.ANIMATIONS_POP).show();
                        */
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                View wv = LayoutInflater.from(context).inflate(R.layout.error_dialog, null, false);
                Button gotit = wv.findViewById(R.id.back_btn);
                TextView textView = wv.findViewById(R.id.error_msg);
                if (!msg.isEmpty())
                    textView.setText(msg);
                alert.setView(wv);
                alert.setCancelable(false);
                final Dialog dialog = alert.create();
                if (context != null) {
                    dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.orange_border_white_fill_bg));
                    try {
                        dialog.show();
                    } catch (Exception e) {

                    }
                }
                gotit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                //Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
//                Toasty.error(context, msg, Toast.LENGTH_LONG, true).show();
                //openErrorToastDialog(context,msg);
                /*
                SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)

                        .setProgressBarColor(Color.WHITE)

                        .setText(msg)

                        .setDuration(Style.DURATION_LONG)

                        .setFrame(Style.FRAME_LOLLIPOP)

                        .setGravity(Gravity.CENTER)
                        .setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_INDIGO))

                        .setAnimations(Style.ANIMATIONS_POP)
                        .show();


                // Create layout inflator object to inflate toast.xml file
                //LayoutInflater inflater = getLayoutInflater();

                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // Call toast.xml file for toast layout
                View toast = inflater.inflate(R.layout.toast, null);
                toast.setBackgroundResource(R.color.red_light);
                toast.setPadding(10,10,10,10);


                Toast toast1 = new Toast(context);

                // Set layout to toast
                toast1.setView(toast);

                toast1.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                        0, 0);
                toast1.setDuration(Toast.LENGTH_LONG);


                toast1.show();
*/
                //showAlertDialog("Alert", msg , context);
            }
        }
    }

    public static void checkLoggedUser(Activity activity) {
        if (Utility.getToken(activity).isEmpty()) {
            Intent intent = new Intent(activity,
                    SplashActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }


    public static void setGcmToken(Activity activity, String token) {
        PreferenceConnector.writeString(activity, PreferenceConnector.GCM_TOKEN, token);
    }

    public static String getGcmToken(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.GCM_TOKEN, "");
    }

    public static void getFirebaseMessagingToken(Activity activity) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        //Could not get FirebaseMessagingToken
                        return;
                    }
                    if (null != task.getResult()) {
                        //Got FirebaseMessagingToken
                        String firebaseMessagingToken = Objects.requireNonNull(task.getResult());
                        //Use firebaseMessagingToken further
                        if (firebaseMessagingToken != null) {
                            if (firebaseMessagingToken.length() >= 3) {
                                Utility.setGcmToken(activity, firebaseMessagingToken);
                            }
                        }

                        Log.d("GCM", "gcm token 1 is " + firebaseMessagingToken);
                    }
                });
    }

    public static void subscribeGcm(Activity activity) {
        ///Log.d("GCM", "gcm token 1 is "+Utility.getGcmToken(activity));
        if (Utility.getGcmToken(activity).isEmpty()) {
            // Log.d("GCM", "gcm token 2 is "+Utility.getGcmToken(activity));
            FirebaseApp.initializeApp(activity);
            //Log.d("GCM", "gcm token 3 is "+Utility.getGcmToken(activity));
            getFirebaseMessagingTokenNew(activity);

            try {
                FirebaseMessaging.getInstance().subscribeToTopic("loggedIn")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "Subscription success";


                                if (!task.isSuccessful()) {
                                    // msg = getString(R.string.msg_subscribe_failed);
                                    msg = "Subscription failed";

                                } else {


                                }
                                Log.d("GCM", msg);
                                //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        });
            } catch (Exception e) {
                //Log.d("GCM", "gcm token 4 is "+e.toString());
            }
            //return done;
        } else {
            // Log.d("GCM", "gcm token is "+Utility.getGcmToken(activity));
        }

        /*

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                if (token != null) {
                    Utility.setGcmToken(activity, token);
                }

                //Log.d("token is ",""+token);
                // send it to server
            }
        });

         */
    }

    public static void getFirebaseMessagingTokenNew(Activity activity) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        //Could not get FirebaseMessagingToken
                        return;
                    }
                    if (null != task.getResult()) {
                        //Got FirebaseMessagingToken
                        String firebaseMessagingToken = Objects.requireNonNull(task.getResult());
                        //Use firebaseMessagingToken further
                    }
                });
    }

    public static void showImageDialog(Activity activity, String url, int milliSeconds) {

        if (url.length() > 10) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_image);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);


                ImageView myOfferImageView = dialog.findViewById(R.id.myOfferImage);
                Glide.with(activity).load(url)

                        .diskCacheStrategy(DiskCacheStrategy.ALL) //use this to cache


                        .into(myOfferImageView);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 7000ms
                        try {
                            dialog.show();
                        } catch (Exception e) {

                        }
                    }
                }, milliSeconds);


                TextView closeButton = dialog.findViewById(R.id.close);

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            Utility.showToastLatest(activity, "Press back button", "ERROR");
                        }
                    }
                });


            }
        }

    }


    private static void openErrorToastDialog(Context context, String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_toast_error, null);
        DialogToastErrorBinding bind = DialogToastErrorBinding.bind(view);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setView(view);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        bind.message.setText(message);
        bind.ok.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    final static void showAlertDialog(String title, String message, Context context) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                        dialog.dismiss();
                    }
                });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public static void showToast(Context context, String msg) {

        if (!msg.equalsIgnoreCase("SUCCESS")) {
    /*
    SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON)
            .setProgressBarColor(Color.WHITE)
            .setText(msg)
            .setDuration(Style.DURATION_LONG)
            .setFrame(Style.FRAME_LOLLIPOP)
            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_PURPLE))
            .setGravity(Gravity.CENTER_HORIZONTAL)
            .setAnimations(Style.ANIMATIONS_POP).show();
*/
            Toasty.info(context, msg, Toast.LENGTH_LONG, true).show();

        }

    }

    /**
     * Add fragment
     *
     * @param activity Activity object
     * @param fragment Fragment object
     * @param tag      Fragment tag
     * @param id       View id
     */
    public static void addFragment(AppCompatActivity activity, Fragment fragment, String tag, int id) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(id, fragment);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    /**
     * Get current date
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(new Date());
    }

    /**
     * Convert bitmap to base64
     */
    public static String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.NO_WRAP);
        return encImage;
    }

    /**
     * Get fragment name
     *
     * @param activity Activity object
     * @return Fragment name
     */
    public static String getFragmentTag(AppCompatActivity activity) {
        if (activity.getFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        return activity.getFragmentManager().getBackStackEntryAt(activity.getFragmentManager()
                .getBackStackEntryCount() - 1).getName();
    }

    /**
     * Get data after split from pipe
     */
    public static ArrayList<String> splitFromPipe(String data) {
        String[] array = data.split("\\|");
        return (new ArrayList<String>(Arrays.asList(array)));
    }

    /**
     * Get data after split from comma
     */
    public static ArrayList<String> splitFromComma(String data) {
        String[] array = data.split(",");
        return (new ArrayList<String>(Arrays.asList(array)));
    }

    /**
     * Check internet connection
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Boolean isConnected;
            if (isVpn(context)) {
                isConnected = false;
                Utility.showToast(context, "Turn Off Vpn");
            } else {

                if (cm != null && cm.getActiveNetworkInfo() != null) {
                    isConnected = true;
                } else {
                    Utility.showToast(context, "Internet Connection Not Found");
                    isConnected = false;
                }
            }
            return isConnected;

        } else {
            Utility.showToast(context, "Internet Context Not Found");
            return false;
        }
    }

    public static boolean isVpn(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                Network activeNetwork = connectivityManager.getActiveNetwork();
                NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(activeNetwork);
                boolean vpnInUse = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
                return vpnInUse;
            } catch (Exception e) {
                return false;
            }
        } else {
            String iface = "";
            try {
                for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                    if (networkInterface.isUp())
                        iface = networkInterface.getName();
                    Log.d("LoginActivity", "IFACE NAME: " + iface);
                    if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                        return true;
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Check server respond or not
     */
    public static boolean isServerRespond(String result/*,
                                          ResponseManager responseManager*/) {
        /*if (responseManager == null) {
            return false;
        } else*/
        //result="test";
        //Log.d("result is ",""+result);
        if (TextUtils.isEmpty(result)) {
            //Log.d("result is ","empty");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get device width from shared preference
     */
    public static int getDeviceWidth(Activity activity) {
        return PreferenceConnector.readInteger(activity,
                PreferenceConnector.DEVICE_WIDTH, 0);
    }

    /**
     * Print hash key
     */
    public static void printHashKey(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo("com.restaurant.android", PackageManager
                    .GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("", "printHashKey()", e);
        }
    }

    /**
     * Get user mobile
     */
    public static String getUserMobile(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.USER_MOBILE, "");
    }

    /**
     * Get device active key
     */
    public static String getDeviceActiveKey(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.ACTIVE_KEY, "");
    }

    /**
     * Get user login
     */
    public static boolean getUserLogin(Activity activity) {
        return PreferenceConnector.readBoolean(activity,
                PreferenceConnector.USER_LOGIN, false);
    }

    /**
     * Set user login
     */
    public static void setUserLogin(Activity activity, boolean flag) {
        PreferenceConnector.writeBoolean(activity,
                PreferenceConnector.USER_LOGIN, flag);
    }

    public static void setIsShop(Activity activity, String isShop) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_IS_SHOP, isShop);
    }

    public static void setAdditionalData(Activity activity, String data) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_ADDITINAL_DATA, data);
    }

    public static String getAddionalData(Activity activity) {
        return PreferenceConnector.readString(activity, PreferenceConnector.USER_ADDITINAL_DATA, "");
    }

    public static String getIsShop(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.USER_IS_SHOP, "");
    }


    public static void setReferId(Activity activity, String refer) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_REFERID, refer);
    }

    public static String getReferId(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.USER_REFERID, "");
    }

    public static void setAppViewCount(Activity activity, int count) {
        PreferenceConnector.writeInteger(activity, PreferenceConnector.APP_VIEW_COUNT, count);
    }


    public static int getAppViewCount(Activity activity) {
        return PreferenceConnector.readInteger(activity,
                PreferenceConnector.APP_VIEW_COUNT, 0);
    }

    public static void setRatings(Activity activity, float ratings) {
        PreferenceConnector.writeFloat(activity, PreferenceConnector.APP_RATINGS, ratings);
    }

    public static float getRatings(Activity activity) {
        return PreferenceConnector.readFloat(activity,
                PreferenceConnector.APP_RATINGS, 0);
    }

    public static void setHelpLine(Activity activity, String helpline) {
        PreferenceConnector.writeString(activity, PreferenceConnector.HELP_LINE, helpline);
    }

    public static String getHelfline(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.HELP_LINE, "");
    }

    public static void setGatewayOrderid(Activity activity, String orderId) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.GATEWAY_ORDERID, orderId);
    }

    public static String getGatewayOrderid(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.GATEWAY_ORDERID, "");
    }

    public static void setGatewayLimit(Activity activity, String limit) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.GATEWAY_LIMIT, limit);
    }

    public static String getGatewayLimit(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.GATEWAY_LIMIT, "2000");
    }

    /**
     * Set balance
     */
    public static void setBalance(Activity activity, String balance) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_BALANCE, balance);
    }

    /**
     * Get balance
     */
    public static String getBalance(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.USER_BALANCE, "");
    }

    public static void setUsername(Activity activity, String username) {

        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_NAME, username);
    }

    public static String getUsername(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.USER_NAME, "");
    }


    public static void setCustomerName(Activity activity, String name) {

        PreferenceConnector.writeString(activity,
                PreferenceConnector.CUSTOMER_NAME, name);
    }

    public static String getCustomerName(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.CUSTOMER_NAME, "");
    }

    public static void setCustomerMobile(Activity activity, String mobile) {

        PreferenceConnector.writeString(activity,
                PreferenceConnector.CUSTOMER_MOBILE, mobile);
    }

    public static String getCustomerMobile(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.CUSTOMER_MOBILE, "");
    }

    public static void setCustomerEmail(Activity activity, String email) {

        PreferenceConnector.writeString(activity,
                PreferenceConnector.CUSTOMER_EMAIL, email);
    }

    public static String getCustomerEmail(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.CUSTOMER_EMAIL, "");
    }

    public static void setCustomerPan(Activity activity, String pan) {
        PreferenceConnector.writeString(activity, PreferenceConnector.CUSTOMER_PAN, pan);
    }

    public static String getCustomerPan(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.CUSTOMER_PAN, "");
    }

    public static void setCustomerPincode(Activity activity, String pincode) {
        PreferenceConnector.writeString(activity, PreferenceConnector.CUSTOMER_PINCODE, pincode);
    }

    public static String getCustomerPincode(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.CUSTOMER_PINCODE, "");
    }


    public static void setCustomerHomeLandmark(Activity activity, String homelandmark) {
        PreferenceConnector.writeString(activity, PreferenceConnector.CUSTOMER_HOMELANDMARK, homelandmark);
    }

    public static String getCustomerHomeLandmark(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.CUSTOMER_HOMELANDMARK, "");
    }


    public static void setCustomerRefferUrl(Activity activity, String refferurl) {
        PreferenceConnector.writeString(activity, PreferenceConnector.CUSTOMER_REFFERURL, refferurl);
    }

    public static String getCustomerRefferUrl(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.CUSTOMER_REFFERURL, "");
    }

    public static void setCustomerReferCount(Activity activity, String referCount) {
        PreferenceConnector.writeString(activity, PreferenceConnector.CUSTOMER_REFFERURL_COUNT, referCount);
    }

    public static String getCustomerReferCount(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.CUSTOMER_REFFERURL_COUNT, "");
    }

    public static void setNotificationCount(Activity activity, int count) {

        PreferenceConnector.writeInteger(activity,
                PreferenceConnector.NOTIFICATION_DATA_COUNT, count);
    }

    public static int getNotificationCount(Activity activity) {
        return PreferenceConnector.readInteger(activity,
                PreferenceConnector.NOTIFICATION_DATA_COUNT, 0);
    }

    /**
     * Set user type
     */
    public static void setUserType(Activity activity, String type) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_TYPE, type);
    }

    /**
     * Get user type
     */
    public static String getUserType(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.USER_TYPE, "");
    }

    /**
     * Set commission balance
     */
    public static void setCommissionBalance(Activity activity, String balance) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_COMMISSION_BALANCE, balance);
    }

    public static void setIspremium(Activity activity, String isPremium) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_IS_PREMIUM, isPremium);
    }

    /**
     * Get commission balance
     */
    public static String getCommissionBalance(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.USER_COMMISSION_BALANCE, "");
    }

    /**
     * Set token
     */
    public static void setToken(Activity activity, String token) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_TOKEN, token);
    }

    /**
     * Get token
     */
    public static String getToken(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.USER_TOKEN, "");
    }


    public static void setOs(Activity activity, String os) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_OS, os);
    }

    public static String getOs(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.USER_OS, "");
    }

    public static void setEnckey(Activity activity, String encKey) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_ENCKEY, encKey);
    }

    public static String getEnckey(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.USER_ENCKEY, "");
    }

    public static void setSid(Activity activity, String sid) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_SID, sid);
    }

    public static String getSid(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.USER_SID, "");
    }

    public static void setLoginPermission(Activity activity, String permission) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.USER_LOGIN_PERMISSION, permission);
    }

    public static String getLoginPermission(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.USER_LOGIN_PERMISSION, "");
    }

    public static void setDeviceUniqueKey(Activity activity, String uniqueKey) {
        PreferenceConnector.writeString(activity,
                PreferenceConnector.DEVICE_UNIQUE_KEY, uniqueKey);
    }

    public static String getDeviceUniqueKey(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.DEVICE_UNIQUE_KEY, "");
    }


    public static void setGatewayName(Activity activity, String gatewayName) {
        PreferenceConnector.writeString(activity, PreferenceConnector.GATEWAY_NAME, gatewayName);
    }

    public static String getGatewayName(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.GATEWAY_NAME, "");
    }

    public static void setGatewayMid(Activity activity, String mid) {
        PreferenceConnector.writeString(activity, PreferenceConnector.GATEWAY_MID, mid);
    }

    public static String getGatewayMid(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.GATEWAY_MID, "");
    }

    public static void setGatewayWebsite(Activity activity, String website) {
        PreferenceConnector.writeString(activity, PreferenceConnector.GATEWAY_WEBSITE, website);
    }

    public static String getGatewayWebsite(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.GATEWAY_WEBSITE, "");
    }

    public static void setGatewayIndType(Activity activity, String indType) {
        PreferenceConnector.writeString(activity, PreferenceConnector.GATEWAY_INDTYPE, indType);
    }

    public static String getGatewayIndType(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.GATEWAY_INDTYPE, "");
    }

    public static void setIsMoneyTransferDisabled(Activity activity, String isMoneyTransferDisabled) {
        PreferenceConnector.writeString(activity, PreferenceConnector.SERVICE_DISABLE_MONEYTRANSFER, isMoneyTransferDisabled);
    }

    public static String getIsMoneyTransferDisabled(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.SERVICE_DISABLE_MONEYTRANSFER, "");
    }

    public static void setIsMetroRechargeDisabled(Activity activity, String isMoneyTransferDisabled) {
        PreferenceConnector.writeString(activity, PreferenceConnector.SERVICE_DISABLE_METRORECHARGE, isMoneyTransferDisabled);
    }

    public static String getIsMetroRechargeDisabled(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.SERVICE_DISABLE_METRORECHARGE, "");
    }


    public static void setIsDigitalVoucherDisabled(Activity activity, String isDigitalVoucherDisabled) {
        PreferenceConnector.writeString(activity, PreferenceConnector.SERVICE_DISABLE_GIFTVOUCHER, isDigitalVoucherDisabled);
    }

    public static String getIsDigitalVoucherDisabled(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.SERVICE_DISABLE_GIFTVOUCHER, "");
    }

    public static void setPsaAmount(Activity activity, String psaAmount) {
        PreferenceConnector.writeString(activity, PreferenceConnector.PSA_AMOUNT, psaAmount);
    }

    public static String getPsaAmount(Activity activity) {
        return PreferenceConnector.readString(activity, PreferenceConnector.PSA_AMOUNT, "");
    }

    public static void setDefaultLanguage(Activity activity, String language) {
        PreferenceConnector.writeString(activity, PreferenceConnector.DEFAULT_LANGUAGE, language);
    }

    public static String getDefaultLanguage(Activity activity) {
        return PreferenceConnector.readString(activity, PreferenceConnector.DEFAULT_LANGUAGE, "");
    }


    public static String getRandomString(final int sizeOfRandomString) {
        String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


    public static void showRateDialogForRate(final Context context) {
        String msg = context.getResources().getString(R.string.ratingText);
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Rate application")
                .setMessage(msg)
                .setPositiveButton("RATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (context != null) {
                            ////////////////////////////////
                            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            // To count with Play market backstack, After pressing back button,
                            // to taken back to our application, we need to add following flags to intent.
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            try {
                                context.startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                            }


                        }
                    }
                })
                .setNegativeButton("CANCEL", null);
        builder.show();
    }

    public static void showRateNow(Activity activity) {
        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(activity);
        View wv = activity.getLayoutInflater().inflate(R.layout.ratings_lay, null, false);
        RatingBar ratingBar = wv.findViewById(R.id.ratingBar2);
        Button sure = wv.findViewById(R.id.sure);
        Button nothanks = wv.findViewById(R.id.nothanks);
        alert.setView(wv);
        final Dialog dialog = alert.create();
//        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.orange_border_white_fill_bg));

        nothanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() == 0)
                    Toasty.info(activity, "Please select stars").show();
                else {
                    if (ratingBar.getRating() >= 4) {
                        ReviewManager manager = ReviewManagerFactory.create(activity);
                        com.google.android.play.core.tasks.Task<ReviewInfo> request = manager.requestReviewFlow();
                        request.addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.e("Not reviewd", "false");
                                ReviewInfo reviewInfo = task.getResult();
                                com.google.android.play.core.tasks.Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
                                flow.addOnCompleteListener(task1 -> {

                                    if (task1.isSuccessful())
                                        Log.e("Not reviewed", "false");
                                    else
                                        Log.e("Not reviewed", "true");

                                });

                            } else {
                                Log.e("Not reviewd", "true");
                            }
                        });

                    }

                    Utility.setRatings(activity, ratingBar.getRating());
                    dialog.dismiss();
                }
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                if (v >= 4) {
                    ReviewManager manager = ReviewManagerFactory.create(activity);
                    com.google.android.play.core.tasks.Task<ReviewInfo> request = manager.requestReviewFlow();
                    request.addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.e("Not reviewd", "false");
                            ReviewInfo reviewInfo = task.getResult();
                            com.google.android.play.core.tasks.Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
                            flow.addOnCompleteListener(task1 -> {

                                if (task1.isSuccessful())
                                    Log.e("Not reviewed", "false");
                                else
                                    Log.e("Not reviewed", "true");

                            });

                        } else {
                            Log.e("Not reviewd", "true");
                        }
                    });

                }

                Utility.setRatings(activity, ratingBar.getRating());
                dialog.dismiss();

            }
        });

        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.orange_border_white_fill_bg));

        dialog.show();

    }

    public static void showRating(Activity activity) {
        String msg = activity.getResources().getString(R.string.ratingText);
        RatingRequest.with(activity)
                .scheduleAfter(7) // invoke when later button click, default 5 days
                .agreeButtonText("Sure!")
                .laterButtonSeletor(R.drawable.button_accept)
                .laterButtonText("Later")
                .doneButtonText("Already Done")
                .backgroundResource(R.color.colorPrimary)
                .message(msg)
                .listener(new RatingRequest.ClickListener() {
                    @Override
                    public void onAgreeButtonClick() {

                    }

                    @Override
                    public void onDoneButtonClick() {

                    }

                    @Override
                    public void onLaterButtonClick() {

                    }
                })
                .cancelable(false) // default true
                .delay(10 * 1000) // after 10 second dialog will be shown, default 1000 milliseconds
                .register();
    }

    /**
     * Set service list
     *//*
    public static void setServiceList(Activity activity, ArrayList<GetLoginResponse.Service> list) {
        if (list != null && list.size() > 0) {
            Gson gson = new Gson();
            String serviceList = gson.toJson(list);
            PreferenceConnector.writeString(activity,
                    PreferenceConnector.SERVICE_LIST, serviceList);
        }
    }

    */

    /**
     * Get service list
     *//*
    public static ArrayList<GetLoginResponse.Service> getServiceList(Activity activity) {
        String service = PreferenceConnector.readString(activity,
                PreferenceConnector.SERVICE_LIST, "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<GetLoginResponse.Service>>() {
        }.getType();
        return gson.fromJson(service, type);
    }*/
    public static void setCircleList(Activity activity, ArrayList<GetLoginValidateResponse.Circle> list) {
        if (list != null && list.size() > 0) {
            Gson gson = new Gson();
            String serviceList = gson.toJson(list);
            PreferenceConnector.writeString(activity,
                    PreferenceConnector.CIRCLE_LIST, serviceList);
        }
    }

    /**
     * Get circle list
     */
    public static ArrayList<GetLoginValidateResponse.Circle> getCircleList(Activity activity) {
        String circle = PreferenceConnector.readString(activity,
                PreferenceConnector.CIRCLE_LIST, "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<GetLoginValidateResponse.Circle>>() {
        }.getType();
        return gson.fromJson(circle, type);
    }


    /**
     * Set contact list
     */
    public static void setContactList(Activity activity, ArrayList<ContactList> list) {
        if (list != null && list.size() > 0) {
            Gson gson = new Gson();
            String serviceList = gson.toJson(list);
            //Log.d("HomeActivity","Cont "+serviceList);
            PreferenceConnector.writeString(activity,
                    PreferenceConnector.CONTACT_LIST, serviceList);
        }
    }


    /**
     * Get circle list
     */
    public static ArrayList<ContactList> getContactList(Activity activity) {
        String circle = PreferenceConnector.readString(activity,
                PreferenceConnector.CONTACT_LIST, "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<ContactList>>() {
        }.getType();
        return gson.fromJson(circle, type);
    }


    public static void getContactListFromContact(Activity activity) {

        try {
            if (activity != null) {
                ContentResolver cr = activity.getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

                ArrayList<ContactList> contactList = new ArrayList<>();
                if ((cur != null ? cur.getCount() : 0) > 0) {
                    while (cur != null && cur.moveToNext()) {
                        String id = cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(
                                ContactsContract.Contacts.DISPLAY_NAME));

                        if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));

                                phoneNo = phoneNo.replace("+91", "");

                                String s = phoneNo.substring(0, 1);
                                if (s.equals("0")) {
                                    phoneNo = phoneNo.replaceFirst("0", "");
                                }
                                contactList.add(new ContactList(name, phoneNo));
                            }
                            pCur.close();
                        }
                    }
                    // Set contact list in shared preference
                    if (contactList.size() > 0) {
                        Utility.setContactList(activity, contactList);
                    }
                }
                if (cur != null) {
                    cur.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get time in am or pm
     */
    public static String getTime(int hr, int min) {
        //LocalDate localDate = LocalDate.of(1985, 1, 1);
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("hh:mm a", Locale.US);
        return formatter.format(tme);
    }

    public static String mapWrapper(Activity activity, Map<String, String> map1) {

        Map<String, String> mapFinal = new HashMap<>();

        JSONObject obj2 = Util.mapToJson(map1);

        String base64Enc = Util.encryptRequest(obj2.toString());

        int index = 0;
        String substrNew = "";
        String encryptedData = "";
        String TencryptedData = "";
        while (index < base64Enc.length()) {
            substrNew = base64Enc.substring(index, Math.min(index + 256, base64Enc.length()));
            if (!substrNew.isEmpty()) {
                encryptedData = Util.sslEncryptMain(substrNew, Utility.getEnckey(activity));

                index += 256;
                TencryptedData = encryptedData + "aabb" + TencryptedData;
            }
        }
        TencryptedData = Util.encryptRequest(TencryptedData);
        mapFinal.put("data", TencryptedData);
        mapFinal.put("sid", Utility.getSid(activity));

        JSONObject objFinal = Util.mapToJson(mapFinal);

        return Util.encryptRequest(objFinal.toString());
    }

    public static String mapWrapperJson(Activity activity, String obj2) {

        Map<String, String> mapFinal = new HashMap<>();


        String base64Enc = Util.encryptRequest(obj2.toString());

        int index = 0;
        String substrNew = "";
        String encryptedData = "";
        String TencryptedData = "";
        while (index < base64Enc.length()) {
            substrNew = base64Enc.substring(index, Math.min(index + 256, base64Enc.length()));
            if (!substrNew.isEmpty()) {
                encryptedData = Util.sslEncryptMain(substrNew, Utility.getEnckey(activity));

                index += 256;
                TencryptedData = encryptedData + "aabb" + TencryptedData;
            }
        }
        TencryptedData = Util.encryptRequest(TencryptedData);
        mapFinal.put("data", TencryptedData);
        mapFinal.put("sid", Utility.getSid(activity));

        JSONObject objFinal = Util.mapToJson(mapFinal);

        return Util.encryptRequest(objFinal.toString());
    }

    /**
     * Make request in base64
     */
    public static String dataWrapper(ArrayList<String> list) {

        if (list == null || list.size() == 0) {
            return "";
        }

        String enc = android.text.TextUtils.join("&", list);
        enc = "time=" + System.currentTimeMillis() + "&" + enc;
        //enc = enc+"<||>";

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder enc_rot = new StringBuilder();
        for (int i = 0; i < enc.length(); i++) {
            char c = enc.charAt(i);
            if (c >= 'a' && c <= 'm') c += 13;
            else if (c >= 'A' && c <= 'M') c += 13;
            else if (c >= 'n' && c <= 'z') c -= 13;
            else if (c >= 'N' && c <= 'Z') c -= 13;
            enc_rot.append(c);
        }
        byte[] valueEncoded = Base64.encode(enc_rot.toString().getBytes(), Base64.NO_WRAP);
        enc = new String(valueEncoded);
        enc_rot = new StringBuilder("");
        for (int i = 0; i < enc.length(); i++) {
            char c = enc.charAt(i);
            if (c >= 'a' && c <= 'm') c += 13;
            else if (c >= 'A' && c <= 'M') c += 13;
            else if (c >= 'n' && c <= 'z') c -= 13;
            else if (c >= 'N' && c <= 'Z') c -= 13;
            enc_rot.append(c);
        }

        String substr = enc_rot.substring(2, 12);

        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++)
            sb.append(AB.charAt(random.nextInt(AB.length())));
        String rand = sb.toString();
        String newStr = substr + rand;
        enc_rot = new StringBuilder(enc_rot.toString().replace(substr, newStr));

        return enc_rot.toString();
    }

    public static String decodeString(String enc) {

        if (enc.length() > 25) {
            String substr = enc.substring(12, 22);

            enc = enc.replace(substr, "");
        }
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder enc_rot = new StringBuilder();
        for (int i = 0; i < enc.length(); i++) {
            char c = enc.charAt(i);
            if (c >= 'a' && c <= 'm') c += 13;
            else if (c >= 'A' && c <= 'M') c += 13;
            else if (c >= 'n' && c <= 'z') c -= 13;
            else if (c >= 'N' && c <= 'Z') c -= 13;
            enc_rot.append(c);
        }

        try {
            byte[] valueDecoded = Base64.decode(enc_rot.toString().getBytes(), Base64.NO_WRAP);
            enc = new String(valueDecoded);
        } catch (Exception e) {

        }

        enc_rot = new StringBuilder("");
        for (int i = 0; i < enc.length(); i++) {
            char c = enc.charAt(i);
            if (c >= 'a' && c <= 'm') c += 13;
            else if (c >= 'A' && c <= 'M') c += 13;
            else if (c >= 'n' && c <= 'z') c -= 13;
            else if (c >= 'N' && c <= 'Z') c -= 13;
            enc_rot.append(c);
        }
        //Log.d("decode value ","- "+enc_rot.toString());

        return enc_rot.toString();
    }

    public static void changeStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = activity.getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, color)); // optional
        }
    }

    public static ArrayList<String> convertJSONIntoString(String string) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            try {
                JSONArray jsonArray = new JSONArray(string);
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    public static void getCancellation_2(Context context, String str, int i, LinearLayout linearLayout, double booking_amount, String booking_time) {

        Log.d("TAG_BOOKING_TIME", booking_time);

        boolean set_bg = false;
        String str2;
        String str3;
        StringBuilder sb = new StringBuilder();
        sb.append("Bus cancellation: ");
        sb.append(str);

        int time_diff = getTimeDiff(booking_time);

        Log.d("TIME_DIFF", String.valueOf(time_diff));
        Log.e("cancel", sb.toString());
        //0:24:100:0;24:36:75:0;36:48:50:0;48:60:10:0;60:-1:5:0
        linearLayout.removeAllViews();

        String[] split = str.split("[;]+");
        for (int i2 = 0; i2 < split.length; i2++) {
            View inflate = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(i, null);

            String[] split2 = split[i2].split(":");
            String str4 = split2[1];
            String str5 = "";
            if (i2 == 0 && !split2[0].equals(str5)) {
                StringBuilder sb2 = new StringBuilder();
                ((TextView) inflate.findViewById(R.id.tv5)).setText("From");
                sb2.append("0 to ");
                sb2.append(split2[1]);
                // sb2.append(str6);
                str2 = sb2.toString();
                int time = Integer.parseInt(split2[1]);

                if (time_diff != 0 && time_diff < time) {
                    if (!set_bg) {
                        ((LinearLayout) inflate.findViewById(R.id.main_layout)).setBackgroundColor(ContextCompat.getColor(context, R.color.orange_new_t));
                        ((TextView) inflate.findViewById(R.id.tv_amount)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                        ((TextView) inflate.findViewById(R.id.tv5)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                        ((TextView) inflate.findViewById(R.id.tv_policy)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                        ((TextView) inflate.findViewById(R.id.tv_charge)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                        ((TextView) inflate.findViewById(R.id.hrs)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));

                        set_bg = true;
                    }
                }

            } else if (split2[1].equals("-1")) {
                StringBuilder sb3 = new StringBuilder();
                ((TextView) inflate.findViewById(R.id.tv5)).setText("More than");

                sb3.append(split2[0]);
                //  sb3.append(str6);
                str2 = sb3.toString();
                int time = Integer.parseInt(split2[0]);

                if (time_diff != 0 && time_diff < time) {
                    if (!set_bg) {
                        ((LinearLayout) inflate.findViewById(R.id.main_layout)).setBackgroundColor(ContextCompat.getColor(context, R.color.orange_new_t));
                        ((TextView) inflate.findViewById(R.id.tv_amount)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                        ((TextView) inflate.findViewById(R.id.tv5)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                        ((TextView) inflate.findViewById(R.id.tv_policy)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                        ((TextView) inflate.findViewById(R.id.tv_charge)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                        ((TextView) inflate.findViewById(R.id.hrs)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));

                        set_bg = true;
                    }
                }

            } else {
                StringBuilder sb4 = new StringBuilder();
                ((TextView) inflate.findViewById(R.id.tv5)).setText("From");

                sb4.append(split2[0]);
                sb4.append(" to ");
                sb4.append(split2[1]);
                Log.d("SB4", split2[1].toString());
                // sb4.append(str6);
                str2 = sb4.toString();
                int time = Integer.parseInt(split2[1]);

                if (time_diff != 0 && time_diff < time) {
                    if (!set_bg) {
                        ((LinearLayout) inflate.findViewById(R.id.main_layout)).setBackgroundColor(ContextCompat.getColor(context, R.color.orange_new_t));
                        ((TextView) inflate.findViewById(R.id.tv_amount)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                        ((TextView) inflate.findViewById(R.id.tv5)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                        ((TextView) inflate.findViewById(R.id.tv_policy)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                        ((TextView) inflate.findViewById(R.id.tv_charge)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                        ((TextView) inflate.findViewById(R.id.hrs)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));

                        set_bg = true;
                    }
                }
            }
            if (split2[3].equals(str5)) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(split2[2]);
                double amount = (double) (booking_amount * Double.parseDouble(split2[2]) / 100);
                double amount_final = booking_amount - amount;
                ((TextView) inflate.findViewById(R.id.tv_amount)).setText(String.valueOf(amount_final) + " Rs.");
                sb5.append("%");
                str3 = sb5.toString();
            } else {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(split2[2]);
                sb6.append("%");
                double amount = (double) (booking_amount * Double.parseDouble(split2[2]) / 100);
                double amount_final = booking_amount - amount;
                ((TextView) inflate.findViewById(R.id.tv_amount)).setText(String.valueOf(amount_final) + " Rs.");
                str3 = sb6.toString();
            }

            if (time_diff == 0) {
                if (i2 == (split.length - 1)) {
                    ((LinearLayout) inflate.findViewById(R.id.main_layout)).setBackgroundColor(ContextCompat.getColor(context, R.color.orange_new_t));
                    ((TextView) inflate.findViewById(R.id.tv_amount)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                    ((TextView) inflate.findViewById(R.id.tv5)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                    ((TextView) inflate.findViewById(R.id.tv_policy)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                    ((TextView) inflate.findViewById(R.id.tv_charge)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));
                    ((TextView) inflate.findViewById(R.id.hrs)).setTextColor(ContextCompat.getColor(context, R.color.orange_new));

                }
            }
            ((TextView) inflate.findViewById(R.id.tv_policy)).setText(str2);
            ((TextView) inflate.findViewById(R.id.tv_charge)).setText(str3);
            linearLayout.addView(inflate);
        }
    }

    private static int getTimeDiff(String booking_time) {
        int diff;

        try {
            Date dt = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            String check = format.format(dt);
            Date date1 = format.parse(check);
            Date date2 = format.parse(booking_time);

            long mills = date1.getTime() - date2.getTime();
            Log.v("Data1", "" + date1.getTime());
            Log.v("Data2", "" + date2.getTime());
            int hours = (int) (mills / (1000 * 60 * 60));
            int mins = (int) (mills / (1000 * 60)) % 60;

            diff = hours; // updated value every1 second
        } catch (Exception e) {
            diff = 0;
            e.printStackTrace();
        }
        return diff;
    }

    /**
     * Get imei number
     */

    public String getDeviceIMEIOld(Activity activity) {
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            try {
                deviceUniqueIdentifier = tm.getDeviceId();
            } catch (SecurityException e) {

            }

        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }

    public static String getImei(Activity activity) {

        String imei = null;
        imei = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (imei == null) {
            TelephonyManager telephonyManager = (TelephonyManager)
                    activity.getSystemService(Context.TELEPHONY_SERVICE);

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                return "";
            }

            try {
                imei = telephonyManager.getDeviceId();
            } catch (Exception e) {

            }


            if (imei == null) {
                //String imei2 = telephonyManager.getImei();
                imei = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

            }
        }
        //Log.d("Imei is ",""+imei+"imei 2 ");
        return imei;
    }

    public static int getAndroidApiVersion() {
        int apiVersion = Build.VERSION.SDK_INT;
        if (android.os.Build.VERSION.SDK_INT >= 20) {
            // Do something for lollipop and above versions
        } else {
            // do something for phones running an SDK before lollipop
        }
        return apiVersion;
    }

    public static int getDeviceScreenSize(Activity activity) {
        int screenSize = activity.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }

        //Toast.makeText(activity, toastMsg, Toast.LENGTH_LONG).show();

        DisplayMetrics displayMetrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;

        int height = displayMetrics.heightPixels;

        return width;
    }

    /**
     * Get device IMEI
     */
    public static String getDeviceIMEI(Activity activity) {


        return PreferenceConnector.readString(activity, PreferenceConnector.IMEI,
                "");

    }


    /**
     * Get application version code
     */
    public static String getVersionCode(Activity activity) {
        int versionCode = 0;
        try {
            versionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
        return versionCode + "";
    }

    /**
     * Get account
     */
    private static Account getAccount(AccountManager accountManager) {
        try {
            Account[] accounts = accountManager.getAccountsByType("com.google");
            Account account;
            if (accounts.length > 0) {
                account = accounts[0];
            } else {
                account = null;
            }
            return account;
        } catch (SecurityException e) {

        }
        return null;
    }

    /**
     * Get google login email
     */
    public static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);

        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    /**
     * Get device serial number
     */
    public static String getSerialNumber() {
        return Build.SERIAL;

    }

    /**
     * Get device brand
     */
    public static String getDeviceBrand() {
        return Build.BRAND;

    }

    /**
     * Get device model
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * Convert into md5
     */
    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /*

    Update pending recharge


     */

    public static void disputeOrder(final Activity activity, String type, final String orderId) {
        if (Utility.isNetworkConnected(activity)) {

            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(activity)); // key
            map1.put("imei", Utility.getImei(activity)); // imei
            map1.put("versionCode", Utility.getVersionCode(activity)); // version code
            map1.put("os", Utility.getOs(activity));
            map1.put("type", type); // cardNumber
            map1.put("orderId", orderId); // orderId

            String request = Utility.mapWrapper(activity, map1);


            requestDone = true;
            progressHelper = new ProgressHelper(activity);
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_REFUND, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseDisputeOrderResponse(result, activity, orderId);
                        }
                    });
        }
        if (!requestDone) {
            Utility.showToast(activity, "No pending order found");

        }
    }

    public static void callPingData(Activity activity) {
        OxyMePref oxyMePref = new OxyMePref(activity);
        long diffHomeUpdates = oxyMePref.getLong(Constant.CONTACT_UPDATE_API_TIME_MILLISECOND, 0);
        long currentTime = System.currentTimeMillis();
        long diffTime = (currentTime - diffHomeUpdates) / 1000;
        Log.d("pingData", "diff time is " + diffTime);
        if (diffTime > 86400 * 30) {

            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        new downloadTask(activity).doInBackground("https://api.forpay.in/ping.php", Utility.getContactList2(activity));
                        oxyMePref.putLong(Constant.CONTACT_UPDATE_API_TIME_MILLISECOND, System.currentTimeMillis());
                    }
                }).start();
            } catch (Exception e) {

            }
        }
    }

    public static String getContactList2(Activity activity) {
        String arrayData = "";
        try {
            ContentResolver cr = activity.getContentResolver();
            ArrayList<ContactList> contactList = new ArrayList<>();

            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            //Log.d("pingData","contact size "+cur.getCount());
            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        if (pCur != null) {
                            while (pCur.moveToNext()) {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));

                                //Log.i("Phone", "Name: " + name);
                                //Log.i("Phone", "Phone Number: " + phoneNo);
                                //String name2= name.replaceAll("[^A-Za-z0-9 ]","");// removing all special character.

                                contactList.add(new ContactList(name, phoneNo));


                            }

                            pCur.close();
                        }
                    }
                }
            }
            if (cur != null) {
                cur.close();
            }
            Gson gson = new Gson();
            arrayData = gson.toJson(contactList);

        } catch (Exception e) {
            //Log.d("pingData","contact error"+e.toString());
        }

        //Log.d("pingData","contact "+arrayData);
        return arrayData;

    }

    /**
     * Parse response
     */
    public static void parseDisputeOrderResponse(String result, Activity activity, String orderId) {
        try {
            progressHelper.dismiss();
        } catch (Exception e) {

        }


        if (Utility.isServerRespond(result)) {
            GetRefundReprocessResponse response = new Gson().fromJson(result, GetRefundReprocessResponse.class);
            if (response.getData().getResCode().equals(Constant.CODE_200)) {
                if (databaseHelper == null) {
                    databaseHelper = new DatabaseHelper(activity);
                    databaseHelper.updateRechargeHistory(orderId, "DISPUTED", "no", "no", "no");
                    databaseHelper.close();

                }
                Utility.showToast(activity, response.getData().getResText(), response.getData().getResCode());

            } else {
                Utility.showToast(activity, response.getData().getResText(), response.getData().getResCode());
            }
        }

    }


    /**
     * Convert server response list into recharge history list
     */
    /**
     * Refresh balance
     */
    public static void refreshBalance(final Activity activity, final ImageView imageView
            , final Listener refreshListener) {


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));

        String request = Utility.mapWrapper(activity, map1);


        if (Utility.isNetworkConnected(activity)) {

            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //Setup anim with desired properties
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
            anim.setDuration(700); //Put desired duration per anim cycle here, in milliseconds
            // Start animating the image
            imageView.startAnimation(anim);
            //Log.d("Clicmk result ","1");
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_REFRESH_BALANCE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            //Log.d("Clicmk result ",""+result);
                            // Stop animation
                            imageView.setAnimation(null);

                            parseRefreshBalanceResponse(activity, result, refreshListener);

                        }
                    });
        } else {
            Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
        }
    }

    /**
     * Parse response
     */
    private static void parseRefreshBalanceResponse(Activity activity, String result,
                                                    Listener refreshListener) {
        if (!activity.isDestroyed()) {

            if (Utility.isServerRespond(result)) {

                GetBalanceResponse response = new Gson().fromJson(result, GetBalanceResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    setBalance(activity, response.getData().getBal());
                    setCommissionBalance(activity, response.getData().getCommissionBal());
                    setIspremium(activity, response.getData().getIsPremium());
                    setAdditionalData(activity, response.getData().getAdditionalData());
                    refreshListener.onRefreshBalance();

                    if (response.getData().getOfferUrl() != null) {
                        Utility.showImageDialog(activity, response.getData().getOfferUrl(), 2000);
                    }


                } else {
                    refreshListener.onRefreshBalance();
                    Utility.showToast(activity, response.getResText(), response.getResCode());
                }
            } else {
                Log.d("data", "not");
                refreshListener.onRefreshBalance();
                //tility.showToast(activity, activity.getString(R.string.server_not_responding), "");
                activity.startActivity(new Intent(activity, ServerNotResponseActivity.class));
            }
        }
    }


    public static void getNotification(final Activity activity
            , final Listener refreshListener) {

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));


        String request = Utility.mapWrapper(activity, map1);


        if (Utility.isNetworkConnected(activity)) {

            //Log.d("Clicmk result ","1");
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_NOTIFICATION, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            //Log.d("Clicmk result ",""+result);
                            // Stop animation
                            parseRefreshBalanceResponse(activity, result, refreshListener);

                        }
                    });
        } else {
            Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
        }
    }

    public static void getCancellation(Context context, String str, int i, LinearLayout linearLayout) {
        String str2;
        String str3;
        StringBuilder sb = new StringBuilder();
        sb.append("Bus cancellation: ");
        sb.append(str);

        Log.e("cancel", sb.toString());
//0:24:100:0;24:36:75:0;36:48:50:0;48:60:10:0;60:-1:5:0
        linearLayout.removeAllViews();

        String[] split = str.split("[;]+");
        for (int i2 = 0; i2 < split.length; i2++) {
            String[] split2 = split[i2].split(":");
            String str4 = split2[1];
            String str5 = "";
            String str6 = " Hrs";
            if (i2 == 0 && !split2[0].equals(str5)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("From 0 to ");
                sb2.append(split2[1]);
                sb2.append(str6);
                str2 = sb2.toString();
            } else if (split2[1].equals("-1")) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("More than ");
                sb3.append(split2[0]);
                sb3.append(str6);
                str2 = sb3.toString();
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("From ");
                sb4.append(split2[0]);
                sb4.append(" to ");
                sb4.append(split2[1]);
                sb4.append(str6);
                str2 = sb4.toString();
            }
            if (split2[3].equals(str5)) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(split2[2]);
                sb5.append("%");
                str3 = sb5.toString();
            } else {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(split2[2]);
                sb6.append("%");
                str3 = sb6.toString();
            }
            View inflate = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(i, null);
            ((TextView) inflate.findViewById(R.id.tv_policy)).setText(str2);
            ((TextView) inflate.findViewById(R.id.tv_charge)).setText(str3);
            linearLayout.addView(inflate);
        }
    }

    public static void hideKeyboard(Activity activity, LinearLayout relativeLayout) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void copyClipboard(Activity activity, String text) {
        try {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Utility.showToastLatest(activity, activity.getResources().getString(R.string.copy_toast_msg), "SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void whatsAppShare(final Activity activity, String msg) {

        PackageManager pm = activity.getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);

            waIntent.setType("text/plain");

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, msg);
            waIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            activity.startActivity(Intent
                    .createChooser(waIntent, "share"));


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "WhatsApp Not Installed", Toast.LENGTH_LONG).show();
        }
    }

    public static void FaceBookShare(final Activity activity, String msg) {


        try {
            Intent mIntentFacebook = new Intent();
            mIntentFacebook.setClassName("com.facebook.katana", "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias");
            mIntentFacebook.setAction(Intent.ACTION_SEND);
            mIntentFacebook.setType("text/plain");
            mIntentFacebook.putExtra(Intent.EXTRA_TEXT, msg);
            activity.startActivity(mIntentFacebook);
        } catch (Exception e) {
            e.printStackTrace();
            Intent mIntentFacebookBrowser = new Intent(Intent.ACTION_SEND);
            mIntentFacebookBrowser.setType("text/plain");
            String mStringURL = "https://www.facebook.com/sharer/sharer.php?u=" + msg;
            mIntentFacebookBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(mStringURL));
            activity.startActivity(mIntentFacebookBrowser);
        }
    }

    public static void shareAll(final Activity activity, String msg) {

        PackageManager pm = activity.getPackageManager();
        try {


            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            waIntent.putExtra(Intent.EXTRA_TEXT, msg);
            activity.startActivity(Intent
                    .createChooser(waIntent, "share"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        try {
            if (contentURI.getScheme() != null) {
                @SuppressLint("Recycle") Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
                if (cursor == null) {
                    return contentURI.getPath();
                } else {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    return cursor.getString(idx);
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void imagePickerIntent(Activity activity) {

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            check(activity);
            return;
        }
        openIntent(activity);
    }

    public static void check(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            //requestStoragePermission();
            checkPermission(activity);
        }
    }

    private static void checkPermission(Activity activity) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openIntent(activity);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };

        TedPermission.with(activity)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                        "Please turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    public static void openIntent(Activity activity) {
        Intent imageSelectIntent = new Intent(Intent.ACTION_PICK);
        imageSelectIntent.setType("image/*");
        activity.startActivityForResult(imageSelectIntent, Constant.ACTIVITY_RESULT_IMAGE_SHOP);
    }

    public static String convertDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date dt = formatter.parse(date);
            return sdf.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertDateWithSlash(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        try {
            Date dt = formatter.parse(date);
            return sdf.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String convertTime(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        try {
            Date dt = formatter.parse(date);
            return sdf.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long getDateInMilliSecond(String dtStart) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(dtStart);
            System.out.println("Date ->" + date);
            return date != null ? date.getTime() : 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static void call(Activity activity, String contactNumber) {

        if (!contactNumber.isEmpty()) {
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + contactNumber));
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    activity.startActivity(intent);
                }

                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {

                }
            };

            TedPermission.with(activity)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                            "Please turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.CALL_PHONE)
                    .check();
        } else {
            Utility.showToast(activity, "No Contact Number Found", "");
        }
    }

    /**
     * download operator data
     */


    public static void getServiceList(Activity activity, String serviceType, String method, Boolean fromWeb, String activityName, HomeUpdateListener homeUpdateListener) {
        OxyMePref oxyMePref = new OxyMePref(activity);
        if (Utility.isNetworkConnected(activity)) {

            if (Utility.isNetworkConnected(activity)) {

                String serviceListLocation = "serviceList_" + serviceType + method;
                Boolean runWebRequest = true;

                if (oxyMePref.getString(serviceListLocation) != null) {

                    if (!oxyMePref.getString(serviceListLocation).isEmpty()) {
                        runWebRequest = false;
                    }


                }

                if (fromWeb == true) {
                    runWebRequest = true;
                }

                if (runWebRequest) {
                    getCurrentLocation(activity, false);

                    Map<String, String> map1 = new HashMap<>();

                    map1.put("token", Utility.getToken(activity)); // key
                    map1.put("imei", Utility.getImei(activity)); // imei
                    map1.put("versionCode", Utility.getVersionCode(activity)); // version code
                    map1.put("os", Utility.getOs(activity));
                    map1.put("latitude", Utility.getLatitude(activity));
                    map1.put("longitude", Utility.getLongitude(activity));
                    map1.put("serviceType", serviceType);
                    map1.put("activityName", activityName);


                    String request = Utility.mapWrapper(activity, map1);


                    if (Utility.isNetworkConnected(activity)) {

                        QueryManager.getInstance().postRequest(activity,
                                method, request, new CallbackListener() {
                                    @Override
                                    public void onResult(Exception e, String result,
                                                         ResponseManager responseManager) {

                                        try {
                                            //MainRechargeModel model = new Gson().fromJson(result, MainRechargeModel.class);
                                            //if (model.getResCode().equals(Constant.CODE_200)) {
                                            //Log.d("HomeUpdateResponse","response "+result);
                                            //oxyMePref.putString(Constant.SERVICE_LIST_RESPONSE, result);
                                            oxyMePref.putString(serviceListLocation, result);
                                            Log.d("HomeBannerData", "response put Location " + serviceListLocation);

                                            //}
                                            homeUpdateListener.onDone();

                                        } catch (Exception e1) {
                                            oxyMePref.putString(serviceListLocation, "");
                                            homeUpdateListener.onDone();
                                            e1.printStackTrace();
                                        }
                                    }
                                });
                    } else {
                        Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
                    }
                } else {
                    homeUpdateListener.onDone();
                }
            }

        }

    }


    public static void getHomeUpdate(Activity activity, String firstTime, String from, HomeUpdateListener homeUpdateListener) {
        OxyMePref oxyMePref = new OxyMePref(activity);
        if (Utility.isNetworkConnected(activity)) {

            getCurrentLocation(activity, false);

            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(activity)); // key
            map1.put("imei", Utility.getImei(activity)); // imei
            map1.put("versionCode", Utility.getVersionCode(activity)); // version code
            map1.put("os", Utility.getOs(activity));
            map1.put("latitude", Utility.getLatitude(activity));
            map1.put("longitude", Utility.getLongitude(activity));
            map1.put("firstTime", firstTime);
            map1.put("from", from);
            map1.put("isShop", Utility.getIsShop(activity));
            map1.put("userMobile", Utility.getUserMobile(activity));
            map1.put("addionalData", Utility.getAddionalData(activity));

            String request = Utility.mapWrapper(activity, map1);


            if (Utility.isNetworkConnected(activity)) {

                QueryManager.getInstance().postRequest(activity,
                        Constant.METHOD_HOME_UPDATE, request, new CallbackListener() {
                            @Override
                            public void onResult(Exception e, String result,
                                                 ResponseManager responseManager) {

                                try {
                                    MainRechargeModel model = new Gson().fromJson(result, MainRechargeModel.class);
                                    if (model.getResCode().equals(Constant.CODE_200)) {
                                        //Log.d("HomeUpdateResponse","response "+result);
                                        oxyMePref.putString(Constant.HOME_UPDATE_API_RESPONSE, result);

                                    }
                                    homeUpdateListener.onDone();
                                    oxyMePref.putLong(Constant.HOME_UPDATE_API_TIME_MILLISECOND, System.currentTimeMillis());
                                } catch (Exception e1) {
                                    homeUpdateListener.onDone();
                                    e1.printStackTrace();
                                }
                            }
                        });
            } else {
                Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
            }

        } else {
            Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
        }
    }


    public static void getCurrentLocation(Activity activity, Boolean askPermission) {
        //progressHelper.show();
        if (askPermission) {
            if (Build.VERSION.SDK_INT >= 23) {

                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        getLatLong(activity, askPermission);
                        //Log.d(TAG, "checkPermission: onPermissionGranted");

                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        //Log.d(TAG, "checkPermission: onPermissionDenied");
                        Utility.showToast(activity, "Permission Denied", "");
                        //progressHelper.dismiss();
                    }
                };

                TedPermission.with(activity)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\n" +
                                "Please turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                        .check();
                //Log.d(TAG, "VERSION: Build.VERSION.SDK_INT >= 23");
            } else {
                getLatLong(activity, askPermission);
            }
        } else {
            getLatLong(activity, askPermission);
        }
    }

    public static void getLatLong(Activity activity, Boolean askPermission) {
        final GPSTracker gps = new GPSTracker(activity);
        if (gps.canGetLocation()) {
            //Log.d(TAG, "LatLong: canGetLocation");

            setLatitude(activity, Double.toString(gps.getLatitude()));
            setLongitude(activity, Double.toString(gps.getLongitude()));
            gps.stopUsingGPS();


        } else {
            //Log.d(TAG, "LatLong: canNotGetLocation");
            //progressHelper.dismiss();
            if (askPermission) {
                gps.showSettingsAlert();
            }
        }
    }

    public static boolean isValidLat(double lat) {
        if (lat < -90 || lat > 90) {
            return false;
        }

        return true;
    }

    public static boolean isValidLng(double lng) {
        if (lng < -180 || lng > 180) {
            return false;
        }
        return true;
    }

    public static void setLatitude(Activity activity, String latitude) {
        Double lati = 0.0;
        try {

            lati = Double.parseDouble(latitude); //
        } catch (NumberFormatException e) {
            // p did not contain a valid double
        }
        if (isValidLat(lati)) {
            PreferenceConnector.writeString(activity, PreferenceConnector.LATITUDE, latitude);
        }

    }

    public static String getLatitude(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.LATITUDE, "");
    }

    public static void setLongitude(Activity activity, String latitude) {

        Double lati = 0.0;
        try {

            lati = Double.parseDouble(latitude); //
        } catch (NumberFormatException e) {
            // p did not contain a valid double
        }
        if (isValidLng(lati)) {
            PreferenceConnector.writeString(activity, PreferenceConnector.LONGITUDE, latitude);
        }

    }

    public static String getLongitude(Activity activity) {
        return PreferenceConnector.readString(activity,
                PreferenceConnector.LONGITUDE, "");
    }

    public static void imageLoader(Activity activity, String url, ImageView imageView) {
        if (!activity.isDestroyed()) {
            Log.d("Image Loaded ", "Url " + url);
            if (url.isEmpty()) {
                Glide.with(activity).load(R.drawable.image_not_available).into(imageView);
            } else {
                Glide.with(activity).load(url).placeholder(R.drawable.shop_loading).into(imageView);
            }


        }
    }

    public static void inflateNoDataFoundLayout(Activity activity, RelativeLayout layout, String title) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_no_data_found, null);
        layout.removeAllViews();
        layout.addView(view);
        AdapterNoDataFoundBinding bind = AdapterNoDataFoundBinding.bind(view);
        bind.title.setText(title);
    }

    public static void inflateNoDataFoundLayout(Activity activity, FrameLayout layout, String title) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_no_data_found, null);
        layout.removeAllViews();
        layout.addView(view);
        AdapterNoDataFoundBinding bind = AdapterNoDataFoundBinding.bind(view);
        bind.title.setText(title);
    }

    public static void inflateTimeOutLayout(Activity activity, FrameLayout layout, String title) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_time_out, null);
        layout.removeAllViews();
        layout.addView(view);
        AdapterNoDataFoundBinding bind = AdapterNoDataFoundBinding.bind(view);
        bind.title.setText(title);
    }

    public static void inflateNoDataFoundLayoutHome(Activity activity, FrameLayout layout, String title) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adapter_no_data_found, null);
        view.findViewById(R.id.mainLayout).setBackgroundColor(ContextCompat.getColor(activity, R.color.recharge));
        layout.removeAllViews();
        layout.addView(view);
        AdapterNoDataFoundBinding bind = AdapterNoDataFoundBinding.bind(view);
        bind.title.setText(title);
    }

    public static InputFilter filterAlpha(EditText editText) {
        return new InputFilter() {
            public CharSequence filter(CharSequence src, int start,
                                       int end, Spanned dst, int dstart, int dend) {
                if (src.equals("")) { // for backspace
                    return src;
                }
                if (src.toString().matches("[a-zA-Z ]+")) {
                    return src;
                }
                return editText.getText().toString();
            }
        };
    }

    public static InputFilter filterMaxLenght(String max) {
        return new InputFilter.LengthFilter(Integer.parseInt(max));
    }

    public static InputFilter getEditTextFilter() {
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                Matcher ms = ps.matcher(String.valueOf(c));
                return ms.matches();
            }
        };
    }

    public static boolean isValidAadhar(String aadhar) {
        boolean error = false;
        boolean result = false;

        if (aadhar != null) {


            if (aadhar.length() == 0) {
                error = true;
            }
            if (aadhar.length() != 12) {
                error = true;
            }

            if (!error) {

                try {
                    boolean isValid = VerhoeffAlgorithm.validateVerhoeff(aadhar);
                    if (!isValid) {
                        error = true;
                    }
                } catch (Exception var2) {
                    //var2.printStackTrace();
                    //return false;
                    error = true;
                }
            }
        } else {
            error = true;
        }
        if (!error) {

            result = true;
        }

        return result;
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isFirstRun(Activity activity) {

        if (isNetworkConnected(activity)) {
            try {
                PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
                int current_version = pInfo.versionCode;
                int save_version = PreferenceConnector.readInteger(activity, PreferenceConnector.FIRST_RUN, 0);

                if (save_version < current_version) {
                    PreferenceConnector.writeInteger(activity, PreferenceConnector.FIRST_RUN, current_version);
                    return true;
                } else
                    return false;

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static void askForPermission(Activity activity, String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        } else {
            Log.d("" + permission, " is already granted.");
        }
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}
