package com.jarvins.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class FileSys implements Serializable{

    String name;

    String type;

    String content;

    boolean folder;

    BigDecimal size;

    String create;
}
