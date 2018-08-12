#!/bin/bash

echo "*********************************************"
echo "INFO:  START geocodeDecodedLocations.sh"
echo "*********************************************"

NAME_NODE=hdfs://localhost:8020
EXEC_DIR=/media/sf_Git-Repo/twitter-streaming/src/main/oozie/src  #${NAME_NODE}/projects/twitter-streaming/exec
DECODED_FILE=${EXEC_DIR}/decoded_locations.txt
GEOLOC_FILE=${EXEC_DIR}/geolocations.txt
echo "INFO:  Exec Directory: ${EXEC_DIR} "

cat ${DECODED_FILE} | while read location; do
        location=${location// /"%20"}
		echo "INFO:  Geocoding for $location "
        result_json=`curl https://nominatim.openstreetmap.org/search/${location}?format=json&addressdetails=1&limit=1`
        geoloc=`echo $result_json | jq '.[0]."lat", .[0]."lon"'`
        geoloc=`echo $geoloc | sed 's/\n/ /g; s/\"//g; s/ /,/g'`
        echo $geoloc >> ${GEOLOC_FILE}
done 

echo "*********************************************"
echo "INFO:  END geocodeDecodedLocations.sh"
echo "*********************************************"
