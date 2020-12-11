package com.jarvins.mapper;

import com.jarvins.entity.Commodity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

public interface SearchMapper {

    @Select("select user.id from user order by rand() limit #{limit}")
    List<Integer> randomSearchUserId(@Param("limit") int limit);

    @Select("select user.id from user left join employee on user.id = employee.user_id where user_id is null order by rand() limit #{limit}")
    List<Integer> randomSearchUserIdNotInEmployee(@Param("limit") int limit);

    @Select("select * from commodity order by rand() limit #{limit}")
    List<Commodity> randomSearchCommodity(@Param("limit") int limit);

    @Select("<script>" +
            "select phone from user where phone in " +
            "<foreach collection = 'list' item = 'item' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<String> selectRepeatPhone(@Param("list") Set<String> phones);

    @Select("<script>" +
            "select address from user where address in " +
            "<foreach collection = 'list' item = 'item' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<String> selectRepeatAddress(@Param("list") Set<String> address);
}
