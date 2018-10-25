#!/bin/bash

# Start Kafka Server (Broker)
cd <>
bin/kafka-server-start.sh config/zookeeper.properties

# Start Consumer
cd <>
java -jar <> consumer
