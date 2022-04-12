

.PHONY: demo
demo:
	@echo "please make sure all databases are created (aka '$(MAKE) db-recreate' or '$(MAKE) db-create')"
	@echo "please make sure all dependencies are prepared (aka '$(MAKE) prepare')"
	@echo "please make sure you have the opentelemetry javaagent downloaded (aka '$(MAKE) libs/opentelemetry-javaagent-1.17.0.jar')"
	@echo "please make sure all services are running (aka '$(MAKE) run-dependencies', '$(MAKE) run-customer', '$(MAKE) run-seller', etc...)"
	./maker.sh ini
	./maker.sh orde patch --state=LIVE

.PHONY: maker
maker:
	./maker.sh ini
	./maker.sh orde patch --state=LIVE

