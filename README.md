# Service Brokers Deep Dive

## [Cloud Foundry S3 Service Broker](s3-service-broker/README.MD)

This project is a Cloud Foundry S3 service broker using Spring Boot.

## [Spring Boot Starter Amazon S3](spring-boot-amazon-s3-master/README.MD)

Example implementation of a Spring Boot starter project that auto-configures an Amazon S3 client.

## How to clean the cloud foundry installation

```
cf delete s3-sample-app -f
cf delete-service s3-service -f
cf disable-service-access amazon-s3
cf delete s3-broker -f
cf deleted-service s3-broker -f
```

## [Recording](https://engineers.sg/v/1056)
