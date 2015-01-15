package com.vagnnermartins.adbelem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.dto.VideoDTO;
import com.vagnnermartins.adbelem.parse.ChurchParse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vagnnermartins on 14/01/15.
 */
public class YouTubeAdapter extends ArrayAdapter<VideoDTO> {

    public YouTubeAdapter(Context context, int resource, List<VideoDTO> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = parent.inflate(getContext(), R.layout.item_video, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        VideoDTO item = getItem(position);
        viewHolder.title.setText(item.getTitle());
        viewHolder.description.setText(item.getDescription());
        UrlImageViewHelper.setUrlDrawable(viewHolder.image, item.getThumbnail().getUrl());
        return convertView;
    }

    class ViewHolder{

        ImageView image;
        TextView title;
        TextView description;

        public ViewHolder(View view){
            this.image = (ImageView) view.findViewById(R.id.item_video_image);
            this.title = (TextView) view.findViewById(R.id.item_video_title);
            this.description = (TextView) view.findViewById(R.id.item_video_description);
        }
    }
}
