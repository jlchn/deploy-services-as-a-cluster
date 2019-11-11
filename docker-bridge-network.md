# bridge docker network 

- default bridge network
- how does one container communicate with the other containers inside of the same network?
- how does one container in network A communicate with containers in network B on the same host?
- how does a container talk to the host?
- how does host talk to a container?
- how does the internet access the service inside of a container?
- how does a container acccess the internet?

## default bridge network

the default bridge network is named `docker0`, you can check via below commands
```bash
ip link
ifconfig 
```
to inspect the default bridge nework, we can use docker network ls command.

```bash
docker network ls
NETWORK ID          NAME                            DRIVER              SCOPE
48c3fe0e7aea        bridge                          bridge              local
818abba1dccc        docker_gwbridge                 bridge              local
bf66d1c580dc        example-voting-app_back-tier    bridge              local
5ef3910f1fec        example-voting-app_default      bridge              local
acfe4e4557d2        example-voting-app_front-tier   bridge              local
fcf49ed594e9        host                            host                local
6plcd7z6yjlc        ingress                         overlay             swarm
```

the default bridge network is named as `bridge` in docker's perspective, to see the detail, we can

```bash
docker network inspect 48c3fe0e7aea

[
    {
        "Name": "bridge",
        "Id": "48c3fe0e7aea5f05e948932072904c6f6455fc2687b0cefce975ff5ffeb97f30",
        "Created": "2019-10-25T10:08:35.657692145+08:00",
        "Scope": "local",
        "Driver": "bridge",
        "EnableIPv6": false,
        "IPAM": {
            "Driver": "default",
            "Options": null,
            "Config": [
                {
                    "Subnet": "172.17.0.0/16",
                    "Gateway": "172.17.0.1"
                }
            ]
        },
        "Internal": false,
        "Attachable": false,
        "Ingress": false,
        "ConfigFrom": {
            "Network": ""
        },
        "ConfigOnly": false,
        "Containers": {
            "0d476d4d757bdd37cfb32d33eaf3a2262b36211aa863d72a8c3c7afc2ded4e91": {
                "Name": "suspicious_knuth",
                "EndpointID": "ca9c3b9de40887e23e7eecdcf579fd6935baff7ebc52400187e6fa2d9c2f0f01",
                "MacAddress": "02:42:ac:11:00:03",
                "IPv4Address": "172.17.0.3/16",
                "IPv6Address": ""
            },
            "d4f45a0070950ea9ba483c508497c00bdf33c7ea20d03655ac280a4f91f8dcb6": {
                "Name": "elastic_kepler",
                "EndpointID": "6009f7c0d72ed445a462a57a69f5ed9b9f502e443324206887483ab2ffdd8cf1",
                "MacAddress": "02:42:ac:11:00:02",
                "IPv4Address": "172.17.0.2/16",
                "IPv6Address": ""
            }
        },
        "Options": {
            "com.docker.network.bridge.default_bridge": "true",
            "com.docker.network.bridge.enable_icc": "true",
            "com.docker.network.bridge.enable_ip_masquerade": "true",
            "com.docker.network.bridge.host_binding_ipv4": "0.0.0.0",
            "com.docker.network.bridge.name": "docker0",
            "com.docker.network.driver.mtu": "1500"
        },
        "Labels": {}
    }
]


```
- `"com.docker.network.bridge.name": "docker0"` indicates the bridge name on the host.
- `"com.docker.network.bridge.enable_ip_masquerade": "true"` is used to enable NAT for this network so that the containers inside the network can talk to the internet.
- from the `Containers` section, we can see there are 2 containers live in this network.
- from the `IPAM` section, we know the subnet of the network is `172.17.0.0/16` and the gateway is `172.17.0.1`

## how does one container communicate with the other containers inside of the same network?
the bridge network acts as a device to allow docker containers which are connected to this device to talk to each other. 

if we catpure packets flow on `docker0` device, we can see below outputs:
``` bash
sudo tcpdump -i docker0 -n
14:59:37.133163 IP 172.17.0.3 > 172.17.0.2: ICMP echo request, id 27, seq 1, length 64
14:59:37.133177 IP 172.17.0.2 > 172.17.0.3: ICMP echo reply, id 27, seq 1, length 64
14:59:38.145739 IP 172.17.0.3 > 172.17.0.2: ICMP echo request, id 27, seq 2, length 64
14:59:38.145785 IP 172.17.0.2 > 172.17.0.3: ICMP echo reply, id 27, seq 2, length 64

```

it tells us
- containers in the same network can talk to each other
- containers in the same network talk to each other via this bridge network.

so what is being used to support such commnication?

actually, there is a veth pair link the docker container with the bridge device, we can use below commands to see interfaces which are connected to the default bridge network.

