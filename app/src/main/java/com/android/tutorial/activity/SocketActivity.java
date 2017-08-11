package com.android.tutorial.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutorial.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Socket socket;

    String message = "Connection Status: ?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab();

        editText = (EditText) findViewById(R.id.messageET);
        textView = (TextView) findViewById(R.id.connectionTV);

        @SuppressLint("WifiManagerLeak") WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.d("Mysocket", "" + ip);
        textView.setText("" + ip);

//        new MyAsyncTask(getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new Thread(new Runnable() {
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(30000);
                    socket = serverSocket.accept();

                    message = "Socket Connected.";
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            textView.setText("" + message);
                        }
                    });

                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    println("" + bufferedReader.read());
                    Log.d("Mysocket", "received: " + bufferedReader.readLine());

                    message = "" + bufferedReader.readLine();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("" + message);
                        }
                    });
                    Thread.sleep(300);
                } catch (IOException e) {
                    message = "IOException: " + e.getMessage();
//                    textView.setText("IOException: " + e.getMessage());
//                    Toast.makeText(SocketActivity.this, "IOException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        textView.setText("" + message);

    }

    public void socketSendMessageClick(View view) {
        try {

            if (socket != null) {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

                printWriter.println("" + editText.getText().toString());
                printWriter.flush();

                Log.d("Mysocket", editText.getText().toString());
            } else {
                Toast.makeText(this, "socket is null", Toast.LENGTH_SHORT).show();
                Log.d("Mysocket", "socket is null");
            }
        } catch (IOException e) {
            Log.d("Mysocket", "IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        Context context;

        MyAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    private void fab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
