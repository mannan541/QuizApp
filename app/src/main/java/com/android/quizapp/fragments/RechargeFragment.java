package com.android.quizapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.quizapp.R;

import java.util.ArrayList;

/**
 * Created by klogi on 04/01/16.
 */
public class RechargeFragment extends Fragment {

    ListView recent_recharge_list;
    ArrayList<String> stringList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        stringList = new ArrayList<String>();

        recent_recharge_list = (ListView) getView().findViewById(R.id.recent_recharge_list);
        for (int i = 0; i < 10; i++) {
            stringList.add("item " + i);
        }

//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
//                android.R.id.text1, stringList);

//        recent_recharge_list.setAdapter(arrayAdapter);


        return inflater.inflate(R.layout.recharge_fragment, container, false);

    }

}
