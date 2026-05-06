# ===== BUILD STAGE =====
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ===== RUN STAGE =====
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render usa questa porta
EXPOSE 10000

# IMPORTANTISSIMO: bind esplicito
CMD ["java", "-jar", "app.jar", "--server.port=10000", "--server.address=0.0.0.0"]