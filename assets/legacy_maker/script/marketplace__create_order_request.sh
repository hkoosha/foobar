#!/bin/bash
set -euo pipefail
MY_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source "$MY_DIR/config.sh"


readonly MY_URL="${MARKETPLACE_URL}"
readonly MY_ID_PROP="${MARKETPLACE__ORDER_REQUEST__ID_PROP}"
readonly MY_DATA_FILE="${MARKETPLACE__ORDER_REQUEST__DATA_FILE}"
readonly MY_OUTPUT_FILE="${MARKETPLACE__ORDER_REQUEST__OUTPUT_FILE}"
readonly MY_ID_FILE="${MARKETPLACE__ORDER_REQUEST__ID_FILE}"

readonly MY_CUSTOMER_ID=$( cat "${CUSTOMER__ADDRESS__INPUT_ID_FILE}" )


cat "${MY_DATA_FILE}" | \
    sed "s/<<CUSTOMER_ID>>/${MY_CUSTOMER_ID}/g" \
    > "${TEMPLATE_OUTPUT}"


rm -f "${MY_OUTPUT_FILE}"
rm -f "${MY_ID_FILE}"

http -v "${MY_URL}" \
     -d -o "${MY_OUTPUT_FILE}" \
     < "${TEMPLATE_OUTPUT}" \
     || \
(http -v "${MY_URL}" \
    < "${TEMPLATE_OUTPUT}" && exit 1)
cat "${MY_OUTPUT_FILE}" | python -m json.tool

echo $(jq "${MY_ID_PROP}" "${MY_OUTPUT_FILE}") | \
     tr -d '"' > "${MY_ID_FILE}"
cat "${MY_ID_FILE}"
