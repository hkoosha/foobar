
FOOBAR_SERVICES_YAML := assets/k8s/foobar


.PHONY: remake-k8s-foobar
remake-k8s-foobar:
	$(EDITOR) assets/make/k8s_foobar.Makefile



.PHONY: _k8s-deploy-loader-compile
_k8s-deploy-loader-compile:
	cat $(FOOBAR_SERVICES_YAML)/loader.yaml | envsubst > $(FOOBAR_SERVICES_YAML)/compiled/loader.yaml

.PHONY: k8s-deploy-loader
k8s-deploy-loader: _k8s-deploy-loader-compile
	kubectl -n $(FOOBAR_NAMESPACE) apply -f $(FOOBAR_SERVICES_YAML)/compiled/loader.yaml

.PHONY: k8s-undeploy-loader
k8s-undeploy-loader: _k8s-deploy-loader-compile
	kubectl -n $(FOOBAR_NAMESPACE) delete -f $(FOOBAR_SERVICES_YAML)/compiled/loader.yaml || true

.PHONY: k8s-redeploy-loader
k8s-redeploy-loader: k8s-undeploy-loader k8s-deploy-loader

.PHONY: remake-yaml-loader
remake-yaml-loader:
	$(EDITOR) $(FOOBAR_SERVICES_YAML)/loader.yaml



.PHONY: _k8s-deploy-maker-compile
_k8s-deploy-maker-compile:
	cat $(FOOBAR_SERVICES_YAML)/maker.yaml | envsubst > $(FOOBAR_SERVICES_YAML)/compiled/maker.yaml

.PHONY: k8s-deploy-maker
k8s-deploy-maker: _k8s-deploy-maker-compile
	kubectl -n $(FOOBAR_NAMESPACE) apply -f $(FOOBAR_SERVICES_YAML)/compiled/maker.yaml

.PHONY: k8s-undeploy-maker
k8s-undeploy-maker: _k8s-deploy-maker-compile
	kubectl -n $(FOOBAR_NAMESPACE) delete -f $(FOOBAR_SERVICES_YAML)/compiled/maker.yaml || true

.PHONY: k8s-redeploy-maker
k8s-redeploy-maker: k8s-undeploy-maker k8s-deploy-maker

.PHONY: remake-yaml-maker
remake-yaml-maker:
	$(EDITOR) $(FOOBAR_SERVICES_YAML)/maker.yaml



.PHONY: _k8s-deploy-customer-compile
_k8s-deploy-customer-compile:
	cat $(FOOBAR_SERVICES_YAML)/customer.yaml | envsubst > $(FOOBAR_SERVICES_YAML)/compiled/customer.yaml

.PHONY: k8s-deploy-customer
k8s-deploy-customer: _k8s-deploy-customer-compile
	kubectl -n $(FOOBAR_NAMESPACE) apply -f $(FOOBAR_SERVICES_YAML)/compiled/customer.yaml

.PHONY: k8s-undeploy-customer
k8s-undeploy-customer: _k8s-deploy-customer-compile
	kubectl -n $(FOOBAR_NAMESPACE) delete -f $(FOOBAR_SERVICES_YAML)/compiled/customer.yaml || true

.PHONY: k8s-redeploy-customer
k8s-redeploy-customer: k8s-undeploy-customer k8s-deploy-customer

.PHONY: remake-yaml-customer
remake-yaml-customer:
	$(EDITOR) $(FOOBAR_SERVICES_YAML)/customer.yaml



.PHONY: _k8s-deploy-seller-compile
_k8s-deploy-seller-compile:
	cat $(FOOBAR_SERVICES_YAML)/seller.yaml | envsubst > $(FOOBAR_SERVICES_YAML)/compiled/seller.yaml

.PHONY: k8s-deploy-seller
k8s-deploy-seller: _k8s-deploy-seller-compile
	kubectl -n $(FOOBAR_NAMESPACE) apply -f $(FOOBAR_SERVICES_YAML)/compiled/seller.yaml

.PHONY: k8s-undeploy-seller
k8s-undeploy-seller: _k8s-deploy-seller-compile
	kubectl -n $(FOOBAR_NAMESPACE) delete -f $(FOOBAR_SERVICES_YAML)/compiled/seller.yaml || true

.PHONY: k8s-redeploy-seller
k8s-redeploy-seller: k8s-undeploy-seller k8s-deploy-seller

