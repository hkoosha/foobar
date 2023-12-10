#!/bin/bash
set -euo pipefail

export SPRING_PROFILES_ACTIVE=color

java -jar ./foobar-loader/build/libs/foobar-loader-0.0.1-SNAPSHOT.jar $@

