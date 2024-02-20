package cn.sibetech.core.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class FlowTaskVo {
    // 任务id
    private String taskId;
    // 意见
    private String comment;

    private String instanceId;

    private String flowDefId;

    private Map<String, Object> variables;

    private String assignee;

    private String nextNodeId;

    private String dataId;

    private String result;
    private String flowKey;

    private String status;
}
