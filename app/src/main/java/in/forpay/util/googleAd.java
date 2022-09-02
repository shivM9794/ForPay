package in.forpay.util;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;

import in.forpay.model.balance.MainRechargeModel;


public class googleAd  {
    private Context mContext;
    private AdView mAdView;

    String serviceListLocation = "serviceList_HomeUpdates" + Constant.METHOD_HOME_UPDATE;
    public googleAd (Context context , AdView adView){
        mContext = context;
        mAdView=adView;
    }

    public AdRequest getAd(){


        OxyMePref oxyMePref = new OxyMePref(mContext);
        MainRechargeModel model = new Gson().fromJson(oxyMePref.getString(serviceListLocation), MainRechargeModel.class);

        if(model.getShowGoogleAd().equals("yes")){
            mAdView = new AdView(mContext);
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId("ca-app-pub-4249580117963409/8486575784");
            MobileAds.initialize(mContext, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            AdRequest adRequest = new AdRequest.Builder().build();
            return adRequest;

        }
        else {

            return null;
        }

    }

}
