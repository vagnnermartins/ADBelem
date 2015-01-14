package com.vagnnermartins.adbelem.ui.helper;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.view.GeneralSwipeRefreshLayout;

/**
 * Created by vagnnermartins on 26/12/14.
 */
public class YouTubeUIHelper {

    public GeneralSwipeRefreshLayout swipeLayout;
    public TextView message;
    public ListView listView;
    public View progress;

    public YouTubeUIHelper(View view){
        this.listView = (ListView) view.findViewById(android.R.id.list);
        this.swipeLayout = (GeneralSwipeRefreshLayout) view.findViewById(R.id.youtube_swipe);
        this.swipeLayout.setListView(listView);
        this.message = (TextView) view.findViewById(R.id.youtube_message);
        this.progress = view.findViewById(R.id.youtube_progress);
    }
}
