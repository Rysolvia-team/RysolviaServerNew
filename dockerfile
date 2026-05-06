# Usa Java 21
FROM eclipse-temurin:21-jdk

# Cartella di lavoro
WORKDIR /app

# Copia tutto il progetto
COPY . .

# Rende eseguibile Maven wrapper (se presente)
RUN chmod +x mvnw || true

# Build applicazione
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Avvio app
CMD ["java", "-jar", "target/rysolvia-0.0.1-SNAPSHOT.jar"]