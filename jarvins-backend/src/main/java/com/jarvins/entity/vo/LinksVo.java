package com.jarvins.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class LinksVo implements Serializable {

    private List<Link> links;

    private  List<Folder> folders;

    @Data
    @Builder
    @AllArgsConstructor
    public static class Link implements Serializable{
        private String name;
        private String link;
    }

    @Data
    @Builder
    public static class Folder implements Serializable{
        String folderName;
        private List<Link> links;
    }
}
