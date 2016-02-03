#!/bin/bash

#Get variables
METHOD=$1
CONTENT_TYPE=$2
URL=$3
DATA=$4

function usage {
    cat<<EOF
        usage: 
            ${0} <METHOD> <CONTENT_TYPE> <URL> [DATA]

        Arguments:
            <METHOD>             : The HTTP-Method to use
            <CONTENT_TYPE>       : The HTTP-Content type
            <URL>                : The URL to query
        optional Arguments:
<<<<<<< HEAD
            [DATA]               : File containing the HTTP-Data
=======
            [DATA]               : The HTTP-Data
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
EOF
    exit -1
}


if  [ ! "${METHOD}" ] \
    || [ ! "${CONTENT_TYPE}" ] \
    || [ ! "${URL}" ]; then
    usage
fi

curl \
-X "${METHOD}" \
-H "Content-Type:${CONTENT_TYPE}" \
<<<<<<< HEAD
-d "@${DATA}" \
=======
-d "${DATA}" \
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
"${URL}"
