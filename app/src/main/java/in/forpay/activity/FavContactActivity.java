package in.forpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.adapter.FavContactAdapter;
import in.forpay.databinding.FragmentFavContactBinding;
import in.forpay.model.FavContactModel;
import in.forpay.model.response.GetFavContactDetailResponse;
import in.forpay.model.response.GetMissCallRecharge;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.RecyclerItemTouchHelper;
import in.forpay.util.Utility;

public class FavContactActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private FragmentFavContactBinding mBinding;
    private Activity activity;
    private ProgressHelper progressHelper;
    private FavContactAdapter adapter;
    private ArrayList<FavContactModel> listData = new ArrayList<>();
    private GetMissCallRecharge missCallRecharge;
    private String missCall="";
    private String missCallNumber="";
    public static ContactUsActivity newInstance() {
        return new ContactUsActivity();
    }

    public FavContactActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = FavContactActivity.this;
        mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_fav_contact);
        //init();

        mBinding.txtAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),AddRechargeActivity.class);
            startActivity(intent);
        });

    }

    private void init() {

        progressHelper = new ProgressHelper(activity);
        mBinding.backBtn.setOnClickListener(v -> onBackPressed());

        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei", Utility.getImei(this)); // imei
        map1.put("versionCode", Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));

        String request = Utility.mapWrapper(this, map1);
        getContact(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(activity, mBinding.appbarLayout);
    }

    /**
     * Set data on UI
     */
    private void setData(ArrayList<GetFavContactDetailResponse.Data> list) {
        if (list.size() == 0) {
            mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);
            mBinding.contactRecycle.setVisibility(View.GONE);
            return;
        }
        mBinding.contactRecycle.setVisibility(View.VISIBLE);

        listData = new ArrayList<>();

        for (GetFavContactDetailResponse.Data data : list) {
            FavContactModel usModel = new FavContactModel();
            usModel.setName(data.getName());
            usModel.setMobile(data.getMobile());
            usModel.setInputValue1(data.getInputValue1());
            usModel.setInputValue2(data.getInputValue2());
            usModel.setOperatorIcon(data.getOperatorIcon());
            usModel.setOperatorId(data.getOperatorId());
            usModel.setDueAmount(data.getDueAmount());
            usModel.setOnClick(data.getOnClick());
            usModel.setSearchData(data.getSearchData());
            usModel.setOperatorName(data.getOperatorName());
            usModel.setMissCall(data.getMissCall());

            usModel.setCanSetMisscall(data.getCanSetMisscall());
            usModel.setMissCallAmount(data.getMissCallAmount());

            listData.add(usModel);
        }

        mBinding.contactRecycle.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new FavContactAdapter(activity, listData,missCall,missCallNumber);
        mBinding.contactRecycle.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mBinding.contactRecycle);


    }


    /**
     * Get contact us detail
     */
    private void getContact(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            //Utility.showToast(activity,"Method is "+Constant.METHOD_FAV_CONTACT);
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_FAV_CONTACT, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect), "");
        }
    }

    /**
     * Parse response
     */
    private void parseResponse(String result) {
        progressHelper.dismiss();
        //mBinding.progressBar.setVisibility(View.GONE);
        if (Utility.isServerRespond(result)) {
            GetFavContactDetailResponse response = new Gson().fromJson(result, GetFavContactDetailResponse.class);


            if (response.getResCode().equals(Constant.CODE_200)) {
                if (response.getDataList().size() > 0) {
                    //Log.d("Database ","here1");
                    missCall=response.getMissCall();
                    missCallNumber=response.getMissCallNumber();
                    setData(response.getDataList());


                } else {
                    //Log.d("Database ","here2");
                    mBinding.contactRecycle.setVisibility(View.GONE);
                    mBinding.textViewNoDataFound.setText(response.getResText());
                    mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);

                    Utility.inflateNoDataFoundLayout(activity, mBinding.inflateLayout, "No Result Found");

                    //Log.d("Database ","here3 "+response.getResText());
                }

            } else {
                if (response.getResCode().equals("129")) {
                    Utility.inflateNoDataFoundLayout(activity, mBinding.inflateLayout, "No Result Found");
                } else {
                    Utility.showToast(activity, response.getResText(), "");
                }

                mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);
                mBinding.textViewNoDataFound.setText(response.getResText());

            }
        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(activity, ServerNotResponseActivity.class));
            mBinding.textViewNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof FavContactAdapter.FavContactHolder) {
            adapter.removeItem(viewHolder.getAdapterPosition());
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}
