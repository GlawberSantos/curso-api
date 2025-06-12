
# Imagem base com Java 17
FROM openjdk:17-jdk-slim

# Argumento para versão da aplicação
ARG JAR_FILE=target/*.jar

# Copia o JAR para o container
COPY ${JAR_FILE} app.jar

# Define o ponto de entrada
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=docker"]