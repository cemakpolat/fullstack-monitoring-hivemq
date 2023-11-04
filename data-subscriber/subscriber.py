import time
import logging as log
import paho.mqtt.client as mqtt
import os

log.info("Subscriber is started...")
broker_host = os.environ.get('MQTT_BROKER_HOST')
topic = os.environ.get('MQTT_TOPIC') +"/#"
broker_port = 1883

def on_connect(client, userdata, flags, rc):
    log.info(f"Connected: {rc}")
    client.subscribe(topic)

def on_message(client, userdata, msg):
    log.info(f"Received message: {msg.payload.decode()}")
    print(f"Received message: {msg.payload.decode()}")

client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

client.connect(broker_host, broker_port)
client.loop_forever()

