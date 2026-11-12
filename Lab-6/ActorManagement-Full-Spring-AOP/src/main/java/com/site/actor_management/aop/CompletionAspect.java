package com.site.actor_management.aop;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Aspect @Component @Order(5)
public class CompletionAspect {
 @After("com.site.actor_management.aop.ActorPointcuts.actorServiceMethods()")
 public void after(JoinPoint jp){
  System.out.println("[AOP @After] Completed (success or failure): "+jp.getSignature().toShortString());
 }
}
