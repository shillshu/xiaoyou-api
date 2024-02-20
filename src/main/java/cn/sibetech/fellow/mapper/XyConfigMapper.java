package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.XyConfig;
import cn.sibetech.fellow.domain.dto.XyConfigQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface XyConfigMapper extends BaseMapper<XyConfig> {
    IPage<XyConfig> queryPage(Page<XyConfig> page, @Param("config") XyConfigQueryDto config);
}
