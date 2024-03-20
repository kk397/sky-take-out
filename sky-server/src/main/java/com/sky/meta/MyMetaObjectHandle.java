package com.sky.meta;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sky.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandle implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        String empId = BaseContext.getCurrentId();
        metaObject.setValue("createUser", empId);
        metaObject.setValue("updateUser", empId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info(metaObject.toString());
        metaObject.setValue("updateTime",LocalDateTime.now());
        String empId = BaseContext.getCurrentId();
        metaObject.setValue("updateUser", empId);
    }
}
