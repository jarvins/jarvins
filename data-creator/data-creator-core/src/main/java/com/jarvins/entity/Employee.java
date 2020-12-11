package com.jarvins.entity;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public class Employee {
    private int id;

    private int userId;

    private String companyName;

    private LocalDate entryDate;

    private String position;

    private BigDecimal salary;

    private float workYear;

    private int probation;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", userId=" + userId +
                ", companyName='" + companyName + '\'' +
                ", entryTime=" + entryDate +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                ", workYear=" + workYear +
                ", probation=" + probation +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
