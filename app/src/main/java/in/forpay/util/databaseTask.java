package in.forpay.util;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import in.forpay.database.DatabaseHelper;
import in.forpay.model.response.GetBusListResponse;
import in.forpay.model.response.GetLoginValidateResponse;

public class databaseTask extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;
    private Context mContext;

    private ProgressHelper mProgressHelper;
    private String mType;
    public databaseTask (Context context){
        mContext = context;
    }

    public databaseTask(Context context,ProgressHelper progressHelper,String type){
        mContext = context;
        mProgressHelper=progressHelper;
        mType=type;
    }

    protected void onPreExecute(){
        super.onPreExecute();
        if(mProgressHelper!=null) {
            mProgressHelper.show("Loading your full data now");
        }

    }

    @Override
    public String doInBackground(String... strings) {
        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(mContext);
        String result=strings[0];
        String type=strings[1];

        if(type.equals("login")){
            GetLoginValidateResponse response = new Gson().fromJson(result, GetLoginValidateResponse.class);
if(response.getResCode().equals("200")) {
    if (response.getData().getServiceList() != null
            && response.getData().getServiceList().size() > 0) {

        for (GetLoginValidateResponse.Service model : response.getData().getServiceList()) {
            databaseHelper.insertServiceType(model);

            //Log.d("Inserted service"," background");
        }

    }
}


            /*if (response.getData().getRechargeHistoryList() != null
                    && response.getData().getRechargeHistoryList().size() > 0) {

                for (RechargeHistory model : response.getData().getRechargeHistoryList()) {
                    databaseHelper.insertRechargeHistory(model);
                }
            }*/



        }

        else if(type.equals("rechargehistory")){

        }
        else if(type.equals("buslist")){
            databaseHelper.deleteTable("busList");
            databaseHelper.deleteTable(databaseHelper.TABLE_BUS_bpdpPoint);
            GetBusListResponse response = new Gson().fromJson(result, GetBusListResponse.class);
            if (response.getResCode().equals(Constant.CODE_200)) {
                int size=response.getData().getBusStopsList().size();
                for(int j=0;j<size;j++){
                    //SystemClock.sleep(1000);
                    String[] fares=response.getData().getBusStopsList().get(j).getFares();
                    String finalFare=fares[0];
                    //Log.d("fare is ",""+fares[0]);

                    String ac=response.getData().getBusStopsList().get(j).getAmenities().getAc();
                    String arrivalTime=response.getData().getBusStopsList().get(j).getArrivalTime();
                    databaseHelper.insertBusList(
                            response.getData().getBusStopsList().get(j).getAmenities().getAc(),
                            response.getData().getBusStopsList().get(j).getAmenities().getSleeper(),
                            response.getData().getBusStopsList().get(j).getAmenities().getPuchback(),
                            response.getData().getBusStopsList().get(j).getAmenities().getLiverTrackingAvailable(),
                            response.getData().getBusStopsList().get(j).getAmenities().getChargingPoint(),
                            response.getData().getBusStopsList().get(j).getArrivalTime(),
                            response.getData().getBusStopsList().get(j).getAvlSeats(),
                            response.getData().getBusStopsList().get(j).getAvlWindowsSeats(),
                            response.getData().getBusStopsList().get(j).getBusType(),
                            response.getData().getBusStopsList().get(j).getBusName(),
                            response.getData().getBusStopsList().get(j).getBookingId(),
                            response.getData().getBusStopsList().get(j).getCancellationPolicy(),
                            response.getData().getBusStopsList().get(j).getPartialCancellationAllowed(),
                            response.getData().getBusStopsList().get(j).getDepartureTime(),
                            response.getData().getBusStopsList().get(j).getTravelDuration(),
                            finalFare
                            );


                    //Log.d("Ac is ",""+ac+" bus time "+arrivalTime);

                }

            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(String file_url) {
        if(mProgressHelper!=null) {
            mProgressHelper.dismiss();
        }

    }

    public interface AsyncResponse {
        void processFinish(String output);
    }



}
