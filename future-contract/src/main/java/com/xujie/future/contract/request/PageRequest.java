package com.xujie.future.contract.request;

import com.xujie.future.contract.enums.OrderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PageRequest {
    /**
     * 页码
     *
     * @mock 1
     */
    private Integer page = 1;
    /**
     * 每页数量
     *
     * @mock 10
     */
    private Integer limit = 10;
    /**
     * 排序
     */
    private OrderEnum order = OrderEnum.ASC;
    /**
     * 排序字段
     *
     * @mock create_time
     */
    private String field = "create_time";


}
