package com.jumkid.share.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static com.jumkid.share.util.Constants.JOURNEY_ID;

@Slf4j
public abstract class AbstractMethodLoggingConfig {

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
        //set custom field in logger
        MDC.put(JOURNEY_ID, journeyId);

        log.info("<<< Handshake >>> {}: {} | method is being called at {}.{} [{}]", JOURNEY_ID, journeyId, className, methodName, sb);
    }

}