``` bash
brctl show
bridge name	bridge id		STP enabled	    interfaces	
docker0		8000.02420edbff73	no		    veth35902d2
							    veth6a20c4e
                                            
```
or we can use ip link command:
```bash
ip link | grep docker0

5: docker0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP mode DEFAULT group default 
166: veth35902d2@if165: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master docker0 state UP mode DEFAULT group default 
180: veth6a20c4e@if179: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master docker0 state UP mode DEFAULT group default
````
as you can see from the output, master of `veth35902d2@if165` and `veth6a20c4e@if179` are both `docker`.

the `@if165` and `@if179` indicates the other end of the veth pair. `veth35902d2@if165` means the interface `veth35902d2` on `docker0` device links to interface with id `165`, `veth6a20c4e@if179` means the interface `veth6a20c4e` on `docker0` device links to interface with id `179`. we cannot find interface 165 or 179 on docker host, because in different network namespace.

in order to verify, we login to one docker container and run `ip link` command
```bash

ip link # inside of docker

1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN mode DEFAULT group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
179: eth0@if180: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP mode DEFAULT group default 
    link/ether 02:42:ac:11:00:03 brd ff:ff:ff:ff:ff:ff link-netnsid 0
```
as you can see, there is an interface with id 179, and the name os eth0, it links with interface 180, which is the one we have seen on docker host. with the help of veth pairs, the containers can talk to each other via bridge device within the same network.

> one thing to mention is for each pair of veth, only one end has IP. and in the docker case, the end inside of docker containers have IPs.

## How does one container in network A communicate with containers in network B on the same host?

containers can only communicate within networks but not across networks, in order to achieved the goal we have 2 ways to workaround:
- connect one container into the other network
- ceate a third network and plug both containers into this network.

```bash
docker network create n1
ocker run -d --net n1 -e HTTP_PORT=1180 -e HTTPS_PORT=1443 -p 2180:1180 -p 2443:1443 -d praqma/network-multitool:latest
docker network connect bridge 5cbd0305ff # connect to the default bridge.

```

if we check ips inside of docker, we can see there are 2 interfaces plugined into this docker container, and it can communicate to containers in both networks.

```bash
ip addr #inside of docker
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
186: eth0@if187: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:1b:00:02 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.27.0.2/16 brd 172.27.255.255 scope global eth0
       valid_lft forever preferred_lft forever
188: eth1@if189: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:11:00:04 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.17.0.4/16 brd 172.17.255.255 scope global eth1
       valid_lft forever preferred_lft forever

ip route
default via 172.17.0.1 dev eth1 
172.17.0.0/16 dev eth1 proto kernel scope link src 172.17.0.4 
172.27.0.0/16 dev eth0 proto kernel scope link src 172.27.0.2

```

be careful even though container C1 and C2 are in the same network A, since C2 has additional network B(172.27.0.0/16), C1 cannot talk to C2 with IP 172.27.0.4 because there is no route in C1's host file to tell C1 where to find network B.

## how does a container talk to the host?

A container can talk to host by using the gateway IP of container networks, which is the IP of `docker0` device.

```bash
ip addr show docker0       
5: docker0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:0e:db:ff:73 brd ff:ff:ff:ff:ff:ff
    inet 172.17.0.1/16 brd 172.17.255.255 scope global docker0
       valid_lft forever preferred_lft forever
    inet6 fe80::42:eff:fedb:ff73/64 scope link 
       valid_lft forever preferred_lft forever
```

```bash
curl http://172.17.0.1:1180
Praqma Network MultiTool (with NGINX) - 526345b0c515 - 172.17.0.2/16
```

## how does host talk to a container?

if we see route table on the host, we can know the host can talk to containers directly by using container's internal IP.

```bash
ip route 
default via 172.26.143.254 dev eno1 proto dhcp metric 100 
172.17.0.0/16 dev docker0 proto kernel scope link src 172.17.0.1 
172.26.143.0/24 dev eno1 proto kernel scope link src 172.26.143.21 metric 100 

ping 172.17.0.2
PING 172.17.0.2 (172.17.0.2) 56(84) bytes of data.
64 bytes from 172.17.0.2: icmp_seq=1 ttl=64 time=0.052 ms
64 bytes from 172.17.0.2: icmp_seq=2 ttl=64 time=0.083 ms
64 bytes from 172.17.0.2: icmp_seq=3 ttl=64 time=0.200 ms
64 bytes from 172.17.0.2: icmp_seq=4 ttl=64 time=0.077 ms
```

we can also access Nginx service inside of container on host using 172.0.0.1(or host IP).

```bash
curl http://127.0.0.1:1180/
Praqma Network MultiTool (with NGINX) - 526345b0c515 - 172.17.0.2/16
```
this is done by port forwarding using iptable, the below iptable rule forward all requests to host port 1443 to container address 172.17.0.2:1180

```bash

sudo iptables --list -t nat
...
Chain DOCKER (1 references)      
DNAT       tcp  --  anywhere             anywhere             tcp dpt:1180 to:172.17.0.2:1180
...
```

## how does the internet access the service inside of a container?
it is the same process like what happens when talking about `how does host talk to a container`.

## how does a container acccess the internet?

it is done by NAT, the MASQUERADE rule of iptables will replace IPs of source ip(172.17.0.0/16) with it's own IP, and then forward the request to the internet, the internet sees NAT server's IP.

when NAT server receives the response, it forward the reponse to container according to port forwarding.

```bash
target     prot opt source               destination         
MASQUERADE  all  --  172.17.0.0/16        anywhere 
```

