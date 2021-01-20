package com.jarvins.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Links {

    private int id;

    private String name;

    private String links;

    private String iconLink;

    private String parent;

    private LocalDateTime createTime;
}
