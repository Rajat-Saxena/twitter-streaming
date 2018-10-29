#!/bin/bash

# Start Kafka Server (Broker)
cd Downloads/kafka_2.11-1.1.0/
bin/kafka-server-start.sh config/server.properties
