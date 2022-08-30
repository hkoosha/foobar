
.PHONY: remake-local-foobar
remake-local-foobar:
	$(EDITOR) assets/make/local_foobar.Makefile


.PHONY: run-customer
run-customer:
	$(GRADLE_RUNNER) :service:customer:bootRun

.PHONY: run-seller
run-seller:
	$(GRADLE_RUNNER) :service:seller:bootRun

.PHONY: run-shipping
run-shipping:
	$(GRADLE_RUNNER) :service:shipping:bootRun

.PHONY: run-warehouse
run-warehouse:
	$(GRADLE_RUNNER) :service:warehouse:bootRun

.PHONY: run-marketplace
run-marketplace:
	$(GRADLE_RUNNER) :service:marketplace:bootRun

.PHONY: run-marketplace-engine
run-marketplace-engine:
	$(GRADLE_RUNNER) :service:marketplace_engine:bootRun

.PHONY: run-loader
run-loader:
	$(GRADLE_RUNNER) :loader:bootRun

	


.PHONY: debug-customer
debug-customer:
	$(GRADLE_RUNNER) :service:customer:bootRun --debug-jvm

.PHONY: debug-seller
debug-seller:
	$(GRADLE_RUNNER) :service:seller:bootRun --debug-jvm

.PHONY: debug-shipping
debug-shipping:
	$(GRADLE_RUNNER) :service:shipping:bootRun --debug-jvm

.PHONY: debug-warehouse
debug-warehouse:
	$(GRADLE_RUNNER) :service:warehouse:bootRun --debug-jvm

.PHONY: debug-marketplace
debug-marketplace:
	$(GRADLE_RUNNER) :service:marketplace:bootRun --debug-jvm

.PHONY: debug-marketplace-engine
debug-marketplace-engine:
	$(GRADLE_RUNNER) :service:marketplace_engine:bootRun --debug-jvm

.PHONY: debug-loader
debug-loader:
	$(GRADLE_RUNNER) :loader:bootRun --debug-jvm

