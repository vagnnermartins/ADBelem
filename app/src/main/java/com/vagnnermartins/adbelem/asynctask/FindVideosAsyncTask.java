package com.vagnnermartins.adbelem.asynctask;

import android.os.AsyncTask;

import com.vagnnermartins.adbelem.callback.Callback;
import com.vagnnermartins.adbelem.dto.VideoDTO;
import com.vagnnermartins.adbelem.service.YouTubeService;

import java.util.List;

/**
 * Created by vagnnermartins on 14/01/15.
 */
public class FindVideosAsyncTask extends AsyncTask<Void, Void, List<VideoDTO>> {

    private Callback callback;
    private Exception exception;

    public FindVideosAsyncTask(Callback callback){
        this.callback = callback;
    }

    @Override
    protected List<VideoDTO> doInBackground(Void... params) {
        List<VideoDTO> result = null;
        try {
            result = YouTubeService.findVideos();
        } catch (Exception e) {
            exception = e;
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<VideoDTO> result) {
        super.onPostExecute(result);
        callback.onReturn(exception, result);
    }
}
