================================
==== RUNNING FOOBAR LOCALLY ====
================================

```bash
make clean \
  libs/opentelemetry-javaagent-1.32.0.jar \
  build-proto \
  build
  
make local-run-dependencies
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
