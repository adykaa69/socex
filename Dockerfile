FROM openjdk:21
COPY ./target/social-extractor-*.jar socex-backend.jar
EXPOSE 8080
ENV CONFIG_ENV=prod
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${CONFIG_ENV}","socex-backend.jar"]
