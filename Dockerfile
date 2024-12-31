FROM openjdk:17-alpine

WORKDIR /app

COPY target/ecommerce-backend-0.0.1-SNAPSHOT.jar /app/ecommerce-backend.jar

EXPOSE 8080

CMD ["java", "-jar", "ecommerce-backend.jar"]