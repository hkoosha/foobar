SHELL := /bin/bash
.DEFAULT_GOAL := help


MYSQL_PASSWORD ?= .
MYSQL_USER ?= root
POSTGRES_PASSWORD ?= .
POSTGRES_USER ?= root

FOOBAR_K8S_SPRING_PROFILES ?= db-postgres,json-logging,expose,zipkin
FOOBAR_K8S_SPRING_PROFILES_KAFKA ?= db-postgres,json-logging,expose,zipkin,kafka

FOOBAR_MINIKUBE_MEMORY ?= 60g
FOOBAR_MINIKUBE_NUM_CPU ?= 16
FOOBAR_MINIKUBE_DRIVER ?= docker
FOOBAR_MINIKUBE_NODES ?= 4
FOOBAR_MINIKUBE_ADDONS ?= dashboard,storage-provisioner,registry
#FOOBAR_MINIKUBE_ADDONS ?= dashboard,storage-provisioner,ingress,registry,default-storageclass,volumesnapshots,csi-hostpath-driver
FOOBAR_DOCKER_IMAGE_VERSION ?= 0.0.1-SNAPSHOT

FOOBAR_REPLICAS ?= 3
FOOBAR_CUSTOMER_REPLICAS ?= ${FOOBAR_REPLICAS}
FOOBAR_MARKETPLACE_REPLICAS ?= ${FOOBAR_REPLICAS}
FOOBAR_MARKETPLACE_ENGINE_REPLICAS ?= ${FOOBAR_REPLICAS}
FOOBAR_SELLER_REPLICAS ?= ${FOOBAR_REPLICAS}
FOOBAR_SHIPPING_REPLICAS ?= ${FOOBAR_REPLICAS}
FOOBAR_WAREHOUSE_REPLICAS ?= ${FOOBAR_REPLICAS}
FOOBAR_LOADER_REPLICAS ?= ${FOOBAR_REPLICAS}

# Bad idea for production! but good for us on a demo app.
# TODO add this too Foobar.kt in buildSrc/...
FOOBAR_OTEL_TRACES_SAMPLER ?= always_on

FOOBAR_DOCKER_REGISTRY ?= localhost:5000/


ifeq ($(FOOBAR_FAST_DOCKER_REGISTRY),true)
	FOOBAR_DOCKER_IMAGE_BASE=host.minikube.internal:5000
	FOOBAR_DOCKER_IMAGE_PULL_POLICY=Always
else
	FOOBAR_DOCKER_IMAGE_BASE=docker.io/library/
	FOOBAR_DOCKER_IMAGE_PULL_POLICY=Never
endif


FOOBAR_NAMESPACE ?= foobar

GRADLE_RUNNER ?= ./gradlew --warning-mode all

ENV ?= localhost


include ./assets/env/_address.env
ifneq (,$(wildcard ./assets/env/${ENV}.env))
	include ./assets/env/${ENV}.env
endif


# include ./assets/legacy_maker/Makefile

include ./assets/make/build.Makefile
include ./assets/make/demo.Makefile
ifeq ($(FOOBAR_FAST_DOCKER_REGISTRY),true)
	include ./assets/make/foobar_docker_fast.Makefile
else
	include ./assets/make/foobar_docker.Makefile
endif
include ./assets/make/k8s_foobar.Makefile
include ./assets/make/k8s_services.Makefile
include ./assets/make/local_services.Makefile
include ./assets/make/local_foobar.Makefile


export



# Crazy regex here to match -> 'make-target: make-dependency-tatgets* # comment'
# where '#', 'comment', 'make-dependency-targets(zero or more)' and spaces are optional.
.PHONY: help
help: # try to print make targets using gawk
	@echo "Trying to print help... if this does not work, try '$(MAKE) help-alt'"
	@echo ''
	@echo 'If you want fast k8s deployments:'
	@echo '1. Run $(MAKE) docker-registry'
	@echo '2. Open port 5000 in your firewall'
	@echo '3. Set the env var FOOBAR_FAST_DOCKER_REGISTRY=true'
	@echo ''
	@echo ''
	@awk '{match($$0, /^([a-z0-9][a-zA-Z0-9\.\-\/]*):( *)([a-z0-9\-]* *)*(# .*)*$$/, m) } \
		{ print m[0];}' ${MAKEFILE_LIST} \
		| grep -Ev '^$$' \
		| sort \
		| sed 's/: \([a-z0-9-]* *\)*//g' \
		| sed 's/://g' \
		| column -t -s'#'

.PHONY: help-alt
help-alt: # try to print make targets using sed only
	@sed -n 's/^\([0-9a-z \-]*\):.*/\1/p' ${MAKEFILE_LIST} \
		| column -t -c 2 -s ':#' \
		| sort


.PHONY: about
about:
	@cat ./README.md

.PHONY: sos
sos:
	@cat ./README_STEPS_K8S.md



libs/opentelemetry-javaagent-1.17.0.jar: # get the OpenTelemetry library injected in docker containers as java agent
	wget \
		https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.17.0/opentelemetry-javaagent.jar \
		-O libs/opentelemetry-javaagent-1.17.0.jar


