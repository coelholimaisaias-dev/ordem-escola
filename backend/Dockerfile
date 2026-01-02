FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw clean install -DskipTests

EXPOSE 8080
CMD ["./mvnw", "spring-boot:run"]
