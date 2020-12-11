package com.jarvins.mapper;

import com.jarvins.entity.dto.Memorandum;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemorandumMapper {

    @Insert("insert into memorandum(title, label, link) VALUE(#{item.title}, #{item.label}, #{item.link})")
    int insertMemorandum(@Param("item") Memorandum item);

    @Select("select * from memorandum where yn = true")
    List<Memorandum> selectMemorandum();

    @Update("update memorandum set yn = false, update_time = CURRENT_TIMESTAMP where id = #{id}")
    int completeMemorandum(@Param("id") int id);
}
