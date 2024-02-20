FROM gradle:8.4.0-jdk21-alpine AS build
ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD

ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon --exclude-task test

FROM openjdk:21-oracle
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar /app/application.jar
CMD ["java", "-jar", "application.jar", "--spring.profiles.active=docker"]
EXPOSE 8080