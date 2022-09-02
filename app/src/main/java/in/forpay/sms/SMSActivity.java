package in.forpay.sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;

import in.forpay.R;
import in.forpay.databinding.ActivitySmsactivityBinding;

public class SMSActivity extends AppCompatActivity {

    private ActivitySmsactivityBinding binding;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_smsactivity);
        activity = this;
    }
}