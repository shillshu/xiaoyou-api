package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.Donation;
import cn.sibetech.fellow.domain.dto.DonationQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DonationMapper extends BaseMapper<Donation> {
    IPage<Donation> queryPage(Page<Donation> page, @Param("dto") DonationQueryDto dto);

    IPage<Donation> queryPageNotZs(Page<Donation> page, @Param("dto") Donation dto);

    List<Donation> findList(@Param("dto") Donation dto);

    Map findTjList(@Param("itemId") String itemId);

    List<Map> findXyTjList(@Param("itemId") String itemId);

    List<Map> getXyDonationList(@Param("openId") String openId);

    public String queryMaxCode(@Param("day") String day);
}
