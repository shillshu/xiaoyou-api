package cn.sibetech.fellow.service.impl;

import cn.sibetech.fellow.domain.SignFellow;
import cn.sibetech.fellow.domain.dto.SignFellowQueryDto;
import cn.sibetech.fellow.mapper.SignFellowMapper;
import cn.sibetech.fellow.service.SignFellowService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SignFellowServiceImpl extends ServiceImpl<SignFellowMapper, SignFellow> implements SignFellowService {
    @Override
    public IPage<SignFellow> page(SignFellowQueryDto dto) {
        Page<SignFellow> page = new Page<SignFellow>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto);
    }

    public List<SignFellow> list(SignFellowQueryDto dto){
        return baseMapper.queryPage(dto);
    }

    public IPage<SignFellow> wqdPage(SignFellowQueryDto dto){
        Page<SignFellow> page = new Page<SignFellow>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryWqdPage(page, dto);
    }

    @Override
    public Long countByPc(String pcId){
        QueryWrapper<SignFellow> wrapper = new QueryWrapper<SignFellow>();
        wrapper.eq("pc_id", pcId);
        wrapper.eq("status", "1");
        wrapper.eq("jh_status", "1");
        return baseMapper.selectCount(wrapper);
    }

    public List<SignFellow> wqdList(SignFellowQueryDto dto){
        return baseMapper.queryWqdPage(dto);
    }

    @Override
    public List<SignFellow> findByFellowId(String fellowId){
        SignFellowQueryDto fellow = new SignFellowQueryDto();
        fellow.setFellowId(fellowId);
        return baseMapper.queryPage(fellow);
    }

    @Override
    public SignFellow findByFellowId(String pcId, String fellowId){
        QueryWrapper<SignFellow> wrapper = new QueryWrapper<SignFellow>();
        wrapper.eq("pc_id", pcId);
        wrapper.eq("fellow_id", fellowId);
        List<SignFellow> list = baseMapper.selectList(wrapper);
        if( null != list && list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }
}

