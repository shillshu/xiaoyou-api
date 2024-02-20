package cn.sibetech.core.service.impl;

import cn.sibetech.core.domain.*;
import cn.sibetech.core.domain.dto.LoginDto;
import cn.sibetech.core.domain.dto.SwitchRoleDto;
import cn.sibetech.core.enums.OpenErrorCode;
import cn.sibetech.core.exception.AuthenticationException;
import cn.sibetech.core.exception.ServiceException;
import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.mapper.RoleMapper;
import cn.sibetech.core.mapper.UserMapper;
import cn.sibetech.core.mapper.UserRoleMapper;
import cn.sibetech.core.service.RoleService;
import cn.sibetech.core.service.SecurityService;
import cn.sibetech.core.util.CryptUtils;
import cn.sibetech.core.util.Identities;
import cn.sibetech.core.util.JwtUtils;
import cn.sibetech.core.util.RoleUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.sibetech.core.util.SecurityConstants.*;

@Service
public class SecurityServiceImpl implements SecurityService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleService roleService;
    @Override
    public ShiroUser getLoginUser(String token) {
        if(StringUtils.isNotEmpty(token)) {
            ShiroUser shiroUser = (ShiroUser) redisTemplate.opsForValue().get(getTokenKey(token));
            if(shiroUser!=null) {
                verifyToken(shiroUser);
                return shiroUser;
            }
        }
        return null;
    }


    @Override
    public void login(LoginDto dto) {
        User user = userMapper.selectByUsername(dto.getUsername());
        if (user == null) {
            throw new AuthenticationException(OpenErrorCode.IncorrectCredentials.getDescription());
        }
        if (StringUtils.isEmpty(user.getPassword()) || StringUtils.isEmpty(user.getSalt())) {
            throw new AuthenticationException(OpenErrorCode.SystemError.getDescription());
        }
        String cryptPwd = CryptUtils.encode(dto.getPassword(), user.getSalt());
        if (!user.getPassword().equals(cryptPwd)) {
            throw new AuthenticationException(OpenErrorCode.IncorrectCredentials.getDescription());
        }
        if (StringUtils.isEmpty(user.getStatus()) || user.getStatus().equals("0")) {
            throw new AuthenticationException(OpenErrorCode.LockedAccount.getDescription());
        }
        ShiroUser shiroUser = new ShiroUser();
        shiroUser.setUserId(user.getId());
        shiroUser.setUsername(user.getUsername());
        shiroUser.setName(user.getTruename());
        shiroUser.setUsertype(user.getUsertype());
        shiroUser.setDeptId(user.getDeptId());
        shiroUser.setDeptName(user.getDeptName());
        shiroUser.setMobile(user.getMobile());
        /**
         * 查询用户的所有角色
         */
        List<Role> roles = roleMapper.selectByUserId(user.getId());
        if (roles != null && roles.size() > 0) {
            shiroUser.setRoles(roles);
            if(roles.size()==1) {
                setCurrentRole(shiroUser, roles.get(0));
            }
        }
        String token = createToken(shiroUser);
        shiroUser.setToken(token);
        SecurityContextHolder.setCurrentUser(shiroUser);
    }

    @Override
    public void logout() {
        ShiroUser currentUser = SecurityContextHolder.getCurrentUser();
        redisTemplate.delete(getTokenKey(currentUser.getUserKey()));
    }

    public String createToken(ShiroUser shiroUser) {
        String userKey = Identities.uuid();
        shiroUser.setUserKey(userKey);
        refreshToken(shiroUser);
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(USER_KEY, userKey);
        claimsMap.put(DETAILS_USER_ID, shiroUser.getUserId());
        claimsMap.put(DETAILS_USERNAME, shiroUser.getUsername());
        String accessToken = JwtUtils.createToken(claimsMap);
        return accessToken;
    }

    @Override
    public void switchRole(SwitchRoleDto dto) {
        ShiroUser currentUser = SecurityContextHolder.getCurrentUser();
        List<Role> myroles = currentUser.getRoles();
        Role currentRole = null;
        if (dto.getRoleId() != null && myroles != null && myroles.size() > 0) {
            for (Role role : myroles) {
                if (dto.getRoleId().equals(role.getId())) {
                    currentRole = role;
                    break;
                }
            }
        }
        if (currentRole != null) {
            setCurrentRole(currentUser, currentRole);
            refreshToken(currentUser);
            SecurityContextHolder.setCurrentUser(currentUser);
        }
        else {
            throw new ServiceException("未找到角色");
        }

    }

    @Override
    public void loginCas(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new AuthenticationException(OpenErrorCode.IncorrectCredentials.getDescription());
        }

        if (StringUtils.isEmpty(user.getStatus()) || user.getStatus().equals("0")) {
            throw new AuthenticationException(OpenErrorCode.LockedAccount.getDescription());
        }
        ShiroUser shiroUser = new ShiroUser();
        shiroUser.setUserId(user.getId());
        shiroUser.setUsername(user.getUsername());
        shiroUser.setName(user.getTruename());
        shiroUser.setUsertype(user.getUsertype());
        shiroUser.setDeptId(user.getDeptId());
        shiroUser.setDeptName(user.getDeptName());
        shiroUser.setMobile(user.getMobile());
        /**
         * 查询用户的所有角色
         */
        List<Role> roles = roleMapper.selectByUserId(user.getId());
        if (roles != null && roles.size() > 0) {
            shiroUser.setRoles(roles);
            if(roles.size()==1) {
                setCurrentRole(shiroUser, roles.get(0));
            }
        }
        String token = createToken(shiroUser);
        shiroUser.setToken(token);
        SecurityContextHolder.setCurrentUser(shiroUser);
    }

    private void setCurrentRole(ShiroUser currentUser, Role role){
        currentUser.setCurrentRole(role);
        List<RolePerm> perms = roleService.selectRolePerms(role.getId());
        if(CollectionUtils.isNotEmpty(perms)) {
            currentUser.setPermissions(perms.stream().map(m-> m.getPermString()).collect(Collectors.toList()));
        }
        initStudentScope(currentUser, role);
    }

    /* 新版学工*/
    private void initStudentScope(ShiroUser currentUser, Role role){
        List<String> bjScopeList = new ArrayList<>();
        List<String> njScopeList = new ArrayList<>();
        List<String> xyScopeList = new ArrayList<>();
        if(RoleUtil.isFdy(role) ){
            List<UserRole> userRoleList = userRoleMapper.queryUserRole(currentUser.getUserId(), role.getId());
            if( CollectionUtils.isNotEmpty(userRoleList)){
                for( UserRole userRole : userRoleList){
                    List<DeptClass> classList = userRoleMapper.queryClass(getScopeStr(userRole.getScope()) );
                    if( CollectionUtils.isNotEmpty(classList)){
                        classList.stream().forEach( item -> {
                            bjScopeList.add(item.getId());
                            if( !njScopeList.contains(item.getNj())){
                                njScopeList.add(item.getNj());
                            }
                            if( !xyScopeList.contains(item.getXyId())){
                                xyScopeList.add(item.getXyId());
                            }
                        });
                    }
                }
            }
            if( CollectionUtils.isEmpty(bjScopeList)){
                bjScopeList.add("error");
            }
            if( CollectionUtils.isEmpty(njScopeList)){
                njScopeList.add("error");
            }
            if( CollectionUtils.isEmpty(xyScopeList)){
                xyScopeList.add("error");
            }
        }else if( RoleUtil.isDeptAdmin(role)){
            List<UserRole> userRoleList = userRoleMapper.queryUserRole(currentUser.getUserId(), role.getId());
            if( CollectionUtils.isNotEmpty(userRoleList)){
                for( UserRole userRole : userRoleList){
                    String scope = userRole.getScope();
                    if(StringUtils.isNotEmpty(scope)){
                        String[] scopes = scope.split(",");
                        if( null != scopes && scopes.length == 2){
                            xyScopeList.add(scopes[1]);
                        }
                    }
                }
            }
            if( CollectionUtils.isEmpty(xyScopeList)){
                xyScopeList.add("error");
            }
        }
        currentUser.setBjScopes(bjScopeList);
        currentUser.setNjScopes(njScopeList);
        currentUser.setXyScopes(xyScopeList);
    }

    /* 旧版学工*/
    private void initStudentScopeOld(ShiroUser currentUser, Role role){
        List<String> bjScopeList = new ArrayList<>();
        List<String> njScopeList = new ArrayList<>();
        List<String> xyScopeList = new ArrayList<>();
        if(RoleUtil.isFdy(role) ){
            List<DeptClass> classList = userRoleMapper.queryUserClass(currentUser.getUserId(), "fdy");
            if( CollectionUtils.isNotEmpty(classList)){
                classList.stream().forEach( item -> {
                    bjScopeList.add(item.getId());
                    if( !njScopeList.contains(item.getNj())){
                        njScopeList.add(item.getNj());
                    }
                    if( !xyScopeList.contains(item.getXyId())){
                        xyScopeList.add(item.getXyId());
                    }
                });
            }
            if( CollectionUtils.isEmpty(bjScopeList)){
                bjScopeList.add("error");
            }
            if( CollectionUtils.isEmpty(njScopeList)){
                njScopeList.add("error");
            }
            if( CollectionUtils.isEmpty(xyScopeList)){
                xyScopeList.add("error");
            }
        }else if( RoleUtil.isDeptAdmin(role)){
            xyScopeList.add(currentUser.getDeptId());
        }
        currentUser.setBjScopes(bjScopeList);
        currentUser.setNjScopes(njScopeList);
        currentUser.setXyScopes(xyScopeList);
    }

    private String getScopeStr(String scope){
        if(StringUtils.isNotEmpty(scope)){
            String[] scopes = scope.split(",");
            if( null != scopes && scopes.length > 0){
                StringBuffer buffer = new StringBuffer();
                for(String sp:scopes){/* 或 */
                    if(StringUtils.isNotEmpty(sp)){
                        String[] ss = sp.split("_");
                        if( null != ss && ss.length > 0){
                            String index = ss[0];/* xy   nj  bj*/
                            String str = "";
                            if( "xy".equals(index)){/*xy_303000*/
                                str =  "s.xy_id = '" + ss[1] + "' ";
                            }
                            else if( "nj".equals(index)) {/*nj_301000_2014*/
                                str = "s.xy_id  = '" + ss[1] + "' and " + "s.nj = '" + ss[2] + "' ";
                            }
                            else if( "bj".equals(index)){/*bj_301000_14010202*/
                                str =  "s.id = '" + ss[2] + "' ";
                            }
                            if( buffer.length() > 0 ){
                                buffer.append(" or ( ").append(str).append(" ) ");
                            }else{
                                buffer.append(" ( ").append(str).append(" ) ");
                            }
                        }
                    }
                }
                return " and (" +  buffer.toString() + " ) ";
            }else{
                return " and ( 1 = 2 ) ";
            }
        }else{
            return " and  ( 1 = 2 ) ";
        }
    }

    private void verifyToken(ShiroUser shiroUser) {
        long expireTime = shiroUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= REFRESH_TIME) {
            refreshToken(shiroUser);
        }
    }
    private void refreshToken(ShiroUser shiroUser) {
        shiroUser.setLoginTime(System.currentTimeMillis());
        shiroUser.setExpireTime(shiroUser.getLoginTime() + EXPIRE_TIME * MILLIS_MINUTE);
        redisTemplate.opsForValue().set(getTokenKey(shiroUser.getUserKey()), shiroUser, EXPIRE_TIME, TimeUnit.MINUTES);
    }
    private String getTokenKey(String token) {
        return TOKEN_PREFIX + token;
    }
}
