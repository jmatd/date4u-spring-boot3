FROM eclipse-temurin:17-alpine
USER root
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY ./fs /root/fs
ENTRYPOINT ["java","-jar","/app.jar"]