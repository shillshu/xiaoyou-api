package cn.sibetech.fellow.mapper;

import cn.sibetech.fellow.domain.FellowAuthInfo;
import cn.sibetech.fellow.domain.GroupMember;
import cn.sibetech.fellow.domain.dto.FellowAuthInfoQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface GroupMemberMapper extends BaseMapper<GroupMember> {
    IPage<FellowAuthInfo> queryPage(Page<FellowAuthInfo> page, @Param("dto") FellowAuthInfoQueryDto dto, @Param("groupIds") List<String> groupId);

    List<FellowAuthInfo> queryPage(@Param("dto") FellowAuthInfo dto, @Param("groupIds") List<String> groupId);
}
