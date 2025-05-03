FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw .
RUN chmod +x mvnw
COPY pom.xml .
RUN ./mvnw dependency:go-offline

COPY src ./src
RUN ./mvnw clean package -DskipTests
RUN cp target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]