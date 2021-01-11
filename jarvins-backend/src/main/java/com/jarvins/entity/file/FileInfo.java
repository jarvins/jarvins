package com.jarvins.entity.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo implements Serializable {

    private int id;

    private String name;

    private String fileName;

    private String type;

    private boolean folder;

    private BigDecimal size;

    private String path;

    private String filePath;

    private String parentPath;

    private LocalDateTime createTime;

    public FileInfo(String name, String fileName, String type, boolean folder, BigDecimal size, String path, String filePath, String parentPath) {
        this.name = name;
        this.fileName = fileName;
        this.type = type;
        this.folder = folder;
        this.size = size;
        this.path = path;
        this.filePath = filePath;
        this.parentPath = parentPath;
    }
}
