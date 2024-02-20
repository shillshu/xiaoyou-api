package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.Fellow;
import cn.sibetech.fellow.domain.FellowBranchMember;
import cn.sibetech.fellow.domain.dto.FellowQueryDto;
import cn.sibetech.fellow.mapper.FellowBranchMemberMapper;
import cn.sibetech.fellow.service.FellowBranchMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FellowBranchMemberServiceImpl extends ServiceImpl<FellowBranchMemberMapper, FellowBranchMember> implements FellowBranchMemberService {

    @Override
    public IPage<Fellow> page(FellowQueryDto dto, String kindId) {
        Page<Fellow> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return baseMapper.queryPage(page, dto, kindId);
    }

    @Override
    public void remove(String fellowId, String branchId){
        QueryWrapper<FellowBranchMember> wrapper= new QueryWrapper<>();
        wrapper.eq("branch_id",branchId);
        wrapper.eq("fellow_id",fellowId);
        baseMapper.delete(wrapper);
    }

    @Override
    public boolean isFellowInBranch(String branchId, String fellowId){
        Long count = this.queryCountFellowId(fellowId, branchId);
        return count > 0;
    }

    @Override
    public List<FellowBranchMember> queryListByFellowId(String fellowId, String branchId){
        QueryWrapper<FellowBranchMember> wrapper= new QueryWrapper<>();
        wrapper.eq("fellow_id",fellowId);
        wrapper.eq("branch_id",branchId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Long queryCountFellowId(String fellowId, String branchId){
        QueryWrapper<FellowBranchMember> wrapper= new QueryWrapper<>();
        wrapper.eq("fellow_id",fellowId);
        wrapper.eq("branch_id",branchId);
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public  void addBranchMember(String fellowId, String branchId){
        FellowBranchMember member = new FellowBranchMember();
        member.setId(Identities.uuid());
        member.setBranchId(branchId);
        member.setFellowId(fellowId);
        baseMapper.insert(member);
    }

    @Override
    public int addBatch(List<String> fellowIds, String branchId){
        int count = 0;
        if(CollectionUtils.isNotEmpty(fellowIds)){
            for(String fellowId:fellowIds){
                if( !isFellowInBranch(branchId, fellowId) ) {
                    FellowBranchMember member = new FellowBranchMember();
                    member.setId(Identities.uuid());
                    member.setBranchId(branchId);
                    member.setFellowId(fellowId);
                    baseMapper.insert(member);
                    count++;
                }
            }
        }
        return count;
    }
}

