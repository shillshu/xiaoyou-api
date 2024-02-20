package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.XyConfig;
import cn.sibetech.fellow.domain.dto.XyConfigQueryDto;
import cn.sibetech.fellow.mapper.XyConfigMapper;
import cn.sibetech.fellow.service.XyConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class XyConfigServiceImpl extends ServiceImpl<XyConfigMapper, XyConfig> implements XyConfigService {
    @Override
    public IPage<XyConfig> queryPage(XyConfigQueryDto dto) {
        Page<XyConfig> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public void add(XyConfig config) {
        baseMapper.insert(config);
    }

    @Override
    public void edit(XyConfig config) {
        baseMapper.updateById(config);
    }

    @Override
    public List<XyConfig> listAll(){
        QueryWrapper<XyConfig> wrapper = new QueryWrapper<>();
        return baseMapper.selectList(wrapper);
    }

    @Override
    public String findByKey(String key, String defaultValue){
        QueryWrapper<XyConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("key", key);
        List<XyConfig> list = baseMapper.selectList(wrapper);
        if(CollectionUtils.isNotEmpty(list)){
            XyConfig config = list.get(0);
            return StringUtils.isNotEmpty(config.getValue()) ? config.getValue() : defaultValue;
        }else{
            return defaultValue;
        }
    }

}

