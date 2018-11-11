FROM openjdk:11.0.1-jdk-slim-sid

ADD target/jsonplaceholder-web.jar /jsonplaceholder.jar
EXPOSE 8080

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/jsonplaceholder.jar"]
