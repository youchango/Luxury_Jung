FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY gradle /app/gradle
COPY gradlew /app/
COPY build.gradle settings.gradle /app/
RUN chmod +x gradlew
# 의존성 캐싱 유도
RUN ./gradlew classes -x test > /dev/null 2>&1 || true
COPY . /app
RUN ./gradlew bootJar -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
