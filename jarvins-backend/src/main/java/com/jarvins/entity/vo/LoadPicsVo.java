package com.jarvins.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class LoadPicsVo implements Serializable {

    private String selectedPic;

    private List<String> pics;
}
