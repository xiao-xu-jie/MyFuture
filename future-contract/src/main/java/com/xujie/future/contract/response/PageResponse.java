package com.xujie.future.contract.response;

import com.xujie.future.contract.request.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 分页统一返回体
 *
 * @param <T>
 */
@Data
public class PageResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private int pageNo;

    /**
     * 页面条数
     */
    private int pageSize;

    /**
     * 总条数
     */
    private int totalCount;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 分页数据
     */
    private Collection<T> data = new ArrayList<>();

    public PageResponse() {
        super();
    }

    public PageResponse(PageRequest page) {
        // long 转 int
        int totalCountInt = (int) totalCount;
        this.pageSize = page.getLimit();
        this.pageNo = page.getPage();
        this.setTotalCount(totalCountInt);
    }

    //当总条数确定时，总条数确定
    private void setTotalCount(int totalCount) {
        this.totalCount = totalCount;

        totalPage = totalCount % this.pageSize == 0
                ? totalCount / this.pageSize
                : totalCount / this.pageSize + 1;

    }

    //当总条数确定时，总条数确定
    public void setValue(PageRequest pageRequest, int totalCount, Collection<T> data) {
        this.data = data;
        this.pageSize = pageRequest.getLimit();
        this.pageNo = pageRequest.getPage();
        this.setTotalCount(totalCount);

    }

    public void setValue(PageRequest pageRequest, long totalCount, Collection<T> data) {
        // long 转 int
        int totalCountInt = (int) totalCount;
        this.data = data;
        this.pageSize = pageRequest.getLimit();
        this.pageNo = pageRequest.getPage();
        this.setTotalCount(totalCountInt);

    }

}