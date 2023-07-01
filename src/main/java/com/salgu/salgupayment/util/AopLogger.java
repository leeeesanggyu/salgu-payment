package com.salgu.salgupayment.util;

import com.google.common.base.Joiner;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;

//@Component
//@Aspect
public class AopLogger {
    private static final Logger logger = LoggerFactory.getLogger(AopLogger.class);

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }

    @Pointcut("within(com.humanreview.*..*)") // 3
    public void onRequest() {
    }

    @Pointcut("within(com.humanreview.util.exception.GlobalExceptionHandler)") // 3
    public void onErrorRequest() {
    }

    @Around("com.humanreview.util.AopLogger.onRequest() || com.humanreview.util.AopLogger.onErrorRequest()") // 4
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
            e.printStackTrace();
        }


        long start = System.currentTimeMillis();
        String userAgent = request.getHeader("User-Agent");
        try {
            return pjp.proceed(pjp.getArgs()); // 6
        }finally {
            long end = System.currentTimeMillis();
            // webhooks 로깅 제외
            if (!request.getRequestURI().startsWith("/api/overseas/zinc/webhook/")) {
                logger.info("Request: {} {}{} < {} ({}ms)  {}", request.getMethod(), request.getRequestURI(),
                        params, HttpRequestUtils.getRemoteAddress(request), end - start,userAgent);
            }
        }
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
