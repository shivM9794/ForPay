package in.forpay.activity.balance;

import android.content.Intent;
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

import in.forpay.R;
import in.forpay.adapter.balance.GiftVoucherAdapter;
import in.forpay.databinding.ActivityGiftVoucherBinding;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.response.GiftVoucherResponse;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class GiftVoucherActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityGiftVoucherBinding binding;
    private AppCompatActivity activity;
    private GiftVoucherAdapter adapter;
    private ArrayList<GiftVoucherResponse.ServiceBean> serviceArrayList=new ArrayList<>();
    private ArrayList<GiftVoucherResponse.ServiceBean> tempServiceArrayList=new ArrayList<>();
    private GiftVoucherResponse model;
    private OxyMePref oxyMePref;
    private String type;
    private boolean isFromHomeActivity;
    private String inputData;
    private ProgressHelper progressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_gift_voucher);
        activity=this;
        oxyMePref = new OxyMePref(activity);

        type=getIntent().getStringExtra(Constant.TYPE);
        isFromHomeActivity=getIntent().getBooleanExtra(Constant.IS_FROM_HOME_ACTIVITY,false);
        //inputData=getIntent().getStringExtra(Constant.INPUT_DATA);
        progressHelper = new ProgressHelper(activity);

        Bundle extras = getIntent().getExtras().getBundle("bundle");

        inputData=extras.getString(Constant.INPUT_DATA,"");


        progressHelper.show();
        Log.d("GiftVoucherAcitysfsfs","input Data "+inputData);
        Utility.getServiceList(activity, type,Constant.METHOD_GIFTVOUCHER_LIST,false,"GiftVoucherActivity", new HomeUpdateListener() {
            @Override
            public void onDone() {
                progressHelper.dismiss();
                String serviceListLocation="serviceList_"+type+Constant.METHOD_GIFTVOUCHER_LIST;
                parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation));
            }
        });


        if (inputData!=null){
            if(!inputData.isEmpty()) {
                binding.edtSearch.setText(inputData);

                Log.d("GiftVoucherAcitysfsfs","filteer input Data "+inputData);
                filterData(inputData);
            }
        }

        init();
    }

    private void parseHomeUpdateResponse(String response) {
        try {
            model=new Gson().fromJson(response, GiftVoucherResponse.class);
            if (model.getResCode().equals(Constant.CODE_200) && model.getBrandList()!=null) {
                Log.d("GiftVoucherActivity","response "+response);
                tempServiceArrayList=new ArrayList<>();
                serviceArrayList=new ArrayList<>();

                for (GiftVoucherResponse.ServiceBean serviceBean:model.getBrandList()){

                        serviceArrayList.add(serviceBean);

                }
                tempServiceArrayList.addAll(serviceArrayList);

                if (serviceArrayList==null || serviceArrayList.size()<12){
                    binding.searchLayout.setVisibility(View.GONE);
                }

                if (serviceArrayList==null ||serviceArrayList.size()==0)
                    Utility.inflateNoDataFoundLayout(activity, binding.inflateLayout,"No Data Found");
                else
                    setAdapter();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        binding.backPress.setOnClickListener(view -> onBackPressed());
        binding.clear.setOnClickListener(view -> {
            binding.edtSearch.setText("");
            binding.inflateLayout.removeAllViews();
            binding.inflateLayout.addView(binding.recyclerView);
            Utility.hideKeyboard(activity);
        });
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()){
                    filterData(s.toString());
                }else {
                    if (adapter != null) {
                        adapter.filterList(serviceArrayList);
                        tempServiceArrayList=new ArrayList<>();
                        tempServiceArrayList.addAll(serviceArrayList);
                    }
                    binding.inflateLayout.removeAllViews();
                    binding.inflateLayout.addView(binding.recyclerView);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setAdapter() {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(activity,2));
        adapter=new GiftVoucherAdapter(activity,serviceArrayList,this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, String tag) {
        startActivity(new Intent(activity,GiftVoucherDetailActivity.class).putExtra(Constant.SERVICE_ARRAY_LIST,tempServiceArrayList.get(position)));
        //binding.edtSearch.setText("");
    }
    private void filterData(String text) {
        try {
            ArrayList<GiftVoucherResponse.ServiceBean> filteredList2 = new ArrayList<>();


            for (GiftVoucherResponse.ServiceBean item : serviceArrayList) {
                if (item.getBrandName().toLowerCase().contains(text.toLowerCase()) || item.getCategory().toLowerCase().contains(text.toLowerCase())) {
                    filteredList2.add(item);
                }
            }

            if (adapter != null) {
                adapter.filterList(filteredList2);
                tempServiceArrayList=new ArrayList<>();
                tempServiceArrayList.addAll(filteredList2);
            }

            if (filteredList2.size()==0){
                Utility.inflateNoDataFoundLayout(activity, binding.inflateLayout,"No Search Data Found");
            }else {
                binding.inflateLayout.removeAllViews();
                binding.inflateLayout.addView(binding.recyclerView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}