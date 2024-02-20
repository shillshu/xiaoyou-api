package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.Order;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface OrderService extends IService<Order> {
    public IPage<Order> page(Order dto, int current, int size);

    public List<Order> findList(Order dto);

    Order findByNo(String no);

}
