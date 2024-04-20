FROM eclipse-temurin:21-jre-alpine
COPY ./target/*.jar /opt/app.jar
EXPOSE 80
WORKDIR /opt
ENTRYPOINT ["java", "-jar", "app.jar"]