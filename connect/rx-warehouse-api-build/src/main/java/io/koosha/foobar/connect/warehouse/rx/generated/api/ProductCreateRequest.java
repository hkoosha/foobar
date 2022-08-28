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
 * ProductCreateRequest
 */
@JsonPropertyOrder({
  ProductCreateRequest.JSON_PROPERTY_ACTIVE,
  ProductCreateRequest.JSON_PROPERTY_NAME,
  ProductCreateRequest.JSON_PROPERTY_UNIT_SINGLE,
  ProductCreateRequest.JSON_PROPERTY_UNIT_MULTIPLE
})
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class ProductCreateRequest {
  public static final String JSON_PROPERTY_ACTIVE = "active";
  private Boolean active;

  public static final String JSON_PROPERTY_NAME = "name";
  private String name;

  public static final String JSON_PROPERTY_UNIT_SINGLE = "unitSingle";
  private String unitSingle;

  public static final String JSON_PROPERTY_UNIT_MULTIPLE = "unitMultiple";
  private String unitMultiple;

  public ProductCreateRequest() { 
  }

  public ProductCreateRequest active(Boolean active) {
    
    this.active = active;
    return this;
  }

   /**
   * Get active
   * @return active
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_ACTIVE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Boolean getActive() {
    return active;
  }


  @JsonProperty(JSON_PROPERTY_ACTIVE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setActive(Boolean active) {
    this.active = active;
  }


  public ProductCreateRequest name(String name) {
    
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getName() {
    return name;
  }


  @JsonProperty(JSON_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setName(String name) {
    this.name = name;
  }


  public ProductCreateRequest unitSingle(String unitSingle) {
    
    this.unitSingle = unitSingle;
    return this;
  }

   /**
   * Get unitSingle
   * @return unitSingle
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_UNIT_SINGLE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getUnitSingle() {
    return unitSingle;
  }


  @JsonProperty(JSON_PROPERTY_UNIT_SINGLE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setUnitSingle(String unitSingle) {
    this.unitSingle = unitSingle;
  }


  public ProductCreateRequest unitMultiple(String unitMultiple) {
    
    this.unitMultiple = unitMultiple;
    return this;
  }

   /**
   * Get unitMultiple
   * @return unitMultiple
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(required = true, value = "")
  @JsonProperty(JSON_PROPERTY_UNIT_MULTIPLE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getUnitMultiple() {
    return unitMultiple;
  }


  @JsonProperty(JSON_PROPERTY_UNIT_MULTIPLE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setUnitMultiple(String unitMultiple) {
    this.unitMultiple = unitMultiple;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductCreateRequest productCreateRequest = (ProductCreateRequest) o;
    return Objects.equals(this.active, productCreateRequest.active) &&
        Objects.equals(this.name, productCreateRequest.name) &&
        Objects.equals(this.unitSingle, productCreateRequest.unitSingle) &&
        Objects.equals(this.unitMultiple, productCreateRequest.unitMultiple);
  }

  @Override
  public int hashCode() {
    return Objects.hash(active, name, unitSingle, unitMultiple);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductCreateRequest {\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    unitSingle: ").append(toIndentedString(unitSingle)).append("\n");
    sb.append("    unitMultiple: ").append(toIndentedString(unitMultiple)).append("\n");
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
