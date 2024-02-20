package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.Sign;
import cn.sibetech.fellow.domain.dto.SignQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


public interface SignMapper extends BaseMapper<Sign> {
    public IPage<Sign> queryPage(Page<Fellow> page, SignQueryDto dto);
}
