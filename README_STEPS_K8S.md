Consider defining `FOOBAR_FAST_DOCKER_REGISTRY=true` to make deployments faster.

```bash
make minikube-start
make k8s-namespace
make kubectl-set-ns

make helm-add-bitnami

make k8s-deploy-deps
make k8s-init-create-db
make k8s-init-create-topics

make clean \
  libs/opentelemetry-javaagent-1.23.0.jar \
  build-proto \
  build-api-generator
ENV=no_db make build-api
make build

make docker-image
make k8s-deploy 

make k8s-exec-maker-cli
make k8s-port-forward-kibana
make k8s-port-forward-grafana # User: admin pass: .

# FOOBAR_REPLICAS=4 make k8s-deploy-loader
```
