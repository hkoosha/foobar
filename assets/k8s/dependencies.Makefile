
.PHONY: remake-k8s-dependencies
remake-k8s-dependencies:
	$(EDITOR) assets/k8s/dependencies.Makefile



# ===============================================================================
# ================================= MINIKUBE ====================================
# ===============================================================================

.PHONY: minikube-start
minikube-start:
	minikube start \
		--insecure-registry host.minikube.internal:5000 \
		--addons=$(FOOBAR_MINIKUBE_ADDONS) \
		--cpus=$(FOOBAR_MINIKUBE_NUM_CPU) \
		--memory=$(FOOBAR_MINIKUBE_MEMORY) \
		--driver=$(FOOBAR_MINIKUBE_DRIVER) \
		--nodes=$(FOOBAR_MINIKUBE_NODES) \
		--wait=all
	kubectl apply -f assets/k8s/apps/metrics_server.yaml
	sleep 1s
	kubectl taint nodes minikube foobar-no-schedule=foobar-no-schedule:NoSchedule
	if [[ "$(FOOBAR_FAST_DOCKER_REGISTRY)" == "true" ]]; then \
		$(MAKE) docker-registry; \
	fi
	$(MAKE) _minikube-after-start || true
	@echo "If waiting for services timedout, it's still possible they'll come onlien soon."

.PHONY: _minikube-after-start
_minikube-after-start:
	@echo "======================================================================"
	@echo "======================================================================"
	@echo "======================================================================"
	@echo "Minikube cluster started. It is better to wait until all nodes in the cluster are ready."
	@echo "You can check the status of nodes using the app k9s."
	@echo "Your next step is probabaly running '$(MAKE) k8s-namespace kubectl-set-ns'."
	@echo "Importnat: when deploying dependencies (e.g, filebeat) some pods might fail "
	@echo "with ImagePullFailed (you can check which pods using k9s), just delete the pod"
	@echo "and it will be created again and image will be pulled properly."
	@echo ""
	@echo "waiting for all pods..."
	kubectl wait --for=condition=Ready --all=true --all-namespaces pods

.PHONY: minikube-kill
minikube-kill:
	minikube stop
	minikube delete
	docker kill foobar-registry || true
	docker rm foobar-registry || true

.PHONY: minikube-purge
minikube-purge:
	minikube delete --purge
	docker kill foobar-registry || true
	docker rm foobar-registry || true

.PHONY: minikube-check
minikube-check:
	minikube status

.PHONY: minikube-dashboard
minikube-dashboard:
	minikube dashboard

.PHONY: minikube-remove-images
minikube-remove-images:
	minikube image rm docker.io/library/customer:$(FOOBAR_DOCKER_IMAGE_VERSION)
	minikube image rm docker.io/library/seller:$(FOOBAR_DOCKER_IMAGE_VERSION)
	minikube image rm docker.io/library/warehouse:$(FOOBAR_DOCKER_IMAGE_VERSION)
	minikube image rm docker.io/library/marketplace:$(FOOBAR_DOCKER_IMAGE_VERSION)
	minikube image rm docker.io/library/marketplace_engine:$(FOOBAR_DOCKER_IMAGE_VERSION)
	minikube image rm docker.io/library/shipping:$(FOOBAR_DOCKER_IMAGE_VERSION)




# ===============================================================================
# ================================= KUBECTL =====================================
# ===============================================================================

.PHONY: kubectl-set-ns
kubectl-set-ns:
	kubectl config set-context --current --namespace=$(FOOBAR_NAMESPACE)


.PHONY: kubectl-wait-all
kubectl-wait-all:
	kubectl wait --namespace=$(FOOBAR_NAMESPACE) --for=condition=Ready --all=true pods


# ===============================================================================
# ================================== HELM =======================================
# ===============================================================================

.PHONY: helm-add-repos
helm-add-repos:
	helm repo add bitnami https://charts.bitnami.com/bitnami
	helm repo add jaegertracing https://jaegertracing.github.io/helm-charts
	helm repo update

.PHONY: helm-install-redis
helm-install-redis:
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		redis bitnami/redis

.PHONY: helm-uninstall-redis
helm-uninstall-redis:
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		redis

.PHONY: helm-install-pg
helm-install-pg:
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		pg bitnami/postgresql \
		-f ./assets/k8s/values/pg.yaml

.PHONY: helm-uninstall-pg
helm-uninstall-pg:
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		pg
	kubectl delete pvc data-pg-postgresql-0

.PHONY: helm-install-kafka
helm-install-kafka:
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		kafka bitnami/kafka \
		-f ./assets/k8s/values/kafka.yaml

