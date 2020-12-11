package com.jarvins.entity.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class BillStatistics {

    int id;

    String type;

    BigDecimal amount;

    LocalDate statisticsDate;

    LocalDateTime createTime;

    LocalDateTime updateTime;
}
