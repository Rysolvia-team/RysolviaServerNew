# =======================
# BUILD STAGE
# =======================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copia solo file necessari (build più stabile e veloce)
COPY pom.xml .
COPY src ./src

# Build ottimizzato
RUN mvn clean package -DskipTests

# =======================
# RUNTIME STAGE
# =======================
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Prende il jar generato (robusto anche se cambia versione)
COPY --from=build /app/target/*.jar app.jar

# Render port (solo documentazione container)
EXPOSE 10000

# 🔥 FIX CRITICO: binding esplicito (evita "No open ports detected")
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=10000", "--server.address=0.0.0.0"]