package com.orangelala.service.ucenter.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.orangelala.service.ucenter.controller.UcenterController;

@Component
@Aspect
public class TestAspect {
	
	@Pointcut("execution(* com.orangelala.service.ucenter.controller.*.*(..)) && @annotation(com.orangelala.service.ucenter.annotation.TestAnnotation)")
	public void pointCut() {}
	
	@Before("pointCut()")
	public void before(JoinPoint joinPoint) {
		//UcenterController controller = (UcenterController) joinPoint.getTarget();
		System.out.println("-------befor-------");
	}
}
