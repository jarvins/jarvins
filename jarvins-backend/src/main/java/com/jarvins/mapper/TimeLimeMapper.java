package com.jarvins.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TimeLimeMapper {

    @Select("select * from timeline")
    List<Map<String,String>> timeLine();
}
