
.PHONY: remake-k8s-services
remake-k8s-services:
	$(EDITOR) assets/make/k8s_services.Makefile



# ===============================================================================
# ================================= MINIKUBE ====================================
# ===============================================================================

.PHONY: minikube-start
minikube-start:
	minikube start \
		--addons=$(FOOBAR_MINIKUBE_ADDONS) \
		--cpus=$(FOOBAR_MINIKUBE_NUM_CPU) \
		--memory=$(FOOBAR_MINIKUBE_MEMORY) \
		--driver=$(FOOBAR_MINIKUBE_DRIVER) \
		--nodes=$(FOOBAR_MINIKUBE_NODES)
	kubectl apply -f assets/k8s/apps/metrics_server.yaml
	sleep 1s
	kubectl taint nodes minikube foobar-no-schedule=foobar-no-schedule:NoSchedule
	@echo "Minikube cluster started. It is better to wait until all nodes in the cluster are ready."
	@echo "You can check the status if nodes using the app k9s."
	@echo "Your next step is probabaly running '$(MAKE) k8s-namespace kubectl-set-ns'."

.PHONY: minikube-kill
minikube-kill:
	minikube stop
	minikube delete

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


# .PHONY: helm-install-jaeger
# helm-install-jaeger:
# 	helm install \
# 		-n $(FOOBAR_NAMESPACE) \
# 		jaeger jaegertracing/jaeger \
# 		--set allInOne.enabled=true \
# 		--set provisionDataStore.cassandra=false \
# 		--set agent.enabled=false \
# 		--set collector.enabled=false \
# 		--set query.enabled=false

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
		$(shell kubectl get pods --namespace foobar -l "zipkin=zipkin" -o jsonpath="{.items[0].metadata.name}") \
		9411:9411

.PHONY: k8s-port-forward-jaeger-ui
k8s-port-forward-jaeger-ui:
	kubectl port-forward \
		--namespace $(FOOBAR_NAMESPACE) \
		$(shell kubectl get pods --namespace foobar -l "app.kubernetes.io/instance=jaeger" -o jsonpath="{.items[0].metadata.name}") \
		16686:16686

.PHONY: k8s-port-forward-elasticsearch
k8s-port-forward-elasticsearch:
	kubectl port-forward \
		--namespace $(FOOBAR_NAMESPACE) \
		svc/elasticsearch \
		9200:9200

# .PHONY: k8s-port-forward-logstash
# k8s-port-forward-logstash:
# 	kubectl port-forward \
# 		--namespace $(FOOBAR_NAMESPACE) \
# 		svc/foobar-logstash \
# 		$(shell kubectl get --namespace foobar -o jsonpath="{.spec.ports[0].port}" services foobar-logstash):$(shell kubectl get --namespace foobar -o jsonpath="{.spec.ports[0].port}" services foobar-logstash)

.PHONY: k8s-port-forward-kibana
k8s-port-forward-kibana:
	kubectl port-forward --namespace foobar svc/elasticsearch-kibana 5601:5601
# kubectl port-forward \
# --namespace $(FOOBAR_NAMESPACE) \
# $(shell kubectl get pods --namespace foobar -l "k8s-app=kibana-logging" -o jsonpath="{.items[0].metadata.name}") \
# 5601:5601


# ===============================================================================
# ================================== CLI ========================================
# ===============================================================================

.PHONY: k8s-run-kafka-cli
k8s-run-kafka-cli:
	kubectl run kafka-client \
		--rm \
		--tty \
		-i \
		--restart='Never' \
		--image docker.io/bitnami/kafka:3.2.1-debian-11-r4 \
		--namespace $(FOOBAR_NAMESPACE) \
		--command bash

.PHONY: k8s-delete-kafka-cli
k8s-delete-kafka-cli:
	kubectl delete pod kafka-client --namespace $(FOOBAR_NAMESPACE) 

