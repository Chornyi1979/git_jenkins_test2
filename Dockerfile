FROM tomcat:9.0-jdk8-openjdk-slim

COPY ./target/my-app-*.war /usr/app/
WORKDIR /usr/app


