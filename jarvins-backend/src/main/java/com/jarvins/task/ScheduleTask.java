package com.jarvins.task;

import com.jarvins.mapper.BillMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jarvins.entity.Constant.TYPE;

@Slf4j
@Service
public class ScheduleTask {

    /**
     * 每日凌晨00:30统计昨日消费汇总
     */

    @Resource
    BillMapper billMapper;

    @Scheduled(cron = "0 30 0 * * ?")
    public void billSummaryTask(){
        String summaryDate = LocalDate.now().plusDays(-1).toString();
        try {
            List<Map<String, String>> summary = billMapper.billSummary(summaryDate);
            List<String> list = summary.stream().map(e -> e.get("type")).collect(Collectors.toList());
            for (String type : TYPE) {
                if (!list.contains(type)) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("type", type);
                    map.put("amount", "0");
                    map.put("statisticsDate", summaryDate);
                    summary.add(map);
                }
            }
            //插入统计表
            int row = billMapper.insertBillStatistics(summary);
            if(row < summary.size()){
                log.warn("bill summary task: [{}],statistics date；[{}], total size: [{}], insert size:[{}], leave: [{}]", LocalDateTime.now(),summary,list.size(),row,list.size() - row);
            }
            else{
                log.info("bill summary task: [{}],statistics date；[{}], total size: [{}], finished.",LocalDateTime.now(),summaryDate,row);
            }
        }catch (DataIntegrityViolationException e){
            log.error("bill summary task: [{}],statistics date；[{}],sql exception occurred.",LocalDateTime.now(),summaryDate);
            e.printStackTrace();
        }
    }
}
