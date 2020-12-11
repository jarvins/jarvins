### 数据随机生成器

#### 目的
生成百万级数据用于学习测试

#### 生成方式
运行程序将直接生成数据并创建对应的数据库表，同时完成数据插入

#### 注意
- 采用mysql数据库，使用mybatis-spring-boot-starter,请自行配置数据库信息
- 自动创建表，请保证并未使用这些表(或者没有重要数据),如果有冲突，请考虑备份自己的数据，或修改创建的表名

#### 表结构设计

- 用户表(user)
    - id 主键id
    - name 姓名
    - sex 性别(0 = 男性,1 = 女性)
    - age 年龄
    - phone 电话
    - born 出生年月日
    - zodiac 生肖
    - address 家庭住址
    - married 是否结婚
    - create_time 创建时间
    - update_time 更新时间
    
- 工作表(employee)
  - id 主键id
  - user_id 用户id
  - company_name 工作单位
  - position 职位
  - salary 薪资
  - work_year 工龄
  - entry_date 入职时间
  - probation 试用期(月)
  - create_time 创建时间
  - update_time 更新时间
  
- 购物表(shopping,如果用户分期,则当期由平台代扣,用户从一个月后开始还款)
  - id 主键id
  - user_id 用户id
  - order_no 订单号
  - product_name 商品名称
  - product_code 商品编号
  - product_price 商品价格
  - payment_channel 支付渠道(0 = 社交账号,1 = 银行卡)
  - channel_name 渠道名
  - payment_amount 支付金额
  - with_hold 是否代扣(0 = 未代扣, 1 = 代扣)
  - with_hold_amount 代扣金额
  - payment_number 支付卡号(社交账号,银行卡号)
  - repayment_period 还款期数
  - payment_time 支付时间
  - create_time 创建时间
  - update_time 更新时间
  
- 商品表(commodity)
  - id 主键id
  - product_type 商品类型
  - product_name 商品名称
  - product_code 商品编号
  - product_price 商品价格
  - seller 卖家
  - salas 销量
  - inventory 库存
  - goods_supply 货源
  - create_time 创建时间
  - update_time 更新时间
  
  
- 还款计划表(repayment_plan,如果用户购买商品使用了平台代扣,则会生成还款计划)
   - id 主键id
   - user_id 用户id
   - order_no 订单号
   - period 还款期数
   - current_period 当前期数
   - should_amount 应还总额
   - should_principal 应还本金
   - should_interest 应还利息
   - should_overdue 应还逾期费
   - should_repay_date 应还时间
   - remain_principal 剩余应还总本金
   - repay_amount 实还总额
   - repay_principal 实还本金
   - repay_interest 实还利息
   - repay_overdue 实还逾期费
   - real_repay_date 实还时间
   - status 结清标志
   - advance_day 提前结清天数
   - overdue_day 逾期天数
   - create_time 创建时间
   - update_time 更新时间
 
- 还款流水表(transaction,还款流水表,每生成一次流水都会触发用户还钱逻辑)
   - id 主键id
   - user_id 用户id
   - transaction_flow_no 流水号
   - order_no 订单号
   - transaction_amount 流水金额
   - period 还款期数
   - transaction_time 流水时间
   - repay_date 还款时间
   - create_time 创建时间
   - update_time 更新时间
