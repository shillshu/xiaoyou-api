package cn.sibetech.core.service.impl;

import cn.sibetech.core.domain.ShiroUser;
import cn.sibetech.core.domain.User;
import cn.sibetech.core.domain.dto.FlowCommentDto;
import cn.sibetech.core.domain.dto.FlowHistoryItemDto;
import cn.sibetech.core.domain.dto.FlowNextDto;
import cn.sibetech.core.domain.dto.FlowTaskDto;
import cn.sibetech.core.domain.vo.FlowActivityFlowVo;
import cn.sibetech.core.domain.vo.FlowActivityVo;
import cn.sibetech.core.domain.vo.FlowQueryVo;
import cn.sibetech.core.domain.vo.FlowTaskVo;
import cn.sibetech.core.exception.WorkflowException;
import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.service.FlowTaskService;
import cn.sibetech.core.service.UserService;
import cn.sibetech.core.util.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.util.ExecutionGraphUtil;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static cn.sibetech.core.util.FlowConstant.COMMENT_MESSAGE;
import static cn.sibetech.core.util.FlowConstant.COMMENT_STATUS;

@Service
public class FlowTaskServiceImpl implements FlowTaskService {
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private UserService userService;
    @Resource
    private RuntimeService runtimeService;

    @Qualifier("processEngine")
    @Resource
    private ProcessEngine processEngine;
    @Override
    public IPage<FlowTaskDto>  queryMyStartedList(FlowQueryVo queryVo) {
        Page<FlowTaskDto> page = new Page(queryVo.getCurrent(), queryVo.getSize());
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .includeProcessVariables();
        ShiroUser user = SecurityContextHolder.getCurrentUser();
        historicProcessInstanceQuery.startedBy(user.getUserId());
        if(CollectionUtils.isNotEmpty(queryVo.getFlowKey())){
            historicProcessInstanceQuery.processDefinitionKeyIn(queryVo.getFlowKey());
        }
        historicProcessInstanceQuery.orderByProcessInstanceStartTime().desc();
        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.listPage(queryVo.getSize() * (queryVo.getCurrent() - 1), queryVo.getSize());
        page.setTotal(historicProcessInstanceQuery.count());
        if(CollectionUtils.isNotEmpty(historicProcessInstances)){
            List<FlowTaskDto> data = historicProcessInstances.stream().map(m->{
                FlowTaskDto dto = new FlowTaskDto();
                dto.setCreateTime(m.getStartTime());
                dto.setFinishTime(m.getEndTime());
                dto.setProcInsId(m.getId());
                // 计算耗时
                if (Objects.nonNull(m.getEndTime())) {
                    long time = m.getEndTime().getTime() - m.getStartTime().getTime();
                    dto.setDuration(getDate(time));
                } else {
                    long time = System.currentTimeMillis() - m.getStartTime().getTime();
                    dto.setDuration(getDate(time));
                }
                // 流程定义信息
                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(m.getProcessDefinitionId())
                        .singleResult();
                dto.setDeployId(pd.getDeploymentId());
                dto.setProcDefName(pd.getName());
                dto.setProcDefVersion(pd.getVersion());
                dto.setCategory(pd.getCategory());
                dto.setFlowKey(pd.getKey());
                dto.setFlowName(pd.getName());
                dto.setProcVars(m.getProcessVariables());
                // 当前所处流程
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(m.getId()).list();
                if (CollectionUtils.isNotEmpty(taskList)) {
                    dto.setTaskId(taskList.get(0).getId());
                    dto.setTaskName(taskList.get(0).getName());
                    dto.setAssigneeId(taskList.get(0).getAssignee());
                } else {
                    List<HistoricActivityInstance> historicActivityInstances =historyService.createHistoricActivityInstanceQuery().processInstanceId(m.getId()).orderByHistoricActivityInstanceEndTime().desc().list();
                    dto.setTaskId(historicActivityInstances.get(0).getId());
                    dto.setTaskName(historicActivityInstances.get(0).getActivityName());
                    dto.setAssigneeId(historicActivityInstances.get(0).getAssignee());
                }
                return dto;
            }).collect(Collectors.toList());
            // 补充流程发起人姓名，工号，部门信息
            List<User> userList = userService.queryByIds(data.stream().filter(m-> StringUtils.isNotEmpty(m.getAssigneeId())).map(FlowTaskDto::getAssigneeId).collect(Collectors.toList()));
            page.setRecords(data.stream().map(m->{
                User u = userList.stream().filter(item->item.getId().equals(m.getStartUserId())).findFirst().orElse(null);
                if(u!=null){
                    m.setAssigneeUsername(u.getUsername());
                    m.setAssigneeName(u.getTruename());
                    m.setAssigneeDeptName(u.getDeptName());
                }
                return m;
            }).collect(Collectors.toList()));
        }
        return page;
    }

