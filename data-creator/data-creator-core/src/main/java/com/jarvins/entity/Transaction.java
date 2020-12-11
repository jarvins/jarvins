package com.jarvins.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class Transaction {

    private int id;

    private int userId;

    private String transactionFlowNo;

    private String orderNo;

    private BigDecimal transactionAmount;

    private int period;

    private LocalDateTime transactionTime;

    private LocalDate repayDate;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
