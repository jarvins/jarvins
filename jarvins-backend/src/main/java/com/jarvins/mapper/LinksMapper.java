package com.jarvins.mapper;

import com.jarvins.entity.dto.Links;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LinksMapper {

    @Select("select * from links")
    List<Links> links();

    @Insert("insert into links(name,links,parent) value(#{name},#{links},#{parent})")
    int addLinks(@Param("name") String name,
                                @Param("links") String links,
                                @Param("parent") String parent);
}
