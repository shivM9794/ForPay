package in.forpay.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.activity.BalanceHistoryActivity;
import in.forpay.activity.BankDetailsActivity;
import in.forpay.activity.FavContactActivity;
import in.forpay.activity.FundRequestActivity;
import in.forpay.activity.LoginHistoryActivity;
import in.forpay.activity.recharge.RechargeHistoryActivity;
import in.forpay.adapter.BalanceHistoryAdapter;
import in.forpay.adapter.HistoryMenuAdapter;
import in.forpay.databinding.FragmentHistory1Binding;
import in.forpay.databinding.FragmentHistoryBinding;
import in.forpay.model.HistoryMenu;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.model.request.BalanceHistory;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;


public class HistoryFragment extends Fragment {

    private Activity activity;
    private FragmentHistory1Binding binding;
    private ProgressHelper progressHelper;

    String serviceListLocation="serviceList_HomeUpdates"+Constant.METHOD_HOME_UPDATE;
    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        return  new HistoryFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history1, container, false);

        activity = getActivity();





        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        progressHelper = new ProgressHelper(activity);

        OxyMePref oxyMePref = new OxyMePref(activity);
        String response=oxyMePref.getString(serviceListLocation);

        try {
            ArrayList<HistoryMenu> list = new ArrayList<>();

            MainRechargeModel model=new Gson().fromJson(response, MainRechargeModel.class);
            if (model.getResCode().equals(Constant.CODE_200)) {


                for(int i=0;i<model.getHistoryMenu().size();i++) {
                    HistoryMenu historyMenu = new HistoryMenu();
                    Log.d("HistoryFragement",model.getHistoryMenu().get(i).getTitle());
                    historyMenu.setActivity(model.getHistoryMenu().get(i).getActivity());
                    historyMenu.setTitle(model.getHistoryMenu().get(i).getTitle());
                    list.add(historyMenu);

                }

            }

            setAdapter(list);
        } catch (Exception e) {
            Log.d("HistoryFragement","Error "+e.toString());
        }

    }
    private void setAdapter(ArrayList<HistoryMenu> list) {
        if (list == null || list.size() == 0) {
            return;
        }



        binding.recyclerView.setAdapter(new HistoryMenuAdapter(activity, list,progressHelper));
    }
    @Override
    public void onStop() {
        progressHelper.dismiss();
        super.onStop();
    }
}
