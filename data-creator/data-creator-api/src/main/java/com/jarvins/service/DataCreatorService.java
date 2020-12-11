package com.jarvins.service;


import com.jarvins.Constant;
import com.jarvins.builder.Shop;
import com.jarvins.builder.ShopBuilder;
import com.jarvins.builder.constructor.*;
import com.jarvins.entity.*;
import com.jarvins.mapper.DataInsertMapper;
import com.jarvins.mapper.SearchMapper;
import com.jarvins.mapper.TableCreatorMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataCreatorService {

    @Resource
    DataInsertMapper dataInsertMapper;

    @Resource
    SearchMapper searchMapper;

    @Resource
    TableCreatorMapper tableCreatorMapper;

    public String createUser(int times) {
        //创建表
        if (tableCreatorMapper.showTable("user") == null) {
            tableCreatorMapper.createUserTable();
        }
        List<User> users;
        for (int i = 0; i < times; i++) {
            /*
            表数据量越大，手机号重复率会增高，由于每次批量插100条，非常容易DuplicateKeyException
            这里先控制手机号不发生重复
             */
            users = filterCreatUser(Constant.BATCH_INSERT_SIZE);
            try {
                int row = dataInsertMapper.batchInsertUser(users);
                log.info("table user: insert [{}] row, total times: [{}], current time: {}.", row, times, i + 1);
            } catch (DuplicateKeyException e) {
                log.warn("table user: DuplicateKeyException,retry.");
                i--;
            } catch (Exception e) {
                log.error("table user: unexpected exception occurred, stop.", e);
                return String.format("table user: total times: %s, finished: %s.", times, i);
            }
        }
        return "OK";
    }

    public String createEmployee(int times) {
        //创建表
        if (tableCreatorMapper.showTable("employee") == null) {
            tableCreatorMapper.createEmployeeTable();
        }
        for (int i = 0; i < times; i++) {
            List<Integer> ids = searchMapper.randomSearchUserIdNotInEmployee(Constant.BATCH_INSERT_SIZE);
            List<Employee> employees = EmployeeConstructor.batchCreate(ids);
            try {
                int row = dataInsertMapper.batchInsertEmployee(employees);
                log.info("table employee: insert {} row, total times: {}, current time: {}.", row, times, i + 1);
            } catch (DuplicateKeyException e) {
                log.warn("table employee: DuplicateKeyException,retry.");
                i--;
            } catch (Exception e) {
                log.error("table employee: unexpected exception occurred, stop.", e);
                return String.format("table employee: total times: %s, finished: %s.", times, i);
            }
        }
        return "OK";
    }

    public String createCommodity(int times) {
        //创建表
        if (tableCreatorMapper.showTable("commodity") == null) {
            tableCreatorMapper.createCommodityTable();
        }
        //太慢了，改用线程池
        ThreadPoolExecutor service = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        CountDownLatch latch = new CountDownLatch(times);
        for (int i = 0; i < times; i++) {
            Runnable task = () -> {
                while (true) {
                    List<Commodity> commodities = CommodityConstructor.create();
                    try {
                        int row = dataInsertMapper.batchInsertCommodity(commodities);
                        latch.countDown();
                        log.info("table commodity: insert {} row, remains {}.", row, latch.getCount());
                        break;
                    } catch (DuplicateKeyException e) {
                        log.warn("table commodity: DuplicateKeyException,retry.");
                    } catch (Exception e) {
                        latch.countDown();
                        log.error("table commodity: unexpected exception occurred, remains {}.", latch.getCount(), e);
                        break;
                    }
                }
            };
            service.submit(task);
        }
        log.info("table commodity: task count {}", service.getTaskCount());
        service.shutdown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.warn("table commodity: countDownLatch, unexpected InterruptedException.");
        }
        log.info("table commodity: thread pool finish task.");
        return "Ok";
    }

    public String createShopping(int times) {
        //创建表
        if (tableCreatorMapper.showTable("shopping") == null) {
            tableCreatorMapper.createShoppingTable();
        }
        for (int i = 0; i < times; i++) {
            List<Integer> userIds = searchMapper.randomSearchUserId(Constant.BATCH_INSERT_SIZE);
            List<Commodity> commodities = searchMapper.randomSearchCommodity(Constant.BATCH_INSERT_SIZE);
            List<Shop> shops = ShopBuilder.build(userIds, commodities);
            List<Shopping> shoppings = ShoppingConstructor.batchCreate(shops);
            try {
                //购物信息
                int row = dataInsertMapper.batchInsertShopping(shoppings);
                log.info("table shopping: insert {} row, total times: {}, current time: {}.", row, times, i + 1);
                //生成还款计划
                createRepaymentPlan(shoppings);
            } catch (DuplicateKeyException e) {
                log.warn("table shopping: DuplicateKeyException,retry.");
                i--;
            } catch (Exception e) {
                log.error("table shopping: unexpected exception occurred, stop.", e);
                return String.format("table shopping: total times: %s, finished: %s.", times, i);
            }
        }
        return "Ok";
    }

    /**
     * 每次过滤掉重复的手机号和地址，保证插入的效率
     *
     * @param size 批量插入的数据量
     * @return 无重复的批量数据
     */
    private List<User> filterCreatUser(int size) {
        Set<User> result = new LinkedHashSet<>();
        while (result.size() < size) {
            //手机号过滤
            List<User> users = UserConstructor.batchCreate(size);
            Set<String> phones = users.stream().map(User::getPhone).collect(Collectors.toSet());
            List<String> repeatPhone = searchMapper.selectRepeatPhone(phones);
            List<User> notRepeat = users.stream().filter(e -> !repeatPhone.contains(e.getPhone())).collect(Collectors.toList());
            //地址过滤
            if (notRepeat.size() > 0) {
                Set<String> address = notRepeat.stream().map(User::getAddress).collect(Collectors.toSet());
                List<String> repeatAddress = searchMapper.selectRepeatAddress(address);
                notRepeat = notRepeat.stream().filter(e -> !repeatAddress.contains(e.getAddress())).collect(Collectors.toList());
            }

            //合法
            result.addAll(notRepeat);
        }
        return result.stream().limit(size).collect(Collectors.toList());
    }

    private void createRepaymentPlan(List<Shopping> list) {
        //过滤出代扣的购物信息
        List<Shopping> withHold = list.stream().filter(Shopping::isWithHold).collect(Collectors.toList());
        log.info("table repayment_plan: shopping size {}", withHold.size());
        List<RepaymentPlan> repaymentPlanList = new LinkedList<>();
        for (Shopping shopping : withHold) {
            List<RepaymentPlan> repaymentPlans = RepaymentPlanConstructor.create(shopping.getUserId(), shopping.getOrderNo(), shopping.getWithHoldAmount(), shopping.getRepaymentPeriod(), shopping.getPaymentTime().toLocalDate());
            repaymentPlanList.addAll(repaymentPlans);
        }
        log.info("table repayment_plan: {} row will be inserted.", repaymentPlanList.size());
        //创建表
        if (tableCreatorMapper.showTable("repayment_plan") == null) {
            tableCreatorMapper.createRepaymentPlanTable();
        }
        try {
            int row = dataInsertMapper.batchInsertRepaymentPlan(repaymentPlanList);
            if (row != repaymentPlanList.size()) {
                log.warn("table repayment_plan: except {} row data inserted, {} in fact.", repaymentPlanList.size(), row);
            } else {
                log.info("table repayment_plan: insert {} row.", row);
            }
            //理论上这里是不会发生DuplicateKeyException
        } catch (Exception e) {
            log.error("table shopping: unexpected exception occurred, stop.", e);
        }
    }
}
