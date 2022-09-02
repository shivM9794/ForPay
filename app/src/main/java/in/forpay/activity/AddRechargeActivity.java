package in.forpay.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.AddRechargeAdapter;
import in.forpay.databinding.ActivityAddRechargeBinding;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.response.GetAddRechargeFavContactResponse;
import in.forpay.model.response.GetMissCallRecharge;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class AddRechargeActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityAddRechargeBinding binding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private OxyMePref oxyMePref;
    private AddRechargeAdapter addRechargeAdapter;
    private ArrayList<GetAddRechargeFavContactResponse.OperatorList> tempArrayList;
    private ArrayList<GetAddRechargeFavContactResponse.OperatorList> arrayList;

    private ArrayList<GetAddRechargeFavContactResponse.OperatorList> searchArrayList;

    private String selectedId = "";
    private String operatorId, bbpsId, serviceType = "";
    private String type = "AddRecharge";
    Boolean fromWeb = true;
    private String mobile, amt, lab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_recharge);
        activity = this;
        progressHelper = new ProgressHelper(activity);
        oxyMePref = new OxyMePref(activity);
        binding.backBtn.setOnClickListener(v -> finish());

        init();

    }

    private void init() {

        progressHelper.show();

        Utility.getServiceList(activity, type, Constant.METHOD_FAV_CONTACT_OPERATOR, fromWeb, "AddRechargeActivity", new HomeUpdateListener() {
            @Override
            public void onDone() {
                progressHelper.dismiss();
                String serviceListLocation = "serviceList_" + type + Constant.METHOD_FAV_CONTACT_OPERATOR;
                parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation));
            }
        });
        Log.d("gdfgdgd", " 0 ");

        binding.edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("gdfgdgd", " 5 ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {

                    Log.d("gdfgdgd", " 4 ");
                    filterData(s.toString());

                    Log.d("gdfgdgd", " 3 ");
                }
                else{
                    filterData("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("gdfgdgd", " 6 ");
            }
        });


    }


    private void filterData(String text) {
        Log.d("gdfgdgd", " 7 ");
        try {
            Log.d("gdfgdgd", " 1 ");
            ArrayList<GetAddRechargeFavContactResponse.OperatorList> filteredList2 = new ArrayList<>();


            for (GetAddRechargeFavContactResponse.OperatorList item : tempArrayList) {
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList2.add(item);
                    Log.d("gdfgdgd", " filter " + item);
                }
            }
            setEWalletAdapter(filteredList2);


        } catch (Exception e) {

            Log.d("gdfgdgd", " 2 ");
            e.printStackTrace();
        }
    }

    private void parseHomeUpdateResponse(String response) {
        try {
            GetAddRechargeFavContactResponse response1 = new Gson().fromJson(response, GetAddRechargeFavContactResponse.class);
            if (response1.getResCode().equals(Constant.CODE_200)) {
//                Log.d("HomeUpdateResponse", "response " + response);
                tempArrayList = response1.getOperatorList();

                setEWalletAdapter(tempArrayList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEWalletAdapter(ArrayList<GetAddRechargeFavContactResponse.OperatorList> searchArrayList2) {
        searchArrayList=searchArrayList2;
        binding.recyclerAddRecharge.setLayoutManager(new GridLayoutManager(activity, 3));
        addRechargeAdapter = new AddRechargeAdapter(activity, this, searchArrayList2);
        binding.recyclerAddRecharge.setAdapter(addRechargeAdapter);
    }

    @Override
    public void onItemClick(int position, String tag) {

        arrayList = new ArrayList<>();
        //Log.d("hjgjhg", "s " + selectedId + " tag " + tag + " pos" + position+" "+tempArrayList.get(position).getPlaceHolder());


        if (selectedId.equals(tag)) {
            // Log.d("hjgjhg", " pos 1");
            selectedId = "";
            arrayList.addAll(searchArrayList);
            binding.pinLayout.setVisibility(View.GONE);

            if (!searchArrayList.get(position).getIconUrl().isEmpty())
                ((AddRechargeActivity) activity).setTopIcon("");

        } else {
            //Log.d("hjgjhg", "size " + tempArrayList.size());
            operatorId = searchArrayList.get(position).getOperatorId();
            bbpsId = searchArrayList.get(position).getBbpsId();
            serviceType = searchArrayList.get(position).getServiceType();
            binding.pinLayout.setVisibility(View.VISIBLE);
            selectedId = searchArrayList.get(position).getOperatorId();
            binding.editMobileUser.setHint(searchArrayList.get(position).getPlaceHolder());
            Utility.hideKeyboard(this);
            binding.label.setText(searchArrayList.get(position).getLabel());
            mobile = searchArrayList.get(position).getPlaceHolder();
            binding.editAmtUser.getText().toString();
            lab = searchArrayList.get(position).getLabel();
            arrayList.add(searchArrayList.get(position));

            if (!searchArrayList.get(position).getIconUrl().isEmpty())
                ((AddRechargeActivity) activity).setTopIcon(searchArrayList.get(position).getIconUrl());
        }
        ((AddRechargeActivity) activity).setOnDataListener(position, selectedId);
        addRechargeAdapter.setItems(selectedId, arrayList);

//        Log.d("Succejhgjhgss","ghfghjhjkkl"+position+tag);

    }

    private void setTopIcon(String s) {
    }

    private void setOnDataListener(int position, String selectedId) {
    }

    public void rechargeBtn(View view) {

        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("mobile", binding.editMobileUser.getText().toString());
        map1.put("amount", binding.editAmtUser.getText().toString());
        map1.put("operatorId", operatorId);
        map1.put("bbpsId", bbpsId);
        map1.put("serviceType", serviceType);

        String request = Utility.mapWrapper(this, map1);

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_ADD_FAV_CONTACT_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseHomeUpdateResponse1(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }

        //Toast.makeText(getApplicationContext(), "Recharge Successful" + " " + binding.editMobileUser.getText().toString() + " " + " ₹" + binding.editAmtUser.getText().toString() + " " + binding.operatorId.getText().toString() + " " + binding.bbpsId.getText().toString(), Toast.LENGTH_SHORT).show();
    }


    private void parseHomeUpdateResponse1(String response) {
        progressHelper.dismiss();
        try {
            GetMissCallRecharge response1 = new Gson().fromJson(response, GetMissCallRecharge.class);
            Utility.showToastLatest(activity, response1.getResText(), response1.getResCode());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

