package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.ActivityFellow;
import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.dto.ActivityFellowQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ActivityFellowMapper extends BaseMapper<ActivityFellow> {
    IPage<ActivityFellow> queryPage(Page<ActivityFellow> page, @Param("dto") ActivityFellowQueryDto dto);

    List<ActivityFellow> queryPage(@Param("dto") ActivityFellow dto);

    List<FellowAuthInfo> queryFellowList(@Param("activityId") List<String> activityId);

    IPage<FellowAuthInfo> queryFellowList(Page<FellowAuthInfo> page,@Param("activityId") List<String> activityId);

}
