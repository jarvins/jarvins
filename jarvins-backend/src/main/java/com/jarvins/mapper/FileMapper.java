package com.jarvins.mapper;


import com.jarvins.entity.file.FileInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Component
public interface FileMapper {

    @Insert("insert into file(name, file_name, type, folder,size, path, file_path, parent_path) value (" +
            "#{fileInfo.name},#{fileInfo.fileName},#{fileInfo.type},#{fileInfo.folder},#{fileInfo.size},#{fileInfo.path},#{fileInfo.filePath},#{fileInfo.parentPath})")
    int insertFileInfo(@Param("fileInfo")FileInfo fileInfo);

    @Select("select * from file where parent_path = #{parentPath}")
    List<FileInfo> selectChildFile(@Param("parentPath") String parentPath);

    @Select("select * from file where path = #{path}")
    FileInfo selectFile(@Param("path") String path);

    @Delete("<script>" +
            "delete from file where path in" +
            "<foreach collection = 'list' item = 'item' separator=',' open='(' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    int batchDeleteFile(@Param("list") List<String> list);

    @Update("update file set size = #{size} where path = #{path}")
    int updateFolderSize(@Param("size") BigDecimal size,
                         @Param("path") String path);
}
