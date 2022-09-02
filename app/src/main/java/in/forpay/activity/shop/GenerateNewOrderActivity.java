package in.forpay.activity.shop;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.shop.GenerateNewOrderAdapter;
import in.forpay.databinding.ActivityGenerateNewOrderBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.shop.GenerateNewOrderModel;
import in.forpay.model.shop.GenerateOrderModel;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class GenerateNewOrderActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityGenerateNewOrderBinding binding;
    private AppCompatActivity activity;
    private GenerateNewOrderAdapter adapter;
    private ArrayList<GenerateNewOrderModel> arrayList = new ArrayList<>();
    private ProgressHelper progressHelper;
    private String userId, deliveryDays, deliveryCharge;
    private StringBuilder itemDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGenerateNewOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        progressHelper = new ProgressHelper(activity);

        init();
    }

    private void init() {
        addEmptyData();
        setGenerateNewOrderAdapter();
        binding.backPress.setOnClickListener(v -> onBackPressed());

        binding.addMoreItem.setOnClickListener(v -> {
            addEmptyData();
            adapter.notifyDataSetChanged();
        });

        binding.createOrder.setOnClickListener(v -> {
            itemDetails = new StringBuilder();
            if (checkValidation()) {
                for (GenerateNewOrderModel model : arrayList) {
                    if (model.getItemName().isEmpty()) {
                        Utility.showToast(activity, "Please Enter Item Name", "");
                    } else if (model.getPrice().isEmpty()) {
                        Utility.showToast(activity, "Please Enter Price", "");
                    } else {
                        itemDetails.append(model.getItemName() + "<>" + model.getQuantity() + "<>" +
                                (Integer.parseInt(model.getPrice()) * Integer.parseInt(model.getQuantity())) + ";");
                        getGenerateOrder();
                    }
                }
            }
        });
    }

    private void addEmptyData() {
        arrayList.add(new GenerateNewOrderModel("", "0", ""));
    }

    private void setGenerateNewOrderAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new GenerateNewOrderAdapter(activity, arrayList, this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, String tag) {

    }

    private boolean checkValidation() {
        userId = binding.userId.getText().toString();
        deliveryDays = binding.deliveryDays.getText().toString();
        deliveryCharge = binding.deliveryCharge.getText().toString();
        if (userId.isEmpty()) {
            binding.userId.setError("Please Enter User Unique Id");
            return false;
        } else if (deliveryDays.isEmpty()) {
            binding.deliveryDays.setError("Please Enter Delivery Days");
            return false;
        } else if (deliveryCharge.isEmpty()) {
            binding.deliveryCharge.setError("Please Enter Delivery Charge");
            return false;
        } else
            return true;
    }

    private void getGenerateOrder() {
        if (Utility.isNetworkConnected(this)) {

            Map<String, String> map1 = new HashMap<>();
            map1.put("token", Utility.getToken(this)); // key
            map1.put("imei",Utility.getImei(this));
            map1.put("appVersion", Utility.getVersionCode(this));
            map1.put("uniqid",userId);
            map1.put("itemDetails",itemDetails.toString());
            map1.put("deliveryCharge",deliveryCharge);
            map1.put("deliveryExpectedDays",deliveryDays);

            String request = Utility.mapWrapper(this, map1);

            generateOrderRequest(request);

        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void generateOrderRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_GENERATE_ORDER, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseGenerateOrderResponse(result, responseManager);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect), "");
        }
    }

    private void parseGenerateOrderResponse(String response, ResponseManager responseManager) {
        progressHelper.dismiss();
        try {
            Log.d("ShopCategoryResponse", "response " + response);

            GenerateOrderModel model = new Gson().fromJson(response, GenerateOrderModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                binding.userId.setText("");
                binding.deliveryDays.setText("");
                binding.deliveryCharge.setText("");
                userId = "";
                deliveryDays = "";
                deliveryCharge = "";
                itemDetails.setLength(0);
                arrayList.clear();
                addEmptyData();
                adapter.notifyDataSetChanged();
                binding.userId.clearFocus();
                binding.recyclerView.clearFocus();
                binding.deliveryDays.clearFocus();
                binding.deliveryCharge.clearFocus();

                Utility.showToast(this, "Order Successfully Created", "200");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}