
.PHONY: remake-local-dependencies
remake-local-dependencies:
	$(EDITOR) assets/docker/dependencies.Makefile


.PHONY: local-run-dependencies
local-run-dependencies:
	docker-compose -f ./assets/docker/docker-compose.yml up

.PHONY: local-stop-dependencies
local-stop-dependencies:
	docker-compose -f ./assets/docker/docker-compose.yml down || true
	sleep 2
	docker-compose -f ./assets/docker/docker-compose.yml rm



## =============================================================================
## =============================================================================
## =============================================================================



.PHONY: local-init-host-pg-drop-db
local-init-host-pg-drop-db:
	PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_maker
	PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_customer
	PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_marketplace
	PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_marketplace_engine
	PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_seller
	PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_shipping
	PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_warehouse

.PHONY: local-init-host-pg-create-db
local-init-host-pg-create-db:
	PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_maker
	PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_customer
	PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_marketplace
	PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_marketplace_engine
	PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_seller
	PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_shipping
	PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_warehouse

.PHONY: local-init-host-pg-recreate-db
local-init-host-pg-recreate-db: local-init-host-pg-drop-db local-init-host-pg-create-db


define _local_init_pg_exec
	docker-compose -f assets/docker/docker-compose.yml exec postgres bash -c \
	 'PGPASSWORD="$(POSTGRES_PASSWORD)" psql -U "$(POSTGRES_USER)" -c $2 $3'
endef

define _local_init_pg_drop_db
	docker-compose -f assets/docker/$(1)/docker-compose.yml exec postgres bash -c \
	 ' PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_maker \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_customer \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_marketplace \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_marketplace_engine \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_seller \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_shipping \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_warehouse'
endef

define _local_init_pg_create_db
	docker-compose -f assets/docker/docker-compose.yml exec postgres bash -c \
	 ' PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_maker \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_customer \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_marketplace \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_marketplace_engine \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_seller \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_shipping \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" createdb -e -U "$(POSTGRES_USER)" foobar_warehouse'
endef

.PHONY: local-init-pg-drop-db
local-init-pg-drop-db:
	if docker-compose -f assets/docker/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_pg_drop_db,all); \
	else \
		$(call _local_init_pg_drop_db,postgres); \
	fi

.PHONY: local-init-pg-create-db
local-init-pg-create-db:
	if docker-compose -f assets/docker/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_pg_create_db,all); \
	else \
		$(call _local_init_pg_create_db,postgres); \
	fi
	
.PHONY: local-init-pg-recreate-db
local-init-pg-recreate-db: local-init-pg-drop-db local-init-pg-create-db

.PHONY: local-init-pg-truncate-db
local-init-pg-truncate-db:
	if docker-compose -f assets/docker/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_pg_exec,all,"truncate customer__address",foobar_customer); \
		$(call _local_init_pg_exec,all,"DELETE FROM customer__customer WHERE 1=1",foobar_customer); \
		$(call _local_init_pg_exec,all,"truncate seller__seller",foobar_seller); \
		$(call _local_init_pg_exec,all,"truncate warehouse__availability",foobar_warehouse); \
		$(call _local_init_pg_exec,all,"DELETE FROM warehouse__product WHERE 1=1",foobar_warehouse); \
		$(call _local_init_pg_exec,all,"truncate marketplace__order_request",foobar_marketplace); \
		$(call _local_init_pg_exec,all,"truncate marketplace__order_request_line_item",foobar_marketplace); \
		$(call _local_init_pg_exec,all,"truncate marketplace__processed_order_request_seller",foobar_marketplace); \
		$(call _local_init_pg_exec,all,"truncate marketplace_engine__availability",foobar_marketplace_engine); \
		$(call _local_init_pg_exec,all,"truncate marketplace_engine__processed_uuid",foobar_marketplace_engine); \
		$(call _local_init_pg_exec,all,"truncate shipping__shipping",foobar_shipping); \
	else \
		$(call _local_init_pg_exec,postgres,"truncate customer__customer",foobar_customer); \
		$(call _local_init_pg_exec,postgres,"truncate customer__address",foobar_customer); \
		$(call _local_init_pg_exec,postgres,"truncate seller__seller",foobar_seller); \
		$(call _local_init_pg_exec,postgres,"truncate warehouse__availability",foobar_warehouse); \
		$(call _local_init_pg_exec,postgres,"truncate warehouse__product",foobar_warehouse); \
		$(call _local_init_pg_exec,postgres,"truncate marketplace__order_request",foobar_marketplace); \
		$(call _local_init_pg_exec,postgres,"truncate marketplace__order_request_line_item",foobar_marketplace); \
		$(call _local_init_pg_exec,postgres,"truncate marketplace__processed_order_request_seller",foobar_marketplace); \
		$(call _local_init_pg_exec,postgres,"truncate marketplace_engine__availability",foobar_marketplace_engine); \
		$(call _local_init_pg_exec,postgres,"truncate marketplace_engine__processed_uuid",foobar_marketplace_engine); \
		$(call _local_init_pg_exec,postgres,"truncate shipping__shipping",foobar_shipping); \
	fi



