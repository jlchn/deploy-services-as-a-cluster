
## using NodePort

```bash
kubectl apply -f config-node-http-server-1.yml
kubectl apply -f deploy-node-http-server-2.yml
kubectl apply -f service-node-http-server-2.yml

kubectl apply -f deploy-node-http-server-2.yml
kubectl apply -f service-node-http-server-2.yml

kubectl get svc # check service 1's port
NAME                    TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
hello-minikube          NodePort    10.99.95.116     <none>        8080:30851/TCP   14d
kubernetes              ClusterIP   10.96.0.1        <none>        443/TCP          14d
service-http-server-1   NodePort    10.102.163.235   <none>        8081:30680/TCP   6s
service-http-server-2   NodePort    10.109.36.211    <none>        8081:30190/TCP   30m


minikube ip # check host ip
192.168.99.100

curl http://192.168.99.100:30680 # to access node-http-server-1
curl http://192.168.99.100:30680/call # to access node-http-server-1 call node-http-server-2
```

## using Ingress

```bash
# follow the steps described in section `using NodePort`, then
minikube addons enable ingress
kubectl apply -f ingress-node-http-server.yml
kubectl get ingress
kubectl describe ingress ingress-node-http-server

http://jlchn.com/1/     # call node-http-server-1 from outside of the world
http://jlchn.com/1/call # make node-http-server-1 call node-http-server-2 from outside of the world
http://jlchn.com/2/     # call node-http-server-2 from outside of the world
```


## concepts and commands

### cluster and node

```bash
kubectl cluster-info # check cluster info
kubectl get nodes # list cluster nodes
kubectl get nodes -l gpu=true   # list node with label
kubectl describe node $NODE_NAME # detail information of a node
kubectl label node minikube-node gpu=true # to label a node
kubectl cordon my-node         # mark my-node as unschedulable
kubectl drain my-node          # drain my-node in preparation for maintenance
kubectl uncordon my-node       # mark my-node as schedulable
kubectl top node my-node       # show metrics for a given node

```

### namespace

```bash
kubectl get ns
kubectl get pod --namespace=default # list pod with namespace name
kubectl create -f ns_custom.yml # create a namespace using yml file
kubectl create namespace custom-namespace  # create a namespace using raw command

```

### pod

a pod is a group of containers that will always run together(withe the same lifecycle) on the same worker node and in the same Linux namespace.

- each pod is like a logical host machine with its own IP, hostname, processes, and so on.
- all containers of a pod run under the same Network and UTS namespaces, they all share the same hostname and network interfaces.
- all containers of a pod run under the same IPC namespace and can communicate through IPC.
- using Volume to allow containers of a pod to share filesystems. by default, the filesystem of each container is fully isolated from each other because container’s filesystem comes from the container image.


```bash
kubectl create -f kubia-manual.yaml -n custom-namespace # create pod in a specific namespace, or add a namespace: `custom-namespace` entry to the metadata section 
kubectl get pods # get all pods in the current namespace
kubectl get pods -o wide # get all pods in the current namespace with Node information
kubectl get pods -o yaml # get all pods in the current namespace in yaml format
kubectl get pods -o json # get all pods in the current namespace in json format

kubectl label pod node-backend version=19.11,app=demo-name # add label to pods
kubectl label pod node-backend version=19.12 --overwrite # update a label
kubectl get pods  --show-labels # get all pods in the current namespace with labels
kubectl get pod -L app,create_method  # display label values in each column(label name is column name)
kubectl get pod -l env=dev # get pods with label selectors
kubectl get pod -l env=dev,create_method=manual  # get pods with label selectors
kubectl get pod -l env!=dev  # get pods with label selectors
kubectl get pod -l env # list all pods that include the env label, whatever its value is 
kubectl get pod -l '!env' # list all pods that don't have env label. 
kubectl get pod -l 'env in (dev,staging)'
kubectl get pod -l 'env notin (dev)'

kubectl describe pod node-backend # detail of pod
kubectl get pod node-backend -o yaml # detail of pod in yaml format
kubectl get pod node-backend -o json # detail of pod in json format


kubectl delete pod node-backend # delete specific pod
kubectl delete pod node-backend -l env=dev # delete pods with specific labels
kubectl delete ns custom-namespace # delete all pods by deleting the whole namespace 
kubectl delete pod --all # delete all pods
```

### deployment
```bash
kubectl get deployment
kubectl delete deployment rc-node-backend-4
```

