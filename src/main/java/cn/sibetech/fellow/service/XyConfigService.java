package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.XyConfig;
import cn.sibetech.fellow.domain.dto.XyConfigQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface XyConfigService extends IService<XyConfig> {
    IPage<XyConfig> queryPage(XyConfigQueryDto dto);

    void add(XyConfig config);

    void edit(XyConfig config);

    public List<XyConfig> listAll();

    String findByKey(String key, String defaultValue);
}
