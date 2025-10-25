####
# This Dockerfile is used in order to build a container that runs the Spring Boot application
#
# Build the image with:
#
# docker build -f docker/Dockerfile -t springboot/sample-demo .
#
# Then run the container using:
#
# docker run -i --rm -p 8080:8080 springboot/sample-demo
####

# Etapa 1: compilar la app
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
#RUN mvn clean package -DskipTests
RUN mvn package -Dmaven.test.skip=true


# Etapa 2: ejecutar la app
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
