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


package io.koosha.foobar.connect.warehouse.rx.generated.api;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * AvailabilityUpdateRequest
 */
@JsonPropertyOrder({
  AvailabilityUpdateRequest.JSON_PROPERTY_UNITS_AVAILABLE,
  AvailabilityUpdateRequest.JSON_PROPERTY_UNITS_TO_FREEZE,
  AvailabilityUpdateRequest.JSON_PROPERTY_PRICE_PER_UNIT
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class AvailabilityUpdateRequest {
  public static final String JSON_PROPERTY_UNITS_AVAILABLE = "unitsAvailable";
  private Long unitsAvailable;

  public static final String JSON_PROPERTY_UNITS_TO_FREEZE = "unitsToFreeze";
  private Long unitsToFreeze;

  public static final String JSON_PROPERTY_PRICE_PER_UNIT = "pricePerUnit";
  private Long pricePerUnit;

  public AvailabilityUpdateRequest() {
  }

  public AvailabilityUpdateRequest unitsAvailable(Long unitsAvailable) {
    
    this.unitsAvailable = unitsAvailable;
    return this;
  }

   /**
   * Get unitsAvailable
   * minimum: 0
   * @return unitsAvailable
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_UNITS_AVAILABLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getUnitsAvailable() {
    return unitsAvailable;
  }


  @JsonProperty(JSON_PROPERTY_UNITS_AVAILABLE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setUnitsAvailable(Long unitsAvailable) {
    this.unitsAvailable = unitsAvailable;
  }


  public AvailabilityUpdateRequest unitsToFreeze(Long unitsToFreeze) {
    
    this.unitsToFreeze = unitsToFreeze;
    return this;
  }

   /**
   * Get unitsToFreeze
   * minimum: 0
   * @return unitsToFreeze
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_UNITS_TO_FREEZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getUnitsToFreeze() {
    return unitsToFreeze;
  }


  @JsonProperty(JSON_PROPERTY_UNITS_TO_FREEZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setUnitsToFreeze(Long unitsToFreeze) {
    this.unitsToFreeze = unitsToFreeze;
  }


  public AvailabilityUpdateRequest pricePerUnit(Long pricePerUnit) {
    
    this.pricePerUnit = pricePerUnit;
    return this;
  }

   /**
   * Get pricePerUnit
   * minimum: 0
   * @return pricePerUnit
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")
  @JsonProperty(JSON_PROPERTY_PRICE_PER_UNIT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Long getPricePerUnit() {
    return pricePerUnit;
  }


  @JsonProperty(JSON_PROPERTY_PRICE_PER_UNIT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPricePerUnit(Long pricePerUnit) {
    this.pricePerUnit = pricePerUnit;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AvailabilityUpdateRequest availabilityUpdateRequest = (AvailabilityUpdateRequest) o;
    return Objects.equals(this.unitsAvailable, availabilityUpdateRequest.unitsAvailable) &&
        Objects.equals(this.unitsToFreeze, availabilityUpdateRequest.unitsToFreeze) &&
        Objects.equals(this.pricePerUnit, availabilityUpdateRequest.pricePerUnit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(unitsAvailable, unitsToFreeze, pricePerUnit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AvailabilityUpdateRequest {\n");
    sb.append("    unitsAvailable: ").append(toIndentedString(unitsAvailable)).append("\n");
    sb.append("    unitsToFreeze: ").append(toIndentedString(unitsToFreeze)).append("\n");
    sb.append("    pricePerUnit: ").append(toIndentedString(pricePerUnit)).append("\n");
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

