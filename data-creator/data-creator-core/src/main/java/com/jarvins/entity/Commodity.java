package com.jarvins.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class Commodity {

    private int id;

    private String productType;

    private String productName;

    private String productCode;

    private BigDecimal productPrice;

    private int salas;

    private String seller;

    private int inventory;

    private String goodsSupply;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Override

    public String toString() {
        return "Commodity{" +
                "id=" + id +
                ", productType='" + productType + '\'' +
                ", productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productPrice=" + productPrice +
                ", seller='" + seller + '\'' +
                ", salas=" + salas +
                ", inventory=" + inventory +
                ", goodsSupply='" + goodsSupply + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
