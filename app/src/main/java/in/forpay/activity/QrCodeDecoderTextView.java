package in.forpay.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import in.forpay.R;
import in.forpay.util.Utility;

public class QrCodeDecoderTextView extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_decoder_textview);

        String resultText = getIntent().getExtras().getString("result");
        TextView textView = findViewById(R.id.recharge_msg);
        TextView openActivity = findViewById(R.id.openActivity);
        ImageView imageView = findViewById(R.id.back_btn);


        imageView.setOnClickListener(v -> finish());

        textView.setText(resultText);
        openActivity.setOnClickListener(v -> {

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(resultText));
                startActivity(intent);
            } catch (Exception e){
                Utility.showToastLatest(this,e.toString(),"ERROR");
            }

        });

    }

}
