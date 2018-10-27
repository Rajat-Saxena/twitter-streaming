#!/bin/bash

echo "*********************************************"
echo "INFO:  START getDecodedLocations.sh"
echo "*********************************************"

EXEC_DIR=/home/cloudera/Desktop/locations
DECODED_FILE=${EXEC_DIR}/decoded_locations.txt
echo "INFO:  Exec Directory: ${EXEC_DIR} "

if [ -f ${DECODED_FILE} ] ; then
	echo "INFO:  Found ${DECODED_FILE}. Deleting."
    rm ${DECODED_FILE}
fi

for line in `cat ${EXEC_DIR}/encoded_locations.txt`; do
        line=$(echo $line| sed 's/\"//g')
        val=`echo -n $line | base64 -d`
        echo $val >> ${DECODED_FILE}
done

echo "INFO:  Decoded locations saved to ${DECODED_FILE}"

echo "*********************************************"
echo "INFO:  END getDecodedLocations.sh"
echo "*********************************************"