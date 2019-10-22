FROM java:8

ENV PROJECT_NAME=configserver

WORKDIR /usr/local/${PROJECT_NAME}/
COPY target/${PROJECT_NAME}.jar /usr/local/${PROJECT_NAME}/${PROJECT_NAME}.jar

VOLUME /tmp/default
EXPOSE 8888


ENTRYPOINT "java"  "-jar"  "./${PROJECT_NAME}.jar"