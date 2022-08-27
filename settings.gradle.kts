rootProject.name = "foobar"

include(
    "foobar-gen",

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
    "connect:marketplace-api",
    "connect:marketplace-api-build",
    "connect:warehouse-api",
    "connect:warehouse-api-build",

    "foobar-maker",
    "foobar-evil",
)
