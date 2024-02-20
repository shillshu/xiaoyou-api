package cn.sibetech.core.service.impl;

import cn.sibetech.core.domain.Role;
import cn.sibetech.core.domain.RolePerm;
import cn.sibetech.core.domain.SelectModel;
import cn.sibetech.core.domain.dto.RolePermDto;
import cn.sibetech.core.domain.dto.RoleQueryDto;
import cn.sibetech.core.exception.ServiceException;
import cn.sibetech.core.mapper.RoleMapper;
import cn.sibetech.core.mapper.RolePermMapper;
import cn.sibetech.core.service.RoleService;
import cn.sibetech.core.util.PermUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RolePermMapper rolePermMapper;

    @Value("${sys.perm-service}")
    private String serviceId;
    @Override
    public IPage<Role> queryPage(RoleQueryDto dto) {
        Page<Role> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return this.baseMapper.queryPage(page, dto);
    }

    @Override
    public  List<SelectModel> selectRoleList(){
        return baseMapper.selectRole();
    }


    @Override
    public void add(Role dto) {
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.eq("name", dto.getName());
        if (this.baseMapper.exists(qw)) {
            throw new ServiceException("用户名已存在");
        }
        this.baseMapper.insert(dto);
    }

    @Override
    public void edit(Role dto) {
        this.baseMapper.updateById(dto);
    }


    @Override
    public void remove(List<String> ids) {
        this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<RolePerm> selectRolePerms(String roleId) {
        return baseMapper.selectRolePerms(roleId, serviceId);
    }
    @Override
    public RolePermDto selectPermModelByRoleId(String roleId) {
        List<RolePerm> list = rolePermMapper.selectList(new QueryWrapper<RolePerm>().eq("role_id", roleId).eq("service_id", serviceId));
        RolePermDto dto = new RolePermDto();
        dto.setRoleId(roleId);
        dto.setPerms(PermUtil.getPerm());
        dto.setPermString(list.stream().map(RolePerm::getPermString).collect(Collectors.toList()));
        return dto;
    }

    /** 查询角色的权限*/
    @Override
    public List<String> selectPerms(String roleId){
        return baseMapper.selectPerms(roleId, serviceId);
    }

    @Override
    @Transactional
    public void grantPerms(String roleId, List<String> permStringList) {
        rolePermMapper.delete(new QueryWrapper<RolePerm>().eq("role_id", roleId).eq("service_id", serviceId));
        if(CollectionUtils.isNotEmpty(permStringList)) {
            for(String permString: permStringList) {
                RolePerm perm = new RolePerm();
                perm.setRoleId(roleId);
                perm.setPermString(permString);
                perm.setServiceId(serviceId);
                rolePermMapper.insert(perm);
            }
        }
    }
}
