package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.AdapterCouponOfferBinding;
import in.forpay.model.response.GetCashBackOfferResponse;
import in.forpay.model.response.GetMissCallRecharge;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class PrepaidCouponAdapter extends RecyclerView.Adapter<PrepaidCouponAdapter.PrepaidViewHolder> {

    private Activity mActivity;
    private ArrayList<GetCashBackOfferResponse.Data> mList = new ArrayList<>();
    ProgressHelper progressHelper;

    public PrepaidCouponAdapter(Activity mActivity, ArrayList<GetCashBackOfferResponse.Data> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
        progressHelper = new ProgressHelper(mActivity);
        //Log.d("prepaidcoupon","adapter 1 "+mList.size());
    }

    @NonNull
    @Override
    public PrepaidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_coupons, parent, false);
        //Log.d("prepaidcoupon","adapter 2 "+mList.size());
        return new PrepaidCouponAdapter.PrepaidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrepaidViewHolder holder, int position) {
        //Log.d("prepaidcoupon","adapter 3 "+mList.size());
        holder.text_View_Amount.setText("₹ " + mList.get(position).getAmount());
        holder.text_Coupon_name.setText(mList.get(position).getCoupon());
        holder.text_Validity.setText(mList.get(position).getExpireDate());
        holder.text_View_Detail.setText(mList.get(position).getDetails());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Coupon_ID", mList.get(position).getId());
                intent.putExtra("Coupon_Amt", mList.get(position).getAmount());
                intent.putExtra("Coupon_Name", mList.get(position).getCoupon());


                mActivity.setResult(Activity.RESULT_OK, intent);
                mActivity.finish();
//                Toast.makeText(mActivity.getApplicationContext(),mList.get(position).getId(),Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        //Log.d("prepaidcoupon","count "+mList.size());
        return mList.size();


    }

    public class PrepaidViewHolder extends RecyclerView.ViewHolder {

        TextView text_View_Amount, text_Validity, text_View_Detail, text_Coupon_name, convertToPoints;
        LinearLayout cardView;

        public PrepaidViewHolder(@NonNull View itemView) {
            super(itemView);
            convertToPoints=itemView.findViewById(R.id.convertToPoints);
            text_View_Amount = itemView.findViewById(R.id.text_View_Amount);
            text_Validity = itemView.findViewById(R.id.text_Validity);
            text_View_Detail = itemView.findViewById(R.id.text_View_Detail);
            text_Coupon_name = itemView.findViewById(R.id.text_Coupon_name);
            cardView = itemView.findViewById(R.id.cardView);

            convertToPoints.setOnClickListener(v->{
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_prepaid_coupon, null);
                bottomSheetView.findViewById(R.id.closeBtn).setOnClickListener(v1 -> bottomSheetDialog.dismiss());
                TextView btnRecharge = bottomSheetView.findViewById(R.id.btnRecharge);
                bottomSheetDialog.setContentView(bottomSheetView);


                TextView couponName = bottomSheetView.findViewById(R.id.couponName);
                TextView couponAmount = bottomSheetView.findViewById(R.id.couponAmount);
                TextView convertablePoints = bottomSheetView.findViewById(R.id.convertablePoints);

                float f1 = Float.parseFloat(mList.get(getAbsoluteAdapterPosition()).getAmount());
                double points=f1*0.90;

                int valuePoints = (int)points;

                couponName.setText("Coupon Name - "+mList.get(getAbsoluteAdapterPosition()).getCoupon());
                couponAmount.setText("Coupon Amount - "+mList.get(getAbsoluteAdapterPosition()).getAmount());
                convertablePoints.setText("You will get "+valuePoints+" Points");
                bottomSheetDialog.show();


                btnRecharge.setOnClickListener(v12 ->{
                    proceedNow(mList.get(getAbsoluteAdapterPosition()).getId(),valuePoints,bottomSheetView);

                });


            });


        }
    }

    private void proceedNow(String couponId,int points,View bottomSheetView) {


        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(mActivity)); // key
        map1.put("imei", Utility.getImei(mActivity)); // imei
        map1.put("versionCode", Utility.getVersionCode(mActivity)); // version code
        map1.put("os", Utility.getOs(mActivity));
        map1.put("couponId", couponId);
        map1.put("points", ""+points);
        String request = Utility.mapWrapper(mActivity, map1);

        if (Utility.isNetworkConnected(mActivity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(mActivity,
                    Constant.METHOD_COUPON_TO_POINTS, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse1(result);
                        }
                    });
        } else {
            Utility.showToast(mActivity, mActivity.getString(R.string.internet_connect), "");
        }
    }


    private void parseResponse1(String result) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetMissCallRecharge response = new Gson().fromJson(result, GetMissCallRecharge.class);

                Utility.showToastLatest(mActivity,response.getResText(),response.getResCode());

        }
    }
}
