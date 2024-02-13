FROM gradle:8.4.0-jdk21-alpine AS dependency-stage
RUN apk update && apk add --no-cache gcompat libstdc++
WORKDIR /home/gradle/project
COPY build.gradle /home/gradle/project
COPY settings.gradle /home/gradle/project
COPY gradle /home/gradle/project/gradle

ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD

RUN gradle dependencies --no-daemon

FROM dependency-stage AS build

ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon --exclude-task test

FROM openjdk:21
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar /app/application.jar
CMD ["java", "-jar", "application.jar", "--spring.profiles.active=docker"]
EXPOSE 8080