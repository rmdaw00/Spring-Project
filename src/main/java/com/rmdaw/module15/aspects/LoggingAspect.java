package com.rmdaw.module15.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Loggable.class);
	
	//Defining Pointcut
	@Pointcut("@annotation(Loggable)")
	public void doLog() {};
	
	@Around(value = "doLog()")
	public Object loggingAround(ProceedingJoinPoint pjp) throws Throwable {
		
		long startTime = System.currentTimeMillis();
		try {
			Object returnValue = pjp.proceed();
			long totalTime = System.currentTimeMillis()-startTime;
			
			LOGGER.info(pjp.getSignature().getName() + ": executed in " + totalTime +"ms");
			return returnValue;
		} catch (Throwable e) {
			
			LOGGER.info(pjp.getSignature().getName() + " Error: " + e.getMessage());
			throw e;
		}
		
	}
	
}
