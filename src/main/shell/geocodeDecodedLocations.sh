#!/bin/bash

echo "*********************************************"
echo "INFO:  START geocodeDecodedLocations.sh"
echo "*********************************************"

EXEC_DIR=/home/cloudera/Desktop/locations
DECODED_FILE=${EXEC_DIR}/decoded_locations.txt
GEOLOC_FILE=${EXEC_DIR}/geolocations
echo "INFO:  Exec Directory: ${EXEC_DIR} "

cat ${DECODED_FILE} | while read location; do
        location=${location// /"%20"}
				echo "INFO:  Geocoding for $location "
        result_json=`curl -X GET "https://nominatim.openstreetmap.org/search/${location}?format=json&addressdetails=1&limit=1"`
        geoloc=`echo $result_json | jq '.[0]."address"."country_code"'`
        geoloc=`echo $geoloc | sed 's/"//g'`
        echo $geoloc >> ${GEOLOC_FILE}
done

mv ${GEOLOC_FILE} ${GEOLOC_FILE}_$(date +%d%m%Y_%H%M%S).txt

rm ${EXEC_DIR}/encoded_locations.txt
rm ${EXEC_DIR}/decoded_locations.txt

echo "*********************************************"
echo "INFO:  END geocodeDecodedLocations.sh"
echo "*********************************************"
