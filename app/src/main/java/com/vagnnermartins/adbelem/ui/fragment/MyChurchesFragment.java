package com.vagnnermartins.adbelem.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.gc.materialdesign.widgets.SnackBar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.adapter.ChurchAdapter;
import com.vagnnermartins.adbelem.app.App;
import com.vagnnermartins.adbelem.enums.StatusEnum;
import com.vagnnermartins.adbelem.parse.ChurchParse;
import com.vagnnermartins.adbelem.ui.activity.ChurchDetailActivity;
import com.vagnnermartins.adbelem.ui.helper.SectorUIHelper;
import com.vagnnermartins.adbelem.util.NavegacaoUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by vagnnermartins on 26/12/14.
 */
public class MyChurchesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private App app;
    private View view;
    private SectorUIHelper ui;
    private boolean error;
    private SnackBar snackBarError;
    private ChurchAdapter listAdapter;
    private ChurchParse selectedChurch;
    private ActionMode.Callback mCallback;
    private ActionMode mMode;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sectors, container, false);
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUpdate();
    }

    private void checkUpdate() {
        if(app.myChurches == null){
            checkStatus(StatusEnum.INICIO);
        }else{
            setList();
        }
    }

    private void init() {
        app = (App) getActivity().getApplication();
        ui = new SectorUIHelper(view);
        ui.swipeLayout.setOnRefreshListener(this);
        ui.swipeLayout.setColorSchemeResources(R.color.churches);
        ui.listView.setOnItemClickListener(onItemClickListener());
        ui.searchView.setOnQueryTextListener(onQueryTextListener());
        ui.listView.setOnItemLongClickListener(onItemLongClickListener());
        configActionMode();
    }

    private void checkStatus(StatusEnum statusEnum){
        if(statusEnum == StatusEnum.INICIO){
            ChurchParse.findMyChurches(onFindMyChurches());
            checkStatus(StatusEnum.EXECUTANDO);
        }else if(statusEnum == StatusEnum.EXECUTANDO){
            ui.message.setVisibility(View.GONE);
            ui.swipeLayout.setRefreshing(true);
        }else if(statusEnum == StatusEnum.EXECUTADO){
            checkStatusExecutado();
        }
    }

    private void checkStatusExecutado() {
        if(isAdded()){
            if(error){
                snackBarError = new SnackBar(getActivity(), getString(R.string.error_find_churches),
                        getString(R.string.try_again), onTryAgainClickListener());
                snackBarError.setIndeterminate(true);
                snackBarError.show();
            }else{
                setList();
            }
            error = false;
            ui.swipeLayout.setRefreshing(false);
        }
    }

    private FindCallback<ChurchParse> onFindMyChurches() {
        return new FindCallback<ChurchParse>() {
            @Override
            public void done(List<ChurchParse> result, ParseException e) {
                if(e == null){
                    if(e == null){
                        app.myChurches = result;
                    }else{
                        error = true;
                    }
                    checkStatus(StatusEnum.EXECUTADO);
                }
            }
        };
    }

    private void setList(){
        if(isAdded()){
            checkResult();
            ui.progress.setVisibility(View.GONE);
            listAdapter = new ChurchAdapter(getActivity(), R.layout.item_church, app.myChurches);
            ui.listView.setAdapter(listAdapter);
        }
    }

    private void checkResult() {
        if(app.myChurches.isEmpty()){
            ui.message.setVisibility(View.VISIBLE);
            ui.message.setText(R.string.no_church);
        }else{
            ui.message.setVisibility(View.GONE);
            ui.progress.setVisibility(View.GONE);
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ChurchParse selected = (ChurchParse) adapterView.getItemAtPosition(position);
                if(mMode == null || selected == selectedChurch) {
                    app.selectedChurch = selected;
                    NavegacaoUtil.navegar(getActivity(), ChurchDetailActivity.class);
                }
                closeActionMode();
            }
        };
    }

    private View.OnClickListener onTryAgainClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatus(StatusEnum.INICIO);
                snackBarError.dismiss();
            }
        };
    }

    private AdapterView.OnItemLongClickListener onItemLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                closeActionMode();
                selectedChurch = (ChurchParse) parent.getItemAtPosition(position);
                if(mMode != null){
                    return false;
                }else{
                    mMode = ((ActionBarActivity)getActivity()).startSupportActionMode(mCallback);
                }
                return true;
            }
        };
    }

    private SearchView.OnQueryTextListener onQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(listAdapter != null){
                    listAdapter.getFilter().filter(newText);
                }
                return false;
            }
        };
    }

    private void closeActionMode(){
        if(mMode != null){
            mMode.finish();
        }
    }

    private void configActionMode(){
        mCallback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                getActivity().getWindow().getDecorView().findViewById(android.R.id.content).findViewById(R.id.toolbar).setVisibility(View.GONE);
                actionMode.setTitle(selectedChurch.getName());
                getActivity().getMenuInflater().inflate(R.menu.context_menu_select_church, menu);
                if(menu != null){
                    if(app.myChurches.contains(selectedChurch)){
                        menu.findItem(R.id.context_menu_select_church_follow).setIcon(R.drawable.ic_out);
                        menu.findItem(R.id.context_menu_select_church_follow).setTitle(R.string.church_detail_unfollow);
                    }else{
                        menu.findItem(R.id.context_menu_select_church_follow).setIcon(R.drawable.ic_in);
                        menu.findItem(R.id.context_menu_select_church_follow).setTitle(R.string.church_detail_follow);
                    }
                }
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.context_menu_select_church_follow:
                        onClickFollowUnfollow(menuItem);
                        break;
                    case R.id.context_menu_select_church_more:
                        app.selectedChurch = selectedChurch;
                        NavegacaoUtil.navegar(getActivity(), ChurchDetailActivity.class);
                        break;
                }
                actionMode.finish();
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                mMode = null;
                getActivity().getWindow().getDecorView().findViewById(android.R.id.content).findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
            }
        };
    }

    private void onClickFollowUnfollow(MenuItem menuItem) {
        SnackBar snackBar;
        selectedChurch.setFollow(!selectedChurch.isFollow());
        if(selectedChurch.isFollow()){
            snackBar = new SnackBar(getActivity(), String.format(getString(R.string.church_detail_follow_message), selectedChurch.getName()));
            app.myChurches.add(selectedChurch);
            menuItem.setChecked(true);
            ChurchParse.saveChurchInLocal(selectedChurch);
        }else{
            snackBar = new SnackBar(getActivity(), String.format(getString(R.string.church_detail_unfollow_message), selectedChurch.getName()));
            app.myChurches.remove(selectedChurch);
            menuItem.setChecked(false);
            listAdapter.remove(selectedChurch);
            ChurchParse.deleteChurchInLocal(selectedChurch);
        }
        checkResult();
        snackBar.show();
    }

    @Override
    public void onRefresh() {
        checkStatus(StatusEnum.INICIO);
    }
}
