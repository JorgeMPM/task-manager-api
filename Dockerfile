# Stage 1: Build. Crea una etapa llamada build con FROM maven:3.9-eclipse-temurin-17 AS build,
# WORKDIR /app, copia pom.xml y la carpeta src a /app y ejecuta mvn clean package -DskipTests — generado por IA
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn clean package -DskipTests

# Stage 2: Run. Crea una etapa con FROM eclipse-temurin:17-jre-alpine, WORKDIR /app,
# copia desde la etapa build el fichero /app/target/*.jar como app.jar, EXPOSE 8080 y
# ENTRYPOINT en formato JSON: java -Dspring.profiles.active=docker -jar app.jar — generado por IA
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
