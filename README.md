# job-offer-processor - 0.0.1-SNAPSHOT

## Prerequisites

* Maven
* JDK 11
* API testing tool (ex. Postman) - * optional *

## Documentation

Swagger documentation could also be used to execute the endpoints and is available on:

http://localhost:8080/swagger-ui/#/

## Overview

On start up, the project will preload the specific arguments that are needed to fetch data. For example - locations and job
categories. They will be kept in memory for optimization reasons.

Data extraction is done as parallel as possible in order to optimize the process. New web clients are deployed on demand
for parallel operations. A maximum of 5 clients can exist at the same time. A static webclient is ran alongside.

<strong>/bin</strong> folder contains the web driver. The application will also try to find the driver in <strong>
target/classes</strong> and <strong>classpath</strong>.

## YML Configuration

All properties are mandatory

    driver:
      arguments:
       - "--headless"
       - "--disable-gpu"
       - "--window-size=1400,800"
       timeoutInSeconds: 10

driver.arguments can be used to modify the chrome driver options.

driver.timeoutInSeconds can be used to specify a timeout for page and modals loading