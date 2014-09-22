#!/bin/bash
./curlRest.sh POST 'application/json' 'localhost:8080/user/_new?recaptcha_challenge_field=aaa&recaptcha_response_field=aaa' '{"lat":47,"lon":1000}'
