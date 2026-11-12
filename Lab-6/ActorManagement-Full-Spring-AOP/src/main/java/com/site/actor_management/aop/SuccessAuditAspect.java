package com.site.actor_management.aop;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Aspect @Component @Order(3)
public class SuccessAuditAspect {
 @AfterReturning(pointcut="com.site.actor_management.aop.ActorPointcuts.actorServiceMethods()",returning="result")
 public void afterReturning(JoinPoint jp,Object result){
  System.out.println("[AOP @AfterReturning] Success: "+jp.getSignature().toShortString());
  System.out.println("[AOP @AfterReturning] Result: "+result);
 }
}
