package com.jarvins.service;

import com.jarvins.builder.Bill;
import com.jarvins.entity.RepaymentPlan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 计算还款计划
 */
public interface RepaymentPlanService {

    /**
     *
     * @param amount 借款金额
     * @param period 借款期数
     * @param loanTime 借款时间
     * @return 账单
     */
    List<Bill> calculateRepaymentPlan(BigDecimal amount, int period, LocalDate loanTime);

    /**
     * 构建完整还款计划
     * @param userId 用户id
     * @param orderNo 订单号
     * @param bills 账单
     * @return
     */
    List<RepaymentPlan> buildRepayment(int userId, String orderNo, List<Bill> bills);
}