.PHONY: k8s-run-my-cli
k8s-run-my-cli:
	kubectl run mariadb-client \
		--rm \
		--tty \
		-i \
		--restart='Never' \
		--image  docker.io/bitnami/mariadb:10.6.8-debian-11-r25 \
		--namespace $(FOOBAR_NAMESPACE) \
		--command -- bash -c \
		'mysql -h mariadb.foobar.svc.cluster.local -uroot -p"$(shell kubectl get secret --namespace foobar mariadb -o jsonpath="{.data.mariadb-root-password}" | base64 -d)"'

.PHONY: k8s-delete-mycli
k8s-delete-mycli:
	kubectl delete pod mariadb-client --namespace $(FOOBAR_NAMESPACE) 



# ===============================================================================
# =================================== INIT ======================================
# ===============================================================================

.PHONY: k8s-init-create-db
k8s-init-create-db:
	kubectl run mariadb-client \
		--rm --tty -i --restart='Never' \
		--image  docker.io/bitnami/mariadb:10.6.8-debian-11-r25 \
		--namespace $(FOOBAR_NAMESPACE) --command -- bash -c \
		'mysql -h mariadb.foobar.svc.cluster.local -uroot -p"$(shell kubectl get secret --namespace foobar mariadb -o jsonpath="{.data.mariadb-root-password}" | base64 -d)" -e "CREATE DATABASE foobar_customer; CREATE DATABASE foobar_seller; CREATE DATABASE foobar_marketplace; CREATE DATABASE foobar_marketplace_engine; CREATE DATABASE foobar_shipping; CREATE DATABASE foobar_warehouse; CREATE DATABASE foobar_maker; SHOW DATABASES"'

.PHONY: k8s-init-drop-db
k8s-init-drop-db:
	kubectl run mariadb-client \
		--rm --tty -i --restart='Never' \
		--image  docker.io/bitnami/mariadb:10.6.8-debian-11-r25 \
		--namespace $(FOOBAR_NAMESPACE) --command -- bash -c \
		'mysql -h mariadb.foobar.svc.cluster.local -uroot -p"$(shell kubectl get secret --namespace foobar mariadb -o jsonpath="{.data.mariadb-root-password}" | base64 -d)" -e "DROP DATABASE foobar_customer; DROP DATABASE foobar_seller; DROP DATABASE foobar_marketplace; DROP DATABASE foobar_marketplace_engine; DROP DATABASE foobar_shipping; DROP DATABASE foobar_warehouse; DROP DATABASE foobar_maker; SHOW DATABASES"'

.PHONY: k8s-init-recreate-db
k8s-init-recreate-db: k8s-init-drop-db k8s-init-create-db

.PHONY: k8s-init-create-topics
k8s-init-create-topics:
	kubectl run kafka-client --rm --tty -i --restart='Never' --image docker.io/bitnami/kafka:3.2.1-debian-11-r4 --namespace $(FOOBAR_NAMESPACE) --command -- bash -c \
		'kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.foobar.svc.cluster.local:9092 --create --replication-factor 1 --partitions 16 --topic foobar__marketplace__order_request__state_changed; \
		 kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.foobar.svc.cluster.local:9092 --create --replication-factor 1 --partitions 16 --topic foobar__marketplace__order_request__state_changed__dead_letter; \
		 kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.foobar.svc.cluster.local:9092 --create --replication-factor 1 --partitions 16 --topic foobar__marketplace_engine__order_request__seller_found; \
		 kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.foobar.svc.cluster.local:9092 --create --replication-factor 1 --partitions 16 --topic foobar__warehouse__availability'

.PHONY: k8s-init-drop-topics
k8s-init-drop-topics:
	kubectl run kafka-client --rm --tty -i --restart='Never' --image docker.io/bitnami/kafka:3.2.1-debian-11-r4 --namespace $(FOOBAR_NAMESPACE) --command -- bash -c \
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
	k8s-apply-filebeat \
	k8s-apply-zipkin
	# helm-install-redis \
	# helm-install-logstash \


