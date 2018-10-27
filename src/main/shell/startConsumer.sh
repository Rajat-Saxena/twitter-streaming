#!/bin/bash

# Start Kafka Server (Broker)
cd <>
bin/kafka-server-start.sh config/zookeeper.properties

# Start Consumer
cd /media/sf_Git-Repo/twitter-streaming/target
java -cp twitter-streaming-1.0-SNAPSHOT-jar-with-dependencies.jar com.Main consumer
