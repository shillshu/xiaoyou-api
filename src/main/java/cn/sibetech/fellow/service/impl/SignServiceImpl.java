package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.Sign;
import cn.sibetech.fellow.domain.dto.SignQueryDto;
import cn.sibetech.fellow.mapper.SignMapper;
import cn.sibetech.fellow.service.SignService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign> implements SignService {
    @Override
    public IPage<Sign> queryPage(SignQueryDto dto) {
        Page<Fellow> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public void editStatus(String ids, String status){
        Sign sign = new Sign();
        sign.setId(ids);
        sign.setStatus(status);
        this.updateById(sign);
    }
}

