
**Docker Compose can manage multi-container applications on a single machine only**

## commands

start the cluster

``` shell
docker-compose -f ./single-node.yml up -d --remove-orphans
```

check service logs

``` shell
docker-compose -f ./single-node.yml logs -f demoservice2 

```

scale the service

the new scaled container has the same hostname as the old one, there would be problems when both containers register to eureka.

``` shell
docker-compose -f ./single-node.yml up -d --remove-orphans --scale demoservice2=2
```

stop the cluster

```
docker-compose -f ./single-node.yml stop
```

remove the cluster from host( remove the containers entirely, with --volumes to also remove the data volume)

```
docker-compose -f ./single-node.yml stop --volumes
```


## control startup order

 - Compose always starts and stops containers in dependency order, where dependencies are determined by depends_on, links, volumes_from, and network_mode: "service:...".
 - Compose does not wait until the app inside of a container is "ready" 

 see https://docs.docker.com/compose/startup-order/ for detail.
