#!/usr/sh

scp -r ./Resource/target/site/apidocs/ login-svr1:/ebi/production/panda/steinbeck/www/chemet/resource
scp -r ./interface/target/site/apidocs/ login-svr1:/ebi/production/panda/steinbeck/www/chemet/interface
scp -r ./CheMet-Core/target/site/apidocs/ login-svr1:/ebi/production/panda/steinbeck/www/chemet/core
scp -r ./CheMet-IO/target/site/apidocs/ login-svr1:/ebi/production/panda/steinbeck/www/chemet/io
scp -r ./CheMet-WebServices/target/site/apidocs/ login-svr1:/ebi/production/panda/steinbeck/www/chemet/webservices
scp -r ./annotation/target/site/apidocs/ login-svr1:/ebi/production/panda/steinbeck/www/chemet/annotation
scp -r ./observation/target/site/apidocs/ login-svr1:/ebi/production/panda/steinbeck/www/chemet/observation

# /service
scp -r ./service/service-core/target/site/apidocs/ login-svr1:/ebi/production/panda/steinbeck/www/chemet/service/service-core
scp -r ./service/lucene-service/target/site/apidocs/ login-svr1:/ebi/production/panda/steinbeck/www/chemet/service/lucene-service

