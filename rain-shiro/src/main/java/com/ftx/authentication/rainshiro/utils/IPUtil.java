package com.ftx.authentication.rainshiro.utils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName IPUtil.java
 * @Description TODO
 * @createTime 2020年10月20日 14:30:00
 */
public class IPUtil {
    public static String getIp(HttpServletRequest request) {
        if (request == null)
            return "";
        String ip = request.getHeader("X-Requested-For");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if("0:0:0:0:0:0:0:1".equals(ip)){
            return "127.0.0.1";
        }
        return ip;
    }


    public static boolean isLocalHost(String ip){
        return "127.0.0.1".equals(ip) || "localhost".equals(ip);
    }
}
