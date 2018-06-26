package com.mqtttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mqtttest.mqtt.MqttHelper;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by innofied on 26/6/18.
 */

public class TestActivity extends AppCompatActivity {
    MqttHelper mqttHelper;
    Button connect, publish, subscribe, disconnect;
    EditText subTopic, pubTopic, pubMessage, server, clientId, port, username, password;
    TextView subMessagetxt;
    private MqttCallback mqttCallback;
    private IMqttActionListener iMqttActionListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mqttHelper = MqttHelper.iniatialize(this);
        handleCallbacks();
        mqttHelper.setCallbacks(mqttCallback, iMqttActionListener);

        subMessagetxt = findViewById(R.id.Subscribe_msg_txt);
        pubMessage = findViewById(R.id.publish_message_Text);
        server = findViewById(R.id.server_txt);
        port = findViewById(R.id.port_txt);
        clientId = findViewById(R.id.clientId_txt);
        username = findViewById(R.id.username_Text);
        password = findViewById(R.id.password_Text);
        subTopic = findViewById(R.id.Subscribe_topic_Text);
        pubTopic = findViewById(R.id.topic_Text);
        connect = findViewById(R.id.connect_btn);
        publish = findViewById(R.id.publish_btn);
        subscribe = findViewById(R.id.subscribe_btn);
        disconnect = findViewById(R.id.disconnect_btn);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttHelper.connect(server.getText().toString(), port.getText().toString(), port.getText().toString(), username.getText().toString(), password.getText().toString());
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mqttHelper.isConnected())
                    mqttHelper.publish(pubMessage.getText().toString().trim(), pubTopic.getText().toString().trim());
            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mqttHelper.isConnected())
                    mqttHelper.subscribe(subTopic.getText().toString().trim());
            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mqttHelper.isConnected())
                    mqttHelper.disconnect();
            }
        });
    }

    private void handleCallbacks() {
        mqttCallback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                Toast.makeText(TestActivity.this, "connectionLost", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                Toast.makeText(TestActivity.this, "messageArrived", Toast.LENGTH_SHORT).show();
                subMessagetxt.setText("Topic-> "+s+" Msg--> "+mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Toast.makeText(TestActivity.this, "deliveryComplete", Toast.LENGTH_SHORT).show();
            }
        };
        iMqttActionListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Toast.makeText(TestActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Toast.makeText(TestActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
            }
        };
    }

}