### service

 a Service is a resource that indicates an entry to a group of pods providing the same service. 
 
 each service has an IP address and port that never change while the service exists. clients can open connections to that IP and port, and those connections are then routed to one of the pods backing that service. this way, clients of service don’t need to know the location of pods, allowing those pods to be moved around the cluster at any time. 

 #### how does the service discovery work?

 - all pods running in the cluster are automatically configured to use k8s DNS(k8s does that by modifying /etc/resolv.conf file in each container).
 - the DNS service is inside a pod of kube-system namespace, which knows all the services running in your system.

```bash
kubectl get services # get all services in the current namespace
kubectl get svc      # get all services in the current namespace
kubectl get svc --all-namespaces

NAME                    TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
service-http-server-1   NodePort    10.102.163.235   <none>        8081:30680/TCP   16h
service-http-server-2   NodePort    10.109.36.211    <none>        8081:30190/TCP   16h

```
- 10.102.163.235 and 10.109.36.211 are service IP of service-http-server-1 and service-http-server-2
- 8081 is the port exposed by service-http-server-1 and service-http-server-2
- 30680 and 30190 are ports published on every cluster hosts. by accessing host-ip:30680 and host-ip:30190, service-http-server-1 and service-http-server-2 can be accessed from ouside of the world.

if service-http-server-1 wants to access service-http-server-2, there are two ways

```bash
 kubectl exec http-server-2-58b5b9b84c-2wvm5 -- curl -s http://10.109.36.211:8081
 kubectl exec http-server-2-58b5b9b84c-2wvm5 -- curl -s http://service-http-server-2.default:8081 # {service-name}.{namespace-name}.{configurable-domain-suffix}:{port}
```

 #### Endpoints

an Endpoints resource is a list of IP addresses and ports exposing a service. we can think of it as the ip and port pair of pods backing the services.

the Endpoints resource sites in between the service and pods, when a client connects to a service, the service proxy selects one of those IP and port pairs and redirects the incoming connection to this Endpoints.

```bash

kubectl describe service service-http-server-1 | grep Endpoints

Endpoints:                172.17.0.11:3000,172.17.0.13:3000
```


### Configmaps

```bash
kubectl apply -f config-node-http-server-1.yml # create a configmap
kubectl get configmaps config-node-http-server-1 -o yaml # describe configmaps
```

note, there are 2 ways to use configMap together with your apps:

- mount configMap as env variables.
- mount configMap to a volume, the apps read the config values from the specific path.

for the first way, the env variables won't be updated after you update the configMap, because the env variables are injected into container when pods start.

for the second way, it will take about 10 seconds for k8s to check and re-mount the volume to pods, even though, to achieve the configMap hot update, the app itself need a way to know when the changes happen. one posible solution might be inroducing a sidecar container which monitoring the config files, when there are changes, it send a signal to your app and then it loads latest configs. this also require the app having the logics to receive the specific signals.



### ReplicationController and ReplicaSet

a ReplicationController ensures its pods are always kept running with desired replicas.

a ReplicaSet is similar to a ReplicationController, but it has more powerful pod selectors.

```bash
kubectl get rc
kubectl describe rc rc-name
kubectl scale rc rc-name --replicas=3  		
kubectl edit rc rc-name # edit rc 	

kubectl describe rs rs-name
kubectl get rs
kubectl delete rs rs-name
```

### troubleshooting

```bash
kubectl exec -it http-server-1-866c7bb467-29ts5 bash            # run bash inside of pod container
kubectl exec http-server-1-866c7bb467-29ts5  -- ls /home/ubuntu/

kubectl logs http-server-1-866c7bb467-29ts5 
kubectl logs -f http-server-1-866c7bb467-29ts5 
kubectl logs http-server-1-866c7bb467-29ts5 --previous          # dump pod logs (stdout) for a previous instantiation of a container

kubectl exec -it -n kube-system nginx-ingress-controller-57bf9855c8-4kgjm cat /etc/nginx/nginx.con # check ingress-nginx configuration file

```

#### CrashLoopBackOff 

- the app inside the container keeps crashing, to know why, you can check the logs of this pods
- some type of parameters of the pod or container was configured incorrectly

### start minikube inside of China network

```bash
export HTTP_PROXY=
export HTTPS_PROXY=
export NO_PROXY=localhost,127.0.0.1,10.96.0.0/12,192.168.99.0/24,192.168.39.0/24
minikube start --image-repository=registry.cn-hangzhou.aliyuncs.com/jianglichn
```
