package in.forpay.adapter.shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerShopNewAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentArrayList = new ArrayList<>();
    private final List<String> stringArrayList = new ArrayList<>();
    private Bundle fragmentBundle;

    public ViewPagerShopNewAdapter(@NonNull FragmentManager fm, Bundle bundle, int behavior) {
        super(fm, behavior);
        fragmentBundle=bundle;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment=fragmentArrayList.get(position);
        fragment.setArguments(fragmentBundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentArrayList.add(fragment);
        stringArrayList.add(title);
    }

    public Fragment getFragment(int position){
        return fragmentArrayList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return stringArrayList.get(position);
    }
}
