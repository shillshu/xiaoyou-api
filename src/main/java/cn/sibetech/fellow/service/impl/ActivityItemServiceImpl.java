package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.ActivityItem;
import cn.sibetech.fellow.domain.dto.ActivityItemQueryDto;
import cn.sibetech.fellow.mapper.ActivityItemMapper;
import cn.sibetech.fellow.service.ActivityItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ActivityItemServiceImpl extends ServiceImpl<ActivityItemMapper, ActivityItem> implements ActivityItemService {
    @Override
    public IPage<ActivityItem> queryPage(ActivityItemQueryDto dto) {
        Page<ActivityItem> page = new Page<ActivityItem>(dto.getPageNum(), dto.getPageSize());
        List<OrderItem> list = new ArrayList<OrderItem>();
        OrderItem item = new OrderItem();
        item.setColumn("when_created");
        item.setAsc(false);
        list.add(item);
        page.setOrders(list);
        QueryWrapper<ActivityItem> wrapper = new QueryWrapper<>();
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<ActivityItem> findList(ActivityItem dto){
        QueryWrapper<ActivityItem> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(dto.getIsZt())){
            wrapper.eq("is_zt", dto.getIsZt());
        }
        if(StringUtils.isNotEmpty(dto.getValid())){
            wrapper.eq("valid", dto.getValid());
        }
        wrapper.orderByDesc("when_created");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void add(ActivityItem item){
        if( null != item && "1".equals(item.getIsZt())){
           this.updateZt();
        }
        baseMapper.insert(item);
    }

    @Override
    public void edit(ActivityItem item){
        if( null != item && "1".equals(item.getIsZt())){
            this.updateZt();
        }
        baseMapper.updateById(item);
    }

    private void updateZt(){
        UpdateWrapper<ActivityItem> wrapper = new UpdateWrapper<>();
        wrapper.eq("is_zt", "1");

        ActivityItem upItem = new ActivityItem();
        upItem.setIsZt("0");
        baseMapper.update(upItem, wrapper);
    }

    @Override
    public ActivityItem findZtItem(){
        ActivityItem item = new ActivityItem();
        item.setIsZt("1");
        List<ActivityItem> list = this.findList(item);
        if( null != list && list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }
}

