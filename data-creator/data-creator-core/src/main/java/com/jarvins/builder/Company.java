package com.jarvins.builder;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Company {
    //工种
    String[] workType;

    //试用期
    int probation;

    //薪资下限
    int minSalary;

    //薪资上限
    int maxSalary;
}
