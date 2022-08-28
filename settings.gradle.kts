rootProject.name = "foobar"

include(
    "foobar-gen",
    "foobar-gen-rx",

    "definitions",
    "common",
    "common-jpa",

    "service:common-kafka",
    "service:common-service",
    "service:common-web",

    "service:customer",
    "service:seller",
    "service:marketplace",
    "service:marketplace_engine",
    "service:warehouse",
    "service:shipping",

    "connect:customer-api",
    "connect:customer-api-build",
    "connect:seller-api",
    "connect:seller-api-build",
    "connect:warehouse-api",
    "connect:warehouse-api-build",
    "connect:marketplace-api",
    "connect:marketplace-api-build",

    "connect:rx-customer-api",
    "connect:rx-customer-api-build",
    "connect:rx-seller-api",
    "connect:rx-seller-api-build",
    "connect:rx-warehouse-api",
    "connect:rx-warehouse-api-build",

    "foobar-maker",
    "foobar-evil",
)
