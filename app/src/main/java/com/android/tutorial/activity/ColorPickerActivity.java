package com.android.tutorial.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.tutorial.R;
import com.android.tutorial.dialog.ColorPickerDialog;

public class ColorPickerActivity extends AppCompatActivity implements ColorPickerDialog.OnColorChangedListener {

    private Paint mPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
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

        mPaint = new Paint();
        // on button click
        new ColorPickerDialog(this, this, mPaint.getColor()).show();
    }


    @Override
    public void colorChanged(int color) {
        Snackbar.make(findViewById(R.id.fab), "" + color, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
