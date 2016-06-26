#AMQP Client

##Overview

This application is a test harness for sending or publishing simple messages to 
[RabbitMQ](https://www.rabbitmq.com/) via [Spring-AMQP](https://projects.spring.io/spring-amqp).

After cloning this repo update the `src/main/resources/application.properties`:

* Update the connection information for connecting locally or to a remote RabbitMQ VHost

* Update the name of the Direct and Topic Exchanages

Build and launch the project:

* `mvn clean install`

* `java -jar ./target/amqp-client-1.0.0.jar`

##Usage

Update _payload_, _URL_, and _routingkey_

> Underscores '_' in the Routing Key will be replaced with Periods '.'

Send message:

    curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X POST -d '{"order":1125, "customer":"Houdini", "item":"Card Deck", "amount":6}' http://localhost:9999/amqp/direct/routingkey



Publish Message:

    curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X POST -d '{"event":7826, "magician":"Teller", "location":"Las Vegas", "Date":"2016-12-25T20:00:000Z"}' http://localhost:9999/amqp/topic/routingkey