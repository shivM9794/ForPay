package in.forpay.fragment.balance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import in.forpay.activity.balance.MobileActivity;
import in.forpay.activity.supportchat.SupportChatActivity;
import in.forpay.adapter.balance.LandLineAdapter;
import in.forpay.adapter.balance.MobileAdapter;
import in.forpay.databinding.FragmentMainRechargeBinding;
import in.forpay.databinding.FragmentMobileRechargeBinding;
import in.forpay.listener.AdapterSetListener;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.model.balance.RechargeTypeModel;
import in.forpay.util.Constant;
import in.forpay.util.ProgressHelper;

public class MobileFragment extends Fragment implements ItemClickListener {

    private FragmentMobileRechargeBinding binding;
    private Activity activity;
    private ArrayList<MainRechargeModel.ServiceBean> arrayList=new ArrayList<>();
    private ArrayList<MainRechargeModel.ServiceBean> tempArrayList=new ArrayList<>();
    private ProgressHelper progressHelper;
    private int position;
    private MobileAdapter adapter;
    private String selectedId="";

    public Fragment getInstance(int position, ArrayList<MainRechargeModel.ServiceBean> arrayList) {
        MobileFragment fragment = new MobileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.POSITION, position);
        bundle.putParcelableArrayList(Constant.HOME_LIST, arrayList);
        Log.d("getInstance", "Bundle : " + position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public MobileFragment() {
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

    public void setFilterData(int viewPagerPosition, ArrayList<MainRechargeModel.ServiceBean> filteredList2, String operatorId){
        position=viewPagerPosition;
        tempArrayList=new ArrayList<>();
        this.arrayList=new ArrayList<>();
        tempArrayList = filteredList2;
        this.arrayList.addAll(tempArrayList);
        if (!operatorId.isEmpty()){
            selectedId=operatorId;
            adapter.setItems(selectedId,tempArrayList);
        }
        if (adapter!=null){
            adapter.setData(arrayList);
        }
    }

    private void init() {

        position = getArguments().getInt(Constant.POSITION);
        tempArrayList = getArguments().getParcelableArrayList(Constant.HOME_LIST);
        arrayList.addAll(tempArrayList);


        setAdapter();
    }

    private void setAdapter() {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(activity,3));
        adapter=new MobileAdapter(activity,tempArrayList,this);
        binding.recyclerView.setAdapter(adapter);
        if (position==0)
            ((MobileActivity)activity).setAdapterComplete();
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
            tempArrayList.addAll(arrayList);
            if(!arrayList.get(position).getTopIconUrl().isEmpty())
                ((MobileActivity)activity).setTopIcon("");
        }else {
            selectedId=arrayList.get(position).getId();
            tempArrayList.add(arrayList.get(position));
            if(!arrayList.get(position).getTopIconUrl().isEmpty())
                ((MobileActivity)activity).setTopIcon(arrayList.get(position).getTopIconUrl());
        }
        //listener.onItemClick(position,selectedId);
        ((MobileActivity) activity).setOnDataListener(position,selectedId);
        adapter.setItems(selectedId,tempArrayList);
    }
}