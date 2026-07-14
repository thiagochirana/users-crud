FROM eclipse-temurin:26-jdk

WORKDIR /app

COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle

RUN chmod +x gradlew

EXPOSE 8080

CMD ["./gradlew", "bootRun", "--no-daemon"]
