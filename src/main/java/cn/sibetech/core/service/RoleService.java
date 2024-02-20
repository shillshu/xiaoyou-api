package cn.sibetech.core.service;

import cn.sibetech.core.domain.Role;
import cn.sibetech.core.domain.RolePerm;
import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.dto.RolePermDto;
import cn.sibetech.core.domain.dto.RoleQueryDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {
    IPage<Role> queryPage(RoleQueryDto dto);

    List<SelectModel> selectRoleList();
    void add(Role dto);

    void edit(Role dto);

    void remove(List<String> ids);

    List<RolePerm> selectRolePerms(String roleId);

    RolePermDto selectPermModelByRoleId(String roleId);

    List<String> selectPerms(String roleId);

    void grantPerms(String roleId, List<String> permStringList);
}
