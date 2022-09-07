
.PHONY: remake-local-services
remake-local-services:
	$(EDITOR) assets/make/local_services.Makefile


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



.PHONY: local-run-mariadb
local-run-mariadb:
	docker-compose -f ./assets/local_deployment/mariadb/docker-compose.yml up

.PHONY: local-stop-mariadb
local-stop-mariadb:
	docker-compose -f ./assets/local_deployment/mariadb/docker-compose.yml down
	docker-compose -f ./assets/local_deployment/mariadb/docker-compose.yml rm


.PHONY: local-run-postgres
local-run-postgres:
	docker-compose -f ./assets/local_deployment/postgres/docker-compose.yml up

.PHONY: local-stop-postgres
local-stop-postgres:
	docker-compose -f ./assets/local_deployment/postgres/docker-compose.yml down
	docker-compose -f ./assets/local_deployment/postgres/docker-compose.yml rm


.PHONY: local-run-dependencies
local-run-dependencies:
	docker-compose -f ./assets/local_deployment/all/docker-compose.yml up

.PHONY: local-stop-dependencies
local-stop-dependencies:
	docker-compose -f ./assets/local_deployment/all/docker-compose.yml down
	docker-compose -f ./assets/local_deployment/all/docker-compose.yml rm


## =============================================================================
## =============================================================================
## =============================================================================


.PHONY: local-init-host-mysql-drop-db
local-init-host-mysql-drop-db:
	mysql -e "DROP DATABASE foobar_maker" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true
	mysql -e "DROP DATABASE foobar_customer" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true
	mysql -e "DROP DATABASE foobar_marketplace" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true
	mysql -e "DROP DATABASE foobar_marketplace_engine" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true
	mysql -e "DROP DATABASE foobar_seller" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true
	mysql -e "DROP DATABASE foobar_shipping" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true
	mysql -e "DROP DATABASE foobar_warehouse" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true

.PHONY: local-init-host-mysql-create-db
local-init-host-mysql-create-db:
	mysql -e "CREATE DATABASE foobar_maker" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"
	mysql -e "CREATE DATABASE foobar_customer" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"
	mysql -e "CREATE DATABASE foobar_marketplace" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"
	mysql -e "CREATE DATABASE foobar_marketplace_engine" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"
	mysql -e "CREATE DATABASE foobar_seller" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"
	mysql -e "CREATE DATABASE foobar_shipping" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"
	mysql -e "CREATE DATABASE foobar_warehouse" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"

.PHONY: local-init-host-mysql-recreate-db
local-init-host-mysql-recreate-db: local-init-host-mysql-drop-db local-init-host-mysql-create-db


define _local_init_my_exec
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec mariadb bash -c \
	 'mysql -D $3 -e $2 -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" '
endef

define _local_init_my_drop_db
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec mariadb bash -c 		   \
	 ' mysql -e "DROP DATABASE foobar_maker"              -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "DROP DATABASE foobar_customer"           -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "DROP DATABASE foobar_marketplace"        -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "DROP DATABASE foobar_marketplace_engine" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "DROP DATABASE foobar_seller"             -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "DROP DATABASE foobar_shipping"           -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "DROP DATABASE foobar_warehouse"          -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" || true'
endef

define _local_init_my_create_db
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec mariadb bash -c 		   \
	 ' mysql -e "CREATE DATABASE foobar_maker"              -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "CREATE DATABASE foobar_customer"           -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "CREATE DATABASE foobar_marketplace"        -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "CREATE DATABASE foobar_marketplace_engine" -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "CREATE DATABASE foobar_seller"             -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "CREATE DATABASE foobar_shipping"           -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "CREATE DATABASE foobar_warehouse"          -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" \
	 ; mysql -e "SHOW DATABASES"                            -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)" '
endef

.PHONY: local-init-my-drop-db
local-init-my-drop-db:
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_my_drop_db,all); \
	else \
		$(call _local_init_my_drop_db,mariadb); \
	fi

.PHONY: local-init-my-create-db
local-init-my-create-db:
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_my_create_db,all); \
	else \
		$(call _local_init_my_create_db,mariadb); \
	fi
	
.PHONY: local-init-my-recreate-db
local-init-my-recreate-db: local-init-my-drop-db local-init-my-create-db

