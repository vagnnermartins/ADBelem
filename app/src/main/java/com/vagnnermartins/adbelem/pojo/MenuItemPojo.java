package com.vagnnermartins.adbelem.pojo;

import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.ui.fragment.AboutFragment;
import com.vagnnermartins.adbelem.ui.fragment.ChurchesFragment;
import com.vagnnermartins.adbelem.ui.fragment.SectorFragment;
import com.vagnnermartins.adbelem.ui.fragment.MyChurchesFragment;
import com.vagnnermartins.adbelem.ui.fragment.NearChurchesFragment;
import com.vagnnermartins.adbelem.ui.fragment.YoutTubeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vagnnermartins on 26/12/14.
 */
public class MenuItemPojo {

    private int idResourceColor;
    private String fragmentName;
    private int idStringResource;
    private int idDrawableResource;

    public MenuItemPojo() {
    }

    private MenuItemPojo(String fragmentName, int idStringResource, int idDrawableResource, int idResourceColor) {
        this.fragmentName = fragmentName;
        this.idStringResource = idStringResource;
        this.idDrawableResource = idDrawableResource;
        this.idResourceColor = idResourceColor;
    }

    public List<MenuItemPojo> getItemsMenu(){
        List<MenuItemPojo> list = new ArrayList<MenuItemPojo>();
        list.add(new MenuItemPojo(ChurchesFragment.class.getName(), R.string.menu_churches, R.drawable.ic_menu_igrejas, R.color.churches));
        list.add(new MenuItemPojo(NearChurchesFragment.class.getName(), R.string.menu_near_churches, R.drawable.ic_menu_igrejas_proximas, R.color.near_churches));
        list.add(new MenuItemPojo(MyChurchesFragment.class.getName(), R.string.menu_my_churches, R.drawable.ic_menu_perfil, R.color.my_churches));
        list.add(new MenuItemPojo(MyChurchesFragment.class.getName(), R.string.menu_events, R.drawable.ic_menu_eventos, R.color.events));
        list.add(new MenuItemPojo(YoutTubeFragment.class.getName(), R.string.menu_youtube, R.drawable.ic_menu_youtube, R.color.youtube));
        list.add(new MenuItemPojo(AboutFragment.class.getName(), R.string.menu_about, R.drawable.ic_menu_sobre, R.color.about));
        return list;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public int getIdStringResource() {
        return idStringResource;
    }

    public int getIdDrawableResource() {
        return idDrawableResource;
    }

    public int getIdResourceColor() {
        return idResourceColor;
    }
}
