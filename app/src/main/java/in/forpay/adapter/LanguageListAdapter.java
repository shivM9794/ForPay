package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.HomeActivity;
import in.forpay.activity.HomeNewActivity;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.model.request.LanguageList;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class LanguageListAdapter extends RecyclerView.Adapter<LanguageListAdapter.ViewHolder> {
    private ProgressHelper progressHelper;
    private Activity mActivity;
    private ArrayList<LanguageList> mList = new ArrayList<>();
    private Boolean isSendToServer=false;

    public LanguageListAdapter(Activity activity, ArrayList<LanguageList> list,Boolean sendToServer) {
        this.mActivity = activity;
        this.mList = list;
        this.isSendToServer=sendToServer;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_choose_language, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.tv_name.setText(mList.get(position).language);

        viewHolder.tv_name_en.setText(mList.get(position).languageLocal);
        viewHolder.iv_icon.setImageResource(0);

        if(Utility.getDefaultLanguage(mActivity).equals(mList.get(position).getCode())){
            //Log.d("Language is ",Utility.getDefaultLanguage(mActivity)+" - "+mList.get(position).getCode());
            viewHolder.iv_icon.setImageResource(R.drawable.ic_check_done);
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name,tv_name_en;
        private ImageView iv_icon;


        ViewHolder(@NonNull View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_name_en = view.findViewById(R.id.tv_name_en);
            iv_icon=view.findViewById(R.id.iv_icon);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.setDefaultLanguage(mActivity,mList.get(getAdapterPosition()).getCode());
                    if(isSendToServer) {
                        updateServerLanguage(mList.get(getAdapterPosition()).getCode());

                        Utility.getServiceList(mActivity, "HomeUpdates", Constant.METHOD_HOME_UPDATE, true, "HomeActivity", new HomeUpdateListener() {
                            @Override
                            public void onDone() {
                                //Intent intent = new Intent(mActivity, HomeNewActivity.class);
                                //intent.putExtra("fromActivity","languageChange");
                                //mActivity.startActivity(intent);
                               // mActivity.finish();

                                Utility.showToastLatest(mActivity,"Language Changed , Close App and Start Again","SUCCESS");
                                mActivity.finish();
                            }
                        });



                    }
                    else{
                        Intent returnIntent = new Intent();

                        mActivity.setResult(Activity.RESULT_OK,returnIntent);
                        mActivity.finish();
                    }




                }
            });
        }
    }

    private void updateServerLanguage(String code){
        progressHelper = new ProgressHelper(mActivity);

        Map<String,String> map1 = new HashMap<>();

        map1.put("token",Utility.getToken(mActivity)); // key
        map1.put("imei",Utility.getImei(mActivity)); // imei
        map1.put("versionCode",Utility.getVersionCode(mActivity)); // version code
        map1.put("os", Utility.getOs(mActivity));
        map1.put("code" , code);

        String request = Utility.mapWrapper(mActivity,map1);



        if (Utility.isNetworkConnected(mActivity)) {
            try {
                progressHelper.show();
            }
            catch (Exception e){

            }
            QueryManager.getInstance().postRequest(mActivity,
                    Constant.METHOD_UPDATE_LANGUAGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            try{
                                progressHelper.dismiss();
                            }
                            catch (Exception e2){

                            }


                        }
                    });
        }

    }

}
