FROM  eed3si9n/sbt:jdk11-alpine AS builder

WORKDIR /code
COPY . /code
RUN sbt "project core" assembly

FROM openjdk:11-slim
COPY --from=builder /code/core/target/scala-**/catch-them-all.jar app.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","/app.jar"]