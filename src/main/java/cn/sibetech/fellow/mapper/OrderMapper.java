package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {
    IPage<Order> queryPage(Page<Order> page, @Param("dto") Order dto);

    List<Order> findList(@Param("dto") Order dto);
}
