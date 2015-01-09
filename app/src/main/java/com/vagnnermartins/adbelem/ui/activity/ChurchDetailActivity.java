package com.vagnnermartins.adbelem.ui.activity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.app.App;
import com.vagnnermartins.adbelem.enums.StatusEnum;
import com.vagnnermartins.adbelem.parse.ChurchParse;
import com.vagnnermartins.adbelem.parse.WorshipParse;
import com.vagnnermartins.adbelem.ui.helper.ChurchDetailUIHelper;
import com.vagnnermartins.adbelem.util.NavegacaoUtil;

import java.util.List;

public class ChurchDetailActivity extends ActionBarActivity {

    private static final float MAP_ZOOM = 16;

    private App app;
    private ChurchDetailUIHelper ui;
    private SupportMapFragment mMapFragment;
    private boolean error;
    private LatLng currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church_detail);
        init();
        checkUpdate();
    }

    private void checkUpdate() {
        if(app.mapWorship.get(app.selectedChurch.getObjectId()) == null){
            checkStatus(StatusEnum.INICIO);
        }else{
            loadWorships();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ui.map = mMapFragment.getMap();
        ui.map.setMyLocationEnabled(true);
        ui.map.getUiSettings().setZoomControlsEnabled(false);
        ui.map.setOnMyLocationChangeListener(onMyLocationChangeListener());
        putPin();
    }

    private void init() {
        app = (App) getApplication();
        ui = new ChurchDetailUIHelper(getWindow().getDecorView().findViewById(android.R.id.content));
        setSupportActionBar(ui.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ui.myLocation.setOnClickListener(onMyLocationClickListener());
        loadValues();
        loadMap();
        ui.mainFone.setOnClickListener(onFoneClickListener());
        ui.webSiteMain.setOnClickListener(onSiteClickListener());
        ui.agenda.setOnClickListener(onAgendaClickListener());
    }

    private void checkStatus(StatusEnum statusEnum){
        if(statusEnum == StatusEnum.INICIO){
            WorshipParse.findWorshipByChurch(app.selectedChurch, onFindWorshipCallback());
            checkStatus(StatusEnum.EXECUTANDO);
        }else if(statusEnum == StatusEnum.EXECUTANDO){
            ui.progressWorship.setVisibility(View.VISIBLE);
        }else if(statusEnum == StatusEnum.EXECUTADO){
            checkStatusExecutado();
        }
    }

    private void checkStatusExecutado() {
        if(error){
            SnackBar snackBarError = new SnackBar(this, getString(R.string.error_find_worship),
                    getString(R.string.try_again), onTryAgainClickListener());
            snackBarError.setIndeterminate(true);
            snackBarError.show();
        }else{
            loadWorships();
        }
        error = false;
    }

    private void loadMap() {
        mMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.church_detail_map, mMapFragment).commit();
    }

    private void loadValues() {
        getSupportActionBar().setTitle("Setor " + app.selectedChurch.getSector() + " - " + app.selectedChurch.getName());
        ui.address.setText(app.selectedChurch.getAddress() != null && !app.selectedChurch.getAddress().isEmpty() ? app.selectedChurch.getAddress() : getString(R.string.uninformed));
        ui.webSite.setText(app.selectedChurch.getWebSite() != null && !app.selectedChurch.getWebSite().isEmpty() ? app.selectedChurch.getWebSite() : getString(R.string.uninformed));
        ui.fone.setText(app.selectedChurch.getFone() != null && !app.selectedChurch.getFone().isEmpty() ? app.selectedChurch.getFone() : getString(R.string.uninformed));
        ui.pastor.setText(app.selectedChurch.getPastor() != null && !app.selectedChurch.getPastor().isEmpty() ? "Pr. " + app.selectedChurch.getPastor() : getString(R.string.uninformed));
    }

    private void putPin() {
        LatLng position = new LatLng(app.selectedChurch.getLatLng().getLatitude(),
                app.selectedChurch.getLatLng().getLongitude());
        ui.map.addMarker(new MarkerOptions()
                .position(position)
                .title(app.selectedChurch.getName()));
        updateLocation(position);
    }

    private void updateLocation(LatLng position) {
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM);
        ui.map.animateCamera(center);
    }

    private FindCallback<WorshipParse> onFindWorshipCallback() {
        return new FindCallback<WorshipParse>() {
            @Override
            public void done(List<WorshipParse> result, ParseException e) {
                if(e == null){
                    app.mapWorship.put(app.selectedChurch.getObjectId(), result);
                }else{
                    error = true;
                }
                checkStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    private void loadWorships() {
        List<WorshipParse> worships = app.mapWorship.get(app.selectedChurch.getObjectId());
        if(worships.isEmpty()){
            ui.worshipMessage.setVisibility(View.VISIBLE);
        }else{
            for(int i = 0; i < worships.size(); i++){
                View item = loadWorship(worships.get(i));
                if(i == worships.size() -1){
                    item.findViewById(R.id.item_worship_separator).setVisibility(View.GONE);
                }
                ui.containerWorship.addView(item);
            }
        }
        ui.progressWorship.setVisibility(View.GONE);
        updateButtonMyLocation();
    }

    private View loadWorship(WorshipParse worship) {
        View item = getLayoutInflater().inflate(R.layout.item_worship, null);
        ((TextView)item.findViewById(R.id.item_worship_name)).setText(worship.getName());
        ((TextView)item.findViewById(R.id.item_worship_day)).setText(worship.getDay().getDay());
        ((TextView)item.findViewById(R.id.item_worship_time)).setText(worship.getTime());
        return item;
    }

    private View.OnClickListener onTryAgainClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatus(StatusEnum.INICIO);
            }
        };
    }

    private View.OnClickListener onMyLocationClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLocation(currentPosition);
            }
        };
    }

    private GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener() {
        return new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(location != null){
                    currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }
        };
    }

    private void updateButtonMyLocation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ui.myLocation.setVisibility(View.GONE);
                ui.myLocation.setVisibility(View.VISIBLE);
            }
        }, 200);
    }

    private View.OnClickListener onSiteClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.selectedChurch.getWebSite() != null &&
                        !app.selectedChurch.getWebSite().isEmpty() &&
                        app.selectedChurch.getWebSite().startsWith("http://")){
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(app.selectedChurch.getWebSite()));
                    startActivity(viewIntent);
                }
            }
        };
    }

    private View.OnClickListener onFoneClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.selectedChurch.getFone() != null &&
                        !app.selectedChurch.getFone().isEmpty()){
                    confirmCall();
                }
            }
        };
    }

    private View.OnClickListener onAgendaClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavegacaoUtil.navegar(ChurchDetailActivity.this, EventsActivity.class);
            }
        };
    }

    private void confirmCall() {
        Dialog dialog = new Dialog(this, getString(R.string.church_detail_confirm_call_title), getString(R.string.church_detail_confirm_call_message));
        dialog.setOnAcceptButtonClickListener(onAcceptClickListener());
        dialog.show();
        dialog.getButtonAccept().setText(getString(R.string.church_detail_confirm));
        dialog.getButtonCancel().setText(getString(android.R.string.cancel));
    }

    private View.OnClickListener onAcceptClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        };
    }

    private void call() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + app.selectedChurch.getFone()));
        startActivity(callIntent);
    }

    private void onClickFollowUnfollow(MenuItem menuItem) {
        SnackBar snackBar;
        ChurchParse item = app.selectedChurch;
        item.setFollow(!item.isFollow());
        if(item.isFollow()){
            snackBar = new SnackBar(this, String.format(getString(R.string.church_detail_follow_message), app.selectedChurch.getName()));
            app.myChurches.add(item);
            menuItem.setChecked(true);
            ChurchParse.saveChurchInLocal(item);
        }else{
            snackBar = new SnackBar(this, String.format(getString(R.string.church_detail_unfollow_message), app.selectedChurch.getName()));
            app.myChurches.remove(item);
            ChurchParse.deleteChurchInLocal(item);
            menuItem.setChecked(false);
        }
        snackBar.show();
    }

    private SaveCallback onSaveChurchCallback() {
        return new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_church_detail, menu);
        if(menu != null){
            menu.findItem(R.id.menu_church_detail_follow).setChecked(app.myChurches.contains(app.selectedChurch));
            if(app.selectedChurch.getFather() == null){
                menu.findItem(R.id.menu_church_detail_congragations).setVisible(true);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_church_detail_congragations:
                NavegacaoUtil.navegar(this, CongregationsActivity.class);
                break;
            case R.id.menu_church_detail_follow:
                onClickFollowUnfollow(item);
                break;
            case R.id.menu_church_detail_agenda:
                NavegacaoUtil.navegar(ChurchDetailActivity.this, EventsActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
