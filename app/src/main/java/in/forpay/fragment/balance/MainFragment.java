package in.forpay.fragment.balance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.forpay.R;
import in.forpay.activity.AddFundNewActivity;
import in.forpay.activity.AddfundActivity;
import in.forpay.activity.ServerNotResponseActivity;
import in.forpay.adapter.balance.ViewPagerBalanceAdapter;
import in.forpay.databinding.FragmentMainBinding;
import in.forpay.fragment.HomeFragment;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.listener.ItemClickListener;
import in.forpay.listener.Listener;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.model.response.GetBalanceResponse;
import in.forpay.network.CallbackListener;
import in.forpay.network.QueryManager;
import in.forpay.network.ResponseManager;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;


public class MainFragment extends Fragment implements ItemClickListener {

    private FragmentMainBinding binding;
    private Activity activity;
    private List<Fragment> mFragments;
    private int index = 0;
    private ProgressHelper progressHelper;
    private OxyMePref oxyMePref;
    private ArrayList<MainRechargeModel.MainMenuBean> arrayList;

    String serviceListLocation = "serviceList_Balance" + Constant.METHOD_REFRESH_BALANCE;
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false);
        activity=getActivity();
        progressHelper=new ProgressHelper(activity);
        oxyMePref = new OxyMePref(activity);

        init();

        return binding.getRoot();
    }

    private void init() {
        arrayList=new ArrayList<>();

        /*
        if (oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE).isEmpty()){
            Utility.getHomeUpdate(activity,"","main", new HomeUpdateListener() {
                @Override
                public void onDone() {
                    parseHomeUpdateResponse(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE));
                }
            });
        }else {
            parseHomeUpdateResponse(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE));
        }


         */


        String serviceListLocation="serviceList_HomeUpdates"+Constant.METHOD_HOME_UPDATE;
        if(oxyMePref.getString(serviceListLocation)==null || oxyMePref.getString(serviceListLocation).isEmpty()) {
            Utility.getServiceList(activity, "HomeUpdates", Constant.METHOD_HOME_UPDATE, false,"MainFragment", new HomeUpdateListener() {
                @Override
                public void onDone() {

                    parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation));
                }
            });
        }
        else {
            parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation));
        }

        //binding.close.setOnClickListener(view -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());
        binding.close.setOnClickListener(view -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.imageViewAddFund.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AddFundNewActivity.class);
            startActivity(intent);
        });

        binding.textViewCommissionBalance.setOnClickListener(view -> {
            Utility.showToastLatest(getContext(),"Incentive Balance","SUCCESS");


            Map<String,String> map1 = new HashMap<>();

            map1.put("token",Utility.getToken(activity)); // key
            map1.put("imei",Utility.getImei(activity)); // imei
            map1.put("versionCode",Utility.getVersionCode(activity)); // version code
            map1.put("os", Utility.getOs(activity));


            String request = Utility.mapWrapper(activity,map1);

            agreePopUp(request);
        });

        binding.imageViewRefresh.setOnClickListener(view -> {
            Utility.refreshBalance(getActivity(), binding.imageViewRefresh, new Listener() {
                @Override
                public void onRefreshBalance() {
                    setBalance();
                }
            });
        });
    }

    /**
     * Set balance
     */
    private void setBalance() {
        // Stop animating the image
        binding.imageViewRefresh.setAnimation(null);

        Utility.getServiceList(activity, "Balance", Constant.METHOD_REFRESH_BALANCE, true,"MainFragment", new HomeUpdateListener() {
            @Override
            public void onDone() {
                GetBalanceResponse response = new Gson().fromJson(oxyMePref.getString(serviceListLocation), GetBalanceResponse.class);
                if(response!=null) {
                    try{

                        String cb = getString(R.string.rupee) + " " + response.getData().getCommissionBal();
                        binding.textViewCommissionBalance.setText(cb);
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

    private void setUpViewPager() {
        ViewPagerBalanceAdapter adapter = new ViewPagerBalanceAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mFragments);
        binding.viewPager.setAdapter(adapter);
        binding.dotsIndicator.setViewPager(binding.viewPager);
    }

    @Override
    public void onItemClick(int position, String tag) {

    }

    private void parseHomeUpdateResponse(String response) {
        try {
            MainRechargeModel model=new Gson().fromJson(response, MainRechargeModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {
                Log.d("HomeUpdateResponse","response "+response);

                setFragments(model);
                setUpViewPager();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFragments(MainRechargeModel model) {
        mFragments = new ArrayList<>();
        index = 0;

        if (model.getMainMenu().size()<=8){
            addFragment((ArrayList<MainRechargeModel.MainMenuBean>) model.getMainMenu());
            return;
        }

        for (MainRechargeModel.MainMenuBean a :model.getMainMenu()){
            arrayList.add(a);

            if (arrayList.size()==9) {
                addFragment(arrayList);
            }else if (model.getMainMenu().size()/9==index && arrayList.size()==model.getMainMenu().size()%9){
                addFragment(arrayList);
            }
        }
    }
    private void addFragment(ArrayList<MainRechargeModel.MainMenuBean> arrayList){
        mFragments.add(new MainRechargeFragment().getInstance(index, arrayList,""));
        this.arrayList =new ArrayList<>();
        index++;
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

                    Utility.refreshBalance(activity, binding.imageViewRefresh, new Listener() {
                        @Override
                        public void onRefreshBalance() {
                            setBalance();
                        }
                    });
                }
                Utility.showToast(activity, response.getString("resText"),response.getString("resCode"));
            }
            catch (Exception e){
                Utility.showToast(activity, "Please try after some time","");
            }

        } else {
            //Utility.showToast(activity, getString(R.string.server_not_responding),"");
            startActivity(new Intent(getActivity(), ServerNotResponseActivity.class));
        }
    }
}