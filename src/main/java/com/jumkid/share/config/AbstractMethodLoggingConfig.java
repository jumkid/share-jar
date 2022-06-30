package com.jumkid.share.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
public abstract class AbstractMethodLoggingConfig {

    public static final String JOURNEY_ID = "Journey-ID";

    public abstract void monitor();

    public abstract void log4AllControllers(JoinPoint joinPoint);

    public void log(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        StringBuilder sb = new StringBuilder();
        String[] argNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i=0;i<argNames.length;i++) {
            sb.append(argNames[i]).append("=").append(args[i]).append(", ");
        }

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String journeyId = request.getHeader(JOURNEY_ID);
        MDC.put(JOURNEY_ID, journeyId);

        log.trace("<<< Handshake >>> {}:{} >> End point {}.{} [{}]", JOURNEY_ID, journeyId, className, methodName, sb);
    }

}
