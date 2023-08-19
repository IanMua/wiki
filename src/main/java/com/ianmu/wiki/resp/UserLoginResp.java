package com.ianmu.wiki.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class UserLoginResp {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String loginName;

    private String name;

    @JsonSerialize(using = ToStringSerializer.class)
    private String token;
}