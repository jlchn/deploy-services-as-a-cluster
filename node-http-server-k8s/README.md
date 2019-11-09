
## using NodePort

```bash
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

curl http://192.168.99.100:30680 to access node-http-server-1

```

## using Ingress

todo

## other useful commands

### cluster and node

```bash
kubectl cluster-info # check cluster info
kubectl get nodes # list cluster nodes
kubectl get nodes -l gpu=true   # list node with label
kubectl describe node $NODE_NAME # detail information of a node
kubectl label node minikube-node gpu=true # to label a node
```

### namespace

```bash
kubectl get ns
kubectl get pod --namespace=default # list pod with namespace name
kubectl create -f ns_custom.yml # create a namespace using yml file
kubectl create namespace custom-namespace  # create a namespace using raw command

```

### pod

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

### delete a deployment or replica set
```bash
kubectl get rs
kubectl delete rs rc-node-backend-4
kubectl get deployment
kubectl delete deployment rc-node-backend-4
```

### services

```bash
kubectl get services # get all services in the current namespace
kubectl get svc      # get all services in the current namespace

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

### replication controller and ReplicaSet

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
kubectl exec -it http-server-1-866c7bb467 bash  # run bash inside of pod container

```

### start minikube inside of China network

```bash
export HTTP_PROXY=
export HTTPS_PROXY=
export NO_PROXY=localhost,127.0.0.1,10.96.0.0/12,192.168.99.0/24,192.168.39.0/24
minikube start --image-repository=registry.cn-hangzhou.aliyuncs.com/jianglichn
```