    public long queryTodoCount(){
        ShiroUser user = SecurityContextHolder.getCurrentUser();
        TaskQuery taskQuery = taskService.createTaskQuery().active().includeProcessVariables().taskCandidateOrAssigned(user.getUserId());
        if(CollectionUtils.isNotEmpty(user.getFlowGroups())) {
            List<String> groups = user.getFlowGroups().stream().map(m->m.getName()).collect(Collectors.toList());
            taskQuery.taskCandidateGroupIn(groups);
        }
        taskQuery.orderByTaskCreateTime().desc();
        return taskQuery.count();
    }

    @Override
    public IPage<FlowTaskDto> queryTodoPage(FlowQueryVo queryVo) {
        Page<FlowTaskDto> page = new Page(queryVo.getCurrent(), queryVo.getSize());
        TaskQuery taskQuery = taskService.createTaskQuery().active().includeProcessVariables();
        ShiroUser user = SecurityContextHolder.getCurrentUser();
        taskQuery.taskCandidateOrAssigned(user.getUserId());
        if(CollectionUtils.isNotEmpty(user.getFlowGroups())) {
            List<String> groups = user.getFlowGroups().stream().map(m->m.getName()).collect(Collectors.toList());
            taskQuery.taskCandidateGroupIn(groups);
        }
        if(CollectionUtils.isNotEmpty(queryVo.getFlowKey())){
            taskQuery.processDefinitionKeyIn(queryVo.getFlowKey());
        }

        taskQuery.orderByTaskCreateTime().desc();
        List<Task> taskList = taskQuery.listPage(queryVo.getSize() * (queryVo.getCurrent() - 1), queryVo.getSize());
        page.setTotal(taskQuery.count());
        if(CollectionUtils.isNotEmpty(taskList)){
            page.setRecords(convertFlowTask(taskList));
        }
        return page;
    }

    private List<FlowTaskDto> convertFlowTask(List<Task> taskList){
        if(CollectionUtils.isEmpty(taskList)){
            return null;
        }
        List<FlowTaskDto> data = taskList.stream().map(m->{
            FlowTaskDto dto = new FlowTaskDto();
            dto.setTaskId(m.getId());
            dto.setTaskName(m.getName());
            dto.setTaskDefKey(m.getTaskDefinitionKey());
            dto.setProcDefId(m.getProcessDefinitionId());
            dto.setProcInsId(m.getProcessInstanceId());
            dto.setCreateTime(m.getCreateTime());
            dto.setExecutionId(m.getExecutionId());
            BpmnModel bpmnModel = repositoryService.getBpmnModel(m.getProcessDefinitionId());
            FlowNode flowNode = (FlowNode)bpmnModel.getFlowElement(m.getTaskDefinitionKey());
            SequenceFlow sequenceFlow = flowNode.getIncomingFlows().get(0);

            dto.setFirstNode(sequenceFlow.getSourceRef().indexOf("startEvent")>=0);

            // 流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(m.getProcessInstanceId())
                    .singleResult();
            dto.setProcVars(m.getProcessVariables());
            dto.setStartUserId(historicProcessInstance.getStartUserId());
            dto.setCategory(m.getCategory());
            dto.setFlowKey(historicProcessInstance.getProcessDefinitionKey());
            dto.setFlowName(historicProcessInstance.getProcessDefinitionName());
            dto.setStartTime(historicProcessInstance.getStartTime());
            return dto;
        }).collect(Collectors.toList());
        // 补充流程发起人姓名，工号，部门信息
        List<User> userList = userService.queryByIds(data.stream().map(FlowTaskDto::getStartUserId).collect(Collectors.toList()));
        return data.stream().map(m->{
            User u = userList.stream().filter(item->item.getId().equals(m.getStartUserId())).findFirst().orElse(null);
            if(u!=null){
                m.setStartUsername(u.getUsername());
                m.setStartUserXm(u.getTruename());
                m.setStartUserDept(u.getDeptName());
            }
            return m;
        }).collect(Collectors.toList());
    }
    public List<FlowTaskDto> queryTodoList(ShiroUser user) {
        TaskQuery taskQuery = taskService.createTaskQuery().active().includeProcessVariables().taskCandidateOrAssigned(user.getUserId());
        if(CollectionUtils.isNotEmpty(user.getFlowGroups())) {
            List<String> groups = user.getFlowGroups().stream().map(m->m.getName()).collect(Collectors.toList());
            taskQuery.taskCandidateGroupIn(groups);
        }
        taskQuery.orderByTaskCreateTime().desc();
        List<Task> taskList = taskQuery.list();
        if(CollectionUtils.isNotEmpty(taskList)){
            return convertFlowTask(taskList);
        }
        return null;
    }
    @Override
    public IPage<FlowTaskDto> queryDoneList(ShiroUser user, FlowQueryVo queryVo) {
        Page<FlowTaskDto> page = new Page(queryVo.getCurrent(), queryVo.getSize());
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables().finished()
                .taskAssignee(user.getUserId())
                .orderByHistoricTaskInstanceEndTime().desc();

        if(StringUtils.isNotEmpty(queryVo.getStartTime())){
            taskInstanceQuery.taskCompletedAfter(DateUtils.convert(queryVo.getStartTime().trim()));
        }
        List<HistoricTaskInstance> historicTaskInstances = taskInstanceQuery.listPage(queryVo.getSize() * (queryVo.getCurrent() - 1), queryVo.getSize());
        page.setTotal(taskInstanceQuery.count());
        if(CollectionUtils.isNotEmpty(historicTaskInstances)){
            List<FlowTaskDto> data = historicTaskInstances.stream().map(m->{
                FlowTaskDto dto = new FlowTaskDto();
                dto.setTaskId(m.getId());
                dto.setCreateTime(m.getCreateTime());
                dto.setFinishTime(m.getEndTime());
                dto.setDuration(getDate(m.getDurationInMillis()));
                dto.setProcDefId(m.getProcessDefinitionId());

                dto.setTaskName(m.getName());
                dto.setTaskDefKey(m.getTaskDefinitionKey());
                dto.setProcInsId(m.getProcessInstanceId());
                dto.setProcVars(m.getProcessVariables());
                dto.setExecutionId(m.getExecutionId());
                // 流程发起人信息
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(m.getProcessInstanceId())
                        .singleResult();
                dto.setStartUserId(historicProcessInstance.getStartUserId());
                dto.setFlowKey(historicProcessInstance.getProcessDefinitionKey());
                dto.setFlowName(historicProcessInstance.getProcessDefinitionName());
                dto.setStartTime(historicProcessInstance.getStartTime());
                // 当前所处流程
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(m.getProcessInstanceId()).list();
                if (CollectionUtils.isNotEmpty(taskList)) {
                    dto.setTaskId(taskList.get(0).getId());
                    dto.setTaskName(taskList.get(0).getName());
                    dto.setAssigneeId(taskList.get(0).getAssignee());
                } else {
                    List<HistoricActivityInstance> historicActivityInstances =historyService.createHistoricActivityInstanceQuery().processInstanceId(m.getProcessInstanceId()).orderByHistoricActivityInstanceEndTime().desc().list();
                    dto.setTaskId(historicActivityInstances.get(0).getId());
                    dto.setTaskName(historicActivityInstances.get(0).getActivityName());
                    dto.setAssigneeId(historicActivityInstances.get(0).getAssignee());
                }
                return dto;
            }).collect(Collectors.toList());
            // 补充流程发起人姓名，工号，部门信息
            List<User> userList = userService.queryByIds(data.stream().map(FlowTaskDto::getStartUserId).collect(Collectors.toList()));
            page.setRecords(data.stream().map(m->{
                User u = userList.stream().filter(item->item.getId().equals(m.getStartUserId())).findFirst().orElse(null);
                if(u!=null){
                    m.setStartUsername(u.getUsername());
                    m.setStartUserXm(u.getTruename());
                    m.setStartUserDept(u.getDeptName());
                }
                return m;
            }).collect(Collectors.toList()));
        }
        return page;
    }

