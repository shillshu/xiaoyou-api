package cn.sibetech.core.service;

import cn.sibetech.core.domain.dto.FlowDefDto;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.InputStream;

/**
 * @author liwj
 * @create 2023/9/20
 */
public interface FlowDefinitionService {
    void importFile(String name, InputStream is);

    IPage<FlowDefDto> query(String name, String flowKey, int current, int size);

    /**
     * 激活或挂起
     * @param state
     * @param deployId
     */
    void updateState(Integer state, String deployId);

    /**
     * 删除
     * @param deployId
     */
    void delete(String deployId);
}
