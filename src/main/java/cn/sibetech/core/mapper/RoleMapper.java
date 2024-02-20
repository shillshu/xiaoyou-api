package cn.sibetech.core.mapper;

import cn.sibetech.core.domain.Role;
import cn.sibetech.core.domain.RolePerm;
import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.dto.RoleQueryDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    List<Role> selectByUserId(@Param("userId") String userId);

    List<RolePerm> selectRolePerms(@Param("roleId")String roleId, @Param("serviceId")String serviceId);

    IPage<Role> queryPage(Page<Role> page, @Param("dto") RoleQueryDto dto);

    /**
     * 查询角色的权限
     * @param roleId
     * @return
     */
    List<String> selectPerms(@Param("roleId") String roleId, @Param("serviceId") String serviceId);

    List<SelectModel> selectRole();
}
