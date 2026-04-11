package com.student.management.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.management.annotation.LogOperation;
import com.student.management.entity.OperationLog;
import com.student.management.repository.OperationLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final OperationLogRepository operationLogRepository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(com.student.management.annotation.LogOperation)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        OperationLog log = new OperationLog();
        boolean success = true;
        String errorMessage = null;

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            LogOperation logOperation = method.getAnnotation(LogOperation.class);

            log.setModule(logOperation.module());
            log.setType(logOperation.type());
            log.setMethod(request.getMethod());
            log.setUrl(request.getRequestURI());
            log.setIpAddress(getClientIp(request));
            log.setOperator(getCurrentUser(request));

            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                try {
                    log.setParams(objectMapper.writeValueAsString(args));
                } catch (Exception e) {
                    log.setParams("参数序列化失败");
                }
            }

            Object result = joinPoint.proceed();
            return result;

        } catch (Throwable e) {
            success = false;
            errorMessage = e.getMessage();
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.setDuration(duration);
            log.setSuccess(success);
            log.setErrorMessage(errorMessage);
            operationLogRepository.save(log);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return "admin";
        }
        return "anonymous";
    }
}
