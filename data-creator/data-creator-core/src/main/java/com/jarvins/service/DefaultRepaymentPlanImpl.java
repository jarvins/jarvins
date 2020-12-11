package com.jarvins.service;

import com.jarvins.Constant;
import com.jarvins.builder.Bill;
import com.jarvins.entity.RepaymentPlan;
import org.apache.poi.ss.formula.functions.Finance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ROUND_CEILING;

/**
 * 生成规则:
 * <p>
 * 账单:
 * pmt生成总额
 * ppmt生成本金
 * ipmt生成利息
 * <p>
 * 还款日: 放款日对日
 */
public class DefaultRepaymentPlanImpl implements RepaymentPlanService {

    @Override
    public List<Bill> calculateRepaymentPlan(BigDecimal amount, int period, LocalDate loanTime) {
        BigDecimal remain = new BigDecimal(amount.toString());
        amount = amount.negate();
        List<Bill> result = new ArrayList<>();
        BigDecimal shouldAmount = BigDecimal.valueOf(Finance.pmt(Constant.MONTH_RATE.doubleValue(), period, amount.doubleValue())).setScale(8, ROUND_CEILING);
        for (int i = 1; i <= period; i++) {
            BigDecimal shouldInterest = BigDecimal.valueOf(Finance.ipmt(Constant.MONTH_RATE.doubleValue(), i, period, amount.doubleValue())).setScale(8, ROUND_CEILING);
            BigDecimal shouldPrincipal = shouldAmount.subtract(shouldInterest);
            if (i > 1) {
                remain = remain.subtract(result.get(result.size()-1).getShouldPrincipal());
            }
            result.add(new Bill(i, shouldAmount, shouldPrincipal, shouldInterest, BigDecimal.ZERO, loanTime.plusMonths(i), remain));
        }
        return result;
    }

    @Override
    public List<RepaymentPlan> buildRepayment(int userId, String orderNo, List<Bill> bills) {
        List<RepaymentPlan> repaymentPlanList = new ArrayList<>();
        bills.forEach(e -> {
            RepaymentPlan repaymentPlan = RepaymentPlan.builder()
                    .userId(userId)
                    .orderNo(orderNo)
                    .period(bills.size())
                    .currentPeriod(e.getPeriod())
                    .shouldAmount(e.getShouldAmount())
                    .shouldPrincipal(e.getShouldPrincipal())
                    .shouldInterest(e.getShouldInterest())
                    .shouldOverdue(e.getShouldOverdue())
                    .shouldRepayDate(e.getShouldRepayDate())
                    .remainPrincipal(e.getRemainPrincipal())
                    .repayAmount(null)
                    .repayPrincipal(null)
                    .repayInterest(null)
                    .repayOverdue(null)
                    .realRepayDate(null)
                    .status(false)
                    .advanceDay(0)
                    .overdueDay(0)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            repaymentPlanList.add(repaymentPlan);
        });
        return repaymentPlanList;
    }


    public static void main(String[] args) {
        DefaultRepaymentPlanImpl defaultRepaymentPlan = new DefaultRepaymentPlanImpl();
        List<Bill> bills = defaultRepaymentPlan.calculateRepaymentPlan(new BigDecimal("1000"), 3, LocalDate.of(2020, 8, 12));
        System.out.println(bills);
    }
}
