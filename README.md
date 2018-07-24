Table of Contents
=================

* [About this project](#about-this-project)
* [Pre-requisites](#pre-requisites)
  * [Setup target project(s) locally](#setup-target-projects-locally)
    * [Setup sbr-control-api](#setup-sbr-control-api)
    * [Setup address-index-api, address-index-demo-ui, address-index-data](#setup-address-index-api-address-index-demo-ui-address-index-data)
  * [Setup Gatling](#setup-gatling)
  * [OS Tuning](#os-tuning)
* [Usage](#usage)
* [Running gatling tests from a docker container](#running-gatling-tests-from-a-docker-container)
* [Configuration](#configuration)
* [License](#license)

# About this project

This project is a repository of Gatling based performance tests targeted at individual endpoints in sbr-control-api.

# Pre-requisites

## Setup target project(s) locally

It is assumed that you have setup the target project locally. See below for links to project supported.

### Setup sbr-control-api

It is assumed that you have followed the guide to setup sbr-control-api (See sbr-control-api's [Development Setup (MacOS)](https://github.com/ONSdigital/sbr-control-api#development-setup-macos) )

### Setup address-index-api, address-index-demo-ui, address-index-data

Head over to the [following](https://collaborate2.ons.gov.uk/confluence/display/RAI/Setting+up+Address+Index+Server+and+UI+with+local+ElasticSearch) page on Confluence.

## Setup Gatling

As a part of setting up [sbr-control-api](#setup-sbr-control-api) or [address-index-api](#setup-address-index-api-address-index-demo-ui-address-index-data), you will have all the pre-requisites in place to run the gatling simulations in this project.

Setup involves:

1. Downloading the zip bundle from [gatling.io](https://gatling.io/download/) and,
1. Setting the `GATLING_HOME` environment variable depending on where the above bundle has been unzipped.

## OS Tuning

In order to subject any kind of meaningful load, OS tuning must be done in order to raise or un-restrict resource consumption limits imposed by consumer oriented OSes.

On macOS, the constraints most relevant to Gatling load tests include:

1. The maximum number of open files (`sysctl kern.maxfiles` )
1. The maximum number of open files per process (`sysctl kern.maxfilesperproc` )
1. The maximum number of open processes
1. The maximum number of open ports ('ephemeral port limit')
1. The maximum time before a TCP socket can be re-used

Instructions on altering all of the above limits is detailed in [macOS Tuning for Gatling](macOS%20Tuning%20for%20Gatling.md)

# Usage

The gatling simulations are executed using the official gatling SBT plugin. Sample usages:

To simply run *all* the simulation(s):
`JAVA_OPTS="-DconfigName=ai-api-addresses-mock-data -DCONCURRENT_USERS=101" sbt "gatling:test" "gatling:lastReport"`
this will run the gatling test against an endpoint defined under the key `ai-api-addresses-mock-data` in one of the configuration files under `conf` directory.

Customize the endpoint by using the `BASE_URL` property like so:
`JAVA_OPTS="-DconfigName=ai-api-addresses-mock-data -DCONCURRENT_USERS=1000 -DBASE_URL=http://localhost:9000" sbt "gatling:test" "gatling:lastReport"`

To run a specific simulation:
`JAVA_OPTS="-DconfigName=ai-api-addresses-mock-data -DCONCURRENT_USERS=101" sbt "gatling:testOnly uk.gov.ons.gatling.simulations.RegistersSimulation" "gatling:lastReport"`

A useful example is as follows, where the number of users is passed in, a specific config file is used and at the end of the simulation the reports is automatically opened:
`JAVA_OPTS="-DCONCURRENT_USERS=100 -DconfigName=ai-api-pcs-exeter" sbt "gatling:test" "gatling:lastReport"`

(The above runs the gatling simulation using the request as in ai-api-pcs.exeter.conf file and assumes that the AI server is running locally, for example, like so:

```shell
JAVA_OPTS="-Xms2g -Xmx2g -DONS_AI_API_ES_PORT=9200 -DONS_AI_API_ES_URI=localhost -DONS_AI_API_HYBRID_INDEX_HIST=hybrid-historical_811_111017_1530276985432" \
sbt "project address-index-server" "run 9001"
```

The above runs the AI server and configures it to use ElasticSearch running at `localhost:9200` with index name specified by   ONS_AI_API_HYBRID_INDEX_HIST property.
)

# Running gatling tests from a docker container

Execute the following command:

```shell
docker run --rm -it -v /Users/ashjindal/ons/gatling/registers-performance-tests:/usr/perftest-workdir --env JAVA_OPTS="-DconfigName=ai-api-simple-pcs -DCONCURRENT_USERS=2000 -DBASE_URL=http://host.docker.internal:9001"  onsdigital/jenkins-slave-sbt
```

# Configuration

The list of configurable properties is visible in the `.conf` files in `src/test/resources/uk/gov/ons/conf/gatling/conf/`

# License

Copyright © 2017, [Office for National Statistics](https://www.ons.gov.uk)

Released under MIT license, see [LICENSE](LICENSE) for details.
