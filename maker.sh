#!/bin/bash
set -euo pipefail

export SPRING_PROFILES_ACTIVE=color,db-mysql

java -jar ./foobar-maker/build/libs/foobar-maker-0.0.1-SNAPSHOT.jar $@

