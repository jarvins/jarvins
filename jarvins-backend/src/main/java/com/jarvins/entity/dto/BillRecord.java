package com.jarvins.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillRecord implements Serializable {

    int id;

    String type;

    BigDecimal amount;

    String label;

    LocalDate recordDate;

    LocalDateTime createTime;

    LocalDateTime updateTime;
}
