package in.forpay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import in.forpay.R;
import in.forpay.databinding.ActivityServerNotResponseBinding;

public class ServerNotResponseActivity extends AppCompatActivity {

    private ActivityServerNotResponseBinding binding;
    private AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityServerNotResponseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        binding.back.setOnClickListener(v -> onBackPressed());
    }
}