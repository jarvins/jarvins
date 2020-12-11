package com.jarvins.entity.file;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class NoteInfo implements Serializable {

    int id;

    String name;

    boolean folder;

    String path;

    String parentPath;

    String content;

    boolean yn;

    LocalDateTime createTime;

    LocalDateTime updateTime;
}
