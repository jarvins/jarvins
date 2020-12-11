package com.jarvins.builder.constructor;

import com.jarvins.builder.BankCardBuilder;
import com.jarvins.builder.Shop;
import com.jarvins.entity.Shopping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.jarvins.Constant.DATA_FORMAT;

/**
 * 这里是业务关联行为的:
 * 购买一次商品,该商品的存货量-1,销量+1
 * 根据自身,选择是否需要关联业务逻辑
 */
public class ShoppingConstructor {

    private static final BigDecimal AMOUNT_3000 = new BigDecimal("3000");

    private static final int[] BELOW = {1, 3, 6};

    private static final int[] ABOVE = {3, 6, 12};

    /*
    订单号生成规则: 当前日期 + uuid + 随机1-31
    支付逻辑: 2种渠道概率均分
    有40%的概率平台代扣(逻辑上生成还款计划)
    平台代扣时,用户支付金额在商品价格的10% - 40%之间随机(取整)
    平台代扣时采用分期还款,分期支持3,6,12期(代扣金额大于3000可采用3,6,12,小于3000可采用1,3,6)
     */
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static Shopping create(int userId, String productName, String productCode, BigDecimal productPrice) {
        String orderNo = (DateTimeFormatter.ofPattern(DATA_FORMAT).format(LocalDate.now()) + UUID.randomUUID().toString() + RANDOM.nextInt(1, 32)).replaceAll("-", "");
        String paymentNumber;
        String channelName;
        int paymentChannel = RANDOM.nextInt(2);
        //社交账号,数据库唯一性约束
        if (paymentChannel == 0) {
            long num = RANDOM.nextInt(1, 10) +
                    RANDOM.nextInt(10, 100) +
                    RANDOM.nextInt(10, 1000) +
                    RANDOM.nextInt(1, 10000);
            paymentNumber = String.valueOf(num);
            channelName = "社交账号";
        }
        //银行卡号
        else {
            paymentNumber = BankCardBuilder.build();
            channelName = "银行卡号";
        }

        //40%的平台代扣概率
        boolean withHold = RANDOM.nextInt(10) < 4;
        //自己全款购买
        if (!withHold) {
            return Shopping.builder()
                    .userId(userId)
                    .orderNo(orderNo)
                    .productName(productName)
                    .productCode(productCode)
                    .productPrice(productPrice)
                    .paymentChannel(paymentChannel)
                    .channelName(channelName)
                    .paymentAmount(productPrice)
                    .withHold(false)
                    .withHoldAmount(BigDecimal.ZERO)
                    .paymentNumber(paymentNumber)
                    .repaymentPeriod(0)
                    .paymentTime(LocalDateTime.now())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
        }

        //支付比例
        int rate = RANDOM.nextInt(10, 51);
        //取整
        BigDecimal paymentAmount = new BigDecimal(productPrice.intValue() * rate / 100);
        //平台代扣金额
        BigDecimal withHoldAmount = productPrice.subtract(paymentAmount);
        //还款期数
        int period;
        if (withHoldAmount.compareTo(AMOUNT_3000) > 0) {
            period = ABOVE[RANDOM.nextInt(ABOVE.length)];
        } else {
            period = BELOW[RANDOM.nextInt(BELOW.length)];
        }
        return Shopping.builder()
                .userId(userId)
                .orderNo(orderNo)
                .productName(productName)
                .productCode(productCode)
                .productPrice(productPrice)
                .paymentChannel(paymentChannel)
                .channelName(channelName)
                .paymentAmount(paymentAmount)
                .withHold(true)
                .withHoldAmount(withHoldAmount)
                .paymentNumber(paymentNumber)
                .repaymentPeriod(period)
                .paymentTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }


    public static List<Shopping> batchCreate(List<Shop> shops) {
        List<Shopping> list = new LinkedList<>();
        shops.forEach(e -> list.add(create(e.getUserId(), e.getProductName(), e.getProductCode(), e.getProductPrice())));
        return list;
    }

    public static void main(String[] args) {
        Shopping shopping = create(12, "华为耳机", "11023410324", new BigDecimal("1199"));
        System.out.println(shopping);
    }
}
