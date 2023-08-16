package com.ianmu.wiki.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WikiUserResetPasswordReq {
    @NotNull(message = "id不能为空")
    private Long id;

    @NotNull(message = "密码不能为空")
    @Size(min = 32, max = 32, message = "密码长度错误")
    private String password;
}