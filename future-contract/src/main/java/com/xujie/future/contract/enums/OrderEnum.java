package com.xujie.future.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OrderEnum {
    ASC("正序", "ASC"),
    DESC("倒序", "DESC");

    private final String desc;
    private final String code;
}
