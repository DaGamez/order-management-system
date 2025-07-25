# Use Eclipse Temurin JRE 11 as base image (more secure)
FROM eclipse-temurin:11-jre-alpine

# Set working directory
WORKDIR /app

# Install curl for healthcheck and timezone data
RUN apk add --no-cache curl tzdata

# Copy the built JAR file
COPY target/order-management-system-0.0.1-SNAPSHOT.jar app.jar

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup && \
    chown -R appuser:appgroup /app

USER appuser

# Expose port 5000 (AWS Beanstalk port)
EXPOSE 5000

# Add healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:5000/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
