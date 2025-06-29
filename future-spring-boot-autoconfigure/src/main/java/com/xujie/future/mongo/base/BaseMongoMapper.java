package com.xujie.future.mongo.base;

import com.mongoplus.aggregate.AggregateWrapper;
import com.mongoplus.conditions.query.QueryWrapper;
import com.mongoplus.conditions.update.UpdateWrapper;
import com.mongoplus.enums.OrderEnum;
import com.mongoplus.mapper.MongoMapperImpl;
import com.mongoplus.model.PageParam;
import com.mongoplus.model.PageResult;
import com.xujie.future.contract.request.PageRequest;

import java.util.List;

public abstract class BaseMongoMapper<T> extends MongoMapperImpl<T> {

    public QueryWrapper<T> createMgoQuery() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        return wrapper;
    }

    public UpdateWrapper<T> createMgoUpdate() {

        UpdateWrapper<T> wrapper = new UpdateWrapper<>();
        return wrapper;
    }

    public List<T> aggregateList(AggregateWrapper wrapper) {
        return this.baseMapper.aggregateList(wrapper, this.clazz);
    }

    public PageResult<T> page(QueryWrapper<T> queryWrapper, PageRequest pageRequest) {
        PageParam pageParam = new PageParam(pageRequest.getPage(), pageRequest.getLimit());
        String field = pageRequest.getField();
        if ("create_time".equals(field)) {
            field = "createTime";
        }
        if ("update_time".equals(field)) {
            field = "updateTime";
        }
        queryWrapper.order(field, OrderEnum.valueOf(pageRequest.getOrder().getCode()).getValue());
        return this.page(queryWrapper, pageParam);
    }


}