    public IPage<FlowTaskDto> queryDonePage(FlowQueryVo queryVo) {
        Page<FlowTaskDto> page = new Page(queryVo.getCurrent(), queryVo.getSize());
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables().finished()
                .taskAssignee(SecurityContextHolder.getCurrentUser().getUserId())
                .orderByHistoricTaskInstanceEndTime().desc();
        if(CollectionUtils.isNotEmpty(queryVo.getFlowKey())){
            taskInstanceQuery.processDefinitionKeyIn(queryVo.getFlowKey());
        }
        if(StringUtils.isNotEmpty(queryVo.getStartTime())){
            taskInstanceQuery.taskCompletedAfter(DateUtils.convert(queryVo.getStartTime().trim()));
        }
        List<HistoricTaskInstance> historicTaskInstances = taskInstanceQuery.listPage(queryVo.getSize() * (queryVo.getCurrent() - 1), queryVo.getSize());
        page.setTotal(taskInstanceQuery.count());
        if(CollectionUtils.isNotEmpty(historicTaskInstances)){
            List<FlowTaskDto> data = historicTaskInstances.stream().map(m->{
                FlowTaskDto dto = new FlowTaskDto();
                dto.setTaskId(m.getId());
                dto.setCreateTime(m.getCreateTime());
                dto.setFinishTime(m.getEndTime());
                dto.setDuration(getDate(m.getDurationInMillis()));
                dto.setProcDefId(m.getProcessDefinitionId());

                dto.setTaskName(m.getName());
                dto.setTaskDefKey(m.getTaskDefinitionKey());
                dto.setProcInsId(m.getProcessInstanceId());
                dto.setProcVars(m.getProcessVariables());
                dto.setExecutionId(m.getExecutionId());
                // 流程发起人信息
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(m.getProcessInstanceId())
                        .singleResult();
                dto.setStartUserId(historicProcessInstance.getStartUserId());
                dto.setFlowKey(historicProcessInstance.getProcessDefinitionKey());
                dto.setFlowName(historicProcessInstance.getProcessDefinitionName());
                dto.setStartTime(historicProcessInstance.getStartTime());
                // 当前所处流程
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(m.getProcessInstanceId()).list();
                if (CollectionUtils.isNotEmpty(taskList)) {
                    dto.setTaskId(taskList.get(0).getId());
                    dto.setTaskName(taskList.get(0).getName());
                    dto.setAssigneeId(taskList.get(0).getAssignee());
                } else {
                    List<HistoricActivityInstance> historicActivityInstances =historyService.createHistoricActivityInstanceQuery().processInstanceId(m.getProcessInstanceId()).orderByHistoricActivityInstanceEndTime().desc().list();
                    dto.setTaskId(historicActivityInstances.get(0).getId());
                    dto.setTaskName(historicActivityInstances.get(0).getActivityName());
                    dto.setAssigneeId(historicActivityInstances.get(0).getAssignee());
                }
                return dto;
            }).collect(Collectors.toList());
            // 补充流程发起人姓名，工号，部门信息
            List<User> userList = userService.queryByIds(data.stream().map(FlowTaskDto::getStartUserId).collect(Collectors.toList()));
            page.setRecords(data.stream().map(m->{
                User u = userList.stream().filter(item->item.getId().equals(m.getStartUserId())).findFirst().orElse(null);
                if(u!=null){
                    m.setStartUsername(u.getUsername());
                    m.setStartUserXm(u.getTruename());
                    m.setStartUserDept(u.getDeptName());
                }
                return m;
            }).collect(Collectors.toList()));
        }
        return page;
    }

