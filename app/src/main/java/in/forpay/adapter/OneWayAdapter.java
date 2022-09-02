package in.forpay.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import in.forpay.fragment.flightbookingfragment.OneWayFragment;
import in.forpay.fragment.flightbookingfragment.RoundFragment;

public class OneWayAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public OneWayAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OneWayFragment oneWayFragment = new OneWayFragment();
                return oneWayFragment;

            case 1:
                RoundFragment roundFragment = new RoundFragment();
                return roundFragment;

            default:
                return null;
        }
    }
}
