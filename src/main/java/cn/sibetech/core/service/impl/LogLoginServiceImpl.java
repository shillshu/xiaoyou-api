package cn.sibetech.core.service.impl;

import cn.sibetech.core.domain.LogLogin;
import cn.sibetech.core.mapper.LogLoginMapper;
import cn.sibetech.core.service.LogLoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class LogLoginServiceImpl extends ServiceImpl<LogLoginMapper, LogLogin> implements LogLoginService {
    public void saveLog(String username, String content, String clientIp, String clientSystem, String clientBrowser, String status){
        LogLogin dto = new LogLogin();
        dto.setUsername(username);
        dto.setContent(content);
        dto.setClientIp(clientIp);
        dto.setClientSystem(clientSystem);
        dto.setClientBrowser(clientBrowser);
        dto.setStatus(status);
        baseMapper.insert(dto);
    }
}
