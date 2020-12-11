package com.jarvins.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Memorandum implements Serializable {

    int id;

    String title;

    String label;

    String link;

    boolean yn;

    LocalDateTime createTime;

    LocalDateTime updateTime;
}
