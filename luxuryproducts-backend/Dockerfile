FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests=true
 
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/GameWebshop-0.0.1-SNAPSHOT.jar /app/GameWebshop-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "GameWebshop-0.0.1-SNAPSHOT.jar"]