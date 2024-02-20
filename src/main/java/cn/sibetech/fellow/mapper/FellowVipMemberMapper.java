package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.FellowVipMember;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


public interface FellowVipMemberMapper extends BaseMapper<FellowVipMember> {
    IPage<Fellow> queryPage(Page<Fellow> page, @Param("dto") FellowQueryDto dto, @Param("kindId") String kindId);

}
