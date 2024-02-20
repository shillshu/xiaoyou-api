package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.Activity;
import cn.sibetech.fellow.domain.dto.ActivityQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ActivityService extends IService<Activity> {
    public IPage<Activity> queryPage(ActivityQueryDto dto);

    public void addActivity(Activity dto);

    List<Activity> findList(Activity dto);

    List<Activity> findListByFellowId(String fellowId);

    public Activity findById(String id);
}
