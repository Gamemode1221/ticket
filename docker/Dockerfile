FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
COPY . .
#RUN sed -i 's/\r$//' gradlew
#RUN chmod +x gradlew
RUN ./gradlew clean bootJar -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

LABEL authors="Nova"
