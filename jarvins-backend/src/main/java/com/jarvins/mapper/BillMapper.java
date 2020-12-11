package com.jarvins.mapper;

import com.jarvins.entity.dto.BillRecord;
import com.jarvins.entity.dto.BillStatistics;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface BillMapper {

    @Insert("insert into bill_record(type, amount, label, record_date) value(#{billRecord.type}, #{billRecord.amount}, #{billRecord.label}, #{billRecord.recordDate})")
    int insertBillRecord(@Param("billRecord") BillRecord billRecord);


    @Select("select * from bill_record where record_date = #{recordDate}")
    List<BillRecord> selectBillRecord(@Param("recordDate") String recordDate);

    @Select("select * from bill_statistics where statistics_date between #{start} and #{end}")
    List<BillStatistics> selectBillStatisticsMonthly(@Param("start") String start,
                                               @Param("end") String end);

    @Select("select type type,sum(amount) amount, #{date} statisticsDate from bill_record where record_date = #{date} group by type;")
    List<Map<String, String>> billSummary(@Param("date") String date);

    @Insert("<script>" +
            "insert into bill_statistics(type, amount, statistics_date) values " +
            "<foreach collection='list' item='e' separator=','> " +
            "( #{e.type}, #{e.amount}, #{e.statisticsDate})" +
            "</foreach>" +
            "</script>")
    int insertBillStatistics(@Param("list") List<Map<String,String>> list);
}
