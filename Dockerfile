FROM openjdk:21
WORKDIR /stockPricesApp
CMD ["./gradlew", "clean", "bootJar"]
COPY build/libs/*.jar app.jar
EXPOSE 8050
