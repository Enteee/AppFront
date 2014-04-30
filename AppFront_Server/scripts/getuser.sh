#!/bin/bash
curl localhost:9200/appfront/user/_search?q=* | jq '.'
