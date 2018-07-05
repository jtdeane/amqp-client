#AMQP Client

##Overview

This application is a test harness for sending or publishing simple messages to 
[RabbitMQ](https://www.rabbitmq.com/) via [Spring-AMQP](https://projects.spring.io/spring-amqp).

After cloning this repo update the `src/main/resources/application.properties`:

* Update the connection information for connecting locally or to a remote RabbitMQ VHost

* Update the name of the Direct and Topic Exchanages

Build and launch the project:

* `mvn clean install`

* `java -jar ./target/amqp-client-1.0.2.jar`

Check health

`http://localhost:9999/actuator/info`

##Usage

Send message:

    curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X POST -d '{"order":1125, "customer":"Houdini", "item":"Card Deck", "amount":6}' http://localhost:9999/amqp/exchange/routingkey


Publish Message:

    curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X POST -d '{"event":7826, "magician":"Teller", "location":"Las Vegas", "Date":"2016-12-25T20:00:000Z"}' http://localhost:9999/amqp/exchange/routingkey
    
##Docker

* Create Network

`docker network create amqp-network`

* Start RabbitMQ

`docker run -d -p 15672:15672 --net=amqp-network --name amqp-broker --hostname amqp-broker rabbitmq:3.6.12-management`

* Create VHost cogito

* Create User client/client w/ admin role

* Set client VHosts permissions (Read,Write,Configure): / and cogito

* Pull down Image

`docker pull jtdeane/amqp-client`

OR

* Build locally

`docker build -t amqp-client:latest .`

* Run Docker

`docker run -d -p 9999:9999 -e JAVA_OPTS='-Xmx256m -Xms128m' --net=amqp-network --hostname amqp-client amp-client:latest`     