package com.android.quizapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quizapp.R;

import java.util.ArrayList;

public class GridActivity extends AppCompatActivity {

    TextView titleTextView;
    GridView gridView;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String titleString = intent.getStringExtra("title");
//        titleString = MyApplication.getGlobalTitle();

        titleTextView = (TextView) findViewById(R.id.gridTitle);
        titleTextView.setText(titleString);

        arrayList = new ArrayList<String>();
        for (int i = 0; i < 9; i++){
            arrayList.add("item "+i);
        }

        gridView = (GridView) findViewById(R.id.grid_view);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,
                android.R.id.text1,arrayList);
        gridView.setAdapter(arrayAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GridActivity.this, "Position: "+position, Toast.LENGTH_SHORT).show();
                Toast.makeText(GridActivity.this, "Value: "+gridView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
