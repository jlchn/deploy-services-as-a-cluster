
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