.PHONY: remake-yaml-seller
remake-yaml-seller:
	$(EDITOR) $(FOOBAR_SERVICES_YAML)/seller.yaml



.PHONY: _k8s-deploy-warehouse-compile
_k8s-deploy-warehouse-compile:
	cat $(FOOBAR_SERVICES_YAML)/warehouse.yaml | envsubst > $(FOOBAR_SERVICES_YAML)/compiled/warehouse.yaml

.PHONY: k8s-deploy-warehouse
k8s-deploy-warehouse: _k8s-deploy-warehouse-compile
	kubectl -n $(FOOBAR_NAMESPACE) apply -f $(FOOBAR_SERVICES_YAML)/compiled/warehouse.yaml

.PHONY: k8s-undeploy-warehouse
k8s-undeploy-warehouse: _k8s-deploy-warehouse-compile
	kubectl -n $(FOOBAR_NAMESPACE) delete -f $(FOOBAR_SERVICES_YAML)/compiled/warehouse.yaml || true

.PHONY: k8s-redeploy-warehouse
k8s-redeploy-warehouse: k8s-undeploy-warehouse k8s-deploy-warehouse

.PHONY: remake-yaml-warehouse
remake-yaml-warehouse:
	$(EDITOR) $(FOOBAR_SERVICES_YAML)/warehouse.yaml



.PHONY: _k8s-deploy-shipping-compile
_k8s-deploy-shipping-compile:
	cat $(FOOBAR_SERVICES_YAML)/shipping.yaml | envsubst > $(FOOBAR_SERVICES_YAML)/compiled/shipping.yaml

.PHONY: k8s-deploy-shipping
k8s-deploy-shipping: _k8s-deploy-shipping-compile
	kubectl -n $(FOOBAR_NAMESPACE) apply -f $(FOOBAR_SERVICES_YAML)/compiled/shipping.yaml

.PHONY: k8s-undeploy-shipping
k8s-undeploy-shipping: _k8s-deploy-shipping-compile
	kubectl -n $(FOOBAR_NAMESPACE) delete -f $(FOOBAR_SERVICES_YAML)/compiled/shipping.yaml || true

.PHONY: k8s-redeploy-shipping
k8s-redeploy-shipping: k8s-undeploy-shipping k8s-deploy-shipping

.PHONY: remake-yaml-shipping
remake-yaml-shipping:
	$(EDITOR) $(FOOBAR_SERVICES_YAML)/shipping.yaml



.PHONY: _k8s-deploy-marketplace-compile
_k8s-deploy-marketplace-compile:
	cat $(FOOBAR_SERVICES_YAML)/marketplace.yaml | envsubst > $(FOOBAR_SERVICES_YAML)/compiled/marketplace.yaml

.PHONY: k8s-deploy-marketplace
k8s-deploy-marketplace: _k8s-deploy-marketplace-compile
	kubectl -n $(FOOBAR_NAMESPACE) apply -f $(FOOBAR_SERVICES_YAML)/compiled/marketplace.yaml

.PHONY: k8s-undeploy-marketplace
k8s-undeploy-marketplace:_k8s-deploy-marketplace-compile
	kubectl -n $(FOOBAR_NAMESPACE) delete -f $(FOOBAR_SERVICES_YAML)/compiled/marketplace.yaml || true

.PHONY: k8s-redeploy-marketplace
k8s-redeploy-marketplace: k8s-undeploy-marketplace k8s-deploy-marketplace

.PHONY: remake-yaml-marketplace
remake-yaml-marketplace:
	$(EDITOR) $(FOOBAR_SERVICES_YAML)/marketplace.yaml



.PHONY: _k8s-deploy-marketplace-engine-compile
_k8s-deploy-marketplace-engine-compile:
	cat $(FOOBAR_SERVICES_YAML)/marketplace_engine.yaml | envsubst > $(FOOBAR_SERVICES_YAML)/compiled/marketplace_engine.yaml

.PHONY: k8s-deploy-marketplace-engine
k8s-deploy-marketplace-engine: _k8s-deploy-marketplace-engine-compile
	kubectl -n $(FOOBAR_NAMESPACE) apply -f $(FOOBAR_SERVICES_YAML)/compiled/marketplace_engine.yaml

.PHONY: k8s-undeploy-marketplace-engine
k8s-undeploy-marketplace-engine: _k8s-deploy-marketplace-engine-compile
	kubectl -n $(FOOBAR_NAMESPACE) delete -f $(FOOBAR_SERVICES_YAML)/compiled/marketplace_engine.yaml || true

