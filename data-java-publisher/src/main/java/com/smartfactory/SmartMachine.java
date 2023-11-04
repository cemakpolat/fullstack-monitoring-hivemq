package com.smartfactory;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Random;

public class SmartMachine {

    public static void main(String[] args) throws MqttException {
        String brokerHost = System.getenv("MQTT_BROKER_HOST");
        String topic = System.getenv("MQTT_TOPIC");
        String deviceId = System.getenv("DEVICE_ID");
        if (brokerHost == null) {
            System.err.println("MQTT_BROKER_HOST environment variable not set, hivemq will be used");
            brokerHost = "hivemq";
        }
        if (topic == null){
            topic = "sensor";
        }
        if (deviceId == null){
            deviceId = "IND_DEV_002";
        }
        
        topic = topic + "/" + deviceId;
        String broker = "tcp://" + brokerHost + ":1883";
        String clientId = "SmartMachine";
        MemoryPersistence persistence = new MemoryPersistence();
        MqttClient client = null;
        try {
            client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");

            
            int qos = 0;

            while (true) {
                // Generate complex device data (replace with your own data generation logic)
                String complexData = generateComplexData(deviceId);

                System.out.println("Publishing message: "+topic+ " - " + complexData);
                MqttMessage message = new MqttMessage(complexData.getBytes());
                message.setQos(qos);

                client.publish(topic, message);
                Thread.sleep(1000); // Publish data every second
            }

        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
            client.close();
        }
    }

    private static String generateComplexData(String deviceId) {
        // Replace this with your own data generation logic
        Random random = new Random();
        int temperature = random.nextInt(40) + 10;
        int humidity = random.nextInt(70) + 30;
        int pressure = random.nextInt(1013) + 990;
        String data = String.format("{\"device_id\": \"%s\", \"temperature\": %d, \"humidity\": %d,\"pressure\": %d }", deviceId, temperature, humidity, pressure);
        return data;
    }
}
