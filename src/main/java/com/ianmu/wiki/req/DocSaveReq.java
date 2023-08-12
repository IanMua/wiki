package com.ianmu.wiki.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocSaveReq {
    private Long id;

    @NotNull(message = "电子书ID不能为空")
    private Long ebookId;

    @NotNull(message = "父文档不能为空")
    private Long parent;

    @NotNull(message = "名称不能为空")
    private String name;

    @NotNull(message = "顺序不能为空")
    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;
}