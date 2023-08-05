package com.ianmu.wiki.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EbookSaveReq {
    private Long id;

    @NotNull(message = "书名不能为空")
    @NotBlank(message = "书名不能为空")
    private String name;

    @NotNull(message = "分类一不能为空")
    @Min(value = 0, message = "分类一类型错误")
    private Long category1Id;

    @NotNull(message = "分类二不能为空")
    @Min(value = 0, message = "分类二类型错误")
    private Long category2Id;

    private String description;

    private String cover;

    private Integer docCount = 0;

    private Integer viewCount = 0;

    private Integer voteCount = 0;
}