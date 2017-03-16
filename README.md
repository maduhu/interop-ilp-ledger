# interop-ilp ledger
This project provides an interop API implementation of ILP Ledger Service.

Contents:

- [Deployment](#deployment)
- [Configuration](#configuration)
- [API](#api)
- [Logging](#logging)
- [Tests](#tests)

## Deployment

Project is built using Maven and uses Circle for Continous Integration.

### Installation and Setup

#### Anypoint Studio
* [https://www.mulesoft.com/platform/studio](https://www.mulesoft.com/platform/studio)
* Clone https://github.com/LevelOneProject/interop-ilp-ledger.git to local Git repository
* Import into Studio as a Maven-based Mule Project with pom.xml
* Go to Run -> Run As Configurations.  Make sure interop-spsp-ilp-ledger project is highlighted.

#### Standalone Mule ESB
* [https://developer.mulesoft.com/download-mule-esb-runtime](https://developer.mulesoft.com/download-mule-esb-runtime)
* Add the environment variable you are testing in (dev, prod, qa, etc).  Open <Mule Installation Directory>/conf/wrapper.conf and find the GC Settings section.  Here there will be a series of wrapper.java.additional.(n) properties.  create a new one after the last one where n=x (typically 14) and assign it the next number (i.e., wrapper.java.additional.15) and assign -DMULE_ENV=dev as its value (wrapper.java.additional.15=-DMULE_ENV=dev)
* Download the zipped project from Git
* Copy zipped file (Mule Archived Project) to <Mule Installation Directory>/apps

### Run Application

#### Anypoint Studio
* Run As Mule Application with Maven

#### Standalone Mule ESB
* CD to <Mule Installation Directory>/bin -> in terminal type ./mule

## Configuration

[pom.xml](./pom.xml) and [circle.yml](./circle.yml) can be found in the repo, also linked here

## API

The API provides methods for the following:
* Find an account by ID 
* Add an account by ID
* List all accounts
* Find a local transfer by id
* Request a transfer
* Excute a transfer
* Retrieve all accounts of all connectors on a ledger

Below are the APIs for reference
* RAML [here](./src/main/api/ilp-ledger-adapter.raml)
* OpenAPI [here](./src/main/resources/documentation/dist/ilp-ledger.yaml)

## Logging

Server path to logs is: <mule_home>/logs/interop-ilp-ledger.log for example: /opt/mule/mule-dfsp1/logs/interop-ilp-ledger.log

Currently the logs are operational and include information such as TraceID and other details related to the calls or transactions such as path, method used, header information and sender/receiver details.

## Tests

Java Unit Test exist for the project and include test for:

* Invalid path should return 404
* Put accounts
* Get account
* Get transfer
* Get health
* Reject transfer should get return valid response
* Get metadata
* Put transfer fulfillment
* Get connectors
* Post messages

#### Anypoint Studio
* Run Unit Tests
* Test API with Anypoint Studio in APIKit Console
* Verify Responses in Studio Console output

Tests are run as part of executing the Maven pom.xml as mvn clean package. Also, test can be run by running com.l1p.interop.spsp.ILPLedgerAdapterFunctionalTest java class as a Test.