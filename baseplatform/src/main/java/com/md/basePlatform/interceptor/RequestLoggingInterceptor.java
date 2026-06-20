package com.md.basePlatform.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 请求日志拦截器：打印方法与 URI 摘要，支持 X-Request-Id。
 */
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    /** 请求关联 ID 请求头（可选）。 */
    public static final String HEADER_REQUEST_ID = "X-Request-Id";

    /** 请求属性中的 requestId。 */
    public static final String ATTR_REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = request.getHeader(HEADER_REQUEST_ID);
        if (requestId == null || requestId.trim().isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }
        request.setAttribute(ATTR_REQUEST_ID, requestId);
        String query = request.getQueryString();
        log.info("http {} {} query={} {}={}",
                request.getMethod(),
                request.getRequestURI(),
                query,
                HEADER_REQUEST_ID,
                requestId);
        return true;
    }
}
