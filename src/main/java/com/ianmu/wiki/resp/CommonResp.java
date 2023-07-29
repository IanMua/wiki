package com.ianmu.wiki.resp;

import lombok.Data;

@Data
public class CommonResp<T> {

    /**
     * 业务成功或失败
     */
    private Boolean success = true;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T content;
}
