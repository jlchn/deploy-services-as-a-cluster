FROM java:8

ENV PROJECT_NAME=demoservice2

WORKDIR /usr/local/${PROJECT_NAME}/
COPY target/${PROJECT_NAME}.jar /usr/local/${PROJECT_NAME}/${PROJECT_NAME}.jar

VOLUME /tmp/default
EXPOSE 8001


ENTRYPOINT "java"  "-jar"  "./${PROJECT_NAME}.jar"