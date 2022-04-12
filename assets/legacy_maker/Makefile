
mkfile_path := $(abspath $(lastword $(MAKEFILE_LIST)))
current_dir := $(patsubst %/,%,$(dir $(mkfile_path)))

.PHONY: customer
customer:
	$(current_dir)/script/customer__create_customer.sh

.PHONY: customer-get-all
customer-get-all:
	$(current_dir)/script/customer__get_customers.sh

.PHONY: customer-get
customer-get:
	$(current_dir)/script/customer__get_customer.sh


.PHONY: customer-addr
customer-addr:
	$(current_dir)/script/customer__create_address.sh

.PHONY: customer-addr-get-all
customer-addr-get-all:
	$(current_dir)/script/customer__get_addresses.sh

.PHONY: customer-addr-get
customer-addr-get:
	$(current_dir)/script/customer__get_address.sh


.PHONY: seller
seller:
	$(current_dir)/script/seller__create_seller.sh

.PHONY: seller-get
seller-get:
	$(current_dir)/script/seller__get_seller.sh
	
.PHONY: seller-get-all
seller-get-all:
	$(current_dir)/script/seller__get_sellers.sh



.PHONY: product
product:
	$(current_dir)/script/warehouse__create_product.sh

.PHONY: product-get-all
product-get-all:
	$(current_dir)/script/warehouse__get_products.sh


.PHONY: availability
availability:
	$(current_dir)/script/warehouse__create_availability.sh

.PHONY: bad-availability
bad-availability:
	$(current_dir)/script/warehouse__create_availability_bad.sh

.PHONY: availability-get-all
availability-get-all:
	$(current_dir)/script/warehouse__get_availabilities.sh


.PHONY: order-request
order-request:
	$(current_dir)/script/marketplace__create_order_request.sh

.PHONY: order-request-get-all
order-request-get-all:
	$(current_dir)/script/marketplace__get_order_requests.sh

.PHONY: line-items-get-all
line-items-get-all:
	$(current_dir)/script/marketplace__get_order_request_line_items.sh

.PHONY: line-item
line-item:
	$(current_dir)/script/marketplace__create_lite_item.sh

.PHONY: order-request-update-state-go-live
order-request-update-state-go-live:
	$(current_dir)/script/marketplace__update_order_request_state.sh LIVE
.PHONY: order-request-update-state-seller
order-request-update-state-seller:
	$(current_dir)/script/marketplace__update_order_request_state.sh WAITING_FOR_SELLER
.PHONY: order-request-update-state-delivery
order-request-update-state-delivery:
	$(current_dir)/script/marketplace__update_order_request_state.sh WAITING_FOR_DELIVERY
.PHONY: order-request-update-state-fulfilled
order-request-update-state-fulfilled:
	$(current_dir)/script/marketplace__update_order_request_state.sh FULFILLED


