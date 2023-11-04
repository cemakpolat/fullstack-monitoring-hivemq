#!/bin/sh

# MQTT broker information
broker_host="hivemq"
broker_port=1883

# Wait for MQTT broker to be available
until nc -z "$broker_host" "$broker_port"; do
    echo "MQTT broker is not available. Retrying..."
    sleep 5
done

echo "MQTT broker is available. Proceeding..."
