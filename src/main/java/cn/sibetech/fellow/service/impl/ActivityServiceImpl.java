package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.fellow.domain.Activity;
import cn.sibetech.fellow.domain.dto.ActivityQueryDto;
import cn.sibetech.fellow.mapper.ActivityMapper;
import cn.sibetech.fellow.service.ActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
    @Override
    public IPage<Activity> queryPage(ActivityQueryDto dto) {
        Page<Activity> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public void addActivity(Activity dto){
        if( !"global".equals(dto.getType())){
            /*新增活动时 非校级活动 活动管理部门设置为当前登陆用户的部门*/
            dto.setDeptId(SecurityContextHolder.getCurrentUser().getDeptId());
        }
        baseMapper.insert(dto);
    }

    @Override
    public List<Activity> findList(Activity dto){
        return baseMapper.queryPage(dto);
    }

    @Override
    public List<Activity> findListByFellowId(String fellowId){
        return baseMapper.findListByFellowId(fellowId);
    }

    @Override
    public Activity findById(String id){
        Activity activity = new Activity();
        activity.setId(id);
        List<Activity> list = this.findList(activity);
        if( null != list && list.size() > 0 ){
            return list.get(0);
        }else{
            return null;
        }
    }

}

