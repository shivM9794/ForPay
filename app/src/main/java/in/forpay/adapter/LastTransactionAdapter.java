package in.forpay.adapter;

import android.app.Activity;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

import in.forpay.R;
import in.forpay.databinding.LayoutLastTransactionBinding;
import in.forpay.model.request.RechargeHistory;
import in.forpay.util.Utility;

public class LastTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Object> arrayList;
    private AutoCompleteTextView mobileNo;
    private TextView operatorData;
    private TextView circleData;
    private EditText amountData;


    public LastTransactionAdapter(Activity activity, ArrayList<Object> arrayList,AutoCompleteTextView mobileNo,TextView operatorData,TextView circleData,EditText amountData) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.mobileNo = mobileNo;
        this.operatorData = operatorData;
        this.circleData = circleData;
        this.amountData = amountData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(activity).inflate(R.layout.layout_last_transaction, parent, false);
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
            holder1.adContainerViewBinding.txtDate.setText(history.getReqTime());

            String status = history.getStatus();
            switch (status) {
                case "FAILED":
                    holder1.adContainerViewBinding.transactionStatus.setText("Your Order is Failed");
                    holder1.adContainerViewBinding.transactionStatus.setTextColor(ContextCompat.getColor(activity, R.color.red_light));

                    break;
                case "PENDING":

                    holder1.adContainerViewBinding.transactionStatus.setText("Your Order is Pending");

                    holder1.adContainerViewBinding.transactionStatus.setTextColor(ContextCompat.getColor(activity, R.color.yellow_light));

                    break;
                case "SUCCESS":
                    holder1.adContainerViewBinding.transactionStatus.setText("Your Order is Success");
                    holder1.adContainerViewBinding.transactionStatus.setTextColor(ContextCompat.getColor(activity, R.color.green_light));

                    break;
                case "DISPUTED":
                    holder1.adContainerViewBinding.transactionStatus.setText("Your Order is Disputed");
                    holder1.adContainerViewBinding.transactionStatus.setTextColor(ContextCompat.getColor(activity, R.color.blue_light));

                    break;
                default:
                    //viewHolder.textViewRefund.setVisibility(View.GONE);
                    //viewHolder.textViewReprocess.setVisibility(View.GONE);
                    break;
            }

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
        LayoutLastTransactionBinding adContainerViewBinding;

        public TransactionHolder(@NonNull View itemView) {
            super(itemView);
            adContainerViewBinding = LayoutLastTransactionBinding.bind(itemView);

            adContainerViewBinding.btnRepeat.setOnClickListener(v -> {
                RechargeHistory history = (RechargeHistory)arrayList.get(getAdapterPosition());
                String operatedId = history.getOperatorId();
                String[] arr = operatedId.split(" ");
                if(operatedId.contains("Dth")){
                    mobileNo.setText(history.getMobile());
                    operatorData.setText(history.getOperatorId());
                    amountData.setText(history.getAmount());
                }else if(operatedId.contains("Postpaid")){
                    mobileNo.setText(history.getMobile());
                    operatorData.setText(history.getOperatorId());
                    amountData.setText(history.getAmount());
                }else if(operatedId.contains("Metro")){
                    mobileNo.setText(history.getMobile());
                    operatorData.setText(history.getOperatorId());
                    amountData.setText(history.getAmount());
                }else {
                    mobileNo.setText(history.getMobile());
                    operatorData.setText(history.getOperatorId());
                    amountData.setText(history.getAmount());


                }



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
