package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.FellowBranchMember;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FellowBranchMemberMapper extends BaseMapper<FellowBranchMember> {
    List<FellowBranchMember> queryList(@Param("member") FellowBranchMember dto);

    IPage<Fellow> queryPage(Page<Fellow> page, @Param("dto") FellowQueryDto dto, @Param("branchId") String branchId);

}
