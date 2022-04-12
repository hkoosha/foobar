#!/bin/bash


which http 1>/dev/null 2>/dev/null || echo "error: HTTPie not on ptath" 1>&2


SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

MY_BASE_DIR="/tmp/foobar"
mkdir -p "${MY_BASE_DIR}"

APP_PREFIX=foobar


TEMPLATE_OUTPUT="${MY_BASE_DIR}/template.out"


CUSTOMER_PORT=4043
CUSTOMER_URL=":${CUSTOMER_PORT}/${APP_PREFIX}/customer/v1/customers"
CUSTOMER__ADDRESS__ID_PROP='.addressId'
CUSTOMER__ADDRESS__ID_FILE="${MY_BASE_DIR}/foobar.customer.addressId"
CUSTOMER__ADDRESS__DATA_FILE="${SCRIPT_DIR}/../data/data.customer.address"
CUSTOMER__ADDRESS__OUTPUT_FILE="${MY_BASE_DIR}/foobar.customer.create_address.output"
CUSTOMER__ADDRESS__INPUT_ID_FILE="${MY_BASE_DIR}/foobar.customer.customerId"
CUSTOMER__CUSTOMER__ID_PROP='.customerId'
CUSTOMER__CUSTOMER__ID_FILE="${MY_BASE_DIR}/foobar.customer.customerId"
CUSTOMER__CUSTOMER__DATA_FILE="${SCRIPT_DIR}/../data/data.customer.customer"
CUSTOMER__CUSTOMER__OUTPUT_FILE="${MY_BASE_DIR}/foobar.customer.create_customer.output"


WAREHOUSE_PORT=4041
WAREHOUSE_URL="http://localhost:${WAREHOUSE_PORT}/${APP_PREFIX}/warehouse/v1/products"
WAREHOUSE__AVAILABILITY__ID_PROP='.availabilityId'
WAREHOUSE__AVAILABILITY__ID_FILE="${MY_BASE_DIR}/foobar.warehouse.availabilityId"
WAREHOUSE__AVAILABILITY__DATA_FILE="${SCRIPT_DIR}/../data/data.warehouse.availability"
WAREHOUSE__AVAILABILITY__DATA_FILE_PROCESSED="${MY_BASE_DIR}/foobar.warehouse.create_availability.data_processed"
WAREHOUSE__AVAILABILITY__DATA_FILE_BAD="${SCRIPT_DIR}/../data/data.warehouse.availability.bad"
WAREHOUSE__AVAILABILITY__OUTPUT_FILE="${MY_BASE_DIR}/foobar.warehouse.create_availability.output"
WAREHOUSE__AVAILABILITY__INPUT_ID_FILE="${MY_BASE_DIR}/foobar.warehouse.productId"
WAREHOUSE__PRODUCT__ID_PROP='.productId'
WAREHOUSE__PRODUCT__ID_FILE="${MY_BASE_DIR}/foobar.warehouse.productId"
WAREHOUSE__PRODUCT__DATA_FILE="${SCRIPT_DIR}/../data/data.warehouse.product"
WAREHOUSE__PRODUCT__OUTPUT_FILE="${MY_BASE_DIR}/foobar.warehouse.create_product.output"


MARKETPLACE_PORT=4040
MARKETPLACE_URL="http://localhost:${MARKETPLACE_PORT}/${APP_PREFIX}/marketplace/v1/order-requests"
MARKETPLACE__ORDER_REQUEST__ID_PROP='.orderRequestId'
MARKETPLACE__ORDER_REQUEST__ID_FILE="${MY_BASE_DIR}/foobar.marketplace.orderRequestId"
MARKETPLACE__ORDER_REQUEST__DATA_FILE="${SCRIPT_DIR}/../data/data.marketplace.orderRequest"
MARKETPLACE__ORDER_REQUEST__UPDATE_DATA_FILE="${SCRIPT_DIR}/../data/data.marketplace.orderRequest.updateState"
MARKETPLACE__ORDER_REQUEST__OUTPUT_FILE="${MY_BASE_DIR}/foobar.marketplace.create_order_request.output"
MARKETPLACE__ORDER_REQUEST__UPDATE_STATE_OUTPUT_FILE="${MY_BASE_DIR}/foobar.marketplace.update_state_order_request.output"
MARKETPLACE__LINE_ITEM__OUTPUT_FILE="${MY_BASE_DIR}/foobar.marketplace.create_line_item.output"
MARKETPLACE__LINE_ITEM__ID_FILE="${MY_BASE_DIR}/foobar.marketplace.lineItemId"
MARKETPLACE__LINE_ITEM__DATA_FILE="${SCRIPT_DIR}/../data/data.marketplace.lineItem"


SELLER_PORT=4045
SELLER_URL=":${SELLER_PORT}/${APP_PREFIX}/seller/v1/sellers"
SELLER__SELLER__ID_PROP='.sellerId'
SELLER__SELLER__ID_FILE="${MY_BASE_DIR}/foobar.seller.sellerId"
SELLER__SELLER__DATA_FILE="${SCRIPT_DIR}/../data/data.seller.seller"
SELLER__SELLER__OUTPUT_FILE="${MY_BASE_DIR}/foobar.seller.create_seller.output"

