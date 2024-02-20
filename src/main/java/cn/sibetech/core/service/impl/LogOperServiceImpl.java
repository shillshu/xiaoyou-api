package cn.sibetech.core.service.impl;

import cn.sibetech.core.domain.ShiroUser;
import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.domain.LogOper;
import cn.sibetech.core.mapper.LogOperMapper;
import cn.sibetech.core.service.LogOperService;
import cn.sibetech.core.util.ServletUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class LogOperServiceImpl extends ServiceImpl<LogOperMapper, LogOper> implements LogOperService {

    @Override
    public void saveSysLog(LogOper dto) {
        baseMapper.insert(dto);
    }

    @Override
    public void saveLog(String content) {
        ShiroUser user = SecurityContextHolder.getCurrentUser();
        LogOper dto = new LogOper();
        dto.setUsername(user.getUsername());
        dto.setContent(content);
        HttpServletRequest request = ServletUtils.getRequest();
        String ip = ServletUtils.getIpAddress(request);
        dto.setClientIp(ip);
        String[] clients = ServletUtils.getClientExplorer(request);
        if(clients!=null && clients.length==2){
            dto.setClientSystem(clients[0]);
            dto.setClientBrowser(clients[1]);
        }
        baseMapper.insert(dto);
    }
}
