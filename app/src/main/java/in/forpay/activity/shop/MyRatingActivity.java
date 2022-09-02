package in.forpay.activity.shop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import in.forpay.adapter.shop.ViewPagerShopAdapter;
import in.forpay.databinding.ActivityMyRatingBinding;
import in.forpay.fragment.shop.MyRatingFragment;
import in.forpay.fragment.shop.ShopRatingFragment;

public class MyRatingActivity extends AppCompatActivity {

    private ActivityMyRatingBinding binding;
    private AppCompatActivity activity;
    private ViewPagerShopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;

        init();
    }

    private void init() {
        setViewPagerData();
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.backPress.setOnClickListener(v->onBackPressed());
    }

    private void setViewPagerData() {
        adapter=new ViewPagerShopAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new ShopRatingFragment(),"Shop Rating");
        adapter.addFragment(new MyRatingFragment(),"My Rating");
        binding.viewPager.setAdapter(adapter);
    }
}