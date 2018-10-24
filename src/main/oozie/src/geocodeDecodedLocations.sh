#!/bin/bash

echo "*********************************************"
echo "INFO:  START geocodeDecodedLocations.sh"
echo "*********************************************"

EXEC_DIR=`pwd`
DECODED_FILE=${EXEC_DIR}/decoded_locations.txt
GEOLOC_FILE=${EXEC_DIR}/geolocations.txt
echo "INFO:  Exec Directory: ${EXEC_DIR} "

if [ -f ${GEOLOC_FILE} ] ; then
	echo "INFO:  Found ${GEOLOC_FILE}. Deleting."
    rm ${GEOLOC_FILE}
fi

cat ${DECODED_FILE} | while read location; do
        location=${location// /"%20"}
		echo "INFO:  Geocoding for $location "
        result_json=`curl -X GET "https://nominatim.openstreetmap.org/search/${location}?format=json&addressdetails=1&limit=1"`
        geoloc=`echo $result_json | jq '.[0]."address"."country"'`
		#geoloc=`echo $result_json | jq '.[0]."lat", .[0]."lon"'`
        geoloc=`echo $geoloc | sed 's/"//g'`
        echo $geoloc >> ${GEOLOC_FILE}
done 

echo "*********************************************"
echo "INFO:  END geocodeDecodedLocations.sh"
echo "*********************************************"
