package com.xujie.future.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

/**
 * 分页统一返回体
 *
 * @param <T>
 */
@Data
public class PageSupport<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //当前页码
    private int pageNo;

    //页面条数
    private int pageSize;

    //总条数
    private int totalCount;

    //总页数
    private int totalPage;

    //分页数据
    private Collection<T> PageData;

    //当总条数确定时，总条数确定
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;

        totalPage = totalCount % this.pageSize == 0
                ? totalCount / this.pageSize
                : totalCount / this.pageSize + 1;

    }

}