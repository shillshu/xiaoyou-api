package cn.sibetech.core.service.impl;

import cn.sibetech.core.domain.dto.FlowDefDto;
import cn.sibetech.core.service.FlowDefinitionService;
import cn.sibetech.core.mapper.FlowDeployMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * @author liwj
 * @create 2023/9/20
 */
@Service
public class FlowDefinitionServiceImpl implements FlowDefinitionService {

    @Resource
    private FlowDeployMapper flowDeployMapper;
    @Resource
    protected RepositoryService repositoryService;
    @Override
    public void importFile(String name, InputStream is) {
        Deployment deployment = repositoryService.createDeployment().addInputStream(name+".bpmn20.xml", is).name(name).deploy();
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
    }

    @Override
    public IPage<FlowDefDto> query(String name, String flowKey, int current, int size) {
        Page<FlowDefDto> page = new Page<>(current, size);
        return flowDeployMapper.queryDeployList(page, name, flowKey);
    }

    @Override
    public void updateState(Integer state, String deployId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
        if(state == 1){
            repositoryService.activateProcessDefinitionById(processDefinition.getId(), true, null);
        }
        else if(state == 2){
            repositoryService.suspendProcessDefinitionById(processDefinition.getId(), true, null);
        }
    }

    @Override
    public void delete(String deployId) {
        repositoryService.deleteDeployment(deployId, true);
    }

}
