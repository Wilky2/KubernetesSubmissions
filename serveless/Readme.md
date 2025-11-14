# Here are the commands I used to install knative

## Create the cluster

```bash
k3d cluster create --api-port 6550 -p "9080:80@loadbalancer" -p "9443:443@loadbalancer" -p "8082:30080@agent:0" --agents 2 --k3s-arg "--disable=traefik@server:*"
```

## Install Flannel CNI

```bash
kubectl apply -f https://raw.githubusercontent.com/flannel-io/flannel/master/Documentation/kube-flannel.yml
```

## Install Istio

```bash
istioctl install --set profile=ambient --set values.global.platform=k3d
```

## Ingress gateway

```bash
helm install istio-ingressgateway istio/gateway -n istio-system --wait
```

## Fetch the External IP address or CNAME by running the command

```bash
kubectl --namespace istio-system get service istio-ingressgateway
```

## Install the required custom resources by running the command

```bash
kubectl apply -f https://github.com/knative/serving/releases/download/knative-v1.18.0/serving-crds.yaml
```

## Install the core components of Knative Serving by running the command

```bash
kubectl apply -f https://github.com/knative/serving/releases/download/knative-v1.18.0/serving-core.yaml
```

## The following commands install Istio and enable its Knative integration

```bash
kubectl apply -f https://github.com/knative/net-istio/releases/download/knative-v1.18.0/net-istio.yaml
```

## Verify the installation

```bash
kubectl get pods -n knative-serving
```

## Configure DNS

```bash
kubectl apply -f https://github.com/knative/serving/releases/download/knative-v1.18.0/serving-default-domain.yaml
```

##  "Hello world" Knative Service

```bash
kubectl apply -f hello.yaml
```

## List Knative Service

```bash
kubectl get ksvc
```