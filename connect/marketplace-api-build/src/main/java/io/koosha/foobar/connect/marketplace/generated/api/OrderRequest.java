/*
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package io.koosha.foobar.connect.marketplace.generated.api;

import java.util.Objects;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * OrderRequest
 */
@JsonPropertyOrder({
  OrderRequest.JSON_PROPERTY_ORDER_REQUEST_ID,
  OrderRequest.JSON_PROPERTY_CUSTOMER_ID,
  OrderRequest.JSON_PROPERTY_SELLER_ID,
  OrderRequest.JSON_PROPERTY_STATE,
  OrderRequest.JSON_PROPERTY_SUB_TOTAL
})

public class OrderRequest {
  public static final String JSON_PROPERTY_ORDER_REQUEST_ID = "orderRequestId";
  private UUID orderRequestId;

  public static final String JSON_PROPERTY_CUSTOMER_ID = "customerId";
  private UUID customerId;

  public static final String JSON_PROPERTY_SELLER_ID = "sellerId";
  private UUID sellerId;

  /**
   * Gets or Sets state
   */
  public enum StateEnum {
    ACTIVE("ACTIVE"),
    
    LIVE("LIVE"),
    
    NO_SELLER_FOUND("NO_SELLER_FOUND"),
    
    WAITING_FOR_SELLER("WAITING_FOR_SELLER"),
    
    FULFILLED("FULFILLED");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StateEnum fromValue(String value) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_STATE = "state";
  private StateEnum state;

  public static final String JSON_PROPERTY_SUB_TOTAL = "subTotal";
  private Long subTotal;

  public OrderRequest() {
  }

  public OrderRequest orderRequestId(UUID orderRequestId) {
    
    this.orderRequestId = orderRequestId;
    return this;
  }

   /**
   * Get orderRequestId
   * @return orderRequestId
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_ORDER_REQUEST_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getOrderRequestId() {
    return orderRequestId;
  }


  @JsonProperty(JSON_PROPERTY_ORDER_REQUEST_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setOrderRequestId(UUID orderRequestId) {
    this.orderRequestId = orderRequestId;
  }


  public OrderRequest customerId(UUID customerId) {
    
    this.customerId = customerId;
    return this;
  }

   /**
   * Get customerId
   * @return customerId
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_CUSTOMER_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getCustomerId() {
    return customerId;
  }


  @JsonProperty(JSON_PROPERTY_CUSTOMER_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }


  public OrderRequest sellerId(UUID sellerId) {
    
    this.sellerId = sellerId;
    return this;
  }

   /**
   * Get sellerId
   * @return sellerId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_SELLER_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public UUID getSellerId() {
    return sellerId;
  }


  @JsonProperty(JSON_PROPERTY_SELLER_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSellerId(UUID sellerId) {
    this.sellerId = sellerId;
  }


  public OrderRequest state(StateEnum state) {
    
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_STATE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public StateEnum getState() {
    return state;
  }


  @JsonProperty(JSON_PROPERTY_STATE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setState(StateEnum state) {
    this.state = state;
  }


  public OrderRequest subTotal(Long subTotal) {
    
    this.subTotal = subTotal;
    return this;
  }

   /**
   * Get subTotal
   * @return subTotal
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_SUB_TOTAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getSubTotal() {
    return subTotal;
  }


  @JsonProperty(JSON_PROPERTY_SUB_TOTAL)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setSubTotal(Long subTotal) {
    this.subTotal = subTotal;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderRequest orderRequest = (OrderRequest) o;
    return Objects.equals(this.orderRequestId, orderRequest.orderRequestId) &&
        Objects.equals(this.customerId, orderRequest.customerId) &&
        Objects.equals(this.sellerId, orderRequest.sellerId) &&
        Objects.equals(this.state, orderRequest.state) &&
        Objects.equals(this.subTotal, orderRequest.subTotal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderRequestId, customerId, sellerId, state, subTotal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderRequest {\n");
    sb.append("    orderRequestId: ").append(toIndentedString(orderRequestId)).append("\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    sellerId: ").append(toIndentedString(sellerId)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    subTotal: ").append(toIndentedString(subTotal)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
