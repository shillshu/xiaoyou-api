package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.FellowBranch;
import cn.sibetech.fellow.domain.dto.FellowBranchQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FellowBranchMapper extends BaseMapper<FellowBranch> {
    IPage<FellowBranch> queryPage(Page<FellowBranch> page, @Param("branch") FellowBranchQueryDto dto);

    List<FellowBranch> queryList(@Param("branch") FellowBranch dto);

    List<FellowBranch> queryFellowBranchList(@Param("fellowId") String fellowId);
}
