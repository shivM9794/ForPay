package in.forpay.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.adapter.LanguageListAdapter;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.DialogFragmentChooseLanguageBinding;
import in.forpay.model.request.LanguageList;
import in.forpay.util.Constant;
import in.forpay.util.Utility;

public class LanguageChangeActivity extends AppCompatActivity {

    private DialogFragmentChooseLanguageBinding mBinding;
    private DatabaseHelper databaseHelper;
    private Activity activity;
    private Boolean sendToServer=false;
    private String loginType="";

    public static LanguageChangeActivity newInstance(){
        return new LanguageChangeActivity();
    }
    public LanguageChangeActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = LanguageChangeActivity.this;
        Utility.setAppLocale(Utility.getDefaultLanguage(this), this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_fragment_choose_language);

        mBinding.backBtn.setOnClickListener(v -> onBackPressed());

        loginType = getIntent().getStringExtra("loginType");

        if(loginType==null) {
            sendToServer = true;
        }
        else if (!loginType.equals(Constant.ACTION_OTP_ACTIVITY)) {
                sendToServer = true;
            }
        //Log.d("LanguageActivity",loginType+"sendToserver "+sendToServer);
        init();



    }



    private void init(){
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        databaseHelper = new DatabaseHelper(activity);
        ArrayList<LanguageList> list = databaseHelper.getLanguageList();

        //Log.d("TAG",""+list.get(0).getLanguage());

        setAdapter(list);
    }

    private void setAdapter(ArrayList<LanguageList> list) {
        if (list == null || list.size() == 0) {
            return;
        }



        mBinding.recyclerView.setAdapter(new LanguageListAdapter(activity, list,sendToServer));
    }

}
