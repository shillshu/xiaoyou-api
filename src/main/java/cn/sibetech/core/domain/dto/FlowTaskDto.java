package cn.sibetech.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FlowTaskDto {
    /**
     * 任务id
     */
    private String taskId;
    private String executionId;

    private String activityId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务Key
     */
    private String taskDefKey;

    private String flowKey;
    private String flowName;
    // 发起人
    private String startUserId;
    // 发起人工号
    private String startUsername;
    // 发起人姓名
    private String startUserXm;
    // 发起人部门
    private String startUserDept;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    // 任务创建时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    // 任务完成时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;
    // 任务耗时
    private String duration;
    // 流程部署编号
    private String deployId;
    // 流程ID
    private String procDefId;
    // 流程实例ID
    private String procInsId;
    // 流程key
    private String procDefKey;
    // 流程定义名称
    private String procDefName;
    // 流程定义内置使用版本
    private int procDefVersion;
    // 流程类型
    private String category;

    // 历史流程实例ID
    private String hisProcInsId;
    /**
     * 任务执行人id
     */
    private String assigneeId;
    // 任务执行人工号
    private String assigneeUsername;
    // 任务执行人姓名
    private String assigneeName;
    // 任务执行人部门名称
    private String assigneeDeptName;
    // 流程变量
    private Map<String, Object> procVars;


    private boolean isFirstNode;
    private String candidate;

    private List<FlowNextDto> nextNodes = new ArrayList<>();

}
