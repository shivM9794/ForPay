package in.forpay.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.forpay.R;
import in.forpay.adapter.balance.ViewPagerBalanceAdapter;
import in.forpay.databinding.ActivityPlusBinding;
import in.forpay.fragment.balance.MainRechargeFragment;
import in.forpay.listener.HomeUpdateListener;
import in.forpay.listener.ItemClickListener;
import in.forpay.model.balance.MainRechargeModel;
import in.forpay.util.Constant;
import in.forpay.util.OxyMePref;
import in.forpay.util.ProgressHelper;
import in.forpay.util.Utility;

public class PlusActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityPlusBinding binding;
    private AppCompatActivity activity;
    private List<Fragment> mFragments;
    private int index = 0;
    private ProgressHelper progressHelper;
    private OxyMePref oxyMePref;
    private ArrayList<MainRechargeModel.MainMenuBean> arrayList;
    private int viewPagerPosition=0;
    private MainRechargeModel model;
    private ArrayList<ArrayList<MainRechargeModel.MainMenuBean>> mList;
    private String from ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPlusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        progressHelper=new ProgressHelper(activity);
        oxyMePref = new OxyMePref(activity);
        Utility.changeStatusBarColor(activity,R.color.white);
        if(getIntent().getBundleExtra("bundle")!=null)
            from=getIntent().getBundleExtra("bundle").getString("from");

        Log.d("PlusActivityFinal","From "+from);


        init();
    }

    private void init() {
        arrayList=new ArrayList<>();
/*
        if (oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE).isEmpty()){
            Utility.getHomeUpdate(activity,"","plus", new HomeUpdateListener() {
                @Override
                public void onDone() {
                    parseHomeUpdateResponse(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE));
                }
            });
        }else {
            parseHomeUpdateResponse(oxyMePref.getString(Constant.HOME_UPDATE_API_RESPONSE));
        }
*/

        Utility.getServiceList(activity, "HomeUpdates",Constant.METHOD_HOME_UPDATE,false,"PlusActivity", new HomeUpdateListener() {
            @Override
            public void onDone() {

                String serviceListLocation="serviceList_HomeUpdates"+Constant.METHOD_HOME_UPDATE;
                parseHomeUpdateResponse(oxyMePref.getString(serviceListLocation));
            }
        });

        binding.close.setOnClickListener(view -> Objects.requireNonNull(activity).getSupportFragmentManager().popBackStack());


        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()){
                    filterData(s.toString());
                    binding.viewPager.disableScroll(true);
                }else {
                    binding.noDataFound.setVisibility(View.GONE);
                    binding.dotsIndicator.setVisibility(View.VISIBLE);

                    binding.viewPager.disableScroll(false);

                    for (int i = 0; i < mList.size(); i++) {
                        setFilterData(i, mList.get(i));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPagerPosition=position;

            }

            @Override
            public void onPageSelected(int position) {
                binding.viewPager.reMeasureCurrentPage(binding.viewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.close.setOnClickListener(view -> onBackPressed());
    }

    private void filterData(String text) {
        try {
            ArrayList<MainRechargeModel.MainMenuBean> filteredList2 = new ArrayList<>();

            for (MainRechargeModel.MainMenuBean item : model.getMainMenu()) {

                if ((item.getName()!=null && item.getName().toLowerCase().contains(text.toLowerCase())) || item.getType().toLowerCase().contains(text.toLowerCase())) {
                    filteredList2.add(item);
                    Log.d("Filter data ",""+item);
                }
            }

            setFilterData(viewPagerPosition,filteredList2);

            if (filteredList2.size()==0){
            }else {
                binding.noDataFound.setVisibility(View.GONE);
            }
            binding.dotsIndicator.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFilterData(int viewPagerPosition, ArrayList<MainRechargeModel.MainMenuBean> filterData) {
        Fragment fragment=mFragments.get(viewPagerPosition);
        if (fragment instanceof MainRechargeFragment){
            ((MainRechargeFragment)fragment).setFilterData(viewPagerPosition,filterData);
        }
    }

    public void clearEditText(){
        binding.edtSearch.setText("");
    }


    private void setUpViewPager() {
        ViewPagerBalanceAdapter adapter = new ViewPagerBalanceAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mFragments);
        binding.viewPager.setAdapter(adapter);
        binding.dotsIndicator.setViewPager(binding.viewPager);
    }

    @Override
    public void onItemClick(int position, String tag) {
        binding.edtSearch.setText("");
    }

    private void parseHomeUpdateResponse(String response) {
        try {
            model=new Gson().fromJson(response, MainRechargeModel.class);
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
        mList=new ArrayList<>();
        index = 0;

        if (model.getMainMenu().size()<=19){
            mFragments.add(new MainRechargeFragment().getInstance(index, (ArrayList<MainRechargeModel.MainMenuBean>) model.getMainMenu(),from));
            mList.add((ArrayList<MainRechargeModel.MainMenuBean>) model.getMainMenu());
            return;
        }

        for (MainRechargeModel.MainMenuBean a :model.getMainMenu()){
            arrayList.add(a);

            if (arrayList.size()==19) {
                addFragment(arrayList);
            }else if (model.getMainMenu().size()/19==index && arrayList.size()==model.getMainMenu().size()%19){
                addFragment(arrayList);
            }
        }
    }
    private void addFragment(ArrayList<MainRechargeModel.MainMenuBean> arrayList){
        mFragments.add(new MainRechargeFragment().getInstance(index, arrayList,from));
        mList.add(arrayList);
        this.arrayList =new ArrayList<>();
        index++;
    }

}