FROM openjdk:8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8801
EXPOSE 9671
ENTRYPOINT ["java","-jar","/app.jar"]