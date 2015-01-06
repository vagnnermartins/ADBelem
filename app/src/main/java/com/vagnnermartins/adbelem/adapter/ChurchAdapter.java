package com.vagnnermartins.adbelem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.parse.ChurchParse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vagnnermartins on 26/12/14.
 */
public class ChurchAdapter extends ArrayAdapter<ChurchParse> {

    private final List<ChurchParse> itensOriginais;

    public ChurchAdapter(Context context, int resource, List<ChurchParse> objects) {
        super(context, resource, objects);
        this.itensOriginais = new ArrayList<ChurchParse>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = parent.inflate(getContext(), R.layout.item_church, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ChurchParse item = getItem(position);
        viewHolder.name.setText(item.getSector() + " - " + item.getName());
        viewHolder.address.setText(item.getAddress());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                List<ChurchParse> values = (List<ChurchParse>) results.values;
                if(values == null){
                    values = itensOriginais;
                }
                for (ChurchParse item : values) {
                    add(item);
                }
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                FilterResults result = new FilterResults();

                if (constraint != null && constraint.toString().length() > 0) {
                    List<ChurchParse> founded = new ArrayList<ChurchParse>();
                    for(ChurchParse item: itensOriginais){
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
        TextView address;

        public ViewHolder(View view){
            this.name = (TextView) view.findViewById(R.id.item_church_name);
            this.address = (TextView) view.findViewById(R.id.item_church_address);
        }
    }
}
