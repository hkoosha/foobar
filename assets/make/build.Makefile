
.PHONY: remake-build
remake-build:
	$(EDITOR) assets/make/build.Makefile


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
prepare: build-proto build-maker build-only 

.PHONY: check
check:
	$(GRADLE_RUNNER) detekt




.PHONY: build-proto
build-proto:
	$(GRADLE_RUNNER) :definitions:clean :definitions:build

.PHONY: build-maker
build-maker:
	$(GRADLE_RUNNER) :foobar-maker:build

.PHONY: build-loader
build-loader:
	$(GRADLE_RUNNER) :foobar-loader:build


