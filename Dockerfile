<<<<<<< HEAD
FROM openjdk:8-jre-alpine

EXPOSE 8080
COPY ./target/my-app-*.jar /usr/app/
WORKDIR /usr/app

CMD java -jar my-app-*.jar
=======
FROM ubuntu:20.04
RUN echo "Hello, world!"
>>>>>>> origin/feature
