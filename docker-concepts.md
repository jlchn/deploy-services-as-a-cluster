## key concepts
- image
    - image is like a template describing the running environment of your applications.
    - layer
        - what is layer? [todo]
        - when building an image, a new layer is created for each individual command in the Dockerfile.  	
- container
    - container is like a running image
    - container is actually a running process on OS
- service
    - a service consists running containers which are created from the same image.
    - a service defines how the image runs(what ports it should use, how many replicas of the container should run)
- swarm
    - enable you distribute services on multiple physical machines, it is defined as a docker cluster
    - swarm manager
    - swarm worker
- stack
    - stack is a group of services that share dependencies, and can be orchestrated and scaled together

## architecture

- client
    - user use client to send commands to server
- daemon
    - do all things clients requested
    - components
        - server: receive requests
        - engine: dispatcher
- registry
- graph: keep downloaded images
## key components

- network
- storage

### network

- host
- bridge 
- overlay
    - [the overlay network architecture](https://success.docker.com/article/ucp-service-discovery-swarm)
- macvlan

### storage

- bind
- volumes
- aufs
- backup and restore


## other references

### Linux Namespace

Docker use Linux Namespace to create a sandbox for process.

- USER (user and user group)
- PID  (process)
- UTS  (host and host name)
- IPC  (message queue, mutex, shared memory)
- MOUNT(file system mount point)
- NETWORK

### Cgroup(Control Group)
Docker use Cgroup to limit the CPU, memory, Network in specific namespaces.

### libcontainer

libcontainer is a tool composes of operations on Namespace and Cgroup.

### x509 issues

```shell
sudo vi /etc/default/docker
#add DOCKER_OPTS="$DOCKER_OPTS --insecure-registry=192.168.2.170:5000"
sudo service docker restart
```
