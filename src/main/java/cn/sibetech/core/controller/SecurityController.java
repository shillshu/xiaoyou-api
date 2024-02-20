package cn.sibetech.core.controller;

import cn.sibetech.core.domain.dto.*;
import cn.sibetech.portal.dto.CasUser;
import cn.sibetech.core.config.ApplicationContextUtils;
import cn.sibetech.core.domain.ShiroUser;
import cn.sibetech.core.exception.AuthenticationException;
import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.domain.LoginVo;
import cn.sibetech.core.exception.ServiceException;
import cn.sibetech.core.request.ResponseBean;
import cn.sibetech.core.service.LogLoginService;
import cn.sibetech.core.service.SecurityService;
import cn.sibetech.core.util.ServletUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static cn.sibetech.core.util.SecurityConstants.*;

/**
 * @author liwj
 * @date 2022/1/21 14:48
 */
@Api(tags = "安全")
@RestController
@RequestMapping("/security")
public class SecurityController {
    @Autowired
    private SecurityService securityService;
    @Autowired
    private LogLoginService logLoginService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public ResponseBean<LoginVo> login(@RequestBody @Validated LoginDto dto, HttpServletRequest request){
        try {
            //校验图形验证码
            securityService.login(dto);
            ShiroUser currentUser = SecurityContextHolder.getCurrentUser();
            String clientIp = ServletUtils.getIpAddress(request);
            String[] clients = ServletUtils.getClientExplorer(request);
            if (CollectionUtils.isNotEmpty(currentUser.getRoles())) {
                logLoginService.saveLog(dto.getUsername(),"成功登录系统", clientIp, clients[0], clients[1], "1");
                if (currentUser.getCurrentRole()!=null) {
                    return ResponseBean.ok(new LoginVo(false, currentUser));
                } else {
                    return ResponseBean.ok(new LoginVo(true, currentUser));
                }
            } else {
                logLoginService.saveLog(dto.getUsername(),"没有权限访问", clientIp, clients[0], clients[1], "1");
                return ResponseBean.error("403", "没有权限访问");
            }

        } catch (AuthenticationException e){
            return ResponseBean.error(e.getMessage());
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    @ApiOperation(value = "登录(CAS)")
    @PostMapping("/login_cas")
    public ResponseBean<CasLoginDto> loginCas(@RequestBody @Valid LoginCasVO user) {
        try {
            String profile = ApplicationContextUtils.getActiveProfile();
            CasUser casUser = (CasUser) redisTemplate.opsForValue().get(profile+":cas:"+user.getUsername());
            if(casUser == null) {
                throw new AuthenticationException("未登录cas");
            }
            securityService.loginCas(casUser.getUsername());
            ShiroUser currentUser = SecurityContextHolder.getCurrentUser();
            logLoginService.saveLog(user.getUsername(),"成功登录系统（cas）", casUser.getIp(), casUser.getClientSystem(), casUser.getClientBrowser(), "1");
            CasLoginDto loginDto = new CasLoginDto();
            loginDto.setToken(currentUser.getToken());
            loginDto.setUsername(currentUser.getUsername());
            loginDto.setName(currentUser.getName());
            loginDto.setDeptName(currentUser.getDeptName());
            return ResponseBean.ok(loginDto);
        } catch (ServiceException e) {
            return ResponseBean.error(e.getMessage());
        }
    }

    @PostMapping("/login_jwt")
    public ResponseBean<CasLoginDto> loginJwt(@RequestBody @Valid LoginCasVO user) {
        try {
            String profile = ApplicationContextUtils.getActiveProfile();
            CasUser casUser = (CasUser) redisTemplate.opsForValue().get(profile+":jwt:"+user.getUsername());
            if(casUser == null) {
                throw new AuthenticationException("未登录");
            }
            securityService.loginCas(casUser.getUsername());
            ShiroUser currentUser = SecurityContextHolder.getCurrentUser();
            logLoginService.saveLog(user.getUsername(),"成功登录系统（jwt）", casUser.getIp(), casUser.getClientSystem(), casUser.getClientBrowser(), "1");
            CasLoginDto loginDto = new CasLoginDto();
            loginDto.setToken(currentUser.getToken());
            loginDto.setUsername(currentUser.getUsername());
            loginDto.setName(currentUser.getName());
            loginDto.setDeptName(currentUser.getDeptName());
            return ResponseBean.ok(loginDto);
        } catch (ServiceException e) {
            return ResponseBean.error(e.getMessage());
        }
    }
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "登出")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @GetMapping("/logout")
    public ResponseBean<String> logout(){
        securityService.logout();
        return ResponseBean.okMsg("注销成功");
    }
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "切换用户角色")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/switch_role")
    public ResponseBean<String> switchRole(@RequestBody SwitchRoleDto dto){
        if(StringUtils.isEmpty(dto.getRoleId())) {
            return ResponseBean.error("角色ID不能为空");
        }
        securityService.switchRole(dto);
        return ResponseBean.okMsg("切换成功");
    }

    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "获取当前用户")
    @ApiImplicitParam(name = AUTHORIZATION, value = TOKEN, paramType = HEADER, required = true)
    @PostMapping("/current_user")
    public ResponseBean<LoginVo> getCurrentUser(){
        try {
            ShiroUser currentUser = SecurityContextHolder.getCurrentUser();
            return ResponseBean.ok(new LoginVo(false, currentUser));
        } catch (AuthenticationException e) {
            return ResponseBean.error("403", e.getMessage());
        }
    }
}
