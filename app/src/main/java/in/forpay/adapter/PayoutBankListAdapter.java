package in.forpay.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.LayoutBankDetailsListBinding;
import in.forpay.model.GetPayoutBankListModel;
import in.forpay.model.response.GetAddPayoutResponse;
import in.forpay.model.response.GetRechargeValidateResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class PayoutBankListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;

    private boolean isFlag;
    private ArrayList<GetPayoutBankListModel> list;
    ProgressHelper progressHelper;
    String pin = "";
    String amount = "";
    String defaultSelectedMode = "";
    String paymentMethod="";

    public PayoutBankListAdapter(Activity activity, ArrayList<GetPayoutBankListModel> list, ProgressHelper progressHelper,String paymentMethod) {
        this.activity = activity;
        this.list = list;
        this.progressHelper = progressHelper;
        this.paymentMethod=paymentMethod;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(activity).inflate(R.layout.layout_bank_details_list, parent, false);
        return new PayoutBankListHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof PayoutBankListHolder) {
            GetPayoutBankListModel usModel = (GetPayoutBankListModel) list.get(position);
            PayoutBankListHolder payoutBankListHolder = (PayoutBankListHolder) holder;
            payoutBankListHolder.adContainerViewBinding.accountName.setText(usModel.getName());
            payoutBankListHolder.adContainerViewBinding.ifscCode.setText(usModel.getIfscCode());
            payoutBankListHolder.adContainerViewBinding.accountNumber.setText(usModel.getAccountNumber());

            payoutBankListHolder.adContainerViewBinding.deleteButton.setOnClickListener(v -> {

                delete(position);
            });

            if(usModel.getStatus().equals("1")){
                payoutBankListHolder.adContainerViewBinding.textViewPayout.setVisibility(View.VISIBLE);
            }
            else{
                payoutBankListHolder.adContainerViewBinding.textViewPayout.setVisibility(View.GONE);
            }

            payoutBankListHolder.adContainerViewBinding.textViewPayout.setOnClickListener(v -> {


                final BottomSheetDialog dialog = new BottomSheetDialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.activity_bottom_sheet_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                final TextView payoutTitle=dialog.findViewById(R.id.payoutTitle);
                payoutTitle.setText("Payout");

                if(paymentMethod.equals("yes")) {
                    final TextView helptext = dialog.findViewById(R.id.helptext);
                    helptext.setVisibility(View.VISIBLE);

                    final TabLayout modtabLayout = dialog.findViewById(R.id.modtab_layout);
                    modtabLayout.setVisibility(View.VISIBLE);

                    final CardView modeTabView=dialog.findViewById(R.id.modeTabView);
                    modeTabView.setVisibility(View.VISIBLE);
                    final HashMap<String, String> modeArray = new HashMap<String, String>();

                    modtabLayout.removeAllTabs();
                    String key = "IMPS";
                    modeArray.put(key, "mode");
                    modtabLayout.addTab(modtabLayout.newTab().setText(key));
                    String key2 = "NEFT";
                    modeArray.put(key2, "mode");
                    modtabLayout.addTab(modtabLayout.newTab().setText(key2));


                    TabLayout.Tab tab = modtabLayout.getTabAt(0); // Count Starts From 0
                    tab.select();
                    helptext.setText("Get Payment Within 5 seconds . Payout charge 7 Rs");
                    defaultSelectedMode = "IMPS";
                    modtabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {

                            defaultSelectedMode = tab.getText().toString();

                            if (defaultSelectedMode.equals("IMPS")) {
                                helptext.setText("Get Payment Within 5 seconds . Payout charge 7 Rs");
                            } else if (defaultSelectedMode.equals("NEFT")) {
                                helptext.setText("It may take upto 3 hr . Payout charge 5 Rs");
                            }

                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
                }
                final EditText editTextAccountNumber = dialog.findViewById(R.id.editTextAccountNumber);
                final EditText editTextIfscCode = dialog.findViewById(R.id.editTextIfscCode);
                final EditText editTextAccountName = dialog.findViewById(R.id.editTextAccountName);
                final TextInputLayout LayoutPin = dialog.findViewById(R.id.layoutPin);
                final TextInputLayout LayoutAmount = dialog.findViewById(R.id.layoutAmount);
                final Button submitBtn = dialog.findViewById(R.id.submitBtn);
                final EditText editTextAmount = dialog.findViewById(R.id.editTextAmount);
                final EditText editTextPin = dialog.findViewById(R.id.editTextPin);
                final ImageView closeBtn = dialog.findViewById(R.id.closeBtn);


                LayoutAmount.setVisibility(View.VISIBLE);
                LayoutPin.setVisibility(View.VISIBLE);
                editTextAccountNumber.setText(usModel.getAccountNumber());
                editTextAccountName.setText(usModel.getName());
                editTextIfscCode.setText(usModel.getIfscCode());
                editTextAccountName.setEnabled(false);
                editTextAccountNumber.setEnabled(false);
                editTextIfscCode.setEnabled(false);


                isFlag = false;

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        isFlag = false;
                    }
                });

                closeBtn.setOnClickListener(v1 -> {
                    isFlag = false;
                    dialog.dismiss();
                });

                submitBtn.setOnClickListener(v1 -> {

                    amount = editTextAmount.getText().toString();
                    pin = editTextPin.getText().toString();


                    Map<String, String> map1 = new HashMap<>();
                    map1.put("token", Utility.getToken(activity)); // key
                    map1.put("imei", Utility.getImei(activity)); // imei
                    map1.put("versionCode", Utility.getVersionCode(activity)); // version code
                    map1.put("os", Utility.getOs(activity));
                    map1.put("operatorId", "250");
                    map1.put("pin", pin);
                    map1.put("amount", amount);
                    map1.put("accountNumber", usModel.getAccountNumber());
                    map1.put("ifscCode", usModel.getIfscCode());
                    map1.put("mode",defaultSelectedMode);


                    String request = Utility.mapWrapper(activity, map1);
                    processPayout(request);


                });


                dialog.show();


            });


        }

    }


    public class PayoutBankListHolder extends RecyclerView.ViewHolder {

        LayoutBankDetailsListBinding adContainerViewBinding;

        public LinearLayout viewBackground;
        public LinearLayout viewForeground;


        private TextView bankName, accountNumber, ifscCode;

        public PayoutBankListHolder(@NonNull View itemView) {
            super(itemView);

            adContainerViewBinding = LayoutBankDetailsListBinding.bind(itemView);
            viewBackground = itemView.findViewById(R.id.background);
            viewForeground = itemView.findViewById(R.id.foreground);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private void processPayout(String request) {


        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_VALIDATE_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponseRecharge(result);
                        }
                    });
        } else {
            Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
        }

    }


    private void parseResponseRecharge(String result) {

        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetRechargeValidateResponse response = new Gson().fromJson(result, GetRechargeValidateResponse.class);

            if (response.getResCode().equals(Constant.CODE_200)) {

                Bundle bundle = new Bundle();
                bundle.putString("outputJson", response.getData().getOutputJson());
                bundle.putString("uniqId", response.getData().getUniqId());
                bundle.putString("serviceType", response.getData().getType());
                bundle.putString("offerDetails", "");
                bundle.putString("pin", pin);
                bundle.putString("amount", amount);
                bundle.putString("selectedMode", "");
                bundle.putString("operatorId", response.getData().getOperatorId());
                bundle.putString("coupon", "");
                bundle.putString("couponId", "");
                bundle.putString("extraData", response.getData().getExtraData());
                bundle.putParcelable(Constant.INSUFFICIENTDATA, response.getData().getInsufficientData());
                bundle.putParcelable(Constant.POPUPDATA, response.getData().getPopupData());


                Utility.openCheckOutActivity(activity, response.getData().getType(), bundle);

            } else {
                Utility.showToast(activity, response.getResText(), response.getResCode());
            }


        } else {
            Utility.showToast(activity, activity.getString(R.string.server_not_responding), "");
        }

    }


    private void deleteBank(String accountNumber, String ifscCode, int position) {


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("accountNumber", accountNumber);
        map1.put("ifscCode", ifscCode);

        String request = Utility.mapWrapper(activity, map1);

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_PAYOUT_BANK_DELETE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result, position);

                        }
                    });
        } else {
            Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
        }
    }

    private void parseResponse(String result, int position) {
        progressHelper.dismiss();
        GetAddPayoutResponse response = new Gson().fromJson(result,
                GetAddPayoutResponse.class);
        if (response.getResCode().equals(Constant.CODE_200)) {


            list.remove(position);
            notifyItemRemoved(position);
        } else {

        }
        Utility.showToastLatest(activity, response.getResText(), response.getResCode());
    }

    private void delete(int position) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Delete Contact");
        alertDialog.setMessage("Are you sure you want to delete.");

        alertDialog.setPositiveButton("DELETE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            deleteBank(list.get(position).getAccountNumber(), list.get(position).getIfscCode(), position);
                        }
                        catch (Exception e){
                            Utility.showToastLatest(activity,"Please try again","ERROR");
                        }

                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        notifyDataSetChanged();
                    }
                });

        alertDialog.show();

    }

    public void removeItem(int position) {

        delete(position);
    }


}

