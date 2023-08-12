package com.ianmu.wiki.resp;

import lombok.Data;

@Data
public class DocQueryResp {
    private Long id;

    private Long ebookId;

    private Long parent;

    private String name;

    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;
}