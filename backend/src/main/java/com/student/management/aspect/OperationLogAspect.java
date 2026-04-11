package com.student.management.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.management.annotation.OperationLog;
import com.student.management.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OperationLogAspect {

    private final OperationLogService operationLogService;
    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(com.student.management.annotation.OperationLog)")
    public void operationLogPointcut() {}

    @Around("operationLogPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        OperationLog annotation = method.getAnnotation(OperationLog.class);
        
        com.student.management.entity.OperationLog operationLog = new com.student.management.entity.OperationLog();
        operationLog.setModule(annotation.module());
        operationLog.setOperationType(annotation.operationType());
        operationLog.setOperationTime(LocalDateTime.now());
        
        HttpServletRequest request = getRequest();
        if (request != null) {
            operationLog.setRequestMethod(request.getMethod());
            operationLog.setRequestUrl(request.getRequestURI());
            operationLog.setIpAddress(getIpAddress(request));
            operationLog.setOperator(getOperator(request));
            
            String params = getRequestParams(point, request);
            operationLog.setRequestParams(params);
        }
        
        Object result = null;
        Exception exception = null;
        
        try {
            result = point.proceed();
            operationLog.setIsSuccess(true);
        } catch (Exception e) {
            exception = e;
            operationLog.setIsSuccess(false);
            operationLog.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            operationLog.setDuration(endTime - startTime);
            
            try {
                operationLogService.saveLog(operationLog);
            } catch (Exception e) {
                log.error("保存操作日志失败", e);
            }
        }
        
        return result;
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String getOperator(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return "user_" + token.substring(7, Math.min(20, token.length()));
        }
        return "anonymous";
    }

    private String getRequestParams(ProceedingJoinPoint point, HttpServletRequest request) {
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            String[] paramNames = signature.getParameterNames();
            Object[] paramValues = point.getArgs();
            
            if (paramNames == null || paramNames.length == 0) {
                return null;
            }
            
            Map<String, Object> params = new HashMap<>();
            for (int i = 0; i < paramNames.length; i++) {
                Object value = paramValues[i];
                if (value instanceof MultipartFile) {
                    params.put(paramNames[i], "MultipartFile: " + ((MultipartFile) value).getOriginalFilename());
                } else if (value instanceof HttpServletRequest) {
                    continue;
                } else {
                    params.put(paramNames[i], value);
                }
            }
            
            if (params.isEmpty()) {
                return null;
            }
            
            String json = objectMapper.writeValueAsString(params);
            if (json.length() > 2000) {
                json = json.substring(0, 2000) + "...";
            }
            return json;
        } catch (Exception e) {
            log.warn("获取请求参数失败", e);
            return null;
        }
    }
}
