
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
	$(EDITOR) assets/make/foobar_docker.Makefile


define _foobar_push_image
	if minikube image ls | grep $(1); then \
		echo image is not unpushed yet: $(1) && exit 1; \
	else \
		minikube image load $(1); \
	fi
endef



.PHONY: docker-image-maker-build
docker-image-maker-build:
	$(GRADLE_RUNNER) :foobar-maker:jibDockerBuild

.PHONY: docker-image-maker-push
docker-image-maker-push:
	$(call _foobar_push_image,$(FOOBAR_MAKER_IMAGE))

.PHONY: docker-image-maker-unpush
docker-image-maker-unpush:
	minikube image rm foobar-maker:0.0.1-SNAPSHOT || true

.PHONY: docker-image-maker
docker-image-maker: \
	docker-image-maker-build \
	docker-image-maker-unpush \
	docker-image-maker-push



.PHONY: docker-image-loader-build
docker-image-loader-build:
	$(GRADLE_RUNNER) :foobar-loader:jibDockerBuild

.PHONY: docker-image-loader-push
docker-image-loader-push:
	$(call _foobar_push_image,$(FOOBAR_LOADER_IMAGE))

.PHONY: docker-image-loader-unpush
docker-image-loader-unpush:
	minikube image rm foobar-loader:0.0.1-SNAPSHOT || true

.PHONY: docker-image-loader
docker-image-loader: \
	docker-image-loader-build \
	docker-image-loader-unpush \
	docker-image-loader-push



.PHONY: docker-image-customer-build
docker-image-customer-build:
	$(GRADLE_RUNNER) :service:customer:jibDockerBuild

.PHONY: docker-image-customer-push
docker-image-customer-push:
	$(call _foobar_push_image,$(FOOBAR_CUSTOMER_IMAGE))

.PHONY: docker-image-customer-unpush
docker-image-customer-unpush:
	minikube image rm foobar-customer:0.0.1-SNAPSHOT || true

.PHONY: docker-image-customer
docker-image-customer: \
	docker-image-customer-build \
	docker-image-customer-unpush \
	docker-image-customer-push



.PHONY: docker-image-seller-build
docker-image-seller-build:
	$(GRADLE_RUNNER) :service:seller:jibDockerBuild

.PHONY: docker-image-seller-push
docker-image-seller-push:
	$(call _foobar_push_image,$(FOOBAR_SELLER_IMAGE))

.PHONY: docker-image-seller-unpush
docker-image-seller-unpush:
	minikube image rm foobar-seller:0.0.1-SNAPSHOT || true

.PHONY: docker-image-seller
docker-image-seller: \
	docker-image-seller-build \
	docker-image-seller-unpush \
	docker-image-seller-push



.PHONY: docker-image-shipping-build
docker-image-shipping-build:
	$(GRADLE_RUNNER) :service:shipping:jibDockerBuild

.PHONY: docker-image-shipping-push
docker-image-shipping-push:
	$(call _foobar_push_image,$(FOOBAR_SHIPPING_IMAGE))

.PHONY: docker-image-shipping-unpush
docker-image-shipping-unpush:
	minikube image rm foobar-shipping:0.0.1-SNAPSHOT || true

.PHONY: docker-image-shipping
docker-image-shipping: \
	docker-image-shipping-build \
	docker-image-shipping-unpush \
	docker-image-shipping-push



.PHONY: docker-image-warehouse-build
docker-image-warehouse-build:
	$(GRADLE_RUNNER) :service:warehouse:jibDockerBuild

.PHONY: docker-image-warehouse-push
docker-image-warehouse-push:
	$(call _foobar_push_image,$(FOOBAR_WAREHOUSE_IMAGE))

.PHONY: docker-image-warehouse-unpush
docker-image-warehouse-unpush:
	minikube image rm foobar-warehouse:0.0.1-SNAPSHOT || true

.PHONY: docker-image-warehouse
docker-image-warehouse: \
	docker-image-warehouse-build \
	docker-image-warehouse-unpush \
	docker-image-warehouse-push



.PHONY: docker-image-marketplace-build
docker-image-marketplace-build:
	$(GRADLE_RUNNER) :service:marketplace:jibDockerBuild

.PHONY: docker-image-marketplace-push
docker-image-marketplace-push:
	$(call _foobar_push_image,$(FOOBAR_MARKETPLACE_IMAGE))

.PHONY: docker-image-marketplace-unpush
docker-image-marketplace-unpush:
	minikube image rm foobar-marketplace:0.0.1-SNAPSHOT || true

.PHONY: docker-image-marketplace
docker-image-marketplace: \
	docker-image-marketplace-build \
	docker-image-marketplace-unpush \
	docker-image-marketplace-push



.PHONY: docker-image-marketplace-engine-build
docker-image-marketplace-engine-build:
	$(GRADLE_RUNNER) :service:marketplace_engine:jibDockerBuild

.PHONY: docker-image-marketplace-engine-push
docker-image-marketplace-engine-push:
	$(call _foobar_push_image,$(FOOBAR_MARKETPLACE_ENGINE_IMAGE))

.PHONY: docker-image-marketplace-engine-unpush
docker-image-marketplace-engine-unpush:
	minikube image rm foobar-marketplace-engine:0.0.1-SNAPSHOT || true

.PHONY: docker-image-marketplace-engine
docker-image-marketplace-engine: \
	docker-image-marketplace-engine-build \
	docker-image-marketplace-engine-unpush \
	docker-image-marketplace-engine-push




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
docker-image: \
	docker-image-build \
	docker-image-unpush \
	docker-image-push