    @Override
    public List<FlowTaskDto> queryCcList(FlowQueryVo queryVo, List<String> taskIds) {
        TaskQuery taskQuery = taskService.createTaskQuery().active().includeProcessVariables().orderByTaskCreateTime().desc();
        List<Task> taskList = taskQuery.list();
        if(CollectionUtils.isNotEmpty(taskList)){
            return convertFlowTask(taskList.stream().filter(m->taskIds.contains(m.getId())).collect(Collectors.toList()));
        }
        return null;
    }
    public IPage<FlowTaskDto> queryCcPage(FlowQueryVo queryVo, List<String> taskIds) {
        Page<FlowTaskDto> page = new Page(queryVo.getCurrent(), queryVo.getSize());
        TaskQuery taskQuery = taskService.createTaskQuery().active().includeProcessVariables().orderByTaskCreateTime().desc();
        if(CollectionUtils.isNotEmpty(taskIds)){
            if(CollectionUtils.isNotEmpty(queryVo.getFlowKey())){
                taskQuery.processDefinitionKeyIn(queryVo.getFlowKey());
            }
//            taskQuery.taskVariableValueEqualsIgnoreCase()
            for(String taskId:taskIds){

            }
            List<Task> taskList = taskQuery.listPage(queryVo.getSize() * (queryVo.getCurrent() - 1), queryVo.getSize());
        }


        return page;
    }

