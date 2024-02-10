FROM openjdk:21
WORKDIR /stockPricesApp
COPY build/libs/web_flux_client-0.0.2.jar web_flux_client-0.0.2.jar
ENTRYPOINT ["java", "-jar", "web_flux_client-0.0.2.jar"]
