package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.Activity;
import cn.sibetech.fellow.domain.dto.ActivityQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ActivityMapper extends BaseMapper<Activity> {
    IPage<Activity> queryPage(Page<Activity> page, @Param("dto") ActivityQueryDto dto);

    List<Activity> queryPage(@Param("dto") Activity dto);

    List<Activity> findListByFellowId(@Param("fellowId") String fellowId);
}
