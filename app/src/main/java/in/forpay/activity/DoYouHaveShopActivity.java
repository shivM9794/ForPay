package in.forpay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import in.forpay.R;
import in.forpay.databinding.ActivityDoyouhaveshopBinding;
import in.forpay.util.Utility;

public class DoYouHaveShopActivity extends AppCompatActivity {
    private ActivityDoyouhaveshopBinding mBinding;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setAppLocale(Utility.getDefaultLanguage(this), this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_doyouhaveshop);
        mBinding.setActivity(this);

        mBinding.customerShopNo.setOnClickListener(v->{
            Utility.setIsShop(DoYouHaveShopActivity.this,"");
            init();
        });

        mBinding.customerShopYes.setOnClickListener(v->{
            Utility.setIsShop(DoYouHaveShopActivity.this,"1");
            init();
        });

    }


    private void init(){

        Log.d("LanguageActivity","result sent from doyouhaveshop");
        Intent returnIntent = new Intent();

        DoYouHaveShopActivity.this.setResult(Activity.RESULT_OK,returnIntent);
        DoYouHaveShopActivity.this.finish();
    }

}
