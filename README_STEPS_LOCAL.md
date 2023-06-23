```bash
make clean \
  libs/opentelemetry-javaagent-1.27.0.jar \
  build-proto \
  build-api-generator

ENV=no_db make build-api
make build
  
make local-run-dependencies
make local-init-my-create-db
make local-init-pg-create-db
make local-init-create-topics

make run-customer
make run-seller
make run-warehouse
make run-marketplace
make run-marketplace-engine
make run-shipping

./maker.sh init --live

# ./loader.sh
```
