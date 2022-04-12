#!/bin/bash
set -euo pipefail

MY_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source "$MY_DIR/config.sh"


readonly MY_TARGET_STATE="${1}"
readonly MY_URL="${MARKETPLACE_URL}"
readonly MY_DATA_FILE="${MARKETPLACE__ORDER_REQUEST__UPDATE_DATA_FILE}"
readonly MY_OUTPUT_FILE="${MARKETPLACE__ORDER_REQUEST__UPDATE_STATE_OUTPUT_FILE}"
readonly THEIR_ID_FILE0="${MARKETPLACE__ORDER_REQUEST__ID_FILE}"


rm -f "${MY_OUTPUT_FILE}"

readonly MY_ID=$(cat "${THEIR_ID_FILE0}")

cat "${MY_DATA_FILE}" | \
    sed "s/<<STATE>>/${MY_TARGET_STATE}/g" \
    > "${TEMPLATE_OUTPUT}"

http -v PATCH "${MY_URL}/${MY_ID}" \
     -d -o "${MY_OUTPUT_FILE}" \
     < "${TEMPLATE_OUTPUT}" \
     || \
(http -v PATCH "${MY_URL}/${MY_ID}" \
    < "${TEMPLATE_OUTPUT}" && exit 1)
echo
cat "${MY_OUTPUT_FILE}" | python -m json.tool

