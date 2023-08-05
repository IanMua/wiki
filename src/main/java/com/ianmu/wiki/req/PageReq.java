package com.ianmu.wiki.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageReq {

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码必须大于0")
    private Integer page;

    @NotNull(message = "每页条数不能为空")
    @Max(value = 1000, message = "每页条数不能超过1000")
    @Min(value = 1, message = "每页条数必须大于0")
    private Integer size;
}