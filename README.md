
## a demonstration to deloy service clusters(spring cloud and nodejs) to single machine, docker swarm, kubernetes, ECS and EKS

- [start the spring cloud cluster on a single machine
 by using docker compose](single-node.md)
- [start the spring cloud cluster using docker swarm](docker-swarm.md)
- start the nodejs service on AWS ElasticBeanstalk
    - [task defination](node-http-server-1/Dockerrun.aws.json)
- start the nodejs service on AWS ECS
    - [caller service task defination](node-http-server-1/aws-task-defination.json)
    - [callee service task defination](node-http-server-2/aws-task-defination.json)