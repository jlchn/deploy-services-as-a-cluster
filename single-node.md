
**Docker Compose can manage multi-container applications on a single machine only**

## structure of compose file

- version - the version of the Compose file 
- services - specifies the services
  - build
  - image
  - ports
  - container_name: do not set it if you want to scale 
  - depends_on
  - environment
- networks - define the network for services.
- volumes -  define the volumes used by services
- configs - add external configuration to containers

## commands

start the cluster

``` shell
docker-compose -f ./single-node.yml up -d --remove-orphans
```

scale the service

the new scaled container has the same hostname as the old one, there would be problems when both containers register to eureka.

``` shell
docker-compose -f ./single-node.yml up -d --remove-orphans --scale demoservice2=2
```