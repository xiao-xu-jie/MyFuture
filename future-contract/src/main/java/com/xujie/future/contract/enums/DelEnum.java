package com.xujie.future.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DelEnum {
    DEL(1, "已删除"),
    UN_DEL(0, "未删除");
    private final Integer code;
    private final String desc;
}
