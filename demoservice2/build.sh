#!/usr/bin/env bash

#colors
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

#vars
PROJECT_NAME="demoservice2"
DOCKER_NAME="jianglichn/${PROJECT_NAME}"
DOCKER_TAG=$(date +%Y%m%d.%H%m)

########################
#build project
########################

mvn clean package -Dmaven.test.skip=true

########################
#build docker
########################

docker build -t ${DOCKER_NAME}:${DOCKER_TAG} .

########################
#push docker
########################

docker push ${DOCKER_NAME}:${DOCKER_TAG}


########################
#run docker
########################

echo -e  "run this docker: ${GREEN}sudo docker run -d -p 8001:8001 --net=\"host\" ${DOCKER_NAME}:${DOCKER_TAG}"

exit 0
