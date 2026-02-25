package it.dst.garage.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GlobalPerformanceAspect {

    @Around("execution(* it.dst.garage.proxy..*(..))")
    public Object watch(ProceedingJoinPoint joinPoint) throws Throwable {
        String previousTime = MDC.get("startNano");
        MDC.put("startNano", String.valueOf(System.nanoTime()));

        try {
            return joinPoint.proceed();
        } finally {
            if (previousTime != null) {
                MDC.put("startNano", previousTime);
            } else {
                MDC.remove("startNano");
            }
        }
    }
}