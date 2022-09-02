package in.forpay.activity.Webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import in.forpay.R;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class WebViewBrowser extends Activity {

    private WebView myWebView;

    ProgressHelper progressHelper;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywebview);
        progressHelper = new ProgressHelper(this);
        myWebView = (WebView) findViewById(R.id.webview1);
        // Configure related browser settings
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        myWebView.setWebViewClient(new myWebClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setAppCacheEnabled(true);
        myWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        myWebView.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
        myWebView.getSettings().setDatabaseEnabled(true);
        if(getIntent().getBundleExtra("bundle")!=null) {
            try {
                String url = getIntent().getBundleExtra("bundle").getString("url");
                Log.d("WebViewActivity","Url  found "+url);
                myWebView.loadUrl(url);
            }
            catch (Exception e){
                Utility.showToastLatest(WebViewBrowser.this,"No Url Found 2","ERROR");
            }
        }
        else{
            //String url="https://u.godigit.com/4pfc0bf";
            //myWebView.loadUrl(url);
            Utility.showToastLatest(WebViewBrowser.this,"No Url Found","ERROR");
        }
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
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressHelper.dismiss();
        }
    }

}
