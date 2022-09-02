package in.forpay.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;

import in.forpay.databinding.ActivityDistributorDetailsBinding;
import in.forpay.model.response.GetDistributorDetailsResponse;

import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

import static in.forpay.util.Utility.getCurrentLocation;

public class DistributorDetailsActivity extends AppCompatActivity {

    private String refer="";
    private ActivityDistributorDetailsBinding mBinding;
    private ProgressHelper progressHelper;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setAppLocale(Utility.getDefaultLanguage(this), this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_distributor_details);
        mBinding.setActivity(this);
        activity=DistributorDetailsActivity.this;
        progressHelper = new ProgressHelper(this);
        refer = getIntent().getStringExtra("refer");

        Log.d("LanguageActivity","distributordetails");
        if(refer!=null){
            mBinding.enterReferId.setText(refer);
        }
        init();


        mBinding.continueBtn.setOnClickListener(v->{

            if(!mBinding.enterReferId.getText().toString().isEmpty()){
                Utility.setReferId(activity,mBinding.enterReferId.getText().toString());


            }
            Intent returnIntent = new Intent();

            activity.setResult(Activity.RESULT_OK,returnIntent);
            activity.finish();
        });




        mBinding.enterReferId.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 7){
                    if(before==0 && count==1){
                        init();
                    }
                    else{

                    }


                    Log.d("LanguageActivity","length " +before+" "+count+" "+start);
                }

            }
        });

    }

    private void init(){

        mBinding.resText.setVisibility(View.GONE);
        getCurrentLocation(activity,true);
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.STORAGE_NAME, Context.MODE_PRIVATE);
        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os",Utility.getOs(activity)); // version code

        map1.put("refererOriginal",sharedPreferences.getString("refererOriginal","")); // email
        map1.put("refer",mBinding.enterReferId.getText().toString()); // email
        map1.put("latitude",Utility.getLatitude(activity)); //
        map1.put("longitude",Utility.getLongitude(activity)); //

        String request = Utility.mapWrapper(activity,map1);


        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_REFER_DETAILS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result, responseManager);

                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }

    }


    private void parseResponse(String result, ResponseManager responseManager) {
        if (!isDestroyed()) {
            progressHelper.dismiss();


            Log.d("LanguageActivity","distributordetails result" +result);
            if (Utility.isServerRespond(result/*, responseManager*/)) {

                try{

                    GetDistributorDetailsResponse response = new Gson().fromJson(result, GetDistributorDetailsResponse.class);

                    if (response.getStatus().equals("SUCCESS")) {


                    }

                    if(!response.getRefer().isEmpty()){
                        mBinding.enterReferId.setText(response.getRefer());
                        Utility.setReferId(activity,response.getRefer()+"|"+response.getDistance());
                    }

                    mBinding.distributorName.setText(response.getDistributorName());
                    mBinding.distributorDistance.setText(response.getDistance());

                    mBinding.resText.setText(response.getResText());
                    mBinding.continueBtn.setText(response.getContinueBtn());
                    if(!response.getResText().isEmpty()){
                        mBinding.resText.setVisibility(View.VISIBLE);
                    }







                }
                catch (Exception e){
                    e.printStackTrace();
                    Utility.showToastLatest(this,
                            e.toString(),"ERROR");
                }

            } else {

                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }



        }
    }

}
