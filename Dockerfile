FROM gradle:8.12.1-jdk21 AS builder

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

FROM openjdk:21-jdk-slim AS runtime

COPY --from=builder /app/build/libs/*.jar /pinback.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/pinback.jar"]