package in.forpay.activity.bbps;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import in.forpay.R;
import in.forpay.databinding.ActivityBbpsComplaintBinding;

public class BbpsComplaint extends AppCompatActivity {


    ActivityBbpsComplaintBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(BbpsComplaint.this,R.layout.activity_bbps_complaint);

        binding.backBtn.setOnClickListener(view -> onBackPressed());

        binding.btnCheckStatus.setOnClickListener(view -> {
            Intent intent = new Intent(BbpsComplaint.this,ComplaintStatusActivity.class);
            startActivity(intent);
        });

        binding.btnNewComplaint.setOnClickListener(view -> {
            Intent intent = new Intent(BbpsComplaint.this,RaiseComplaint.class);
            startActivity(intent);
        });

        binding.btnSearchTransaction.setOnClickListener(view -> {
            Intent intent = new Intent(BbpsComplaint.this,SearchTransaction.class);
            startActivity(intent);
        });
    }
}