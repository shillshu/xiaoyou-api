package cn.sibetech.core.service.impl;

import cn.sibetech.core.domain.*;
import cn.sibetech.core.domain.dto.UserQueryDto;
import cn.sibetech.core.exception.ServiceException;
import cn.sibetech.core.mapper.FlowGroupMapper;
import cn.sibetech.core.mapper.RoleMapper;
import cn.sibetech.core.mapper.UserMapper;
import cn.sibetech.core.mapper.UserRoleMapper;
import cn.sibetech.core.service.UserService;
import cn.sibetech.core.util.CryptUtils;
import cn.sibetech.core.util.Identities;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private FlowGroupMapper flowGroupMapper;

    @Resource
    private RoleMapper roleMapper;
    @Override
    public IPage<User> queryPage(UserQueryDto dto) {
        Page<User> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return this.baseMapper.queryPage(page, dto);
    }

    @Override
    public List<SelectModel> selectDeptList(String fatherId){
        return baseMapper.selectDeptList(fatherId);
    }

    @Override
    public void add(User dto) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", dto.getUsername());
        if (this.baseMapper.exists(qw)) {
            throw new ServiceException("用户名已存在");
        }
        dto.setId(dto.getUsername());
        dto.setStatus("1");
        String pass = "Cug@si" + dto.getUsername() + "b";
        String salt = CryptUtils.generateSalt();
        dto.setSalt(salt);
        String newCrptPwd = CryptUtils.encode(pass, dto.getSalt());
        dto.setPassword(newCrptPwd);
        dto.setUserNumber(dto.getUsername());
        baseMapper.insert(dto);
        this.addUserRole(dto.getId(), dto.getRoleId());
    }

    /* 处理用户角色 一个用户一个角色*/
    private void addUserRole(String userId, String roleId){
        if( StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(roleId)){
            Long count = userRoleMapper.selectCount( new QueryWrapper<UserRole>().eq("user_id", userId).eq("role_id", roleId));
            /* 如果没有这个角色才处理*/
            if( count == 0){
                userRoleMapper.delete(new QueryWrapper<UserRole>().eq("user_id", userId)); /* 删除这个用户所有的角色*/
                UserRole userRole = new UserRole();
                userRole.setId(Identities.uuid());
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }

    }


    @Override
    public void edit(User dto){
        baseMapper.updateById(dto);
        this.addUserRole(dto.getId(), dto.getRoleId());
    }

    @Override
    public User findById(String id){
        User user = baseMapper.selectById(id);
        if( null != user ){
            List<Role> roleList = roleMapper.selectByUserId(id);
            if(CollectionUtils.isNotEmpty(roleList)){
                Role role = roleList.get(0);
                user.setRoleId(role.getId());
                user.setRoleName(role.getName());
            }
        }
        return user;
    }

    @Override
    public List<User> queryByIds(List<String> ids) {
        return baseMapper.queryByIds(ids);
    }

    public List<User> queryFlowGroupUser(List<String> groups){
        return flowGroupMapper.queryUserList(groups);
    }

    @Override
    public ShiroUser queryUserInfo(String username) {
        User user = this.baseMapper.selectByUsername(username);
        if(user != null) {
            ShiroUser shiroUser = new ShiroUser(user.getId(), user.getUsername(), user.getTruename(), user.getUsertype());
            shiroUser.setFlowGroups(flowGroupMapper.queryForUser(shiroUser.getUserId()));
            return shiroUser;
        }
        return null;
    }
}
