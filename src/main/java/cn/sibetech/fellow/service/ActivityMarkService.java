package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.ActivityMark;
import com.baomidou.mybatisplus.extension.service.IService;



public interface ActivityMarkService extends IService<ActivityMark> {
    public ActivityMark findByFellowId(String activityId, String fellowId);

    public int getOrderNo(String itemId);
}
