package com.vagnnermartins.adbelem.ui.helper;

import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.vagnnermartins.adbelem.R;

/**
 * Created by vagnnermartins on 29/12/14.
 */
public class NearChurchesUIHelper {

    public View myLocation;
    public View update;
    public View progress;
    public TextView editSearch;
    public GoogleMap map;

    public NearChurchesUIHelper(View view){
        this.myLocation = view.findViewById(R.id.near_chuches_my_location);
        this.update = view.findViewById(R.id.near_chuches_update);
        this.progress = view.findViewById(R.id.near_chuches_progress);
        this.editSearch = (TextView) view.findViewById(R.id.near_chuches_edit_search);
    }

}
