package com.android.tutorial.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.tutorial.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class YoutubeDownloaderActivity extends AppCompatActivity {

    URL url;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_downloader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (TextView) findViewById(R.id.textView);
        new MyAsycnTask().execute();
    }

    public void downloadVideo() {
        InputStream is = null;
        try {
            is = url.openStream();
            HttpURLConnection huc = (HttpURLConnection) url.openConnection(); //to know the size of video
            int size = huc.getContentLength();

            if (huc != null) {
                String fileName = "FILE.mp4";
                String storagePath = Environment.getExternalStorageDirectory().toString();
                File f = new File(storagePath, fileName);

                FileOutputStream fos = new FileOutputStream(f);
                byte[] buffer = new byte[1024];
                int len1 = 0;
                if (is != null) {
                    while ((len1 = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, len1);
                    }
                }
                if (fos != null) {
                    fos.close();
                }
            }
        } catch (MalformedURLException mue) {
            textView.setText("MalformedURLException:" + mue.getMessage());
            mue.printStackTrace();
        } catch (IOException ioe) {
            textView.setText("IOException:" + ioe.getMessage());
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
                textView.setText("IOException:" + ioe.getMessage());
            }
        }
    }

    private class MyAsycnTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                url = new URL("https://www.youtube.com/watch?v=PT2_F-1esPk");
            } catch (MalformedURLException e) {
                textView.setText("MalformedURLException:" + e.getMessage());
            }
            textView.setText("Downloading..\n" + url);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            downloadVideo();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textView.setText("Donwloaded File.mp4 in external storage");
        }
    }
}
