#!/bin/bash
set -euo pipefail
MY_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source "$MY_DIR/config.sh"


readonly MY_URL="${WAREHOUSE_URL}"
readonly MY_ID_PROP="${WAREHOUSE__AVAILABILITY__ID_PROP}"
readonly MY_DATA_FILE_UNPROCESSED="${WAREHOUSE__AVAILABILITY__DATA_FILE}"
readonly MY_DATA_FILE_PROCESSED="${WAREHOUSE__AVAILABILITY__DATA_FILE_PROCESSED}"
readonly MY_OUTPUT_FILE="${WAREHOUSE__AVAILABILITY__OUTPUT_FILE}"
readonly MY_ID_FILE="${WAREHOUSE__AVAILABILITY__ID_FILE}"
readonly THEIR_ID_FILE0="${WAREHOUSE__PRODUCT__ID_FILE}"
readonly ENTITY0_ID_FILE="${SELLER__SELLER__ID_FILE}"
readonly ENTITY0_ID=$(cat "${ENTITY0_ID_FILE}")


rm -f "${MY_OUTPUT_FILE}"
rm -f "${MY_ID_FILE}"
rm -f "${MY_DATA_FILE_PROCESSED}"

cat "${MY_DATA_FILE_UNPROCESSED}" | sed "s/@PLACEHOLDER_SELLER/$ENTITY0_ID/g" > "${MY_DATA_FILE_PROCESSED}"


MY_ID=$(cat "${THEIR_ID_FILE0}")

http -v "${MY_URL}/${MY_ID}/availabilities" \
     -d -o "${MY_OUTPUT_FILE}" \
     < "${MY_DATA_FILE_PROCESSED}" \
     || \
(http -v "${MY_URL}/${MY_ID}/availabilities" \
    < "${MY_DATA_FILE_PROCESSED}" && exit 1)
#while true; do curl -X POST -H "Content-Type: application/json" "${MY_URL}/${MY_ID}/availabilities" -d "@${MY_DATA_FILE}"; done
echo
cat "${MY_OUTPUT_FILE}" | python -m json.tool

echo $(jq "${MY_ID_PROP}" "${MY_OUTPUT_FILE}") | \
     tr -d '"' > "${MY_ID_FILE}"
cat "${MY_ID_FILE}"

