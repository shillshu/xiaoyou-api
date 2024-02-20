package cn.sibetech.core.service;

import cn.sibetech.core.domain.ShiroUser;
import cn.sibetech.core.domain.dto.LoginDto;
import cn.sibetech.core.domain.dto.SwitchRoleDto;

import java.util.Map;

public interface SecurityService {
    ShiroUser getLoginUser(String token);

    void login(LoginDto dto);

    void logout();

    String createToken(ShiroUser shiroUser);

    void switchRole(SwitchRoleDto dto);

    void loginCas(String username);
}