.PHONY: helm-uninstall-kafka
helm-uninstall-kafka:
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		kafka
	kubectl delete pvc data-kafka-controller-0
	kubectl delete pvc data-kafka-controller-1
	kubectl delete pvc data-kafka-controller-2


.PHONY: helm-install-prometheus
helm-install-prometheus:
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		prometheus bitnami/kube-prometheus
	kubectl -n $(FOOBAR_NAMESPACE) apply -f assets/k8s/apps/monitoring/podmonitor.yaml
	kubectl -n $(FOOBAR_NAMESPACE) apply -f assets/k8s/apps/monitoring/prometheus.yaml
	kubectl -n $(FOOBAR_NAMESPACE) apply -f assets/k8s/apps/monitoring/servicemonitor.yaml

.PHONY: helm-uninstall-prometheus
helm-uninstall-prometheus:
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		prometheus


.PHONY: helm-install-jaeger
helm-install-jaeger:
	# helm install \
	#  -n $(FOOBAR_NAMESPACE) \
	#  jaeger-op jaegertracing/jaeger-operator
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		jaeger jaegertracing/jaeger \
		--values ./assets/k8s/values/jaeger.yaml
	# kubectl -n $(FOOBAR_NAMESPACE) apply -f assets/k8s/apps/observability/tracing.yaml

.PHONY: helm-uninstall-jaeger
helm-uninstall-jaeger:
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		jaeger


.PHONY: helm-install-grafana
helm-install-grafana:
	kubectl -n $(FOOBAR_NAMESPACE) \
		create secret generic foobar-grafana-datasource --from-file=assets/grafana/datasource.yaml
	kubectl -n $(FOOBAR_NAMESPACE) \
		create configmap foobar-grafana-dash-foobar --from-file=assets/grafana/dash_foobar.json
	kubectl -n $(FOOBAR_NAMESPACE) \
		create configmap foobar-grafana-dash-maker --from-file=assets/grafana/dash_maker.json
	kubectl -n $(FOOBAR_NAMESPACE) \
		create configmap foobar-grafana-dash-foobar-jvm --from-file=assets/grafana/dash_jvm_by_foobar_pod.json
	kubectl -n $(FOOBAR_NAMESPACE) \
		create configmap foobar-grafana-dash-prometheus --from-file=assets/grafana/dash_prometheus.json
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		grafana bitnami/grafana \
		-f ./assets/k8s/values/grafana.yaml

.PHONY: helm-uninstall-grafana
helm-uninstall-grafana:
	kubectl -n $(FOOBAR_NAMESPACE) delete secret foobar-grafana-datasource || true
	kubectl -n $(FOOBAR_NAMESPACE) delete configmap foobar-grafana-dash-maker || true
	kubectl -n $(FOOBAR_NAMESPACE) delete configmap foobar-grafana-dash-foobar || true
	kubectl -n $(FOOBAR_NAMESPACE) delete configmap foobar-grafana-dash-foobar-jvm || true
	kubectl -n $(FOOBAR_NAMESPACE) delete configmap foobar-grafana-dash-prometheus || true
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		grafana
	kubectl delete pvc grafana

.PHONY: helm-install-elasticsearch
helm-install-elasticsearch:
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		elasticsearch bitnami/elasticsearch \
		-f ./assets/k8s/values/elasticsearch.yaml

.PHONY: helm-uninstall-elasticsearch
helm-uninstall-elasticsearch:
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		elasticsearch
	kubectl delete pvc elasticsearch-kibana
	kubectl delete pvc data-elasticsearch-data-0
	kubectl delete pvc data-elasticsearch-master-0


# ===============================================================================
# =================================== APPLY =====================================
# ===============================================================================

.PHONY: k8s-namespace
k8s-namespace:
	kubectl apply -f ./assets/k8s/namespace/namespace.yml

.PHONY: k8s-apply-filebeat
k8s-apply-filebeat:
	kubectl apply -f assets/k8s/apps/filebeat1.yaml -n $(FOOBAR_NAMESPACE) 

.PHONY: k8s-delete-filebeat
k8s-delete-filebeat:
	kubectl delete -f assets/k8s/apps/filebeat1.yaml -n $(FOOBAR_NAMESPACE) 

# ===============================================================================
# ================================= PORT_FORWARD ================================
# ===============================================================================
#

.PHONY: k8s-port-forward-jaeger-ui
k8s-port-forward-jaeger-ui:
	kubectl port-forward \
		--namespace $(FOOBAR_NAMESPACE) \
		svc/jaeger-query \
		16686:16686
		# $(shell kubectl get pods --namespace foobar -l "app.kubernetes.io/instance=jaeger" -o jsonpath="{.items[0].metadata.name}") \

