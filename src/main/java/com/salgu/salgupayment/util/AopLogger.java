package com.salgu.salgupayment.util;

import com.google.common.base.Joiner;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Aspect
public class AopLogger {

    @Pointcut("within(com.salgu.salgupayment.*.controller..*)") // 3
    public void onRequest() {
    }

    @Pointcut("within(com.salgu.salgupayment.util.exception.GlobalExceptionHandler)") // 3
    public void onErrorRequest() {
    }

    @Around("com.salgu.salgupayment.util.AopLogger.onRequest() || com.salgu.salgupayment.util.AopLogger.onErrorRequest()") // 4
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = // 5
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Map<String, String[]> paramMap = request.getParameterMap();
        String params = "";
        try {
            if (!paramMap.isEmpty()) {
                params = " [" + paramMapToString(paramMap) + "]";
            } else if (request.getContentType()!=null&&request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
                params = getParams(request).toString().length() > 0 ? " [" + getParams(request).toString() + "]" : "";
            }
        } catch (JSONException | NullPointerException e) {
            log.error("", e);
        }


        long start = System.currentTimeMillis();
        String userAgent = request.getHeader("User-Agent");
        try {
            return pjp.proceed(pjp.getArgs()); // 6
        }finally {
            long end = System.currentTimeMillis();
            // webhooks 로깅 제외
            String clientIp = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For") : request.getRemoteAddr();
            String requestIdFromGateway = request.getHeader("Trace-ID") != null ? request.getHeader("Trace-ID") : null;
            log.info("Request: {} {}{} < {} / {} ({}ms) {} {}", request.getMethod(), request.getRequestURI(),
                    params, request.getRemoteHost(), clientIp, end - start, requestIdFromGateway, userAgent);
        }
    }

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }

    private static JSONObject getParams(HttpServletRequest request) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            String replaceParam = param.replaceAll("\\.", "-");
            jsonObject.put(replaceParam, request.getParameter(param));
        }
        return jsonObject;
    }
}
