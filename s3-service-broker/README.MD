# Cloud Foundry S3 Service Broker

This project is a Cloud Foundry S3 service broker using Spring Boot.

## Deploy to Cloud Foundry

Create dependencies:

> cf create-service p-mysql 512mb s3-broker-db

Push to local cloud foundry pcfdev:

> cf push

Test:

> curl -H "X-Broker-API-Version: 2.9" http://admin:admin@s3-broker.local.pcfdev.io/v2/catalog


## Usage

Register the service broker:

> cf create-service-broker amazon-s3 admin admin http://s3-broker.local.pcfdev.io

Enable service access:

> cf enable-service-access amazon-s3

Create service instance:

> cf create-service amazon-s3 s3-basic s3-service

## TODO

Use Spring Cloud Foundry Service Broker Template:

> https://github.com/spring-cloud/spring-cloud-cloudfoundry-service-broker
