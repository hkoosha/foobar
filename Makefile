SHELL := /bin/bash
.DEFAULT_GOAL := help


MYSQL_PASSWORD := .
MYSQL_USER := root

FOOBAR_MINIKUBE_MEMORY := 40g
FOOBAR_MINIKUBE_NUM_CPU := 16
FOOBAR_MINIKUBE_DRIVER := docker
FOOBAR_MINIKUBE_NODES := 8
FOOBAR_MINIKUBE_ADDONS := dashboard,storage-provisioner,ingress,registry
#FOOBAR_MINIKUBE_ADDONS := dashboard,storage-provisioner,ingress,registry,default-storageclass,volumesnapshots,csi-hostpath-driver
FOOBAR_DOCKER_IMAGE_VERSION := 0.0.1-SNAPSHOT

FOOBAR_REPLICAS=3
FOOBAR_CUSTOMER_REPLICAS=${FOOBAR_REPLICAS}
FOOBAR_MARKETPLACE_REPLICAS=${FOOBAR_REPLICAS}
FOOBAR_MARKETPLACE_ENGINE_REPLICAS=${FOOBAR_REPLICAS}
FOOBAR_SELLER_REPLICAS=${FOOBAR_REPLICAS}
FOOBAR_SHIPPING_REPLICAS=${FOOBAR_REPLICAS}
FOOBAR_WAREHOUSE_REPLICAS=${FOOBAR_REPLICAS}

# Bad idea for production! but good for us on a demo app.
# TODO add this too Foobar.kt in buildSrc/...
FOOBAR_OTEL_TRACES_SAMPLER=always_on

export


FOOBAR_NAMESPACE := foobar

GRADLE_RUNNER ?= ./gradlew --warning-mode all

ENV ?= localhost


# include ./assets/legacy_maker/Makefile
include ./assets/make/build.Makefile
include ./assets/make/demo.Makefile
include ./assets/make/foobar_docker.Makefile
include ./assets/make/k8s_foobar.Makefile
include ./assets/make/k8s_services.Makefile
include ./assets/make/local_services.Makefile
include ./assets/make/local_foobar.Makefile




ifneq (,$(wildcard ./assets/env/${ENV}.env))
	include ./assets/env/${ENV}.env
	export
endif


.PHONY: help
help: # print available help commands
	@echo "Try '$(MAKE) help0' which required gawk, if that does not work, try '$(MAKE) help1'"

.PHONY: help0
help0: # try to print make targets using gawk
	@awk '{match($$0, /^([a-z0-9][a-zA-Z0-9\.\-\/]*):( *)(# .*)*$$/, m) } { print m[0];}' ${MAKEFILE_LIST} | grep -Ev '^$$' | sort | column -t -s'#'

.PHONY: help1
help1: # try to print make targets using sed
	@sed -n 's/^\([0-9a-z \-]*\):.*/\1/p' ${MAKEFILE_LIST} | column -t -c 2 -s ':#' | sort



libs/opentelemetry-javaagent-1.17.0.jar: # get the OpenTelemetry library injected in docker containers as java agent
	wget \
		https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.17.0/opentelemetry-javaagent.jar \
		-O libs/opentelemetry-javaagent-1.17.0.jar


