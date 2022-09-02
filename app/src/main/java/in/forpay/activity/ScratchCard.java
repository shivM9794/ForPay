package in.forpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cooltechworks.views.ScratchTextView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.model.response.GetScratchCardResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class ScratchCard extends AppCompatActivity {

    private TextView claim;
    private Activity activity;
    private ProgressHelper progressHelper;
    private TextView idScratchCardIv;
    private String scratchId="";
    ScratchTextView scratchTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);
        activity = ScratchCard.this;
        progressHelper=new ProgressHelper(activity);
        claim = findViewById(R.id.claim);
        ImageView imageView = findViewById(R.id.back_btn);
        idScratchCardIv=findViewById(R.id.idScratchCardIv);
        imageView.setOnClickListener(v -> finish());
        init();

        claim.setOnClickListener(v->{
            init();
        });
         scratchTextView = findViewById(R.id.idScratchCardIv);
        scratchTextView.setRevealListener(new ScratchTextView.IRevealListener() {
            @Override
            public void onRevealed(ScratchTextView iv) {
                // this method is called after revealing the image.
                //Toast.makeText(ScratchCard.this, "image is revealed", Toast.LENGTH_SHORT).show();
                if(!scratchId.isEmpty()){
                    claim.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onRevealPercentChangedListener(ScratchTextView siv, float percent) {
                // we can check how much percentage of
                // image is revealed using percent variable
            }
        });
    }


    private void init(){

        progressHelper.show();
        Map<String,String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(this)); // key
        map1.put("imei",Utility.getImei(this)); // imei
        map1.put("versionCode",Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("scratchId",scratchId);

        String request = Utility.mapWrapper(this,map1);

        getProfileDetails(request);
    }


    private void getProfileDetails(String request) {
        if (Utility.isNetworkConnected(activity)) {
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_SCRATCH_CARD, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            progressHelper.dismiss();
                            parseResponse(result);
                        }
                    });
        }
        else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }

    private void parseResponse(String result) {
        if (Utility.isServerRespond(result)) {
            GetScratchCardResponse response = new Gson().fromJson(result, GetScratchCardResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {
                if(response.getScratchId().isEmpty()){
                    idScratchCardIv.setText(response.getPoints());
                    claim.setVisibility(View.GONE);
                    Utility.showToast(activity, response.getResText(),response.getResCode());
                    scratchId="";

                }
                else {
                    idScratchCardIv.setText(response.getPoints());
                    scratchId=response.getScratchId();
                }


            } else {
                Utility.showToast(activity, response.getResText(),response.getResCode());


            }
        } else {

            startActivity(new Intent(activity, ServerNotResponseActivity.class));


        }
    }


}
