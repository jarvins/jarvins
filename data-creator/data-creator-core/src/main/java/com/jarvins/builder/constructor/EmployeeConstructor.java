package com.jarvins.builder.constructor;

import com.jarvins.Constant;
import com.jarvins.builder.Company;
import com.jarvins.builder.CompanyBuilder;
import com.jarvins.entity.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.jarvins.builder.CompanyBuilder.COMPANY;

public class EmployeeConstructor {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static Employee create(int userId) {
        //决定命运的分数
        int score = RANDOM.nextInt(21);
        Company company = CompanyBuilder.build(score);
        String[] workType = company.getWorkType();
        float workYear = RANDOM.nextInt(1, 5) + ((float) RANDOM.nextInt(1, 10)) / 10;
        LocalDate entryDate = Constant.DATE.plusMonths(-(int) (workYear * 12));

        return Employee.builder()
                .userId(userId)
                .companyName(COMPANY[RANDOM.nextInt(COMPANY.length)])
                .position(workType[RANDOM.nextInt(workType.length)])
                .probation(company.getProbation())
                .salary(BigDecimal.valueOf(RANDOM.nextInt(company.getMinSalary(), company.getMaxSalary())))
                .entryDate(entryDate)
                .workYear(workYear)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }


    public static List<Employee> batchCreate(List<Integer> uds){
        List<Employee> employees = new LinkedList<>();
        uds.forEach(e -> employees.add(create(e)));
        return employees;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Employee employee = create(23);
            System.out.println(employee);
        }
    }
}
