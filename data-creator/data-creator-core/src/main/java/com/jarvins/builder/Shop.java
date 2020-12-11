package com.jarvins.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class Shop {

    int userId;

    String productName;

    String productCode;

    BigDecimal productPrice;
}
