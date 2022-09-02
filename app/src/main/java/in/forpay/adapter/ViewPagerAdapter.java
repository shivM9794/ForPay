package in.forpay.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import in.forpay.fragment.HomeFragment1;
import in.forpay.fragment.ProfileFragment3;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {

        Fragment fragment = null;

        if (i == 0){
            try {
                fragment = new HomeFragment1();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (i == 1){
            fragment = new ProfileFragment3();
        }else if (i == 2){
            fragment = new ProfileFragment3();
        }else if (i == 3){
            fragment = new ProfileFragment3();
        }else if (i == 4){
            fragment = new ProfileFragment3();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String Title = null;

        if (position == 0){
            Title = "Home";
        }else if (position == 1){
            Title = "Invite";
        } else if (position == 2){
            Title = "Profile";
        }

        return Title;
    }
}