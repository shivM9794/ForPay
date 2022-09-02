package in.forpay.fragment.balance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import in.forpay.R;
import in.forpay.activity.balance.GiftVoucherActivity;
import in.forpay.activity.balance.LandLineActivity;
import in.forpay.activity.balance.MobileActivity;
import in.forpay.adapter.balance.HomeAdapter;
import in.forpay.adapter.balance.LandLineAdapter;
import in.forpay.databinding.FragmentMainRechargeBinding;
import in.forpay.databinding.FragmentMobileRechargeBinding;
import in.forpay.listener.AdapterSetListener;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.balance.RechargeTypeModel;
import in.forpay.model.response.GetServiceList;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class LandLineFragment extends Fragment implements ItemClickListener {

    private FragmentMobileRechargeBinding binding;
    private Activity activity;
    private ArrayList<GetServiceList.ServiceBean> arrayList=new ArrayList<>();
    private ArrayList<GetServiceList.ServiceBean> tempArrayList=new ArrayList<>();
    private ProgressHelper progressHelper;
    private int position;
    private String title;
    private String url;
    private int AD_OFFSET = 3;
    private int AD_INDEX = 2;
    private LandLineAdapter adapter;
    private String selectedId="";
    private String selectedBbpsId="";

    public Fragment getInstance(int position, ArrayList<GetServiceList.ServiceBean> arrayList) {
        LandLineFragment fragment = new LandLineFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.POSITION, position);
        bundle.putParcelableArrayList(Constant.HOME_LIST, arrayList);
        Log.d("getInstance", "Bundle : " + position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public LandLineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_recharge, container, false);
        activity=getActivity();
        progressHelper=new ProgressHelper(activity);

        init();

        return binding.getRoot();
    }

    public void setFilterData(int viewPagerPosition, ArrayList<GetServiceList.ServiceBean> filteredList2, String operatorId,String bbpsId){
        position=viewPagerPosition;
        tempArrayList=new ArrayList<>();
        this.arrayList=new ArrayList<>();
        tempArrayList = filteredList2;
        this.arrayList.addAll(tempArrayList);
        if (!operatorId.isEmpty()){
            selectedId=operatorId;
            adapter.setItems(selectedId,tempArrayList);
        }
        if(!bbpsId.isEmpty()){

            selectedBbpsId=bbpsId;
            adapter.setItems(selectedBbpsId,tempArrayList);
        }

        if (adapter!=null){
            adapter.setData(arrayList);
        }
    }

    private void init() {

        try {
            position = getArguments().getInt(Constant.POSITION);
            tempArrayList = getArguments().getParcelableArrayList(Constant.HOME_LIST);
            arrayList.addAll(tempArrayList);
            setAdapter();
            if (position == 0)
                ((LandLineActivity) activity).setAdapterComplete();
        }
        catch (Exception e){
            Utility.showToastLatest(activity,e.toString(),"ERROR");
        }
    }

    private void setAdapter() {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(activity,3));
        adapter=new LandLineAdapter(activity,tempArrayList,this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(int position, String tag) {
        tempArrayList=new ArrayList<>();
        if (selectedId.equals(tag)){
            selectedId="";
            selectedBbpsId="";
            tempArrayList.addAll(arrayList);
            if(!arrayList.get(position).getTopIconUrl().isEmpty())
                ((LandLineActivity) activity).setTopIcon("");

            if(arrayList.get(position).getIsBillFetch().equals("0")){
                ((LandLineActivity) activity).setAmountVisibility(View.VISIBLE);
            }else {
                ((LandLineActivity) activity).setAmountVisibility(View.GONE);
            }

        }else {
            selectedId=arrayList.get(position).getId();
            selectedBbpsId=arrayList.get(position).getBbpsId();
            tempArrayList.add(arrayList.get(position));
            if(!arrayList.get(position).getTopIconUrl().isEmpty())
                ((LandLineActivity) activity).setTopIcon(arrayList.get(position).getTopIconUrl());

            if(arrayList.get(position).getIsBillFetch().equals("0"))
                ((LandLineActivity) activity).setAmountVisibility(View.VISIBLE);
            else
                ((LandLineActivity) activity).setAmountVisibility(View.GONE);
        }

        ((LandLineActivity) activity).setOnDataListener(position,selectedId,position,selectedBbpsId);
        adapter.setItems(selectedId,tempArrayList);
    }
}