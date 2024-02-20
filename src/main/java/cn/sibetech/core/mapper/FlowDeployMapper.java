package cn.sibetech.core.mapper;

import cn.sibetech.core.domain.dto.FlowDefDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author liwj
 * @create 2023/9/20
 */
public interface FlowDeployMapper {

    IPage<FlowDefDto> queryDeployList(Page<FlowDefDto> page, @Param("name") String name,@Param("flowKey") String flowKey);
}
