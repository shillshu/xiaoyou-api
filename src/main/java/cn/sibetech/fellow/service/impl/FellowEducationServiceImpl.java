package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.FellowEducation;
import cn.sibetech.fellow.mapper.FellowEducationMapper;
import cn.sibetech.fellow.service.FellowEducationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FellowEducationServiceImpl extends ServiceImpl<FellowEducationMapper, FellowEducation> implements FellowEducationService {

    @Override
    public List<FellowEducation> queryList(FellowEducation edu){
        return baseMapper.queryList(edu);
    }
    @Override
    public List<FellowEducation> queryListByFellowId(String fellowId){
        QueryWrapper<FellowEducation> wrapper= new QueryWrapper<>();
        wrapper.eq("data_id",fellowId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Long queryCountFellowId(String fellowId){
        QueryWrapper<FellowEducation> wrapper= new QueryWrapper<>();
        wrapper.eq("data_id",fellowId);
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public List<FellowEducation> queryByFellowInfo(String fellowId,String xm, String rxnf, String bynf){
        return baseMapper.queryByFellowInfo(fellowId, xm, "", rxnf, bynf);
    }

    @Override
    public String findByXmAndXh(String xm, String xh){
        List<FellowEducation> list = baseMapper.queryByFellowInfo("", xm, xh, "", "");
        if( null != list && list.size() > 0){
            return list.get(0).getId();
        }else{
            return null;
        }
    }

    @Override
    public String findByFellowIdAndXh(String fellowId, String xh){
        List<FellowEducation> list = baseMapper.queryByFellowInfo(fellowId, "", xh, "", "");
        if( null != list && list.size() > 0){
            return list.get(0).getId();
        }else{
            return null;
        }
    }

}

