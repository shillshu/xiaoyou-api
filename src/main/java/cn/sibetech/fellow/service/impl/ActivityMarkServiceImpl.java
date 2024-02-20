package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.ActivityMark;
import cn.sibetech.fellow.mapper.ActivityMarkMapper;
import cn.sibetech.fellow.service.ActivityMarkService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActivityMarkServiceImpl extends ServiceImpl<ActivityMarkMapper, ActivityMark> implements ActivityMarkService {

    @Override
    public ActivityMark findByFellowId(String itemId, String fellowId){
        QueryWrapper<ActivityMark> wrapper = new QueryWrapper<ActivityMark>();
        wrapper.eq("item_id", itemId);
        wrapper.eq("fellow_id", fellowId);
        List<ActivityMark> list = baseMapper.selectList(wrapper);
        if( null != list && list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public int getOrderNo(String itemId){
        QueryWrapper<ActivityMark> wrapper = new QueryWrapper<ActivityMark>();
        wrapper.eq("item_id", itemId);
        int count = baseMapper.selectCount(wrapper).intValue();
        return count++;
    }

}

