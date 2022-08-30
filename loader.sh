#!/bin/bash
set -euo pipefail

export SPRING_PROFILES_ACTIVE=color

java -jar ./loader/build/libs/loader-0.0.1-SNAPSHOT.jar $@

