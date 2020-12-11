package com.jarvins.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeiboDTO implements Serializable {

    int rank;

    int hotCount;

    String title;

    String href;
}
