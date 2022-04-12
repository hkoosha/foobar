#!/bin/bash
set -euo pipefail
MY_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source "$MY_DIR/config.sh"


readonly MY_URL="${CUSTOMER_URL}"
readonly MY_ID_PROP="${CUSTOMER__ADDRESS__ID_PROP}"
readonly MY_DATA_FILE="${CUSTOMER__ADDRESS__DATA_FILE}"
readonly MY_OUTPUT_FILE="${CUSTOMER__ADDRESS__OUTPUT_FILE}"
readonly MY_ID_FILE="${CUSTOMER__ADDRESS__ID_FILE}"
readonly THEIR_ID_FILE0="${CUSTOMER__ADDRESS__INPUT_ID_FILE}"


rm -f "${MY_OUTPUT_FILE}"
rm -f "${MY_ID_FILE}"

MY_ID=$(cat "${THEIR_ID_FILE0}")

http -v "${MY_URL}/${MY_ID}/addresses" \
     -d -o "${MY_OUTPUT_FILE}" \
     < "${MY_DATA_FILE}" \
     || \
(http -v "${MY_URL}/${MY_ID}/addresses" \
    < "${MY_DATA_FILE}" && exit 1)
echo
cat "${MY_OUTPUT_FILE}" | python -m json.tool

echo $(jq "${MY_ID_PROP}" "${MY_OUTPUT_FILE}") | \
     tr -d '"' > "${MY_ID_FILE}"
cat "${MY_ID_FILE}"

