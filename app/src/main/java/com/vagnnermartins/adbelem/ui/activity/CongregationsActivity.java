package com.vagnnermartins.adbelem.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.gc.materialdesign.widgets.SnackBar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.adapter.ChurchAdapter;
import com.vagnnermartins.adbelem.app.App;
import com.vagnnermartins.adbelem.enums.StatusEnum;
import com.vagnnermartins.adbelem.parse.ChurchParse;
import com.vagnnermartins.adbelem.ui.helper.SectorUIHelper;
import com.vagnnermartins.adbelem.util.NavegacaoUtil;

import java.util.List;

public class CongregationsActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener{

    private App app;
    private SectorUIHelper ui;
    private boolean error;
    private SnackBar snackBarError;
    private ChurchAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congregations);
        init();
        checkUpdate();
    }

    private void checkUpdate() {
        if(app.mapCongragations.get(app.selectedChurch.getObjectId()) == null){
            checkStatus(StatusEnum.INICIO);
        }else{
            setList();
        }
    }

    private void init() {
        app = (App) getApplication();
        ui = new SectorUIHelper(getWindow().getDecorView().findViewById(android.R.id.content));
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Congragações " + app.selectedChurch.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ui.swipeLayout.setOnRefreshListener(this);
        ui.swipeLayout.setColorSchemeResources(R.color.churches);
        ui.listView.setOnItemClickListener(onItemClickListener());
        ui.searchView.setOnQueryTextListener(onQueryTextListener());
    }

    private void checkStatus(StatusEnum statusEnum){
        if(statusEnum == StatusEnum.INICIO){
            ChurchParse.findCongregationsByChurch(onFindCongregationsByChurch(), app.selectedChurch);
            checkStatus(StatusEnum.EXECUTANDO);
        }else if(statusEnum == StatusEnum.EXECUTANDO){
            ui.message.setVisibility(View.GONE);
            ui.swipeLayout.setRefreshing(true);
        }else if(statusEnum == StatusEnum.EXECUTADO){
            checkStatusExecutado();
        }
    }

    private void checkStatusExecutado() {
        if(error){
            snackBarError = new SnackBar(this, getString(R.string.error_find_churches),
                    getString(R.string.try_again), onTryAgainClickListener());
            snackBarError.setIndeterminate(true);
            snackBarError.show();
        }else{
            setList();
        }
        error = false;
        ui.swipeLayout.setRefreshing(false);
        ui.progress.setVisibility(View.GONE);
    }

    private void setList(){
        List list = app.mapCongragations.get(app.selectedChurch.getObjectId());
        if(list.isEmpty()){
            ui.message.setVisibility(View.VISIBLE);
            ui.message.setText(R.string.no_church);
        }else{
            ui.message.setVisibility(View.GONE);
            ui.progress.setVisibility(View.GONE);
        }
        listAdapter = new ChurchAdapter(this, R.layout.item_church, list);
        ui.listView.setAdapter(listAdapter);
    }

    private FindCallback<ChurchParse> onFindCongregationsByChurch() {
        return new FindCallback<ChurchParse>() {
            @Override
            public void done(List<ChurchParse> result, ParseException e) {
                if(e == null){
                    app.mapCongragations.put(app.selectedChurch.getObjectId(), result);
                }else{
                    error = true;
                }
                checkStatus(StatusEnum.EXECUTADO);
            }

        };
    }

    private AdapterView.OnItemClickListener onItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                app.selectedChurch = (ChurchParse) adapterView.getItemAtPosition(position);
                NavegacaoUtil.navegar(CongregationsActivity.this, ChurchDetailActivity.class);
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

    private View.OnClickListener onTryAgainClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatus(StatusEnum.INICIO);
                snackBarError.dismiss();
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        checkStatus(StatusEnum.INICIO);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