    @Override
    public void complete(FlowTaskVo taskVo) {
        Task task = taskService.createTaskQuery().taskId(taskVo.getTaskId()).singleResult();
        if(task == null){
            // 任务不存在
            throw new WorkflowException("任务不存在");
        }
        Map<String, Object> variables = taskService.getVariables(task.getId());
        if(StringUtils.isNotEmpty(taskVo.getStatus()) && taskVo.getStatus().equals("end")){
            variables.put("projectStatus", taskVo.getStatus());
            taskService.addComment(taskVo.getTaskId(), task.getProcessInstanceId(), COMMENT_STATUS, "end");
        }
        else {
            taskService.addComment(taskVo.getTaskId(), task.getProcessInstanceId(), COMMENT_STATUS, "success");
        }

        taskService.addComment(taskVo.getTaskId(), task.getProcessInstanceId(), COMMENT_MESSAGE, taskVo.getComment());
        taskService.setAssignee(taskVo.getTaskId(), SecurityContextHolder.getCurrentUser().getUserId());

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        Process mainProcess = bpmnModel.getMainProcess();
        UserTask currentUserTask = (UserTask)mainProcess.getFlowElement(task.getTaskDefinitionKey(), true);

        MultiInstanceLoopCharacteristics multiInstance = currentUserTask.getLoopCharacteristics();

        boolean flag = false;
        // 会签节点
        if (Objects.nonNull(multiInstance)) {
            long count = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).taskDefinitionKey(task.getTaskDefinitionKey()).count();
            Integer rejectCount = (Integer) variables.get(task.getName()+"_rejected_count");
            Integer approveCount = (Integer) variables.get(task.getName()+"_approved_count");
            if(approveCount==null){
                approveCount=1;
            }
            else {
                approveCount++;
            }
            if(rejectCount==null){
                rejectCount = 0;
            }
            variables.put(task.getName()+"_approved_count", approveCount);
            variables.put(task.getName()+"_rejected_count", rejectCount);
            if(count==1){
                if(rejectCount<=0){
                    flag = true;
                }
            }
            else {
                flag = true;
            }
        }
        else {
            flag = true;
        }
        if(flag) {
            taskService.complete(taskVo.getTaskId(), variables);
        }
        else {
            variables.put(task.getName()+"_rejected_count", 0);
            variables.put(task.getName()+"_approved_count", 0);
            taskService.setVariables(task.getId(),variables);
            rejectFirstNode(task, mainProcess, currentUserTask);
        }
    }

    @Override
    public void reject(FlowTaskVo taskVo) {
        Task task = taskService.createTaskQuery().taskId(taskVo.getTaskId()).singleResult();
        if(task == null){
            // 任务不存在
            throw new WorkflowException("任务不存在");
        }
        taskService.addComment(taskVo.getTaskId(), task.getProcessInstanceId(), COMMENT_STATUS, "reject");
        taskService.addComment(taskVo.getTaskId(), task.getProcessInstanceId(), COMMENT_MESSAGE, taskVo.getComment());
        taskService.setAssignee(taskVo.getTaskId(), SecurityContextHolder.getCurrentUser().getUserId());

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        Process mainProcess = bpmnModel.getMainProcess();
        UserTask currentUserTask = (UserTask)mainProcess.getFlowElement(task.getTaskDefinitionKey(), true);

        MultiInstanceLoopCharacteristics multiInstance = currentUserTask.getLoopCharacteristics();
        // 会签节点
        if (Objects.nonNull(multiInstance)) {
            Map<String, Object> variables = taskService.getVariables(task.getId());
            String rejectKey = task.getName()+"_rejected_count";
            Integer rejectCount = (Integer) variables.get(rejectKey);
            if(rejectCount==null){
                rejectCount=1;
            }
            else {
                rejectCount++;
            }
            long count = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).taskDefinitionKey(task.getTaskDefinitionKey()).count();
            if(count>1){
                variables.put(rejectKey, rejectCount);
                taskService.complete(taskVo.getTaskId(), variables);
            }
            else {
                variables.put(rejectKey, 0);
                variables.put(task.getName()+"_approved_count", 0);
                taskService.setVariables(task.getId(),variables);
                rejectFirstNode(task, mainProcess, currentUserTask);
            }
        }
        else {
            rejectFirstNode(task, mainProcess, currentUserTask);
        }

    }

    private void rejectFirstNode(Task task, Process mainProcess, UserTask currentUserTask){
        // 查询历史节点实例
        List<HistoricActivityInstance> activityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .finished()
                .orderByHistoricActivityInstanceEndTime().asc().list();
        List<String> activityIdList = activityInstanceList.stream()
                .filter(activityInstance ->
                        BpmnXMLConstants.ELEMENT_TASK_USER.equals(activityInstance.getActivityType()))
                .map(HistoricActivityInstance::getActivityId)
                .filter(activityId -> !task.getTaskDefinitionKey().equals(activityId))
                .distinct()
                .collect(Collectors.toList());
        List<String> rejectActivityIds = new ArrayList<>();
        for (String activityId : activityIdList) {
            // 回退到主流程的节点
            FlowNode toBackFlowElement = (FlowNode) mainProcess.getFlowElement(activityId, true);
            // 判断 【工具类判断是否可以从源节点 到 目标节点】
            Set<String> set = new HashSet<>();
            if (toBackFlowElement != null && ExecutionGraphUtil.isReachable(mainProcess,toBackFlowElement, currentUserTask, set)) {
                rejectActivityIds.add(activityId);
            }
        }
        if(CollectionUtils.isNotEmpty(rejectActivityIds)){
            runtimeService.createChangeActivityStateBuilder().processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdTo(task.getTaskDefinitionKey(), rejectActivityIds.get(0)).changeState();
        }
    }

    @Override
    public Task queryByInstance(String instanceId) {
        List<Task> list = taskService.createTaskQuery().processInstanceId(instanceId).list();
        if(CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public String queryCurrentStep(String instanceId) {
        Task task = queryByInstance(instanceId);
        if(task!=null){
            return task.getName();
        }
        List<HistoricActivityInstance> historicActivityInstances =historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId).orderByHistoricActivityInstanceEndTime().desc().list().stream().filter(hiActInst-> !"sequenceFlow".equals(hiActInst.getActivityType())).collect(Collectors.toList());
        return historicActivityInstances.get(0).getActivityType();
    }

    @Override
    public FlowTaskDto queryTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            FlowTaskDto flowTaskDto = new FlowTaskDto();
            flowTaskDto.setTaskId(task.getId());
            flowTaskDto.setTaskName(task.getName());
            flowTaskDto.setTaskDefKey(task.getTaskDefinitionKey());
            flowTaskDto.setProcDefId(task.getProcessDefinitionId());
            flowTaskDto.setProcInsId(task.getProcessInstanceId());
            flowTaskDto.setCreateTime(task.getCreateTime());
            flowTaskDto.setExecutionId(task.getExecutionId());
            flowTaskDto.setCategory(task.getCategory());
            Map<String, Object> variables = taskService.getVariables(task.getId());
            flowTaskDto.setProcVars(variables);

            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            FlowNode flowNode = (FlowNode)bpmnModel.getFlowElement(task.getTaskDefinitionKey());
            SequenceFlow sequenceFlow = flowNode.getIncomingFlows().get(0);

            flowTaskDto.setFirstNode(sequenceFlow.getSourceRef().indexOf("startEvent")>=0);

            List<UserTask> nextUserTask = FlowableUtils.getNextUserTasks(repositoryService, task, variables);
            if (CollectionUtils.isNotEmpty(nextUserTask)) {
                List<FlowNextDto> nextList = new ArrayList<>();
                for (UserTask userTask : nextUserTask) {
                    FlowNextDto flowNextDto = new FlowNextDto();
                    flowNextDto.setName(userTask.getName());
                    flowNextDto.setId(userTask.getId());
                    MultiInstanceLoopCharacteristics multiInstance = userTask.getLoopCharacteristics();
                    // 会签节点
                    if (Objects.nonNull(multiInstance)) {
                        flowNextDto.setType(FlowConstant.PROCESS_MULTI_INSTANCE);
                    }
                    nextList.add(flowNextDto);
                }
                flowTaskDto.setNextNodes(nextList);

            }
            return flowTaskDto;
        }
        return null;
    }

    public List<FlowHistoryItemDto> queryHistory(String procInsId){
        List<HistoricActivityInstance> list = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId).finished()
                .orderByHistoricActivityInstanceEndTime()
