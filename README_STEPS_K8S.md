=================================
===== RUNNING FOOBAR ON K8S =====
=================================

```bash
make minikube-start
make k8s-namespace
make kubectl-set-ns

make helm-add-repos

make k8s-deploy-deps
make k8s-init-create-db
make k8s-init-create-topics

make clean \
  libs/opentelemetry-javaagent-1.32.0.jar \
  build-proto \
  build

# do not forget to open port 5000 if you have a firewall.
make docker-image
make k8s-deploy 

make k8s-exec-maker-cli
make k8s-port-forward-kibana
make k8s-port-forward-grafana # User: admin pass: .

# FOOBAR_REPLICAS=4 make k8s-deploy-loader
```
