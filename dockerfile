# ===== BUILD STAGE =====
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copia tutto il progetto
COPY . .

# Build jar
RUN mvn clean package -DskipTests

# ===== RUN STAGE =====
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia il jar dal build stage
COPY --from=build /app/target/rysolvia-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 10000

# Avvia app
CMD ["java", "-jar", "app.jar"]