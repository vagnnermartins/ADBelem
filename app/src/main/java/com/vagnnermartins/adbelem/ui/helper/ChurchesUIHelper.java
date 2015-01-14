package com.vagnnermartins.adbelem.ui.helper;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.vagnnermartins.adbelem.R;

/**
 * Created by vagnnermartins on 29/12/14.
 */
public class ChurchesUIHelper {

    public ViewPager mViewPager;

    public ChurchesUIHelper(View view){
        this.mViewPager = (ViewPager) view.findViewById(R.id.churches_viewpager);
    }
}
