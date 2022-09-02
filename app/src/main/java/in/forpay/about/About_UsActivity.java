package in.forpay.about;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.adapter.SocialMediaAdapter;
import in.forpay.databinding.ActivityAboutUsBinding;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.model.response.GetSocialListResponse;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class About_UsActivity extends AppCompatActivity {

    private ActivityAboutUsBinding binding;
    private AppCompatActivity activity;
    private ProgressHelper progressHelper;
    private OxyMePref oxyMePref;
    private String type = "SocialList";
    private String search_type;
    Boolean fromWeb = true;
    private ArrayList<GetSocialListResponse.DataList> mList = new ArrayList<>();
    private String TAG = "AboutUSActivity";
    private SocialMediaAdapter mediaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        activity = this;
        progressHelper = new ProgressHelper(activity);
        oxyMePref = new OxyMePref(activity);

        inits();

        binding.backBtn.setOnClickListener(v -> finish());


    }

    private void inits() {

        progressHelper.show();

        Utility.getServiceList(activity, type, Constant.METHOD_SOCIAL, fromWeb, "AboutUSActivity", new HomeUpdateListener() {
            @Override
            public void onDone() {
                progressHelper.dismiss();
                //parseHomeUpdateResponse(oxyMePref.getString(Constant.SERVICE_LIST_RESPONSE));
                String serviceListLocation = "serviceList_" + type + Constant.METHOD_SOCIAL;
                parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation));
            }
        });

        binding.titleSocial.setText("Social Media");

    }

    private void parseHomeUpdateResponse(String response) {

        try {

            GetSocialListResponse response1 = new Gson().fromJson(response, GetSocialListResponse.class);

            if (response1.getResCode().equals(Constant.CODE_200)) {

                mList = response1.getDataList();

                setSocialAdapter();


            }

        } catch (Exception e) {
            Log.d("AboutUsResponse", "response error" + e.toString());
            e.printStackTrace();
        }
    }

    private void setSocialAdapter() {

        binding.recyclerSocialMedia.setLayoutManager(new LinearLayoutManager(activity));
//        Log.d("dfdgfdgds", "fdfdfgdfgd" + mList.size());
        mediaAdapter = new SocialMediaAdapter(activity, mList);
        binding.recyclerSocialMedia.setAdapter(mediaAdapter);

    }
}