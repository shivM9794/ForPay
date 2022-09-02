package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;

import in.forpay.R;
import in.forpay.activity.RechargeDetailsActivity;
import in.forpay.databinding.LayoutRecentTransactionBinding;
import in.forpay.model.request.RechargeHistory;
import in.forpay.util.Utility;

public class RecentTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Object> arrayList;

    public RecentTransactionAdapter(Activity activity, ArrayList<Object> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(activity).inflate(R.layout.layout_recent_transaction, parent, false);
        return new TransactionHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof TransactionHolder){
            TransactionHolder holder1 = (TransactionHolder)holder;
            RechargeHistory history = (RechargeHistory)arrayList.get(position);
            String operatorId = history.getOperatorId();
            holder1.adContainerViewBinding.txtOrderId.setText(history.getOrderId());
            holder1.adContainerViewBinding.txtAmount.setText("Rs. "+history.getAmount());
            holder1.adContainerViewBinding.transactionAmount.setText(history.getMobile());
            holder1.adContainerViewBinding.transactionDate.setText(history.getReqTime());

            holder1.adContainerViewBinding.mainLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, RechargeDetailsActivity.class);
                    intent.putExtra("orderId", history.getOrderId());
                    intent.putExtra("viewType","cc");
                    intent.putExtra("remark","");
                    activity.startActivity(intent);
                }
            });


            String status = history.getStatus();
            String strProfit=history.getProfit();
            switch (status) {
                case "FAILED":
                    holder1.adContainerViewBinding.transactionStatus.setText("Failed");
                    holder1.adContainerViewBinding.transactionStatus.setTextColor(ContextCompat.getColor(activity, R.color.red_light));

                    break;
                case "PENDING":

                    holder1.adContainerViewBinding.transactionStatus.setText("Pending");

                    holder1.adContainerViewBinding.transactionStatus.setTextColor(ContextCompat.getColor(activity, R.color.yellow_light));

                    break;
                case "SUCCESS":
                    holder1.adContainerViewBinding.transactionStatus.setText("Success");
                    holder1.adContainerViewBinding.transactionStatus.setTextColor(ContextCompat.getColor(activity, R.color.green_light));

                    break;
                case "DISPUTED":
                    holder1.adContainerViewBinding.transactionStatus.setText("Disputed");
                    holder1.adContainerViewBinding.transactionStatus.setTextColor(ContextCompat.getColor(activity, R.color.blue_light));

                    break;
                default:
                    //viewHolder.textViewRefund.setVisibility(View.GONE);
                    //viewHolder.textViewReprocess.setVisibility(View.GONE);
                    break;
            }

            //double profit = 0;
            double cusCreditUsed1,retCreditUsed=0;

            try {

                cusCreditUsed1 = NumberFormat.getInstance().parse(history.getCusCreditUsed()).doubleValue();
                retCreditUsed = NumberFormat.getInstance().parse(history.getCreditUsed()).doubleValue();
                //profit = cusCreditUsed1 - retCreditUsed;

            } catch(Exception nfe) {
                Log.d("Error ",nfe.toString());
            }

            String marginAmount = activity.getString(R.string.rupee) + history.getMarginAmount();
            //float twoDigitsF=0;
            try {
                //DecimalFormat decimalProfit = new DecimalFormat("#.##");
                //twoDigitsF = Float.valueOf(decimalProfit.format(profit));
            }
            catch (Exception e){

            }

            if(history.getStatus().equals("FAILED")){
                //twoDigitsF=0;
            }
            holder1.adContainerViewBinding.profitTime.setText("Profit "+strProfit);

            Utility.imageLoader(activity,"https://api.forpay.in/image/operator/"+operatorId+".png",holder1.adContainerViewBinding.recentIcon);
            /*
            if(isAssetExists("logo/"+operatorId+".png")){

                try
                {
                    // get input stream
                    InputStream ims = activity.getAssets().open("logo/"+operatorId+".png");
                    // load image as Drawable
                    Drawable d = Drawable.createFromStream(ims, null);
                    // set image to ImageView
                    holder1.adContainerViewBinding.recentIcon.setImageDrawable(d);

                    ims .close();
                }
                catch(Exception ex)
                {
                    Glide.with(activity).load(R.drawable.mobile_1).into(holder1.adContainerViewBinding.recentIcon);
                }
            }
            else{
                Glide.with(activity).load(R.drawable.mobile_1).into(holder1.adContainerViewBinding.recentIcon);
            }

             */
        }




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class TransactionHolder extends RecyclerView.ViewHolder {
        LayoutRecentTransactionBinding adContainerViewBinding;

        public TransactionHolder(@NonNull View itemView) {
            super(itemView);
            adContainerViewBinding = LayoutRecentTransactionBinding.bind(itemView);

            itemView.setOnClickListener(v -> {
                RechargeHistory history = (RechargeHistory)arrayList.get(getAdapterPosition());
                Intent intent = new Intent(activity, RechargeDetailsActivity.class);
                intent.putExtra("TransId", history.getTransId());
                intent.putExtra("amount", history.getAmount());
                intent.putExtra("mobile", history.getMobile());
                intent.putExtra("reqTime", history.getReqTime());
                intent.putExtra("orderId", history.getOrderId());
                intent.putExtra("operatorName", history.getService());
                intent.putExtra("operatorId",history.getOperatorId());

                intent.putExtra("status", history.getStatus());


                intent.putExtra("creditused", history.getCreditUsed());
                intent.putExtra("cusCreditUsed",history.getCusCreditUsed());
                intent.putExtra("marginamount", history.getMarginAmount());

                intent.putExtra("beneName",history.getBeneficiaryName());
                //Log.e("Account name"," Account  -"+history.getBeneficiaryName());

                intent.putExtra("beneficiaryAccountNumber", history.getBeneficiaryAccountNumber());

                intent.putExtra("viewType","rc");
                intent.putExtra("remark","");

                //mActivity.startActivity(intent);
            });
        }
    }

    private boolean isAssetExists(String pathInAssetsDir) {
        AssetManager assetManager = activity.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(pathInAssetsDir);
            if (null != inputStream) {
                return true;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return false;
    }

}
