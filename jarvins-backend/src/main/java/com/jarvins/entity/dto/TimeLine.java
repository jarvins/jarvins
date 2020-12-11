package com.jarvins.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class TimeLine implements Serializable {

    String date;

    String version;

    List<String> content;
}
