package com.demo.company.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

  public static final String FIELD_ADDRESS_NAME = "addressName";
  public static final String FIELD_ADDRESS = "address";
  public static final String FIELD_CITY = "city";

  @Field(value = Address.FIELD_ADDRESS_NAME)
  private Integer addressName;

  @Field(value = Address.FIELD_ADDRESS)
  private String address;

  @Field(value = Address.FIELD_CITY)
  private String city;

}
