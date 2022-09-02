package in.forpay.adapter.balance;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerBalanceAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList ;
    private FragmentManager mManager;

    public ViewPagerBalanceAdapter(FragmentManager manager, int behavior, List<Fragment> mFragments) {
        super(manager,behavior);
        this.mFragmentList=mFragments;
        this.mManager = manager;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public Fragment getFragment(int position){
        return mFragmentList.get(position);
    }

    public void removeFragment(/*Fragment fragment,*/int position) {
        mManager.beginTransaction().remove(mFragmentList.get(position)).commit();
        mFragmentList.remove(position);
        notifyDataSetChanged();
    }
}
