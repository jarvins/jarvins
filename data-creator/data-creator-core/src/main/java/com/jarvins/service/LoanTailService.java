package com.jarvins.service;

import com.jarvins.entity.RepaymentPlan;
import com.jarvins.entity.Transaction;


public interface LoanTailService {

    RepaymentPlan calculateRealBill(RepaymentPlan repaymentPlan);

    RepaymentPlan settleBill(Transaction transaction, RepaymentPlan repaymentPlan);
}
