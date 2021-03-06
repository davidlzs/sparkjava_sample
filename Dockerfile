FROM java:8-jre-alpine
WORKDIR /code
COPY target/gettingstarted-1.0-SNAPSHOT-jar-with-dependencies.jar /code/gettingstarted-1.0-SNAPSHOT-jar-with-dependencies.jar
CMD ["java", "-cp", "gettingstarted-1.0-SNAPSHOT-jar-with-dependencies.jar", "com.dliu.java8.BlogApplication"]
