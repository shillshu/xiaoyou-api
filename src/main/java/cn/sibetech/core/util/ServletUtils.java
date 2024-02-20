package cn.sibetech.core.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 客户端工具类
 *
 * @author ruoyi
 */
public class ServletUtils {
    /**
     * 判断IP是否在指定范围；
     */
    public static boolean ipIsValid(String ipSection, String ip) {
        if (ipSection == null) {
            throw new NullPointerException("IP段不能为空！");
        }
        if (ip == null) {
            throw new NullPointerException("IP不能为空！");
        }
        ipSection = ipSection.trim();
        ip = ip.trim();
        final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
        final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;
        if (!ipSection.matches(REGX_IPB) || !ip.matches(REGX_IP)) {
            return false;
        }
        int idx = ipSection.indexOf('-');
        String[] sips = ipSection.substring(0, idx).split("\\.");
        String[] sipe = ipSection.substring(idx + 1).split("\\.");
        String[] sipt = ip.split("\\.");
        long ips = 0L, ipe = 0L, ipt = 0L;
        for (int i = 0; i < 4; ++i) {
            ips = ips << 8 | Integer.parseInt(sips[i]);
            ipe = ipe << 8 | Integer.parseInt(sipe[i]);
            ipt = ipt << 8 | Integer.parseInt(sipt[i]);
        }
        if (ips > ipe) {
            long t = ips;
            ips = ipe;
            ipe = t;
        }
        return ips <= ipt && ipt <= ipe;
    }
    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        try {
            return getRequestAttributes().getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    public static ServletRequestAttributes getRequestAttributes() {
        try {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            return (ServletRequestAttributes) attributes;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取真实IP
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    public static String[] getClientExplorer(HttpServletRequest request) {
        String[] explorer = new String[]{"", ""};
        String info = request.getHeader("user-agent");
        if (StringUtils.isEmpty(info)) {
            return null;
        }
        UserAgent userAgent = UserAgent.parseUserAgentString(info);

        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        Browser browser = userAgent.getBrowser();
        if (operatingSystem != null) {
            explorer[0] = operatingSystem.getName();
        }
        if (browser != null) {
            explorer[1] = browser.getName();
        }
        return explorer;
    }
    /**
     * 判断是否手机浏览器
     * @param request
     * @return
     */
    public static boolean judgeIsMoblie(HttpServletRequest request) {
        String info = request.getHeader("user-agent");
        if(StringUtils.isEmpty(info)) {
            return false;
        }
        UserAgent userAgent = UserAgent.parseUserAgentString(info);
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        DeviceType deviceType = operatingSystem.getDeviceType();
        if("mobile".equalsIgnoreCase(deviceType.getName())){
            return true;
        }
        return false;
    }
    public static boolean validateWx(HttpServletRequest request){
        String info = request.getHeader("user-agent");
        if(StringUtils.isEmpty(info)) {
            return false;
        }
        info=info.toLowerCase();
        if (info.indexOf("micromessenger") > 0) {
            // 是微信浏览器
            return true;
        }
        return false;
    }

    public static String encodeURL(String str){
        if(StringUtils.isEmpty(str)){
            return "";
        }
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
