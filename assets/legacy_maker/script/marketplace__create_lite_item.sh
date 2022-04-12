#!/bin/bash
set -euo pipefail

MY_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source "$MY_DIR/config.sh"


readonly MY_URL="${MARKETPLACE_URL}"
readonly MY_ID_PROP="${CUSTOMER__ADDRESS__ID_PROP}"
readonly MY_DATA_FILE="${MARKETPLACE__LINE_ITEM__DATA_FILE}"
readonly MY_OUTPUT_FILE="${MARKETPLACE__LINE_ITEM__OUTPUT_FILE}"
readonly MY_ID_FILE="${MARKETPLACE__LINE_ITEM__ID_FILE}"
readonly THEIR_ID_FILE0="${MARKETPLACE__ORDER_REQUEST__ID_FILE}"
readonly THEIR_ID_FILE1="${WAREHOUSE__PRODUCT__ID_FILE}"


rm -f "${MY_OUTPUT_FILE}"
rm -f "${MY_ID_FILE}"

readonly MY_ID=$(cat "${THEIR_ID_FILE0}")

readonly MY_PRODUCT_ID=$(cat "${THEIR_ID_FILE1}")

cat "${MY_DATA_FILE}" | \
    sed "s/<<PRODUCT_ID>>/${MY_PRODUCT_ID}/g" \
    > "${TEMPLATE_OUTPUT}"


http -v "${MY_URL}/${MY_ID}/line-items" \
     -d -o "${MY_OUTPUT_FILE}" \
     < "${TEMPLATE_OUTPUT}" \
     || \
(http -v "${MY_URL}/${MY_ID}/line-items" \
    < "${TEMPLATE_OUTPUT}" && exit 1)
echo
cat "${MY_OUTPUT_FILE}" | python -m json.tool

echo $(jq "${MY_ID_PROP}" "${MY_OUTPUT_FILE}") | \
     tr -d '"' > "${MY_ID_FILE}"
cat "${MY_ID_FILE}"

