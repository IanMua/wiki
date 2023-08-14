package com.ianmu.wiki.entity;

import lombok.Data;

@Data
public class WikiUser {
    private Long id;

    private String loginName;

    private String name;

    private String password;
}