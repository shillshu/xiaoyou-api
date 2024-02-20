package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.ActivityFellow;
import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.dto.ActivityFellowQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ActivityFellowService extends IService<ActivityFellow> {
    public IPage<ActivityFellow> queryPage(ActivityFellowQueryDto dto);

    public IPage<FellowAuthInfo> page(List<String> activityIds, int current, int size);

    public List<ActivityFellow> list(ActivityFellow dto);

    public ActivityFellow findByFellowId(String activityId, String fellowId, String valid);

    List<FellowAuthInfo> fellowList(List<String> activityIds);

    public Long count(List<String> activityIds);

    public Long count(String activityId);

}
