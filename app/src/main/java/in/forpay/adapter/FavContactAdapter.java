package in.forpay.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.forpay.R;
import in.forpay.databinding.LayoutFavContactBinding;
import in.forpay.model.FavContactModel;
import in.forpay.model.response.GetFavContactDetailResponse;
import in.forpay.model.response.GetMissCallRecharge;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class FavContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<FavContactModel> list;
    ProgressHelper progressHelper;
    private String missCall="";
    private String missCallNumber="";

    public FavContactAdapter(Activity activity, ArrayList<FavContactModel> list,String missCall,String missCallNumber) {
        this.activity = activity;
        this.list = list;
        progressHelper = new ProgressHelper(activity);
        this.missCall=missCall;
        this.missCallNumber=missCallNumber;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View adView = LayoutInflater.from(activity).inflate(R.layout.layout_fav_contact, parent, false);
        return new FavContactHolder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FavContactHolder) {
            FavContactModel usModel = (FavContactModel) list.get(position);
            FavContactHolder favContactHolder = (FavContactHolder) holder;
            favContactHolder.adContainerViewBinding.txtMobile.setText(usModel.getMobile());
            favContactHolder.adContainerViewBinding.txtName.setText(usModel.getName());
            favContactHolder.adContainerViewBinding.txtOperator.setText(usModel.getOperatorName());
            if(usModel.getMissCall().equals("1")){

                favContactHolder.adContainerViewBinding.favMisscallRecharge.setImageResource(R.drawable.misscall);
            }
            else{
                favContactHolder.adContainerViewBinding.favMisscallRecharge.setImageResource(R.drawable.missed_call_red);
            }

            if(missCall.equals("yes")){
                favContactHolder.adContainerViewBinding.favMisscallRecharge.setVisibility(View.VISIBLE);
            }

            if(!usModel.getCanSetMisscall().equals("yes")){
                favContactHolder.adContainerViewBinding.favMisscallRecharge.setVisibility(View.GONE);
            }
            //Log.d("FAVCONTACTADAPTER", "Url " + usModel.getCanSetMisscall());
            Utility.imageLoader(activity, usModel.getOperatorIcon(), favContactHolder.adContainerViewBinding.operatorIcon);

            String str = "";

            if (!usModel.getDueAmount().isEmpty()) {
                str = "Bill Amount " + usModel.getDueAmount();
            }

            List<String> departmentList = Arrays.asList(str.split(","));

            favContactHolder.adContainerViewBinding.departmentChips.removeAllViews();
            for (String s : departmentList) {
                Chip mChip = (Chip) activity.getLayoutInflater().inflate(R.layout.department_chips, null, false);
                mChip.setText(s);
                int paddingDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, activity.getResources().getDisplayMetrics());
                mChip.setPadding(paddingDp, 5, paddingDp, 5);
                favContactHolder.adContainerViewBinding.departmentChips.addView(mChip);
            }


            if (!usModel.getOnClick().isEmpty()) {
                favContactHolder.adContainerViewBinding.btnCall.setBackgroundColor(activity.getResources().getColor(R.color.success));
            } else {
                favContactHolder.adContainerViewBinding.btnCall.setVisibility(View.GONE);
            }
        }

    }

    public void filterList(ArrayList<FavContactModel> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FavContactHolder extends RecyclerView.ViewHolder {
        LayoutFavContactBinding adContainerViewBinding;
        public LinearLayout viewForeground;
        public RelativeLayout viewBackground;

        public FavContactHolder(@NonNull View itemView) {
            super(itemView);
            adContainerViewBinding = LayoutFavContactBinding.bind(itemView);

            viewBackground = itemView.findViewById(R.id.background);
            viewForeground = itemView.findViewById(R.id.foreground);

            adContainerViewBinding.btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openCall(activity, list.get(getAdapterPosition()));

                }
            });

            adContainerViewBinding.topLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    updateUsername(list.get(getAdapterPosition()), adContainerViewBinding.txtName);
                }
            });

            adContainerViewBinding.favMisscallRecharge.setOnClickListener(v -> {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(activity).inflate(R.layout.fragment_miss__call, null);
                bottomSheetView.findViewById(R.id.closeBtn).setOnClickListener(v1 -> bottomSheetDialog.dismiss());
                bottomSheetDialog.setContentView(bottomSheetView);
                TextView missCall = bottomSheetView.findViewById(R.id.misscallNumber);

                TextView btnRecharge = bottomSheetView.findViewById(R.id.btnRecharge);

                TextView rechargeNoID = bottomSheetView.findViewById(R.id.rechargeNoID);

                missCall.setText(list.get(getAbsoluteAdapterPosition()).getMobile());
                rechargeNoID.setText("Give Misscall To : "+missCallNumber);

                EditText missAmount = bottomSheetView.findViewById(R.id.misscallAmount);
                missAmount.setText(list.get(getAbsoluteAdapterPosition()).getMissCallAmount());
                //Log.d("jkhss","missCallAmount "+list.get(getAbsoluteAdapterPosition()).getMissCallAmount());


                btnRecharge.setOnClickListener(v12 ->
                        proceedNow(list.get(getAbsoluteAdapterPosition()).getOperatorId(),list.get(getAbsoluteAdapterPosition()).getMobile(),bottomSheetView));
                bottomSheetDialog.show();
            });
        }
    }

    private void proceedNow(String operatorId,String mobile,View bottomSheetView) {
        EditText missAmount = bottomSheetView.findViewById(R.id.misscallAmount);

        String amount=missAmount.getText().toString();

        Map<String, String> map1 = new HashMap<>();
        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("mobile", mobile);
        map1.put("operatorId", operatorId);
        map1.put("amount", amount);
        //map1.put("missCallNumber", binding.misscallNumber.getText().toString());

        String request = Utility.mapWrapper(activity, map1);

        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_MISS_CALL_RECHARGE, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse1(result);
                        }
                    });
        } else {
            Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
        }
    }

    private void parseResponse1(String result) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            GetMissCallRecharge response = new Gson().fromJson(result, GetMissCallRecharge.class);

            if (response.getResCode().equals(Constant.CODE_200)) {
                Utility.showToastLatest(activity,response.getResText(),response.getResCode());
                //Log.e("FavMissCall", "MissCallRechargeDone");


            } else {
                Utility.showToastLatest(activity,response.getResText(),response.getResCode());

                //Log.e("error", response.getResText());

            }
        }
    }

    public static void openCall(Activity activity, FavContactModel data) {
        try {
            //Intent intent = new Intent(Intent.ACTION_DIAL);
            //intent.setData(Uri.parse("tel:"+phone));
            //activity.startActivity(intent);

            Bundle bundle = new Bundle();

            bundle.putString(Constant.TITLE_SHOW, activity.getString(R.string.icon_title_fav_contact) + " Payment");
            bundle.putString(Constant.INPUT_DATA, data.getSearchData());
            bundle.putString(Constant.FAV_MOBILE, data.getMobile());
            bundle.putString(Constant.FAV_INPUTVALUE1, data.getInputValue1());
            bundle.putString(Constant.FAV_INPUTVALUE2, data.getInputValue2());
            //Utility.showToast(activity,"Input is "+data.getSearchData());

            Utility.openActivity(activity, data.getOnClick(), bundle);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseResponse(String result) {
        progressHelper.dismiss();
        //mBinding.progressBar.setVisibility(View.GONE);
        if (Utility.isServerRespond(result)) {
            GetFavContactDetailResponse response = new Gson().fromJson(result, GetFavContactDetailResponse.class);


            if (response.getResCode().equals(Constant.CODE_200)) {
                Log.e("name", "updated");
            } else {
                Log.e("error", response.getResText());
            }
        }
    }

    /**
     * Update contact name or delete
     */
    private void setContact(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            //Utility.showToast(activity,"Method is "+Constant.METHOD_FAV_CONTACT);
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_UPDATE_FAV_CONTACT, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, activity.getString(R.string.internet_connect), "");
        }
    }

    private void updateUsername(final FavContactModel favContactModel, final TextView name) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Update Name");
        alertDialog.setMessage("Enter Contact Name");

        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setText(favContactModel.getName());
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("UPDATE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        name.setText(input.getText().toString());


                        setContact(requestPost(favContactModel, input.getText().toString(), false));


                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    private String requestPost(FavContactModel favContactModel, String name, boolean delete) {


        Map<String, String> map1 = new HashMap<>();

        map1.put("token", Utility.getToken(activity)); // key
        map1.put("imei", Utility.getImei(activity)); // imei
        map1.put("versionCode", Utility.getVersionCode(activity)); // version code
        map1.put("os", Utility.getOs(activity));
        map1.put("favNumber", favContactModel.getMobile());
        if (delete)
            map1.put("deleteContact", "yes");
        else
            map1.put("favName", name);

        return Utility.mapWrapper(activity, map1);

    }


    private void delete(int position) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Delete Contact");
        alertDialog.setMessage("Are you sure you want to delete.");

        alertDialog.setPositiveButton("DELETE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        setContact(requestPost(list.get(position), "", true));
                        list.remove(position);
                        notifyItemRemoved(position);

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
