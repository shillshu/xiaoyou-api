package cn.sibetech.fellow.service.impl;

import cn.sibetech.core.util.Identities;
import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.GroupMember;
import cn.sibetech.fellow.domain.dto.FellowAuthInfoQueryDto;
import cn.sibetech.fellow.mapper.GroupMemberMapper;
import cn.sibetech.fellow.service.GroupMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements GroupMemberService {
    @Override
    public IPage<FellowAuthInfo> queryPage(FellowAuthInfoQueryDto dto, List<String> groupIdList) {
        Page<FellowAuthInfo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        if(CollectionUtils.isNotEmpty(groupIdList)) {
            return baseMapper.queryPage(page,  dto, groupIdList);
        }else{
            return null;
        }
    }

    @Override
    public List<FellowAuthInfo> listFellow(FellowAuthInfo dto,List<String> groupIdList){
        if(CollectionUtils.isNotEmpty(groupIdList)) {
            return baseMapper.queryPage(dto, groupIdList);
        }else{
            return null;
        }
    }


    @Override
    public Long countByFellowId(String groupId, String fellowId){
        QueryWrapper<GroupMember> wrapper= new QueryWrapper<>();
        wrapper.eq("group_id",groupId);
        wrapper.eq("fellow_id",fellowId);
        return baseMapper.selectCount(wrapper);
    }

    @Override
    public boolean isFellowInKind(String groupId, String fellowId){
        Long count = this.countByFellowId(groupId, fellowId);
        return count > 0;
    }

    @Override
    public void remove(String fellowId, String groupId){
        QueryWrapper<GroupMember> wrapper= new QueryWrapper<>();
        wrapper.eq("group_id",groupId);
        wrapper.eq("fellow_id",fellowId);
        baseMapper.delete(wrapper);
    }

    @Override
    public Long count(List<String> groupIds){
        QueryWrapper<GroupMember> wrapper= new QueryWrapper<>();
        wrapper.in("group_id",groupIds);
        return baseMapper.selectCount(wrapper);
    }


    @Override
    public int addBatch(List<String> fellowIds, String groupId){
        int count = 0;
        if(CollectionUtils.isNotEmpty(fellowIds)){
            for(String fellowId:fellowIds){
                if( !isFellowInKind(groupId, fellowId) ) {
                    GroupMember member = new GroupMember();
                    member.setFellowId(fellowId);
                    member.setGroupId(groupId);
                    member.setId(Identities.uuid());
                    baseMapper.insert(member);
                    count++;
                }
            }
        }
        return count;
    }


}

