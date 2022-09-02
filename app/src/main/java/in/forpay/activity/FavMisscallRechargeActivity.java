package in.forpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import in.forpay.R;
import in.forpay.databinding.ActivityFavMisscallRechargeBinding;

public class FavMisscallRechargeActivity extends AppCompatActivity {

    private ActivityFavMisscallRechargeBinding binding;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_fav_misscall_recharge);
        activity = this;

        binding.backBtn.setOnClickListener(v -> finish());
    }
}