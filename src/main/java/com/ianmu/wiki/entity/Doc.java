package com.ianmu.wiki.entity;

import lombok.Data;

@Data
public class Doc {
    private Long id;

    private Long ebookId;

    private Long parent;

    private String name;

    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;
}