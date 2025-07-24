# Use Eclipse Temurin JRE 11 as base image (more secure)
FROM eclipse-temurin:11-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY target/order-management-system-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Install curl for healthcheck
RUN apk add --no-cache curl

# Add healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
