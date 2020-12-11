package com.jarvins.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
public class RepaymentPlan {

    private int id;

    private int userId;

    private String orderNo;

    private int period;

    private int currentPeriod;

    private BigDecimal shouldAmount;

    private BigDecimal shouldPrincipal;

    private BigDecimal shouldInterest;

    private BigDecimal shouldOverdue;

    private LocalDate shouldRepayDate;

    private BigDecimal remainPrincipal;

    private BigDecimal repayAmount;

    private BigDecimal repayPrincipal;

    private BigDecimal repayInterest;

    private BigDecimal repayOverdue;

    private LocalDate realRepayDate;

    private boolean status;

    private int advanceDay;

    private int overdueDay;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