.PHONY: k8s-redeploy-marketplace-engine
k8s-redeploy-marketplace-engine: k8s-undeploy-marketplace-engine k8s-deploy-marketplace-engine

.PHONY: remake-yaml-marketplace-engine
remake-yaml-marketplace-engine:
	$(EDITOR) $(FOOBAR_SERVICES_YAML)/marketplace_engine.yaml


.PHONY: remake-yaml-foobar
remake-yaml-foobar:
	$(EDITOR) $(FOOBAR_SERVICES_YAML)/*.yaml


.PHONY: k8s-deploy
k8s-deploy: \
	k8s-deploy-customer \
	k8s-deploy-seller \
	k8s-deploy-warehouse \
	k8s-deploy-marketplace \
	k8s-deploy-marketplace-engine \
	k8s-deploy-shipping \
	k8s-deploy-maker

.PHONY: k8s-undeploy
k8s-undeploy: \
	k8s-undeploy-customer \
	k8s-undeploy-seller \
	k8s-undeploy-warehouse \
	k8s-undeploy-marketplace \
	k8s-undeploy-marketplace-engine \
	k8s-undeploy-shipping \
	k8s-undeploy-maker \
	k8s-undeploy-loader

.PHONY: k8s-redeploy
k8s-redeploy: \
	k8s-redeploy-customer \
	k8s-redeploy-seller \
	k8s-redeploy-warehouse \
	k8s-redeploy-marketplace \
	k8s-redeploy-marketplace-engine \
	k8s-redeploy-shipping \
	k8s-redeploy-maker



.PHONY: k8s-exec-maker-cli
k8s-exec-maker-cli:
	kubectl exec $(shell kubectl get pods --namespace $(FOOBAR_NAMESPACE) -l foobar-tool=maker -o jsonpath='{.items[0].metadata.name}') \
		--tty \
		-i \
		--namespace $(FOOBAR_NAMESPACE) \
		-- bash -c 'chmod +x /maker.sh && echo alias maker="/maker.sh" >> ~/.bashrc && bash'

.PHONY: k8s-exec-maker-demo
k8s-exec-maker-demo:
	kubectl exec $(shell kubectl get pods --namespace $(FOOBAR_NAMESPACE) -l foobar-tool=maker -o jsonpath='{.items[0].metadata.name}') \
		--tty \
		-i \
		--namespace $(FOOBAR_NAMESPACE) \
		-- bash -c 'chmod +x /maker.sh && /maker.sh ini --live'

.PHONY: k8s-delete-maker-cli
k8s-delete-maker-cli:
	kubectl delete pod foobar-maker --namespace $(FOOBAR_NAMESPACE)


.PHONY: k8s-port-forward-customer
k8s-port-forward-customer:
	kubectl port-forward \
      --namespace $(FOOBAR_NAMESPACE) \
      svc/foobar-customer \
	  4040:8080

.PHONY: k8s-port-forward-seller
k8s-port-forward-seller:
	kubectl port-forward \
      --namespace $(FOOBAR_NAMESPACE) \
      svc/foobar-seller \
	  4041:8080

.PHONY: k8s-port-forward-marketplace
k8s-port-forward-marketplace:
	kubectl port-forward \
      --namespace $(FOOBAR_NAMESPACE) \
      svc/foobar-marketplace \
	  4042:8080

.PHONY: k8s-port-forward-marketplace-engine
k8s-port-forward-marketplace-engine:
	kubectl port-forward \
      --namespace $(FOOBAR_NAMESPACE) \
      svc/foobar-marketplace-engine \
	  4043:8080

.PHONY: k8s-port-forward-shipping
k8s-port-forward-shipping:
	kubectl port-forward \
      --namespace $(FOOBAR_NAMESPACE) \
      svc/foobar-shipping \
	  4044:8080

.PHONY: k8s-port-forward-warehouse
k8s-port-forward-warehouse:
	kubectl port-forward \
      --namespace $(FOOBAR_NAMESPACE) \
      svc/foobar-warehouse \
	  4045:8080

.PHONY: k8s-port-forward-loader
k8s-port-forward-loader:
	kubectl port-forward \
      --namespace $(FOOBAR_NAMESPACE) \
      svc/foobar-loader \
	  4046:8080
