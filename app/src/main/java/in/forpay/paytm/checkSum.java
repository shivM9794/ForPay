package in.forpay.paytm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.model.response.GetAddFundResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class checkSum extends AppCompatActivity implements PaytmPaymentTransactionCallback {
    String cusId,orderId,amt,mid,response="";
    private ProgressHelper progressHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



    init("");

    }

    private void init(String type){
        progressHelper = new ProgressHelper(this);

        amt = getIntent().getStringExtra("amount");

        mid=Utility.getGatewayMid(this);
        cusId="noValue";


        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(this)); // key
        map1.put("imei",Utility.getImei(this)); // imei
        map1.put("versionCode" , Utility.getVersionCode(this)); // version code
        map1.put("os", Utility.getOs(this));
        map1.put("amount",amt);

        map1.put("gateway",Utility.getGatewayName(this));

        ;


        if(type.equals("response")) {
        map1.put("response",response);
            String request = Utility.mapWrapper(this,map1);
            paymentResponseRequest(request);
        }
        else{
            String request = Utility.mapWrapper(this,map1);
            paymentRequest(request);
        }
    }

    private void paymentResponseRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();



            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_ADDFUND_GATEWAY_RESPONSE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result, responseManager,"response");

                            //Log.d("TEST","request - "+result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect),"");
        }
    }

    private void paymentRequest(String request) {
        if (Utility.isNetworkConnected(this)) {
            progressHelper.show();



            QueryManager.getInstance().postRequest(this,
                    Constant.METHOD_ADDFUNDGATEWAY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result, responseManager,"");

                            //Log.d("TEST","request - "+result);
                        }
                    });
        } else {
            Utility.showToast(this, getString(R.string.internet_connect),"");
        }
    }

    private void parseResponse(String result, ResponseManager responseManager,String type) {
        if (!isDestroyed()) {
            progressHelper.dismiss();

            if (Utility.isServerRespond(result/*, responseManager*/)) {


                //Log.d("TEST","output  responseed "+result);
                try{
                    GetAddFundResponse response = new Gson().fromJson(result, GetAddFundResponse.class);

                    if (response.getResCode().equals(Constant.CODE_200)) {
                        if(type.equals("response")){
                            //Log.d("Response ","received ");
                            Utility.showToastLatest(this,
                                    response.getResText(),"SUCCESS");
                        }
                        else {

                            //Log.d("Received ", checkSum);

                            PaytmPGService service = PaytmPGService.getProductionService();
                            HashMap<String, String> params = new HashMap<>();
                            params.put("MID", response.getData().getMid());
                            params.put("ORDER_ID", response.getData().getOrderId());
                            params.put("CUST_ID", response.getData().getCusId());
                            params.put("CHANNEL_ID", response.getData().getChannelId());
                            params.put("TXN_AMOUNT", response.getData().getAmount());
                            params.put("WEBSITE", response.getData().getWebsite());

                            params.put("CALLBACK_URL", response.getData().getCbUrl());

                            params.put("INDUSTRY_TYPE_ID", response.getData().getIndType());
                            params.put("CHECKSUMHASH", response.getData().getCheckSum());

                            PaytmOrder order = new PaytmOrder(params);

                            service.initialize(order, null);
                            service.startPaymentTransaction(checkSum.this, true, true,
                                    checkSum.this);
                        }






                    }

                    else {
                        Utility.showToastLatest(this,
                                response.getResText(),"ERROR");
                    }
                }
                catch (Exception e){
                    //Log.d("TEST","output  ex "+e.toString());

                    Utility.showToast(this, "Data not received "+e.toString(),"");
                }

            } else {
                //Utility.showToast(this, getString(R.string.server_not_responding),"");
                startActivity(new Intent(this, ServerNotResponseActivity.class));
            }




        }
    }


    @Override

    public void onTransactionResponse(Bundle bundle){
        Utility.showToastLatest(this,"Your payment is successfull","SUCCESS");
        response=bundle.toString();
        try {
            byte[] dataRes = response.getBytes("UTF-8");
            response = Base64.encodeToString(dataRes, Base64.DEFAULT);
           // Log.d("response encode  ",response);
        }
        catch (Exception e){
            //Log.d("Error encode ",e.toString());
        }
        init("response");


        //Log.d("Bundle is ",bundle.toString());


    }

    @Override
    public void networkNotAvailable(){
        Utility.showToastLatest(this,"Network not available","FAILED");
    }

    @Override
    public void clientAuthenticationFailed(String msg){
        Utility.showToastLatest(this,"Authentication failed from server","FAILED");
    }

    @Override
    public void someUIErrorOccurred(String msg){
        Utility.showToastLatest(this,"UI error occurred","FAILED");
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s,String s1){
        Utility.showToastLatest(this,"Error on loading web page","FAILED");
    }

    @Override
    public void onBackPressedCancelTransaction(){
        Utility.showToastLatest(this,"Back button pressed , Transaction cancelled ","FAILED");
    }

    @Override
    public void onTransactionCancel(String s,Bundle bundle){
        Utility.showToastLatest(this,"Transaction Cancelled","FAILED");
    }





}
