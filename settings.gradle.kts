rootProject.name = "foobar"

include(
    "definitions",
    "common",

    "service:customer",
    "service:seller",
    "service:marketplace",
    "service:marketplace_engine",
    "service:warehouse",
    "service:shipping",

    "foobar-maker",
    "foobar-maker-exporter",
    "foobar-loader",
)
