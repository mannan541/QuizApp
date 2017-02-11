package com.android.quizapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quizapp.R;

import java.util.ArrayList;

public class GridActivity extends AppCompatActivity {

    TextView titleTextView;
    GridView gridView;
    ArrayList<String> arrayList;
    ArrayAdapter adapter;
    EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String titleString = intent.getStringExtra("title");
//        titleString = MyApplication.getGlobalTitle();

        titleTextView = (TextView) findViewById(R.id.gridTitle);
        titleTextView.setText(titleString);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        arrayList = new ArrayList<String>();
        for (int i = 0; i < 9; i++) {
            arrayList.add("item " + i);
        }

        gridView = (GridView) findViewById(R.id.grid_view);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, arrayList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GridActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
                Toast.makeText(GridActivity.this, "Value: " + gridView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                GridActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}
