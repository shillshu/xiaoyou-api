package cn.sibetech.core.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

/**
 * @author liwj
 * @date 2022/2/23 15:45
 */
public class JwtUtils {
    private static final String SSO_JWT_SECURITY_KEY = "jwt.token.$%^&%$_-$%%$12qazw2sxQAZX00004";

    public static String createToken(Map<String, Object> claims) {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, SSO_JWT_SECURITY_KEY).compact();
        return token;
    }
    public static Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(SSO_JWT_SECURITY_KEY).parseClaimsJws(token).getBody();
    }
    public static String getValue(Claims claims, String key) {
        return toStr(claims.get(key), "");
    }
    /**
     * 根据令牌获取用户标识
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserKey(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }
    public static String getUserKey(Claims claims) {
        return getValue(claims, SecurityConstants.USER_KEY);
    }
    public static String toStr(Object value, String defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }
}