.PHONY: k8s-port-forward-elasticsearch
k8s-port-forward-elasticsearch:
	kubectl port-forward \
		--namespace $(FOOBAR_NAMESPACE) \
		svc/elasticsearch \
		9200:9200

.PHONY: k8s-port-forward-kibana
k8s-port-forward-kibana:
	kubectl port-forward --namespace $(FOOBAR_NAMESPACE) \
		svc/elasticsearch-kibana \
		5601:5601

.PHONY: k8s-port-forward-prometheus
k8s-port-forward-prometheus:
	kubectl port-forward \
		--namespace $(FOOBAR_NAMESPACE) \
		svc/prometheus-kube-prometheus-prometheus 9090:9090

.PHONY: k8s-port-forward-alertmanager
k8s-port-forward-alertmanager:
	kubectl port-forward \
		--namespace $(FOOBAR_NAMESPACE) \
		svc/prometheus-kube-prometheus-alertmanager 9093:9093

.PHONY: k8s-port-forward-grafana
k8s-port-forward-grafana:
	kubectl port-forward \
		--namespace $(FOOBAR_NAMESPACE) \
		svc/grafana \
		3000:3000

.PHONY: k8s-port-forward-stop
k8s-port-forward-stop:
	pkill -ef 'kubectl port-forward' || true

.PHONY: k8s-port-forward-services
k8s-port-forward-services:
	kubectl port-forward --namespace $(FOOBAR_NAMESPACE) \
		svc/jaeger-query \
		16686:16686 &
	kubectl port-forward --namespace $(FOOBAR_NAMESPACE) \
		svc/elasticsearch-kibana \
		5601:5601 &
	kubectl port-forward --namespace $(FOOBAR_NAMESPACE) \
		svc/prometheus-kube-prometheus-prometheus \
		9090:9090 &
	kubectl port-forward --namespace $(FOOBAR_NAMESPACE) \
		svc/prometheus-kube-prometheus-alertmanager \
		9093:9093 &
	kubectl port-forward --namespace $(FOOBAR_NAMESPACE) \
		svc/grafana \
		3000:3000 &


.PHONY: k8s-port-forward
k8s-port-forward: k8s-port-forward-services k8s-port-forward-foobar

# ===============================================================================
# ================================== CLI ========================================
# ===============================================================================

.PHONY: k8s-run-kafka-cli
k8s-run-kafka-cli:
	kubectl exec kafka-0 \
		-it \
		--namespace $(FOOBAR_NAMESPACE) \
		-- bash

.PHONY: k8s-run-pg-cli
k8s-run-pg-cli:
	kubectl exec pg-postgresql-0 \
		-it \
		--namespace $(FOOBAR_NAMESPACE) \
		-- bash -c \
		'PGPASSWORD="." -U root psql'

.PHONY: k8s-run-my-cli
k8s-run-my-cli:
	kubectl exec postgres-0 \
		-it \
		--namespace $(FOOBAR_NAMESPACE) \
		-- bash -c \
		'PGPASSWORD="$(shell kubectl get secret --namespace foobar postgres -o jsonpath="{.data.postgres-root-password}" | base64 -d)" psql -h postgres.foobar.svc.cluster.local -U root -p 5432'
		

# ===============================================================================
# =================================== INIT ======================================
# ===============================================================================

define _k8s_init_pg_exec
	kubectl exec postgres-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c \
		'PGPASSWORD=$(PGPASSWORD) psql -h postgres.foobar.svc.cluster.local -U root -p 5432 -d $2 -c $1'
endef


.PHONY: k8s-init-create-db
k8s-init-create-db: 
	$(eval PGPASSWORD=$(shell kubectl get secret --namespace foobar pg-postgresql -o jsonpath="{.data.postgres-password}" | base64 -d))
	kubectl exec pg-postgresql-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c ' \
		  PGPASSWORD="$(PGPASSWORD)" createdb -e -U root foobar_customer; \
		  PGPASSWORD="$(PGPASSWORD)" createdb -e -U root foobar_seller; \
		  PGPASSWORD="$(PGPASSWORD)" createdb -e -U root foobar_marketplace; \
		  PGPASSWORD="$(PGPASSWORD)" createdb -e -U root foobar_marketplace_engine; \
		  PGPASSWORD="$(PGPASSWORD)" createdb -e -U root foobar_shipping; \
		  PGPASSWORD="$(PGPASSWORD)" createdb -e -U root foobar_warehouse; \
		  PGPASSWORD="$(PGPASSWORD)" createdb -e -U root foobar_maker; \
		  '
	kubectl exec pg-postgresql-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c ' \
		  PGPASSWORD="$(PGPASSWORD)" psql -U postgres foobar_customer -c "ALTER USER root WITH SUPERUSER"; \
		  '
	kubectl exec pg-postgresql-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c ' \
		  PGPASSWORD="$(PGPASSWORD)" psql -U postgres foobar_customer -c "ALTER USER root WITH LOGIN"; \
		  '
	kubectl exec pg-postgresql-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c ' \
		  PGPASSWORD="$(PGPASSWORD)" psql -U postgres foobar_customer -c "ALTER USER root WITH REPLICATION"; \
		  '

