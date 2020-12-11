package com.jarvins.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZhihuDTO implements Serializable {

    int rank;

    int hotCount;

    String title;

    String extra;

    int answerCount;

    String href;
}
