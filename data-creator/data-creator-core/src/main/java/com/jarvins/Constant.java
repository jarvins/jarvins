package com.jarvins;


import java.math.BigDecimal;
import java.time.LocalDate;

public class Constant {

    //统计截止年
    public final static int YEAR = 2020;

    //统计截止时间
    public final static LocalDate DATE = LocalDate.of(2020,12,31);

    //商品信息获取地址
    public static final String URL = "https://api-gw.onebound.cn/taobao/item_search?key=tel13259911525&secret=20201022&api_name=item_search&q=%s&start_price=%s&end_price=%s&page=%s&cat=0&discount_only=&sort=&page_size=&seller_info=&nick=&ppath=&imgid=&filter=";

    //日期格式
    public static final String DATA_FORMAT = "yyyy-MM-dd";

    //时间格式
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //月利率,年利18%
    public static final BigDecimal MONTH_RATE = new BigDecimal("0.015");

    //日利率
    public static final BigDecimal DAY_RATE = new BigDecimal("0.0005");

    //逾期日利率
    public static final BigDecimal OVERDUE_RATE = new BigDecimal("0.0005");

    //一年的月份
    public static final int MONTH = 12;

    //批量插入的数量
    public static final int BATCH_INSERT_SIZE = 100;
}