## =============================================================================
## =============================================================================
## =============================================================================



define _local_postgres_bash
	docker-compose -f assets/docker/docker-compose.yml exec postgres bash
endef

.PHONY: local-pg-bash
local-pg-bash:
	if docker-compose -f assets/docker/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_postgres_bash,all); \
	else \
		$(call _local_postgres_bash,postgres); \
	fi

define _local_postgres_cli
	docker-compose -f assets/docker/docker-compose.yml exec postgres bash -c \
		'PGPASSWORD="$(POSTGRES_PASSWORD)" psql -U "$(POSTGRES_USER)" -d postgres'
endef

.PHONY: local-pg-cli
local-pg-cli:
	if docker-compose -f assets/docker/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_postgres_cli,all); \
	else \
		$(call _local_postgres_cli,postgres); \
	fi



## =============================================================================
## =============================================================================
## =============================================================================



define _local_init_truncate_topics
	docker-compose -f assets/docker/docker-compose.yml exec kafka bash -c \
		'kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar--marketplace--order-request--state-changed --add-config retention.ms=100; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar--marketplace--order-request--state-changed--dead-letter --add-config retention.ms=100; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar--marketplace-engine--order-request--seller-found --add-config retention.ms=100; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar--warehouse--availability --add-config retention.ms=100; \
		 sleep 2s; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar--marketplace--order-request--state-changed --delete-config retention.ms; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar--marketplace--order-request--state-changed--dead-letter --delete-config retention.ms; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar--marketplace-engine--order-request--seller-found --delete-config retention.ms; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar--warehouse--availability --delete-config retention.ms;'
endef


define _local_init_create_topics
	docker-compose -f assets/docker/docker-compose.yml exec kafka bash -c \
		'kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 16 --topic foobar--marketplace--order-request--state-changed; \
		 kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 16 --topic foobar--marketplace--order-request--state-changed--dead-letter; \
		 kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 16 --topic foobar--marketplace-engine--order-request--seller-found; \
		 kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 16 --topic foobar--warehouse--availability'
endef

define _local_init_drop_topics
	docker-compose -f assets/docker/docker-compose.yml exec kafka bash -c \
		'kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic foobar--marketplace--order-request--state-changed; \
		 kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic foobar--marketplace--order-request--state-changed--dead-letter; \
		 kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic foobar--marketplace-engine--order-request--seller-found; \
		 kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic foobar--warehouse--availability'
endef

.PHONY: local-init-truncate-topics
local-init-truncate-topics:
	if docker-compose -f assets/docker/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_truncate_topics,all); \
	else \
		$(call _local_init_truncate_topics,kafka); \
	fi

.PHONY: local-init-create-topics
local-init-create-topics:
	if docker-compose -f assets/docker/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_create_topics,all); \
	else \
		$(call _local_init_create_topics,kafka); \
	fi

.PHONY: local-init-drop-topics
local-init-drop-topics:
	if docker-compose -f assets/docker/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_drop_topics,all); \
	else \
		$(call _local_init_drop_topics,kafka); \
	fi

.PHONY: local-init-recreate-topics
local-init-recreate-topics: local-init-drop-topics local-init-create-topics



## =============================================================================
## =============================================================================
## =============================================================================



.PHONY: local-init
local-init: \
	local-init-pg-create-db \
	local-init-create-topics

