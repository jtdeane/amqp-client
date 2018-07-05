FROM openjdk:8u141-jdk-slim
MAINTAINER jeremydeane.net
EXPOSE 9999
RUN mkdir /app/
COPY target/amqp-client-1.0.2.jar /app/
ENTRYPOINT exec java $JAVA_OPTS -Damqp.hostname='amqp-broker' -jar /app/amqp-client-1.0.2.jar