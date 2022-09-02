package in.forpay.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.forpay.R;
import in.forpay.activity.AddFundNewActivity;
import in.forpay.activity.AddfundActivity;
import in.forpay.activity.PanAgentActivity;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.activity.billpayment.BillPaymentActivity;
import in.forpay.activity.digitalgiftvoucher.DigitalGiftVouchersActivity;
import in.forpay.activity.moneytransfer.MoneyTransferActivity;
import in.forpay.activity.recharge.RechargeActivity;
import in.forpay.databinding.FragmentHome1Binding;
import in.forpay.fragment.digitalgiftvoucher.DigitalGiftVouchersFragment;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.listener.Listener;
import in.forpay.model.ContactList;
import in.forpay.model.response.GetBalanceResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class HomeFragment extends Fragment {

    private FragmentHome1Binding mBinding;
    Bundle bundle;

    private Activity activity;
    private ProgressHelper progressHelper;

    private OxyMePref oxyMePref;

    String serviceListLocation = "serviceList_Balance" + Constant.METHOD_REFRESH_BALANCE;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();

        oxyMePref = new OxyMePref(activity);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home1, container, false);
        mBinding.setFragment(this);

        if(getActivity()!=null) {
            Utility.setAppLocale(Utility.getDefaultLanguage(getActivity()),getActivity());

        }
        initView();
