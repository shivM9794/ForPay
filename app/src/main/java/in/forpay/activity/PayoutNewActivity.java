package in.forpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import in.forpay.R;
import in.forpay.adapter.PayoutBankListAdapter;
import in.forpay.databinding.ActivityPayoutNewBinding;
import in.forpay.dialog.BottomSheetDialog;
import in.forpay.model.GetPayoutBankListModel;
import in.forpay.model.response.GetPayoutBankDetails;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.RecyclerItemTouchHelperPayout;
import in.forpay.util.Utility;

public class PayoutNewActivity extends AppCompatActivity implements RecyclerItemTouchHelperPayout.RecyclerItemTouchHelperListener {

    ActivityPayoutNewBinding binding;
    ProgressHelper progressHelper;
    Activity activity;
    PayoutBankListAdapter payoutBankListAdapter;
    private ArrayList<GetPayoutBankListModel> listData;
    String paymentMethod="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payout_new);
        activity = this;
        progressHelper = new ProgressHelper(this);
        setToolbar();
        initReq();
        init();

    }

    private void setData(ArrayList<GetPayoutBankDetails.Data> list) {
        //Log.d("jhgsdjfgdsj",mList.size()+"");


        listData = new ArrayList<>();

        for (GetPayoutBankDetails.Data data : list) {
            GetPayoutBankListModel usModel = new GetPayoutBankListModel();
            usModel.setName(data.getName());
            usModel.setAccountNumber(data.getAccountNumber());
            usModel.setIfscCode(data.getIfscCode());
            usModel.setStatus(data.getStatus());

            listData.add(usModel);
        }

        binding.recyclerListBankDetails.setLayoutManager(new LinearLayoutManager(activity));
        payoutBankListAdapter = new PayoutBankListAdapter(activity, listData, progressHelper,paymentMethod);
        binding.recyclerListBankDetails.setAdapter(payoutBankListAdapter);
        payoutBankListAdapter.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelperPayout(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerListBankDetails);

    }

    private void initReq() {
        Log.e("cal", "true");
        progressHelper.show();

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));

        String request = Utility.mapWrapper(this, map1);

        if (in.forpay.util.Utility.isNetworkConnected(activity)) {

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_PAYOUT_BANK_DETAILS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            progressHelper.dismiss();
                            parseResponse(result);


                        }
                    });
        } else {
            in.forpay.util.Utility.showToast(activity, getString(R.string.internet_connect), "");
        }


    }

    private void parseResponse(String result) {

        if(result!=null) {
            GetPayoutBankDetails response = new Gson().fromJson(result, GetPayoutBankDetails.class);

            if (response.getResCode().equals(Constant.CODE_200)) {
                binding.textBalance.setText("₹ " + response.getBalance());

                if (response.getShowAddButton().equals("yes")) {
                    binding.addBankDetailsBtn.setVisibility(View.VISIBLE);
                }
                paymentMethod=response.getPaymentMethod();

                setData(response.getBankDetails());


            } else {
                Utility.showToastLatest(activity, response.getResText(), response.getResCode());
            }
        }
        else{
            Utility.showToastLatest(activity,"Please try again","ERROR");
        }
    }

    private void init() {


        binding.addBankDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                bottomSheetDialog.show(getSupportFragmentManager(), "abcd");


            }
        });


    }


    private void setToolbar() {
        binding.backBtn.setOnClickListener(v -> onBackPressed());
        binding.offerTitle.setText("Payout");
    }


    private void parseResponseRecharge(String result) {
        if (!isDestroyed()) {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

                if (response.getResCode().equals(Constant.CODE_200)) {

                    Intent intent = new Intent(this, RechargeProcessActivity.class);
                    intent.putExtra("amount", response.getData().getBillAmount());
                    intent.putExtra("mobile", Utility.getUsername(this));
                    intent.putExtra("operatorId", "250");
                    intent.putExtra("creditUsed", response.getData().getCreditUsed());
                    intent.putExtra("bal", response.getData().getBal());
                    intent.putExtra("service", response.getData().getService());
                    intent.putExtra("billAmount", response.getData().getBillAmount());
                    intent.putExtra("billName", response.getData().getBillName());
                    intent.putExtra("beneficiaryAccountNumber", response.getData().getBeneficiaryAccountNumber());
                    intent.putExtra("beneficiaryName", response.getData().getBeneficiaryName());
                    intent.putExtra("type", "payout");
                    intent.putExtra("pin", "0000");
                    intent.putExtra("extraAmount", "");
                    intent.putExtra("profit", response.getData().getProfit());
                    intent.putExtra("customerPay", response.getData().getCustomerPay());
                    intent.putExtra("serviceCharge", response.getData().getServiceCharge());
                    intent.putExtra("opValue1", "");
                    intent.putExtra("starSelected", response.getData().getStarSelected());
                    intent.putExtra("validateDetails", response.getData().getValidateDetails());

                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Utility.showToastLatest(this, e.toString(), "ERROR");
                    }


                } else {
                    Utility.showToast(this, response.getResText(), response.getResCode());
                }


            } else {
                Utility.showToast(this, getString(R.string.server_not_responding), "");
            }
        }
    }

    private void processPayout() {


        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("operatorId", "250");
        map1.put("mobile", Utility.getUsername(activity));
        map1.put("pin", "0000");


        String request = Utility.mapWrapper(this, map1);


        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponseRecharge(result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }

    }

    private void SubmitReq() {
        Log.e("cal", "true");
        progressHelper.show();

        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("bankSubmit", "yes");

        String request = Utility.mapWrapper(this, map1);


        if (in.forpay.util.Utility.isNetworkConnected(activity)) {

            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_PAYOUT, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {

                            progressHelper.dismiss();


                            try {
                                JSONObject jsonObject = new JSONObject(result);

                                if (jsonObject.optString("resCode").equals(Constant.CODE_200)) {
//                                initUI(jsonObject.getJSONArray("fieldArray"));
                                    binding.textBalance.setText(jsonObject.optString("balance"));
                                    Toasty.success(PayoutNewActivity.this, jsonObject.getString("resText")).show();


                                    if (jsonObject.optString("processPayout").equals("yes")) {
                                        processPayout();
                                        //finish();
                                    }
                                    //processPayout();

                                } else
                                    Utility.showToastLatest(PayoutNewActivity.this, jsonObject.getString("resText"), "ERROR");


                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }

                        }
                    });
        } else {
            in.forpay.util.Utility.showToast(activity, getString(R.string.internet_connect), "");
        }


    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof PayoutBankListAdapter.PayoutBankListHolder) {
            payoutBankListAdapter.removeItem(viewHolder.getAdapterPosition());
        }


    }

}
