package com.vagnnermartins.adbelem.ui.helper;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.vagnnermartins.adbelem.R;

/**
 * Created by vagnnermartins on 26/12/14.
 */
public class ChurchDetailUIHelper {

    public Toolbar toolbar;
    public View mainFone;
    public View webSiteMain;
    public TextView fone;
    public TextView webSite;
    public TextView address;
    public TextView worshipMessage;
    public TextView pastor;
    public View progressWorship;
    public LinearLayout containerWorship;
    public View myLocation;
    public View agenda;
    public GoogleMap map;

    public ChurchDetailUIHelper(View view){
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        this.mainFone = view.findViewById(R.id.church_detail_phone_main);
        this.webSiteMain = view.findViewById(R.id.church_detail_website_main);
        this.fone = (TextView) view.findViewById(R.id.church_detail_phone);
        this.webSite = (TextView) view.findViewById(R.id.church_detail_website);
        this.address = (TextView) view.findViewById(R.id.church_detail_address);
        this.worshipMessage = (TextView) view.findViewById(R.id.church_detail_worship_message);
        this.pastor = (TextView) view.findViewById(R.id.church_detail_pastor);
        this.progressWorship = view.findViewById(R.id.church_detail_worship_progress);
        this.containerWorship = (LinearLayout) view.findViewById(R.id.church_detail_worship_container);
        this.myLocation = view.findViewById(R.id.church_detail_my_location);
        this.agenda = view.findViewById(R.id.church_detail_agenda);
    }
}
