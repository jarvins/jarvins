package com.jarvins.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JuejinItem implements Serializable {

    private int collectionCount;

    private String url;

    private String title;

    private String user;

    private String time;
}