.PHONY: local-init-my-truncate-db
local-init-my-truncate-db:
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_my_exec,all,"truncate customer__address",foobar_customer); \
		$(call _local_init_my_exec,all,"DELETE FROM customer__customer WHERE 1=1",foobar_customer); \
		$(call _local_init_my_exec,all,"truncate seller__seller",foobar_seller); \
		$(call _local_init_my_exec,all,"truncate warehouse__availability",foobar_warehouse); \
		$(call _local_init_my_exec,all,"DELETE FROM warehouse__product WHERE 1=1",foobar_warehouse); \
		$(call _local_init_my_exec,all,"truncate marketplace__order_request",foobar_marketplace); \
		$(call _local_init_my_exec,all,"truncate marketplace__order_request_line_item",foobar_marketplace); \
		$(call _local_init_my_exec,all,"truncate marketplace__processed_order_request_seller",foobar_marketplace); \
		$(call _local_init_my_exec,all,"truncate marketplace_engine__availability",foobar_marketplace_engine); \
		$(call _local_init_my_exec,all,"truncate marketplace_engine__processed_uuid",foobar_marketplace_engine); \
		$(call _local_init_my_exec,all,"truncate shipping__shipping",foobar_shipping); \
	else \
		$(call _local_init_my_exec,mariadb,"truncate customer__customer",foobar_customer); \
		$(call _local_init_my_exec,mariadb,"truncate customer__address",foobar_customer); \
		$(call _local_init_my_exec,mariadb,"truncate seller__seller",foobar_seller); \
		$(call _local_init_my_exec,mariadb,"truncate warehouse__availability",foobar_warehouse); \
		$(call _local_init_my_exec,mariadb,"truncate warehouse__product",foobar_warehouse); \
		$(call _local_init_my_exec,mariadb,"truncate marketplace__order_request",foobar_marketplace); \
		$(call _local_init_my_exec,mariadb,"truncate marketplace__order_request_line_item",foobar_marketplace); \
		$(call _local_init_my_exec,mariadb,"truncate marketplace__processed_order_request_seller",foobar_marketplace); \
		$(call _local_init_my_exec,mariadb,"truncate marketplace_engine__availability",foobar_marketplace_engine); \
		$(call _local_init_my_exec,mariadb,"truncate marketplace_engine__processed_uuid",foobar_marketplace_engine); \
		$(call _local_init_my_exec,mariadb,"truncate shipping__shipping",foobar_shipping); \
	fi





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
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec postgres bash -c \
	 'PGPASSWORD="$(POSTGRES_PASSWORD)" psql -U "$(POSTGRES_USER)" -c $2 $3'
endef

define _local_init_pg_drop_db
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec postgres bash -c \
	 ' PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_maker \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_customer \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_marketplace \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_marketplace_engine \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_seller \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_shipping \
	 ; PGPASSWORD="$(POSTGRES_PASSWORD)" dropdb -e -if -U "$(POSTGRES_USER)" foobar_warehouse'
endef

define _local_init_pg_create_db
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec postgres bash -c \
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
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_pg_drop_db,all); \
	else \
		$(call _local_init_pg_drop_db,postgres); \
	fi

.PHONY: local-init-pg-create-db
local-init-pg-create-db:
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_pg_create_db,all); \
	else \
		$(call _local_init_pg_create_db,postgres); \
	fi
	
.PHONY: local-init-pg-recreate-db
local-init-pg-recreate-db: local-init-pg-drop-db local-init-pg-create-db

.PHONY: local-init-pg-truncate-db
local-init-pg-truncate-db:
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
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






define _local_my_cli
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec mariadb bash -c \
		'mysql -u "$(MYSQL_USER)" -p"$(MYSQL_PASSWORD)"'
endef

.PHONY: local-my-cli
local-my-cli:
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_my_cli,all); \
	else \
		$(call _local_my_cli,mariadb); \
	fi

define _local_my_bash
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec mariadb bash
endef

.PHONY: local-my-bash
local-my-bash:
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_my_bash,all); \
	else \
		$(call _local_my_bash,mariadb); \
	fi


define _local_postgres_bash
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec postgres bash
endef

.PHONY: local-pg-bash
local-pg-bash:
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_postgres_bash,all); \
	else \
		$(call _local_postgres_bash,postgres); \
	fi

define _local_postgres_cli
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec postgres bash -c \
		'PGPASSWORD="$(POSTGRES_PASSWORD)" psql -U "$(POSTGRES_USER)" -d postgres'
endef

.PHONY: local-pg-cli
local-pg-cli:
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_postgres_cli,all); \
	else \
		$(call _local_postgres_cli,postgres); \
	fi

## =============================================================================
## =============================================================================
## =============================================================================

define _local_init_truncate_topics
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec kafka bash -c \
		'kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar__marketplace__order_request__state_changed --add-config retention.ms=100; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar__marketplace__order_request__state_changed__dead_letter --add-config retention.ms=100; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar__marketplace_engine__order_request__seller_found --add-config retention.ms=100; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar__warehouse__availability --add-config retention.ms=100; \
		 sleep 2s; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar__marketplace__order_request__state_changed --delete-config retention.ms; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar__marketplace__order_request__state_changed__dead_letter --delete-config retention.ms; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar__marketplace_engine__order_request__seller_found --delete-config retention.ms; \
		 kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name foobar__warehouse__availability --delete-config retention.ms;'
endef


define _local_init_create_topics
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec kafka bash -c \
		'kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 16 --topic foobar__marketplace__order_request__state_changed; \
		 kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 16 --topic foobar__marketplace__order_request__state_changed__dead_letter; \
		 kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 16 --topic foobar__marketplace_engine__order_request__seller_found; \
		 kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 16 --topic foobar__warehouse__availability'
endef

define _local_init_drop_topics
	docker-compose -f assets/local_deployment/$(1)/docker-compose.yml exec kafka bash -c \
		'kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic foobar__marketplace__order_request__state_changed; \
		 kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic foobar__marketplace__order_request__state_changed__dead_letter; \
		 kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic foobar__marketplace_engine__order_request__seller_found; \
		 kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic foobar__warehouse__availability'
endef

.PHONY: local-init-truncate-topics
local-init-truncate-topics:
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_truncate_topics,all); \
	else \
		$(call _local_init_truncate_topics,kafka); \
	fi

.PHONY: local-init-create-topics
local-init-create-topics:
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
		$(call _local_init_create_topics,all); \
	else \
		$(call _local_init_create_topics,kafka); \
	fi

.PHONY: local-init-drop-topics
local-init-drop-topics:
	if docker-compose -f assets/local_deployment/all/docker-compose.yml top | grep PPID > /dev/null; then \
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
	local-init-my-create-db \
	local-init-create-topics
