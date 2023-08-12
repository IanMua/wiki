package com.ianmu.wiki.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DocQueryReq extends PageReq{
    private Long id;

    private Long parent;

    private String name;
}