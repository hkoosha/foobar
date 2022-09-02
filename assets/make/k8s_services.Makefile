
.PHONY: remake-k8s-services
remake-k8s-services:
	$(EDITOR) assets/make/k8s_services.Makefile



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
		--nodes=$(FOOBAR_MINIKUBE_NODES)
	kubectl apply -f assets/k8s/apps/metrics_server.yaml
	sleep 1s
	kubectl taint nodes minikube foobar-no-schedule=foobar-no-schedule:NoSchedule
	if [[ "$(FOOBAR_FAST_DOCKER_REGISTRY)" == "true" ]]; then \
		$(MAKE) docker-registry; \
	fi
	$(MAKE) _minikube-after-start

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

.PHONY: minikube-kill
minikube-kill:
	minikube stop
	minikube delete
	docker kill foobar-registry
	docker rm foobar-registry

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




# ===============================================================================
# ================================== HELM =======================================
# ===============================================================================

.PHONY: helm-add-bitnami
helm-add-bitnami:
	helm repo add bitnami https://charts.bitnami.com/bitnami

# .PHONY: helm-add-jaeger
# helm-add-jaeger:
# 	helm repo add jaegertracing https://jaegertracing.github.io/helm-charts

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

.PHONY: helm-install-mariadb
helm-install-mariadb:
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		mariadb bitnami/mariadb \
		-f ./assets/k8s/helm/values/my.yaml

.PHONY: helm-uninstall-mariadb
helm-uninstall-mariadb:
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		mariadb


.PHONY: helm-install-kafka
helm-install-kafka:
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		kafka bitnami/kafka \
		-f ./assets/k8s/helm/values/kafka.yaml

.PHONY: helm-uninstall-kafka
helm-uninstall-kafka:
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		kafka


.PHONY: helm-install-prometheus
helm-install-prometheus:
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		prometheus bitnami/kube-prometheus
	kubectl apply -f assets/k8s/monitoring/podmonitor.yaml
	kubectl apply -f assets/k8s/monitoring/servicemonitor.yaml
	kubectl apply -f assets/k8s/monitoring/prometheus.yaml

.PHONY: helm-uninstall-prometheus
helm-uninstall-prometheus:
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		prometheus


.PHONY: helm-install-grafana
helm-install-grafana:
	kubectl create secret generic foobar-grafana-datasource --from-file=assets/grafana/datasource.yaml
	kubectl create configmap foobar-grafana-dash-foobar --from-file=assets/grafana/dash_foobar.json
	kubectl create configmap foobar-grafana-dash-foobar-jvm --from-file=assets/grafana/dash_jvm_by_foobar_pod.json
	kubectl create configmap foobar-grafana-dash-prometheus --from-file=assets/grafana/dash_prometheus.json
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		grafana bitnami/grafana \
		-f ./assets/k8s/helm/values/grafana.yaml

.PHONY: helm-uninstall-grafana
helm-uninstall-grafana:
	kubectl delete secret foobar-grafana-datasource || true
	kubectl delete configmap foobar-grafana-dash-foobar || true
	kubectl delete configmap foobar-grafana-dash-foobar-jvm || true
	kubectl delete configmap foobar-grafana-dash-prometheus || true
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		grafana

.PHONY: helm-install-jaeger
helm-install-jaeger:
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		jaeger ./assets/k8s/helm/github.com__jaegertracing__helm-charts/charts/jaeger/ \
		-f ./assets/k8s/helm/values/jaeger.yaml

.PHONY: helm-uninstall-jaeger
helm-uninstall-jaeger:
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		jaeger


.PHONY: helm-install-elasticsearch
helm-install-elasticsearch:
	helm install \
		-n $(FOOBAR_NAMESPACE) \
		elasticsearch bitnami/elasticsearch \
		-f ./assets/k8s/helm/values/elasticsearch.yaml

.PHONY: helm-uninstall-elasticsearch
helm-uninstall-elasticsearch:
	helm uninstall \
		-n $(FOOBAR_NAMESPACE) \
		elasticsearch


# .PHONY: helm-install-logstash
# helm-install-logstash:
# 	helm install \
# 		-n $(FOOBAR_NAMESPACE) \
# 		foobar-logstash bitnami/logstash \
# 		-f ./assets/k8s/helm/values/logstash.yaml

# .PHONY: helm-uninstall-logstash
# helm-uninstall-logstash:
# 	helm uninstall \
# 		-n $(FOOBAR_NAMESPACE) \
# 		foobar-logstash



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

.PHONY: k8s-apply-zipkin
k8s-apply-zipkin:
	kubectl apply -f assets/k8s/apps/zipkin.yaml -n $(FOOBAR_NAMESPACE) 

.PHONY: k8s-delete-zipkin
k8s-delete-zipkin:
	kubectl delete -f assets/k8s/apps/zipkin.yaml -n $(FOOBAR_NAMESPACE) 

