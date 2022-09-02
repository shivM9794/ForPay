package in.forpay.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import in.forpay.fragment.flightbookingfragment.OneWayFragment;
import in.forpay.fragment.flightbookingfragment.RoundFragment;
import in.forpay.fragment.fpl.Team1Fragment;
import in.forpay.fragment.fpl.Team2Fragment;

public class FPLMatchAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;
    public FPLMatchAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Team1Fragment team1Fragment = new Team1Fragment();
                return team1Fragment;

            case 1:
                Team2Fragment team2Fragment = new Team2Fragment();
                return team2Fragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
