package com.vagnnermartins.adbelem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.parse.ChurchParse;
import com.vagnnermartins.adbelem.parse.EventParse;
import com.vagnnermartins.adbelem.util.DataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vagnnermartins on 05/01/15.
 */
public class EventAdapter extends ArrayAdapter<EventParse> {

    private final List<EventParse> itensOriginais;

    public EventAdapter(Context context, int resource, List<EventParse> objects) {
        super(context, resource, objects);
        this.itensOriginais = new ArrayList<EventParse>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = parent.inflate(getContext(), R.layout.item_event, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        EventParse item = getItem(position);
        viewHolder.name.setText(item.getName());
        viewHolder.date.setText(DataUtil.formatDateToString(item.getDate()));
        viewHolder.time.setText(DataUtil.transformDateToSting(item.getDate(), "HH:mm"));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                List<EventParse> values = (List<EventParse>) results.values;
                if(values == null){
                    values = itensOriginais;
                }
                for (EventParse item : values) {
                    add(item);
                }
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();

                if (constraint != null && constraint.toString().length() > 0) {
                    List<EventParse> founded = new ArrayList<EventParse>();
                    for(EventParse item: itensOriginais){
                        if(item.toString().toLowerCase().contains(constraint)){
                            founded.add(item);
                        }
                    }
                    result.values = founded;
                    result.count = founded.size();
                }
                return result;
            }
        };
    }

    class ViewHolder{

        TextView name;
        TextView date;
        TextView time;

        public ViewHolder(View view){
            this.name = (TextView) view.findViewById(R.id.item_event_name);
            this.date = (TextView) view.findViewById(R.id.item_event_date);
            this.time = (TextView) view.findViewById(R.id.item_event_time);
        }
    }
}