//                .orderByHistoricActivityInstanceStartTime()
                .asc().list();
        List<FlowHistoryItemDto> hisFlowList = new ArrayList<>();
        for (HistoricActivityInstance histIns : list) {
            if (StringUtils.isNotBlank(histIns.getTaskId())) {
                FlowHistoryItemDto historyDto = new FlowHistoryItemDto();
                historyDto.setActivityId(histIns.getActivityId());
                historyDto.setActivityName(histIns.getActivityName());
                historyDto.setActivityType(histIns.getActivityType());
                historyDto.setExecutionId(histIns.getExecutionId());
                historyDto.setTaskId(histIns.getTaskId());
                historyDto.setStartTime(histIns.getStartTime());
                historyDto.setEndTime(histIns.getEndTime());

                if (StringUtils.isNotBlank(histIns.getAssignee())) {
                    historyDto.setAssignee(histIns.getAssignee());
                    User user = userService.findById(histIns.getAssignee());
                    historyDto.setAssigneeName(user.getTruename());
                    historyDto.setAssigneeUsername(user.getUsername());
                    historyDto.setAssigneeDeptName(user.getDeptName());
                }
                // 展示审批人员
//                List<HistoricIdentityLink> linksForTask = historyService.getHistoricIdentityLinksForTask(histIns.getTaskId());
//                StringBuilder stringBuilder = new StringBuilder();
//                for (HistoricIdentityLink identityLink : linksForTask) {
//                    // 获选人,候选组/角色(多个)
//                    if ("candidate".equals(identityLink.getType())) {
//                        if (StringUtils.isNotBlank(identityLink.getUserId())) {
//                            User user = userService.queryById(identityLink.getUserId());
//                            stringBuilder.append(user.getTruename()).append(",");
//                        }
//                        if (StringUtils.isNotBlank(identityLink.getGroupId())) {
//                            Role role = roleService.getById(identityLink.getGroupId());
//                            stringBuilder.append(role.getName()).append(",");
//                        }
//                    }
//                }
//                if (StringUtils.isNotBlank(stringBuilder)) {
//                    flowTask.setCandidate(stringBuilder.substring(0, stringBuilder.length() - 1));
//                }

//                flowTask.setDuration(histIns.getDurationInMillis() == null || histIns.getDurationInMillis() == 0 ? null : getDate(histIns.getDurationInMillis()));
                // 获取意见评论内容
                List<Comment> commentList = taskService.getTaskComments(historyDto.getTaskId());
                List<Comment> statusComment = taskService.getTaskComments(historyDto.getTaskId(), COMMENT_STATUS);
                commentList.forEach(comment -> {
                    if (histIns.getTaskId().equals(comment.getTaskId())) {
                        historyDto.setComment(FlowCommentDto.builder().type(comment.getType()).comment(comment.getFullMessage()).build());
                    }
                });
                statusComment.forEach(comment -> {
                    if (histIns.getTaskId().equals(comment.getTaskId()) && COMMENT_STATUS.equals(comment.getType())) {
                        historyDto.setStatus(FlowCommentDto.builder().type(comment.getType()).comment(comment.getFullMessage()).build());
                    }
                });
                hisFlowList.add(historyDto);
            }
        }

