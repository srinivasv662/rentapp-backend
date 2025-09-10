# ---------- Build Stage ----------
# Use Java 17 JDK to build the app
FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /app

# Copy Maven wrapper + pom.xml (helps caching dependencies)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download all dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the Spring Boot JAR
RUN ./mvnw clean package -DskipTests


# ---------- Run Stage ----------
# Use lightweight Java 17 JRE for runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (Render sets $PORT automatically)
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "app.jar"]
