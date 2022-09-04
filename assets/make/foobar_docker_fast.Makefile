FOOBAR_LOADER_IMAGE_TAG := 0.0.1-SNAPSHOT
FOOBAR_MAKER_IMAGE_TAG := 0.0.1-SNAPSHOT
FOOBAR_MAKER_EXPORTER_IMAGE_TAG := 0.0.1-SNAPSHOT
FOOBAR_CUSTOMER_IMAGE_TAG := 0.0.1-SNAPSHOT
FOOBAR_SELLER_IMAGE_TAG := 0.0.1-SNAPSHOT
FOOBAR_WAREHOUSE_IMAGE_TAG := 0.0.1-SNAPSHOT
FOOBAR_SHIPPING_IMAGE_TAG := 0.0.1-SNAPSHOT
FOOBAR_MARKETPLACE_IMAGE_TAG := 0.0.1-SNAPSHOT
FOOBAR_MARKETPLACE_ENGINE_IMAGE_TAG := 0.0.1-SNAPSHOT

FOOBAR_LOADER_IMAGE := foobar-loader
FOOBAR_MAKER_IMAGE := foobar-maker
FOOBAR_MAKER_EXPORTER_IMAGE := foobar-maker-exporter
FOOBAR_CUSTOMER_IMAGE := foobar-customer
FOOBAR_SELLER_IMAGE := foobar-seller
FOOBAR_WAREHOUSE_IMAGE := foobar-warehouse
FOOBAR_SHIPPING_IMAGE := foobar-shipping
FOOBAR_MARKETPLACE_IMAGE := foobar-marketplace
FOOBAR_MARKETPLACE_ENGINE_IMAGE := foobar-marketplace-engine



.PHONY: remake-foobar-docker
remake-foobar-docker:
	$(EDITOR) assets/make/foobar_docker_fast.Makefile



# https://github.com/distribution/distribution/blob/main/docs/spec/api.md#deleting-an-image
#
.PHONY: _docker-image-unpush
_docker-image-unpush:
	curl -L -XDELETE "http://localhost:5000/v2/$(_FOOBAR_UNPUSH_IMAGE)/manifests/$(shell curl \
		--silent -LI \
		-H 'Accept: application/vnd.docker.distribution.manifest.v2+json' \
		localhost:5000/v2/$(_FOOBAR_UNPUSH_IMAGE)/manifests/$(_FOOBAR_UNPUSH_TAG) | \
		grep Docker-Content-Digest | \
		cut -f2- -d: | \
		tr -cd '[:print:]' | tr -d ' ')"
	curl -L "http://localhost:5000/v2/$(_FOOBAR_UNPUSH_IMAGE)/tags/list"



.PHONY: docker-image-maker-build
docker-image-maker-build:
	$(GRADLE_RUNNER) :foobar-maker:jib

.PHONY: docker-image-maker-push
docker-image-maker-push:
	echo "No need to run this target"

.PHONY: docker-image-maker-unpush
docker-image-maker-unpush:
	$(MAKE) _docker-image-unpush \
		_FOOBAR_UNPUSH_IMAGE=$(FOOBAR_MAKER_IMAGE) \
		_FOOBAR_UNPUSH_TAG=$(FOOBAR_MAKER_IMAGE_TAG)

.PHONY: docker-image-maker
docker-image-maker: docker-image-maker-unpush docker-image-maker-build



.PHONY: docker-image-maker-exporter-build
docker-image-maker-exporter-build:
	$(GRADLE_RUNNER) :foobar-maker-exporter:jib

.PHONY: docker-image-maker-exporter-push
docker-image-maker-exporter-push:
	echo "No need to run this target"

.PHONY: docker-image-maker-exporter-unpush
docker-image-maker-exporter-unpush:
	$(MAKE) _docker-image-unpush \
		_FOOBAR_UNPUSH_IMAGE=$(FOOBAR_MAKER_EXPORTER_IMAGE) \
		_FOOBAR_UNPUSH_TAG=$(FOOBAR_MAKER_EXPORTER_IMAGE_TAG)

.PHONY: docker-image-maker-exporter
docker-image-maker-exporter: docker-image-maker-exporter-unpush docker-image-maker-exporter-build



.PHONY: docker-image-loader-build
docker-image-loader-build:
	$(GRADLE_RUNNER) :loader:jib

.PHONY: docker-image-loader-push
docker-image-loader-push:
	echo "No need to run this target"

.PHONY: docker-image-loader-unpush
docker-image-loader-unpush:
	$(MAKE) _docker-image-unpush \
		_FOOBAR_UNPUSH_IMAGE=$(FOOBAR_LOADER_IMAGE) \
		_FOOBAR_UNPUSH_TAG=$(FOOBAR_LOADER_IMAGE_TAG)

.PHONY: docker-image-loader
docker-image-loader: docker-image-loader-unpush docker-image-loader-build



.PHONY: docker-image-customer-build
docker-image-customer-build:
	$(GRADLE_RUNNER) :service:customer:jib

