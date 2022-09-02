package in.forpay.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import es.dmoral.toasty.Toasty;
import in.forpay.R;
import in.forpay.util.ProgressHelper;

public class VideoViewActivity extends AppCompatActivity {
    private ProgressHelper progressHelper;
    private Uri videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        progressHelper=new ProgressHelper(this);
        progressHelper.show();

        VideoView videoView = findViewById(R.id.videoView);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        if(getIntent().getStringExtra("url")!=null && !getIntent().getStringExtra("url").isEmpty()) {
            videoUrl = Uri.parse(getIntent().getStringExtra("url"));
            videoView.setVideoURI(videoUrl);
            videoView.start();
        }

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progressHelper.dismiss();

            }
        });

        findViewById(R.id.download_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(VideoViewActivity.this)
                        .setTitle("Download")
                        .setMessage("Want to download this video, in case if not play.")
                        .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                downloadVideo();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();

            }
        });

    }

    private void downloadVideo(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }

        else{
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(videoUrl);
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Forpay_KYC.mp4");
            downloadManager.enqueue(request);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101){
            if (resultCode == RESULT_OK  )
                downloadVideo();
            else
                Toasty.error(VideoViewActivity.this,"Please Provide Permission to download video.").show();
        }
    }
}