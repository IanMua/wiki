package com.ianmu.wiki.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategorySaveReq {
    private Long id;

    private Long parent;

    @NotNull(message = "分类名称不能为空")
    private String name;

    @NotNull(message = "排序不能为空")
    private Integer sort;
}