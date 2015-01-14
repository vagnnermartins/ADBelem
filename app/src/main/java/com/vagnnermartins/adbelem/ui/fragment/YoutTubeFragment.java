package com.vagnnermartins.adbelem.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.widgets.SnackBar;
import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.adapter.ChurchAdapter;
import com.vagnnermartins.adbelem.adapter.YouTubeAdapter;
import com.vagnnermartins.adbelem.app.App;
import com.vagnnermartins.adbelem.asynctask.FindVideosAsyncTask;
import com.vagnnermartins.adbelem.callback.Callback;
import com.vagnnermartins.adbelem.dto.VideoDTO;
import com.vagnnermartins.adbelem.enums.StatusEnum;
import com.vagnnermartins.adbelem.ui.helper.YouTubeUIHelper;

import java.util.List;

/**
 * Created by vagnnermartins on 26/12/14.
 */
public class YoutTubeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private App app;
    private View view;
    private YouTubeUIHelper ui;
    private boolean error;
    private SnackBar snackBarError;
    private YouTubeAdapter listAdapter;
    private FindVideosAsyncTask findVideosAsyncTask;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_youtube, container, false);
        init();
        checkUpdate();
        return view;
    }

    private void checkUpdate() {
        if(app.videos == null){
            checkStatus(StatusEnum.INICIO);
        }else{
            setList();
        }
    }

    private void init() {
        ui = new YouTubeUIHelper(view);
        app = (App) getActivity().getApplication();
        ui.swipeLayout.setOnRefreshListener(this);
        ui.swipeLayout.setColorSchemeResources(R.color.youtube);
    }

    private void checkStatus(StatusEnum statusEnum){
        if(statusEnum == StatusEnum.INICIO){
            findVideosAsyncTask = new FindVideosAsyncTask(onFindVideosCallback());
            app.registerTask(findVideosAsyncTask);
            findVideosAsyncTask.execute();
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
            ui.progress.setVisibility(View.GONE);
        }
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

    private void setList(){
        if(isAdded()){
            if(app.videos.isEmpty()){
                ui.message.setVisibility(View.VISIBLE);
                ui.message.setText(R.string.youtube_no_video);
            }else{
                ui.message.setVisibility(View.GONE);
                ui.progress.setVisibility(View.GONE);
            }
            listAdapter = new YouTubeAdapter(getActivity(), R.layout.item_church, app.videos);
            ui.listView.setAdapter(listAdapter);
        }
    }

    private Callback onFindVideosCallback() {
        return new Callback() {
            @Override
            public void onReturn(Exception e, Object... objects) {
                if(e == null){
                    app.videos = (List<VideoDTO>) objects[0];
                }else{
                    error = true;
                }
                checkStatus(StatusEnum.EXECUTADO);
            }
        };
    }

    @Override
    public void onRefresh() {
        checkStatus(StatusEnum.INICIO);
    }
}
