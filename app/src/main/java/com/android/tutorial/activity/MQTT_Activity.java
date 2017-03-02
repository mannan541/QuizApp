package com.android.tutorial.activity;


import android.app.Activity;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.tutorial.R;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 * Created by Mannan on 2/24/2017.
 */

public class MQTT_Activity extends Activity { // main classımız

    public static final String BROKER_URL = "tcp://broker.hivemq.com";

// Brokerımızın ip adresini ve kulllanacağımız portu tanımladık

//Parametre olarak kullancağız.

    public static final String clientId = "android-client";
    public static final String TOPIC = "a/b";
    private MqttClient mqttClient;

//client id, topic belirledik ve mqtt kütüphanesinden nesne oluşturduk.

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private Handler handler = new Handler();

/*listview ve adapter tanımladık. adapterin amacı gelen verileri üzerinde tutacak ve listviewin ekrana basmasını sağlayacak. Handler isebir çeşit thread. Android user interface tek thread üzerinde çalışır. Siz ağa bağlanmaya çalışırken görsel ekrana yansıtılamayacak ve hata verecek. Bu çakışmayı önlemek amacıyla ağa bu handlerın içinden bağlanacağız.*/

    public class PushCallback implements MqttCallback {

        private ContextWrapper context;

        public PushCallback(ContextWrapper context) {

            this.context = context;
        }

/* Mqtt interfacesini implement ettik. Bu sayede kütüphane içindeki metotları görebileceğiz. Constructera da verimizi tutacak bir ContextWrapper koyduk.*/


        @Override
        public void connectionLost(Throwable cause) {
    /* implement ettiğimizde kütüphanedeki tüm metotlar otomatik olarak göründü. Bağlantı kopması durumunda ne yapması gerektiğini yazmamız gerek. Ama projenin sadeliği için boş bıraktım.*/
        }

        @Override
        public void messageArrived(String topic, final MqttMessage message) throws Exception {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter.add(new String(message.getPayload()));
                    adapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.listview);

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

    }

/* Klasik android metodu. adapterdeki veriyi listviewe ile ekrana yazdırdık*/

    @Override
    protected void onResume() {
        super.onResume();


        try {
            mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());

            mqttClient.setCallback(new PushCallback(this));

            mqttClient.connect();

            //Subscribe to all subtopics of homeautomation            mqttClient.subscribe(TOPIC);


        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "hata!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

/* On resume metodu program açıldığında mqtt bağlantısını da açacak. Bunun için kütüphaneden oluşturduğumuz nesneye yukarıda tanımlamış olduğumuz parametreleri gönderiyoruz. Broker ip bilgileri ve topic bilgileri gelecek. setcallback'e Pushcallbacki tanıtıyoruz. mqtt bağlantısını kuruyoruz.subscribe ediyoruz.*/

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mqttClient.disconnect(0);
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "hata!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}
