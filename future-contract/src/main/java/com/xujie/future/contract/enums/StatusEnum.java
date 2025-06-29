package com.xujie.future.contract.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    CLOSED(0, "关闭"),
    OPENED(1, "启用");
    private final Integer code;
    private final String desc;
}