# ===============================================================================
# ================================= PORT_FORWARD ================================
# ===============================================================================
#
.PHONY: k8s-port-forward-zipkin
k8s-port-forward-zipkin:
	kubectl port-forward \
		--namespace $(FOOBAR_NAMESPACE) \
		svc/zipkin \
		9411:9411
		# $(shell kubectl get pods --namespace foobar -l "zipkin=zipkin" -o jsonpath="{.items[0].metadata.name}") \

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
		svc/zipkin \
		9411:9411 &
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

.PHONY: k8s-run-my-cli
k8s-run-my-cli:
	kubectl exec mariadb-0 \
		-it \
		--namespace $(FOOBAR_NAMESPACE) \
		-- bash -c \
		'mysql -h mariadb.foobar.svc.cluster.local -uroot -p"$(shell kubectl get secret --namespace foobar mariadb -o jsonpath="{.data.mariadb-root-password}" | base64 -d)"'


# ===============================================================================
# =================================== INIT ======================================
# ===============================================================================

.PHONY: k8s-init-zipkin-db
k8s-init-zipkin-db:
	kubectl exec mariadb-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c \
		  'mysql -D zipkin -h mariadb.foobar.svc.cluster.local -uroot \
		  -p"$(shell kubectl get secret --namespace foobar mariadb -o jsonpath="{.data.mariadb-root-password}" | base64 -d)" \
		  -e " \
		  CREATE TABLE IF NOT EXISTS zipkin_spans ( \
			trace_id BIGINT NOT NULL, \
			id BIGINT NOT NULL, \
			name VARCHAR(255) NOT NULL, \
			parent_id BIGINT, \
			debug BIT(1), \
			start_ts BIGINT, \
			duration BIGINT \
		) ENGINE=InnoDB ROW_FORMAT=COMPRESSED; \
		ALTER TABLE zipkin_spans ADD UNIQUE KEY(trace_id, id) ; \
		ALTER TABLE zipkin_spans ADD INDEX(trace_id, id) ; \
		ALTER TABLE zipkin_spans ADD INDEX(trace_id) ; \
		ALTER TABLE zipkin_spans ADD INDEX(name) ; \
		ALTER TABLE zipkin_spans ADD INDEX(start_ts) ; \
		CREATE TABLE IF NOT EXISTS zipkin_annotations ( \
			trace_id BIGINT NOT NULL , \
			span_id BIGINT NOT NULL , \
			a_key VARCHAR(255) NOT NULL , \
			a_value BLOB, \
			a_type INT NOT NULL, \
			a_timestamp BIGINT, \
			endpoint_ipv4 INT, \
			endpoint_ipv6 BINARY(16), \
			endpoint_port SMALLINT, \
			endpoint_service_name VARCHAR(255)  \
		) ENGINE=InnoDB ROW_FORMAT=COMPRESSED; \
		ALTER TABLE zipkin_annotations ADD UNIQUE KEY(trace_id, span_id, a_key, a_timestamp) ; \
		ALTER TABLE zipkin_annotations ADD INDEX(trace_id, span_id) ; \
		ALTER TABLE zipkin_annotations ADD INDEX(trace_id) ; \
		ALTER TABLE zipkin_annotations ADD INDEX(endpoint_service_name) ; \
		ALTER TABLE zipkin_annotations ADD INDEX(a_type) ; \
		ALTER TABLE zipkin_annotations ADD INDEX(a_key) ; \
		CREATE TABLE IF NOT EXISTS zipkin_dependencies ( \
			day DATE NOT NULL, \
			parent VARCHAR(255) NOT NULL, \
			child VARCHAR(255) NOT NULL, \
			call_count BIGINT \
		) ENGINE=InnoDB ROW_FORMAT=COMPRESSED; \
		ALTER TABLE zipkin_spans ADD trace_id_high BIGINT NOT NULL DEFAULT 0; \
		ALTER TABLE zipkin_annotations ADD trace_id_high BIGINT NOT NULL DEFAULT 0; \
		ALTER TABLE zipkin_spans   DROP INDEX trace_id, ADD UNIQUE KEY(trace_id_high, trace_id, id); \
		ALTER TABLE zipkin_annotations DROP INDEX trace_id, ADD UNIQUE KEY(trace_id_high, trace_id, span_id, a_key, a_timestamp); \
		ALTER TABLE zipkin_dependencies ADD error_count BIGINT; \
		ALTER TABLE zipkin_spans ADD remote_service_name VARCHAR(255); \
		ALTER TABLE zipkin_spans ADD INDEX(remote_service_name); \
		"'

.PHONY: k8s-init-create-db
k8s-init-create-db: k8s-init-zipkin-db
	kubectl exec mariadb-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c \
		  'mysql -h mariadb.foobar.svc.cluster.local -uroot \
		  -p"$(shell kubectl get secret --namespace foobar mariadb -o jsonpath="{.data.mariadb-root-password}" | base64 -d)" \
		  -e "CREATE DATABASE foobar_customer; \
		      CREATE DATABASE foobar_seller; \
			  CREATE DATABASE foobar_marketplace; \
			  CREATE DATABASE foobar_marketplace_engine; \
			  CREATE DATABASE foobar_shipping; \
			  CREATE DATABASE foobar_warehouse; \
			  CREATE DATABASE foobar_maker; \
			  CREATE DATABASE zipkin; \
			  SHOW DATABASES"'

