package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.DonationItem;
import cn.sibetech.fellow.domain.dto.DonationItemQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DonationItemMapper extends BaseMapper<DonationItem> {
    IPage<DonationItem> queryPage(Page<DonationItem> page, @Param("dto") DonationItemQueryDto dto);

    List<DonationItem> queryPage(@Param("dto") DonationItemQueryDto dto);
}
