package com.android.quizapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quizapp.R;
import com.claudiodegio.msv.MaterialSearchView;
import com.claudiodegio.msv.OnSearchViewListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements OnSearchViewListener {

    ListView listView;
    TextView titleTextView;
    ArrayList<String> arrayList;
    MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listview);
        arrayList = new ArrayList<String>();
        titleTextView = (TextView) findViewById(R.id.listViewTitle);
        searchView = (MaterialSearchView) findViewById(R.id.listSearchView);

        Intent intent = getIntent();
        String titleString = intent.getStringExtra("title");

//        titleString = MyApplication.getGlobalTitle();
        titleTextView.setText(titleString);

        for (int i = 0; i < 10; i++) {
            arrayList.add("item " + i);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, arrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
                Toast.makeText(ListActivity.this, "Value: " + listView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_menu, menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
//            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchViewShown() {
        Toast.makeText(ListActivity.this, "onSearchViewShown.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchViewClosed() {
        Toast.makeText(ListActivity.this, "onSearchViewClosed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Toast.makeText(ListActivity.this, "onQueryTextSubmit: " + s, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onQueryTextChange(String s) {
        Toast.makeText(ListActivity.this, "onQueryTextChange: " + s, Toast.LENGTH_SHORT).show();
    }

}