.PHONY: k8s-init-drop-db
k8s-init-drop-db:
	kubectl exec mariadb-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c \
		  'mysql -h mariadb.foobar.svc.cluster.local -uroot \
		  -p"$(shell kubectl get secret --namespace foobar mariadb -o jsonpath="{.data.mariadb-root-password}" | base64 -d)" \
		  -e "DROP DATABASE foobar_customer; \
		      DROP DATABASE foobar_seller; \
			  DROP DATABASE foobar_marketplace; \
			  DROP DATABASE foobar_marketplace_engine; \
			  DROP DATABASE foobar_shipping; \
			  DROP DATABASE foobar_warehouse; \
			  DROP DATABASE foobar_maker; \
			  SHOW DATABASES"'

_FOOBAR_K8S_MARIADB_PASSWORD ?= $(shell kubectl get secret --namespace foobar mariadb -o jsonpath="{.data.mariadb-root-password}" 2>/dev/null | base64 -d 2>/dev/null)

define _k8s_init_my_exec
	kubectl exec mariadb-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c \
		  'mysql -h mariadb.foobar.svc.cluster.local -uroot -D $2 \
		  -p"$(_FOOBAR_K8S_MARIADB_PASSWORD)" \
		  -e $1'
endef

.PHONY: k8s-init-truncate-db
k8s-init-truncate-db:
	$(call _k8s_init_my_exec,"truncate customer__address",foobar_customer); \
	$(call _k8s_init_my_exec,"DELETE FROM customer__customer WHERE 1=1",foobar_customer); \
	$(call _k8s_init_my_exec,"truncate seller__seller",foobar_seller); \
	$(call _k8s_init_my_exec,"truncate warehouse__availability",foobar_warehouse); \
	$(call _k8s_init_my_exec,"DELETE FROM warehouse__product WHERE 1=1",foobar_warehouse); \
	$(call _k8s_init_my_exec,"truncate marketplace__order_request",foobar_marketplace); \
	$(call _k8s_init_my_exec,"truncate marketplace__order_request_line_item",foobar_marketplace); \
	$(call _k8s_init_my_exec,"truncate marketplace__processed_order_request_seller",foobar_marketplace); \
	$(call _k8s_init_my_exec,"truncate marketplace_engine__availability",foobar_marketplace_engine); \
	$(call _k8s_init_my_exec,"truncate marketplace_engine__processed_uuid",foobar_marketplace_engine); \
	$(call _k8s_init_my_exec,"truncate shipping__shipping",foobar_shipping);

.PHONY: k8s-init-recreate-db
k8s-init-recreate-db: k8s-init-drop-db k8s-init-create-db

.PHONY: k8s-init-create-topics
k8s-init-create-topics:
	kubectl exec kafka-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c \
		'kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.foobar.svc.cluster.local:9092 --create --replication-factor 1 --partitions 16 --topic foobar__marketplace__order_request__state_changed; \
		 kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.foobar.svc.cluster.local:9092 --create --replication-factor 1 --partitions 16 --topic foobar__marketplace__order_request__state_changed__dead_letter; \
		 kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.foobar.svc.cluster.local:9092 --create --replication-factor 1 --partitions 16 --topic foobar__marketplace_engine__order_request__seller_found; \
		 kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.foobar.svc.cluster.local:9092 --create --replication-factor 1 --partitions 16 --topic foobar__warehouse__availability'

.PHONY: k8s-init-drop-topics
k8s-init-drop-topics:
	kubectl exec kafka-0 -it --namespace $(FOOBAR_NAMESPACE) -- bash -c \
		'kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.foobar.svc.cluster.local:9092 --delete --topic foobar__marketplace__order_request__state_changed; \
		 kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.foobar.svc.cluster.local:9092 --delete --topic foobar__marketplace__order_request__state_changed__dead_letter; \
		 kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.foobar.svc.cluster.local:9092 --delete --topic foobar__marketplace_engine__order_request__seller_found; \
		 kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.foobar.svc.cluster.local:9092 --delete --topic foobar__warehouse__availability'

.PHONY: k8s-init-recreate-topics
k8s-init-recreate-topics: k8s-init-drop-topics k8s-init-create-topics

.PHONY: k8s-init
k8s-init: k8s-init-create-db k8s-init-create-topics



.PHONY: k8s-deploy-deps
k8s-deploy-deps: \
	helm-add-bitnami \
	helm-install-mariadb \
	helm-install-kafka \
	helm-install-jaeger \
	helm-install-elasticsearch \
	helm-install-prometheus \
	helm-install-grafana \
	k8s-apply-filebeat \
	k8s-apply-zipkin
	# helm-install-redis \
	# helm-install-logstash \


# ===============================================================================
# ================================= REGISTRY ====================================
# ===============================================================================

.PHONY: docker-registry
docker-registry:
	docker run -e REGISTRY_STORAGE_DELETE_ENABLED=true -d -p 5000:5000 --restart=always --name foobar-registry registry:2

