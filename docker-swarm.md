
docker swarm will help to start a new container when one exits.
create a swarm cluster

```shell
docker swarm init --advertise-addr 192.168.0.109 # create a swarm manager node
docker swarm join --token TOKEN-NEEDED-HERE 192.168.0.109:2377 # run this command on other hosts to join this swarm cluster
```

check whether swarm mode is active or not

``` shell
docker info | grep Swarm
```

check the node in the cluster

``` shell
docker node ls
```

retrieve the join command

``` shell
docker swarm join-token worker # run on manager node
```

create or update a stack/cluster

```
# this will not restart the docker when the replicas show running.
# if the service's defination is not changed in the yml file, it won't be restart as well.
docker stack deploy -c docker-swarm.yml test-stack 
```

update a service 

```shell
docker service update x1jqad6yz09o --force
```

remove a stack

```
docker stack rm demo-stack-3
```

rolling upgrade a service

from shell
``` shell
# --update-parallelism: number of tasks to update at the same time.
# --update-delay: time to wait before updating the next batch of tasks.
# --update-order start-first: start the new one, then stop the old one
# --update-failure-action=rollback : enforce a rollback when an update fails instead of pause the update

docker service update yuk1nbmqf0gj  XXXXX(--images:xx)  --update-parallelism 1 --update-delay 30s --update-failure-action=rollback --update-order start-first

```
from compose file

```
    deploy:
      mode: replicated
      replicas: 2
      update_config:
        parallelism: 1
        delay: 10s
        failure_action: rollback
        order: start-first
```


rollback a service to previous version

```
docker service rollback service-name --rollback-parallelism 1 --rollback-delay 30s
```


## References

about resource reservation: https://semaphoreci.com/community/tutorials/scheduling-services-on-a-docker-swarm-mode-cluster