//        Utility.fillSlider(mBinding,getActivity());
         return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressHelper = new ProgressHelper(getActivity());
        init();

    }

    private void initView() {
        fillData();

    }

    private void fillData() {


        if(Utility.getIsDigitalVoucherDisabled(getActivity()).equals("1")){
            mBinding.quickMenu.setVisibility(View.GONE);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setBalance();

    }

    public void onClickBalace(int type){
        if(type==1){
            Utility.showToastLatest(getContext(),"Main Balance","SUCCESS");
        }
        else if(type==2){
            Utility.showToastLatest(getContext(),"Incentive Balance","SUCCESS");



            Map<String,String> map1 = new HashMap<>();

            map1.put("token",Utility.getToken(activity)); // key
            map1.put("imei",Utility.getImei(activity)); // imei
            map1.put("versionCode",Utility.getVersionCode(activity)); // version code
            map1.put("os", Utility.getOs(activity));

            String request = Utility.mapWrapper(activity,map1);


            agreePopUp(request);
        }
    }

    private void agreePopUp(final String request) {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_valid_commissiontransfer);
        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);


            TextView textViewAgree = dialog.findViewById(R.id.textViewAgree);
            if(textViewAgree!=null) {
                textViewAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        transferBalance(request);
                    }
                });

                dialog.show();
            }
        }
    }

    private void transferBalance(String request) {
        if (Utility.isNetworkConnected(activity)) {
            progressHelper.show();
            QueryManager.getInstance().postRequest(activity,
                    Constant.METHOD_BALANCE_TRANSFER, request, new CallbackListener() {
                        @Override
                        public void onResult(Exception e, String result,
                                             ResponseManager responseManager) {
                            parseTransferBalanceResponse(result);
                        }
                    });
        } else {
            Utility.showToast(activity, getString(R.string.internet_connect),"");
        }
    }


    private void parseTransferBalanceResponse(String result) {
        progressHelper.dismiss();
        if (Utility.isServerRespond(result)) {
            try{
                JSONObject response = new JSONObject(result);

                //String status = response.getData().getStatus();
                if (response.getString("resCode").equals(Constant.CODE_200)) {

                    Utility.refreshBalance(activity, mBinding.imageViewRefresh, new Listener() {
                        @Override
                        public void onRefreshBalance() {
                            setBalance();
                        }
                    });
                    Utility.showToast(activity, response.getString("resText"),response.getString("resCode"));


                } else {

                    Utility.showToast(activity, response.getString("resText"),response.getString("resCode"));

                }
            }
            catch (Exception e){
                Utility.showToast(activity, "Please try after some time","");
            }

        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(activity, ServerNotResponseActivity.class));
        }
    }
    /**
     * Click on refresh icon
     */
    public void onClickRefresh() {
        Utility.refreshBalance(getActivity(), mBinding.imageViewRefresh, new Listener() {
            @Override
            public void onRefreshBalance() {
                setBalance();

            }
        });
    }

    public void onClickAddFund() {
        Intent intent = new Intent(activity, AddFundNewActivity.class);
        startActivity(intent);
    }

    /**
     * Click on options
     */
    public void onClickOptions(int position) {
        if (getActivity() != null) {
            progressHelper.show();
            onClickFromHome(position);
        }
    }

    public void onClickFromHome(int position) {


        DigitalGiftVouchersFragment fragment;

        switch (position) {

            case 1: // Recharge
                startActivity(new Intent(activity, RechargeActivity.class)
                .putExtra("tabItem","noData"));
                break;
            case 6: //Metro card recharge
                if(!Utility.getIsMetroRechargeDisabled(getActivity()).equals("1")) {
                    startActivity(new Intent(activity, RechargeActivity.class).putExtra("tabItem", "metroData"));
                }
                break;
            case 2: // Money Transfer
                if(!Utility.getIsMoneyTransferDisabled(getActivity()).equals("1")) {
                    startActivity(new Intent(activity, MoneyTransferActivity.class));
                }
                break;
            case 3: // Payment
                startActivity(new Intent(activity, BillPaymentActivity.class));
                break;
            case 4: //Bus Booking
                Utility.showToastLatest(activity, "This service will be available soon", "ERROR");

//                Intent intent2 = new Intent(getActivity(), BusBookingActivity.class);
//                startActivity(intent2);
                break;
            case 5: //Pan Agent
                startActivity(new Intent(activity, PanAgentActivity.class));
                break;
            case 7: // Fashion & Lifestyle
                startActivity(new Intent(activity, DigitalGiftVouchersActivity.class)
                        .putExtra("userType",Constant.SERVICE_DIGITAL_FASHION)
                        .putExtra(Constant.TITLE_SHOW,getString(R.string.icon_title_fashion))
                        .putExtra(Constant.STATE_FROM, Constant.SERVICE_DIGITAL_FASHION));
                break;
            case 8: // Online Shopping
                startActivity(new Intent(activity, DigitalGiftVouchersActivity.class)
                        .putExtra("userType",Constant.SERVICE_DIGITAL_ONLINE_SHOP)
                        .putExtra(Constant.TITLE_SHOW,getString(R.string.icon_title_online_shopping))
                        .putExtra(Constant.STATE_FROM, Constant.SERVICE_DIGITAL_ONLINE_SHOP));
                break;
            case 9: // Entertainment
                startActivity(new Intent(activity, DigitalGiftVouchersActivity.class)
                        .putExtra("userType",Constant.SERVICE_DIGITAL_ENTERTAINMENT)
                        .putExtra(Constant.TITLE_SHOW,getString(R.string.icon_title_entertainment))
                        .putExtra(Constant.STATE_FROM, Constant.SERVICE_DIGITAL_ENTERTAINMENT));
                break;
            case 10: // Hotel
                startActivity(new Intent(activity, DigitalGiftVouchersActivity.class)
                        .putExtra("userType",Constant.SERVICE_DIGITAL_HOTEL)
                        .putExtra(Constant.TITLE_SHOW,getString(R.string.icon_title_hotel))
                        .putExtra(Constant.STATE_FROM, Constant.SERVICE_DIGITAL_HOTEL));
                break;
            case 11: // Travel
                startActivity(new Intent(activity, DigitalGiftVouchersActivity.class)
                        .putExtra("userType",Constant.SERVICE_DIGITAL_HOTEL)
                        .putExtra(Constant.TITLE_SHOW,getString(R.string.icon_title_travel))
                        .putExtra(Constant.STATE_FROM, Constant.SERVICE_DIGITAL_TRAVEL));
                break;
            case 12: // Healthy & Beauty
                startActivity(new Intent(activity, DigitalGiftVouchersActivity.class)
                        .putExtra("userType",Constant.SERVICE_DIGITAL_HOTEL)
                        .putExtra(Constant.TITLE_SHOW,getString(R.string.icon_title_health_and_beauty))
                        .putExtra(Constant.STATE_FROM,Constant.SERVICE_DIGITAL_HEALTH));
                break;

        }
    }


    private void init() {


        new Thread(new Runnable() {
            public void run() {
                getContactList();
            }
        }).start();
    }

    /**
     * Set balance
     */
    private void setBalance() {
        // Stop animating the image
        mBinding.imageViewRefresh.setAnimation(null);


        Utility.getServiceList(activity, "Balance", Constant.METHOD_REFRESH_BALANCE, true,"HomeFragment", new HomeUpdateListener() {
            @Override
            public void onDone() {
                GetBalanceResponse response = new Gson().fromJson(oxyMePref.getString(serviceListLocation), GetBalanceResponse.class);
                if(response!=null) {
                    try{

                        mBinding.textViewBalance.setText("₹ " + response.getData().getBal());

                        String b = getString(R.string.rupee) + " " + response.getData().getBal();
                        mBinding.textViewBalance.setText(b);
                        String cb = getString(R.string.rupee) + " " + response.getData().getCommissionBal();
                        mBinding.textViewCommissionBalance.setText(cb);
                    }
                    catch (Exception e){
                        Utility.showToastLatest(activity,e.toString(),"ERROR");

                    }
                }
                else{
                    Utility.showToastLatest(activity,"Server not responding","ERROR");
                }

            }
        });


    }

    /**
     * Get contact list
     */
    private void getContactList() {

        try {
            if(getActivity()!=null){
            ContentResolver cr = getActivity().getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            ArrayList<ContactList> contactList = new ArrayList<>();
            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                            phoneNo = phoneNo.replace("+91", "");

                            String s = phoneNo.substring(0, 1);
                            if (s.equals("0")) {
                                phoneNo = phoneNo.replaceFirst("0", "");
                            }
                            contactList.add(new ContactList(name, phoneNo));
                        }
                        pCur.close();
                    }
                }
                // Set contact list in shared preference
                if (contactList.size() > 0) {
                    Utility.setContactList(getActivity(), contactList);
                }
            }
            if (cur != null) {
                cur.close();
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        progressHelper.dismiss();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
