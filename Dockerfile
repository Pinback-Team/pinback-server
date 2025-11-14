FROM gradle:8.12.1-jdk21 AS builder

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jre-jammy AS runtime

COPY --from=builder /app/api/build/libs/*.jar /pinback.jar

EXPOSE 8080

ENTRYPOINT ["sh","-c","java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar /pinback.jar"]