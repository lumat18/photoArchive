FROM openjdk:11
ADD target/photoarchive.jar photoarchive.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "photoarchive.jar"]