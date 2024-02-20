package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.FellowVipMember;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import cn.sibetech.fellow.mapper.FellowVipMemberMapper;
import cn.sibetech.fellow.service.FellowVipMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FellowVipMemberServiceImpl extends ServiceImpl<FellowVipMemberMapper, FellowVipMember> implements FellowVipMemberService {
    @Override
    public IPage<Fellow> page(FellowQueryDto dto, String kindId) {
        Page<Fellow> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto, kindId);
    }

    @Override
    public Long countByFellowId(String kindId, String fellowId){
        QueryWrapper<FellowVipMember> wrapper= new QueryWrapper<>();
        wrapper.eq("kind_id",kindId);
        wrapper.eq("fellow_id",fellowId);
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public boolean isFellowInKind(String kindId, String fellowId){
        Long count = this.countByFellowId(kindId, fellowId);
        return count > 0;
    }

    @Override
    public void remove(String fellowId, String kindId){
        QueryWrapper<FellowVipMember> wrapper= new QueryWrapper<>();
        wrapper.eq("kind_id",kindId);
        wrapper.eq("fellow_id",fellowId);
        baseMapper.delete(wrapper);
    }

    @Override
    public int addBatch(List<String> fellowIds, String kindId){
        int count = 0;
        if(CollectionUtils.isNotEmpty(fellowIds)){
            for(String fellowId:fellowIds){
                if( !isFellowInKind(kindId, fellowId) ) {
                    FellowVipMember member = new FellowVipMember();
                    member.setId(Identities.uuid());
                    member.setKindId(kindId);
                    member.setFellowId(fellowId);
                    baseMapper.insert(member);
                    count++;
                }
            }
        }
        return count;
    }


}

