package com.vagnnermartins.adbelem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.pojo.MenuItemPojo;

import java.util.List;

/**
 * Created by vagnnermartins on 26/12/14.
 */
public class MenuAdapter extends ArrayAdapter<MenuItemPojo> {

    private int resource;

    public MenuAdapter(Context context, int resource, List<MenuItemPojo> items) {
        super(context, resource, items);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder itemHolder;
        MenuItemPojo itemMenu = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, null);
            itemHolder = new ItemHolder(convertView);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        itemHolder.name.setText(itemMenu.getIdStringResource());
        itemHolder.image.setImageResource(itemMenu.getIdDrawableResource());
        return convertView;
    }

    public class ItemHolder {
        TextView name;
        ImageView image;

        ItemHolder(View view) {
            this.name = (TextView) view.findViewById(R.id.item_menu_name);
            this.image = (ImageView) view.findViewById(R.id.item_menu_image);
        }
    }
}