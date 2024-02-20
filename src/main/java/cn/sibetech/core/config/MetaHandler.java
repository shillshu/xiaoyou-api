package cn.sibetech.core.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @author hexl
 * @date 2019/9/20
 */
@Component
public class MetaHandler implements MetaObjectHandler {

    /**
     * insert 自动填充whenCreated,whenModified属性为系统当前时间
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        setFieldValByName("whenCreated", now, metaObject);
        setFieldValByName("whenModified", now, metaObject);
    }

    /**
     * update 自动填充whenModified属性为系统当前时间
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        setFieldValByName("whenModified", now, metaObject);
    }
}
