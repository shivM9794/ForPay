package in.forpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityBuyTshirtBinding;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class BuyTShirtActivity extends AppCompatActivity {

    private ActivityBuyTshirtBinding mBinding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private OxyMePref oxyMePref;
    String TAG = "BuyTShirtActivity";
    int minteger = 1;
    private String noOfSize = "S";
    String textSize = "";
    String addressId = "";
    private static final int REQUEST_SELECT_CONTACT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_buy_tshirt);
        activity = this;
        progressHelper = new ProgressHelper(this);
        oxyMePref = new OxyMePref(this);
        init();
        mBinding.backBtn.setOnClickListener(view -> finish());


        mBinding.txtBuyNow.setOnClickListener(v -> {
//            Log.d("sdgfs","ggdd "+addressId);
            if (addressId.isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), BuyTShirtAddressActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_CONTACT);


            } else {
                buyNow();
            }
        });


        mBinding.rgNoOfSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                //Log.d("jsdfjgsjss", "Checked " + radioButton.getText());
                String text = radioButton.getText().toString();
                textSize = text;
                noOfSize = text;
                setColor();

            }
        });


    }

    private void init() {
        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        String request = Utility.mapWrapper(this, map1);

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_TSHIRT, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            progressHelper.dismiss();
                            parseTshirtResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    private void parseTshirtResponse(String result) {

        if (result != null) {

            try {
                JSONObject obj = new JSONObject(result);
                String previewUrl = obj.getString("previewUrl");
                String price = obj.getString("price");
                String resCode = obj.getString("resCode");
                String resText = obj.getString("resText");
                if (resCode.equals(Constant.CODE_200)) {

                    mBinding.txtTshirtAmt.setText(price);
                    Utility.imageLoader(activity, previewUrl, mBinding.imgTshirt);

                } else {
                    Utility.showToastLatest(activity, resText, resCode);
                }
            } catch (Exception e) {

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                if (data != null) {

                    addressId = data.getStringExtra("addressId");
                    //Log.d("sdgfs", "add " + addressId);

                }

            }
            catch (Exception e) {
                Utility.showToast(BuyTShirtActivity.this, getString(R.string.something_went_wrong), "");
            }
        }
    }


    private void setColor() {

        if (mBinding.tvSmall.isChecked()) {
            mBinding.tvSmall.setTextColor(getApplicationContext().getResources().getColor(R.color.orange_new));
        } else {
            mBinding.tvSmall.setTextColor(getApplicationContext().getResources().getColor(R.color.comfort_default));
        }
    }

    public void sizeChart(View view) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(BuyTShirtActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tshirt_size_sheet, (LinearLayout) findViewById(R.id.bottomSheetContainer));
        bottomSheetView.findViewById(R.id.closeBtn).setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    public void increaseInteger(View view) {
        minteger = minteger + 1;
        display(minteger);

    }

    public void decreaseInteger(View view) {
        if (minteger > 1) {
            minteger = minteger - 1;
            display(minteger);
        }
    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.integer_number);
        displayInteger.setText("" + number);
    }


    private void buyNow() {

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("operatorId", "533");
        map1.put("quantity", "" + minteger);
        map1.put("size", noOfSize);
        map1.put("addressId", addressId);
//        Log.d("Number_of_size", "hjjh" + noOfSize);

        String request = Utility.mapWrapper(this, map1);

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseBuyResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }

//        Toast.makeText(getApplicationContext(), "Order Placed " + "Quanity " + minteger + " Size " + noOfSize, Toast.LENGTH_SHORT).show();
    }


    private void parseBuyResponse(String result) {
        if (result != null) {

            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

                if (response.getResCode().equals(Constant.CODE_200)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("outputJson", response.getData().getOutputJson());
                    bundle.putString("uniqId", response.getData().getUniqId());
                    bundle.putString("serviceType", response.getData().getType());
                    bundle.putString("extraData", response.getData().getExtraData());
                    bundle.putParcelable(Constant.INSUFFICIENTDATA, response.getData().getInsufficientData());
                    bundle.putParcelable(Constant.POPUPDATA, response.getData().getPopupData());
                    bundle.putString("offerDetails", "");

                    Utility.openCheckOutActivity(activity, response.getData().getType(), bundle);


                } else {
                    Utility.showToast(activity, response.getResText(), response.getResCode());
                }

            } else {
                Utility.showToast(this, getString(R.string.server_not_responding), "");
            }

        }
    }
}