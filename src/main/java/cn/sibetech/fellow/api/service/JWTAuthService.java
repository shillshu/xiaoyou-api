package cn.sibetech.fellow.api.service;


import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthService{
    private static final int DEFAULT_EXPIRE_DAYS = 7;
    private static Log logger = LogFactory.getLog(JWTAuthService.class);
    private static final String SSO_JWT_SECURITY_KEY = "jwt.token.$%^&%$_-$%%$12qazw2sxQAZX00004";

    public JWTAuthService() {
    }

    public static String issue(String userId, Integer expSeconds) {
        Calendar cal = Calendar.getInstance();
        Map<String, Object> claims = new HashMap();
        claims.put("iss", userId);
        if (expSeconds == null) {
            cal.add(5, 7);
            expSeconds = (int)(cal.getTimeInMillis() / 1000L);
        } else {
            expSeconds = (int)(cal.getTimeInMillis() / 1000L) + expSeconds;
        }

        claims.put("exp", expSeconds);
        return (new JWTSigner("jwt.token.$%^&%$_-$%%$12qazw2sxQAZX00004")).sign(claims);
    }

    public static Map<String, Object> verify(String token) {
        try {
            JWTVerifier jwtVerifier = new JWTVerifier("jwt.token.$%^&%$_-$%%$12qazw2sxQAZX00004");
            return jwtVerifier.verify(token);
        } catch (Exception var2) {
            logger.error("认证头【" + token + "】验证不合法", var2);
            return null;
        }
    }

    public static String verifyAndReturnUserId(String token) {
        try {
            JWTVerifier jwtVerifier = new JWTVerifier("jwt.token.$%^&%$_-$%%$12qazw2sxQAZX00004");
            Map<String, Object> result = jwtVerifier.verify(token);
            return result != null ? MapUtils.getString(result, "iss") : null;
        } catch (Exception var3) {
            logger.error("认证头【" + token + "】验证不合法", var3);
            return null;
        }
    }

    public static String getBindIdByJWT(HttpServletRequest request) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorizationHeader)) {
            return null;
        } else {
            String token = StringUtils.substringAfter(authorizationHeader, " ");
            String bindId = JWTAuthService.verifyAndReturnUserId(token);
            if (StringUtils.isEmpty(bindId)) {
                throw new Exception();
            } else {
                return bindId;
            }
        }
    }
}
