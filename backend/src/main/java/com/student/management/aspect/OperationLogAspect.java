package com.student.management.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.management.entity.OperationLog;
import com.student.management.repository.OperationLogRepository;
import com.student.management.service.TokenService;
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

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class OperationLogAspect {

    private final OperationLogRepository operationLogRepository;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(com.student.management.annotation.OperationLog)")
    public void operationLogPointCut() {
    }

    @Around("operationLogPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.student.management.annotation.OperationLog operationLogAnnotation =
                method.getAnnotation(com.student.management.annotation.OperationLog.class);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        OperationLog operationLog = new OperationLog();
        operationLog.setOperationModule(operationLogAnnotation.module());
        operationLog.setOperationType(operationLogAnnotation.type());
        operationLog.setRequestMethod(request != null ? request.getMethod() : "");
        operationLog.setRequestUrl(request != null ? request.getRequestURI() : "");
        operationLog.setIpAddress(getClientIp(request));
        operationLog.setOperator(getCurrentUsername(request));

        if (operationLogAnnotation.saveParams()) {
            try {
                Object[] args = joinPoint.getArgs();
                if (args != null && args.length > 0) {
                    String params = objectMapper.writeValueAsString(args[0]);
                    if (params.length() > 2000) {
                        params = params.substring(0, 2000) + "...";
                    }
                    operationLog.setRequestParams(params);
                }
            } catch (Exception e) {
                operationLog.setRequestParams("参数序列化失败");
            }
        }

        Object result;
        try {
            result = joinPoint.proceed();
            operationLog.setSuccess(true);
        } catch (Throwable throwable) {
            operationLog.setSuccess(false);
            operationLog.setErrorMessage(throwable.getMessage());
            throw throwable;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            operationLog.setExecutionTime(executionTime);
            operationLog.setOperationTime(LocalDateTime.now());

            try {
                operationLogRepository.save(operationLog);
            } catch (Exception e) {
                log.error("保存操作日志失败", e);
            }
        }

        return result;
    }

    private String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
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
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private String getCurrentUsername(HttpServletRequest request) {
        if (request == null) {
            return "anonymous";
        }
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = tokenService.getUsernameFromToken(token);
            return username != null ? username : "anonymous";
        }
        return "anonymous";
    }
}
