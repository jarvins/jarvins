package com.jarvins.service;

import com.jarvins.Constant;
import com.jarvins.entity.RepaymentPlan;
import com.jarvins.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 还款金额生成规则:
 * 若用户正常还款,则还款计划不变更，按还款计划的还款
 * 若用户提前还款,则重新计算利息,利息 = 当前剩余本金 * 日利率 * 计息天数计算(并不影响下一期的资金占用天数,简化模型)
 * 若用户逾期,则重新计算还款计划,逾期费 = 当前剩余本金 * 逾期日利率 * 逾期天数(没有缓冲期,没有最大期限)
 * <p>
 * 每次以该用户最早未还的期数生成实际还款计划，并构建流水.比如:
 * 某个用户是3期的还款计划,第一期未还,若生成还款计划,则以第一期的还款时间构建新还款计划
 * 某个用户是3期的还款计划,第一期,第二期已还,第三期未还,若生成还款计划,则以第三期的还款时间构建新还款计划
 */
public class DefaultLoanTailImpl implements LoanTailService {

    /**
     * 生成实际还款时的还款计划
     *
     * @param repaymentPlan 初始还款计划
     * @return 实际还款计划
     */
    @Override
    public RepaymentPlan calculateRealBill(RepaymentPlan repaymentPlan) {
        LocalDate repayDate = LocalDate.now();
        repaymentPlan.setRealRepayDate(repayDate);
        //提前还
        if (repayDate.isBefore(repaymentPlan.getShouldRepayDate())) {
            BigDecimal interest = cal(Constant.DAY_RATE, repaymentPlan.getShouldRepayDate(), repaymentPlan.getRemainPrincipal());
            BigDecimal amount = repaymentPlan.getShouldInterest().add(repaymentPlan.getShouldPrincipal());
            repaymentPlan.setShouldInterest(interest);
            repaymentPlan.setShouldAmount(amount);
        } else if (repayDate.isAfter(repaymentPlan.getShouldRepayDate())) {
            BigDecimal overdue = cal(Constant.OVERDUE_RATE, repaymentPlan.getShouldRepayDate(), repaymentPlan.getRemainPrincipal());
            BigDecimal amount = repaymentPlan.getShouldAmount().add(overdue);
            repaymentPlan.setShouldOverdue(overdue);
            repaymentPlan.setShouldAmount(amount);
        }
        return repaymentPlan;
    }

    /**
     * 还款流水入账，生成新还款计划和入账结果
     * 由于流水是实时计算出来的结果，入账不做二次校验
     *
     * @param transaction
     * @param repaymentPlan
     */
    @Override
    public RepaymentPlan settleBill(Transaction transaction, RepaymentPlan repaymentPlan) {
        LocalDate repayDate = transaction.getRepayDate();
        BigDecimal transactionAmount = transaction.getTransactionAmount();
        LocalDate shouldRepayDate = repaymentPlan.getShouldRepayDate();

        //逾期
        if (repayDate.isAfter(shouldRepayDate)) {
            repaymentPlan.setShouldOverdue(transactionAmount.subtract(repaymentPlan.getShouldAmount()));
            repaymentPlan.setOverdueDay((int) (repayDate.toEpochDay() - shouldRepayDate.toEpochDay()));
        }
        //提前还
        else if (repayDate.isBefore(shouldRepayDate)) {
            repaymentPlan.setShouldInterest(transactionAmount.subtract(repaymentPlan.getShouldPrincipal()));
            repaymentPlan.setAdvanceDay((int) (shouldRepayDate.toEpochDay() - repayDate.toEpochDay()));
        }

        //正常还还款计划不变

        repaymentPlan.setShouldAmount(transactionAmount);
        repaymentPlan.setRepayAmount(transactionAmount);
        repaymentPlan.setRepayPrincipal(repaymentPlan.getShouldPrincipal());
        repaymentPlan.setRepayInterest(repaymentPlan.getShouldInterest());
        repaymentPlan.setRepayOverdue(repaymentPlan.getShouldOverdue());
        repaymentPlan.setRealRepayDate(repayDate);
        repaymentPlan.setStatus(true);
        return repaymentPlan;
    }

    private BigDecimal cal(BigDecimal rate, LocalDate shouldRepayDate, BigDecimal remainAmount) {
        long day = Math.abs(LocalDate.now().toEpochDay() - shouldRepayDate.toEpochDay());
        return remainAmount.multiply(rate).multiply(new BigDecimal(day));
    }

}
