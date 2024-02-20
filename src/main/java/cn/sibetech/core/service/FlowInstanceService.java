package cn.sibetech.core.service;

import java.util.Map;

public interface FlowInstanceService {
    /**
     * 发起
     * @param flowDefId
     * @param variables
     */
    String startProcess(String flowDefId, Map<String, Object> variables);

    String startProcess(String flowDefId,String userId, Map<String, Object> variables);

    /**
     * 查看运行中的流程实例列表
     */


}
