FROM openjdk:8
EXPOSE 8099
ADD target/ecommerce-service.jar ecommerce-service.jar
ENTRYPOINT ["java", "-jar", "ecommerce-service.jar"]