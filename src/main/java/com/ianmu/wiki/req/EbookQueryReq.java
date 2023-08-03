package com.ianmu.wiki.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EbookQueryReq extends PageReq {
    private Integer id;

    private String name;

//    private Long category1Id;
//
//    private Long category2Id;
//
//    private String description;
//
//    private String cover;
//
//    private Integer docCount;
//
//    private Integer viewCount;
//
//    private Integer voteCount;
}