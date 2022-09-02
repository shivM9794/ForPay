package in.forpay.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.TshirtAddressAdapter;
import in.forpay.databinding.ActivityBuyTshirtAddressBinding;
import in.forpay.fragment.TshirtBottomSheet;
import in.forpay.model.response.GetAddressResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class BuyTShirtAddressActivity extends AppCompatActivity {

    private ActivityBuyTshirtAddressBinding binding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private OxyMePref oxyMePref;
    String TAG = "BuyTShirtAddressActivity";
    private ArrayList<GetAddressResponse.Data> data = new ArrayList<>();
    private TshirtAddressAdapter tshirtAddressAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_buy_tshirt_address);
        activity = this;
        progressHelper = new ProgressHelper(this);
        oxyMePref = new OxyMePref(this);

        init(false);

        inits();

        binding.backBtn.setOnClickListener(view -> finish());


    }

    private void inits() {

        binding.txtAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TshirtBottomSheet tshirtBottomSheet = new TshirtBottomSheet();
                tshirtBottomSheet.show(getSupportFragmentManager(), "fffde");
            }
        });
    }

    private void init(Boolean showProgressbar) {

        if (showProgressbar) {
            progressHelper.show();

        }
        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this, map1);
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_SHOP_ADDRESS_LIST, request, new CallbackListener() {
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

        GetAddressResponse response = new Gson().fromJson(result, GetAddressResponse.class);
        if (response.getResCode().equals(Constant.CODE_200)) {


        } else {

            Utility.showToastLatest(activity, response.getResText(), response.getResCode());

        }

        data = response.getData();
        setAdapter();

    }

    private void setAdapter() {

        binding.recyclerTshirt.setLayoutManager(new LinearLayoutManager(activity));
        tshirtAddressAdapter = new TshirtAddressAdapter(activity, data);
        binding.recyclerTshirt.setAdapter(tshirtAddressAdapter);
    }


}



