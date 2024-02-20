package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.Activity;
import cn.sibetech.fellow.domain.ActivityFellow;
import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.dto.ActivityFellowQueryDto;
import cn.sibetech.fellow.mapper.ActivityFellowMapper;
import cn.sibetech.fellow.service.ActivityFellowService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActivityFellowServiceImpl extends ServiceImpl<ActivityFellowMapper, ActivityFellow> implements ActivityFellowService {
    @Override
    public IPage<ActivityFellow> queryPage(ActivityFellowQueryDto dto) {
        Page<ActivityFellow> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public IPage<FellowAuthInfo> page(List<String> activityIds, int current, int size) {
        Page<FellowAuthInfo> page = new Page<FellowAuthInfo>(current, size);
        if(CollectionUtils.isNotEmpty(activityIds)) {
            return baseMapper.queryFellowList(page, activityIds);
        }else{
            return null;
        }
    }

    @Override
    public List<ActivityFellow> list(ActivityFellow dto) {
        return baseMapper.queryPage( dto);
    }

    @Override
    public ActivityFellow findByFellowId(String activityId, String fellowId, String valid){
        QueryWrapper<ActivityFellow> wrapper = new QueryWrapper<ActivityFellow>();
        wrapper.eq("activity_Id", activityId);
        wrapper.eq("fellow_id", fellowId);
        if(StringUtils.isNotEmpty(valid)) {
            wrapper.eq("is_valid", valid);
        }
        List<ActivityFellow> list = baseMapper.selectList(wrapper);
        if( null != list && list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<FellowAuthInfo> fellowList(List<String> activityIds) {
        if(CollectionUtils.isNotEmpty(activityIds)) {
            return baseMapper.queryFellowList(activityIds);
        }else{
            return null;
        }
    }


    @Override
    public Long count(List<String> activityIds){
        QueryWrapper<ActivityFellow> wrapper= new QueryWrapper<>();
        wrapper.in("activity_Id",activityIds);
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public Long count(String activityId){
        QueryWrapper<ActivityFellow> wrapper= new QueryWrapper<>();
        wrapper.eq("activity_Id",activityId);
        return baseMapper.selectCount(wrapper);
    }


}

