package com.jarvins.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class Shopping {

    private int id;

    private int userId;

    private String orderNo;

    private String productName;

    private String productCode;

    private BigDecimal productPrice;

    //支付渠道,0:社交账号,1:银行卡
    private int paymentChannel;

    private String channelName;

    private BigDecimal paymentAmount;

    private boolean withHold;

    private BigDecimal withHoldAmount;

    private String paymentNumber;


    private int repaymentPeriod;

    private LocalDateTime paymentTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Override
    public String toString() {
        return "Shopping{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderNo='" + orderNo + '\'' +
                ", productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productPrice=" + productPrice +
                ", paymentChannel=" + paymentChannel +
                ", paymentAmount=" + paymentAmount +
                ", withHoldAmount=" + withHoldAmount +
                ", paymentNumber='" + paymentNumber + '\'' +
                ", repaymentPeriod=" + repaymentPeriod +
                ", paymentTime=" + paymentTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
