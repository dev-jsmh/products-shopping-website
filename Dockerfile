# get maven to build the application
FROM maven:3.8.1-openjdk-17 AS builder

WORKDIR /app

# copy the pom file and download dependencies first (optimizes caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# copy source code and build the application
COPY src ./src
RUN mvn clean install -DskipTests

# stage 2: run the application
From eclipse-temurin:17-jre-alpine

# create a new user with no root privileges
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# copy only compiled app from builder stage
COPY --from=builder /app/target/*.jar app.jar

# expose the spring boot port
EXPOSE 8080

#execute the application
ENTRYPOINT [ "java", "-jar", "app.jar" ]