# Here are the commands I used to install Istio and deploy the app

## Installing Istio on k3d-k3s

### Create the cluster

```bash
k3d cluster create --api-port 6550 -p "9080:80@loadbalancer" -p "9443:443@loadbalancer" -p "8082:30080@agent:0" --agents 2 --k3s-arg "--disable=traefik@server:*"
```

### Install Flannel CNI

```bash
kubectl apply -f https://raw.githubusercontent.com/flannel-io/flannel/master/Documentation/kube-flannel.yml
```

### Install Istio
```bash
istioctl install --set profile=ambient --set values.global.platform=k3d
```

### Ingress gateway (optional)

```bash
helm install istio-ingress istio/gateway -n istio-ingress --create-namespace --wait
```


## Deploy the Sample app

### Start by deploying the application

```bash
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.28/samples/bookinfo/platform/kube/bookinfo.yaml
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.28/samples/bookinfo/platform/kube/bookinfo-versions.yaml
```

### To verify that the application is running

```bash
kubectl get pods
```

### Install gateways.gateway.networking.k8s.io, if It's not install

```bash
kubectl apply -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.2.0/standard-install.yaml
```

### Deploy a gateway called bookinfo-gateway

```bash
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.28/samples/bookinfo/gateway-api/bookinfo-gateway.yaml
```

### Change the service type to ClusterIP by annotating the gateway

```bash
kubectl annotate gateway bookinfo-gateway networking.istio.io/service-type=ClusterIP --namespace=default
```

### Check the status of the gateway

```bash
kubectl get gateway
```

### Access the application

```bash
kubectl port-forward svc/bookinfo-gateway-istio 8080:80
```

Navigate to http://localhost:8080/productpage

### Add Bookinfo to the mesh

```bash
kubectl label namespace default istio.io/dataplane-mode=ambient
```

### Visualize the application and metrics

```bash
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.28/samples/addons/prometheus.yaml
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.28/samples/addons/kiali.yaml
```

#### Access kiali dashboard

```bash
istioctl dashboard kiali
```


#### Sending some traffic 

```cmd
for /l %i in (1,1,100) do curl -s -I http://localhost:8080/productpage >nul
```

```bash
for i in $(seq 1 100); do curl -sSI -o /dev/null http://localhost:8080/productpage; done
```

### Create an authorization policy that restricts which services can communicate with the productpage service
```bash
kubectl apply -f authorizationpolicy.yaml
```

### Enforce Layer 7 authorization policy

#### Create a waypoint for the proxy namespace

```bash
istioctl waypoint apply --enroll-namespace --wait
```

#### View the waypoint

```bash
kubectl get gtw waypoint
```

#### Adding a L7 authorization policy will explicitly allow the curl service to send GET requests to the productpage service, but perform no other operations

```bash
kubectl apply -f authorizationpolicyl7.yaml
```

### Split traffic between services

```bash
kubectl apply -f httproute.yaml
```

### Clean up

#### Remove waypoint proxies

```bash
kubectl label namespace default istio.io/use-waypoint-
istioctl waypoint delete --all
```

#### Remove the namespace from the ambient data plane

```bash
kubectl label namespace default istio.io/dataplane-mode-
```

#### Remove the sample application

```bash
kubectl delete -f authorizationpolicy.yaml
kubectl delete -f authorizationpolicyl7.yaml
kubectl delete -f httproute.yaml
kubectl delete -f https://raw.githubusercontent.com/istio/istio/release-1.28/samples/bookinfo/platform/kube/bookinfo.yaml
kubectl delete -f https://raw.githubusercontent.com/istio/istio/release-1.28/samples/bookinfo/platform/kube/bookinfo-versions.yaml
kubectl delete -f https://raw.githubusercontent.com/istio/istio/release-1.28/samples/bookinfo/gateway-api/bookinfo-gateway.yaml
```

#### Uninstall Istio

```bash
istioctl uninstall -y --purge
kubectl delete namespace istio-system
```


### Uninstall gateways.gateway.networking.k8s.io

```bash
kubectl delete -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.2.0/standard-install.yaml
```