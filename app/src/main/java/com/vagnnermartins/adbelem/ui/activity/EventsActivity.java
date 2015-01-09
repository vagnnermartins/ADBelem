package com.vagnnermartins.adbelem.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.gc.materialdesign.widgets.SnackBar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.adapter.EventAdapter;
import com.vagnnermartins.adbelem.app.App;
import com.vagnnermartins.adbelem.enums.StatusEnum;
import com.vagnnermartins.adbelem.parse.EventParse;
import com.vagnnermartins.adbelem.ui.helper.EventsUIHelper;

import java.util.List;

public class EventsActivity extends ActionBarActivity {

    private App app;
    private EventsUIHelper ui;
    private boolean error;
    private SnackBar snackBarError;
    private EventAdapter listAdapter;
    private EventParse selectedEvent;
    private ActionMode.Callback mCallback;
    private ActionMode mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        init();
        checkUpdate();
    }

    private void checkUpdate() {
        if(app.mapEvents.get(app.selectedChurch.getObjectId()) == null){
            checkStatus(StatusEnum.INICIO);
        }else{
            checkStatus(StatusEnum.EXECUTADO);
        }
    }

    private void init() {
        app = (App) getApplication();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Agenda " + app.selectedChurch.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ui = new EventsUIHelper(getWindow().getDecorView().findViewById(android.R.id.content));
        ui.searchView.setOnQueryTextListener(onQueryTextListener());
        ui.listView.setOnItemLongClickListener(onItemLongClickListener());
        ui.listView.setOnItemClickListener(onItemClickListener());
        configActionMode();
    }

    private void checkStatus(StatusEnum statusEnum){
        if(statusEnum == StatusEnum.INICIO){
            EventParse.findEventsByChurch(onFindEventsByChuch(), app.selectedChurch);
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

    private void setList() {
        List<EventParse> list = app.mapEvents.get(app.selectedChurch.getObjectId());
        if(list.isEmpty()){
            ui.message.setVisibility(View.VISIBLE);
            ui.message.setText(R.string.event_acitivty_no_event);
        }
        listAdapter = new EventAdapter(this, R.layout.item_event, list);
        ui.listView.setAdapter(listAdapter);
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

    private FindCallback<EventParse> onFindEventsByChuch() {
        return new FindCallback<EventParse>() {
            @Override
            public void done(List<EventParse> result, ParseException e) {
                if(e == null){
                    app.mapEvents.put(app.selectedChurch.getObjectId(), result);
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
                EventParse selected = (EventParse) adapterView.getItemAtPosition(position);
                if(mMode == null || selected == selectedEvent){
                    //TODO Context Menu
                }
                closeActionMode();
            }
        };
    }

    private AdapterView.OnItemLongClickListener onItemLongClickListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                closeActionMode();
                selectedEvent = (EventParse) parent.getItemAtPosition(position);
                if(mMode != null){
                    return false;
                }else{
                    mMode = startSupportActionMode(mCallback);
                }
                return true;
            }
        };
    }

    private void configActionMode(){
        mCallback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                getWindow().getDecorView().findViewById(android.R.id.content).findViewById(R.id.toolbar).setVisibility(View.GONE);
                actionMode.setTitle(selectedEvent.getName());
                getMenuInflater().inflate(R.menu.context_menu_select_event, menu);
                if(menu != null){
                    if(app.myEvents.contains(selectedEvent)){
                        menu.findItem(R.id.context_menu_select_event_follow).setIcon(R.drawable.ic_out);
                        menu.findItem(R.id.context_menu_select_event_follow).setTitle(R.string.church_detail_unfollow);
                    }else{
                        menu.findItem(R.id.context_menu_select_event_follow).setIcon(R.drawable.ic_in);
                        menu.findItem(R.id.context_menu_select_event_follow).setTitle(R.string.church_detail_follow);
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
                    case R.id.context_menu_select_event_follow:
                        onClickFollowUnfollow(menuItem);
                        break;
                }
                actionMode.finish();
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                mMode = null;
                getWindow().getDecorView().findViewById(android.R.id.content).findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
            }
        };
    }

    private void onClickFollowUnfollow(MenuItem menuItem) {
        SnackBar snackBar;
        selectedEvent.setFollow(!selectedEvent.isFollow());
        String message = String.format(getString(R.string.event_acitivty_follow_message), selectedEvent.getName());
        if(selectedEvent.isFollow()){
            snackBar = new SnackBar(this, message);
            app.myEvents.add(selectedEvent);
            menuItem.setChecked(true);
            EventParse.saveEventInLocal(selectedEvent);
        }else{
            snackBar = new SnackBar(this, message);
            app.myEvents.remove(selectedEvent);
            menuItem.setChecked(false);
            EventParse.deleteEventInLocal(selectedEvent);
        }
        snackBar.show();
    }

    private void closeActionMode(){
        if(mMode != null){
            mMode.finish();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
