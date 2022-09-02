package in.forpay.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.model.response.GetStockHistoryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class StockHistoryAdapter extends RecyclerView.Adapter<StockHistoryAdapter.ViewHolder> {

    private Activity mActivity;
    private boolean isFlag;
    private ArrayList<GetStockHistoryResponse.Data> mList = new ArrayList<>();
    private ProgressHelper progressHelper;
    String toUser="";


    public StockHistoryAdapter(Activity activity, ArrayList<GetStockHistoryResponse.Data> list) {
        this.mActivity = activity;
        this.mList = list;
        this.progressHelper = new ProgressHelper(mActivity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stock_history_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        viewHolder.textViewDate.setText(mList.get(position).getDate());

        toUser =  mList.get(position).getToUser();

        viewHolder.textViewToUser.setText(toUser);


        String status =  mList.get(position).getStatus();
        viewHolder.textViewStatus.setText(status);

        switch (status){
            case "DUE":
              //  viewHolder.viewLine.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.red_light));
                viewHolder.textViewStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.red_light));
                break;
            case "PARTIALLY DUE":

             //   viewHolder.viewLine.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.blue_light));
                viewHolder.textViewStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.blue_light));
                break;
            case "PAID":

             //   viewHolder.viewLine.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.green_light));
                viewHolder.textViewStatus.setTextColor(ContextCompat.getColor(mActivity, R.color.green_light));
                break;

            default:

                break;
        }

        viewHolder.textViewType.setText(mList.get(position).getType());

        viewHolder.textViewAmount.setText(mList.get(position).getAmount());
        viewHolder.textViewPaidAmount.setText(mList.get(position).getPaidAmount());
        viewHolder.textViewOrderId.setText(mList.get(position).getOrderId());

        viewHolder.textViewFromUser.setText(mList.get(position).getFromUser());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewDate;
        private TextView textViewToUser;
        private TextView textViewFromUser;
        private TextView textViewStatus;
        private TextView textViewType;
        private TextView textViewAmount;
        private TextView textViewPaidAmount;
        private TextView textViewOrderId;


        ViewHolder(@NonNull View view) {
            super(view);
            textViewDate = view.findViewById(R.id.textViewDate);
            textViewToUser = view.findViewById(R.id.textViewToUser);
            textViewFromUser = view.findViewById(R.id.textViewFromUser);
            textViewStatus = view.findViewById(R.id.textViewStatus);
            textViewType = view.findViewById(R.id.textViewType);
            textViewAmount = view.findViewById(R.id.textViewAmount);
            textViewPaidAmount = view.findViewById(R.id.textViewPaidAmount);
            textViewOrderId = view.findViewById(R.id.textViewOrderId);


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!Utility.getUsername(mActivity).equalsIgnoreCase(toUser)) {
                            updateStockDialog(getAdapterPosition());
                        }
                    }
                });


        }
    }


    /*

    Show pop up for stock update

     */

    private void updateStockDialog(final int position){

        final BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_update_stock);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        isFlag = false;

        TextView textViewStockId= dialog.findViewById(R.id.textViewStockId);
        TextView textViewUpdateStock = dialog.findViewById(R.id.textViewUpdateStock);

        final EditText editViewPaidAmount=dialog.findViewById(R.id.editViewPaidAmount);
        final EditText editViewPin = dialog.findViewById(R.id.editViewPin);
        textViewStockId.setText(mList.get(position).getOrderId());
        editViewPaidAmount.setText(mList.get(position).getPaidAmount());

        textViewUpdateStock.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String paidAmount = editViewPaidAmount.getText().toString().trim();
                String pin = editViewPin.getText().toString().trim();


                if (validation(paidAmount, pin)) {


                    Map<String,String> map1 = new HashMap<>();

                    map1.put("token",Utility.getToken(mActivity)); // key
                    map1.put("imei",Utility.getImei(mActivity)); // imei
                    map1.put("versionCode",Utility.getVersionCode(mActivity)); // version code
                    map1.put("os", Utility.getOs(mActivity));
                    map1.put("orderId" , mList.get(position).getOrderId());
                    map1.put("paidAmount" , paidAmount);
                    map1.put("pin",pin); // beneficiaryId
                    String request = Utility.mapWrapper(mActivity,map1);
                    updateStock(request, dialog);

                }

            }
        });


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                isFlag = false;
            }
        });

        dialog.show();






    }


    private void updateStock(String request, final BottomSheetDialog dialog) {
        if (Utility.isNetworkConnected(mActivity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(mActivity,
                    Constant.METHOD_STOCK_UPDATE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseUpdateStockResponse(result, dialog);
                        }
                    });
        } else {
            Utility.showToast(mActivity, mActivity.getString(R.string.internet_connect),"");
        }
    }


    private void parseUpdateStockResponse(String result, BottomSheetDialog dialog) {
        try {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                JSONObject response = new JSONObject(result);

                 if (response.getString("resCode").equals(Constant.CODE_200)) {

                    Utility.showToast(mActivity, response.getString("resText"),response.getString("resCode"));
                    dialog.dismiss();

                } else {
                     //Toast.makeText(mActivity,response.getString("resText"),Toast.LENGTH_LONG).show();
                     //Log.d("HomeActivity","result "+result+" resText "+response.getString("resText"));

                     Utility.showToast(mActivity, response.getString("resText"),response.getString("resCode"));
                     //Log.d("HomeActivity","result 1"+result+" resText "+response.getString("resText"));

                 }
            } else {
                //Utility.showToast(mActivity, mActivity.getString(R.string.server_not_responding),"");
                mActivity.startActivity(new Intent(mActivity, ServerNotResponseActivity.class));
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    private boolean validation(String amount, String pin) {

        if (TextUtils.isEmpty(amount)) {
            Utility.showToast(mActivity, "Please enter amount","");
            return false;
        } else if (TextUtils.isEmpty(pin)) {
            Utility.showToast(mActivity, "Please enter pin","");
            return false;
        }
        return true;
    }

}
