package com.jarvins.builder.constructor;

import com.jarvins.builder.Bill;
import com.jarvins.entity.RepaymentPlan;
import com.jarvins.entity.Transaction;
import com.jarvins.service.DefaultLoanTailImpl;
import com.jarvins.service.DefaultRepaymentPlanImpl;
import com.jarvins.service.LoanTailService;
import com.jarvins.service.RepaymentPlanService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RepaymentPlanConstructor {

    /**
     * 生成还款计划
     * @param userId
     * @param orderNo
     * @param amount
     * @param period
     * @param loanTime
     * @return
     */
    public static List<RepaymentPlan> create(int userId, String orderNo, BigDecimal amount, int period, LocalDate loanTime) {
        RepaymentPlanService service = new DefaultRepaymentPlanImpl();
        List<Bill> bills = service.calculateRepaymentPlan(amount, period, loanTime);
        return service.buildRepayment(userId, orderNo, bills);
    }

    /**
     * 生成冲账结果
     * @param transaction
     * @param repaymentPlan
     * @return
     */
    public static RepaymentPlan create(Transaction transaction, RepaymentPlan repaymentPlan) {
        LoanTailService service = new DefaultLoanTailImpl();
        return service.settleBill(transaction, repaymentPlan);
    }
}
