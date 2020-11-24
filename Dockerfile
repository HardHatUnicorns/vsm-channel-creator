FROM openjdk:11-jre-slim-buster
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} vsm-channel-creator.jar
ENTRYPOINT ["java","-jar","/vsm-channel-creator.jar"]