.PHONY: k8s-init-truncate-db
k8s-init-truncate-db:
	$(eval PGPASSWORD=$(shell kubectl get secret --namespace foobar postgres -o jsonpath="{.data.postgres-root-password}" | base64 -d))
	$(call _k8s_init_pg_exec,"TRUNCATE customer__address",foobar_customer); \
	$(call _k8s_init_pg_exec,"TRUNCATE customer__customer CASCADE",foobar_customer); \
	$(call _k8s_init_pg_exec,"TRUNCATE seller__seller",foobar_seller); \
	$(call _k8s_init_pg_exec,"TRUNCATE warehouse__availability",foobar_warehouse); \
	$(call _k8s_init_pg_exec,"TRUNCATE warehouse__product CASCADE",foobar_warehouse); \
	$(call _k8s_init_pg_exec,"TRUNCATE marketplace__order_request",foobar_marketplace); \
	$(call _k8s_init_pg_exec,"TRUNCATE marketplace__order_request_line_item",foobar_marketplace); \
	$(call _k8s_init_pg_exec,"TRUNCATE marketplace__processed_order_request_seller",foobar_marketplace); \
	$(call _k8s_init_pg_exec,"TRUNCATE marketplace_engine__availability",foobar_marketplace_engine); \
	$(call _k8s_init_pg_exec,"TRUNCATE marketplace_engine__processed_uuid",foobar_marketplace_engine); \
	$(call _k8s_init_pg_exec,"TRUNCATE shipping__shipping",foobar_shipping);

.PHONY: k8s-init-recreate-db
k8s-init-recreate-db: k8s-init-drop-db k8s-init-create-db

.PHONY: k8s-init-create-topics
k8s-init-create-topics:
	kubectl exec kafka-controller-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c \
		'kafka-topics.sh --bootstrap-server kafka:9092 --create --replication-factor 1 --partitions 16 --topic foobar--marketplace--order-request--state-changed; \
		 kafka-topics.sh --bootstrap-server kafka:9092 --create --replication-factor 1 --partitions 16 --topic foobar--marketplace--order-request--state-changed--dead-letter; \
		 kafka-topics.sh --bootstrap-server kafka:9092 --create --replication-factor 1 --partitions 16 --topic foobar--marketplace-engine--order-request--seller-found; \
		 kafka-topics.sh --bootstrap-server kafka:9092 --create --replication-factor 1 --partitions 16 --topic foobar--warehouse--availability'

.PHONY: k8s-init-drop-topics
k8s-init-drop-topics:
	kubectl exec kafka-controller-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c \
		'kafka-topics.sh --bootstrap-server kafka:9092 --delete --topic foobar--marketplace--order-request--state-changed; \
		 kafka-topics.sh --bootstrap-server kafka:9092 --delete --topic foobar--marketplace--order-request--state-changed--dead-letter; \
		 kafka-topics.sh --bootstrap-server kafka:9092 --delete --topic foobar--marketplace-engine--order-request--seller-found; \
		 kafka-topics.sh --bootstrap-server kafka:9092 --delete --topic foobar--warehouse--availability'

.PHONY: k8s-init-recreate-topics
k8s-init-recreate-topics: k8s-init-drop-topics k8s-init-create-topics

.PHONY: k8s-init
k8s-init: k8s-init-create-db k8s-init-create-topics



.PHONY: k8s-deploy-deps
k8s-deploy-deps: \
	helm-add-repos \
	helm-install-pg \
	helm-install-kafka \
	helm-install-jaeger \
	helm-install-elasticsearch \
	helm-install-prometheus \
	helm-install-grafana \
	k8s-apply-filebeat
	kubectl wait --for=condition=Ready --all=true --all-namespaces pods --timeout=300s
	# helm-install-redis \
	# helm-install-logstash \


# ===============================================================================
# ================================= REGISTRY ====================================
# ===============================================================================

.PHONY: docker-registry
docker-registry:
	docker run -e REGISTRY_STORAGE_DELETE_ENABLED=true -d -p 5000:5000 --name foobar-registry registry:2

