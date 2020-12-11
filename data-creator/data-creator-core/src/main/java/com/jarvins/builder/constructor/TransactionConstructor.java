package com.jarvins.builder.constructor;

import com.jarvins.entity.RepaymentPlan;
import com.jarvins.entity.Transaction;
import com.jarvins.service.DefaultLoanTailImpl;
import com.jarvins.service.LoanTailService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 生成还款流水
 * 由于流水是实时的,所以针对用于而言具有随机性
 * 整体控制逻辑如下:
 * <p>
 * 流水号: 当前日期 + 订单号 + 随机3位数
 * <p>
 * 为了保证业务逻辑相对简单,保证在还款时，不会一次还部分，一次还多期，或者一次还完当期还有剩的
 * 所有流水都保证还完当期，不缺也不剩
 * <p>
 * 同时为了保证多样性，在外部逻辑上应该随机抽取用户还钱，实时计算应还金额，并构建流水，触发冲账业务
 */
public class TransactionConstructor {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    /**
     * 生成流水是按照某个用户的某期还款计划生成的,这里的还款计划应该是随机查询到的
     * @param repaymentPlan 某个还款计划
     * @return 随机生成的流水
     */
    public static Transaction create(RepaymentPlan repaymentPlan) {
        //生成流水号
        String transactionFlowNo = LocalDate.now().toString().replaceAll("-", "") +
                repaymentPlan.getOrderNo() +
                RANDOM.nextInt(100, 1000);

        LoanTailService service = new DefaultLoanTailImpl();
        RepaymentPlan realRepayment = service.calculateRealBill(repaymentPlan);

        return Transaction.builder()
                .orderNo(realRepayment.getOrderNo())
                .userId(realRepayment.getUserId())
                .period(realRepayment.getCurrentPeriod())
                .transactionFlowNo(transactionFlowNo)
                .transactionTime(LocalDateTime.now())
                .repayDate(realRepayment.getRealRepayDate())
                .transactionAmount(realRepayment.getShouldAmount())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }
}
