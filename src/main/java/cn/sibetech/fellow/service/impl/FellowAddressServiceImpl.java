package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.FellowAddress;
import cn.sibetech.fellow.mapper.FellowAddressMapper;
import cn.sibetech.fellow.service.FellowAddressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FellowAddressServiceImpl extends ServiceImpl<FellowAddressMapper, FellowAddress> implements FellowAddressService {

    @Override
    public List<FellowAddress> queryList(FellowAddress address){
        return baseMapper.queryList(address);
    }

    @Override
    public List<FellowAddress> queryListByFellowId(String fellowId){
        QueryWrapper<FellowAddress> wrapper= new QueryWrapper<>();
        wrapper.eq("data_id",fellowId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Long queryCountFellowId(String fellowId){
        QueryWrapper<FellowAddress> wrapper= new QueryWrapper<>();
        wrapper.eq("data_id",fellowId);
        return baseMapper.selectCount(wrapper);
    }
}

