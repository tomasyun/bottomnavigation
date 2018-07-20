package www.yuntdev.com.bottomNavigationLibrary.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    private int size;
    private List<Fragment> fragments;

    public MyViewPagerAdapter(FragmentManager fm, int size, List<Fragment> fragments) {
        super(fm);
        this.size = size;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return size;
    }
}
