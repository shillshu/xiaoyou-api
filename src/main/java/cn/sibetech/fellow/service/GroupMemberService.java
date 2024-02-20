package cn.sibetech.fellow.service;

import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.GroupMember;
import cn.sibetech.fellow.domain.dto.FellowAuthInfoQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface GroupMemberService extends IService<GroupMember> {
    public IPage<FellowAuthInfo> queryPage(FellowAuthInfoQueryDto dto, List<String> groupIdList);

    public List<FellowAuthInfo> listFellow(FellowAuthInfo dto, List<String> groupIdList);

    Long countByFellowId(String groupId, String fellowId);

    boolean isFellowInKind(String groupId, String fellowId);

    void remove(String fellowId, String groupId);

    public Long count(List<String> groupIds);

    int addBatch(List<String> fellowIds, String groupId);
}
