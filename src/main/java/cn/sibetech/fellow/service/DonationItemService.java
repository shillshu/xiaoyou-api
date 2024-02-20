package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.DonationItem;
import cn.sibetech.fellow.domain.dto.DonationItemQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface DonationItemService extends IService<DonationItem> {
    public IPage<DonationItem> queryPage(DonationItemQueryDto dto);

    public List<DonationItem> findList(DonationItemQueryDto dto);

}
