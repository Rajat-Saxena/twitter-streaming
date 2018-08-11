#!/bin/bash

echo "*********************************************"
echo "INFO:  START getEncodedLocations.sh"
echo "*********************************************"

echo "INFO:  Calculate partial rowkey"
ROW_KEY=05-08-2018

echo "INFO:  Fetch locations from HBase in Json format"
curl -X GET localhost:8070/twitter-streaming-tbl/05-08-2018*/tweet-data:location -H "Accept: application/json" > location.json

echo "INFO:  Filter out encoded locations from Json file"
jq '.Row[].Cell[]."$"' location.json > encoded_locations.txt

echo "INFO:  Store the last rowkey"
jq '.Row[-1].key' location.json > last_rowkey.txt

echo "*********************************************"
echo "INFO:  END getEncodedLocations.sh"
echo "*********************************************"
