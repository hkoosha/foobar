#!/bin/bash
set -euo pipefail
MY_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source "$MY_DIR/config.sh"


readonly MY_URL="${CUSTOMER_URL}"
readonly THEIR_ID_FILE0="${CUSTOMER__CUSTOMER__ID_FILE}"
readonly THEIR_ID_FILE1="${CUSTOMER__ADDRESS__ID_FILE}"
readonly MY_ID=$(cat "${THEIR_ID_FILE0}")
readonly MY_ID1=$(cat "${THEIR_ID_FILE1}")


http -v "${MY_URL}/${MY_ID}/addresses/${MY_ID1}"

