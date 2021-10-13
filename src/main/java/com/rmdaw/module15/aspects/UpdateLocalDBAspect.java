package com.rmdaw.module15.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.rmdaw.module15.data.DAOs.CommonDAO;


/**
 * Custom created aspect to save local database within aspect 
 * 
 * 
 * @author Rashed_Dawabsheh
 *
 */
@Component
@Aspect
public class UpdateLocalDBAspect {

	@Pointcut("@annotation(UpdateLocalDB)")
	
	public void doSave() {
		//
	}
	
	@Around(value="doSave()")
	public Object saveLocalDB(ProceedingJoinPoint prodJoinPoint) throws Throwable {
		Object returnValue = prodJoinPoint.proceed();	
		CommonDAO obj = (CommonDAO) prodJoinPoint.getTarget();		
		obj.saveLocalDB(); 
		
		return returnValue;
	}
	
}
