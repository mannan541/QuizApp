package com.android.quizapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.quizapp.R;

import java.util.ArrayList;

/**
 * Created by klogi on 04/01/16.
 */
public class RechargeFragment extends Fragment {

    ListView listView;
    ArrayList<String> arrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recharge_fragment, container, false);

        arrayList = new ArrayList<String>();

        listView = (ListView) rootView.findViewById(R.id.recent_recharge_list);
        for (int i = 0; i < 10; i++) {
            arrayList.add("item " + i);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,
                android.R.id.text1, arrayList);

        listView.setAdapter(arrayAdapter);

        listView.setAdapter(arrayAdapter);


        return rootView;

    }

}
