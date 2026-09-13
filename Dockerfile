FROM eclipse-temurin:21-jre-jammy

# CI builds and tests this artifact before building the runtime image.
COPY api/build/libs/*.jar /pinback.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar /pinback.jar"]
