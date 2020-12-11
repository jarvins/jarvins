package com.jarvins.mapper;

import com.jarvins.entity.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DataInsertMapper {

    @Transactional
    @Insert("<script>" +
            "insert into " +
            "user (id, name, sex, age, phone, born, zodiac, address, married, create_time, update_time)" +
            "values" +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.id},#{item.name},#{item.sex},#{item.age},#{item.phone},#{item.born},#{item.zodiac},#{item.address},#{item.married},#{item.createTime},#{item.updateTime})" +
            "</foreach>" +
            "</script>")
    int batchInsertUser(@Param("list") List<User> list);

    @Transactional
    @Insert("<script>" +
            "insert into " +
            "employee(id, user_id, company_name, position, salary, work_year, entry_date, probation, create_time, update_time)" +
            "values" +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.id},#{item.userId},#{item.companyName},#{item.position},#{item.salary},#{item.workYear},#{item.entryDate},#{item.probation},#{item.createTime},#{item.updateTime})" +
            "</foreach>" +
            "</script>")
    int batchInsertEmployee(@Param("list") List<Employee> list);

    @Transactional
    @Insert("<script>" +
            "insert into " +
            "shopping(id, user_id, order_no, product_name, product_code, product_price, payment_channel, channel_name, payment_amount, with_hold, with_hold_amount, payment_number, repayment_period, payment_time, create_time, update_time)" +
            "values" +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.id},#{item.userId},#{item.orderNo},#{item.productName},#{item.productCode},#{item.productPrice},#{item.paymentChannel},#{item.channelName},#{item.paymentAmount},#{item.withHold},#{item.withHoldAmount},#{item.paymentNumber},#{item.repaymentPeriod},#{item.paymentTime},#{item.createTime},#{item.updateTime})" +
            "</foreach>" +
            "</script>")
    int batchInsertShopping(@Param("list") List<Shopping> list);

    @Transactional
    @Insert("<script>" +
            "insert into " +
            "commodity(id, product_type, product_name, product_code, product_price, salas, seller, inventory, goods_supply, create_time, update_time)" +
            "values" +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.id},#{item.productType},#{item.productName},#{item.productCode},#{item.productPrice},#{item.salas},#{item.seller},#{item.inventory},#{item.goodsSupply},#{item.createTime},#{item.updateTime})" +
            "</foreach>" +
            "</script>")
    int batchInsertCommodity(@Param("list") List<Commodity> list);

    @Transactional
    @Insert("<script>" +
            "insert into " +
            "repayment_plan(id, user_id, order_no, period, current_period, should_amount, should_principal, should_interest, should_overdue, should_repay_date, remain_principal, repay_amount, repay_principal, repay_interest, repay_overdue, real_repay_date, status, advance_day, overdue_day, create_time, update_time)" +
            "values" +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.id},#{item.userId},#{item.orderNo},#{item.period},#{item.currentPeriod},#{item.shouldAmount},#{item.shouldPrincipal},#{item.shouldInterest},#{item.shouldOverdue},#{item.shouldRepayDate},#{item.remainPrincipal},#{item.repayAmount},#{item.repayPrincipal},#{item.repayInterest},#{item.repayOverdue},#{item.realRepayDate},#{item.status},#{item.advanceDay},#{item.overdueDay},#{item.createTime},#{item.updateTime})" +
            "</foreach>" +
            "</script>")
    int batchInsertRepaymentPlan(@Param("list") List<RepaymentPlan> list);

    @Transactional
    @Insert("<script>" +
            "insert into " +
            "transaction(id, user_id, transaction_flow_no, order_no, transaction_amount, period, transaction_time, repay_date, update_time, create_time)" +
            "values" +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.id},#{item.userId},#{item.transactionFlowNo},#{item.orderNo},#{item.transactionAmount},#{item.period},#{item.transactionTime},#{item.repayDate},#{item.createTime},#{item.updateTime})" +
            "</foreach>" +
            "</script>")
    int batchInsertTransaction(@Param("list") List<Transaction> list);

}
