package in.forpay.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.databinding.ItemLoginHistoryBinding;
import in.forpay.model.LoginHistoryModel;
import in.forpay.model.response.GetLogoutResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class LoginHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<LoginHistoryModel> mList;
    private ProgressHelper progressHelper;

    public LoginHistoryAdapter(Activity activity, ArrayList<LoginHistoryModel> list) {
        this.activity = activity;
        this.mList = list;
        this.progressHelper = new ProgressHelper(activity);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(activity).inflate(R.layout.item_login_history, parent, false);
        return new LoginHistoryHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof LoginHistoryHolder){
            LoginHistoryModel usModel = (LoginHistoryModel)mList.get(position);
            LoginHistoryHolder loginHistoryHolder = (LoginHistoryHolder)holder;

            loginHistoryHolder.adContainerViewBinding.txtLastActive.setText(usModel.getActiveTime());
            loginHistoryHolder.adContainerViewBinding.txtImei.setText(usModel.getImei());
            loginHistoryHolder.adContainerViewBinding.txtLoginTime.setText(usModel.getLoginTime());
            if(usModel.getImei().equals(Utility.getDeviceIMEI(activity))){
                loginHistoryHolder.adContainerViewBinding.btnLogoutText.setText("Current Device");
                loginHistoryHolder.adContainerViewBinding.btnLogoutText.getBackground().setColorFilter(activity.getResources().getColor(R.color.green_new), PorterDuff.Mode.SRC_IN);
            }


                //

        }
    }

    public void filterList(ArrayList<LoginHistoryModel> filteredList) {
        mList = filteredList;
        notifyDataSetChanged();
    }

    private void sendLogoutRequest(String imei,final int position,String otp,Dialog dialog){


        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(activity)); // key
        map1.put("imei",Utility.getImei(activity)); // imei
        map1.put("versionCode",Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("logoutImei" , imei);
        map1.put("otp" , otp);
        map1.put("type",imei); // beneficiaryId

        String request = Utility.mapWrapper(activity,map1);



        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_LOGOUT, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result,imei,position,dialog);
                        }
                    });
        } else {
            Utility.showToastLatest(activity, activity.getString(R.string.internet_connect),"ERROR");
        }



    }

    private void parseResponse(String result,String imei, int position,Dialog dialog){
        try{
            progressHelper.dismiss();
        }
        catch (Exception e){

        }

        if(Utility.isServerRespond(result)){
            GetLogoutResponse response = new Gson().fromJson(result, GetLogoutResponse.class);
            if(response.getResCode().equals(Constant.CODE_201)){
                addOtpVerifyDialog(imei,position);
            }
            else if(response.getResCode().equals(Constant.CODE_200)){
                if(dialog!=null) {
                    mList.remove(position);
                    notifyItemRemoved(position);
                    dialog.dismiss();
                }
                Utility.showToastLatest(activity,response.getResText(),"SUCCESS");
            }
            else{
                Utility.showToastLatest(activity,response.getResText(),"ERROR");
            }

        }
        else {
            //Utility.showToast(activity, activity.getString(R.string.server_not_responding), "");
            activity.startActivity(new Intent(activity, ServerNotResponseActivity.class));
        }

    }

    private void addOtpVerifyDialog(String imei, int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        if(!dialog.isShowing()) {
            dialog.setContentView(R.layout.dialog_otp);
            if(dialog.getWindow()!=null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }

            EditText editTextOtp = dialog.findViewById(R.id.editTextOtp);


            TextView textViewVerifyNow = dialog.findViewById(R.id.textViewVerifyNow);

            textViewVerifyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String otp=editTextOtp.getText().toString();
                    if(otp.isEmpty()){
                        Utility.showToastLatest(activity,activity.getString(R.string.error_enter_otp),"ERROR");
                        return;
                    }
                    sendLogoutRequest(imei,position,otp,dialog);





                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class LoginHistoryHolder extends RecyclerView.ViewHolder {
        ItemLoginHistoryBinding adContainerViewBinding;

        LoginHistoryHolder(@NonNull View itemView) {
            super(itemView);
            adContainerViewBinding = ItemLoginHistoryBinding.bind(itemView);

            adContainerViewBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                String imeiClick=adContainerViewBinding.txtImei.getText().toString();
                if(imeiClick.equals(Utility.getDeviceIMEI(activity))){
                    Utility.showToastLatest(activity,"Can not logout from same device","ERROR");
                }
                else{
                    sendLogoutRequest(imeiClick,getAdapterPosition(),"",null);
                }
                }
            });
        }
    }


}
