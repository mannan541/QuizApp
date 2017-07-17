package com.android.tutorial.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.tutorial.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mannan on 7/7/2017.
 */

public class PaginatedListViewAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public PaginatedListViewAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_item, null);

        TextView name = (TextView) vi.findViewById(R.id.list_item_name_text_view);

        HashMap<String, String> item = new HashMap<String, String>();
        item = data.get(position);

        //Setting all values in listview
        name.setText(item.get("name"));
        return vi;
    }

}
