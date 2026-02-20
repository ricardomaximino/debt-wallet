# Build stage
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

# Install dependencies needed for mvnw (if any, typically none for sh)
RUN apk add --no-cache binutils

# Copy maven wrapper first for better caching
COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml

# Ensure mvnw has execution permissions
RUN chmod +x mvnw

# Download dependencies (only using pom.xml and .mvn)
RUN ./mvnw dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN ./mvnw package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy jar from build stage
COPY --from=build /app/target/debit-wallet-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
