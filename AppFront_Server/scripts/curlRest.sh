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
            [DATA]               : The HTTP-Data
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
-d "${DATA}" \
"${URL}"
