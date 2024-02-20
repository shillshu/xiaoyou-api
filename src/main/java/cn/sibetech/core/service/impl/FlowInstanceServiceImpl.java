package cn.sibetech.core.service.impl;

import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.service.FlowInstanceService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class FlowInstanceServiceImpl implements FlowInstanceService {
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private IdentityService identityService;
    @Override
    public String startProcess(String flowDefId, Map<String, Object> variables) {
        return startProcess(flowDefId, SecurityContextHolder.getCurrentUser().getUserId(), variables);
    }

    public String startProcess(String flowDefId,String userId, Map<String, Object> variables) {
        // 设置发起人
        identityService.setAuthenticatedUserId(userId);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(flowDefId, variables);
        return instance.getId();
    }
}
