FROM lordgasmic/jre14:1.0.0
WORKDIR /images
WORKDIR /app

COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
