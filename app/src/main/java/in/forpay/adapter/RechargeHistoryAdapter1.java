package in.forpay.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import in.forpay.R;
import in.forpay.activity.RechargeDetailsActivity;
import in.forpay.database.DatabaseHelper;
import in.forpay.model.request.RechargeHistory;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class RechargeHistoryAdapter1 extends RecyclerView.Adapter<RechargeHistoryAdapter1.ViewHolder> {

    private Activity mActivity;
    private ArrayList<RechargeHistory> mList = new ArrayList<>();
    private ProgressHelper progressHelper;
    private DatabaseHelper databaseHelper;
    private String misShop="";

    public RechargeHistoryAdapter1(Activity activity, ArrayList<RechargeHistory> list,String isShop) {
        this.mActivity = activity;
        this.mList = list;
        this.progressHelper = new ProgressHelper(mActivity);
        this.misShop=isShop;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recharge_history_item1, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        String status = mList.get(position).getStatus();

        String operatorId = mList.get(position).getOperatorId();

        viewHolder.orderStatus.setText(status);
        switch (status) {
            case "FAILED":
               //viewHolder.orderStatus.setText("Your order is failed");
                viewHolder.orderStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.red_light));

                break;
            case "PENDING":

                //viewHolder.orderStatus.setText("Your order is pending");

                viewHolder.orderStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.yellow_light));

                break;
            case "SUCCESS":
                //viewHolder.orderStatus.setText("Your order is successful");
                viewHolder.orderStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.green_light));

                break;
            case "DISPUTED":
                //viewHolder.orderStatus.setText("Your order is Disputed");
                viewHolder.orderStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.blue_light));

                break;
            default:
                //viewHolder.textViewRefund.setVisibility(View.GONE);
                //viewHolder.textViewReprocess.setVisibility(View.GONE);
                break;
        }

        /*viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, RechargeDetailsActivity.class);
                intent.putExtra("TransId", mList.get(position).getTransId());
                intent.putExtra("amount", mList.get(position).getAmount());
                intent.putExtra("mobile", mList.get(position).getMobile());
                intent.putExtra("reqTime", mList.get(position).getReqTime());
                intent.putExtra("orderId", mList.get(position).getOrderId());
                intent.putExtra("operatorName", mList.get(position).getService());
                intent.putExtra("operatorId",mList.get(position).getOperatorId());

                intent.putExtra("status", mList.get(position).getStatus());


                intent.putExtra("creditused", mList.get(position).getCreditUsed());
                intent.putExtra("cusCreditUsed",mList.get(position).getCusCreditUsed());
                intent.putExtra("marginamount", mList.get(position).getMarginAmount());

                intent.putExtra("beneName",mList.get(position).getBeneficiaryName());
                //Log.e("Account name"," Account  -"+mList.get(position).getBeneficiaryName());

                intent.putExtra("beneficiaryAccountNumber", mList.get(position).getBeneficiaryAccountNumber());

                intent.putExtra("viewType","rc");
                intent.putExtra("remark","");

                //mActivity.startActivity(intent);
            }
        });*/

        String dateTime = mList.get(position).getReqTime();
        viewHolder.textViewDatetime.setText(dateTime);

        String bankAccount = mList.get(position).getBeneficiaryAccountNumber();


        String orderId = mList.get(position).getOrderId();
        viewHolder.orderId.setText("Order Id : "+orderId);

        String mobile = mList.get(position).getMobile();

        String amount = mActivity.getString(R.string.rupees) + mList.get(position).getAmount();

        viewHolder.orderPrice.setText(amount);

        String operator = mList.get(position).getService();


        //viewHolder.orderDescription.setText("Transaction for "+operator+" on "+mobile);
        viewHolder.operator.setText(operator);
        viewHolder.mobile.setText(mobile);
        viewHolder.textViewAmount.setText(mList.get(position).getCreditUsed());
        viewHolder.transactionId.setText(mList.get(position).getTransId());

        Utility.imageLoader(mActivity,"https://api.forpay.in/image/operator/"+operatorId+".png",viewHolder.operatorIcon);
        /*
        if(isAssetExists("logo/"+operatorId+".png")){

            try
            {
                // get input stream
                InputStream ims = mActivity.getAssets().open("logo/"+operatorId+".png");
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
                // set image to ImageView
                viewHolder.operatorIcon.setImageDrawable(d);

                ims .close();
            }
            catch(Exception ex)
            {
                Glide.with(mActivity).load(R.drawable.mobile_1).into(viewHolder.operatorIcon);
            }
        }
        else{
            Glide.with(mActivity).load(R.drawable.mobile_1).into(viewHolder.operatorIcon);

        }

         */

        double profit = 0;
        double cusCreditUsed1,retCreditUsed=0;

        try {

                cusCreditUsed1 = NumberFormat.getInstance().parse(mList.get(position).getCusCreditUsed()).doubleValue();
                retCreditUsed = NumberFormat.getInstance().parse(mList.get(position).getCreditUsed()).doubleValue();
                profit = cusCreditUsed1 - retCreditUsed;

        } catch(Exception nfe) {
            Log.d("Error ",nfe.toString());
        }

        String marginAmount = mActivity.getString(R.string.rupee) + mList.get(position).getMarginAmount();

        DecimalFormat decimalProfit = new DecimalFormat("#.##");
        float twoDigitsF = Float.valueOf(decimalProfit.format(profit));
        if(mList.get(position).getStatus().equals("FAILED")){
            twoDigitsF=0;
        }

        //viewHolder.commissiontv.setText(twoDigitsF+"");



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout cardView;
        private TextView orderId;

        private TextView textViewCustomerCopy;
        private TextView textViewRetailerCopy,textViewDatetime,orderPrice,orderStatus,commissiontv,operator,mobile,textViewRefund
                ,textViewAmount,transactionId;

        public ImageView operatorIcon;


        ViewHolder(@NonNull View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            orderId = view.findViewById(R.id.orderId);

            textViewCustomerCopy=view.findViewById(R.id.textViewCustomerCopy);
            textViewRetailerCopy=view.findViewById(R.id.textViewRetailerCopy);
            textViewDatetime=view.findViewById(R.id.textViewDatetime);
            orderPrice=view.findViewById(R.id.orderPrice);
            orderStatus=view.findViewById(R.id.orderStatus);
            operatorIcon=view.findViewById(R.id.operatorIcon);
            commissiontv=view.findViewById(R.id.commissiontv);
            operator=view.findViewById(R.id.operator);
            mobile=view.findViewById(R.id.mobile);
            textViewRefund = view.findViewById(R.id.textViewRefund);
            textViewAmount = view.findViewById(R.id.textViewAmount);
            transactionId = view.findViewById(R.id.transactionId);


            textViewCustomerCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, RechargeDetailsActivity.class);
                    intent.putExtra("orderId", mList.get(getAdapterPosition()).getOrderId());
                    intent.putExtra("viewType","cc");
                    intent.putExtra("remark","");
                    mActivity.startActivity(intent);
                }
            });

            textViewRetailerCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mActivity, RechargeDetailsActivity.class);
                    intent.putExtra("orderId", mList.get(getAdapterPosition()).getOrderId());
                    intent.putExtra("viewType","rc");
                    intent.putExtra("remark","");
                    mActivity.startActivity(intent);

                }
            });

            textViewRefund.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utility.disputeOrder(mActivity,"2",mList.get(getAdapterPosition()).getOrderId());
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, RechargeDetailsActivity.class);
                    intent.putExtra("orderId", mList.get(getAdapterPosition()).getOrderId());
                    intent.putExtra("viewType","rc");
                    intent.putExtra("remark","");
                    mActivity.startActivity(intent);
                }
            });
        }
    }


    private boolean isAssetExists(String pathInAssetsDir) {
        AssetManager assetManager = mActivity.getAssets();
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
