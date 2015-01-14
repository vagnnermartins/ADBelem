package com.vagnnermartins.adbelem.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vagnnermartins.adbelem.app.App;
import com.vagnnermartins.adbelem.ui.fragment.CongregationsFragment;
import com.vagnnermartins.adbelem.ui.fragment.SectorFragment;

/**
 * Created by vagnnermartins on 29/12/14.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int TOTAL_TAB = 2;
    private final Context context;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case SectorFragment.POSITION:
                fragment = Fragment.instantiate(context, SectorFragment.class.getName());
                break;
            case CongregationsFragment.POSITION:
                fragment = Fragment.instantiate(context, CongregationsFragment.class.getName());
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return TOTAL_TAB;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence result = null;
        switch (position) {
            case SectorFragment.POSITION:
                result =  SectorFragment.NAME_TAB.toUpperCase();
                break;
            case CongregationsFragment.POSITION:
                result =  CongregationsFragment.NAME_TAB.toUpperCase();
                break;
        }
        return result;
    }
}
