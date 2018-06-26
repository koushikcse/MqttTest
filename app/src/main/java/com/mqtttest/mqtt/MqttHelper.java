package com.mqtttest.mqtt;

import android.content.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by innofied on 26/6/18.
 */

public class MqttHelper {
    private static MqttHelper mqttHelper;
    private MqttAndroidClient mqttClient;
    private Context context;
    private MqttCallback mqttCallback;
    private IMqttActionListener iMqttActionListener;

    private MqttHelper(Context context) {
        this.context = context;
    }

    public static MqttHelper iniatialize(Context context) {
        if (mqttHelper == null) {
            mqttHelper = new MqttHelper(context);
        }
        return mqttHelper;
    }

    public void setCallbacks(MqttCallback mqttCallback, IMqttActionListener iMqttActionListener) {
        this.mqttCallback = mqttCallback;
        this.iMqttActionListener = iMqttActionListener;
    }

    public void connect(String server, String port, String clientId, String username, String password) {
//        String server = "tcp://broker.hivemq.com:1883";
//        String clientId = "kusu123";
//        String username = "kusu", password = "1234";
        String uri = "tcp://" + server + ":" + port;
        mqttClient = new MqttAndroidClient(context, uri, clientId);
        mqttClient.setCallback(mqttCallback);

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        try {
            IMqttToken token = mqttClient.connect(mqttConnectOptions);
            token.setActionCallback(iMqttActionListener);
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    public void publish(String msg, String topic) {
        MqttMessage mqttMessage = new MqttMessage(msg.getBytes());
        mqttMessage.setRetained(false);
        mqttMessage.setQos(2);
        try {
            mqttClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        try {
            mqttClient.subscribe(topic, 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return mqttClient.isConnected();
    }

}
