#!/bin/bash
<<<<<<< HEAD
./curlRest.sh POST type/text 'localhost:8080/user/_new?recaptcha_challenge_field=aaa&recaptcha_response_field=aaa'
=======
./curlRest.sh POST 'application/json' 'localhost:8080/user/_new?recaptcha_challenge_field=aaa&recaptcha_response_field=aaa' '{"lat":47,"lon":1000}'
>>>>>>> 2100ef44a449a07da0d5ad008abf8b1211c0fb42
