package com.ianmu.wiki.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WikiUserQueryResp extends PageResp {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String loginName;

    private String name;

    private String password;
}