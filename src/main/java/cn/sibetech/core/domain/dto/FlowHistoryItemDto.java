package cn.sibetech.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FlowHistoryItemDto {
    private String activityId;
    private String activityName;
    private String activityType;
    private String taskId;
    private String processDefinitionId;
    private String processInstanceId;
    private String executionId;
    private String assignee;
    private String assigneeUsername;
    private String assigneeName;
    private String assigneeDeptName;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private FlowCommentDto comment;
    private FlowCommentDto status;
}
