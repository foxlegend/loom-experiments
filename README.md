# Project Loom experimentations

This project tries to benchmark loom usage on several well-known frameworks:

* Quarkus,
* Spring Boot.

## Requirements

* Docker,
* A Java 21 distribution,
* K6 for load injection.

## Start the infrastructure

Each project requires: 

* a database, available in the compose.yml file,
* a third party webservice to call: third-party-werbservices,
* some services for application and infrastructure monitoring:
  * VictoriaMetrics: a timeseries database,
  * ProcessExporter: to monitor processes resources usage,
  * Grafana.

### Steps

* Build the thirdparty-rest-webservices java project
  * ./mvnw package
* Start the compose stack:
  * docker compose up -d

## Run the application

Simply package and run the application (preferably not in development mode).

### Quarkus

```shell
# PACKAGE
# - with maven
./mvnw package
# - with the Quarkus CLI
quarkus build

# RUN the application
java -jar target/quarkus-app/quarkus-run.jar #-Dquarkus.profile=200p
```

#### Profiles

Available profiles are:

* prod: default one,
* 200p: REST client connection pool is set to 200, Database connection pool is initialized and set to 200,
* 500p: REST client connection pool is set to 500, Database connection pool is initialized and set to 500.

### Spring Boot

TBD

### Helidon

TBD

## Endpoints

### /cpu/platform, /cpu/virtual

Increases a counter for a given `duration`.

Query parameters:

* `duration` (OPTIONAL): defaults to 300 ms.

### /sleep/platform, /sleep/virtual, /sleep/reactive

Performs a simple `Thread.sleep` of a given `duration`.

Query parameters:

* `duration` (OPTIONAL): defaults to 300 ms.

### /uuid/platform, /uuid/virtual, /uuid/reactive

Performs `rsCount` web service calls to the `thirdparty-rest-webservices` with a duration of `rsSleep` ms,
and `dbCount` database calls with a duration of `dbSleep` seconds.

Query parameters:

* `rsCount` (OPTIONAL): defaults to 1,
* `rsSleep` (OPTIONAL): defaults to 300ms,
* `dbCount` (OPTIONAL): defaults to 2,
* `dbSleep` (OPTIONAL): defaults to 0.025s (25ms).

## Load injection

You can use k6 for load injection: scripts are available in the `k6` directory.

The following example launches a k6 test, with 1000 VU during 2 minutes. Live metrics are sent to VictoriaMetrics.

```
K6_PROMETHEUS_RW_SERVER_URL=http://localhost:8428/api/v1/write \
K6_PROMETHEUS_RW_TREND_STATS='p(95),p(99),min,max,avg,med' \
k6 run -o experimental-prometheus-rw -u 1000 -d 2m k6/uuid-virtual.js
```