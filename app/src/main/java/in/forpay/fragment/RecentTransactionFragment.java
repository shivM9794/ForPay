package in.forpay.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import in.forpay.R;
import in.forpay.adapter.RecentTransactionAdapter;
import in.forpay.database.DatabaseHelper;
import in.forpay.databinding.FragmentRecentTransactionBinding;
import in.forpay.fragment.balance.MobileFragment;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.model.request.RechargeHistory;
import in.forpay.model.response.GetRechargeHistoryResponse;
import in.forpay.util.Utility;

public class RecentTransactionFragment extends Fragment {

    private FragmentRecentTransactionBinding binding;
    private Activity activity;
    private ArrayList<Object> arrayList = new ArrayList<>();
    private ArrayList<Object>tempArrayList=new ArrayList<>();
    private RecentTransactionAdapter recentTransactionAdapter;
    private DatabaseHelper databaseHelper;
    private int mIndex;

    public RecentTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_recent_transaction, container, false);
        activity=getActivity();
        databaseHelper = new DatabaseHelper(activity);

        mIndex = getArguments().getInt("index");

        ArrayList<RechargeHistory> list = databaseHelper.getRechargeHistoryList("","");
        ArrayList<RechargeHistory> temp=new ArrayList<>();
        temp.addAll(list);
        temp.addAll(list);
        temp.addAll(list);
        temp.addAll(list);
        temp.addAll(list);
        temp.addAll(list);
        setAdapter(list);

        setupRecyclerView(binding.recyclerView);

        return binding.getRoot();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", mIndex);
        Log.d("test", "call onSaveInstanceState:" + mIndex);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(recentTransactionAdapter);
    }

    private void setAdapter(ArrayList<RechargeHistory> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        arrayList.clear();
        arrayList.addAll(list);
        tempArrayList.clear();
        setData();
        recentTransactionAdapter=new RecentTransactionAdapter(activity,tempArrayList);
        binding.recyclerView.setAdapter(recentTransactionAdapter);
        recentTransactionAdapter.notifyDataSetChanged();
    }

    private void setData() {
        if (arrayList.size()<=2){
            return;
        }

        for (int i = mIndex*2; i < arrayList.size(); i++) {
            tempArrayList.add(arrayList.get(i));

            if (tempArrayList.size()==2){
                return;
            }else if (arrayList.size()/2==mIndex && tempArrayList.size()==arrayList.size()%2){
               return;
            }
        }
    }
}