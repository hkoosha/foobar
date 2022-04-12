
.PHONY: stop-gradle
stop-gradle:
	$(GRADLE_RUNNER) --stop

.PHONY: build
build:
	$(GRADLE_RUNNER) build

.PHONY: build-only
build-only:
	$(GRADLE_RUNNER) build -xtest

.PHONY: test
test:
	$(GRADLE_RUNNER) test

.PHONY: clean
clean:
	$(GRADLE_RUNNER) clean

.PHONY: retest
retest:
	$(GRADLE_RUNNER) --rerun-tasks test

.PHONY: prepare
prepare: build-proto build-generator build-api build-maker build-only 




.PHONY: build-proto
build-proto:
	$(GRADLE_RUNNER) :definitions:clean :definitions:build

.PHONY: build-maker
build-maker:
	$(GRADLE_RUNNER) :foobar-maker:build




.PHONY: build-api-generator
build-api-generator:
	$(GRADLE_RUNNER) :foobar-gen:clean :foobar-gen:build
	cp ./foobar-gen/build/libs/foobar-gen-0.0.1-SNAPSHOT.jar libs/foobar-gen.jar


.PHONY: api-customer
api-customer: build-api-generator
	$(GRADLE_RUNNER) \
		:service:customer:generateOpenApiDocs \
		:connect:customer-api:foobar-clean-api-build \
		:connect:customer-api:openApiGenerate

.PHONY: api-seller
api-seller: build-api-generator
	$(GRADLE_RUNNER) \
		:service:seller:generateOpenApiDocs \
		:connect:seller-api:foobar-clean-api-build \
		:connect:seller-api:openApiGenerate

.PHONY: api-warehouse
api-warehouse: build-api-generator
	$(GRADLE_RUNNER) \
		:service:warehouse:generateOpenApiDocs \
		:connect:warehouse-api:foobar-clean-api-build \
		:connect:warehouse-api:openApiGenerate

.PHONY: api-marketplace
api-marketplace: build-api-generator
	$(GRADLE_RUNNER) \
		:service:marketplace:generateOpenApiDocs \
		:connect:marketplace-api:foobar-clean-api-build \
		:connect:marketplace-api:openApiGenerate

.PHONY: build-api
build-api: api-customer api-seller api-warehouse api-marketplace

