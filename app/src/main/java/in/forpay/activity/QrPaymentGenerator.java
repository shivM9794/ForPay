package in.forpay.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.ActivityQrPaymentGeneratorBinding;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class QrPaymentGenerator extends AppCompatActivity {

    private static final String TAG = "QrPaymentGenerator";
    private ActivityQrPaymentGeneratorBinding binding;
    Activity activity;
    ProgressHelper progressHelper;
    Boolean showProgressbar = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utility.checkLoggedUser(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_payment_generator);
        activity = this;
        progressHelper = new ProgressHelper(this);
        init();
    }


    private void init() {
        binding.generateQrBtn.setOnClickListener(v -> {
            progressHelper.show();

            Map<String, String> map1 = new HashMap<>();

            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei", Utility.getImei(this)); // imei
            map1.put("versionCode", Utility.getVersionCode(this)); // version code
            map1.put("os", Utility.getOs(this));
            map1.put("amount", binding.userAmt.getText().toString());

            String request = Utility.mapWrapper(this, map1);


            if (Utility.isNetworkConnected(this)) {
                QueryManager.getInstance().postRequest(this, Constant.METHOD_QR_PAYMENT_GENERATOR,
                        request, new CallbackListener() {
                    @Override
                    public void onResult(Exception e, String result, ResponseManager responseManager) {
                        progressHelper.dismiss();
                        parseSubmitResponseData(result);
                    }
                });
            }
        });

        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
    }


    private void parseSubmitResponseData(String result) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            String resCode = jsonObject.getString("resCode");
            String resText = jsonObject.getString("resText");
            String shopName = jsonObject.getString("shopName");
            String amount = jsonObject.getString("amount");
            String image = jsonObject.getString("image");

            if (resCode.equals(Constant.CODE_200)) {
                binding.LayoutQrCode.setVisibility(View.VISIBLE);
                binding.shopName.setText(shopName);
                binding.amount.setText(amount);
                showQr(image);
            } else {
                Utility.showToastLatest(activity, resText, "ERROR");
            }

        } catch (Exception e) {
            Utility.showToastLatest(activity, e.toString(), "ERROR");
        }


    }

    private void showQr(String base64Image) {

        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        binding.qrImage.setImageBitmap(decodedByte);
    }
}
