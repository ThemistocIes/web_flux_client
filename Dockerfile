FROM openjdk:21-jdk-alpine
WORKDIR /stockPricesApp
COPY build/libs/web_flux_client-0.0.2.jar app.jar
ENTRYPOINT ["java","-jar","/web_flux_client-0.0.2.jar"]
