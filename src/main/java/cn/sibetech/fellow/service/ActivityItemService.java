package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.ActivityItem;
import cn.sibetech.fellow.domain.dto.ActivityItemQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ActivityItemService extends IService<ActivityItem> {
    public IPage<ActivityItem> queryPage(ActivityItemQueryDto dto);

    public List findList(ActivityItem dto);

    public void add(ActivityItem item);

    public void edit(ActivityItem item);

    public ActivityItem findZtItem();
}
