package in.forpay.activity.supportchat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import java.io.File;

import in.forpay.R;
import in.forpay.databinding.ActivityImageDetailBinding;
import in.forpay.util.Constant;

public class ImageDetailActivity extends AppCompatActivity {

    private ActivityImageDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_detail);

        String filePath = getIntent().getStringExtra("img_url");
        showImageFromFilePath(filePath);

        boolean isFromShopChat=getIntent().getBooleanExtra(Constant.IS_FROM_SHOP_CHAT,false);
        if (isFromShopChat){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
                getWindow().getDecorView().setSystemUiVisibility(flags);
                getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.gray_bg_shop)); // optional
            }
        }

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showImageFromFilePath(String filePath) {
        try {
            File imgFile = new File(filePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                binding.fullImage.setImageBitmap(myBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
