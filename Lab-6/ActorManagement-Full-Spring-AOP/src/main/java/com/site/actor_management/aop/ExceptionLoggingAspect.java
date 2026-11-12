package com.site.actor_management.aop;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Aspect @Component @Order(2)
public class ExceptionLoggingAspect {
 @AfterThrowing(pointcut="com.site.actor_management.aop.ActorPointcuts.actorServiceMethods()",throwing="error")
 public void afterThrowing(JoinPoint jp,Throwable error){
  System.out.println("[AOP @AfterThrowing] "+jp.getSignature().toShortString()+" threw "+error);
 }
}
