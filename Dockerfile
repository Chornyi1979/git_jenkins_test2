FROM openjdk:8-jre-alpine

EXPOSE 80
COPY ./target/my-app-*.jar /usr/app/
WORKDIR /usr/app

CMD java -jar -Dserver.port=80 my-app-*.jar


