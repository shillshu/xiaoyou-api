package cn.sibetech.core.service;

import cn.sibetech.core.domain.LogLogin;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LogLoginService extends IService<LogLogin> {
    void saveLog(String username, String content, String clientIp, String clientSystem, String clientBrowser, String status);
}
