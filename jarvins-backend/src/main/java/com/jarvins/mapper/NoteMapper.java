package com.jarvins.mapper;

import com.jarvins.entity.file.NoteInfo;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("insert into note(name, folder, path, parent_path, content) value (#{noteFileSys.name}, #{noteFileSys.folder}, #{noteFileSys.path}, #{noteFileSys.parentPath}, #{noteFileSys.content})")
    int insertFolder(@Param("noteFileSys") NoteInfo noteFileSys);

    @Update("update note set content = #{content}, update_time = CURRENT_TIMESTAMP where path = #{path}")
    int updateNote(@Param("content") String content,
                   @Param("path") String path);

    @Update("update note set size = #{size}, update_time = CURRENT_TIMESTAMP where path = #{path}")
    int updateFolderSize(@Param("size") BigDecimal size,
                         @Param("path") String path);

    @Select("select * from note where yn = true and path = #{path}")
    NoteInfo selectNote(@Param("path") String path);

    @Select("select * from note where yn = true and parent_path = #{parentPath}")
    List<NoteInfo> selectChild(@Param("parentPath") String parentPath);

    @Update("<script>" +
            "update note set yn = false, update_time = CURRENT_TIMESTAMP where yn = true and path in" +
            "<foreach collection = 'list' item = 'item' separator=',' open='(' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    int batchDeleteNote(@Param("list") List<String> list);
}
