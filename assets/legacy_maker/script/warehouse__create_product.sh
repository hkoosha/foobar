#!/bin/bash
set -euo pipefail
MY_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source "$MY_DIR/config.sh"


readonly MY_URL="${WAREHOUSE_URL}"
readonly MY_ID_PROP="${WAREHOUSE__PRODUCT__ID_PROP}"
readonly MY_DATA_FILE="${WAREHOUSE__PRODUCT__DATA_FILE}"
readonly MY_OUTPUT_FILE="${WAREHOUSE__PRODUCT__OUTPUT_FILE}"
readonly MY_ID_FILE="${WAREHOUSE__PRODUCT__ID_FILE}"


rm -f "${MY_OUTPUT_FILE}"
rm -f "${MY_ID_FILE}"

http -v "${MY_URL}" \
     -d -o "${MY_OUTPUT_FILE}" \
     < "${MY_DATA_FILE}" \
     || \
(http -v "${MY_URL}" \
    < "${MY_DATA_FILE}" && exit 1)
cat "${MY_OUTPUT_FILE}" | python -m json.tool

echo $(jq "${MY_ID_PROP}" "${MY_OUTPUT_FILE}") | \
     tr -d '"' > "${MY_ID_FILE}"
cat "${MY_ID_FILE}"

