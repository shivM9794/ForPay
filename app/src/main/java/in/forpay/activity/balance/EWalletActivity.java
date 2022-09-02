package in.forpay.activity.balance;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.EWalletAdapter;
import in.forpay.adapter.balance.CouponOfferAdapter;
import in.forpay.adapter.balance.LandLinePlaceHolderAdapter;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.ActivityEwalletBinding;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.response.GetEWalletListResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.model.response.GetServiceList;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class EWalletActivity extends AppCompatActivity implements ItemClickListener {
    private ActivityEwalletBinding binding;
    private AppCompatActivity activity;
    private DatabaseHelper databaseHelper;
    private ProgressHelper progressHelper;
    private OxyMePref oxyMePref;
    private ArrayList<GetEWalletListResponse.BrandList> tempArrayList;
    private ArrayList<GetEWalletListResponse.BrandList> arrayList;
    private EWalletAdapter eWalletAdapter;
    private String selectedId = "";
    //    private String topIconUrl = "";
    private String ewallet_SessionId = "";

    private int index = 0;
    private ArrayList<GetServiceList.ServiceBean> serviceArrayList;
    private String operatorId = "";
    private String bbpsId = "";
    private LandLinePlaceHolderAdapter landLinePlaceHolderAdapter;
    private ArrayList<GetServiceList.ServiceBean> tempServiceArrayList = new ArrayList<>();
    private ArrayList<String> valueArrayList = new ArrayList<>();
    private String title;
    private String pin = "";
    private String type = "EwalletList";
    private String search_type;
    private String mobile = "";
    private String amount = "0";
    private int viewPagerPosition = 0;
    private GetServiceList model;
    private String inputData = "";
    private String favMobile, inputValue1, inputValue2 = "";
    private String selectedCoupon = "";
    private String selectedCouponId = "";
    private String topIconUrl = "";
    private CouponOfferAdapter couponOfferAdapter;
    private String TAG = "EWalletActivity";
    Boolean fromWeb = false;
    private boolean selfButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ewallet);
        activity = this;
        databaseHelper = new DatabaseHelper(activity);
        progressHelper = new ProgressHelper(activity);
        oxyMePref = new OxyMePref(activity);

        init();
        binding.rechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recharge();
            }
        });


    }

    private void recharge() {

        progressHelper.show();

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("mobile", binding.editMobileUser.getText().toString());
        map1.put("otp", binding.editTextOTP.getText().toString());
        map1.put("pin", binding.editTextPin.getText().toString());
        map1.put("customerName", binding.editNameUser.getText().toString());
        map1.put("amount", binding.editAmtUser.getText().toString());
        map1.put("ewalletId", selectedId);
        map1.put("ewalletSessionId", ewallet_SessionId);

        String callMethod = Constant.METHOD_EWALLET_SEND_OTP;
        if (!ewallet_SessionId.isEmpty()) {
            callMethod = Constant.METHOD_VALIDATE_RECHARGE;

        }

        String request = Utility.mapWrapper(this, map1);

        if (Utility.isNetworkConnected(this)) {
            QueryManager.getInstance().postRequest(this, callMethod, request, new CallbackListener() {
                @Override
                public void onResult(Exception e, String result, ResponseManager responseManager) {
                    progressHelper.dismiss();
                    if (!ewallet_SessionId.isEmpty()) {
                        parseSubmitValidateResponseData(result);
                    } else {
                        parseSubmitResponseData(result);
                    }

                }
            });
        }
    }

    private void parseSubmitValidateResponseData(String result) {


        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

            if (response.getResCode().equals(Constant.CODE_200)) {

                Bundle bundle = new Bundle();
                bundle.putString("outputJson", response.getData().getOutputJson());
                bundle.putString("uniqId", response.getData().getUniqId());
                bundle.putString("serviceType", response.getData().getType());
                bundle.putString("offerDetails", "");
                bundle.putString("pin", binding.editTextPin.getText().toString());
                bundle.putString("amount", response.getData().getBillAmount());
                bundle.putString("selectedMode", "");
                bundle.putString("mobile", response.getData().getMobile());
                bundle.putString("operatorId", response.getData().getOperatorId());
                bundle.putString("extraData", response.getData().getExtraData());
                bundle.putString("coupon", "");
                bundle.putString("couponId", "");
                bundle.putParcelable(Constant.INSUFFICIENTDATA, response.getData().getInsufficientData());
                bundle.putParcelable(Constant.POPUPDATA, response.getData().getPopupData());

                binding.editTextPin.setText("");
                binding.editTextOTP.setText("");
                ewallet_SessionId = "";

                Utility.openCheckOutActivity(activity, response.getData().getType(), bundle);


            } else {
                Utility.showToast(this, response.getResText(), response.getResCode());
            }


        } else {
            Utility.showToast(this, getString(R.string.server_not_responding), "");
        }
    }

    private void parseSubmitResponseData(String result) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            String resCode = jsonObject.getString("resCode");
            String resText = jsonObject.getString("resText");
            ewallet_SessionId = jsonObject.getString("ewalletSessionId");

            if (resCode.equals(Constant.CODE_200)) {
                Utility.showToastLatest(activity, resText, resCode);
                binding.editTextOTP.setVisibility(View.VISIBLE);
                binding.viewEwallet2.setVisibility(View.VISIBLE);
            } else {
                Utility.showToastLatest(activity, resText, "ERROR");
            }

        } catch (Exception e) {
            Utility.showToastLatest(activity, e.toString(), "ERROR");
        }


    }

    private void init() {

        progressHelper.show();

        Utility.getServiceList(activity, type, Constant.METHOD_EWALLET_LIST, fromWeb, "EWalletActivity", new HomeUpdateListener() {
            @Override
            public void onDone() {
                progressHelper.dismiss();
                //parseHomeUpdateResponse(oxyMePref.getString(Constant.SERVICE_LIST_RESPONSE));
                String serviceListLocation = "serviceList_" + type + Constant.METHOD_EWALLET_LIST;
                parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation));
            }
        });


        binding.title.setText("EWallet Recharge");


