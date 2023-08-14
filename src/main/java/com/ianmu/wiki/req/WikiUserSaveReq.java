package com.ianmu.wiki.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class WikiUserSaveReq {
    private Long id;

    @NotNull(message = "登录名不能为空")
    @NotBlank(message = "登录名不能为空")
    private String loginName;

    private String name;

    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[._~!@#$^&*])[A-Za-z0-9._~!@#$^&*]{6,20}$", message = "密码至少包含数字、英文和特殊符号，长度6-20")
    private String password;
}