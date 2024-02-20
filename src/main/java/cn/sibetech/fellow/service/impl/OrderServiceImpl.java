package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.Order;
import cn.sibetech.fellow.mapper.OrderMapper;
import cn.sibetech.fellow.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Override
    public IPage<Order> page(Order dto, int current, int size) {
        Page<Order> page = new Page<>(current, size);
        return baseMapper.queryPage(page, dto);
    }

    @Override
    public List<Order> findList(Order dto){
        return baseMapper.findList(dto);
    }

    @Override
    public Order findByNo(String no){
        QueryWrapper<Order> wrapper= new QueryWrapper<>();
        wrapper.eq("order_no",no);
        List<Order> orderList = baseMapper.selectList(wrapper);
        if( null != orderList && orderList.size() > 0){
            return orderList.get(0);
        }else{
            return null;
        }
    }

}

