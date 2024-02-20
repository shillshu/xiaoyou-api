package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.FellowResume;
import cn.sibetech.fellow.mapper.FellowResumeMapper;
import cn.sibetech.fellow.service.FellowResumeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FellowResumeServiceImpl extends ServiceImpl<FellowResumeMapper, FellowResume> implements FellowResumeService {

    @Override
    public List<FellowResume> queryList(FellowResume resume){
        return baseMapper.queryList(resume);
    }
    @Override
    public List<FellowResume> queryListByFellowId(String fellowId){
        QueryWrapper<FellowResume> wrapper= new QueryWrapper<>();
        wrapper.eq("data_id",fellowId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Long queryCountFellowId(String fellowId){
        QueryWrapper<FellowResume> wrapper= new QueryWrapper<>();
        wrapper.eq("data_id",fellowId);
        return baseMapper.selectCount(wrapper);
    }
}

