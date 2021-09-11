package org.prgrms.kdt.aop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // PointCut / target 범위 설정 (Advice가 적용될 범위)
    //@Around("org.prgrms.kdt.aop.CommonPointCut.repositoryInsertMethodPointCut()")
    @Around("@annotation(org.prgrms.kdt.aop.TrackTime)")
    // Advice 설정
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before method called -> {}", joinPoint.getSignature().toString());
        var startTime = System.nanoTime();
        var result = joinPoint.proceed();
        var endTime = System.nanoTime() - startTime;
        log.info("After method called -> {} and time taken {} nanoS", result, endTime);
        return result;
        // PointCut의 범위 내에서 모든 메소드(target)가 실행될때마다 Advice가 실행된다
        // joinPoint.proceed()는 해당 advice를 실행시킨 메소드(target)를 가리킨다
        // result는 해당 메소드(target)의 리턴값
    }



}
