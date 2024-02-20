package cn.sibetech.core.filter;

import cn.sibetech.core.config.ApplicationContextUtils;
import cn.sibetech.core.domain.ShiroUser;
import cn.sibetech.core.service.SecurityService;
import cn.sibetech.core.util.AntPathMatcher;
import cn.sibetech.core.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liwj
 * @date 2022/1/26 16:41
 */
@Slf4j
public class AppFilter implements Filter {
    private static String[] excludesPattern = new String[]{"/security/login", "/security/login_cas", "/security/login_jwt", "/captcha/**", "/doc.html", "/swagger-ui.html**", "/swagger-resources/**", "/v2/api-docs", "/webjars/**", "/actuator/", "/actuator/**", "/micro_app",
            "/resource/showImg/**", "/resource/preview/**", "/resource/downloadExport/**"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            String url = request.getServletPath();
            if (ignore(url)) {
                filterChain.doFilter(request, response);
            } else {
                String token = request.getHeader("Authorization");
                if (StringUtils.isNotEmpty(token)) {
                    Claims claims = JwtUtils.parseToken(token);
                    if (claims != null) {
                        SecurityService securityService = ApplicationContextUtils.get(SecurityService.class);
                        String userKey = JwtUtils.getUserKey(claims);
                        ShiroUser shiroUser = securityService.getLoginUser(userKey);
                        if (shiroUser != null) {
                            SecurityContextHolder.setCurrentUser(shiroUser);
                            filterChain.doFilter(request, response);
                        } else {
                            render(response, "401", "登录状态已过期");
                        }
                    } else {
                        render(response, "401", "令牌已过期或验证不正确");
                    }
                } else {
                    log.error("Authorization不能为空");
                    render(response, "401", "令牌不能为空");
                }
            }

        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private void render(HttpServletResponse response, String errcode, String errmsg) {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            response.getWriter().write("{\"errcode\":\"" + errcode + "\", \"errmsg\":\"" + errmsg + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean ignore(String requestURI) {
        for (String pattern : excludesPattern) {
            if (AntPathMatcher.instance().matches(pattern, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
