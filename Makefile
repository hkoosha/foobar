SHELL := /bin/bash
.DEFAULT_GOAL := sos


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


.PHONY: sos
sos:
	@sed -n 's/^\([0-9a-z \-]*\):.*/\1/p' ${MAKEFILE_LIST} | column -t -c 2 -s ':#' | sort | grep -v remake

.PHONY: sos-full
sos-full:
	@sed -n 's/^\([0-9a-z \-]*\):.*/\1/p' ${MAKEFILE_LIST} | column -t -c 2 -s ':#' | sort




libs/opentelemetry-javaagent-1.17.0.jar:
	wget https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.17.0/opentelemetry-javaagent.jar -O libs/opentelemetry-javaagent-1.17.0.jar





.PHONY: remake-build
remake-build:
	vim assets/make/build.Makefile

.PHONY: remake-demo
remake-demo:
	vim assets/make/demo.Makefile

.PHONY: remake-foobar-docker
remake-foobar-docker:
	vim assets/make/foobar_docker.Makefile

.PHONY: remake-k8s-foobar
remake-k8s-foobar:
	vim assets/make/k8s_foobar.Makefile

.PHONY: remake-k8s-services
remake-k8s-services:
	vim assets/make/k8s_services.Makefile

.PHONY: remake-local-services
remake-local-services:
	vim assets/make/local_services.Makefile

.PHONY: remake-local-foobar
remake-local-foobar:
	vim assets/make/local_foobar.Makefile

