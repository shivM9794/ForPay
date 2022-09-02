package in.forpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.supportchat.SupportChatActivity;
import in.forpay.adapter.ContactUsAdapter;
import in.forpay.databinding.FragmentContactUsBinding;
import in.forpay.model.ContactUsModel;
import in.forpay.model.response.GetContactDetailResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ContactUsActivity extends AppCompatActivity {

    private FragmentContactUsBinding mBinding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private ContactUsAdapter adapter;
    private ArrayList<ContactUsModel> listData = new ArrayList<>();
    public static ContactUsActivity newInstance() {
        return new ContactUsActivity();
    }

    public ContactUsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ContactUsActivity.this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_contact_us);
        progressHelper = new ProgressHelper(activity);
        init();
    }


    private void init() {
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());
        mBinding.btnClear.setOnClickListener(v -> mBinding.edtSearch.setText(""));

        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));

        String request = Utility.mapWrapper(activity,map1);

        getContact(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(activity,mBinding.appbarLayout);
    }

    /**
     * Set data on UI
     */
    private void setData(ArrayList<GetContactDetailResponse.Data> list) {
        if (list.size() == 0) {
            mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);
            mBinding.contactRecycle.setVisibility(View.GONE);
            return;
        }
        mBinding.contactRecycle.setVisibility(View.VISIBLE);

        listData = new ArrayList<>();

       for (GetContactDetailResponse.Data data: list){
            ContactUsModel usModel = new ContactUsModel();
            usModel.setName(data.getName());
            usModel.setDepartment(data.getDepartment());
            usModel.setLanguage(data.getLanguage());
            usModel.setMobile(data.getMobile());
            usModel.setStatus(data.getStatus());

            listData.add(usModel);
      }

            mBinding.contactRecycle.setLayoutManager(new LinearLayoutManager(activity));
            adapter =new ContactUsAdapter(activity,listData);
            mBinding.contactRecycle.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        mBinding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterData(String text) {
        try {
            ArrayList<ContactUsModel> filteredList2 = new ArrayList<>();

            for (ContactUsModel item : listData) {
                if (item.getDepartment().toLowerCase().contains(text.toLowerCase()) ||
                        item.getLanguage().toLowerCase().contains(text.toLowerCase()) ||
                        item.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList2.add(item);
                }
            }

            if (filteredList2.size()==0){
                Utility.inflateNoDataFoundLayout(activity, mBinding.inflateLayout, "No Result Found");
            }else {
                mBinding.inflateLayout.removeAllViews();
                mBinding.inflateLayout.addView(mBinding.contactRecycle);
            }
            if (adapter != null)
                adapter.filterList(filteredList2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Get contact us detail
     */
    private void getContact(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_CONTACT_US, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
        progressHelper.dismiss();
            //mBinding.progressBar.setVisibility(View.GONE);
            if (Utility.isServerRespond(result)) {
                GetContactDetailResponse response = new Gson().fromJson(result, GetContactDetailResponse.class);

                if(response.getGotoChat()!=null){
                    if(response.getGotoChat().equals("1")){

                        Utility.showToast(this, getString(R.string.error_no_agent_available), "");
                        Intent intent = new Intent(this,
                                SupportChatActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }

                if (response.getResCode().equals(Constant.CODE_200)) {
                    if(response.getDataList().size()>0){
                        //Log.d("Database ","here1");
                        setData(response.getDataList());
                        mBinding.btnMainEmail.setText(response.getEmail());
                    }
                    else{
                        //Log.d("Database ","here2");
                        mBinding.contactRecycle.setVisibility(View.GONE);
                        mBinding.textViewNoDataFound.setText(response.getResText());
                        mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);

                        mBinding.btnMainEmail.setText(response.getEmail());
                        Utility.inflateNoDataFoundLayout(activity, mBinding.inflateLayout, "No Result Found");

                        //Log.d("Database ","here3 "+response.getResText());
                    }

                } else {
                    if (response.getResCode().equals("129")){
                        Utility.inflateNoDataFoundLayout(activity, mBinding.inflateLayout, "No Result Found");
                    }else {
                        Utility.showToast(activity, response.getResText(),"");
                    }

                    mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);
                    mBinding.textViewNoDataFound.setText(response.getResText());
                    mBinding.btnMainEmail.setText(response.getEmail());
                }
            } else {
                //Utility.showToast(activity, getString(R.string.server_not_responding),"");
                startActivity(new Intent(activity, ServerNotResponseActivity.class));
                mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);
            }
    }
}
