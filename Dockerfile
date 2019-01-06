FROM openjdk:8-jdk-alpine

COPY ./build/libs/code-flow-service-*.jar code-flow-service.jar

ENTRYPOINT ["java", "-jar", "/code-flow-service.jar"]