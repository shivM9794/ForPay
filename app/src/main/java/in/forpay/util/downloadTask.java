package in.forpay.util;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class downloadTask extends AsyncTask<String, Void, String> {

    private Activity mActivity;

    public downloadTask (Activity activity){
        mActivity = activity;
    }
    static JSONObject jObj = null;
    private int responseHttp = 0;
    InputStream is;
    JSONArray jsonArray = null;
    static String json = "";
    private static final String PROTOCOL = "";
    private static ProgressHelper progressHelper;
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
       // progressHelper.show();
        Log.v("MainActivityLog", "Begin Download");


    }

    @Override
    public String doInBackground(String... urltest) {

        // TODO Auto-generated method stub
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        //String bannerName=urltest[1];
        String pingData=null;
        if(urltest[1]!=null){
             pingData =urltest[1];
        }
        //Log.d("pingData",pingData);

        try {

            URL url = new URL(urltest[0]);
            /*
        if(pingData!=null){
            if(!pingData.isEmpty()){
                url = new URL(urltest[0]);
                //Log.d("Downloadtask 1",urltest[0]);
            }
            else{
                url=new URL(urltest[0]);
                //Log.d("Downloadtask 2",urltest[0]);
            }
        }
        else{
            url=new URL(urltest[0]);
            //Log.d("Downloadtask 3",urltest[0]);
        }


             */
            URLConnection connection = url.openConnection();

            connection.setReadTimeout(5000);
            //connection.setConnectTimeout(10000);
            HttpURLConnection httpConnection = (HttpURLConnection) connection;

            if(pingData!=null) {
                if (!pingData.isEmpty()) {
                    httpConnection.setRequestMethod("POST");
                }
            }


            try(OutputStream os = httpConnection.getOutputStream()) {
                byte[] input = pingData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            responseHttp = httpConnection.getResponseCode();
            if (responseHttp == HttpURLConnection.HTTP_OK) {

                is = connection.getInputStream();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.d("Downloadtask 5",e.toString());
        }






        //Log.d("Downloadtask 4",urltest[0]+" banner name "+pingData+"json -"+json+"rescode"+responseHttp);
        return json+"resCodeIsAndroid"+responseHttp;
    }

    @Override
    protected void onPostExecute(String file_url) {
        // Hide the progress bar
        //Log.v("MainActivityLog", "Begin Download Compelete");

    }
}
