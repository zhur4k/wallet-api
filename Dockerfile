FROM gradle:8.11.1-jdk17 AS stage1
WORKDIR /app
COPY . /app
RUN java -version
RUN ./gradlew dependencies --no-daemon
RUN ./gradlew build --no-daemon
FROM openjdk:17-jdk-slim AS final

WORKDIR /app
COPY --from=stage1 /app/build/libs/*.jar app.jar
EXPOSE ${APP_PORT}
CMD ["java", "-jar", "app.jar"]
