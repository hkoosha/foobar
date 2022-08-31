
.PHONY: remake-k8s-grafana
remake-k8s-grafana:
	$(EDITOR) assets/make/k8s_grafana.Makefile


.PHONY: k8s-grafana-create-datasource
k8s-grafana-create-datasource:
	kubectl -n $(FOOBAR_NAMESPACE) create secret generic foobar-grafana-datasource \
		--from-file=./assets/grafana/datasource.yaml
	 
.PHONY: k8s-grafana-create-dashboards
k8s-grafana-create-dashboards:
	kubectl -n $(FOOBAR_NAMESPACE) create configmap foobar-dash-jvm-customer \
		--from-file=./assets/grafana/jvm-micrometer_rev1.json
	 

