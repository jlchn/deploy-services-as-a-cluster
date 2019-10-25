## docker command

``` shell
#login into docker
docker exec -it containterId/containerName /bin/bash
docker exec -it -u 0 containterId/containerName /bin/bash # login as root

#list containers
docker container ls # list running
docker container ls --all # list all
docker container ls -aq # list running in quite mode

#stop specific container
docker container stop ${HASH}/${NAME}  #gracefully 
docker container kill ${HASH}/${NAME}  #force shotdown

#stop all containers
docker stop $(docker ps -a -q) 
#remove all containers
docker rm $(docker ps -a -q) 
#delete all stopped containers
docker rm $( docker ps -q -f status=exited) #container

#list images
docker image ls

#build image
docker build -t ${DOCKER_NAME}:${DOCKER_TAG} .

#publish image
docker push ${DOCKER_NAME}:${DOCKER_TAG}

#delete specific image
docker image rm ${IMAGE_ID} 
#delete all dangling (unused) images
docker rmi $( docker images -q -f dangling=true) #image
docker rmi $(docker images -q)
#delete images that weeks ago
docker ps -a | grep 'weeks ago' | awk '{print $1}' | xargs --no-run-if-empty docker rm #image

#save docker images as tar file
docker save -o
#load the image into docker
docker load -i

```

## docker service and stack command
- [docker service commands using docker compose](single-node.md)
- [docker service and stack commands using docker swarm](docker-swarm.md)

