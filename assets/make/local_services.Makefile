

.PHONY: local-run-zipkin
local-run-zipkin:
	docker-compose -f ./assets/local_deployment/zipkin/docker-compose.yml up

.PHONY: local-stop-zipkin
local-stop-zipkin:
	docker-compose -f ./assets/local_deployment/zipkin/docker-compose.yml down
	docker-compose -f ./assets/local_deployment/zipkin/docker-compose.yml rm



.PHONY: local-run-jaeger
local-run-jaeger:
	docker-compose -f ./assets/local_deployment/jaeger/docker-compose.yml up

.PHONY: local-stop-jaeger
local-stop-jaeger:
	docker-compose -f ./assets/local_deployment/jaeger/docker-compose.yml down
	docker-compose -f ./assets/local_deployment/jaeger/docker-compose.yml rm



.PHONY: local-run-kafka
local-run-kafka:
	docker-compose -f ./assets/local_deployment/kafka/docker-compose.yml up

.PHONY: local-stop-kafka
local-stop-kafka:
	docker-compose -f ./assets/local_deployment/kafka/docker-compose.yml down
	docker-compose -f ./assets/local_deployment/kafka/docker-compose.yml rm



.PHONY: local-run-dependencies
local-run-dependencies:
	docker-compose -f ./assets/local_deployment/all/docker-compose.yml up

.PHONY: local-stop-dependencies
local-stop-dependencies:
	docker-compose -f ./assets/local_deployment/all/docker-compose.yml down
	docker-compose -f ./assets/local_deployment/all/docker-compose.yml rm




.PHONY: local-init-drop-db
local-init-drop-db:
	mysql -e "DROP DATABASE foobar_maker" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true
	mysql -e "DROP DATABASE foobar_customer" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true
	mysql -e "DROP DATABASE foobar_marketplace" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true
	mysql -e "DROP DATABASE foobar_marketplace_engine" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true
	mysql -e "DROP DATABASE foobar_seller" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true
	mysql -e "DROP DATABASE foobar_shipping" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true
	mysql -e "DROP DATABASE foobar_warehouse" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true

.PHONY: local-init-create-db
local-init-create-db:
	mysql -e "CREATE DATABASE foobar_maker" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"
	mysql -e "CREATE DATABASE foobar_customer" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"
	mysql -e "CREATE DATABASE foobar_marketplace" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"
	mysql -e "CREATE DATABASE foobar_marketplace_engine" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"
	mysql -e "CREATE DATABASE foobar_seller" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"
	mysql -e "CREATE DATABASE foobar_shipping" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"
	mysql -e "CREATE DATABASE foobar_warehouse" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"

.PHONY: local-init-recreate-db
local-init-recreate-db: local-init-drop-db local-init-create-db



define _local_init_create_topics
	docker exec $(1) bash -c \
		'kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 16 --topic foobar__marketplace__order_request__entity_state; kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 16 --topic foobar__marketplace__order_request__entity_state__dead_letter; kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 16 --topic foobar__marketplace_engine__order_request__seller; kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 16 --topic foobar__warehouse__availability'
endef

.PHONY: local-init-create-topics
local-init-create-topics:
	$(call _local_init_create_topics,kafka-kafka-1) || $(call _local_init_create_topics,all-kafka-1)

.PHONY: local-init-drop-topics
local-init-drop-topics:
	docker exec kafka-kafka-1 bash -c \
		'kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic foobar__marketplace__order_request__entity_state; kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic foobar__marketplace__order_request__entity_state__dead_letter; kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic foobar__marketplace_engine__order_request__seller; kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic foobar__warehouse__availability'

.PHONY: local-init-recreate-topics
local-init-recreate-topics: local-init-drop-topics local-init-create-topics



.PHONY: local-init
local-init:
	local-init-create-db local-init-create-topics

.PHONY: local-drop
local-drop:
	local-init-drop-db local-init-drop-topics

.PHONY: local-recreate
local-recreate:
	local-init-recreate-db local-init-recreate-topics

