package com.vagnnermartins.adbelem.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.asynctask.FindVideosAsyncTask;
import com.vagnnermartins.adbelem.callback.Callback;
import com.vagnnermartins.adbelem.dto.VideoDTO;

import java.util.List;

public class YoutubeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_youtube);
        new FindVideosAsyncTask(new Callback() {
            @Override
            public void onReturn(Exception error, Object... objects) {
                if(error == null){
                    List<VideoDTO> result = (List<VideoDTO>) objects[0];
                    for (int i = 0; i < result.size(); i++) {
                        Log.d("", result.get(i).getTitle());
                    }
                }
            }
        }).execute();
    }

}
