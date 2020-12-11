package com.jarvins.entity.file;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;


@Getter
public class FileType {

    private static final List<String> ALLOW_TYPE = Arrays.asList("doc","docx","pdf","xls","xlsx","png","jpg","jpeg","gif","mp4","rmvb","avi","zip","rar");

    /**
     * 通过文件头字节也无法判断文件类型,直接使用文件后缀
     */
    public static String getType(String name) {
        String type =  name.substring(name.lastIndexOf(".")+1);
        if(ALLOW_TYPE.contains(type)){
            return type;
        }
        throw new IllegalArgumentException();
    }
}
