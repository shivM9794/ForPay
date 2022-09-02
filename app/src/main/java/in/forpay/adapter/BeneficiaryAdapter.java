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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.RechargeProcessActivity;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.database.DatabaseHelper;
import in.forpay.model.request.RechargeHistory;
import in.forpay.model.response.GetCustomerHistoryResponse;
import in.forpay.model.response.GetDeleteBeneficiaryResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.model.response.GetSendFundResponse;
import in.forpay.model.response.GetVerifyDeleteBeneficiaryResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class BeneficiaryAdapter extends RecyclerView.Adapter<BeneficiaryAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<GetCustomerHistoryResponse.Beneficiary> mList = new ArrayList<>();
    private boolean isFlag;
    private ProgressHelper progressHelper;
    private String mMobile = "";
    private String operatorId = "";
    private String mobile="";
    private String amount="0";
    private String pin,otp,beneficiaryId="";

    public BeneficiaryAdapter(Activity activity, ArrayList<GetCustomerHistoryResponse.Beneficiary> list,
                              String mobile) {
        this.mActivity = activity;
        this.mList = list;
        this.progressHelper = new ProgressHelper(mActivity);
        this.mMobile = mobile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.beneficiary_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (position % 2 == 0) {
            viewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(mActivity,
                    R.color.light_gray));
        } else {
            viewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(mActivity,
                    R.color.white));
        }

        viewHolder.textViewId.setText(mList.get(position).getBeneficiaryId());
        viewHolder.textViewName.setText(mList.get(position).getBeneficiaryName());
        viewHolder.textViewMobile.setText(mList.get(position).getBeneficiaryMobileNumber());
        viewHolder.textViewAccountNumber.setText(mList.get(position).getBeneficiaryAccountNumber());
        viewHolder.textViewIFSCCode.setText(mList.get(position).getIfscCode());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView textViewId;
        private TextView textViewName;
        private TextView textViewMobile;
        private TextView textViewAccountNumber;
        private TextView textViewIFSCCode;

        public ViewHolder(@NonNull View view) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            textViewId = view.findViewById(R.id.textViewId);
            textViewName = view.findViewById(R.id.textViewName);
            textViewMobile = view.findViewById(R.id.textViewMobile);
            textViewAccountNumber = view.findViewById(R.id.textViewAccountNumber);
            textViewIFSCCode = view.findViewById(R.id.textViewIFSCCode);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendDeleteDialog(getAdapterPosition());
                }
            });
        }
    }

    /**
     * Show pop up for send money & delete beneficiary
     */
    private void sendDeleteDialog(final int position) {
        final BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_send_money_delete_beneficiary);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        isFlag = false;
        TextView textViewBeneficiaryId = dialog.findViewById(R.id.textViewBeneficiaryId);
        final EditText editViewAmount = dialog.findViewById(R.id.editViewAmount);
        final View viewLineAmount = dialog.findViewById(R.id.viewLineAmount);
        final EditText editViewPin = dialog.findViewById(R.id.editViewPin);
        final View viewLinePin = dialog.findViewById(R.id.viewLinePin);
        final EditText editViewOtp = dialog.findViewById(R.id.editViewOtp);
        final View viewLineOTP = dialog.findViewById(R.id.viewLineOTP);
        TextView textViewSendFund = dialog.findViewById(R.id.textViewSendFund);
        final TextView textViewLabel = dialog.findViewById(R.id.textViewLabel);
        TextView textViewDelete = dialog.findViewById(R.id.textViewDelete);
        textViewBeneficiaryId.setText(mList.get(position).getBeneficiaryId());
        textViewLabel.setText("Send Fund");


        textViewSendFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pin =editViewPin.getText().toString().trim();
                amount=editViewAmount.getText().toString().trim();
                otp=editViewOtp.getText().toString().trim();
                if (validation(amount, pin, otp)) {


                    mobile=mMobile;
                    beneficiaryId=mList.get(position).getBeneficiaryId();



                    Map<String,String> map1 = new HashMap<>();
                    map1.put("token",Utility.getToken(mActivity)); // key
                    map1.put("imei",Utility.getImei(mActivity)); // imei
                    map1.put("versionCode",Utility.getVersionCode(mActivity)); // version code
                    map1.put("os", Utility.getOs(mActivity));
                    map1.put("mobile", mMobile);
                    map1.put("operatorId","94"); // operatorId
                    map1.put("amount",amount);
                    map1.put("pin",pin); // pin
                    map1.put("beneficiaryId",beneficiaryId); // beneficiaryId



                    String request = Utility.mapWrapper(mActivity,map1);
                    sendFund(request, dialog);
                }
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = "test";
                String pin = "test";
                String otp = editViewOtp.getText().toString().trim();

                if(otp=="" || otp.isEmpty()==true){
                    isFlag=false;
                }
                else{
                    isFlag=true;
                }

                if (isFlag) {

                    if (validation(amount, pin, otp)) {



                        Map<String,String> map1 = new HashMap<>();

                        map1.put("token",Utility.getToken(mActivity)); // key
                        map1.put("imei",Utility.getImei(mActivity)); // imei
                        map1.put("versionCode",Utility.getVersionCode(mActivity)); // version code
                        map1.put("os", Utility.getOs(mActivity));
                        map1.put("mobile" , mMobile);
                        map1.put("otp" , otp);
                        map1.put("beneficiaryId",mList.get(position).getBeneficiaryId()); // beneficiaryId

                        String request = Utility.mapWrapper(mActivity,map1);


                        deleteVerify(request, dialog, position);
                    }

                } else {


                    Map<String,String> map1 = new HashMap<>();

                    map1.put("token",Utility.getToken(mActivity)); // key
                    map1.put("imei",Utility.getImei(mActivity)); // imei
                    map1.put("versionCode",Utility.getVersionCode(mActivity)); // version code
                    map1.put("os", Utility.getOs(mActivity));
                    map1.put("mobile" , mMobile);

                    map1.put("beneficiaryId",mList.get(position).getBeneficiaryId()); // beneficiaryId

                    String request = Utility.mapWrapper(mActivity,map1);




                    delete(request, new OtpListener() {
                        @Override
                        public void onOtp() {
                            isFlag = true;
                            editViewAmount.setVisibility(View.GONE);
                            viewLineAmount.setVisibility(View.GONE);
                            editViewPin.setVisibility(View.GONE);
                            viewLinePin.setVisibility(View.GONE);
                            editViewOtp.setVisibility(View.VISIBLE);
                            viewLineOTP.setVisibility(View.VISIBLE);
                            textViewLabel.setText("Delete Beneficiary");

                        }
                    });
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

    /**
     * Validation for all fields
     */
    private boolean validation(String amount, String pin, String otp) {

        if (TextUtils.isEmpty(amount)) {
            Utility.showToast(mActivity, "Please enter amount","");
            return false;
        } else if (TextUtils.isEmpty(pin)) {
            Utility.showToast(mActivity, "Please enter pin","");
            return false;
        } else if (TextUtils.isEmpty(otp) && isFlag) {
            Utility.showToast(mActivity, "Please enter otp","");
            return false;
        }
        return true;
    }

    /**
     * Send fund to beneficiary
     */
    private void sendFund(String request, final BottomSheetDialog dialog) {
        if (Utility.isNetworkConnected(mActivity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(mActivity,
                    Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseSendFundResponse(result, dialog);
                        }
                    });
        } else {
            Utility.showToast(mActivity, mActivity.getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */

    private void parseSendFundResponse(String result, BottomSheetDialog dialog) {
        try{
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

            if (response.getResCode().equals(Constant.CODE_200)) {

                Intent intent = new Intent(mActivity, RechargeProcessActivity.class);
                intent.putExtra("amount", amount);
                intent.putExtra("mobile", mobile);
                intent.putExtra("operatorId", "94");
                intent.putExtra("creditUsed", response.getData().getCreditUsed());
                intent.putExtra("bal", response.getData().getBal());
                intent.putExtra("service", response.getData().getBal());
                intent.putExtra("billAmount", response.getData().getBillAmount());
                intent.putExtra("billName", response.getData().getBillName());
                intent.putExtra("beneficiaryAccountNumber", response.getData().getBeneficiaryAccountNumber());
                intent.putExtra("beneficiaryName", response.getData().getBeneficiaryName());
                intent.putExtra("type", "moneytransfer");
                intent.putExtra("pin", pin);
                intent.putExtra("extraAmount", "");
                intent.putExtra("profit", response.getData().getProfit());
                intent.putExtra("customerPay", response.getData().getCustomerPay());
                intent.putExtra("serviceCharge", response.getData().getServiceCharge());
                intent.putExtra("beneficiaryId",beneficiaryId);
                intent.putExtra("starSelected",response.getData().getStarSelected());
                intent.putExtra("validateDetails",response.getData().getValidateDetails());

                try {
                    mActivity.startActivity(intent);
                } catch (NullPointerException e) {
                    Utility.showToastLatest(mActivity, e.toString(), "ERROR");
                } catch (Exception e) {
                    Utility.showToastLatest(mActivity, e.toString(), "ERROR");
                }


            } else {
                Utility.showToast(mActivity, response.getResText(), response.getResCode());
            }


        } else {
            //Utility.showToast(mActivity, mActivity.getString(R.string.server_not_responding), "");
            mActivity.startActivity(new Intent(mActivity, ServerNotResponseActivity.class));
        }
    }
        catch(Exception e){

        }
    }

    /**
     * Delete beneficiary
     */
    private void delete(String request, final OtpListener listener) {
        if (Utility.isNetworkConnected(mActivity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(mActivity,
                    Constant.METHOD_DELETE_BENEFICIARY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseDeleteResponse(result, listener);
                        }
                    });
        } else {
            Utility.showToast(mActivity, mActivity.getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseDeleteResponse(String result, OtpListener listener) {
        try {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetDeleteBeneficiaryResponse response = new Gson().fromJson(result,
                        GetDeleteBeneficiaryResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    Utility.showToast(mActivity, response.getResText(),response.getResCode());

                    listener.onOtp();
                } else {
                    Utility.showToast(mActivity, response.getResText(),response.getResCode());
                }
            } else {
                //Utility.showToast(mActivity, mActivity.getString(R.string.server_not_responding),"");
                mActivity.startActivity(new Intent(mActivity, ServerNotResponseActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete beneficiary verify by otp
     */
    private void deleteVerify(String request, final BottomSheetDialog dialog, final int position) {
        if (Utility.isNetworkConnected(mActivity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(mActivity,
                    Constant.METHOD_DELETE_BENEFICIARY_VERIFY, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseDeleteVerifyResponse(result, dialog, position);
                        }
                    });
        } else {
            Utility.showToast(mActivity, mActivity.getString(R.string.internet_connect),"");
        }
    }

    /**
     * Parse response
     */
    private void parseDeleteVerifyResponse(String result, BottomSheetDialog dialog, int position) {
        try {
            progressHelper.dismiss();
            if (Utility.isServerRespond(result)) {
                GetVerifyDeleteBeneficiaryResponse response = new Gson().fromJson(result,
                        GetVerifyDeleteBeneficiaryResponse.class);
                if (response.getResCode().equals(Constant.CODE_200)) {
                    Utility.showToast(mActivity, response.getResText(),response.getResCode());

                    //EditText editViewOtp2 = dialog.findViewById(R.id.editViewOtp);
                    //editViewOtp2.setText("");
                    //mList.get(position).getBeneficiaryId();
                    dialog.dismiss();
                    mList.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Utility.showToast(mActivity, response.getResText(),response.getResCode());
                }
            } else {
                //Utility.showToast(mActivity, mActivity.getString(R.string.server_not_responding),"");
                mActivity.startActivity(new Intent(mActivity, ServerNotResponseActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface OtpListener {
        void onOtp();
    }
}


