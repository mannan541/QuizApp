package com.android.tutorial.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.tutorial.R;

import github.nisrulz.screenshott.ScreenShott;

public class ScreenCaptureActivity extends AppCompatActivity {

    Button scButton;
    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_capture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mainLayout = (LinearLayout) findViewById(R.id.content_screen_capture);
        scButton = (Button) findViewById(R.id.bScreen);
        scButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

// View with spaces as per constraints
                Bitmap bitmap_view = ScreenShott.getInstance().takeScreenShotOfView(view);

// RootView
                Bitmap bitmap_rootview = ScreenShott.getInstance().takeScreenShotOfRootView(view);

// Just the View without any constraints
                Bitmap bitmap_hiddenview = ScreenShott.getInstance().takeScreenShotOfJustView(view);

                ScreenShott.getInstance().saveScreenshotToPicturesFolder(getApplicationContext(),
                        bitmap_rootview, "my_screenshot_filename");

            }
        });

    }

}
