FROM openjdk
EXPOSE 8080
ADD build/libs/RequestQueue-0.0.1-SNAPSHOT.jar RequestQueue-0.0.1-SNAPSHOT.jar
ADD saved.json saved.json
ENTRYPOINT ["java", "-jar", "/RequestQueue-0.0.1-SNAPSHOT.jar"]