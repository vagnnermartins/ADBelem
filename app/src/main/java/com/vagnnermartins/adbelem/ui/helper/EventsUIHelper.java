package com.vagnnermartins.adbelem.ui.helper;

import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.view.GeneralSwipeRefreshLayout;

/**
 * Created by vagnnermartins on 26/12/14.
 */
public class EventsUIHelper {

    public SearchView searchView;
    public GeneralSwipeRefreshLayout swipeLayout;
    public ListView listView;
    public View progress;
    public TextView message;

    public EventsUIHelper(View view){
        this.searchView = (SearchView) view.findViewById(R.id.events_search);
        this.swipeLayout = (GeneralSwipeRefreshLayout) view.findViewById(R.id.events_swipe);
        this.listView = (ListView) view.findViewById(R.id.events_list);
        this.swipeLayout.setListView(listView);
        this.progress = view.findViewById(R.id.events_progress);
        this.message = (TextView) view.findViewById(R.id.events_message);
    }

}
