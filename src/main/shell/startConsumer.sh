#!/bin/bash

# Start Consumer
cd /media/sf_Git-Repo/twitter-streaming/target
java -cp twitter-streaming-1.0-SNAPSHOT-jar-with-dependencies.jar com.Main consumer
