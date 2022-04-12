#!/bin/bash
set -euo pipefail

java -cp @/app/jib-classpath-file $(cat /app/jib-main-class-file) $@
