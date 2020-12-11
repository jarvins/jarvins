package com.jarvins.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface TableCreatorMapper {

    @Select("show tables like #{tableName}")
    Object showTable(@Param("tableName") String tableName);

    @Update("CREATE TABLE user\n" +
            "(\n" +
            "    id int PRIMARY KEY NOT NULL COMMENT '主键id' AUTO_INCREMENT,\n" +
            "    name varchar(8) NOT NULL COMMENT '名字',\n" +
            "    sex tinyint NOT NULL COMMENT '性别,1为男性,0为女性',\n" +
            "    age int NOT NULL COMMENT '年龄',\n" +
            "    phone varchar(11) NOT NULL COMMENT '电话',\n" +
            "    born date NOT NULL COMMENT '出生日期',\n" +
            "    zodiac varchar(1) NOT NULL COMMENT '生肖',\n" +
            "    address varchar(64) NOT NULL COMMENT '家庭住址',\n" +
            "    married tinyint NOT NULL COMMENT '是否结婚',\n" +
            "    create_time datetime NOT NULL COMMENT '创建时间',\n" +
            "    update_time datetime NOT NULL COMMENT '更新时间',\n" +
            "    UNIQUE INDEX user_id_uindex (id),\n" +
            "    UNIQUE INDEX user_phone_uindex (phone),\n" +
            "    UNIQUE INDEX user_address_uindex (address)\n" +
            ");")
    void createUserTable();

    @Update("CREATE TABLE employee\n" +
            "(\n" +
            "    id int PRIMARY KEY NOT NULL COMMENT '主键id' AUTO_INCREMENT,\n" +
            "    user_id int NOT NULL COMMENT '用户id',\n" +
            "    company_name varchar(64) NOT NULL COMMENT '工作单位',\n" +
            "    position varchar(64) NOT NULL COMMENT '职位',\n" +
            "    salary decimal(10,2) NOT NULL COMMENT '薪水',\n" +
            "    work_year float NOT NULL COMMENT '工龄',\n" +
            "    entry_date date NOT NULL COMMENT '入职时间',\n" +
            "    probation int NOT NULL COMMENT '试用期(月)',\n" +
            "    create_time datetime NOT NULL COMMENT '创建时间',\n" +
            "    update_time datetime NOT NULL COMMENT '更新时间',\n" +
            "    UNIQUE INDEX employee_id_uindex (id),\n" +
            "    UNIQUE INDEX employee_user_id_uindex (user_id)\n" +
            ");")
    void createEmployeeTable();

    @Update("CREATE TABLE shopping\n" +
            "(\n" +
            "    id int PRIMARY KEY NOT NULL COMMENT '主键id' AUTO_INCREMENT,\n" +
            "    user_id int NOT NULL COMMENT '用户id',\n" +
            "    order_no varchar(64) NOT NULL COMMENT '订单号',\n" +
            "    product_name varchar(128) NOT NULL COMMENT '商品名称',\n" +
            "    product_code varchar(64) NOT NULL COMMENT '商品编号',\n" +
            "    product_price decimal NOT NULL COMMENT '商品价格',\n" +
            "    payment_channel int NOT NULL COMMENT '支付渠道,0:社交账号,1:银行卡',\n" +
            "    channel_name varchar(64) NOT NULL COMMENT '渠道名',\n" +
            "    payment_amount decimal(10,2) NOT NULL COMMENT '支付金额',\n" +
            "    with_hold tinyint NOT NULL COMMENT '是否代扣,0:未代扣,1:代扣',\n" +
            "    with_hold_amount decimal NOT NULL COMMENT '代扣金额',\n" +
            "    payment_number varchar(64) NOT NULL COMMENT '支付卡号(社交账号,银行卡号)',\n" +
            "    repayment_period int NOT NULL COMMENT '还款期数',\n" +
            "    payment_time datetime NOT NULL COMMENT '支付时间',\n" +
            "    create_time datetime NOT NULL COMMENT '创建时间',\n" +
            "    update_time datetime NOT NULL COMMENT '更新时间',\n" +
            "    UNIQUE INDEX shopping_id_uindex (id),\n" +
            "    UNIQUE INDEX shopping_order_no_uindex (order_no)\n" +
            ");")
    void createShoppingTable();

    @Update("CREATE TABLE commodity\n" +
            "(\n" +
            "    id int PRIMARY KEY NOT NULL COMMENT '主键id' AUTO_INCREMENT,\n" +
            "    product_type varchar(64) NOT NULL COMMENT '商品类型',\n" +
            "    product_name varchar(128) NOT NULL COMMENT '商品名称',\n" +
            "    product_code varchar(64) NOT NULL COMMENT '商品编号',\n" +
            "    product_price decimal(10,2) NOT NULL COMMENT '商品价格',\n" +
            "    salas int NOT NULL COMMENT '销量',\n" +
            "    seller varchar(64) NOT NULL COMMENT '卖家',\n" +
            "    inventory int NOT NULL COMMENT '库存',\n" +
            "    goods_supply varchar(64) NOT NULL COMMENT '货源',\n" +
            "    create_time datetime NOT NULL COMMENT '创建时间',\n" +
            "    update_time datetime NOT NULL COMMENT '更新时间',\n" +
            "    UNIQUE INDEX commodity_id_uindex (id),\n" +
            "    UNIQUE INDEX commodity_product_code_uindex (product_code)\n" +
            ");")
    void createCommodityTable();

    @Update("CREATE TABLE repayment_plan\n" +
            "(\n" +
            "    id int PRIMARY KEY NOT NULL COMMENT '主键id' AUTO_INCREMENT,\n" +
            "    user_id int NOT NULL COMMENT '用户id',\n" +
            "    order_no varchar(64) NOT NULL COMMENT '订单号',\n" +
            "    period int NOT NULL COMMENT '期数',\n" +
            "    current_period int NOT NULL COMMENT '当前期数',\n" +
            "    should_amount decimal(10,2) NOT NULL COMMENT '应还总额',\n" +
            "    should_principal decimal(10,2) NOT NULL COMMENT '应还本金',\n" +
            "    should_interest decimal(10,2) NOT NULL COMMENT '应还利息',\n" +
            "    should_overdue decimal(10,2) NOT NULL COMMENT '应还逾期费',\n" +
            "    should_repay_date date NOT NULL COMMENT '应还时间',\n" +
            "    remain_principal decimal(10,2) NOT NULL COMMENT '剩余应还总本金',\n" +
            "    repay_amount decimal(10,2) COMMENT '实还总额',\n" +
            "    repay_principal decimal(10,2) COMMENT '实还本金',\n" +
            "    repay_interest decimal(10,2) COMMENT '实还利息',\n" +
            "    repay_overdue decimal(10,2) COMMENT '实还逾期费',\n" +
            "    real_repay_date date COMMENT '实还时间',\n" +
            "    status tinyint NOT NULL COMMENT '结清标志',\n" +
            "    advance_day int NOT NULL COMMENT '提前结清天数',\n" +
            "    overdue_day int NOT NULL COMMENT '逾期天数',\n" +
            "    create_time datetime NOT NULL COMMENT '创建时间',\n" +
            "    update_time datetime NOT NULL COMMENT '更新时间',\n" +
            "    UNIQUE INDEX repayment_plan_id_uindex (id),\n" +
            "    UNIQUE INDEX repayment_plan_order_no_current_period_uindex (order_no, current_period)\n" +
            ");")
    void createRepaymentPlanTable();

    @Update("CREATE TABLE transaction\n" +
            "(\n" +
            "    id int PRIMARY KEY NOT NULL COMMENT '主键id' AUTO_INCREMENT,\n" +
            "    user_id int NOT NULL COMMENT '用户id',\n" +
            "    transaction_flow_no varchar(64) NOT NULL COMMENT '流水号',\n" +
            "    order_no varchar(64) NOT NULL COMMENT '订单号',\n" +
            "    transaction_amount decimal(10,2) NOT NULL COMMENT '流水金额',\n" +
            "    period int NOT NULL COMMENT '还款期数',\n" +
            "    transaction_time datetime NOT NULL COMMENT '流水时间',\n" +
            "    repay_date date NOT NULL COMMENT '还款时间',\n" +
            "    update_time datetime NOT NULL COMMENT '更新时间',\n" +
            "    create_time datetime NOT NULL COMMENT '创建时间',\n" +
            "    UNIQUE INDEX transaction_id_uindex (id),\n" +
            "    UNIQUE INDEX transaction_transaction_flow_no_uindex (transaction_flow_no),\n" +
            "    UNIQUE INDEX transaction_order_no_uindex (order_no)\n" +
            ");")
    void createTransactionTable();
}
