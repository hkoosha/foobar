
.PHONY: remake-foobar-docker
remake-foobar-docker:
	$(EDITOR) assets/make/foobar_docker.Makefile


.PHONY: docker-image-maker-build
docker-image-maker-build:
	$(GRADLE_RUNNER) :foobar-maker:jibDockerBuild

.PHONY: docker-image-maker-push
docker-image-maker-push:
	minikube image load foobar-maker:0.0.1-SNAPSHOT

.PHONY: docker-image-maker-unpush
docker-image-maker-unpush:
	minikube image rm foobar-maker:0.0.1-SNAPSHOT || true

.PHONY: docker-image-maker
docker-image-maker: \
	docker-image-maker-build \
	docker-image-maker-unpush \
	docker-image-maker-push



.PHONY: docker-image-customer-build
docker-image-customer-build:
	$(GRADLE_RUNNER) :service:customer:jibDockerBuild

.PHONY: docker-image-customer-push
docker-image-customer-push:
	minikube image load foobar-customer:0.0.1-SNAPSHOT

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
	minikube image load foobar-seller:0.0.1-SNAPSHOT

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
	minikube image load foobar-shipping:0.0.1-SNAPSHOT

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
	minikube image load foobar-warehouse:0.0.1-SNAPSHOT

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
	minikube image load foobar-marketplace:0.0.1-SNAPSHOT

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
	minikube image load foobar-marketplace-engine:0.0.1-SNAPSHOT

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
	docker-image-customer-build \
	docker-image-seller-build \
	docker-image-shipping-build \
	docker-image-warehouse-build \
	docker-image-marketplace-build \
	docker-image-marketplace-engine-build

.PHONY: docker-image-push
docker-image-push: \
	docker-image-maker-push \
	docker-image-customer-push \
	docker-image-seller-push \
	docker-image-shipping-push \
	docker-image-warehouse-push \
	docker-image-marketplace-push \
	docker-image-marketplace-engine-push

.PHONY: docker-image-unpush
docker-image-unpush: \
	docker-image-maker-unpush \
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

