package com.michalpolic.weather.android.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.michalpolic.weather.android.fragment.NavigationDrawerFragment;
import com.michalpolic.weather.android.R;
import com.michalpolic.weather.android.entity.NavigationItem;

import java.util.List;

/**
 * Created by Michal on 8.6.2015.
 */
public class NavigationAdapter extends ArrayAdapter {

    private final Activity mContext;
    private final NavigationDrawerFragment mNavigationDrawerFragment;


    static class ViewHolder {
        public TextView text;
        public ImageView image;
    }


    public NavigationAdapter(NavigationDrawerFragment context, int resource, int textViewResourceId, List objects) {
        super(context.getActivity(), resource, textViewResourceId, objects);
        this.mContext = context.getActivity();
        this.mNavigationDrawerFragment = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            rowView = inflater.inflate(R.layout.item_navigation_list, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) rowView.findViewById(R.id.icon_navigation_id);
            viewHolder.text = (TextView) rowView.findViewById(R.id.text_navigation_id);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        NavigationItem i = (NavigationItem) this.getItem(position);
        holder.text.setText(i.getText());
        holder.image.setImageDrawable(i.getIcon());
        if(mNavigationDrawerFragment.getCurrentSelectedPosition() == position){
            rowView.setBackgroundColor(mContext.getResources().getColor(R.color.navigation_background_selected));
        }else {
            rowView.setBackgroundColor(mContext.getResources().getColor(R.color.navigation_background));
        }

        return rowView;
    }

}
