package com.jarvins.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FileSys implements Serializable{

    String name;

    String type;

    String content;

    boolean folder;
}
