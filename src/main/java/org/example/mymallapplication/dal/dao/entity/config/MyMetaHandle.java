package org.example.mymallapplication.dal.dao.entity.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.example.mymallapplication.common.BaseContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaHandle implements MetaObjectHandler {
    public static String infoLog() {
        String userId = BaseContext.getCurrentId();
        if (userId != null) {
            log.info("修改人员：{}", userId);
            return userId;
        } else {
            log.info("未登陆");
            return null;
        }
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填写...");
        String userId = infoLog();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        if (userId != null) {
            this.strictInsertFill(metaObject, "createBy", String.class, userId);
            this.strictInsertFill(metaObject, "updateBy", String.class, userId);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填写...");
        String userId = infoLog();
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        this.strictUpdateFill(metaObject, "updateBy", String.class, userId);
    }
}
