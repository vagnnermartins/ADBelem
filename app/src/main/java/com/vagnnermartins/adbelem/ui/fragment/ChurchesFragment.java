package com.vagnnermartins.adbelem.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.adapter.SectionsPagerAdapter;
import com.vagnnermartins.adbelem.app.App;
import com.vagnnermartins.adbelem.ui.helper.ChurchesUIHelper;

/**
 * Created by vagnnermartins on 29/12/14.
 */
public class ChurchesFragment extends Fragment {

    private ChurchesUIHelper ui;
    private App app;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_churches, container, false);
        init();
        return view;
    }

    private void init() {
        app = (App) getActivity().getApplication();
        ui = new ChurchesUIHelper(view);
        ui.mViewPager.setAdapter(new SectionsPagerAdapter(getActivity(), getChildFragmentManager()));
        ui.mViewPager.getAdapter().notifyDataSetChanged();
        ui.mViewPager.invalidate();
    }
}
