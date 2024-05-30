# simple-java-c8-application

Simple Camunda 8 Project which uses Kotlin and the "zeebe-client-java" for C8/Zeebe. 

Env File is needed with following contents. 

```
ZEEBE_ADDRESS='[Your Credentials]'
ZEEBE_CLIENT_ID='[Your Credentials]'
ZEEBE_CLIENT_SECRET='[Your Credentials]'
ZEEBE_AUTHORIZATION_SERVER_URL='https://login.cloud.camunda.io/oauth/token'
ZEEBE_REST_ADDRESS='[Your Credentials]'
ZEEBE_GRPC_ADDRESS='[Your Credentials]'
ZEEBE_TOKEN_AUDIENCE='zeebe.camunda.io'
```

Those can be downloaded when creating new client credentials in C8 SAAS for the cluster. 