.PHONY: docker-image-customer-push
docker-image-customer-push:
	echo "No need to run this target"

.PHONY: docker-image-customer-unpush
docker-image-customer-unpush:
	$(MAKE) _docker-image-unpush \
		_FOOBAR_UNPUSH_IMAGE=$(FOOBAR_CUSTOMER_IMAGE) \
		_FOOBAR_UNPUSH_TAG=$(FOOBAR_CUSTOMER_IMAGE_TAG)

.PHONY: docker-image-customer
docker-image-customer: docker-image-customer-unpush docker-image-customer-build



.PHONY: docker-image-seller-build
docker-image-seller-build:
	$(GRADLE_RUNNER) :service:seller:jib

.PHONY: docker-image-seller-push
docker-image-seller-push:
	echo "No need to run this target"

.PHONY: docker-image-seller-unpush
docker-image-seller-unpush:
	$(MAKE) _docker-image-unpush \
		_FOOBAR_UNPUSH_IMAGE=$(FOOBAR_SELLER_IMAGE) \
		_FOOBAR_UNPUSH_TAG=$(FOOBAR_SELLER_IMAGE_TAG)

.PHONY: docker-image-seller
docker-image-seller: docker-image-seller-unpush docker-image-seller-build



.PHONY: docker-image-shipping-build
docker-image-shipping-build:
	$(GRADLE_RUNNER) :service:shipping:jib

.PHONY: docker-image-shipping-push
docker-image-shipping-push:
	echo "No need to run this target"

.PHONY: docker-image-shipping-unpush
docker-image-shipping-unpush:
	$(MAKE) _docker-image-unpush \
		_FOOBAR_UNPUSH_IMAGE=$(FOOBAR_SHIPPING_IMAGE) \
		_FOOBAR_UNPUSH_TAG=$(FOOBAR_SHIPPING_IMAGE_TAG)

.PHONY: docker-image-shipping
docker-image-shipping: docker-image-shipping-unpush docker-image-shipping-build



.PHONY: docker-image-warehouse-build
docker-image-warehouse-build:
	$(GRADLE_RUNNER) :service:warehouse:jib

.PHONY: docker-image-warehouse-push
docker-image-warehouse-push:
	echo "No need to run this target"

.PHONY: docker-image-warehouse-unpush
docker-image-warehouse-unpush:
	$(MAKE) _docker-image-unpush \
		_FOOBAR_UNPUSH_IMAGE=$(FOOBAR_WAREHOUSE_IMAGE) \
		_FOOBAR_UNPUSH_TAG=$(FOOBAR_WAREHOUSE_IMAGE_TAG)

.PHONY: docker-image-warehouse
docker-image-warehouse: docker-image-warehouse-unpush docker-image-warehouse-build



.PHONY: docker-image-marketplace-build
docker-image-marketplace-build:
	$(GRADLE_RUNNER) :service:marketplace:jib

.PHONY: docker-image-marketplace-push
docker-image-marketplace-push:
	echo "No need to run this target"

.PHONY: docker-image-marketplace-unpush
docker-image-marketplace-unpush:
	$(MAKE) _docker-image-unpush \
		_FOOBAR_UNPUSH_IMAGE=$(FOOBAR_MARKETPLACE_IMAGE) \
		_FOOBAR_UNPUSH_TAG=$(FOOBAR_MARKETPLACE_IMAGE_TAG)

.PHONY: docker-image-marketplace
docker-image-marketplace: docker-image-marketplace-unpush docker-image-marketplace-build



.PHONY: docker-image-marketplace-engine-build
docker-image-marketplace-engine-build:
	$(GRADLE_RUNNER) :service:marketplace_engine:jib

.PHONY: docker-image-marketplace-engine-push
docker-image-marketplace-engine-push:
	echo "No need to run this target"

.PHONY: docker-image-marketplace-engine-unpush
docker-image-marketplace-engine-unpush:
	$(MAKE) _docker-image-unpush \
		_FOOBAR_UNPUSH_IMAGE=$(FOOBAR_MARKETPLACE_ENGINE_IMAGE) \
		_FOOBAR_UNPUSH_TAG=$(FOOBAR_MARKETPLACE_ENGINE_IMAGE_TAG)

.PHONY: docker-image-marketplace-engine
docker-image-marketplace-engine: docker-image-marketplace-engine-unpush docker-image-marketplace-engine-build




.PHONY: docker-image-build
docker-image-build: \
	build-only \
	docker-image-maker-build \
	docker-image-maker-exporter-build \
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
	docker-image-maker-exporter-push \
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
	docker-image-maker-exporter-unpush \
	docker-image-loader-unpush \
	docker-image-customer-unpush \
	docker-image-seller-unpush \
	docker-image-shipping-unpush \
	docker-image-warehouse-unpush \
	docker-image-marketplace-unpush \
	docker-image-marketplace-engine-unpush

.PHONY: docker-image
docker-image: docker-image-unpush docker-image-build 

