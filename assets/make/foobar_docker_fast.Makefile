
FOOBAR_LOADER_IMAGE := foobar-loader:0.0.1-SNAPSHOT
FOOBAR_MAKER_IMAGE := foobar-maker:0.0.1-SNAPSHOT
FOOBAR_CUSTOMER_IMAGE := foobar-customer:0.0.1-SNAPSHOT
FOOBAR_SELLER_IMAGE := foobar-seller:0.0.1-SNAPSHOT
FOOBAR_WAREHOUSE_IMAGE := foobar-warehouse:0.0.1-SNAPSHOT
FOOBAR_SHIPPING_IMAGE := foobar-shipping:0.0.1-SNAPSHOT
FOOBAR_MARKETPLACE_IMAGE := foobar-marketplace:0.0.1-SNAPSHOT
FOOBAR_MARKETPLACE_ENGINE_IMAGE := foobar-marketplace-engine:0.0.1-SNAPSHOT

.PHONY: remake-foobar-docker
remake-foobar-docker:
	$(EDITOR) assets/make/foobar_docker_fast.Makefile





.PHONY: docker-image-maker-build
docker-image-maker-build:
	$(GRADLE_RUNNER) :foobar-maker:jib

.PHONY: docker-image-maker-push
docker-image-maker-push:
	echo "No need to run this target"

.PHONY: docker-image-maker-unpush
docker-image-maker-unpush:
	echo "No need to run this target"

.PHONY: docker-image-maker
docker-image-maker: docker-image-maker-build



.PHONY: docker-image-loader-build
docker-image-loader-build:
	$(GRADLE_RUNNER) :loader:jib

.PHONY: docker-image-loader-push
docker-image-loader-push:
	echo "No need to run this target"

.PHONY: docker-image-loader-unpush
docker-image-loader-unpush:
	echo "No need to run this target"

.PHONY: docker-image-loader
docker-image-loader: docker-image-loader-build



.PHONY: docker-image-customer-build
docker-image-customer-build:
	$(GRADLE_RUNNER) :service:customer:jib

.PHONY: docker-image-customer-push
docker-image-customer-push:
	echo "No need to run this target"

.PHONY: docker-image-customer-unpush
docker-image-customer-unpush:
	echo "No need to run this target"

.PHONY: docker-image-customer
docker-image-customer: docker-image-customer-build



.PHONY: docker-image-seller-build
docker-image-seller-build:
	$(GRADLE_RUNNER) :service:seller:jib

.PHONY: docker-image-seller-push
docker-image-seller-push:
	echo "No need to run this target"

.PHONY: docker-image-seller-unpush
docker-image-seller-unpush:
	echo "No need to run this target"

.PHONY: docker-image-seller
docker-image-seller: docker-image-seller-build 



.PHONY: docker-image-shipping-build
docker-image-shipping-build:
	$(GRADLE_RUNNER) :service:shipping:jib

.PHONY: docker-image-shipping-push
docker-image-shipping-push:
	echo "No need to run this target"

.PHONY: docker-image-shipping-unpush
docker-image-shipping-unpush:
	echo "No need to run this target"

.PHONY: docker-image-shipping
docker-image-shipping: docker-image-shipping-build



.PHONY: docker-image-warehouse-build
docker-image-warehouse-build:
	$(GRADLE_RUNNER) :service:warehouse:jib

.PHONY: docker-image-warehouse-push
docker-image-warehouse-push:
	echo "No need to run this target"

.PHONY: docker-image-warehouse-unpush
docker-image-warehouse-unpush:
	echo "No need to run this target"

.PHONY: docker-image-warehouse
docker-image-warehouse: docker-image-warehouse-build



.PHONY: docker-image-marketplace-build
docker-image-marketplace-build:
	$(GRADLE_RUNNER) :service:marketplace:jib

.PHONY: docker-image-marketplace-push
docker-image-marketplace-push:
	echo "No need to run this target"

.PHONY: docker-image-marketplace-unpush
docker-image-marketplace-unpush:
	echo "No need to run this target"

.PHONY: docker-image-marketplace
docker-image-marketplace: docker-image-marketplace-build



.PHONY: docker-image-marketplace-engine-build
docker-image-marketplace-engine-build:
	$(GRADLE_RUNNER) :service:marketplace_engine:jib

.PHONY: docker-image-marketplace-engine-push
docker-image-marketplace-engine-push:
	echo "No need to run this target"

.PHONY: docker-image-marketplace-engine-unpush
docker-image-marketplace-engine-unpush:
	echo "No need to run this target"

.PHONY: docker-image-marketplace-engine
docker-image-marketplace-engine: docker-image-marketplace-engine-push




.PHONY: docker-image-build
docker-image-build: \
	build-only \
	docker-image-maker-build \
	docker-image-loader-build \
	docker-image-customer-build \
	docker-image-seller-build \
	docker-image-shipping-build \
	docker-image-warehouse-build \
	docker-image-marketplace-build \
	docker-image-marketplace-engine-build

.PHONY: docker-image-push
docker-image-push: \
	docker-image-maker-push \
	docker-image-loader-push \
	docker-image-customer-push \
	docker-image-seller-push \
	docker-image-shipping-push \
	docker-image-warehouse-push \
	docker-image-marketplace-push \
	docker-image-marketplace-engine-push

.PHONY: docker-image-unpush
docker-image-unpush: \
	docker-image-maker-unpush \
	docker-image-loader-unpush \
	docker-image-customer-unpush \
	docker-image-seller-unpush \
	docker-image-shipping-unpush \
	docker-image-warehouse-unpush \
	docker-image-marketplace-unpush \
	docker-image-marketplace-engine-unpush

.PHONY: docker-image
docker-image: docker-image-build 

