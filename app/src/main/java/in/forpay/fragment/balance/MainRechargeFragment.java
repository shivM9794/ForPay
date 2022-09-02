package in.forpay.fragment.balance;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import in.forpay.R;
import in.forpay.adapter.balance.HomeAdapter;
import in.forpay.databinding.FragmentMainRechargeBinding;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class MainRechargeFragment extends Fragment implements ItemClickListener {

    private FragmentMainRechargeBinding binding;
    private Activity activity;
    private ArrayList<MainRechargeModel.MainMenuBean> arrayList=new ArrayList<>();
    private ProgressHelper progressHelper;
    private int position;
    private String title;
    private String url;
    private int AD_OFFSET = 3;
    private int AD_INDEX = 2;
    private HomeAdapter adapter;
    private OxyMePref oxyMePref;
    private MainRechargeModel model;
    private AlertDialog splashDialog;
    private String from ="";

    public Fragment getInstance( int position, ArrayList<MainRechargeModel.MainMenuBean> arrayList, String from) {
        MainRechargeFragment fragment = new MainRechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from",from);
        bundle.putInt(Constant.POSITION, position);
        bundle.putParcelableArrayList(Constant.HOME_LIST, arrayList);
        Log.d("getInstance", "Bundle : " + position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public MainRechargeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_recharge, container, false);
        activity=getActivity();
        progressHelper=new ProgressHelper(activity);
        oxyMePref = new OxyMePref(activity);

        init();

        return binding.getRoot();
    }

    public void setFilterData(int viewPagerPosition, ArrayList<MainRechargeModel.MainMenuBean> filteredList2){
        position=viewPagerPosition;
        arrayList=new ArrayList<>();
        this.arrayList=new ArrayList<>();
        arrayList = filteredList2;
        if (adapter!=null){
            adapter.setData(arrayList);
        }
    }

    private void init() {

        try {
            position = getArguments().getInt(Constant.POSITION);
            arrayList = getArguments().getParcelableArrayList(Constant.HOME_LIST);

            setAdapter();
        }
        catch (Exception e){

        }
/*
        arrayList.clear();


        Log.d("MainRechargeFragment", "HomeList data "+oxyMePref.getString(Constant.HOME_LIST));
        MainRechargeModel model = new Gson().fromJson(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE), MainRechargeModel.class);

        if (model.getMainMenu() != null) {
            if (model.getMainMenu().size() > 0) {

                arrayList.addAll(model.getMainMenu());

            }

        }


 */
        setSplashDialog();
    }

    private void setAdapter() {
        binding.recyclerView.setLayoutManager(new GridLayoutManager(activity,4));
        from=getArguments().getString("from");
        adapter=new HomeAdapter(activity,arrayList,this,from);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position, String tag) {
        ArrayList<MainRechargeModel.MainMenuBean> arrayList = new ArrayList<>();

        if(!from.equals("homeCategory")) {
            arrayList = this.arrayList;
        }else {
            ArrayList<MainRechargeModel.MainMenuBean> quickMenuBeanArrayList =new ArrayList<>();
            for (MainRechargeModel.MainMenuBean menuBean: this.arrayList){
                if (menuBean.getIsBbps().equals("1"))
                    quickMenuBeanArrayList.add(menuBean);
            }
            arrayList=quickMenuBeanArrayList;
        }
        try {
            String type = arrayList.get(position).getType();
            Bundle bundle = new Bundle();
            bundle.putString("url", arrayList.get(position).getUrl());
            Log.d("MainRechargeFragment", "Clickecd " + type);
            Utility.openActivity(activity, type, bundle);
        }
        catch (Exception e){
            Utility.showToastLatest(activity,"Fragment Error "+e.toString(),"ERROR");
        }
    }

    private void setSplashDialog(){
        View view= LayoutInflater.from(activity).inflate(R.layout.dialog_splash,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(activity,R.style.ShopTheme);
        builder.setView(view);
        splashDialog=builder.create();
        Objects.requireNonNull(splashDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}