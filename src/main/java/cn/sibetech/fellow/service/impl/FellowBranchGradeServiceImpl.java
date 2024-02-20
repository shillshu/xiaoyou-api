package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.FellowBranchGrade;
import cn.sibetech.fellow.mapper.FellowBranchGradeMapper;
import cn.sibetech.fellow.service.FellowBranchGradeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FellowBranchGradeServiceImpl extends ServiceImpl<FellowBranchGradeMapper, FellowBranchGrade> implements FellowBranchGradeService {
    @Override
    public IPage<FellowBranchGrade> page(FellowBranchGrade dto, int current, int size) {
        Page<FellowBranchGrade> page = new Page<FellowBranchGrade>(current, size);
        List<OrderItem> list = new ArrayList<OrderItem>();
        OrderItem item = new OrderItem();
        item.setColumn("when_created");
        item.setAsc(false);
        list.add(item);
        page.setOrders(list);
        QueryWrapper<FellowBranchGrade> wrapper = new QueryWrapper<>();
        return baseMapper.selectPage(page, wrapper);
    }
}

