package com.android.tutorial.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutorial.R;
import com.android.tutorial.adapters.PaginatedListViewAdapter;
import com.android.tutorial.interfaces.EndlessScrollListener;
import com.android.tutorial.utils.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class PaginatedListViewActivity extends AppCompatActivity {

    // All variables
    XMLParser parser;
    Document doc;
    String xml;
    ListView lv;
    PaginatedListViewAdapter adapter;
    ArrayList<HashMap<String, String>> menuItems;
    ProgressDialog pDialog;

    private String URL = "http://api.androidhive.info/list_paging/?page=1";

    // XML node keys
    static final String KEY_ITEM = "item"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_NAME = "name";

    // Flag for current page
    int current_page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paginated_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab();

        lv = (ListView) findViewById(R.id.paginatedListview);

        menuItems = new ArrayList<HashMap<String, String>>();

        parser = new XMLParser();
//        xml = parser.getXmlFromUrl(URL); // getting XML
//        doc = parser.getDomElement(xml); // getting DOM element

//        NodeList nl = doc.getElementsByTagName(KEY_ITEM);
        // looping through all item nodes <item>
/*
        for (int i = 0; i < nl.getLength(); i++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(i);
            // adding each child node to HashMap key => value
            map.put(KEY_ID, parser.getValue(e, KEY_ID)); // id not using any where
            map.put(KEY_NAME, parser.getValue(e, KEY_NAME));

            // adding HashList to ArrayList
            menuItems.add(map);
        }
*/

        // LoadMore button
        Button btnLoadMore = new Button(this);
        btnLoadMore.setText("Load More");

        ProgressBar progressBar = new ProgressBar(this);

        // Adding Load More button to lisview at bottom
        lv.addFooterView(progressBar);

        // Getting adapter
        adapter = new PaginatedListViewAdapter(this, menuItems);
        lv.setAdapter(adapter);

        new loadMoreListView().execute();

        /**
         * Listening to Load More button click event
         * */
        btnLoadMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Starting a new async task
                new loadMoreListView().execute();
            }
        });

        lv.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                current_page = page;
                new loadMoreListView().execute();
                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        /**
         * Listening to listview single row selected
         * **/
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.list_item_name_text_view))
                        .getText().toString();

                Toast.makeText(PaginatedListViewActivity.this, "" + name, Toast.LENGTH_SHORT).show();
                // Starting new intent
//                Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
//                in.putExtra(KEY_NAME, name);
//                startActivity(in);
            }
        });
    }

    /**
     * Async Task that send a request to url
     * Gets new list view data
     * Appends to list view
     */
    private class loadMoreListView extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(
                    PaginatedListViewActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
//            pDialog.show();
        }

        protected Void doInBackground(Void... unused) {
//            runOnUiThread(new Runnable() {
//                public void run() {
            // increment current page

            // Next page request
            URL = "http://api.androidhive.info/list_paging/?page=" + current_page;

            try {

                xml = parser.getXmlFromUrl(URL); // getting XML
                doc = parser.getDomElement(xml); // getting DOM element

                NodeList nl = doc.getElementsByTagName(KEY_ITEM);
                // looping through all item nodes <item>
                for (int i = 0; i < nl.getLength(); i++) {
                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();
                    Element e = (Element) nl.item(i);

                    // adding each child node to HashMap key => value
                    map.put(KEY_ID, parser.getValue(e, KEY_ID));
                    map.put(KEY_NAME, parser.getValue(e, KEY_NAME));

                    // adding HashList to ArrayList
                    menuItems.add(map);
                }
            } catch (Exception e) {
            }

                /*    // get listview current position - used to maintain scroll position
                    int currentPosition = lv.getFirstVisiblePosition();

                    // Appending new data to menuItems ArrayList
                    adapter = new PaginatedListViewAdapter(
                            PaginatedListViewActivity.this,
                            menuItems);
                    lv.setAdapter(adapter);
                    // Setting new scroll position
                    lv.setSelectionFromTop(currentPosition + 1, 0);*/

//                }
//            });

            return (null);
        }

        protected void onPostExecute(Void unused) {
            // closing progress dialog
//            pDialog.dismiss();

            // get listview current position - used to maintain scroll position
            int currentPosition = lv.getFirstVisiblePosition();

            current_page += 1;

            // Appending new data to menuItems ArrayList
            adapter = new PaginatedListViewAdapter(
                    PaginatedListViewActivity.this,
                    menuItems);
            lv.setAdapter(adapter);
            // Setting new scroll position
            lv.setSelectionFromTop(currentPosition + 1, 0);
        }
    }

    private void fab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new loadMoreListView().execute();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

}
