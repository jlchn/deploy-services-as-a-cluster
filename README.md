
# A demonstration to deloy service clusters(spring cloud and nodejs) to single machine, docker swarm, kubernetes, ECS and EKS

## Deployments
- [start the spring cloud cluster on a single machine
 by using docker compose](single-node.md)
- [start the spring cloud cluster using docker swarm](docker-swarm.md)
- [start the nodejs services on AWS ElasticBeanstalk](node-http-server-eb/Dockerrun.aws.json)
- start the nodejs services on AWS ECS
    - [caller service task defination](node-http-server-ecs/aws-task-defination-node-http-server-1.json)
    - [callee service task defination](node-http-server-ecs/aws-task-defination-node-http-server-2.json)
- [start the nodejs services on K8S](node-http-server-k8s/README.md)

## Knowledges under the Hook

- docker
  - [docker basic concepts](./docker-concepts.md)
  - [docker command cheatsheet](./docker-command-cheatsheet.md)
  - [docker bridge network](./docker-bridge-network.md)
  - [docker overlay network](./docker-overlay-network.md)
  - [docker service, service discovery, load banlance and ingress](https://success.docker.com/article/ucp-service-discovery-swarm)
- k8s
