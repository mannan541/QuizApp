package com.android.quizapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.quizapp.R;
import com.android.quizapp.utils.NetworkAccessInfo;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MQTT_IOT_Activity extends AppCompatActivity {

    TextView textView;
    View layoutView;

    private ScrollView scrollView;
    private TextInputLayout urlInputWrapper;
    private TextInputLayout portInputWrapper;
    private TextInputLayout userNameInputWrapper;
    private TextInputLayout passwordInputWrapper;
    private TextInputLayout qosWrapper;
    private TextInputLayout messageInputWrapper;
    private TextInputLayout topicInputWrapper;
    AppCompatButton submitBtn;

    String brokerURL = "tcp://broker.hivemq.com";
    String port = "1883";
    String payloadMessage = "ON";
    int qos = 0;
    String topic = "a/b";
    String userName = "testUser";
    String userPassword = "abc123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt__iot_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab();

        textView = (TextView) findViewById(R.id.textview);
        layoutView = (RelativeLayout) findViewById(R.id.content_mqtt__iot_);

        urlInputWrapper = (TextInputLayout) findViewById(R.id.url_wrapper);
        portInputWrapper = (TextInputLayout) findViewById(R.id.port_wrapper);
        userNameInputWrapper = (TextInputLayout) findViewById(R.id.userName_wrapper);
        passwordInputWrapper = (TextInputLayout) findViewById(R.id.password_wrapper);
        qosWrapper = (TextInputLayout) findViewById(R.id.qos_wrapper);
        topicInputWrapper = (TextInputLayout) findViewById(R.id.topic_wrapper);
        messageInputWrapper = (TextInputLayout) findViewById(R.id.message_wrapper);
        submitBtn = (AppCompatButton) findViewById(R.id.submitButton);

        urlInputWrapper.getEditText().setText("tcp://");
        portInputWrapper.getEditText().setText("1883");
        qosWrapper.getEditText().setText("0");
        userNameInputWrapper.getEditText().setText(userName);
        passwordInputWrapper.getEditText().setText(userPassword);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTextInputs();
                if (NetworkAccessInfo.isNetworkAvailable(getApplicationContext())) {
                    mqttFunction();
                } else {
                    Snackbar.make(layoutView, "No Internet.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

    }

    private void getTextInputs() {
        if (!urlInputWrapper.getEditText().getText().toString().equals(""))
            brokerURL = urlInputWrapper.getEditText().getText().toString();
        if (!portInputWrapper.getEditText().getText().toString().equals(""))
            port = portInputWrapper.getEditText().getText().toString();
        if (!messageInputWrapper.getEditText().getText().toString().equals(""))
            payloadMessage = messageInputWrapper.getEditText().getText().toString();
        if (!qosWrapper.getEditText().getText().toString().equals(""))
            qos = Integer.parseInt(qosWrapper.getEditText().getText().toString());
        if (!topicInputWrapper.getEditText().getText().toString().equals(""))
            topic = topicInputWrapper.getEditText().getText().toString();
        if (!userNameInputWrapper.getEditText().getText().toString().equals(""))
            userName = userNameInputWrapper.getEditText().getText().toString();
        if (!passwordInputWrapper.getEditText().getText().toString().equals(""))
            userPassword = passwordInputWrapper.getEditText().getText().toString();
    }

    private void mqttFunction() {
        String clientId = MqttClient.generateClientId();
        final MqttAndroidClient client =
                new MqttAndroidClient(this.getApplicationContext(), brokerURL + ":" + port,
                        clientId);
        final MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        final byte[] payload = payloadMessage.getBytes();
        options.setWill(topic, payload, qos, false);
        options.setUserName(userName);
        options.setPassword(userPassword.toCharArray());
        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected

                    Snackbar.make(layoutView, "Connection successful.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    textView.setText("We are connected with: " + asyncActionToken);
//                    Toast.makeText(MainActivity.this, "We are connected with: " + asyncActionToken, Toast.LENGTH_SHORT).show();
                    Log.d("Tag", "onSuccess");

                    String payload = "the payload";
                    byte[] encodedPayload = new byte[0];
                    try {
                        payload = payloadMessage;
                        encodedPayload = payload.getBytes("UTF-8");
                        MqttMessage message = new MqttMessage(encodedPayload);
                        message.setRetained(true);
                        client.publish(topic, message);
                        Snackbar.make(layoutView, "Successfully Published with: \ntopic:" + topic + "\nmessage:" + message
                                + "\nuserName: " + options.getUserName(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        textView.setText("published with: \ntopic:" + topic + "\nmessage:" + message
                                + "\nuserName: " + options.getUserName());
                    } catch (UnsupportedEncodingException | MqttException e) {
                        Snackbar.make(layoutView, "UnsupportedEncodingException: " + e, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        textView.setText("UnsupportedEncodingException: " + e);
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    textView.setText("We are failed with: " + asyncActionToken + "\n" + exception);
                    Snackbar.make(layoutView, "Failed with: " + asyncActionToken, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

//                    Toast.makeText(MainActivity.this, "We are failed with: " + asyncActionToken
//                            + "\n" + "Throwable exception: " + exception, Toast.LENGTH_SHORT).show();
                    Log.d("Tag", "onFailure");

                }
            });
        } catch (MqttException e) {
            Snackbar.make(layoutView, "MqttException:" + e, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            textView.setText("MqttException: " + e);
        }

    }

    private void fab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
