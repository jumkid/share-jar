package com.jumkid.share.config.custom;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;

public class CustomPerformanceMonitorInterceptor extends PerformanceMonitorInterceptor {

    public CustomPerformanceMonitorInterceptor() {}

    public CustomPerformanceMonitorInterceptor(boolean useDynamicLogger) {
        setUseDynamicLogger(useDynamicLogger);
    }

    @Override
    protected Object invokeUnderTrace(MethodInvocation invocation, Log log) throws Throwable {
        String name = createInvocationTraceName(invocation);
        long start = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            log.trace("performance monitor | time spent: " + time + "ms | method: " + name);
        }
    }
}