//        binding.dotsIndicator.setVisibility(View.VISIBLE);
//        binding.placeHolderLayout.setVisibility(View.GONE);
//        //binding.frameLayout.setVisibility(View.GONE);
//
        binding.backPress.setOnClickListener(view -> onBackPressed());

//        binding.clear.setOnClickListener(view -> {
//            if (!binding.edtSearch.getText().toString().isEmpty()) {
//                binding.edtSearch.setText("");
//                Utility.hideKeyboard(activity);
//            }
//        });

    }

    private void parseHomeUpdateResponse(String response) {
        try {

            GetEWalletListResponse response1 = new Gson().fromJson(response, GetEWalletListResponse.class);

            if (response1.getResCode().equals(Constant.CODE_200)) {
//                Log.d("HomeUpdateResponse", "response " + response);

//                tempArrayList = new ArrayList<>();
                tempArrayList = response1.getBrandList();

                setEWalletAdapter();


            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void setEWalletAdapter() {

        binding.recyclerViewPlaceHolder.setLayoutManager(new GridLayoutManager(activity, 3));
        eWalletAdapter = new EWalletAdapter(activity, tempArrayList, this);
        binding.recyclerViewPlaceHolder.setAdapter(eWalletAdapter);


    }


    @Override
    public void onItemClick(int position, String tag) {

        ewallet_SessionId = "";
        arrayList = new ArrayList<>();
        Log.d("hjgjhg", "s " + selectedId + " tag " + tag + " pos" + position);

        if (selectedId.equals(tag)) {
            Log.d("hjgjhg", " pos 1");
            selectedId = "";
            arrayList.addAll(tempArrayList);
            binding.pinLayout.setVisibility(View.GONE);

            if (!tempArrayList.get(position).getImage().isEmpty())
                ((EWalletActivity) activity).setTopIcon("");

        } else {
            Log.d("hjgjhg", "size " + tempArrayList.size());
            binding.pinLayout.setVisibility(View.VISIBLE);
            selectedId = tempArrayList.get(position).getEwalletId();
            arrayList.add(tempArrayList.get(position));

            if (!tempArrayList.get(position).getImage().isEmpty())
                ((EWalletActivity) activity).setTopIcon(tempArrayList.get(position).getImage());


        }
        //listener.onItemClick(position,selectedId);
        ((EWalletActivity) activity).setOnDataListener(position, selectedId);
        eWalletAdapter.setItems(selectedId, arrayList);

//        Log.d("Succejhgjhgss","ghfghjhjkkl"+position+tag);


    }

    private void setTopIcon(String s) {
    }

    private void setOnDataListener(int position, String selectedId) {
    }

}