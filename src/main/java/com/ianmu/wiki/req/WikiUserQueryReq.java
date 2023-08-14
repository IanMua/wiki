package com.ianmu.wiki.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WikiUserQueryReq extends PageReq {
    private Long id;

    private String loginName;

    private String name;

    private String password;
}