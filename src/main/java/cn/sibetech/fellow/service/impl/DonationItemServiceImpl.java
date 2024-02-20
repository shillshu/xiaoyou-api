package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.DonationItem;
import cn.sibetech.fellow.domain.dto.DonationItemQueryDto;
import cn.sibetech.fellow.mapper.DonationItemMapper;
import cn.sibetech.fellow.service.DonationItemService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DonationItemServiceImpl extends ServiceImpl<DonationItemMapper, DonationItem> implements DonationItemService {
    @Override
    public IPage<DonationItem> queryPage(DonationItemQueryDto dto) {
        Page<DonationItem> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public List<DonationItem> findList(DonationItemQueryDto dto){
        return baseMapper.queryPage(dto);
    }
}

