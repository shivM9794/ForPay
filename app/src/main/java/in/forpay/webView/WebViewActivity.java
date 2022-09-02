package in.forpay.webView;

import android.app.Activity;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import in.forpay.R;
import in.forpay.util.ProgressHelper;

public class WebViewActivity extends Activity {

    private WebView webView;
    ProgressHelper progressHelper;
    ProgressBar progressBar;
    private ImageView back;
    private TextView web_title;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        progressHelper = new ProgressHelper(this);
        webView = (WebView) findViewById(R.id.webView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        back = findViewById(R.id.back_btn);
        back.setOnClickListener(v -> onBackPressed());
        web_title = findViewById(R.id.web_title);
        String title = getIntent().getStringExtra("webName");
        if(title.equals("tosNew") || title.equals("Term Of Service")){
            web_title.setText(getResources().getString(R.string.title_nav_tos));
        }
        else if(title.equals("sampleKyc")){
            web_title.setText("Sample Kyc");
        }
        else if(title.equals("download")) {
            web_title.setText("Download file");
        }
        else if(title.equals("CashbackPolicy")){
            web_title.setText("Cashback Policy");
        }
        else {
            web_title.setText(getResources().getString(R.string.title_nav_privacy_policy));
        }


        webView.setWebViewClient(new myWebClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        webView.getSettings().setDatabaseEnabled(true);

        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "forpay");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                if(dm!=null) {
                    dm.enqueue(request);
                }
                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();

            }
        });








    }


    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            progressHelper.show();
            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            progressHelper.dismiss();
        }
    }


}