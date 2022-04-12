#!/bin/bash
set -euo pipefail
MY_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source "$MY_DIR/config.sh"


readonly MY_URL="${SELLER_URL}"
readonly THEIR_ID_FILE="${SELLER__SELLER__ID_FILE}"
readonly MY_ID=$(cat "${THEIR_ID_FILE}")

http -v "${MY_URL}/${MY_ID}"