//        if(CollectionUtils.isNotEmpty(hisFlowList)){
//            List<String> activityIds = hisFlowList.stream().map(FlowHistoryItemDto::getExecutionId).distinct().collect(Collectors.toList());
//            List<FlowHistoryDto> historyDtoList = new ArrayList<>();
//
//            for (String activityId : activityIds) {
//                FlowHistoryDto flowHistoryDto = new FlowHistoryDto();
//                flowHistoryDto.setActivityId(activityId);
//                flowHistoryDto.setList(hisFlowList.stream().filter(m->m.getExecutionId().equals(activityId)).collect(Collectors.toList()));
//                flowHistoryDto.setActivityName(flowHistoryDto.getList().get(0).getActivityName());
//                historyDtoList.add(flowHistoryDto);
//            }
//            return historyDtoList;
//        }

        return hisFlowList;
    }

    @Override
    public InputStream diagram(String processId) {
        String processDefinitionId;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        // 如果流程已经结束，则得到结束节点
        if (Objects.isNull(processInstance)) {
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        } else {// 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        }

        // 获得活动的节点
        List<HistoricActivityInstance> highLightedFlowList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processId).orderByHistoricActivityInstanceEndTime().asc().list();

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        List<String> highLightedFlows = new ArrayList<>();
        List<String> highLightedNodes = new ArrayList<>();

//        for (HistoricActivityInstance tempActivity : highLightedFlowList) {
//            if ("sequenceFlow".equals(tempActivity.getActivityType())) {
//                //高亮线
//                highLightedFlows.add(tempActivity.getActivityId());
//            } else {
//                //高亮节点
//                highLightedNodes.add(tempActivity.getActivityId());
//            }
//        }
        String firstActivityId = highLightedFlowList.get(2).getActivityId();
        int startIndex = 0;
        for(int i=0; i<highLightedFlowList.size(); i++) {
            HistoricActivityInstance tempActivity = highLightedFlowList.get(i);
            if(tempActivity.getActivityId().equals(firstActivityId)){
                startIndex = i;
            }
        }
        System.out.println(startIndex);

        for(int i=0; i<highLightedFlowList.size(); i++){
            HistoricActivityInstance tempActivity = highLightedFlowList.get(i);
            if(i<2){
                if ("sequenceFlow".equals(tempActivity.getActivityType())) {
                    //高亮线
                    highLightedFlows.add(tempActivity.getActivityId());
                } else {
                    //高亮节点
                    highLightedNodes.add(tempActivity.getActivityId());
                }
            }
            else {
                if(i>=startIndex){
                    if ("sequenceFlow".equals(tempActivity.getActivityType())) {
                        //高亮线
                        highLightedFlows.add(tempActivity.getActivityId());
                    } else {
                        //高亮节点
                        highLightedNodes.add(tempActivity.getActivityId());
                    }
                }
            }
        }

        ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();

        //获取自定义图片生成器
        ProcessDiagramGenerator diagramGenerator = new CustomProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedNodes, highLightedFlows, configuration.getActivityFontName(),
                configuration.getLabelFontName(), configuration.getAnnotationFontName(), configuration.getClassLoader(), 1.0, true);
        return in;
    }

    public InputStream diagramByDef(String processDefinitionId) {
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        List<String> highLightedFlows = new ArrayList<>();
        List<String> highLightedNodes = new ArrayList<>();
        ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();

        //获取自定义图片生成器
        ProcessDiagramGenerator diagramGenerator = new CustomProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedNodes, highLightedFlows, configuration.getActivityFontName(),
                configuration.getLabelFontName(), configuration.getAnnotationFontName(), configuration.getClassLoader(), 1.0, true);
        return in;
    }

    private List<FlowActivityFlowVo> getAssigneeName(UserTask task, Map<String, Object> map){
        List<FlowActivityFlowVo> flows = new ArrayList<>();
        if(StringUtils.isNotEmpty(task.getAssignee())){
            if(task.getAssignee().startsWith("$")){
                if(Objects.nonNull(task.getLoopCharacteristics())){
                    String variable = task.getLoopCharacteristics().getInputDataItem();
                    variable=variable.replace("${", "").replace("}","");
                    List<String> userIds = (List<String>) map.get(variable);
                    if(CollectionUtils.isNotEmpty(userIds)){
                        List<User> user = userService.queryByIds(userIds);
                        flows.addAll(user.stream().map(m->{
                            FlowActivityFlowVo flow = new FlowActivityFlowVo();
                            flow.setAssigneeName(m.getTruename());
                            return flow;
                        }).collect(Collectors.toList()));
                    }
                }
                else {
                    String variable = task.getAssignee().substring(2, task.getAssignee().length()-1);
                    String userId = (String) map.get(variable);
                    if(StringUtils.isNotEmpty(userId)){
                        User user = userService.findById(userId);
                        FlowActivityFlowVo flow = new FlowActivityFlowVo();
                        flow.setAssigneeName(user.getTruename());
                        flows.add(flow);
                    }
                }
            }
            else {
                User user = userService.findById(task.getAssignee());
                FlowActivityFlowVo flow = new FlowActivityFlowVo();
                flow.setAssigneeName(user.getTruename());
                flows.add(flow);
            }
        }
        else if(CollectionUtils.isNotEmpty(task.getCandidateGroups())) {
            List<User> users = userService.queryFlowGroupUser(task.getCandidateGroups());
            flows.addAll(users.stream().map(m->{
                FlowActivityFlowVo flow = new FlowActivityFlowVo();
                flow.setAssigneeName(m.getTruename());
                return flow;
            }).collect(Collectors.toList()));
        }
        return flows;
    }
    @Override
    public List<FlowActivityVo> queryTaskList(String processId) {
        String processDefinitionId;
        Map<String, Object> processVariables;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().includeProcessVariables().processInstanceId(processId).singleResult();
        // 如果流程已经结束，则得到结束节点
        if (Objects.isNull(processInstance)) {
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().includeProcessVariables().processInstanceId(processId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
            processVariables = pi.getProcessVariables();
        } else {// 如果流程没有结束，则取当前活动节点
            processDefinitionId = processInstance.getProcessDefinitionId();
            processVariables = processInstance.getProcessVariables();
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        List<Process> processes = bpmnModel.getProcesses();
        List<UserTask> datas = new ArrayList<>();
        processes.forEach(process -> {
            List<UserTask> userTasks = process.findFlowElementsOfType(UserTask.class);
            datas.addAll(userTasks);
        });
        List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processId).list();
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processId).list();
        List<FlowActivityVo> list = new ArrayList<>();
        datas.forEach(userTask -> {
            FlowActivityVo vo = new FlowActivityVo();
            GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(userTask.getId());
            vo.setName(userTask.getName());
            vo.setX(graphicInfo.getX());
            vo.setY(graphicInfo.getY());
            vo.setWidth(graphicInfo.getWidth());
            vo.setHeight(graphicInfo.getHeight());
            List<FlowActivityFlowVo> flows = new ArrayList<>();
            taskInstances.forEach(t->{
                if(StringUtils.equals(userTask.getId(), t.getTaskDefinitionKey())){
                    FlowActivityFlowVo flow = new FlowActivityFlowVo();
                    flow.setStatus("已结束");
                    taskList.forEach(runtimeTask -> {
                        if(StringUtils.equals(runtimeTask.getId(), t.getId())){
                            flow.setStatus(runtimeTask.isSuspended()?"挂起":"处理中");
                            if(CollectionUtils.isNotEmpty(userTask.getCandidateGroups())) {
                                List<User> users = userService.queryFlowGroupUser(userTask.getCandidateGroups());
                                flow.setAssigneeName(users.stream().map(User::getTruename).collect(Collectors.joining(", ")));
                            }
                        }
                    });
                    flow.setCreateTime(t.getCreateTime());
                    flow.setEndTime(t.getEndTime());
                    if(StringUtils.isNotEmpty(t.getAssignee())){
                        User user = userService.findById(t.getAssignee());
                        flow.setAssigneeName(user.getTruename());
                    }
                    flows.add(flow);
                }
            });
            if(CollectionUtils.isEmpty(flows)){
                flows.addAll(getAssigneeName(userTask, processVariables));
            }
            vo.setFlows(flows);
            list.add(vo);
        });
        return list;
    }

    public String readXml(String key) throws IOException {
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
        InputStream inputStream = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
    }

    public String generateXML(String key){
        FileOutputStream fos = null;
        try {
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
            InputStream inputStream = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
            String xml = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
            String md5 = Identities.getUUID8();
            String path = "/flow/"+md5+"/";
            File file = VFSUtil.getFileIsNotExistCreate(path);
            byte bytes[] = new byte[512];
            bytes = xml.getBytes();
            int b = bytes.length;
            fos = new FileOutputStream(file.getAbsolutePath()+File.separator+key+".bpmn20.xml");
            fos.write(bytes, 0, b);
            fos.write(bytes);
            return path+key+".bpmn20.xml";
        } catch (IOException e){
            e.printStackTrace();
            return null;
        } finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Map<String,Object> queryVars(String procInsId){
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).includeProcessVariables().singleResult();
        if (Objects.isNull(processInstance)) {
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInsId).includeProcessVariables().singleResult();
            return pi.getProcessVariables();
        } else {// 如果流程没有结束，则取当前活动节点
           return processInstance.getProcessVariables();
        }
    }

    @Override
    public void end(FlowTaskVo taskVo) {
        Task task = taskService.createTaskQuery().taskId(taskVo.getTaskId()).singleResult();
        if(task == null){
            // 任务不存在
            throw new WorkflowException("任务不存在");
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        Process mainProcess = bpmnModel.getMainProcess();
        List<EndEvent> endNodes = mainProcess.findFlowElementsOfType(EndEvent.class, false);
        if (CollectionUtils.isNotEmpty(endNodes)) {
            String endId = endNodes.get(0).getId();
            List<Execution> executions = runtimeService.createExecutionQuery()
                    .parentId(taskVo.getInstanceId()).list();
            List<String> executionIds = new ArrayList<>();
            executions.forEach(execution -> executionIds.add(execution.getId()));
            // 变更流程为已结束状态
            runtimeService.createChangeActivityStateBuilder()
                    .moveExecutionsToSingleActivityId(executionIds, endId).changeState();
        }
    }

    /**
     * 流程完成时间处理
     *
     * @param ms
     * @return
     */
    private String getDate(long ms) {

        long day = ms / (24 * 60 * 60 * 1000);
        long hour = (ms / (60 * 60 * 1000) - day * 24);
        long minute = ((ms / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (ms / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        if (day > 0) {
            return day + "天" + hour + "小时" + minute + "分钟";
        }
        if (hour > 0) {
            return hour + "小时" + minute + "分钟";
        }
        if (minute > 0) {
            return minute + "分钟";
        }
        if (second > 0) {
            return second + "秒";
        } else {
            return 0 + "秒";
        }
    }
}
