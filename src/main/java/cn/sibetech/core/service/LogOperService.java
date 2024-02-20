package cn.sibetech.core.service;

import cn.sibetech.core.domain.LogOper;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LogOperService extends IService<LogOper> {
    void saveSysLog(LogOper logOper);

    void saveLog(String content);
}
