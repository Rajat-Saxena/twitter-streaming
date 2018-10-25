#!/bin/bash

echo "*********************************************"
echo "INFO:  START getEncodedLocations.sh"
echo "*********************************************"

EXEC_DIR=/home/cloudera/Desktop/locations
LAST_ROWKEY_FILE=${EXEC_DIR}/last_rowkey.txt
echo "INFO:  Exec Directory: ${EXEC_DIR} "

echo "INFO:  Calculate partial rowkey"
ROWKEY_DIR=${EXEC_DIR}/last_rowkey_dir

LAST_ROWKEY=`cat ${LAST_ROWKEY_FILE}`
echo "INFO:  Last rowkey: ${LAST_ROWKEY} "
DATE=`echo $LAST_ROWKEY | cut -d'_' -f1`
TIME=`echo $LAST_ROWKEY | cut -d'_' -f2`
TIME_HOUR_LST=`echo $TIME | cut -d':' -f1`
TIME_MIN_LST=`echo $TIME | cut -d':' -f2`
TIME_SEC_LST=`echo $TIME | cut -d':' -f3`

TIME_HOUR_NEW=$TIME_HOUR_LST #Handle later for changes in hour
TIME_MIN_NEW=$((TIME_MIN_LST+1))
TIME_SEC_NEW=00
ROWKEY_NEW=${DATE}_${TIME_HOUR_NEW}:${TIME_MIN_NEW}:${TIME_SEC_NEW}_0
echo "INFO:  New rowkey start: ${ROWKEY_NEW} "

TIME_HOUR_END=$TIME_HOUR_LST #Handle later for changes in hour
TIME_MIN_END=$((TIME_MIN_NEW+5))
TIME_SEC_END=00
ROWKEY_END=${DATE}_${TIME_HOUR_END}:${TIME_MIN_END}:${TIME_SEC_END}_0
echo "INFO:  New rowkey end: ${ROWKEY_END} "

echo "INFO:  Fetch locations from HBase in Json format"
curl -X GET localhost:8070/twitter-streaming-tbl/${ROWKEY_NEW},${ROWKEY_END}/tweet-data:location -H "Accept: application/json" > ${EXEC_DIR}/location_tmp.json

echo "INFO:  Filter out encoded locations from Json file"
jq '.Row[].Cell[]."$"' ${EXEC_DIR}/location_tmp.json > ${EXEC_DIR}/encoded_locations.txt

echo "INFO:  Store the last rowkey"
LAST_KEY=`jq '.Row[-1].key' ${EXEC_DIR}/location_tmp.json`
LAST_KEY=$(echo $LAST_KEY| sed 's/\"//g')
WRITE_KEY=`echo -n $LAST_KEY | base64 -d`
echo "INFO:  Writing the last rowkey as $WRITE_KEY "
echo ${WRITE_KEY} > ${LAST_ROWKEY_FILE}

echo "INFO:  Remove temp file"
rm ${EXEC_DIR}/location_tmp.json

echo "*********************************************"
echo "INFO:  END getEncodedLocations.sh"
echo "*********************************************"
