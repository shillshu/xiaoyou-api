package cn.sibetech.core.service;

import cn.sibetech.core.domain.ShiroUser;
import cn.sibetech.core.domain.dto.FlowHistoryItemDto;
import cn.sibetech.core.domain.dto.FlowTaskDto;
import cn.sibetech.core.domain.vo.FlowActivityVo;
import cn.sibetech.core.domain.vo.FlowQueryVo;
import cn.sibetech.core.domain.vo.FlowTaskVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.flowable.task.api.Task;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface FlowTaskService {

    /**
     * 查询我发起的
     * @param queryVo
     * @return
     */
    IPage<FlowTaskDto> queryMyStartedList(FlowQueryVo queryVo);
    long queryTodoCount();
    /**
     * 查询待办
     * @param queryVo
     * @return
     */
    IPage<FlowTaskDto> queryTodoPage(FlowQueryVo queryVo);
    List<FlowTaskDto> queryTodoList(ShiroUser user);
    /**
     * 查询已办
     * @param queryVo
     * @return
     */
    IPage<FlowTaskDto> queryDoneList(ShiroUser user, FlowQueryVo queryVo);
    IPage<FlowTaskDto> queryDonePage(FlowQueryVo queryVo);
    List<FlowTaskDto> queryCcList(FlowQueryVo queryVo, List<String> taskIds);
    IPage<FlowTaskDto> queryCcPage(FlowQueryVo queryVo, List<String> taskIds);
    void complete(FlowTaskVo taskVo);

    /**
     * 驳回
     * @param taskVo
     */
    void reject(FlowTaskVo taskVo);

    Task queryByInstance(String instanceId);

    String queryCurrentStep(String instanceId);
    /**
     * 查看详情
     * @param taskId
     */
    FlowTaskDto queryTask(String taskId);
    List<FlowHistoryItemDto> queryHistory(String procInsId);

    InputStream diagram(String processId);
    InputStream diagramByDef(String processDefinitionId);
    List<FlowActivityVo> queryTaskList(String processId);

    String readXml(String key) throws IOException;
    String generateXML(String key);
    Map<String,Object> queryVars(String procInsId);

    void end(FlowTaskVo vo);
}
