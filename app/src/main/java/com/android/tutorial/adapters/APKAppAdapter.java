package com.android.tutorial.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tutorial.R;
import com.android.tutorial.interfaces.APKSelectListener;
import com.android.tutorial.models.AppAPK;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mannan on 3/22/2017.
 */

public class APKAppAdapter extends BaseAdapter implements Filterable {

    private List<AppAPK> array_data;
    private List<AppAPK> new_array_data;
    Context context;
    APKSelectListener apkSelectListener;

    public APKAppAdapter(Context context, List<AppAPK> array_data, APKSelectListener apkSelectListener) {
        this.context = context;
        this.array_data = array_data;
        this.new_array_data = array_data;
        this.apkSelectListener = apkSelectListener;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return new_array_data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView app_name;
        ImageView app_icon;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
            convertView = layoutInflater.inflate(R.layout.list_row_apk_apps, null);
            holder = new ViewHolder();

            holder.app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
            holder.app_name = (TextView) convertView.findViewById(R.id.app_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.app_icon.setImageDrawable(new_array_data.get(position).getAppLoadIcon());
        holder.app_name.setText(new_array_data.get(position).getAppLabel());

        holder.app_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apkSelectListener.onAPKClick(new_array_data.get(position));
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                new_array_data = (ArrayList<AppAPK>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<AppAPK> FilteredArrList = new ArrayList<AppAPK>();
                if (array_data == null) {
                    array_data = new ArrayList<AppAPK>(new_array_data); // saves the original data in mOriginalValues
                }
                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = array_data.size();
                    results.values = array_data;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < array_data.size(); i++) {
                        String data_name = array_data.get(i).getAppLabel();
                        String data_package = array_data.get(i).getAppPackage();
                        Drawable data_icon = array_data.get(i).getAppLoadIcon();
                        if (data_name.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(
                                    new AppAPK(data_name, data_package, data_icon)
                            );
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

}
