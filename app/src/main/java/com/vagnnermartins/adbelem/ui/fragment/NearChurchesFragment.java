package com.vagnnermartins.adbelem.ui.fragment;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.gc.materialdesign.widgets.SnackBar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.app.App;
import com.vagnnermartins.adbelem.asynctask.SearchAddressAsyncTask;
import com.vagnnermartins.adbelem.callback.Callback;
import com.vagnnermartins.adbelem.enums.StatusEnum;
import com.vagnnermartins.adbelem.parse.ChurchParse;
import com.vagnnermartins.adbelem.ui.activity.ChurchDetailActivity;
import com.vagnnermartins.adbelem.ui.helper.NearChurchesUIHelper;
import com.vagnnermartins.adbelem.util.DistanceUtil;
import com.vagnnermartins.adbelem.util.KeyboardUtil;
import com.vagnnermartins.adbelem.util.NavegacaoUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vagnnermartins on 26/12/14.
 */
public class NearChurchesFragment extends Fragment {

    private static final int METERS = 1000;
    private static final int MAX_RESULT_TEXT = 1;
    private static final float ZOOM_DEFAULT = 16.0f;

    private View view;
    private NearChurchesUIHelper ui;
    private App app;
    private HashMap<Marker, ChurchParse> mapMarkers;
    private SupportMapFragment mMapaFragment;
    private LatLng currentPosition;
    private LatLng lastPositionUpdate;
    private SearchAddressAsyncTask searchAddressAsyncTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapaFragment = SupportMapFragment.newInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_near_churches, container, false);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initAfterStart();
        checkUpdate();
    }

    private void checkUpdate() {
        if(app.allChurches == null){
            checkStatus(StatusEnum.INICIO);
        }else{
            addAllPin();
        }
    }

    private void init() {
        app = (App) getActivity().getApplication();
        ui = new NearChurchesUIHelper(view);
        mapMarkers = new HashMap<>();
        getChildFragmentManager().beginTransaction().add(R.id.near_chuches_map, mMapaFragment).commit();
        ui.myLocation.setOnClickListener(onMyLocationClickListener());
        ui.update.setOnClickListener(onUpdateClickListener());
        ui.editSearch.setOnEditorActionListener(onEditorActionListener());
    }

    private void initAfterStart() {
        ui.map = mMapaFragment.getMap();
        ui.map.setMyLocationEnabled(true);
        ui.map.getUiSettings().setZoomControlsEnabled(false);
        ui.map.setOnMyLocationChangeListener(onMyLocationChangeListener());
        ui.map.getUiSettings().setZoomControlsEnabled(false);
        ui.map.setOnInfoWindowClickListener(onInfoWindowCliclListener());
        ui.map.setOnCameraChangeListener(onCameraChangeListener());
    }

    private void checkStatusByText(StatusEnum status){
        if(status == StatusEnum.INICIO){
            statusInicioByText();
        }else if(status == StatusEnum.EXECUTANDO){
            statusExecutandoText();
        }else if(status == StatusEnum.EXECUTADO){
            statusExecutadoText();
        }
    }

    private void checkStatus(StatusEnum status){
        if(status == StatusEnum.INICIO){
            lastPositionUpdate = ui.map.getCameraPosition().target;
            ChurchParse.findChurchByLocation(onFindChurchByLocation(), lastPositionUpdate, METERS);
            checkStatus(StatusEnum.EXECUTANDO);
        }else if(status == StatusEnum.EXECUTANDO){
            statusExecutando();
        }else if(status == StatusEnum.EXECUTADO){
            statusExecutado();
        }
    }

    private void statusInicioByText() {
        if(app.isInternetConnection(getActivity())){
            searchAddressAsyncTask = new SearchAddressAsyncTask(getActivity(), ui.editSearch.getText().toString(),
                    onSearchAddressCallback(), MAX_RESULT_TEXT);
            searchAddressAsyncTask.execute();
            app.registerTask(searchAddressAsyncTask);
            checkStatusByText(StatusEnum.EXECUTANDO);
        }else{
            checkStatusByText(StatusEnum.EXECUTADO);
        }
    }

    private void statusExecutandoText() {
        ui.progress.setVisibility(View.VISIBLE);
    }

    private void statusExecutadoText() {
        ui.progress.setVisibility(View.GONE);
    }

    private void statusExecutando() {
        ui.progress.setVisibility(View.VISIBLE);
    }

    private void statusExecutado() {
        ui.progress.setVisibility(View.GONE);
    }

    private GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener() {
        return new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                if(currentPosition == null){
                    updateMapPosition(current, ZOOM_DEFAULT);
                    checkStatus(StatusEnum.INICIO);
                }
                currentPosition = current;
            }
        };
    }

    private View.OnClickListener onMyLocationClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPosition != null){
                    updateMapPosition(currentPosition, ZOOM_DEFAULT);
                }
            }
        };
    }

    private TextView.OnEditorActionListener onEditorActionListener() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_SEARCH:
                        checkStatusByText(StatusEnum.INICIO);
                        KeyboardUtil.hideKeyboard(getActivity(), ui.editSearch);
                        return true;
                }
                return false;
            }
        };
    }

    private GoogleMap.OnInfoWindowClickListener onInfoWindowCliclListener() {
        return new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                app.selectedChurch = mapMarkers.get(marker);
                NavegacaoUtil.navegar(getActivity(), ChurchDetailActivity.class);
            }
        };
    }

    private GoogleMap.OnCameraChangeListener onCameraChangeListener() {
        return new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if(cameraPosition != null && currentPosition != null){
                    LatLng positionCamera = cameraPosition.target;
                    double distancia;
                    if(lastPositionUpdate == null){
                        distancia = DistanceUtil.calcularDistanciaEntreDoisPontos(currentPosition.latitude, currentPosition.longitude,
                                positionCamera.latitude, positionCamera.longitude);
                    }else{
                        distancia = DistanceUtil.calcularDistanciaEntreDoisPontos(lastPositionUpdate.latitude, lastPositionUpdate.longitude,
                                positionCamera.latitude, positionCamera.longitude);
                    }
                    if(distancia > METERS){
                        checkStatus(StatusEnum.INICIO);
                    }
                }
            }
        };
    }

    private View.OnClickListener onUpdateClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatus(StatusEnum.INICIO);
            }
        };
    }

    private FindCallback<ChurchParse> onFindChurchByLocation() {
        return new FindCallback<ChurchParse>() {
            @Override
            public void done(List<ChurchParse> result, ParseException e) {
                if(e == null){
                    ui.map.clear();
                    mapMarkers.clear();
                    app.allChurches.addAll(result);
                    addAllPin();
                }else{
                    showMessage(R.string.near_churches_error);
                }
                checkStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    private Callback onSearchAddressCallback() {
        return new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                if(error == null){
                    List<Address> address = (List<Address>) objects[0];
                    if(!address.isEmpty()){
                        updateMapPosition(new LatLng(address.get(0).getLatitude(),
                                address.get(0).getLongitude()), ZOOM_DEFAULT);
                    }
                    app.unregisterTask(searchAddressAsyncTask);
                    checkStatusByText(StatusEnum.EXECUTADO);
                }
            }
        };
    }

    private void showMessage(int text){
        if(isAdded()){
            SnackBar snack = new SnackBar(getActivity(), getString(text));
            snack.show();
        }
    }

    private void updateMapPosition(LatLng latLng, float zoom) {
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        ui.map.animateCamera(center);
    }

    private void addAllPin(){
        for(ChurchParse item : app.allChurches){
            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(item.getLatLng().getLatitude(), item.getLatLng().getLongitude()))
                    .title(item.getSector() + " - " + item.getName());
            mapMarkers.put(ui.map.addMarker(options), item);
        }
    }
}
