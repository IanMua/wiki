package com.ianmu.wiki.req;

import lombok.Data;

@Data
public class PageReq{
    private Integer page;

    private Integer size;
}